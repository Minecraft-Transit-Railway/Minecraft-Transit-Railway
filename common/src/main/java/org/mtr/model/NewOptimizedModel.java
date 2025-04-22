package org.mtr.model;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public final class NewOptimizedModel {

	public final Identifier texture;
	@Nullable
	private final VertexBuffer vertexBuffer;
	private final VertexFormat.DrawMode drawMode;

	public NewOptimizedModel(Identifier texture, VertexFormat.DrawMode drawMode, @Nullable Consumer<VertexConsumer> callback) {
		this.vertexBuffer = callback == null ? null : VertexBuffer.createAndUpload(drawMode, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, callback);
		this.texture = texture;
		this.drawMode = drawMode;
	}

	public void render(Matrix4f matrix4f, float lightMultiplier, @Nullable ShaderProgram shaderProgram) {
		if (vertexBuffer != null && shaderProgram != null) {
			if (shaderProgram.modelViewMat != null) {
				shaderProgram.modelViewMat.set(new Matrix4f(RenderSystem.getModelViewMatrix()).mul(matrix4f));
				shaderProgram.modelViewMat.upload();
			}
			if (shaderProgram.colorModulator != null) {
				shaderProgram.colorModulator.set(new float[]{lightMultiplier, lightMultiplier, lightMultiplier, 1});
				shaderProgram.colorModulator.upload();
			}
			vertexBuffer.draw();
		}
	}

	public void begin(@Nullable ShaderProgram shaderProgram) {
		if (vertexBuffer != null && shaderProgram != null) {
			vertexBuffer.bind();
			shaderProgram.initializeUniforms(drawMode, RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix(), MinecraftClient.getInstance().getWindow());
			shaderProgram.bind();
		}
	}
}
