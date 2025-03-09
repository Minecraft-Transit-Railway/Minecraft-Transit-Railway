package org.mtr.model.render.object;

import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL33;
import org.mtr.model.render.batch.MaterialProperties;
import org.mtr.model.render.model.Mesh;
import org.mtr.model.render.tool.GlStateTracker;
import org.mtr.model.render.vertex.VertexAttributeMapping;

import java.io.Closeable;

public final class VertexArray implements Closeable {

	private int id;

	public final MaterialProperties materialProperties;
	public final IndexBuffer indexBuffer;
	public final VertexAttributeMapping mapping;

	public VertexArray(Mesh mesh, VertexAttributeMapping mapping) {
		id = GL33.glGenVertexArrays();
		materialProperties = mesh.materialProperties;
		indexBuffer = mesh.indexBuffer;
		this.mapping = mapping;
		GL33.glBindVertexArray(id);
		mapping.setupAttributesToVao(mesh.vertexBuffer);
		mesh.indexBuffer.bind(GL33.GL_ELEMENT_ARRAY_BUFFER);
		unbind();
	}

	public void bind() {
		GlStateTracker.assertProtected();
		GL33.glBindVertexArray(id);
	}

	public static void unbind() {
		GlStateTracker.assertProtected();
		GL33.glBindVertexArray(0);
	}

	public void draw() {
		GL33.glDrawElements(GL33.GL_TRIANGLES, indexBuffer.getVertexCount(), indexBuffer.indexType, 0L);
	}

	@Override
	public void close() {
		if (RenderSystem.isOnRenderThread()) {
			GL33.glDeleteVertexArrays(id);
			id = 0;
		} else {
			RenderSystem.recordRenderCall(this::close);
		}
	}
}
