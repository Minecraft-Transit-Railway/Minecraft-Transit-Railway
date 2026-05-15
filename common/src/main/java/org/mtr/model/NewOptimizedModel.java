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

import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

/**
 * One drawable mesh — a single texture, a single GPU vertex buffer, one draw call.
 *
 * <p>Instances are created during model build (see
 * {@link NewOptimizedModelGroup#build(VertexFormat.DrawMode)}) and live for the rest of
 * the parent loader's lifetime. The buffer is uploaded once via
 * {@link VertexBuffer#createAndUpload}; subsequent frames just call {@link #render} and
 * the GPU re-uses the stored vertices.</p>
 *
 * <p>If the supplied {@code callback} is {@code null}, no buffer is allocated and both
 * {@link #begin} and {@link #render} no-op. This is the path for empty groups.</p>
 *
 * <p><b>Naming note:</b> the "New" prefix is historical — there is no non-{@code New}
 * counterpart any more. See {@code docs/MIGRATIONS.md} §4 for the planned rename.</p>
 */
public final class NewOptimizedModel {

	public final Identifier texture;
	@Nullable
	private final VertexBuffer vertexBuffer;
	private final VertexFormat.DrawMode drawMode;

	/**
	 * @param texture  the texture this mesh draws with
	 * @param drawMode the GL primitive type ({@code TRIANGLES} for OBJ, {@code QUADS} for
	 *                 Blockbench)
	 * @param callback the vertex-emitter callback invoked exactly once to populate the
	 *                 buffer, or {@code null} to skip buffer creation entirely
	 */
	public NewOptimizedModel(Identifier texture, VertexFormat.DrawMode drawMode, @Nullable Consumer<VertexConsumer> callback) {
		this.vertexBuffer = callback == null ? null : VertexBuffer.createAndUpload(drawMode, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, callback);
		this.texture = texture;
		this.drawMode = drawMode;
	}

	/**
	 * Issue the draw call after {@link #begin(ShaderProgram)} has bound the buffer.
	 *
	 * @param matrix4f         the per-instance model matrix multiplied into the active view matrix
	 * @param lightMultiplier  brightness scale in {@code [0, 1]}; {@code 1} for full-bright stages
	 * @param shaderProgram    the active shader, may be {@code null} during reload
	 */
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

	/**
	 * Bind this model's vertex buffer and initialise the shader uniforms for the current
	 * GL state. Must be paired with one or more {@link #render(Matrix4f, float, ShaderProgram)}
	 * calls per frame, then the next mesh's {@code begin(...)}.
	 */
	public void begin(@Nullable ShaderProgram shaderProgram) {
		if (vertexBuffer != null && shaderProgram != null) {
			vertexBuffer.bind();
			shaderProgram.initializeUniforms(drawMode, RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix(), MinecraftClient.getInstance().getWindow());
			shaderProgram.bind();
		}
	}
}
