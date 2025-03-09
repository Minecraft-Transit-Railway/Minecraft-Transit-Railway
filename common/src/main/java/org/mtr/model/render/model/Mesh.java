package org.mtr.model.render.model;

import org.mtr.model.render.batch.MaterialProperties;
import org.mtr.model.render.object.IndexBuffer;
import org.mtr.model.render.object.VertexBuffer;

import java.io.Closeable;

public final class Mesh implements Closeable {

	public final VertexBuffer vertexBuffer;
	public final IndexBuffer indexBuffer;

	public final MaterialProperties materialProperties;

	public Mesh(VertexBuffer vertexBuffer, IndexBuffer indexBuffer, MaterialProperties materialProperties) {
		this.vertexBuffer = vertexBuffer;
		this.indexBuffer = indexBuffer;
		this.materialProperties = materialProperties;
	}

	@Override
	public void close() {
		vertexBuffer.close();
		indexBuffer.close();
	}
}
