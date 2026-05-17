package org.mtr.mod.render;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.render.model.Mesh;
import org.mtr.mapping.render.model.RawMesh;
import org.mtr.mapping.render.object.VertexArray;

import java.io.Closeable;

public final class StaticObjMesh implements Closeable {

	public final Identifier texture;
	public final VertexArray vertexArray;
	private final Mesh mesh;

	public StaticObjMesh(RawMesh rawMesh) {
		this.texture = rawMesh.materialProperties.getTexture();
		mesh = rawMesh.upload(GpuObjRenderer.VERTEX_ATTRIBUTE_MAPPING);
		vertexArray = new VertexArray(mesh, GpuObjRenderer.VERTEX_ATTRIBUTE_MAPPING);
		GpuObjRenderer.setupInstanceAttributes(vertexArray);
	}

	@Override
	public void close() {
		vertexArray.close();
		mesh.close();
	}
}
