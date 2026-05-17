package org.mtr.mod.render;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33;
import org.mtr.libraries.it.unimi.dsi.fastutil.bytes.ByteArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.floats.FloatArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.render.batch.MaterialProperties;
import org.mtr.mapping.render.object.VertexArray;
import org.mtr.mapping.render.object.VertexBuffer;
import org.mtr.mapping.render.shader.ShaderManager;
import org.mtr.mapping.render.vertex.VertexAttributeMapping;
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
	private static final int TRANSLATION_X_INDEX = 12;
	private static final int TRANSLATION_Y_INDEX = 13;
	private static final int TRANSLATION_Z_INDEX = 14;

	private final ShaderManager shaderManager = new ShaderManager();
	private final VertexBuffer instanceBuffer = new VertexBuffer();
	private final Object2ObjectOpenHashMap<ObjBatchKey, BatchEntry> batches = new Object2ObjectOpenHashMap<>();
	private ByteBuffer byteBuffer = ByteBuffer.allocateDirect(INSTANCE_STRIDE);

	public void queue(ObjBatchKey batchKey, MaterialProperties materialProperties, StaticObjMesh staticObjMesh, Matrix4f matrix, int packedLight, int packedColor, boolean useDefaultOffset) {
		final MeshEntry meshEntry = batches.computeIfAbsent(batchKey, key -> new BatchEntry(materialProperties)).meshes.computeIfAbsent(staticObjMesh, key -> new MeshEntry());
		for (int i = 0; i < MATRIX_FLOATS; i++) {
			meshEntry.matrixData.add(matrix.get(i / 4, i % 4));
		}
		meshEntry.packedColors.add(packedColor);
		meshEntry.packedLights.add(packedLight);
		meshEntry.useDefaultOffsets.add((byte) (useDefaultOffset ? 1 : 0));
	}

	public void renderOpaque(Vector3d offset) {
		for (final RenderStage renderStage : RenderStage.values()) {
			batches.forEach((batchKey, batchEntry) -> {
				if (batchKey.renderStage == renderStage && !batchKey.translucent) {
					shaderManager.setupShaderBatchState(batchEntry.materialProperties);
					batchEntry.meshes.forEach((staticObjMesh, meshEntry) -> render(batchEntry.materialProperties, staticObjMesh, meshEntry, offset));
					shaderManager.cleanupShaderBatchState();
				}
			});
		}
	}

	public void clear() {
		batches.values().forEach(batchEntry -> batchEntry.meshes.values().forEach(MeshEntry::clear));
	}

	public static void setupInstanceAttributes(VertexArray vertexArray) {
		vertexArray.bind();
		INSTANCE.instanceBuffer.bind(GL33.GL_ARRAY_BUFFER);
		setupInstanceAttribute(VertexAttributeType.COLOR);
		setupInstanceAttribute(VertexAttributeType.UV_LIGHTMAP);
		setupInstanceAttribute(VertexAttributeType.MATRIX_MODEL);
		VertexArray.unbind();
	}

	private void render(MaterialProperties materialProperties, StaticObjMesh staticObjMesh, MeshEntry meshEntry, Vector3d offset) {
		final int instanceCount = meshEntry.getInstanceCount();
		if (instanceCount == 0) {
			return;
		}

		ensureCapacity(instanceCount);
		byteBuffer.clear();

		for (int i = 0; i < instanceCount; i++) {
			final int baseIndex = i * MATRIX_FLOATS;
			final boolean useDefaultOffset = meshEntry.useDefaultOffsets.getByte(i) != 0;
			for (int j = 0; j < MATRIX_FLOATS; j++) {
				float value = meshEntry.matrixData.getFloat(baseIndex + j);
				if (useDefaultOffset) {
					if (j == TRANSLATION_X_INDEX) {
						value -= (float) offset.getXMapped();
					} else if (j == TRANSLATION_Y_INDEX) {
						value -= (float) offset.getYMapped();
					} else if (j == TRANSLATION_Z_INDEX) {
						value -= (float) offset.getZMapped();
					}
				}
				byteBuffer.putFloat(value);
			}
			byteBuffer.putInt(meshEntry.packedColors.getInt(i));
			byteBuffer.putShort((short) (meshEntry.packedLights.getInt(i) >> 16));
			byteBuffer.putShort((short) meshEntry.packedLights.getInt(i));
		}

		byteBuffer.flip();
		staticObjMesh.vertexArray.bind();
		instanceBuffer.bind(GL33.GL_ARRAY_BUFFER);
		instanceBuffer.upload(byteBuffer, VertexBuffer.USAGE_STREAM_DRAW);
		materialProperties.vertexAttributeState.apply();
		GL33.glDrawElementsInstanced(GL33.GL_TRIANGLES, staticObjMesh.vertexArray.indexBuffer.getVertexCount(), staticObjMesh.vertexArray.indexBuffer.indexType, 0, instanceCount);
	}

	private void ensureCapacity(int instanceCount) {
		final int requiredBytes = Math.max(INSTANCE_STRIDE, instanceCount * INSTANCE_STRIDE);
		if (byteBuffer.capacity() < requiredBytes) {
			byteBuffer = ByteBuffer.allocateDirect(requiredBytes);
		}
	}

	private static void setupInstanceAttribute(VertexAttributeType vertexAttributeType) {
		vertexAttributeType.toggleAttributeArray(true);
		vertexAttributeType.setupAttributePointer(VERTEX_ATTRIBUTE_MAPPING.strideInstance, VERTEX_ATTRIBUTE_MAPPING.pointers.get(vertexAttributeType));
		vertexAttributeType.setAttributeDivisor(1);
	}

	private static final class BatchEntry {

		private final MaterialProperties materialProperties;
		private final Object2ObjectOpenHashMap<StaticObjMesh, MeshEntry> meshes = new Object2ObjectOpenHashMap<>();

		private BatchEntry(MaterialProperties materialProperties) {
			this.materialProperties = materialProperties;
		}
	}

	private static final class MeshEntry {

		private final FloatArrayList matrixData = new FloatArrayList();
		private final IntArrayList packedColors = new IntArrayList();
		private final IntArrayList packedLights = new IntArrayList();
		private final ByteArrayList useDefaultOffsets = new ByteArrayList();

		private int getInstanceCount() {
			return packedLights.size();
		}

		private void clear() {
			matrixData.clear();
			packedColors.clear();
			packedLights.clear();
			useDefaultOffsets.clear();
		}
	}
}
