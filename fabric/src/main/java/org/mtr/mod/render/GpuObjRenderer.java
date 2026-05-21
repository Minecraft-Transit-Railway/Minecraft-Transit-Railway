package org.mtr.mod.render;

import org.joml.Matrix4f;
import org.lwjgl.opengl.ARBInstancedArrays;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import org.mtr.libraries.it.unimi.dsi.fastutil.bytes.ByteArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.render.batch.MaterialProperties;
import org.mtr.mapping.render.object.VertexArray;
import org.mtr.mapping.render.object.VertexBuffer;
import org.mtr.mapping.render.shader.ShaderManager;
import org.mtr.mapping.render.vertex.VertexAttributeMapping;
import org.mtr.mapping.render.vertex.VertexAttributeState;
import org.mtr.mapping.render.vertex.VertexAttributeSource;
import org.mtr.mapping.render.vertex.VertexAttributeType;
import org.mtr.mod.data.IGui;
import org.mtr.mod.Init;
import org.mtr.mod.resource.RenderStage;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class GpuObjRenderer implements IGui {

	public static final GpuObjRenderer INSTANCE = new GpuObjRenderer();

	public static final VertexAttributeMapping VERTEX_ATTRIBUTE_MAPPING = new VertexAttributeMapping.Builder()
			.set(VertexAttributeType.COLOR, VertexAttributeSource.INSTANCE_BUFFER)
			.set(VertexAttributeType.UV_OVERLAY, VertexAttributeSource.GLOBAL)
			.set(VertexAttributeType.UV_LIGHTMAP, VertexAttributeSource.INSTANCE_BUFFER)
			.set(VertexAttributeType.MATRIX_MODEL, VertexAttributeSource.INSTANCE_BUFFER)
			.build();

	static {
		Init.LOGGER.info(
				"GPU instance mapping: stride={}, colorPtr={}, lightPtr={}, matrixPtr={}",
				VERTEX_ATTRIBUTE_MAPPING.strideInstance,
				VERTEX_ATTRIBUTE_MAPPING.pointers.get(VertexAttributeType.COLOR),
				VERTEX_ATTRIBUTE_MAPPING.pointers.get(VertexAttributeType.UV_LIGHTMAP),
				VERTEX_ATTRIBUTE_MAPPING.pointers.get(VertexAttributeType.MATRIX_MODEL)
		);
	}

	private static final int MATRIX_FLOATS = 16;
	private static final int MATRIX_BYTES = MATRIX_FLOATS * Float.BYTES;
	private static final int INSTANCE_STRIDE = MATRIX_BYTES + Integer.BYTES + Integer.BYTES;
	private static final VertexAttributeState DEFAULT_DRAW_STATE = new VertexAttributeState(ARGB_WHITE, GraphicsHolder.getDefaultLight(), org.mtr.mapping.render.tool.Utilities.create());
	private static boolean loggedMissingInstanceDivisorSupport;

	private final ShaderManager shaderManager = new ShaderManager();
	private final VertexBuffer instanceBuffer = new VertexBuffer();
	private final Object2ObjectOpenHashMap<ObjBatchKey, BatchEntry> batches = new Object2ObjectOpenHashMap<>();
	private final ObjectArrayList<BatchEntry> activeBatches = new ObjectArrayList<>();
	private final ObjectArrayList<BatchEntry>[] activeOpaqueBatchesByStage = createBatchLists();
	private final byte[] scratchInstanceData = new byte[INSTANCE_STRIDE];
	private final ByteBuffer scratchInstanceBuffer = ByteBuffer.wrap(scratchInstanceData).order(ByteOrder.nativeOrder());
	private ByteBuffer byteBuffer = createByteBuffer(INSTANCE_STRIDE);
	private double frameOffsetX;
	private double frameOffsetY;
	private double frameOffsetZ;
	private boolean frameActive;
	@Nullable
	private GraphicsHolder frameGraphicsHolder;

	private GpuObjRenderer() {
	}

	public void beginFrame(Vector3d offset, GraphicsHolder graphicsHolder) {
		frameOffsetX = offset.getXMapped();
		frameOffsetY = offset.getYMapped();
		frameOffsetZ = offset.getZMapped();
		frameActive = true;
		frameGraphicsHolder = graphicsHolder;
	}

	public void reloadShaders() {
		shaderManager.reloadShaders();
	}

	public void reload() {
		clear();
		batches.clear();
		shaderManager.reloadShaders();
	}

	public Vector3d getFrameOffset() {
		return new Vector3d(frameOffsetX, frameOffsetY, frameOffsetZ);
	}

	public Matrix4f captureFrameMatrix(StoredMatrixTransformations storedMatrixTransformations, Vector3d offset) {
		return InstancingMatrixHelper.captureMatrix(frameGraphicsHolder, storedMatrixTransformations, offset);
	}

	public boolean isFrameActive() {
		return frameActive && frameGraphicsHolder != null;
	}

	@Nullable
	public GpuObjDebugStats.DiagnosticSample queue(ObjBatchKey batchKey, MaterialProperties materialProperties, StaticObjMesh staticObjMesh, Matrix4f drawMatrix, @Nullable Matrix4f diagnosticMatrix, int packedLight, int packedColor, boolean useDefaultOffset, GpuObjDebugStats.Source source) {
		return beginQueue(batchKey, materialProperties, staticObjMesh, packedLight, packedColor, source).queue(drawMatrix, diagnosticMatrix, useDefaultOffset);
	}

	public QueuedMesh beginQueue(ObjBatchKey batchKey, MaterialProperties materialProperties, StaticObjMesh staticObjMesh, int packedLight, int packedColor, GpuObjDebugStats.Source source) {
		final BatchEntry batchEntry = batches.computeIfAbsent(batchKey, key -> new BatchEntry(materialProperties));
		final boolean newBatch = !batchEntry.activeThisFrame;
		if (!batchEntry.activeThisFrame) {
			batchEntry.activeThisFrame = true;
			activeBatches.add(batchEntry);
			if (!batchKey.translucent) {
				activeOpaqueBatchesByStage[batchKey.renderStage.ordinal()].add(batchEntry);
			}
		}

		final MeshEntry meshEntry = batchEntry.meshes.computeIfAbsent(staticObjMesh, MeshEntry::new);
		final boolean newMesh = meshEntry.instanceCount == 0;
		if (meshEntry.instanceCount == 0) {
			batchEntry.activeMeshes.add(meshEntry);
		}

		final int effectivePackedColor = packedColor == ARGB_WHITE ? staticObjMesh.materialColor : packedColor;
		final int effectivePackedLight = materialProperties.vertexAttributeState.lightmapUV == null ? packedLight : materialProperties.vertexAttributeState.lightmapUV;
		return new QueuedMesh(source, batchKey, staticObjMesh, meshEntry, newBatch, newMesh, effectivePackedColor, effectivePackedLight, materialProperties);
	}

	private GpuObjDebugStats.DiagnosticSample queue(MeshEntry meshEntry, ObjBatchKey batchKey, StaticObjMesh staticObjMesh, Matrix4f drawMatrix, @Nullable Matrix4f diagnosticMatrix, int effectivePackedLight, int effectivePackedColor, boolean useDefaultOffset, GpuObjDebugStats.Source source, boolean newBatch, boolean newMesh, MaterialProperties materialProperties) {
		scratchInstanceBuffer.clear();
		putPackedColor(effectivePackedColor);
		putPackedLight(effectivePackedLight);
		scratchInstanceBuffer.putFloat(drawMatrix.m00());
		scratchInstanceBuffer.putFloat(drawMatrix.m01());
		scratchInstanceBuffer.putFloat(drawMatrix.m02());
		scratchInstanceBuffer.putFloat(drawMatrix.m03());
		scratchInstanceBuffer.putFloat(drawMatrix.m10());
		scratchInstanceBuffer.putFloat(drawMatrix.m11());
		scratchInstanceBuffer.putFloat(drawMatrix.m12());
		scratchInstanceBuffer.putFloat(drawMatrix.m13());
		scratchInstanceBuffer.putFloat(drawMatrix.m20());
		scratchInstanceBuffer.putFloat(drawMatrix.m21());
		scratchInstanceBuffer.putFloat(drawMatrix.m22());
		scratchInstanceBuffer.putFloat(drawMatrix.m23());
		scratchInstanceBuffer.putFloat(drawMatrix.m30());
		scratchInstanceBuffer.putFloat(drawMatrix.m31());
		scratchInstanceBuffer.putFloat(drawMatrix.m32());
		scratchInstanceBuffer.putFloat(drawMatrix.m33());
		final GpuObjDebugStats.DiagnosticSample diagnosticSample = GpuObjDebugStats.captureDiagnosticSample(source, batchKey, staticObjMesh, diagnosticMatrix == null ? drawMatrix : diagnosticMatrix, useDefaultOffset);
		if (diagnosticSample != null) {
			diagnosticSample.setInstanceColor(effectivePackedColor);
			diagnosticSample.setInstanceLight(effectivePackedLight);
			diagnosticSample.setMaterialState(materialProperties, staticObjMesh);
			diagnosticSample.setPreparedDrawMatrix(drawMatrix);
			meshEntry.diagnosticSample = diagnosticSample;
		}
		meshEntry.addInstance(scratchInstanceData);
		GpuObjDebugStats.recordInstanceQueued(source, newBatch, newMesh);
		return diagnosticSample;
	}

	private void putPackedColor(int packedColor) {
		scratchInstanceBuffer.put((byte) ((packedColor >>> 24) & 0xFF));
		scratchInstanceBuffer.put((byte) ((packedColor >>> 16) & 0xFF));
		scratchInstanceBuffer.put((byte) ((packedColor >>> 8) & 0xFF));
		scratchInstanceBuffer.put((byte) (packedColor & 0xFF));
	}

	private void putPackedLight(int packedLight) {
		scratchInstanceBuffer.putShort((short) (packedLight >>> 16));
		scratchInstanceBuffer.putShort((short) packedLight);
	}

	public void renderOpaque(Vector3d offset) {
		final boolean collectTimings = GpuObjDebugStats.shouldCollectTimings();
		final long startNanos = collectTimings ? System.nanoTime() : 0;
		try {
			if (!shaderManager.isReady()) {
				shaderManager.reloadShaders();
			}

			for (final RenderStage renderStage : RenderStage.values()) {
				final ObjectArrayList<BatchEntry> batchEntries = activeOpaqueBatchesByStage[renderStage.ordinal()];
				for (int i = 0; i < batchEntries.size(); i++) {
					final BatchEntry batchEntry = batchEntries.get(i);
					final MaterialProperties activeMaterialProperties = batchEntry.materialProperties;
					shaderManager.setupShaderBatchState(activeMaterialProperties);
					uploadBatchInstances(batchEntry);
					for (int j = 0; j < batchEntry.activeMeshes.size(); j++) {
						render(activeMaterialProperties, batchEntry.activeMeshes.get(j), offset);
					}
					shaderManager.cleanupShaderBatchState();
				}
			}
		} finally {
			if (collectTimings) {
				GpuObjDebugStats.recordRenderOpaqueNanos(System.nanoTime() - startNanos);
			}
		}
	}

	public void clear() {
		for (int i = 0; i < activeBatches.size(); i++) {
			final BatchEntry batchEntry = activeBatches.get(i);
			batchEntry.clear();
			batchEntry.activeThisFrame = false;
		}
		activeBatches.clear();
		for (final ObjectArrayList<BatchEntry> batchEntries : activeOpaqueBatchesByStage) {
			batchEntries.clear();
		}
		frameActive = false;
		frameGraphicsHolder = null;
	}

	public static void setupInstanceAttributes(VertexArray vertexArray) {
		vertexArray.bind();
		INSTANCE.instanceBuffer.bind(GL33.GL_ARRAY_BUFFER);
		setupInstanceAttribute(VertexAttributeType.COLOR);
		setupInstanceAttribute(VertexAttributeType.UV_LIGHTMAP);
		setupInstanceAttribute(VertexAttributeType.MATRIX_MODEL);
		VertexArray.unbind();
	}

	private void render(MaterialProperties materialProperties, MeshEntry meshEntry, Vector3d offset) {
		final int instanceCount = meshEntry.instanceCount;
		if (instanceCount == 0) {
			return;
		}

		final VertexArray vertexArray = meshEntry.getOrCreateVertexArray(materialProperties);
		vertexArray.bind();
		instanceBuffer.bind(GL33.GL_ARRAY_BUFFER);
		setupInstanceAttributePointers(meshEntry.instanceOffsetBytes);
		DEFAULT_DRAW_STATE.apply();
		materialProperties.vertexAttributeState.apply();
		if (meshEntry.diagnosticSample != null) {
			meshEntry.diagnosticSample.setDrawVertexArrayState(vertexArray.materialProperties);
		}
		GpuObjDebugStats.finalizeDiagnosticSample(meshEntry.diagnosticSample, offset, instanceCount);
		final boolean collectTimings = GpuObjDebugStats.shouldCollectTimings();
		final long drawStartNanos = collectTimings ? System.nanoTime() : 0;
		GL33.glDrawElementsInstanced(GL33.GL_TRIANGLES, meshEntry.staticObjMesh.vertexArray.indexBuffer.getVertexCount(), meshEntry.staticObjMesh.vertexArray.indexBuffer.indexType, 0, instanceCount);
		if (collectTimings) {
			GpuObjDebugStats.recordInstanceDrawNanos(System.nanoTime() - drawStartNanos);
		}
		GpuObjDebugStats.recordDrawInstanced();
	}

	private void uploadBatchInstances(BatchEntry batchEntry) {
		batchEntry.payload.clear();
		for (int i = 0; i < batchEntry.activeMeshes.size(); i++) {
			final MeshEntry meshEntry = batchEntry.activeMeshes.get(i);
			meshEntry.instanceOffsetBytes = batchEntry.payload.size();
			batchEntry.payload.addElements(batchEntry.payload.size(), meshEntry.payload.elements(), 0, meshEntry.payload.size());
		}

		final int payloadSize = batchEntry.payload.size();
		if (payloadSize == 0) {
			return;
		}

		ensureCapacity(payloadSize);
		byteBuffer.clear();
		byteBuffer.put(batchEntry.payload.elements(), 0, payloadSize);
		byteBuffer.flip();
		instanceBuffer.bind(GL33.GL_ARRAY_BUFFER);
		final boolean collectTimings = GpuObjDebugStats.shouldCollectTimings();
		final long uploadStartNanos = collectTimings ? System.nanoTime() : 0;
		instanceBuffer.upload(byteBuffer, payloadSize, VertexBuffer.USAGE_STREAM_DRAW);
		if (collectTimings) {
			GpuObjDebugStats.recordInstanceUploadNanos(System.nanoTime() - uploadStartNanos);
		}
	}

	private void ensureCapacity(int requiredBytes) {
		final int minimumCapacity = Math.max(INSTANCE_STRIDE, requiredBytes);
		if (byteBuffer.capacity() < requiredBytes) {
			byteBuffer = createByteBuffer(minimumCapacity);
		}
	}

	private static ByteBuffer createByteBuffer(int capacity) {
		return ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
	}

	private static void setupInstanceAttribute(VertexAttributeType vertexAttributeType) {
		vertexAttributeType.toggleAttributeArray(true);
		vertexAttributeType.setupAttributePointer(VERTEX_ATTRIBUTE_MAPPING.strideInstance, VERTEX_ATTRIBUTE_MAPPING.pointers.get(vertexAttributeType));
		applyInstanceAttributeDivisor(vertexAttributeType);
	}

	private static void setupInstanceAttributePointers(int instanceOffsetBytes) {
		setupInstanceAttributePointer(VertexAttributeType.COLOR, instanceOffsetBytes);
		setupInstanceAttributePointer(VertexAttributeType.UV_LIGHTMAP, instanceOffsetBytes);
		setupInstanceAttributePointer(VertexAttributeType.MATRIX_MODEL, instanceOffsetBytes);
	}

	private static void setupInstanceAttributePointer(VertexAttributeType vertexAttributeType, int instanceOffsetBytes) {
		vertexAttributeType.setupAttributePointer(VERTEX_ATTRIBUTE_MAPPING.strideInstance, instanceOffsetBytes + VERTEX_ATTRIBUTE_MAPPING.pointers.get(vertexAttributeType));
	}

	private static void applyInstanceAttributeDivisor(VertexAttributeType vertexAttributeType) {
		for (int i = 0; i < vertexAttributeType.span; i++) {
			applyInstanceAttributeDivisor(vertexAttributeType.location + i);
		}
	}

	private static void applyInstanceAttributeDivisor(int location) {
		try {
			final org.lwjgl.opengl.GLCapabilities capabilities = GL.getCapabilities();
			if (capabilities.OpenGL33) {
				GL33.glVertexAttribDivisor(location, 1);
			} else if (capabilities.GL_ARB_instanced_arrays) {
				ARBInstancedArrays.glVertexAttribDivisorARB(location, 1);
			} else if (!loggedMissingInstanceDivisorSupport) {
				loggedMissingInstanceDivisorSupport = true;
				Init.LOGGER.warn("[MTR Debug] GPU instancing is missing glVertexAttribDivisor support; instance attributes will not advance per instance.");
			}
		} catch (IllegalStateException e) {
			if (!loggedMissingInstanceDivisorSupport) {
				loggedMissingInstanceDivisorSupport = true;
				Init.LOGGER.warn("[MTR Debug] GPU instancing could not query GL capabilities for instance divisors.", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static ObjectArrayList<BatchEntry>[] createBatchLists() {
		final ObjectArrayList<BatchEntry>[] batchLists = new ObjectArrayList[RenderStage.values().length];
		for (int i = 0; i < batchLists.length; i++) {
			batchLists[i] = new ObjectArrayList<>();
		}
		return batchLists;
	}

	private static final class BatchEntry {

		private final MaterialProperties materialProperties;
		private final Object2ObjectOpenHashMap<StaticObjMesh, MeshEntry> meshes = new Object2ObjectOpenHashMap<>();
		private final ObjectArrayList<MeshEntry> activeMeshes = new ObjectArrayList<>();
		private final ByteArrayList payload = new ByteArrayList();
		private boolean activeThisFrame;

		private BatchEntry(MaterialProperties materialProperties) {
			this.materialProperties = materialProperties;
		}

		private void clear() {
			for (int i = 0; i < activeMeshes.size(); i++) {
				activeMeshes.get(i).clear();
			}
			activeMeshes.clear();
			payload.clear();
		}
	}

	private static final class MeshEntry {

		private final StaticObjMesh staticObjMesh;
		private final ByteArrayList payload = new ByteArrayList();
		private VertexArray vertexArray;
		private GpuObjDebugStats.DiagnosticSample diagnosticSample;
		private int instanceOffsetBytes;
		private int instanceCount;

		private MeshEntry(StaticObjMesh staticObjMesh) {
			this.staticObjMesh = staticObjMesh;
		}

		private void clear() {
			diagnosticSample = null;
			payload.clear();
			instanceOffsetBytes = 0;
			instanceCount = 0;
		}

		private void addInstance(byte[] instanceData) {
			payload.addElements(payload.size(), instanceData, 0, INSTANCE_STRIDE);
			instanceCount++;
		}

		private VertexArray getOrCreateVertexArray(MaterialProperties materialProperties) {
			if (vertexArray == null) {
				vertexArray = staticObjMesh.createVertexArray(materialProperties);
			}
			return vertexArray;
		}
	}

	public final class QueuedMesh {

		private final GpuObjDebugStats.Source source;
		private final ObjBatchKey batchKey;
		private final StaticObjMesh staticObjMesh;
		private final MeshEntry meshEntry;
		private final boolean newBatch;
		private final boolean newMesh;
		private final int effectivePackedColor;
		private final int effectivePackedLight;
		private final MaterialProperties materialProperties;
		private boolean recordedActiveState;

		private QueuedMesh(GpuObjDebugStats.Source source, ObjBatchKey batchKey, StaticObjMesh staticObjMesh, MeshEntry meshEntry, boolean newBatch, boolean newMesh, int effectivePackedColor, int effectivePackedLight, MaterialProperties materialProperties) {
			this.source = source;
			this.batchKey = batchKey;
			this.staticObjMesh = staticObjMesh;
			this.meshEntry = meshEntry;
			this.newBatch = newBatch;
			this.newMesh = newMesh;
			this.effectivePackedColor = effectivePackedColor;
			this.effectivePackedLight = effectivePackedLight;
			this.materialProperties = materialProperties;
		}

		@Nullable
		public GpuObjDebugStats.DiagnosticSample queue(Matrix4f drawMatrix, @Nullable Matrix4f diagnosticMatrix, boolean useDefaultOffset) {
			final boolean recordNewBatch = !recordedActiveState && newBatch;
			final boolean recordNewMesh = !recordedActiveState && newMesh;
			recordedActiveState = true;
			return GpuObjRenderer.this.queue(meshEntry, batchKey, staticObjMesh, drawMatrix, diagnosticMatrix, effectivePackedLight, effectivePackedColor, useDefaultOffset, source, recordNewBatch, recordNewMesh, materialProperties);
		}
	}
}
