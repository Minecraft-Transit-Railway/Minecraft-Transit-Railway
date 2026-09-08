package org.mtr.model;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;
import org.mtr.resource.RenderStage;

import java.util.function.Consumer;

//? if >= 26.1 {
/*import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.systems.RenderPass;
import net.minecraft.client.renderer.DynamicUniforms;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.mtr.render.MoreRenderLayers;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.joml.Vector3f;
import org.joml.Vector4f;
*///? } else if >= 1.21.4 {
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
//? if >= 26.1 {
	/*@Nullable
	private final GpuBuffer vertexBuffer;
	private final int indexCount;
*///? } else {
	@Nullable
	private final VertexBuffer vertexBuffer;
//? }
	private final VertexFormat.Mode drawMode;

	/**
	 * Reusable model-view matrix scratch — render is single-threaded on the GL thread, so
	 * a single buffer avoids a per-draw allocation. See {@code docs/PERFORMANCE.md} §3.2.
	 */
	private static final Matrix4f MODEL_VIEW_SCRATCH = new Matrix4f();
//? if >= 26.1 {
	/*private static final Vector3f MODEL_OFFSET_SCRATCH = new Vector3f();
	private static final Matrix4f TEXTURE_MATRIX_SCRATCH = new Matrix4f();

	// Every instance's uniform values have to exist at once, because they are written in one batch
	// before the pass opens, so the single scratch matrix the older versions reuse is not enough.
	// These pools grow to the largest batch seen and are then reused, which keeps the property that
	// scratch object was there for: nothing is allocated per instance once the frames settle. Both
	// are only read again by writeTransforms, which happens before the next batch touches them.
	private static final ObjectArrayList<Matrix4f> MODEL_VIEW_POOL = new ObjectArrayList<>();
	private static final ObjectArrayList<Vector4f> COLOR_MODULATOR_POOL = new ObjectArrayList<>();
*///? }
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
	 * @param texture     the texture this mesh draws with
	 * @param drawMode    the GL primitive type ({@code TRIANGLES} for OBJ, {@code QUADS} for
	 *                    Blockbench)
	 * @param renderStage the stage this mesh is drawn in, which decides the render layer
	 * @param callback    the vertex-emitter callback invoked exactly once to populate the
	 *                    buffer, or {@code null} to skip buffer creation entirely
	 */
//? if >= 26.1 {
	/*public NewOptimizedModel(ResourceLocation texture, VertexFormat.Mode drawMode, RenderStage renderStage, @Nullable Consumer<VertexConsumer> callback) {
		// The mesh is built once and handed straight to the GPU. From 26.1 the vertex data lives in a
		// GpuBuffer rather than a VertexBuffer, and the index count has to be kept because the draw
		// call needs it; the old VertexBuffer carried that itself.
		GpuBuffer builtBuffer = null;
		int builtIndexCount = 0;

		if (callback != null) {
			// Built through an allocator of its own rather than the shared tesselator. Beginning on the
			// shared one hands out a builder over a single buffer, so a mesh built while another is part
			// way through overwrites it, and the result is a model with some of its corners belonging to
			// something else. The versions before this one avoided it too, by way of uploadStatic, which
			// allocates privately for the same reason.
			//
			// The upload happens while the allocator is still open, because the mesh points into memory
			// the allocator owns until then.
			//
			// Packed in the format of the layer that will draw it, rather than one fixed format. Up to
			// 26.1 the buffer carried its own format and set the attribute pointers from it, so a mesh
			// whose layout was merely a superset of what the shader read still drew correctly. From
			// 26.1 the layout comes from the pipeline instead: the light stages draw through the beacon
			// beam pipeline, which reads a 32 byte vertex, so a 36 byte entity vertex was walked at the
			// wrong stride and every position after the first was read out of the middle of the
			// previous vertex. The result was the lit parts of a model streaming off to infinity while
			// everything drawn through an entity layer looked right.
			try (final ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(1536)) {
				final BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, drawMode, MoreRenderLayers.get(renderStage, texture).format());
				callback.accept(bufferBuilder);
				try (final MeshData meshData = bufferBuilder.build()) {
					if (meshData != null) {
						builtIndexCount = meshData.drawState().indexCount();
						builtBuffer = RenderSystem.getDevice().createBuffer(() -> "MTR model " + texture, GpuBuffer.USAGE_VERTEX, meshData.vertexBuffer());
					}
				}
			}
		}

		this.vertexBuffer = builtBuffer;
		this.indexCount = builtIndexCount;
		this.texture = texture;
		this.drawMode = drawMode;
	}
*///? } else {
	public NewOptimizedModel(ResourceLocation texture, VertexFormat.Mode drawMode, RenderStage renderStage, @Nullable Consumer<VertexConsumer> callback) {
		// One format for every stage here. The buffer carries it and sets the attribute pointers from
		// it, so a layer that reads fewer elements than this holds still draws correctly.
		this.vertexBuffer = callback == null ? null : createVertexBuffer(drawMode, DefaultVertexFormat.NEW_ENTITY, callback);
		this.texture = texture;
		this.drawMode = drawMode;
	}
//? }

	/**
	 * Issue the draw call after {@link #begin(CompiledShaderProgram)} has bound the buffer.
	 *
	 * @param matrix4f        the per-instance model matrix multiplied into the active view matrix
	 * @param lightMultiplier brightness scale in {@code [0, 1]}; {@code 1} for full-bright stages
	 * @param shaderProgram   the active shader, may be {@code null} during reload
	 */
//? if >= 26.1 {
	/*public static DynamicUniforms.Transform transformFor(int index, Matrix4f matrix4f, float lightMultiplier) {
		while (MODEL_VIEW_POOL.size() <= index) {
			MODEL_VIEW_POOL.add(new Matrix4f());
			COLOR_MODULATOR_POOL.add(new Vector4f());
		}
		final float[] colorModulator = colorModulatorFor(lightMultiplier);
		return new DynamicUniforms.Transform(
			MODEL_VIEW_POOL.get(index).set(RenderSystem.getModelViewMatrix()).mul(matrix4f),
			COLOR_MODULATOR_POOL.get(index).set(colorModulator[0], colorModulator[1], colorModulator[2], colorModulator[3]),
			MODEL_OFFSET_SCRATCH,
			TEXTURE_MATRIX_SCRATCH
		);
	}

	public void render(RenderPass renderPass, GpuBufferSlice transform) {
		if (vertexBuffer != null) {
			renderPass.setUniform("DynamicTransforms", transform);
			renderPass.drawIndexed(0, 0, indexCount, 1);
		}
	}
*///? } else if >= 1.21.4 {
	public void render(Matrix4f matrix4f, float lightMultiplier, @Nullable CompiledShaderProgram shaderProgram) {
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
//? } else {
	/*public void render(Matrix4f matrix4f, float lightMultiplier, @Nullable ShaderInstance shaderProgram) {
		if (vertexBuffer != null && shaderProgram != null) {
			if (shaderProgram.MODEL_VIEW_MATRIX != null) {
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
*///? }

//? if >= 26.1 {
	/*// Unused from 26.1: meshes are uploaded straight into a GpuBuffer by their owner.
*///? }
//? if < 26.1 {
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
//? }

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
//? if >= 26.1 {
	/*public void begin(RenderPass renderPass, RenderType renderLayer, AbstractTexture abstractTexture) {
		if (vertexBuffer != null) {
			renderPass.setPipeline(renderLayer.pipeline());
			RenderSystem.bindDefaultUniforms(renderPass);
			renderPass.setVertexBuffer(0, vertexBuffer);

			// All three samplers have to be bound by hand. The render setup that would normally do it
			// keeps its texture map private, and the entity vertex format carries overlay and lightmap
			// coordinates, so binding only the model's own texture would light every model wrongly
			// without any error to show for it.
			//
			// The model's own texture is passed in rather than looked up here: asking the texture
			// manager for one loads and uploads it the first time, and an upload cannot be issued while
			// a render pass is open. The overlay and the lightmap are already resident by this point in
			// the frame, so those two are still read directly.
			renderPass.bindTexture("Sampler0", abstractTexture.getTextureView(), abstractTexture.getSampler());
			renderPass.bindTexture("Sampler1", Minecraft.getInstance().gameRenderer.overlayTexture().getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
			renderPass.bindTexture("Sampler2", Minecraft.getInstance().gameRenderer.lightmap(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));

			// Quads are drawn through the shared sequential index buffer rather than one of our own.
			final RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(drawMode);
			renderPass.setIndexBuffer(autoStorageIndexBuffer.getBuffer(indexCount), autoStorageIndexBuffer.type());
		}
	}
*///? } else if >= 1.21.4 {
	public void begin(@Nullable CompiledShaderProgram shaderProgram) {
		if (vertexBuffer != null && shaderProgram != null) {
			vertexBuffer.bind();
			shaderProgram.setDefaultUniforms(drawMode, RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix(), Minecraft.getInstance().getWindow());
			shaderProgram.apply();
		}
	}
//? } else {
	/*public void begin(@Nullable ShaderInstance shaderProgram) {
		if (vertexBuffer != null && shaderProgram != null) {
			vertexBuffer.bind();
			shaderProgram.setDefaultUniforms(drawMode, RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix(), Minecraft.getInstance().getWindow());
			shaderProgram.apply();
		}
	}
*///? }
}
