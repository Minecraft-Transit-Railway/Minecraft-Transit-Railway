package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TileEntityRailRenderer extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity te2, double x, double y, double z, float partialTicks, int destroyStage) {
		World worldIn = te2.getWorld();
		BlockPos pos = te2.getPos();
		IBlockState state1, state2;
		for (int a = -1; a <= 1; a++)
			for (int b = -1; b <= 1; b++) {
				state1 = worldIn.getBlockState(pos.add(a, 0, b));
				state2 = worldIn.getBlockState(pos.add(-a, 0, -b));
				if (state1.getBlock() instanceof BlockRailDummy && state2.getBlock() instanceof BlockRailBase2) {
					state1 = worldIn.getBlockState(pos.add(b, 0, -a));
					state2 = worldIn.getBlockState(pos.add(-b, 0, a));
					double i = a != 0 && b != 0 ? Math.sqrt(2) / 2D : 1;
					int xShift = 0, zShift = 0;
					int radius = 0;
					if (state1.getBlock() instanceof BlockRailCurved) {
						radius = state1.getValue(BlockRailCurved.ROTATION) * 16 + te2.getBlockMetadata();
						xShift = (int) Math.round(b * i * radius);
						zShift = (int) Math.round(-a * i * radius);
					}
					if (state2.getBlock() instanceof BlockRailCurved) {
						radius = state2.getValue(BlockRailCurved.ROTATION) * 16 + te2.getBlockMetadata();
						xShift = (int) Math.round(-b * i * radius);
						zShift = (int) Math.round(a * i * radius);
					}
					renderRail(x, y, z, radius, xShift, zShift);
				}
			}
	}

	private void renderRail(double x, double y, double z, int radius, int xShift, int zShift) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.translate(x + xShift + 0.5, y, z + zShift + 0.5);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		bindTexture(new ResourceLocation("mtr:textures/blocks/BlockRailNormal.png"));
		double interval = 1D / radius;
		for (double a = 0; a < Math.PI * 2; a += interval) {
			double x1 = (radius + 0.5) * Math.sin(a), z1 = (radius + 0.5) * Math.cos(a);
			double x2 = (radius - 0.5) * Math.sin(a), z2 = (radius - 0.5) * Math.cos(a);
			double x3 = (radius + 0.5) * Math.sin(a + interval), z3 = (radius + 0.5) * Math.cos(a + interval);
			double x4 = (radius - 0.5) * Math.sin(a + interval), z4 = (radius - 0.5) * Math.cos(a + interval);
			worldrenderer.pos(x1, 0.0625, z1).tex(1, 0).endVertex();
			worldrenderer.pos(x3, 0.0625, z3).tex(1, 1).endVertex();
			worldrenderer.pos(x4, 0.0625, z4).tex(0, 1).endVertex();
			worldrenderer.pos(x2, 0.0625, z2).tex(0, 0).endVertex();
		}
		tessellator.draw();
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}
}