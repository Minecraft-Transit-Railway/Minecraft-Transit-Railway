package org.mtr.mod.render;

import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Matrix4f;
import org.lwjgl.opengl.ARBInstancedArrays;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import org.mtr.libraries.it.unimi.dsi.fastutil.bytes.ByteArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.OptimizedModel;
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
import org.mtr.mod.resource.OptimizedModelWrapper;
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
	public static final VertexAttributeMapping DIAGNOSTIC_VERTEX_ATTRIBUTE_MAPPING = new VertexAttributeMapping.Builder()
			.set(VertexAttributeType.COLOR, VertexAttributeSource.GLOBAL)
			.set(VertexAttributeType.UV_OVERLAY, VertexAttributeSource.GLOBAL)
			.set(VertexAttributeType.UV_LIGHTMAP, VertexAttributeSource.GLOBAL)
			.set(VertexAttributeType.MATRIX_MODEL, VertexAttributeSource.GLOBAL)
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
	private static final MaterialProperties DEBUG_WHITE_CUTOUT_MATERIAL = new MaterialProperties(OptimizedModel.ShaderType.CUTOUT, OptimizedModelWrapper.WHITE_TEXTURE, null);
	private static final int RAIL_REFERENCE_COLOR = 0xA000FFFF;
	private static final int VEHICLE_REFERENCE_COLOR = 0xA0FFFF00;
	private static final int RAIL_STATIC_MATCHED_SAMPLE_COLOR = 0x8000FF80;
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
	@Nullable
	private GraphicsHolder frameGraphicsHolder;

	private GpuObjRenderer() {
	}

	public void beginFrame(Vector3d offset, GraphicsHolder graphicsHolder) {
		frameOffsetX = offset.getXMapped();
		frameOffsetY = offset.getYMapped();
		frameOffsetZ = offset.getZMapped();
		frameGraphicsHolder = graphicsHolder;
	}

	public void reloadShaders() {
		shaderManager.reloadShaders();
	}

	public Vector3d getFrameOffset() {
		return new Vector3d(frameOffsetX, frameOffsetY, frameOffsetZ);
	}

	public Matrix4f captureFrameMatrix(StoredMatrixTransformations storedMatrixTransformations, Vector3d offset) {
		return InstancingMatrixHelper.captureMatrix(frameGraphicsHolder, storedMatrixTransformations, offset);
	}

	@Nullable
	public GpuObjDebugStats.DiagnosticSample queue(ObjBatchKey batchKey, MaterialProperties materialProperties, StaticObjMesh staticObjMesh, Matrix4f drawMatrix, @Nullable Matrix4f diagnosticMatrix, int packedLight, int packedColor, boolean useDefaultOffset, GpuObjDebugStats.Source source) {
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
		final long startNanos = System.nanoTime();
		try {
			if (!shaderManager.isReady()) {
				shaderManager.reloadShaders();
			}

			for (final RenderStage renderStage : RenderStage.values()) {
				final ObjectArrayList<BatchEntry> batchEntries = activeOpaqueBatchesByStage[renderStage.ordinal()];
				for (int i = 0; i < batchEntries.size(); i++) {
					final BatchEntry batchEntry = batchEntries.get(i);
					final MaterialProperties activeMaterialProperties = GpuObjDebugStats.shouldForceWhiteCutout() ? DEBUG_WHITE_CUTOUT_MATERIAL : batchEntry.materialProperties;
					shaderManager.setupShaderBatchState(activeMaterialProperties);
					if (GpuObjDebugStats.shouldForceNoCull()) {
						RenderSystem.disableCull();
					}
					for (int j = 0; j < batchEntry.activeMeshes.size(); j++) {
						render(activeMaterialProperties, batchEntry.activeMeshes.get(j), offset);
					}
					if (GpuObjDebugStats.shouldForceNoCull()) {
						RenderSystem.enableCull();
					}
					shaderManager.cleanupShaderBatchState();
				}
			}
			renderRailDiagnosticReferences(GpuObjDebugStats.getCurrentRailDiagnosticSample());
			renderDiagnosticReference(GpuObjDebugStats.getCurrentVehicleDiagnosticSample(), VEHICLE_REFERENCE_COLOR);
		} finally {
			GpuObjDebugStats.recordRenderOpaqueNanos(System.nanoTime() - startNanos);
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

		uploadMeshInstances(meshEntry);
		final VertexArray vertexArray = meshEntry.getOrCreateVertexArray(materialProperties);
		vertexArray.bind();
		instanceBuffer.bind(GL33.GL_ARRAY_BUFFER);
		setupInstanceAttributePointers(0);
		DEFAULT_DRAW_STATE.apply();
		materialProperties.vertexAttributeState.apply();
		if (meshEntry.diagnosticSample != null) {
			meshEntry.diagnosticSample.setDrawVertexArrayState(vertexArray.materialProperties);
			GpuObjDebugStats.recordVaoAttributeState(meshEntry.diagnosticSample, describeVaoAttributeState());
		}
		GpuObjDebugStats.finalizeDiagnosticSample(meshEntry.diagnosticSample, offset, instanceCount);
		final long drawStartNanos = System.nanoTime();
		GL33.glDrawElementsInstanced(GL33.GL_TRIANGLES, meshEntry.staticObjMesh.vertexArray.indexBuffer.getVertexCount(), meshEntry.staticObjMesh.vertexArray.indexBuffer.indexType, 0, instanceCount);
		GpuObjDebugStats.recordInstanceDrawNanos(System.nanoTime() - drawStartNanos);
		GpuObjDebugStats.recordDrawInstanced();
	}

	private void uploadMeshInstances(MeshEntry meshEntry) {
		final int payloadSize = meshEntry.payload.size();
		if (payloadSize == 0) {
			return;
		}

		ensureCapacity(payloadSize);
		byteBuffer.clear();
		byteBuffer.put(meshEntry.payload.elements(), 0, payloadSize);
		byteBuffer.flip();
		instanceBuffer.bind(GL33.GL_ARRAY_BUFFER);
		final long uploadStartNanos = System.nanoTime();
		instanceBuffer.upload(byteBuffer, payloadSize, VertexBuffer.USAGE_STREAM_DRAW);
		GpuObjDebugStats.recordInstanceUploadNanos(System.nanoTime() - uploadStartNanos);
	}

	private void renderDiagnosticReference(@Nullable GpuObjDebugStats.DiagnosticSample diagnosticSample, int color) {
		if (diagnosticSample == null || !diagnosticSample.isDrawn()) {
			return;
		}

		final Matrix4f referenceMatrix = diagnosticSample.getPreparedDrawMatrix();
		diagnosticSample.setSingleDrawReferenceMatrix(referenceMatrix);
		final MaterialProperties referenceMaterial = new MaterialProperties(diagnosticSample.getShaderTypeEnum(), OptimizedModelWrapper.WHITE_TEXTURE, null);
		final VertexArray diagnosticVertexArray = diagnosticSample.getStaticObjMesh().getDiagnosticVertexArray(referenceMaterial);
		final VertexAttributeState referenceState = new VertexAttributeState(color, GraphicsHolder.getDefaultLight(), new org.mtr.mapping.holder.Matrix4f(new org.joml.Matrix4f(referenceMatrix)));
		shaderManager.setupShaderBatchState(referenceMaterial);
		RenderSystem.disableCull();
		RenderSystem.disableDepthTest();
		diagnosticVertexArray.bind();
		referenceState.apply();
		diagnosticVertexArray.materialProperties.vertexAttributeState.apply();
		diagnosticVertexArray.draw();
		RenderSystem.enableDepthTest();
		RenderSystem.enableCull();
		shaderManager.cleanupShaderBatchState();
	}

	private void renderRailDiagnosticReferences(@Nullable GpuObjDebugStats.DiagnosticSample diagnosticSample) {
		if (diagnosticSample == null || !diagnosticSample.isDrawn()) {
			return;
		}

		final GpuObjDebugStats.RailViewMode railViewMode = GpuObjDebugStats.getRailViewMode();
		try {
			switch (railViewMode) {
				case INSTANCED:
					break;
				case STATIC_MATCHED:
					renderStaticMatchedReference(diagnosticSample, RAIL_STATIC_MATCHED_SAMPLE_COLOR);
					break;
				case NORMAL:
					break;
				case ALL:
				default:
					renderDiagnosticReference(diagnosticSample, RAIL_REFERENCE_COLOR);
					renderStaticMatchedReference(diagnosticSample, RAIL_STATIC_MATCHED_SAMPLE_COLOR);
					break;
			}
		} catch (RuntimeException e) {
			Init.LOGGER.error(
					"[MTR Debug] Rail diagnostic render failed for railView={} sample={} shader={} texture={}",
					railViewMode.label,
					diagnosticSample.getSourceSampleId(),
					diagnosticSample.getShaderType(),
					diagnosticSample.getTextureId(),
					e
			);
			GpuObjDebugStats.setRailViewMode(GpuObjDebugStats.RailViewMode.NORMAL);
		}
	}

	private void renderStaticMatchedReference(GpuObjDebugStats.DiagnosticSample diagnosticSample, int packedColor) {
		final Matrix4f referenceMatrix = diagnosticSample.getPreparedDrawMatrix();
		diagnosticSample.setStaticMatchedReferenceMatrix(referenceMatrix);
		final MaterialProperties referenceMaterial = GpuObjDebugStats.shouldForceWhiteCutout() ? DEBUG_WHITE_CUTOUT_MATERIAL : diagnosticSample.createMatchedMaterialProperties();
		final VertexArray diagnosticVertexArray = diagnosticSample.getStaticObjMesh().getDiagnosticVertexArray(referenceMaterial);
		final VertexAttributeState referenceState = new VertexAttributeState(packedColor, GraphicsHolder.getDefaultLight(), new org.mtr.mapping.holder.Matrix4f(new org.joml.Matrix4f(referenceMatrix)));
		shaderManager.setupShaderBatchState(referenceMaterial);
		if (GpuObjDebugStats.shouldForceNoCull()) {
			RenderSystem.disableCull();
		}
		diagnosticVertexArray.bind();
		referenceState.apply();
		diagnosticVertexArray.materialProperties.vertexAttributeState.apply();
		diagnosticVertexArray.draw();
		if (GpuObjDebugStats.shouldForceNoCull()) {
			RenderSystem.enableCull();
		}
		shaderManager.cleanupShaderBatchState();
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

	private static String describeVaoAttributeState() {
		return String.format(
				"vao=%d arrayBuffer=%d | %s | %s | %s | %s | %s | %s",
				GL33.glGetInteger(GL33.GL_VERTEX_ARRAY_BINDING),
				GL33.glGetInteger(GL33.GL_ARRAY_BUFFER_BINDING),
				describeAttribute("COLOR", VertexAttributeType.COLOR.location),
				describeAttribute("LIGHT", VertexAttributeType.UV_LIGHTMAP.location),
				describeAttribute("MATRIX0", VertexAttributeType.MATRIX_MODEL.location),
				describeAttribute("MATRIX1", VertexAttributeType.MATRIX_MODEL.location + 1),
				describeAttribute("MATRIX2", VertexAttributeType.MATRIX_MODEL.location + 2),
				describeAttribute("MATRIX3", VertexAttributeType.MATRIX_MODEL.location + 3)
		);
	}

	private static String describeAttribute(String label, int location) {
		return String.format(
				"%s loc=%d enabled=%s stride=%d pointer=%d divisor=%d buffer=%d",
				label,
				location,
				GL33.glGetVertexAttribi(location, GL33.GL_VERTEX_ATTRIB_ARRAY_ENABLED) != 0,
				GL33.glGetVertexAttribi(location, GL33.GL_VERTEX_ATTRIB_ARRAY_STRIDE),
				GL33.glGetVertexAttribPointer(location, GL33.GL_VERTEX_ATTRIB_ARRAY_POINTER),
				GL33.glGetVertexAttribi(location, GL33.GL_VERTEX_ATTRIB_ARRAY_DIVISOR),
				GL33.glGetVertexAttribi(location, GL33.GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING)
		);
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
		private boolean activeThisFrame;

		private BatchEntry(MaterialProperties materialProperties) {
			this.materialProperties = materialProperties;
		}

		private void clear() {
			for (int i = 0; i < activeMeshes.size(); i++) {
				activeMeshes.get(i).clear();
			}
			activeMeshes.clear();
		}
	}

	private static final class MeshEntry {

		private final StaticObjMesh staticObjMesh;
		private final ByteArrayList payload = new ByteArrayList();
		private VertexArray vertexArray;
		private GpuObjDebugStats.DiagnosticSample diagnosticSample;
		private int instanceCount;

		private MeshEntry(StaticObjMesh staticObjMesh) {
			this.staticObjMesh = staticObjMesh;
		}

		private void clear() {
			diagnosticSample = null;
			payload.clear();
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
}
