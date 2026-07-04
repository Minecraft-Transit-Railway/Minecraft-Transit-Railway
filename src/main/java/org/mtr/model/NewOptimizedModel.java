package org.mtr.model;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

//? if >= 1.21.4 {
import net.minecraft.client.renderer.CompiledShaderProgram;
//? } else {
/*import net.minecraft.client.renderer.ShaderInstance;
 *///? }

/**
 * One drawable mesh — a single texture, a single GPU vertex buffer, one draw call.
 *
 * <p>Instances are created during model build (see
 * {@link NewOptimizedModelGroup#build(VertexFormat.Mode)}) and live for the rest of
 * the parent loader's lifetime. The buffer is uploaded once via
 * {@link VertexBuffer#uploadStatic}; subsequent frames just call {@link #render} and
 * the GPU re-uses the stored vertices.</p>
 *
 * <p>If the supplied {@code callback} is {@code null}, no buffer is allocated and both
 * {@link #begin} and {@link #render} no-op. This is the path for empty groups.</p>
 *
 * <p><b>Naming note:</b> the "New" prefix is historical — there is no non-{@code New}
 * counterpart any more. See {@code docs/MIGRATIONS.md} §4 for the planned rename.</p>
 */
public final class NewOptimizedModel {

	public final ResourceLocation texture;
	@Nullable
	private final VertexBuffer vertexBuffer;
	private final VertexFormat.Mode drawMode;

	/**
	 * Reusable model-view matrix scratch — render is single-threaded on the GL thread, so
	 * a single buffer avoids a per-draw allocation. See {@code docs/PERFORMANCE.md} §3.2.
	 */
	private static final Matrix4f MODEL_VIEW_SCRATCH = new Matrix4f();
	/**
	 * Pre-built {@code colorModulator} arrays for every value of {@code lightMultiplier}
	 * the renderer ever emits. The renderer derives {@code lightMultiplier} from a 4-bit
	 * lightmap value (16 levels), or sets {@code 1.0f} for full-bright stages. We pack
	 * that into a 17-entry table — index {@code 0..15} for {@code i / 15f}, index
	 * {@code 16} for the full-bright case. Callers that supply a different value (e.g.
	 * fractional dimming for cinematic fades) fall back to a freshly allocated array.
	 * See {@code docs/PERFORMANCE.md} §3.2 item 1.
	 */
	private static final float[][] COLOR_MODULATORS;

	static {
		COLOR_MODULATORS = new float[17][];
		for (int i = 0; i < 16; i++) {
			final float v = i / 15f;
			COLOR_MODULATORS[i] = new float[]{v, v, v, 1f};
		}
		COLOR_MODULATORS[16] = new float[]{1f, 1f, 1f, 1f};
	}

	/**
	 * @param texture  the texture this mesh draws with
	 * @param drawMode the GL primitive type ({@code TRIANGLES} for OBJ, {@code QUADS} for
	 *                 Blockbench)
	 * @param callback the vertex-emitter callback invoked exactly once to populate the
	 *                 buffer, or {@code null} to skip buffer creation entirely
	 */
	public NewOptimizedModel(ResourceLocation texture, VertexFormat.Mode drawMode, @Nullable Consumer<VertexConsumer> callback) {
		this.vertexBuffer = callback == null ? null : createVertexBuffer(drawMode, DefaultVertexFormat.NEW_ENTITY, callback);
		this.texture = texture;
		this.drawMode = drawMode;
	}

	/**
	 * Issue the draw call after {@link #begin(CompiledShaderProgram)} has bound the buffer.
	 *
	 * @param matrix4f        the per-instance model matrix multiplied into the active view matrix
	 * @param lightMultiplier brightness scale in {@code [0, 1]}; {@code 1} for full-bright stages
	 * @param shaderProgram   the active shader, may be {@code null} during reload
	 */
//? if >= 1.21.4 {
	public void render(Matrix4f matrix4f, float lightMultiplier, @Nullable CompiledShaderProgram shaderProgram) {
//? } else {
	/*public void render(Matrix4f matrix4f, float lightMultiplier, @Nullable ShaderInstance shaderProgram) {
//
*///? }
		if (vertexBuffer != null && shaderProgram != null) {
			if (shaderProgram.MODEL_VIEW_MATRIX != null) {
				// Reuse the static scratch matrix instead of `new Matrix4f(...)` per draw call.
				MODEL_VIEW_SCRATCH.set(RenderSystem.getModelViewMatrix()).mul(matrix4f);
				shaderProgram.MODEL_VIEW_MATRIX.set(MODEL_VIEW_SCRATCH);
				shaderProgram.MODEL_VIEW_MATRIX.upload();
			}
			if (shaderProgram.COLOR_MODULATOR != null) {
				shaderProgram.COLOR_MODULATOR.set(colorModulatorFor(lightMultiplier));
				shaderProgram.COLOR_MODULATOR.upload();
			}
			vertexBuffer.draw();
		}
	}

	public static VertexBuffer createVertexBuffer(VertexFormat.Mode drawMode, VertexFormat vertexFormat, Consumer<VertexConsumer> callback) {
//? if >= 1.21.4 {
		return VertexBuffer.uploadStatic(drawMode, vertexFormat, callback);
//? } else {
		/*final BufferBuilder builder = Tesselator.getInstance().begin(drawMode, vertexFormat);
		callback.accept(builder);
		final MeshData renderedBuffer = builder.build();
		final VertexBuffer vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
		vertexBuffer.bind();
		vertexBuffer.upload(renderedBuffer);
		return vertexBuffer;
*///? }
	}

	private static float[] colorModulatorFor(float lightMultiplier) {
		// Map [0, 1] → 16-step quantised table when possible; otherwise allocate.
		if (lightMultiplier >= 1f) {
			return COLOR_MODULATORS[16];
		}
		if (lightMultiplier <= 0f) {
			return COLOR_MODULATORS[0];
		}
		final int index = Math.round(lightMultiplier * 15f);
		final float quantised = index / 15f;
		// Only return the cached array when the requested value rounds cleanly — otherwise
		// allocate to preserve exact appearance for callers passing arbitrary floats.
		//noinspection FloatingPointEquality
		if (quantised == lightMultiplier) {
			return COLOR_MODULATORS[index];
		}
		return new float[]{lightMultiplier, lightMultiplier, lightMultiplier, 1f};
	}

	/**
	 * Bind this model's vertex buffer and initialise the shader uniforms for the current
	 * GL state. Must be paired with one or more {@link #render(Matrix4f, float, CompiledShaderProgram)}
	 * calls per frame, then the next mesh's {@code begin(...)}.
	 */
//? if >= 1.21.4 {
	public void begin(@Nullable CompiledShaderProgram shaderProgram) {
//? } else {
	/*public void begin(@Nullable ShaderInstance shaderProgram) {
//
*///? }
		if (vertexBuffer != null && shaderProgram != null) {
			vertexBuffer.bind();
			shaderProgram.setDefaultUniforms(drawMode, RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix(), Minecraft.getInstance().getWindow());
			shaderProgram.apply();
		}
	}
}
