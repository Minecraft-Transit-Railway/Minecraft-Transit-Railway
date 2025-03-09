package org.mtr.model.render.tool;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgram;
import org.lwjgl.opengl.GL33;
import org.mtr.MTR;

import java.util.Locale;

public final class GlStateTracker {

	private static boolean supportVertexAttributeDivisor;
	private static boolean isGl4ES = false;

	private static int vertArrayBinding;
	private static int arrayBufBinding;
	private static int elementBufBinding;
	private static ShaderProgram currentShaderProgram;
	private static boolean isStateProtected;

	public static void capture() {
		final int contextVersion = GL33.glGetInteger(GL33.GL_MAJOR_VERSION) * 10 + GL33.glGetInteger(GL33.GL_MINOR_VERSION);
		supportVertexAttributeDivisor = contextVersion >= 33;
		final String glVersion = GL33.glGetString(GL33.GL_VERSION);
		isGl4ES = glVersion != null && glVersion.toLowerCase(Locale.ENGLISH).contains("gl4es");

		if (isStateProtected) {
			return;
		}

		vertArrayBinding = GL33.glGetInteger(GL33.GL_VERTEX_ARRAY_BINDING);
		arrayBufBinding = GL33.glGetInteger(GL33.GL_ARRAY_BUFFER_BINDING);
		elementBufBinding = GL33.glGetInteger(GL33.GL_ELEMENT_ARRAY_BUFFER_BINDING);
		currentShaderProgram = RenderSystem.getShader();
		isStateProtected = true;
	}

	public static void restore() {
		if (!isStateProtected) {
			final IllegalStateException e = new IllegalStateException("GlStateTracker: Not captured");
			MTR.LOGGER.error("", e);
			throw e;
		}
		GL33.glBindVertexArray(vertArrayBinding);
		GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, arrayBufBinding);
		GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, elementBufBinding);

		RenderSystem.setShader(currentShaderProgram);

		// Obtain original state from RenderSystem?
		RenderSystem.enableCull();
		RenderSystem.depthMask(true);

		isStateProtected = false;
	}

	public static void assertProtected() {
		if (!isStateProtected) {
			final IllegalStateException e = new IllegalStateException("GlStateTracker: Not protected");
			MTR.LOGGER.error("", e);
			throw e;
		}
	}

	public static boolean supportVertexAttributeDivisor() {
		return supportVertexAttributeDivisor;
	}

	public static boolean isGl4ES() {
		return isGl4ES;
	}
}
