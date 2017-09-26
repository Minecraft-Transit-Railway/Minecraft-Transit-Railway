package MTR;

import MTR.blocks.BlockPSD;
import MTR.blocks.BlockPSDDoor;
import MTR.blocks.BlockPSDDoorClosed;
import MTR.blocks.BlockPSDGlassVeryEnd;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class TileEntityPSDTopRenderer extends TileEntitySpecialRenderer<TileEntityPSDTopEntity> {

	@Override
	public void renderTileEntityAt(TileEntityPSDTopEntity te, double x, double y, double z, float scale, int arg5) {
		IBlockState stateBelow = getWorld().getBlockState(te.getPos().down());
		if (stateBelow.getBlock() instanceof BlockPSDDoor || stateBelow.getBlock() instanceof BlockPSDDoorClosed) {
			boolean side = stateBelow.getValue(BlockPSD.SIDE);
			if (te.arrow == 1 ^ side) {
				int color = te.color;
				int facing = te.getBlockMetadata();
				if (color > 0) {
					int bound = te.bound;
					GlStateManager.pushMatrix();
					GlStateManager.pushAttrib();
					String chinese = "", english = "";
					chinese = I18n.format("gui.toc", new Object[0]) + I18n
							.format("destination." + String.valueOf(te.color * 4 - 4 + bound) + "c", new Object[0]);
					english = I18n.format("gui.to", new Object[0]) + " "
							+ I18n.format("destination." + String.valueOf(te.color * 4 - 4 + bound), new Object[0]);
					FontRenderer var20 = getFontRenderer();
					GlStateManager.translate(x + 0.5, y + 0.4375, z + 0.5);
					GlStateManager.rotate(-facing * 90 - 90, 0, 1, 0);
					GlStateManager.translate(side ? -0.5 : 0.5, 0, -0.124);
					GlStateManager.scale(0.01953125, -0.01953125, 0.01953125);
					int chineseWidth = var20.getStringWidth(chinese);
					int englishWidth = var20.getStringWidth(english);
					GlStateManager.depthMask(false);
					if (chineseWidth > 50)
						GlStateManager.scale(50F / chineseWidth, 1, 1);
					var20.drawString(chinese, side ? 0 : -chineseWidth, 0, 0x000000);
					if (chineseWidth > 50)
						GlStateManager.scale(chineseWidth / 50F, 1, 1);
					GlStateManager.translate(0, 9, 0);
					GlStateManager.scale(0.4, 0.4, 0.4);
					if (englishWidth > 125)
						GlStateManager.scale(125F / englishWidth, 1, 1);
					var20.drawString(english, side ? 0 : -englishWidth, 0, 0x000000);
					GlStateManager.depthMask(true);
					GlStateManager.popAttrib();
					GlStateManager.popMatrix();
				}
			}
		}
		if (stateBelow.getBlock() instanceof BlockPSDGlassVeryEnd) {
			GlStateManager.pushMatrix();
			GlStateManager.pushAttrib();
			GlStateManager.translate(x, y, z);
			renderWhiteEnd(te.getBlockMetadata(), stateBelow.getValue(BlockPSDGlassVeryEnd.SIDE));
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
		}
	}

	private void renderWhiteEnd(int facing, boolean side) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		bindTexture(new ResourceLocation("mtr:textures/blocks/BlockPSDTopBody.png"));
		final double a = Math.sqrt(0.001953125);
		final double x = 0.0625 / (Math.sin(Math.PI / 8D) + Math.cos(Math.PI / 8D));
		final double b1 = x * Math.sin(Math.PI / 8D), b2 = (0.0625 - b1) * Math.cos(Math.PI / 4D);
		switch (facing) {
		case 0:
			// bottom
			worldrenderer.pos(side ? 0 : 1, 0, 1).tex(1, 0.0625).endVertex();
			worldrenderer.pos(side ? -b2 : 1 - b2, b1, side ? 1 - b2 : 1 + b2).tex(1, 0).endVertex();
			worldrenderer.pos(side ? 1 - b2 : -b2, b1, side ? -b2 : b2).tex(0, 0).endVertex();
			worldrenderer.pos(side ? 1 : 0, 0, 0).tex(0, 0.0625).endVertex();
			// top
			worldrenderer.pos(side ? -b2 : 1 - b2, b1, side ? 1 - b2 : 1 + b2).tex(1, 0.0625).endVertex();
			worldrenderer.pos(side ? -a : 1 - a, 0.0625, side ? 1 - a : 1 + a).tex(1, 0).endVertex();
			worldrenderer.pos(side ? 1 - a : -a, 0.0625, side ? -a : a).tex(0, 0).endVertex();
			worldrenderer.pos(side ? 1 - b2 : -b2, b1, side ? -b2 : b2).tex(0, 0.0625).endVertex();
			break;
		case 1:
			// bottom
			worldrenderer.pos(0, 0, side ? 0 : 1).tex(1, 0.0625).endVertex();
			worldrenderer.pos(side ? b2 : -b2, b1, side ? -b2 : 1 - b2).tex(1, 0).endVertex();
			worldrenderer.pos(side ? 1 + b2 : 1 - b2, b1, side ? 1 - b2 : -b2).tex(0, 0).endVertex();
			worldrenderer.pos(1, 0, side ? 1 : 0).tex(0, 0.0625).endVertex();
			// top
			worldrenderer.pos(side ? b2 : -b2, b1, side ? -b2 : 1 - b2).tex(1, 0.0625).endVertex();
			worldrenderer.pos(side ? a : -a, 0.0625, side ? -a : 1 - a).tex(1, 0).endVertex();
			worldrenderer.pos(side ? 1 + a : 1 - a, 0.0625, side ? 1 - a : -a).tex(0, 0).endVertex();
			worldrenderer.pos(side ? 1 + b2 : 1 - b2, b1, side ? 1 - b2 : -b2).tex(0, 0.0625).endVertex();
			break;
		case 2:
			// bottom
			worldrenderer.pos(side ? 1 : 0, 0, 0).tex(1, 0.0625).endVertex();
			worldrenderer.pos(side ? 1 + b2 : b2, b1, side ? b2 : -b2).tex(1, 0).endVertex();
			worldrenderer.pos(side ? b2 : 1 + b2, b1, side ? 1 + b2 : 1 - b2).tex(0, 0).endVertex();
			worldrenderer.pos(side ? 0 : 1, 0, 1).tex(0, 0.0625).endVertex();
			// top
			worldrenderer.pos(side ? 1 + b2 : b2, b1, side ? b2 : -b2).tex(1, 0.0625).endVertex();
			worldrenderer.pos(side ? 1 + a : a, 0.0625, side ? a : -a).tex(1, 0).endVertex();
			worldrenderer.pos(side ? a : 1 + a, 0.0625, side ? 1 + a : 1 - a).tex(0, 0).endVertex();
			worldrenderer.pos(side ? b2 : 1 + b2, b1, side ? 1 + b2 : 1 - b2).tex(0, 0.0625).endVertex();
			break;
		case 3:
			// bottom
			worldrenderer.pos(1, 0, side ? 1 : 0).tex(1, 0.0625).endVertex();
			worldrenderer.pos(side ? 1 - b2 : 1 + b2, b1, side ? 1 + b2 : b2).tex(1, 0).endVertex();
			worldrenderer.pos(side ? -b2 : b2, b1, side ? b2 : 1 + b2).tex(0, 0).endVertex();
			worldrenderer.pos(0, 0, side ? 0 : 1).tex(0, 0.0625).endVertex();
			// top
			worldrenderer.pos(side ? 1 - b2 : 1 + b2, b1, side ? 1 + b2 : b2).tex(1, 0.0625).endVertex();
			worldrenderer.pos(side ? 1 - a : 1 + a, 0.0625, side ? 1 + a : a).tex(1, 0).endVertex();
			worldrenderer.pos(side ? -a : a, 0.0625, side ? a : 1 + a).tex(0, 0).endVertex();
			worldrenderer.pos(side ? -b2 : b2, b1, side ? b2 : 1 + b2).tex(0, 0.0625).endVertex();
			break;
		}
		tessellator.draw();
	}
}