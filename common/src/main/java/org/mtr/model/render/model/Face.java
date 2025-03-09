package org.mtr.model.render.model;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Face {

	public final int[] vertices;

	public Face(int[] vertices) {
		this.vertices = vertices;
	}

	public Face(Face face) {
		this.vertices = Arrays.copyOf(face.vertices, face.vertices.length);
	}

	public static List<Face> triangulate(int[] vertices) {
		final List<Face> result = new ArrayList<>();
		if (vertices.length > 3) {
			for (int i = 2; i < vertices.length; i++) {
				result.add(new Face(new int[]{vertices[0], vertices[i - 1], vertices[i]}));
			}
		} else {
			result.add(new Face(new int[]{vertices[0], vertices[1], vertices[2]}));
		}
		return result;
	}

	public void flip() {
		ArrayUtils.reverse(vertices);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Face)) {
			return false;
		}
		return Arrays.equals(vertices, ((Face) object).vertices);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(vertices);
	}
}