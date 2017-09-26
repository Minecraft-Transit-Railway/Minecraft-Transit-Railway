package MTR;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityRailBoosterRenderer extends TileEntitySpecialRenderer<TileEntityRailBoosterEntity> {

	public TileEntityRailBoosterRenderer() {
	}

	@Override
	public void renderTileEntityAt(TileEntityRailBoosterEntity te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		int meta = te.getBlockMetadata();
		int angle = (int) (-45D * (meta % 8) + 450D) % 360;
		boolean powered = meta >= 8;
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		FontRenderer var20 = getFontRenderer();
		String var18 = String.valueOf((powered ? te.speedBoost : te.speedSlow) / 10F);
		GlStateManager.translate(x + 0.5, y + 0.0626, z + 0.5);
		GlStateManager.scale(0.01, -0.01, 0.01);
		GlStateManager.rotate(90, 1, 0, 0);
		GlStateManager.rotate(-angle + 90, 0, 0, 1);
		GL11.glNormal3f(0, 1, 0);
		GlStateManager.depthMask(false);
		var20.drawString(var18, -var20.getStringWidth(var18) / 2, 0, 0xFFFFFF);
		GlStateManager.depthMask(true);
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}
}