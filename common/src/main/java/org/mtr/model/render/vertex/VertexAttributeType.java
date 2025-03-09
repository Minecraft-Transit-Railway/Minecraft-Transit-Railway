package org.mtr.model.render.vertex;

import org.lwjgl.opengl.GL33;
import org.mtr.model.render.tool.GlStateTracker;

public enum VertexAttributeType {

	// Location must be consistent with MC_FORMAT_BLOCK_MAT in ShaderManager
	POSITION(0, GL33.GL_FLOAT, 3, 1, false, false),
	COLOR(1, GL33.GL_UNSIGNED_BYTE, 4, 1, true, false),
	UV_TEXTURE(2, GL33.GL_FLOAT, 2, 1, false, false),
	UV_OVERLAY(3, GL33.GL_SHORT, 2, 1, false, true),
	UV_LIGHTMAP(4, GL33.GL_SHORT, 2, 1, false, true),
	NORMAL(5, GL33.GL_BYTE, 3, 1, true, false),
	MATRIX_MODEL(6, GL33.GL_FLOAT, 4, 4, false, false);

	/**
	 * The location of the first OpenGL vertex attribute, corresponding to the one assigned by the Minecraft VertexFormat.
	 */
	public final int location;
	/**
	 * The OpenGL type of each element in this attribute. GL_FLOAT/GL_BYTE/GL_UNSIGNED_BYTE/GL_SHORT.
	 */
	public final int type;
	/**
	 * The count of elements in each of the OpenGL vertex attribute.
	 */
	public final int size;
	/**
	 * The amount of OpenGL vertex attributes compositing this attribute.
	 * 1 for pretty much everything, while a 4x4 matrix is represented by 4 attributes, each of which has a size of 4.
	 */
	public final int span;
	/**
	 * The total size this attribute occupies in the buffer. sizeof(type) * size * span.
	 */
	public final int byteSize;
	/**
	 * Whether to normalize the attribute in glVertexAttribPointer.
	 */
	public final boolean normalized;
	/**
	 * Whether to use glVertexAttribIPointer instead of glVertexAttribPointer.
	 */
	public final boolean iPointer;

	VertexAttributeType(int location, int type, int size, int span, boolean normalized, boolean iPointer) {
		this.location = location;
		this.type = type;
		this.size = size;
		this.span = span;
		this.normalized = normalized;
		this.iPointer = iPointer;

		final int singleSize;
		switch (type) {
			case GL33.GL_FLOAT:
				singleSize = 4;
				break;
			case GL33.GL_UNSIGNED_BYTE:
			case GL33.GL_BYTE:
				singleSize = 1;
				break;
			case GL33.GL_SHORT:
				singleSize = 2;
				break;
			default:
				singleSize = 0;
				break;
		}

		byteSize = singleSize * size * span;
	}

	public void toggleAttributeArray(boolean enable) {
		for (int i = 0; i < span; i++) {
			if (enable) {
				GL33.glEnableVertexAttribArray(location + i);
			} else {
				GL33.glDisableVertexAttribArray(location + i);
			}
		}
	}

	public void setupAttributePointer(int stride, int pointer) {
		for (int i = 0; i < span; i++) {
			final int attributePointer = pointer + (i * byteSize / span);
			if (iPointer) {
				if (!GlStateTracker.isGl4ES()) {
					GL33.glVertexAttribIPointer(location + i, size, type, stride, attributePointer);
				} else {
					// GL4ES doesn't have binding for Attrib*i, so make the shader use float
					GL33.glVertexAttribPointer(location + i, size, type, false, stride, attributePointer);
				}
			} else {
				GL33.glVertexAttribPointer(location + i, size, type, normalized, stride, attributePointer);
			}
		}
	}

	public void setAttributeDivisor(int divisor) {
		if (GlStateTracker.supportVertexAttributeDivisor()) {
			for (int i = 0; i < span; i++) {
				GL33.glVertexAttribDivisor(location + i, divisor);
			}
		}
	}
}
