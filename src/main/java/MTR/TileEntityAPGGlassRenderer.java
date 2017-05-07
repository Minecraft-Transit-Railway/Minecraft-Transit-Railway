package MTR;

import MTR.blocks.BlockAPGGlassBottom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class TileEntityAPGGlassRenderer extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity te2, double x, double y, double z, float scale, int arg5) {
		TileEntityAPGGlassEntity te = (TileEntityAPGGlassEntity) te2;
		IBlockState stateBelow = getWorld().getBlockState(te2.getPos().down());
		if (stateBelow.getBlock() instanceof BlockAPGGlassBottom) {
			boolean side = stateBelow.getValue(BlockAPGGlassBottom.SIDE);
			int facing = te2.getBlockMetadata();
			if (!getWarning(EnumFacing.getHorizontal(facing), te2.getPos(), side))
				if (te.arrow == 1 ^ side) {
					int color = te.color;
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
						GlStateManager.translate(x + 0.5, y + 0.46875, z + 0.5);
						GlStateManager.rotate(-facing * 90 - 90, 0, 1, 0);
						GlStateManager.translate(side ? -0.5 : 0.5, 0, -0.249);
						GlStateManager.scale(0.009765625, -0.009765625, 0.009765625);
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
	}

	private boolean getWarning(EnumFacing facing, BlockPos pos, boolean side) {
		int warningX, warningZ;
		BlockPos pos2 = pos;
		switch (facing) {
		case NORTH:
			pos2 = pos.add(0, 0, 1);
			break;
		case EAST:
			pos2 = pos.add(-1, 0, 0);
			break;
		case SOUTH:
			pos2 = pos.add(0, 0, -1);
			break;
		case WEST:
			pos2 = pos.add(1, 0, 0);
			break;
		default:
		}
		warningX = ((side ? pos2 : pos).getX() % 8 + 8) % 8;
		warningZ = ((side ? pos2 : pos).getZ() % 8 + 8) % 8;
		return warningX < 4 ^ warningZ < 4;
	}
}