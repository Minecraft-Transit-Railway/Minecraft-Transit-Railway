package org.mtr.model.render.object;

import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;
import org.mtr.model.render.tool.GlStateTracker;

import java.io.Closeable;
import java.nio.ByteBuffer;

public class VertexBuffer implements Closeable {

	private int id;

	public static final int USAGE_STATIC_DRAW = GL33.GL_STATIC_DRAW;
	public static final int USAGE_DYNAMIC_DRAW = GL33.GL_DYNAMIC_DRAW;
	public static final int USAGE_STREAM_DRAW = GL33.GL_STREAM_DRAW;

	public VertexBuffer() {
		id = GL33.glGenBuffers();
	}

	public void bind(int target) {
		GlStateTracker.assertProtected();
		GL33.glBindBuffer(target, id);
	}

	public void upload(ByteBuffer buffer, int usage) {
		final int vboPrevious = GL33.glGetInteger(GL33.GL_ARRAY_BUFFER_BINDING);
		GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, id);
		buffer.clear();
		GL33.glBufferData(GL33.GL_ARRAY_BUFFER, buffer, usage);
		GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboPrevious);
	}

	public void upload(ByteBuffer buffer, int size, int usage) {
		final int vboPrevious = GL33.glGetInteger(GL33.GL_ARRAY_BUFFER_BINDING);
		GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, id);
		buffer.clear();
		GL33.nglBufferData(GL33.GL_ARRAY_BUFFER, size, MemoryUtil.memAddress0(buffer), usage);
		GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboPrevious);
	}

	@Override
	public void close() {
		if (RenderSystem.isOnRenderThread()) {
			GL33.glDeleteBuffers(id);
			id = 0;
		} else {
			RenderSystem.recordRenderCall(this::close);
		}
	}
}
