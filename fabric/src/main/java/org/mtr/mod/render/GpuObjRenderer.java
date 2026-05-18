package org.mtr.mod.render;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33;
import org.mtr.libraries.it.unimi.dsi.fastutil.bytes.ByteArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntArrayList;
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
import org.mtr.mod.resource.RenderStage;

import java.nio.ByteBuffer;

public final class GpuObjRenderer implements IGui {

	public static final GpuObjRenderer INSTANCE = new GpuObjRenderer();

	public static final VertexAttributeMapping VERTEX_ATTRIBUTE_MAPPING = new VertexAttributeMapping.Builder()
			.set(VertexAttributeType.COLOR, VertexAttributeSource.INSTANCE_BUFFER)
			.set(VertexAttributeType.UV_OVERLAY, VertexAttributeSource.GLOBAL)
			.set(VertexAttributeType.UV_LIGHTMAP, VertexAttributeSource.INSTANCE_BUFFER)
			.set(VertexAttributeType.MATRIX_MODEL, VertexAttributeSource.INSTANCE_BUFFER)
			.build();

	private static final int MATRIX_FLOATS = 16;
	private static final int MATRIX_BYTES = MATRIX_FLOATS * Float.BYTES;
	private static final int INSTANCE_STRIDE = MATRIX_BYTES + Integer.BYTES + Integer.BYTES;
	private static final int TRANSLATION_X_BYTE_OFFSET = 12 * Float.BYTES;
	private static final int TRANSLATION_Y_BYTE_OFFSET = 13 * Float.BYTES;
	private static final int TRANSLATION_Z_BYTE_OFFSET = 14 * Float.BYTES;
	private static final VertexAttributeState DEFAULT_DRAW_STATE = new VertexAttributeState(ARGB_WHITE, GraphicsHolder.getDefaultLight(), org.mtr.mapping.render.tool.Utilities.create());

	private final ShaderManager shaderManager = new ShaderManager();
	private final VertexBuffer instanceBuffer = new VertexBuffer();
	private final Object2ObjectOpenHashMap<ObjBatchKey, BatchEntry> batches = new Object2ObjectOpenHashMap<>();
	private final ObjectArrayList<BatchEntry> activeBatches = new ObjectArrayList<>();
	private final ObjectArrayList<BatchEntry>[] activeOpaqueBatchesByStage = createBatchLists();
	private final byte[] scratchInstanceData = new byte[INSTANCE_STRIDE];
	private final ByteBuffer scratchInstanceBuffer = ByteBuffer.wrap(scratchInstanceData);
	private ByteBuffer byteBuffer = ByteBuffer.allocateDirect(INSTANCE_STRIDE);

	public void reloadShaders() {
		shaderManager.reloadShaders();
	}

	public void queue(ObjBatchKey batchKey, MaterialProperties materialProperties, StaticObjMesh staticObjMesh, Matrix4f matrix, int packedLight, int packedColor, boolean useDefaultOffset, GpuObjDebugStats.Source source) {
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

		scratchInstanceBuffer.clear();
		scratchInstanceBuffer.putFloat(matrix.m00());
		scratchInstanceBuffer.putFloat(matrix.m01());
		scratchInstanceBuffer.putFloat(matrix.m02());
		scratchInstanceBuffer.putFloat(matrix.m03());
		scratchInstanceBuffer.putFloat(matrix.m10());
		scratchInstanceBuffer.putFloat(matrix.m11());
		scratchInstanceBuffer.putFloat(matrix.m12());
		scratchInstanceBuffer.putFloat(matrix.m13());
		scratchInstanceBuffer.putFloat(matrix.m20());
		scratchInstanceBuffer.putFloat(matrix.m21());
		scratchInstanceBuffer.putFloat(matrix.m22());
		scratchInstanceBuffer.putFloat(matrix.m23());
		scratchInstanceBuffer.putFloat(matrix.m30());
		scratchInstanceBuffer.putFloat(matrix.m31());
		scratchInstanceBuffer.putFloat(matrix.m32());
		scratchInstanceBuffer.putFloat(matrix.m33());
		scratchInstanceBuffer.putInt(packedColor);
		scratchInstanceBuffer.putInt(packedLight);
		meshEntry.addInstance(scratchInstanceData, useDefaultOffset);
		GpuObjDebugStats.recordInstanceQueued(source, newBatch, newMesh);
	}

	public void renderOpaque(Vector3d offset) {
		if (!shaderManager.isReady()) {
			shaderManager.reloadShaders();
		}

		for (final RenderStage renderStage : RenderStage.values()) {
			final ObjectArrayList<BatchEntry> batchEntries = activeOpaqueBatchesByStage[renderStage.ordinal()];
			for (int i = 0; i < batchEntries.size(); i++) {
				final BatchEntry batchEntry = batchEntries.get(i);
				shaderManager.setupShaderBatchState(batchEntry.materialProperties);
				for (int j = 0; j < batchEntry.activeMeshes.size(); j++) {
					render(batchEntry.materialProperties, batchEntry.activeMeshes.get(j), offset);
				}
				shaderManager.cleanupShaderBatchState();
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

		ensureCapacity(meshEntry.payload.size());
		byteBuffer.clear();
		byteBuffer.put(meshEntry.payload.elements(), 0, meshEntry.payload.size());

		for (int i = 0; i < meshEntry.defaultOffsetInstanceIndices.size(); i++) {
			final int baseByteOffset = meshEntry.defaultOffsetInstanceIndices.getInt(i) * INSTANCE_STRIDE;
			byteBuffer.putFloat(baseByteOffset + TRANSLATION_X_BYTE_OFFSET, byteBuffer.getFloat(baseByteOffset + TRANSLATION_X_BYTE_OFFSET) - (float) offset.getXMapped());
			byteBuffer.putFloat(baseByteOffset + TRANSLATION_Y_BYTE_OFFSET, byteBuffer.getFloat(baseByteOffset + TRANSLATION_Y_BYTE_OFFSET) - (float) offset.getYMapped());
			byteBuffer.putFloat(baseByteOffset + TRANSLATION_Z_BYTE_OFFSET, byteBuffer.getFloat(baseByteOffset + TRANSLATION_Z_BYTE_OFFSET) - (float) offset.getZMapped());
		}

		byteBuffer.flip();
		meshEntry.staticObjMesh.vertexArray.bind();
		instanceBuffer.bind(GL33.GL_ARRAY_BUFFER);
		instanceBuffer.upload(byteBuffer, VertexBuffer.USAGE_STREAM_DRAW);
		DEFAULT_DRAW_STATE.apply();
		meshEntry.staticObjMesh.vertexArray.materialProperties.vertexAttributeState.apply();
		materialProperties.vertexAttributeState.apply();
		GL33.glDrawElementsInstanced(GL33.GL_TRIANGLES, meshEntry.staticObjMesh.vertexArray.indexBuffer.getVertexCount(), meshEntry.staticObjMesh.vertexArray.indexBuffer.indexType, 0, instanceCount);
		GpuObjDebugStats.recordDrawInstanced();
	}

	private void ensureCapacity(int requiredBytes) {
		final int minimumCapacity = Math.max(INSTANCE_STRIDE, requiredBytes);
		if (byteBuffer.capacity() < requiredBytes) {
			byteBuffer = ByteBuffer.allocateDirect(minimumCapacity);
		}
	}

	private static void setupInstanceAttribute(VertexAttributeType vertexAttributeType) {
		vertexAttributeType.toggleAttributeArray(true);
		vertexAttributeType.setupAttributePointer(VERTEX_ATTRIBUTE_MAPPING.strideInstance, VERTEX_ATTRIBUTE_MAPPING.pointers.get(vertexAttributeType));
		vertexAttributeType.setAttributeDivisor(1);
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
		private final IntArrayList defaultOffsetInstanceIndices = new IntArrayList();
		private int instanceCount;

		private MeshEntry(StaticObjMesh staticObjMesh) {
			this.staticObjMesh = staticObjMesh;
		}

		private void clear() {
			payload.clear();
			defaultOffsetInstanceIndices.clear();
			instanceCount = 0;
		}

		private void addInstance(byte[] instanceData, boolean useDefaultOffset) {
			payload.addElements(payload.size(), instanceData, 0, INSTANCE_STRIDE);
			if (useDefaultOffset) {
				defaultOffsetInstanceIndices.add(instanceCount);
			}
			instanceCount++;
		}
	}
}
