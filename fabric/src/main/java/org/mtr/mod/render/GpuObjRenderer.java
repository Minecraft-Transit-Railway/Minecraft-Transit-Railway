package org.mtr.mod.render;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
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

	private final ShaderManager shaderManager = new ShaderManager();
	private final VertexBuffer instanceBuffer = new VertexBuffer();
	private final Object2ObjectOpenHashMap<ObjBatchKey, BatchEntry> batches = new Object2ObjectOpenHashMap<>();
	private ByteBuffer byteBuffer = ByteBuffer.allocateDirect(INSTANCE_STRIDE);

	public void queue(ObjBatchKey batchKey, MaterialProperties materialProperties, StaticObjMesh staticObjMesh, ObjInstance objInstance) {
		batches.computeIfAbsent(batchKey, key -> new BatchEntry(materialProperties)).meshes.computeIfAbsent(staticObjMesh, key -> new ObjectArrayList<>()).add(objInstance);
	}

	public void renderOpaque(Vector3d offset) {
		for (final RenderStage renderStage : RenderStage.values()) {
			batches.forEach((batchKey, batchEntry) -> {
				if (batchKey.renderStage == renderStage && !batchKey.translucent) {
					batchEntry.meshes.forEach((staticObjMesh, instances) -> render(batchEntry.materialProperties, staticObjMesh, instances, offset));
				}
			});
		}
	}

	public void clear() {
		batches.values().forEach(batchEntry -> batchEntry.meshes.values().forEach(ObjectArrayList::clear));
	}

	public static void setupInstanceAttributes(VertexArray vertexArray) {
		vertexArray.bind();
		INSTANCE.instanceBuffer.bind(GL33.GL_ARRAY_BUFFER);
		setupInstanceAttribute(VertexAttributeType.COLOR);
		setupInstanceAttribute(VertexAttributeType.UV_LIGHTMAP);
		setupInstanceAttribute(VertexAttributeType.MATRIX_MODEL);
		VertexArray.unbind();
	}

	private void render(MaterialProperties materialProperties, StaticObjMesh staticObjMesh, ObjectArrayList<ObjInstance> instances, Vector3d offset) {
		if (instances.isEmpty()) {
			return;
		}

		ensureCapacity(instances.size());
		byteBuffer.clear();

		for (int i = 0; i < instances.size(); i++) {
			final ObjInstance objInstance = instances.get(i);
			final Matrix4f matrix = objInstance.useDefaultOffset ? new Matrix4f(objInstance.matrix).translate((float) -offset.getXMapped(), (float) -offset.getYMapped(), (float) -offset.getZMapped()) : objInstance.matrix;
			for (int j = 0; j < MATRIX_FLOATS; j++) {
				byteBuffer.putFloat(matrix.get(j / 4, j % 4));
			}
			byteBuffer.putInt(objInstance.packedColor);
			byteBuffer.putShort((short) (objInstance.packedLight >> 16));
			byteBuffer.putShort((short) objInstance.packedLight);
		}

		byteBuffer.flip();
		shaderManager.setupShaderBatchState(materialProperties);
		staticObjMesh.vertexArray.bind();
		instanceBuffer.bind(GL33.GL_ARRAY_BUFFER);
		instanceBuffer.upload(byteBuffer, VertexBuffer.USAGE_STREAM_DRAW);
		materialProperties.vertexAttributeState.apply();
		GL33.glDrawElementsInstanced(GL33.GL_TRIANGLES, staticObjMesh.vertexArray.indexBuffer.getVertexCount(), staticObjMesh.vertexArray.indexBuffer.indexType, 0, instances.size());
		shaderManager.cleanupShaderBatchState();
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
		private final Object2ObjectOpenHashMap<StaticObjMesh, ObjectArrayList<ObjInstance>> meshes = new Object2ObjectOpenHashMap<>();

		private BatchEntry(MaterialProperties materialProperties) {
			this.materialProperties = materialProperties;
		}
	}
}
