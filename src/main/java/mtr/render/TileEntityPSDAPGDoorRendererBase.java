package mtr.render;

import org.lwjgl.opengl.GL11;

import mtr.tile.TileEntityPSDAPGDoorBase;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public abstract class TileEntityPSDAPGDoorRendererBase extends TileEntitySpecialRenderer<TileEntityPSDAPGDoorBase> {

	@Override
	public void render(TileEntityPSDAPGDoorBase te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.translate(x, y, z);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

		bufferBuilder.pos(1, 0, 0).color(128, 128, 128, 255).endVertex();
		bufferBuilder.pos(1, 1, 0).color(128, 128, 128, 255).endVertex();
		bufferBuilder.pos(0, 1, 0).color(128, 128, 128, 255).endVertex();
		bufferBuilder.pos(0, 0, 0).color(128, 128, 128, 255).endVertex();

		tessellator.draw();

		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}
}
