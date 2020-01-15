package mtr.render;

import mtr.tile.TileEntityOBAController;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityOBAControllerRenderer extends TileEntitySpecialRenderer<TileEntityOBAController> {

	@Override
	public void render(TileEntityOBAController te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (te.displayBlock != null && te.routes != null) {
			GlStateManager.pushMatrix();
			GlStateManager.pushAttrib();
			final FontRenderer fontRenderer = getFontRenderer();
			GlStateManager.translate(x + 0.5, y, z + 0.5);
			GlStateManager.rotate(-te.displayBlock.getHorizontalAngle(), 0, 1, 0);
			GlStateManager.translate(-0.5 - te.screenX, 1 + te.screenY, 0.59375);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
			GlStateManager.disableLighting();

			final int oneBlockWidth = te.screenWidth > 2 ? 256 / te.screenWidth : 128;
			GlStateManager.scale(1F / oneBlockWidth, -1F / oneBlockWidth, 1F / oneBlockWidth);
			GlStateManager.translate(8, 8, 0);

			int maxItems = (oneBlockWidth * te.screenHeight - 16) / 12;
			final String[] titles = te.title.split("\n");
			for (int i = 0; i < Math.min(titles.length, maxItems); i++) {
				final String title = fontRenderer.trimStringToWidth(titles[i], oneBlockWidth * te.screenWidth - 16);
				fontRenderer.drawString(title, oneBlockWidth / 2 * te.screenWidth - 8 - fontRenderer.getStringWidth(title) / 2, i * 12, 0xFFFFFF);
			}
			maxItems -= titles.length;
			GlStateManager.translate(0, 12 * titles.length, 0);

			for (int i = 0; i < Math.min(te.routes.length, maxItems); i++) {
				fontRenderer.drawString(fontRenderer.trimStringToWidth(te.routes[i], 30), 0, i * 12, 0xFFFFFF);
				fontRenderer.drawString(te.arrivals[i], oneBlockWidth * te.screenWidth - 16 - fontRenderer.getStringWidth(te.arrivals[i]), i * 12, 0xFFFFFF);
			}
			GlStateManager.translate(36, 0, 0);

			GlStateManager.scale(2 / 3D, 2 / 3D, 2 / 3D);
			for (int i = 0; i < Math.min(te.routes.length, maxItems); i++) {
				fontRenderer.drawString(fontRenderer.trimStringToWidth(te.destinations[i], oneBlockWidth * 3 * te.screenWidth / 2 - 24 - 54 - 54), 0, i * 18, 0xFFFFFF);
			}

			GlStateManager.scale(0.5, 0.5, 0.5);
			for (int i = 0; i < Math.min(te.routes.length, maxItems); i++) {
				fontRenderer.drawString(te.deviation[i], 0, i * 36 + 16, 0xFFFFFF);
				fontRenderer.drawString(te.vehicleIds[i], 66, i * 36 + 16, 0xFFFFFF);
			}

			GlStateManager.enableLighting();
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
		}
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}
}
