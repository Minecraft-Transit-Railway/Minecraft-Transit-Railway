package org.mtr.tool;

//? if >= 26.1 {

/*import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import org.jspecify.annotations.Nullable;
import org.joml.Matrix3x2f;

// One quad handed to the interface renderer to draw later.
//
// From 26.1 a screen records what it wants drawn instead of drawing it, and geometry that is not
// a rectangle, a sprite or a glyph is recorded by handing over one of these. Everything this mod
// draws through Drawing arrives here.
//
// The corners are already in screen coordinates: Drawing multiplies them by its own matrix before
// it gets this far, which is why the pose given to the renderer is the identity. Keeping that
// arrangement means the quads may be rotated or sheared, which the rectangle state vanilla offers
// cannot express.
public final class GuiQuadRenderState implements GuiElementRenderState {

	private final RenderPipeline pipeline;
	private final TextureSetup textureSetup;
	@Nullable
	private final ScreenRectangle scissorArea;
	private final ScreenRectangle bounds;
	private final float[] positions;
	private final int[] colors;
	private final float[] uvs;

	private static final Matrix3x2f IDENTITY = new Matrix3x2f();

	public GuiQuadRenderState(RenderPipeline pipeline, TextureSetup textureSetup, @Nullable ScreenRectangle scissorArea, float[] positions, int[] colors, @Nullable float[] uvs) {
		this.pipeline = pipeline;
		this.textureSetup = textureSetup;
		this.scissorArea = scissorArea;
		this.positions = positions;
		this.colors = colors;
		this.uvs = uvs;

		float minX = positions[0];
		float minY = positions[1];
		float maxX = positions[0];
		float maxY = positions[1];
		for (int i = 2; i < positions.length; i += 2) {
			minX = Math.min(minX, positions[i]);
			minY = Math.min(minY, positions[i + 1]);
			maxX = Math.max(maxX, positions[i]);
			maxY = Math.max(maxY, positions[i + 1]);
		}
		// The renderer sorts and culls by this, so it has to cover the quad rather than describe it
		// exactly; a rotated quad is reported by the box around it.
		bounds = new ScreenRectangle((int) Math.floor(minX), (int) Math.floor(minY), (int) Math.ceil(maxX - minX), (int) Math.ceil(maxY - minY));
	}

	@Override
	public void buildVertices(VertexConsumer vertexConsumer) {
		for (int i = 0; i < 4; i++) {
			final VertexConsumer vertex = vertexConsumer.addVertexWith2DPose(IDENTITY, positions[i * 2], positions[i * 2 + 1]);
			if (uvs != null) {
				vertex.setUv(uvs[i * 2], uvs[i * 2 + 1]);
			}
			vertex.setColor(colors[i]);
		}
	}

	@Override
	public RenderPipeline pipeline() {
		return pipeline;
	}

	@Override
	public TextureSetup textureSetup() {
		return textureSetup;
	}

	@Override
	@Nullable
	public ScreenRectangle scissorArea() {
		return scissorArea;
	}

	@Override
	public ScreenRectangle bounds() {
		return bounds;
	}
}

*///? }
