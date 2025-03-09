package org.mtr.model.render.vertex;

public enum VertexAttributeSource {

	/**
	 * Specified dynamically from code when the draw call (BatchManager.enqueue) is placed,
	 * or in MaterialProp during model loading. MaterialProp has priority.
	 */
	GLOBAL,
	/**
	 * Stored statically in OpenGL vertex buffer.
	 */
	VERTEX_BUFFER,
	/**
	 * Stored statically in OpenGL instance buffer.
	 */
	INSTANCE_BUFFER,
}
