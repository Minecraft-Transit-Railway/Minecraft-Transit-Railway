package org.mtr.mod.render;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.render.model.Mesh;
import org.mtr.mapping.render.model.RawMesh;
import org.mtr.mapping.render.object.VertexArray;

import java.io.Closeable;

public final class StaticObjMesh implements Closeable {

	public final Identifier texture;
	public final VertexArray vertexArray;
	private final VertexArray diagnosticVertexArray;
	public final int vertexCount;
	public final float minX;
	public final float minY;
	public final float minZ;
	public final float maxX;
	public final float maxY;
	public final float maxZ;
	public final float centerX;
	public final float centerY;
	public final float centerZ;
	private final Mesh mesh;

	public StaticObjMesh(RawMesh rawMesh) {
		this.texture = rawMesh.materialProperties.getTexture();
		vertexCount = rawMesh.vertices.size();
		float minX = Float.POSITIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY;
		float minZ = Float.POSITIVE_INFINITY;
		float maxX = Float.NEGATIVE_INFINITY;
		float maxY = Float.NEGATIVE_INFINITY;
		float maxZ = Float.NEGATIVE_INFINITY;
		for (final org.mtr.mapping.render.vertex.Vertex vertex : rawMesh.vertices) {
			minX = Math.min(minX, vertex.position.getX());
			minY = Math.min(minY, vertex.position.getY());
			minZ = Math.min(minZ, vertex.position.getZ());
			maxX = Math.max(maxX, vertex.position.getX());
			maxY = Math.max(maxY, vertex.position.getY());
			maxZ = Math.max(maxZ, vertex.position.getZ());
		}
		this.minX = vertexCount > 0 ? minX : 0;
		this.minY = vertexCount > 0 ? minY : 0;
		this.minZ = vertexCount > 0 ? minZ : 0;
		this.maxX = vertexCount > 0 ? maxX : 0;
		this.maxY = vertexCount > 0 ? maxY : 0;
		this.maxZ = vertexCount > 0 ? maxZ : 0;
		centerX = (this.minX + this.maxX) / 2;
		centerY = (this.minY + this.maxY) / 2;
		centerZ = (this.minZ + this.maxZ) / 2;
		mesh = rawMesh.upload(GpuObjRenderer.VERTEX_ATTRIBUTE_MAPPING);
		vertexArray = new VertexArray(mesh, GpuObjRenderer.VERTEX_ATTRIBUTE_MAPPING);
		diagnosticVertexArray = new VertexArray(mesh, GpuObjRenderer.DIAGNOSTIC_VERTEX_ATTRIBUTE_MAPPING);
		GpuObjRenderer.setupInstanceAttributes(vertexArray);
	}

	public VertexArray getDiagnosticVertexArray(org.mtr.mapping.render.batch.MaterialProperties materialProperties) {
		return new VertexArray(diagnosticVertexArray, materialProperties);
	}

	@Override
	public void close() {
		diagnosticVertexArray.close();
		vertexArray.close();
		mesh.close();
	}
}
