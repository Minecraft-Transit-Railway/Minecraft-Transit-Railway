package org.mtr.mod.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

final class GpuObjGlStateGuard implements AutoCloseable {

	private final State state;

	private GpuObjGlStateGuard(State state) {
		this.state = state;
	}

	static GpuObjGlStateGuard capture() {
		return new GpuObjGlStateGuard(State.capture());
	}

	static State captureState() {
		return State.capture();
	}

	static void clearInstanceBufferBinding() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void close() {
		state.restore();
	}

	static final class State {

		private final int currentProgram;
		private final int vertexArray;
		private final int arrayBuffer;
		private final int elementArrayBuffer;
		private final boolean hasElementArrayBufferState;
		private final int activeTexture;
		private final int texture2D;
		private final boolean depthTest;
		private final boolean depthMask;
		private final boolean blend;
		private final boolean cullFace;
		private final boolean polygonOffsetFill;
		private final int blendSrcRgb;
		private final int blendDstRgb;
		private final int blendSrcAlpha;
		private final int blendDstAlpha;
		private final int blendEquationRgb;
		private final int blendEquationAlpha;
		private final int cullFaceMode;
		private final int frontFace;
		private final float polygonOffsetFactor;
		private final float polygonOffsetUnits;

		private State() {
			currentProgram = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
			vertexArray = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
			arrayBuffer = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
			hasElementArrayBufferState = vertexArray != 0;
			elementArrayBuffer = hasElementArrayBufferState ? GL11.glGetInteger(GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING) : 0;
			activeTexture = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
			texture2D = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
			depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
			depthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
			blend = GL11.glIsEnabled(GL11.GL_BLEND);
			cullFace = GL11.glIsEnabled(GL11.GL_CULL_FACE);
			polygonOffsetFill = GL11.glIsEnabled(GL11.GL_POLYGON_OFFSET_FILL);
			blendSrcRgb = GL11.glGetInteger(GL14.GL_BLEND_SRC_RGB);
			blendDstRgb = GL11.glGetInteger(GL14.GL_BLEND_DST_RGB);
			blendSrcAlpha = GL11.glGetInteger(GL14.GL_BLEND_SRC_ALPHA);
			blendDstAlpha = GL11.glGetInteger(GL14.GL_BLEND_DST_ALPHA);
			blendEquationRgb = GL11.glGetInteger(GL20.GL_BLEND_EQUATION_RGB);
			blendEquationAlpha = GL11.glGetInteger(GL20.GL_BLEND_EQUATION_ALPHA);
			cullFaceMode = GL11.glGetInteger(GL11.GL_CULL_FACE_MODE);
			frontFace = GL11.glGetInteger(GL11.GL_FRONT_FACE);
			polygonOffsetFactor = GL11.glGetFloat(GL11.GL_POLYGON_OFFSET_FACTOR);
			polygonOffsetUnits = GL11.glGetFloat(GL11.GL_POLYGON_OFFSET_UNITS);
		}

		private static State capture() {
			return new State();
		}

		private void restore() {
			setEnabled(GL11.GL_DEPTH_TEST, depthTest);
			GL11.glDepthMask(depthMask);
			setEnabled(GL11.GL_BLEND, blend);
			setEnabled(GL11.GL_CULL_FACE, cullFace);
			setEnabled(GL11.GL_POLYGON_OFFSET_FILL, polygonOffsetFill);
			GL14.glBlendFuncSeparate(blendSrcRgb, blendDstRgb, blendSrcAlpha, blendDstAlpha);
			GL20.glBlendEquationSeparate(blendEquationRgb, blendEquationAlpha);
			GL11.glCullFace(cullFaceMode);
			GL11.glFrontFace(frontFace);
			GL11.glPolygonOffset(polygonOffsetFactor, polygonOffsetUnits);
			GL20.glUseProgram(currentProgram);
			GL30.glBindVertexArray(vertexArray);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, arrayBuffer);
			if (hasElementArrayBufferState) {
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, elementArrayBuffer);
			}
			GL13.glActiveTexture(activeTexture);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture2D);
		}

		String summary() {
			return String.format(
					"program=%d vao=%d array=%d element=%d activeTex=%d tex2D=%d depthTest=%s depthMask=%s blend=%s cull=%s polygonOffsetFill=%s",
					currentProgram,
					vertexArray,
					arrayBuffer,
					hasElementArrayBufferState ? elementArrayBuffer : -1,
					activeTexture - GL13.GL_TEXTURE0,
					texture2D,
					depthTest,
					depthMask,
					blend,
					cullFace,
					polygonOffsetFill
			);
		}

		private static void setEnabled(int capability, boolean enabled) {
			if (enabled) {
				GL11.glEnable(capability);
			} else {
				GL11.glDisable(capability);
			}
		}
	}
}
