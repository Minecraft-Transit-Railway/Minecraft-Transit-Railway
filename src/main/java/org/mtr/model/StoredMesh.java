package org.mtr.model;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

//? if >= 26.1 {
/*import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
*///? } else {
import com.mojang.blaze3d.vertex.VertexBuffer;
//? }

/**
 * A mesh uploaded to the GPU once and drawn many times, with no texture of its own.
 *
 * <p>This exists because the two versions hold that mesh differently. Before 26.1 a
 * {@code VertexBuffer} carried both the data and the vertex count; from 26.1 the data lives in a
 * {@code GpuBuffer} and the count has to be kept alongside it. Wrapping the pair here keeps the
 * difference in one place, so the map tile code that uses it reads the same on every version.</p>
 */
public final class StoredMesh {

//? if >= 26.1 {
	/*@Nullable
	private final GpuBuffer buffer;
	private final int indexCount;
	private final VertexFormat.Mode drawMode;
*///? } else {
	@Nullable
	private final VertexBuffer vertexBuffer;
//? }

	private StoredMesh(VertexFormat.Mode drawMode, VertexFormat vertexFormat, Consumer<VertexConsumer> callback) {
//? if >= 26.1 {
		/*GpuBuffer builtBuffer = null;
		int builtIndexCount = 0;

		// An allocator of its own rather than the shared tesselator, for the reason given where the
		// models are built: the shared one hands out a builder over a single buffer, so meshes built
		// while another is part way through corrupt each other.
		try (final ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(1536)) {
			final BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, drawMode, vertexFormat);
			callback.accept(bufferBuilder);

			try (final MeshData meshData = bufferBuilder.build()) {
				if (meshData != null) {
					builtIndexCount = meshData.drawState().indexCount();
					builtBuffer = RenderSystem.getDevice().createBuffer(() -> "MTR stored mesh", GpuBuffer.USAGE_VERTEX, meshData.vertexBuffer());
				}
			}
		}

		this.buffer = builtBuffer;
		this.indexCount = builtIndexCount;
		this.drawMode = drawMode;
*///? } else {
		vertexBuffer = NewOptimizedModel.createVertexBuffer(drawMode, vertexFormat, callback);
//? }
	}

	public static StoredMesh create(VertexFormat.Mode drawMode, VertexFormat vertexFormat, Consumer<VertexConsumer> callback) {
		return new StoredMesh(drawMode, vertexFormat, callback);
	}

	/**
	 * Releases the GPU resources backing this mesh.
	 */
	public void close() {
//? if >= 26.1 {
		/*if (buffer != null) {
			buffer.close();
		}
*///? } else {
		if (vertexBuffer != null) {
			vertexBuffer.close();
		}
//? }
	}

//? if >= 26.1 {
	/*// Bind this mesh into a pass that already has its pipeline set, then draw it once with a
	// transform that has already been written. The transform arrives as a slice rather than as
	// matrices, because writing one maps a buffer and that cannot be done while a pass is open;
	// the caller writes the whole batch beforehand. The colour travels inside it, the global shader
	// colour the older code set no longer existing.
	public void draw(RenderPass renderPass, GpuBufferSlice transform) {
		if (buffer != null) {
			renderPass.setVertexBuffer(0, buffer);
			final RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(drawMode);
			renderPass.setIndexBuffer(autoStorageIndexBuffer.getBuffer(indexCount), autoStorageIndexBuffer.type());
			renderPass.setUniform("DynamicTransforms", transform);
			renderPass.drawIndexed(0, 0, indexCount, 1);
		}
	}
*///? } else {
	@Nullable
	public VertexBuffer getVertexBuffer() {
		return vertexBuffer;
	}
//? }
}
