package MTR;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.I18n;

public class TileEntityStationNameRenderer extends TileEntitySpecialRenderer<TileEntityStationNameEntity> {

	public TileEntityStationNameRenderer() {
	}

	@Override
	public void renderTileEntityAt(TileEntityStationNameEntity te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		int meta = te.getBlockMetadata();
		int facing = meta % 4;
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		String chinese = I18n.format("station." + te.station + "c", new Object[0]),
				english = I18n.format("station." + te.station, new Object[0]);
		FontRenderer var20 = getFontRenderer();
		GlStateManager.translate(x + 0.5, y + 1, z + 0.5);
		GlStateManager.rotate(-facing * 90 - 90, 0, 1, 0);
		GlStateManager.translate(0, 0, -0.485);
		GlStateManager.scale(0.03125, -0.03125, 0.03125);
		GlStateManager.depthMask(false);
		GL11.glNormal3f(0, 0, 0.01F);
		var20.drawString(chinese, -var20.getStringWidth(chinese) / 2, 0, 0xFFFFFF);
		GlStateManager.translate(0, 0, 0.5);
		GL11.glNormal3f(0, 0, 1);
		var20.drawString(chinese, -var20.getStringWidth(chinese) / 2, 0, 0xFFFFFF);
		GlStateManager.translate(0, 10, -0.5);
		GlStateManager.scale(0.5, 0.5, 0.5);
		GL11.glNormal3f(0, 0, 0.01F);
		var20.drawString(english, -var20.getStringWidth(english) / 2, 0, 0xFFFFFF);
		GlStateManager.translate(0, 0, 1);
		GL11.glNormal3f(0, 0, 1);
		var20.drawString(english, -var20.getStringWidth(english) / 2, 0, 0xFFFFFF);
		GlStateManager.depthMask(true);
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}
}