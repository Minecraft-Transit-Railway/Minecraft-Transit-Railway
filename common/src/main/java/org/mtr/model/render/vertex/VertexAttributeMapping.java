package org.mtr.model.render.vertex;

import org.lwjgl.opengl.GL33;
import org.mtr.model.render.object.VertexBuffer;

import java.util.HashMap;
import java.util.Map;

public final class VertexAttributeMapping {

	public final Map<VertexAttributeType, VertexAttributeSource> sources;
	public final Map<VertexAttributeType, Integer> pointers = new HashMap<>();
	public final int strideVertex, strideInstance;
	public final int paddingVertex, paddingInstance;

	private VertexAttributeMapping(Map<VertexAttributeType, VertexAttributeSource> sources) {
		this.sources = sources;

		int strideVertex = 0;
		int strideInstance = 0;
		for (final VertexAttributeType vertexAttributeType : VertexAttributeType.values()) {
			switch (sources.get(vertexAttributeType)) {
				case VERTEX_BUFFER:
					pointers.put(vertexAttributeType, strideVertex);
					strideVertex += vertexAttributeType.byteSize;
					break;
				case INSTANCE_BUFFER:
					pointers.put(vertexAttributeType, strideInstance);
					strideInstance += vertexAttributeType.byteSize;
					break;
			}
		}
		if (strideVertex % 2 != 0) {
			strideVertex++;
			paddingVertex = 1;
		} else {
			paddingVertex = 0;
		}
		if (strideInstance % 2 != 0) {
			strideInstance++;
			paddingInstance = 1;
		} else {
			paddingInstance = 0;
		}

		this.strideVertex = strideVertex;
		this.strideInstance = strideInstance;
	}

	public void setupAttributesToVao(VertexBuffer vertexBuf) {
		for (final VertexAttributeType vertexAttributeType : VertexAttributeType.values()) {
			switch (sources.get(vertexAttributeType)) {
				case GLOBAL:
					vertexAttributeType.toggleAttributeArray(false);
					break;
				case VERTEX_BUFFER:
					vertexAttributeType.toggleAttributeArray(true);
					vertexBuf.bind(GL33.GL_ARRAY_BUFFER);
					vertexAttributeType.setupAttributePointer(strideVertex, pointers.get(vertexAttributeType));
					vertexAttributeType.setAttributeDivisor(0);
					break;
			}
		}
	}

	public static class Builder {

		private final HashMap<VertexAttributeType, VertexAttributeSource> sources;

		public Builder() {
			sources = new HashMap<>(VertexAttributeType.values().length);
			for (final VertexAttributeType vertexAttributeType : VertexAttributeType.values()) {
				sources.put(vertexAttributeType, VertexAttributeSource.VERTEX_BUFFER);
			}
		}

		public Builder set(VertexAttributeType vertexAttributeType, VertexAttributeSource vertexAttributeSource) {
			sources.put(vertexAttributeType, vertexAttributeSource);
			return this;
		}

		public VertexAttributeMapping build() {
			return new VertexAttributeMapping(sources);
		}
	}
}
