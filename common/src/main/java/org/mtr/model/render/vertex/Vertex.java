package org.mtr.model.render.vertex;

import org.joml.Vector3f;
import org.mtr.model.render.tool.Utilities;

import java.util.Objects;

public final class Vertex {

	public Vector3f position;
	public Vector3f normal;
	public float u, v;

	public int color;
	public int light;

	public Vertex() {
	}

	public Vertex(Vertex vertex) {
		position = Utilities.copy(vertex.position);
		normal = Utilities.copy(vertex.normal);
		u = vertex.u;
		v = vertex.v;
		color = vertex.color;
		light = vertex.light;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Vertex vertex = (Vertex) o;
		return Float.compare(vertex.u, u) == 0 && Float.compare(vertex.v, v) == 0 && position.equals(vertex.position) && normal.equals(vertex.normal);
	}

	@Override
	public int hashCode() {
		return Objects.hash(position, normal, u, v);
	}
}
