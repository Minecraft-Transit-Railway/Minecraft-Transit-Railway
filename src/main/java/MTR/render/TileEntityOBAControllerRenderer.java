package mtr.render;

import mtr.tile.TileEntityOBAController;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;

public class TileEntityOBAControllerRenderer extends TileEntitySpecialRenderer<TileEntityOBAController> {

	private static final int MAX_ARRIVALS = 20;

	@Override
	public void render(TileEntityOBAController te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (te.displayBlock != null) {
			GlStateManager.pushMatrix();
			GlStateManager.pushAttrib();
			final FontRenderer fontRenderer = getFontRenderer();
			final BlockPos pos = te.getPos();
			GlStateManager.translate(x + 0.5, y, z + 0.5);
			GlStateManager.rotate(-te.displayBlock.getHorizontalAngle(), 0, 1, 0);
			GlStateManager.translate(-0.4375, 1.9375, 0.59375);
			GlStateManager.scale(0.0078125, -0.0078125, 0.0078125);

			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
			GlStateManager.disableLighting();
			fontRenderer.drawString(fontRenderer.trimStringToWidth(te.title, 128 * 14 / 16), 0, 0, 0xFFFFFF);
			GlStateManager.translate(0, 12, 0);
			GlStateManager.scale(0.5, 0.5, 0.5);
			if (te.routes != null)
				for (int i = 0; i < Math.min(te.routes.length, MAX_ARRIVALS); i++) {
					fontRenderer.drawString(fontRenderer.trimStringToWidth(te.routes[i], 30), 0, i * 24 + 6, 0xFFFFFF);
					fontRenderer.drawString(fontRenderer.trimStringToWidth(te.destinations[i], 172 - 32), 36, i * 24, 0xFFFFFF);
					fontRenderer.drawString(te.statuses[i], 36, i * 24 + 12, 0xFFFFFF);
					fontRenderer.drawString(te.vehicleIds[i], 120, i * 24 + 12, 0xFFFFFF);
					fontRenderer.drawString(te.arrivals[i], 256 - 32 - fontRenderer.getStringWidth(te.arrivals[i]), i * 24 + 6, 0xFFFFFF);
				}
			GlStateManager.enableLighting();
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
		}
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}
}
