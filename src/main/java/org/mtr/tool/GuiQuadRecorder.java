package org.mtr.tool;

//? if >= 26.1 {

/*import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.TextureSetup;
import org.mtr.mixin.GuiRenderStateAccessor;

// Collects the vertices this mod's drawing produces and hands each finished quad to the interface
// renderer.
//
// From 26.1 a screen records what it wants drawn rather than drawing it, so writing into a vertex
// consumer taken from the world's buffer source, which is what the drawing code does and what
// worked while screens drew immediately, puts the vertices somewhere that is never drawn.
// Standing in as that consumer leaves every drawing call site unchanged: the quads arrive here
// instead and are recorded.
//
// The drawing code writes each vertex as a position, then a colour, then a texture coordinate if
// the quad has one, and emits four vertices per quad in one run, never leaving one half finished
// because it culls whole quads before writing any of their vertices. A quad is therefore complete
// at the colour of its fourth vertex, or at the texture coordinate when there is one, which is why
// the caller says up front which it will be rather than this guessing from what arrives.
public class GuiQuadRecorder implements VertexConsumer {

	private final GuiGraphics context;
	private final RenderPipeline pipeline;
	private final TextureSetup textureSetup;
	private final boolean textured;

	private final float[] positions = new float[8];
	private final int[] colors = new int[4];
	private final float[] uvs = new float[8];
	private int vertexCount;

	public GuiQuadRecorder(GuiGraphics context, RenderPipeline pipeline, TextureSetup textureSetup, boolean textured) {
		this.context = context;
		this.pipeline = pipeline;
		this.textureSetup = textureSetup;
		this.textured = textured;
	}

	@Override
	public VertexConsumer addVertex(float x, float y, float z) {
		positions[vertexCount * 2] = x;
		positions[vertexCount * 2 + 1] = y;
		// Depth is dropped. The interface is ordered by the sequence things are recorded in now
		// rather than by a third axis, so carrying it would say nothing.
		return this;
	}

	@Override
	public VertexConsumer setColor(int color) {
		colors[vertexCount] = color;
		if (!textured) {
			finishVertex();
		}
		return this;
	}

	@Override
	public VertexConsumer setColor(int red, int green, int blue, int alpha) {
		return setColor((alpha << 24) | (red << 16) | (green << 8) | blue);
	}

	@Override
	public VertexConsumer setUv(float u, float v) {
		uvs[vertexCount * 2] = u;
		uvs[vertexCount * 2 + 1] = v;
		if (textured) {
			finishVertex();
		}
		return this;
	}

	private void finishVertex() {
		vertexCount++;
		if (vertexCount == 4) {
			vertexCount = 0;
			((GuiRenderStateAccessor) context).getGuiRenderState().addGuiElement(new GuiQuadRenderState(
				pipeline,
				textureSetup,
				GuiHelper.getGuiScissor(),
				positions.clone(),
				colors.clone(),
				textured ? uvs.clone() : null
			));
		}
	}

	// The drawing this stands in for asks for none of the following.

	@Override
	public VertexConsumer setUv1(int u, int v) {
		return this;
	}

	@Override
	public VertexConsumer setUv2(int u, int v) {
		return this;
	}

	@Override
	public VertexConsumer setNormal(float x, float y, float z) {
		return this;
	}

	@Override
	public VertexConsumer setLineWidth(float width) {
		return this;
	}
}

*///? }
