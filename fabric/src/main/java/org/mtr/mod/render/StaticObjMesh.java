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
	public final int materialColor;
	public final String rawVertexSample;
	private final Mesh mesh;

	public StaticObjMesh(RawMesh rawMesh) {
		this.texture = rawMesh.materialProperties.getTexture();
		final Integer rawMaterialColor = rawMesh.materialProperties.vertexAttributeState.color;
		materialColor = rawMaterialColor == null ? 0xFFFFFFFF : rawMaterialColor;
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
		rawVertexSample = sampleRawVertices(rawMesh);
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

	private static String sampleRawVertices(RawMesh rawMesh) {
		final StringBuilder stringBuilder = new StringBuilder();
		final int count = Math.min(3, rawMesh.vertices.size());
		for (int i = 0; i < count; i++) {
			final org.mtr.mapping.render.vertex.Vertex vertex = rawMesh.vertices.get(i);
			if (i > 0) {
				stringBuilder.append(" | ");
			}
			stringBuilder.append(String.format(
					"v%d pos=(%.5f, %.5f, %.5f) normal=(%.5f, %.5f, %.5f) uv=(%.5f, %.5f)",
					i,
					getX(vertex.position),
					getY(vertex.position),
					getZ(vertex.position),
					getX(vertex.normal),
					getY(vertex.normal),
					getZ(vertex.normal),
					vertex.u,
					vertex.v
			));
		}
		return stringBuilder.length() == 0 ? "none" : stringBuilder.toString();
	}

	private static float getX(@javax.annotation.Nullable org.mtr.mapping.holder.Vector3f vector3f) {
		return vector3f == null ? Float.NaN : vector3f.getX();
	}

	private static float getY(@javax.annotation.Nullable org.mtr.mapping.holder.Vector3f vector3f) {
		return vector3f == null ? Float.NaN : vector3f.getY();
	}

	private static float getZ(@javax.annotation.Nullable org.mtr.mapping.holder.Vector3f vector3f) {
		return vector3f == null ? Float.NaN : vector3f.getZ();
	}

	@Override
	public void close() {
		diagnosticVertexArray.close();
		vertexArray.close();
		mesh.close();
	}
}
