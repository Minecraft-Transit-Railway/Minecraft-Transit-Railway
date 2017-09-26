package MTR;

import MTR.blocks.BlockPSDDoor;
import MTR.blocks.BlockPSDGlassEnd;
import MTR.client.ModelAPGDoorBottom;
import MTR.client.ModelAPGDoorTop;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityDoorRenderer extends TileEntitySpecialRenderer<TileEntityDoorEntity> {

	private final ModelAPGDoorBottom model1;
	private final ModelAPGDoorTop model2;

	public TileEntityDoorRenderer() {
		model1 = new ModelAPGDoorBottom();
		model2 = new ModelAPGDoorTop();
	}

	@Override
	public void renderTileEntityAt(TileEntityDoorEntity te, double x, double y, double z, float scale, int arg5) {
		int meta = te.getBlockMetadata();
		int facing = meta % 4;
		boolean side = (meta & 4) > 0;
		boolean top = (meta & 8) > 0;
		World worldIn = te.getWorld();
		BlockPos pos = te.getPos();
		if (worldIn.getBlockState(pos).getBlock() instanceof BlockPSDDoor)
			renderPSD(te, x, y, z, worldIn, pos, facing, side, top);
		else
			renderAPG(te, x, y, z, worldIn, pos, facing, side, top);
	}

	private void renderPSD(TileEntityDoorEntity te, double x, double y, double z, World worldIn, BlockPos pos,
			int facing, boolean side, boolean top) {
		Block block1 = worldIn.getBlockState(pos.north()).getBlock();
		Block block2 = worldIn.getBlockState(pos.east()).getBlock();
		Block block3 = worldIn.getBlockState(pos.south()).getBlock();
		Block block4 = worldIn.getBlockState(pos.west()).getBlock();
		boolean end = block1 instanceof BlockPSDGlassEnd || block2 instanceof BlockPSDGlassEnd
				|| block3 instanceof BlockPSDGlassEnd || block4 instanceof BlockPSDGlassEnd;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableLighting();
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldrenderer = tessellator.getBuffer();
		bindTexture(new ResourceLocation("mtr:textures/blocks/BlockPSDDoor" + (end ? "End" : "") + ".png"));
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		if (end)
			renderPSDDoorEnd(worldrenderer, facing, side, top, te.position);
		else
			renderPSDDoor(worldrenderer, facing, side, top, te.position);
		tessellator.draw();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}

	private void renderPSDDoor(VertexBuffer wr, int facing, boolean side, boolean top, int position) {
		switch (facing) {
		case 0:
			GlStateManager.translate(0, 0, position / (side ? 32F : -32F));
			// front
			wr.pos(0.875, 0, 1).tex(side ? 0 : 0.5, top ? 0.5 : 1).endVertex();
			wr.pos(0.875, 1, 1).tex(side ? 0 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos(0.875, 1, 0).tex(side ? 0.5 : 0, top ? 0 : 0.5).endVertex();
			wr.pos(0.875, 0, 0).tex(side ? 0.5 : 0, top ? 0.5 : 1).endVertex();
			// back
			wr.pos(1, 0, 0).tex(side ? 0.5 : 1, top ? 0.5 : 1).endVertex();
			wr.pos(1, 1, 0).tex(side ? 0.5 : 1, top ? 0 : 0.5).endVertex();
			wr.pos(1, 1, 1).tex(side ? 1 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos(1, 0, 1).tex(side ? 1 : 0.5, top ? 0.5 : 1).endVertex();
			// left
			wr.pos(0.875, 0, 0).tex(0.5625, 0.5).endVertex();
			wr.pos(0.875, 1, 0).tex(0.5625, 0).endVertex();
			wr.pos(1, 1, 0).tex(0.5, 0).endVertex();
			wr.pos(1, 0, 0).tex(0.5, 0.5).endVertex();
			// right
			wr.pos(1, 0, 1).tex(0.5625, 0.5).endVertex();
			wr.pos(1, 1, 1).tex(0.5625, 0).endVertex();
			wr.pos(0.875, 1, 1).tex(0.5, 0).endVertex();
			wr.pos(0.875, 0, 1).tex(0.5, 0.5).endVertex();
			break;
		case 1:
			GlStateManager.translate(position / (side ? -32F : 32F), 0, 0);
			// front
			wr.pos(0, 0, 0.875).tex(side ? 0 : 0.5, top ? 0.5 : 1).endVertex();
			wr.pos(0, 1, 0.875).tex(side ? 0 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos(1, 1, 0.875).tex(side ? 0.5 : 0, top ? 0 : 0.5).endVertex();
			wr.pos(1, 0, 0.875).tex(side ? 0.5 : 0, top ? 0.5 : 1).endVertex();
			// back
			wr.pos(1, 0, 1).tex(side ? 0.5 : 1, top ? 0.5 : 1).endVertex();
			wr.pos(1, 1, 1).tex(side ? 0.5 : 1, top ? 0 : 0.5).endVertex();
			wr.pos(0, 1, 1).tex(side ? 1 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos(0, 0, 1).tex(side ? 1 : 0.5, top ? 0.5 : 1).endVertex();
			// left
			wr.pos(1, 0, 0.875).tex(0.5625, 0.5).endVertex();
			wr.pos(1, 1, 0.875).tex(0.5625, 0).endVertex();
			wr.pos(1, 1, 1).tex(0.5, 0).endVertex();
			wr.pos(1, 0, 1).tex(0.5, 0.5).endVertex();
			// right
			wr.pos(0, 0, 1).tex(0.5625, 0.5).endVertex();
			wr.pos(0, 1, 1).tex(0.5625, 0).endVertex();
			wr.pos(0, 1, 0.875).tex(0.5, 0).endVertex();
			wr.pos(0, 0, 0.875).tex(0.5, 0.5).endVertex();
			break;
		case 2:
			GlStateManager.translate(0, 0, position / (side ? -32F : 32F));
			// front
			wr.pos(0.125, 0, 0).tex(side ? 0 : 0.5, top ? 0.5 : 1).endVertex();
			wr.pos(0.125, 1, 0).tex(side ? 0 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos(0.125, 1, 1).tex(side ? 0.5 : 0, top ? 0 : 0.5).endVertex();
			wr.pos(0.125, 0, 1).tex(side ? 0.5 : 0, top ? 0.5 : 1).endVertex();
			// back
			wr.pos(0, 0, 1).tex(side ? 0.5 : 1, top ? 0.5 : 1).endVertex();
			wr.pos(0, 1, 1).tex(side ? 0.5 : 1, top ? 0 : 0.5).endVertex();
			wr.pos(0, 1, 0).tex(side ? 1 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos(0, 0, 0).tex(side ? 1 : 0.5, top ? 0.5 : 1).endVertex();
			// left
			wr.pos(0, 0, 0).tex(0.5625, 0.5).endVertex();
			wr.pos(0, 1, 0).tex(0.5625, 0).endVertex();
			wr.pos(0.125, 1, 0).tex(0.5, 0).endVertex();
			wr.pos(0.125, 0, 0).tex(0.5, 0.5).endVertex();
			// right
			wr.pos(0.125, 0, 1).tex(0.5625, 0.5).endVertex();
			wr.pos(0.125, 1, 1).tex(0.5625, 0).endVertex();
			wr.pos(0, 1, 1).tex(0.5, 0).endVertex();
			wr.pos(0, 0, 1).tex(0.5, 0.5).endVertex();
			break;
		case 3:
			GlStateManager.translate(position / (side ? 32F : -32F), 0, 0);
			// front
			wr.pos(1, 0, 0.125).tex(side ? 0 : 0.5, top ? 0.5 : 1).endVertex();
			wr.pos(1, 1, 0.125).tex(side ? 0 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos(0, 1, 0.125).tex(side ? 0.5 : 0, top ? 0 : 0.5).endVertex();
			wr.pos(0, 0, 0.125).tex(side ? 0.5 : 0, top ? 0.5 : 1).endVertex();
			// back
			wr.pos(0, 0, 0).tex(side ? 0.5 : 1, top ? 0.5 : 1).endVertex();
			wr.pos(0, 1, 0).tex(side ? 0.5 : 1, top ? 0 : 0.5).endVertex();
			wr.pos(1, 1, 0).tex(side ? 1 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos(1, 0, 0).tex(side ? 1 : 0.5, top ? 0.5 : 1).endVertex();
			// left
			wr.pos(0, 0, 0.125).tex(0.5625, 0.5).endVertex();
			wr.pos(0, 1, 0.125).tex(0.5625, 0).endVertex();
			wr.pos(0, 1, 0).tex(0.5, 0).endVertex();
			wr.pos(0, 0, 0).tex(0.5, 0.5).endVertex();
			// right
			wr.pos(1, 0, 0).tex(0.5625, 0.5).endVertex();
			wr.pos(1, 1, 0).tex(0.5625, 0).endVertex();
			wr.pos(1, 1, 0.125).tex(0.5, 0).endVertex();
			wr.pos(1, 0, 0.125).tex(0.5, 0.5).endVertex();
			break;
		}
	}

	private void renderPSDDoorEnd(VertexBuffer wr, int facing, boolean side, boolean top, int position) {
		float p1 = position / (side ? 32F : -32F), p2 = position / (side ? 64F : -64F);
		switch (facing) {
		case 0:
			// front
			wr.pos(0.875, 0, (side ? 0.5 : 1) + p1).tex(side ? 0.25 : 0.5, top ? 0.5 : 1).endVertex();
			wr.pos(0.875, 1, (side ? 0.5 : 1) + p1).tex(side ? 0.25 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos(0.875, 1, (side ? 0 : 0.5) + p1).tex(side ? 0.5 : 0.25, top ? 0 : 0.5).endVertex();
			wr.pos(0.875, 0, (side ? 0 : 0.5) + p1).tex(side ? 0.5 : 0.25, top ? 0.5 : 1).endVertex();
			// back
			wr.pos(1, 0, (side ? 0 : 0.5) + p1).tex(side ? 0.5 : 0.75, top ? 0.5 : 1).endVertex();
			wr.pos(1, 1, (side ? 0 : 0.5) + p1).tex(side ? 0.5 : 0.75, top ? 0 : 0.5).endVertex();
			wr.pos(1, 1, (side ? 0.5 : 1) + p1).tex(side ? 0.75 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos(1, 0, (side ? 0.5 : 1) + p1).tex(side ? 0.75 : 0.5, top ? 0.5 : 1).endVertex();
			// left
			wr.pos(0.875, 0, (side ? 0 : 0.5) + p1).tex(0.5625, 0.5).endVertex();
			wr.pos(0.875, 1, (side ? 0 : 0.5) + p1).tex(0.5625, 0).endVertex();
			wr.pos(1, 1, (side ? 0 : 0.5) + p1).tex(0.5, 0).endVertex();
			wr.pos(1, 0, (side ? 0 : 0.5) + p1).tex(0.5, 0.5).endVertex();
			// right
			wr.pos(1, 0, (side ? 0.5 : 1) + p1).tex(0.5625, 0.5).endVertex();
			wr.pos(1, 1, (side ? 0.5 : 1) + p1).tex(0.5625, 0).endVertex();
			wr.pos(0.875, 1, (side ? 0.5 : 1) + p1).tex(0.5, 0).endVertex();
			wr.pos(0.875, 0, (side ? 0.5 : 1) + p1).tex(0.5, 0.5).endVertex();

			// front
			wr.pos(0.75, 0, (side ? 1 : 0.5) + p2).tex(side ? 0 : 0.25, top ? 0.5 : 1).endVertex();
			wr.pos(0.75, 1, (side ? 1 : 0.5) + p2).tex(side ? 0 : 0.25, top ? 0 : 0.5).endVertex();
			wr.pos(0.75, 1, (side ? 0.5 : 0) + p2).tex(side ? 0.25 : 0, top ? 0 : 0.5).endVertex();
			wr.pos(0.75, 0, (side ? 0.5 : 0) + p2).tex(side ? 0.25 : 0, top ? 0.5 : 1).endVertex();
			// back
			wr.pos(0.875, 0, (side ? 0.5 : 0) + p2).tex(side ? 0.75 : 1, top ? 0.5 : 1).endVertex();
			wr.pos(0.875, 1, (side ? 0.5 : 0) + p2).tex(side ? 0.75 : 1, top ? 0 : 0.5).endVertex();
			wr.pos(0.875, 1, (side ? 1 : 0.5) + p2).tex(side ? 1 : 0.75, top ? 0 : 0.5).endVertex();
			wr.pos(0.875, 0, (side ? 1 : 0.5) + p2).tex(side ? 1 : 0.75, top ? 0.5 : 1).endVertex();
			// left
			wr.pos(0.75, 0, (side ? 0.5 : 0) + p2).tex(side ? 0.0625 : 0.5625, 1).endVertex();
			wr.pos(0.75, 1, (side ? 0.5 : 0) + p2).tex(side ? 0.0625 : 0.5625, 0.9375).endVertex();
			wr.pos(0.875, 1, (side ? 0.5 : 0) + p2).tex(side ? 0 : 0.5, 0.9375).endVertex();
			wr.pos(0.875, 0, (side ? 0.5 : 0) + p2).tex(side ? 0 : 0.5, 1).endVertex();
			// right
			wr.pos(0.875, 0, (side ? 1 : 0.5) + p2).tex(side ? 0.5625 : 0.0625, 1).endVertex();
			wr.pos(0.875, 1, (side ? 1 : 0.5) + p2).tex(side ? 0.5625 : 0.0625, 0.9375).endVertex();
			wr.pos(0.75, 1, (side ? 1 : 0.5) + p2).tex(side ? 0.5 : 0, 0.9375).endVertex();
			wr.pos(0.75, 0, (side ? 1 : 0.5) + p2).tex(side ? 0.5 : 0, 1).endVertex();
			break;
		case 1:
			// front
			wr.pos((side ? 0.5 : 0) - p1, 0, 0.875).tex(side ? 0.25 : 0.5, top ? 0.5 : 1).endVertex();
			wr.pos((side ? 0.5 : 0) - p1, 1, 0.875).tex(side ? 0.25 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 1 : 0.5) - p1, 1, 0.875).tex(side ? 0.5 : 0.25, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 1 : 0.5) - p1, 0, 0.875).tex(side ? 0.5 : 0.25, top ? 0.5 : 1).endVertex();
			// back
			wr.pos((side ? 1 : 0.5) - p1, 0, 1).tex(side ? 0.5 : 0.75, top ? 0.5 : 1).endVertex();
			wr.pos((side ? 1 : 0.5) - p1, 1, 1).tex(side ? 0.5 : 0.75, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 0.5 : 0) - p1, 1, 1).tex(side ? 0.75 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 0.5 : 0) - p1, 0, 1).tex(side ? 0.75 : 0.5, top ? 0.5 : 1).endVertex();
			// left
			wr.pos((side ? 1 : 0.5) - p1, 0, 0.875).tex(0.5625, 0.5).endVertex();
			wr.pos((side ? 1 : 0.5) - p1, 1, 0.875).tex(0.5625, 0).endVertex();
			wr.pos((side ? 1 : 0.5) - p1, 1, 1).tex(0.5, 0).endVertex();
			wr.pos((side ? 1 : 0.5) - p1, 0, 1).tex(0.5, 0.5).endVertex();
			// right
			wr.pos((side ? 0.5 : 0) - p1, 0, 1).tex(0.5625, 0.5).endVertex();
			wr.pos((side ? 0.5 : 0) - p1, 1, 1).tex(0.5625, 0).endVertex();
			wr.pos((side ? 0.5 : 0) - p1, 1, 0.875).tex(0.5, 0).endVertex();
			wr.pos((side ? 0.5 : 0) - p1, 0, 0.875).tex(0.5, 0.5).endVertex();

			// front
			wr.pos((side ? 0 : 0.5) - p2, 0, 0.75).tex(side ? 0 : 0.25, top ? 0.5 : 1).endVertex();
			wr.pos((side ? 0 : 0.5) - p2, 1, 0.75).tex(side ? 0 : 0.25, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 0.5 : 1) - p2, 1, 0.75).tex(side ? 0.25 : 0, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 0.5 : 1) - p2, 0, 0.75).tex(side ? 0.25 : 0, top ? 0.5 : 1).endVertex();
			// back
			wr.pos((side ? 0.5 : 1) - p2, 0, 0.875).tex(side ? 0.75 : 1, top ? 0.5 : 1).endVertex();
			wr.pos((side ? 0.5 : 1) - p2, 1, 0.875).tex(side ? 0.75 : 1, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 0 : 0.5) - p2, 1, 0.875).tex(side ? 1 : 0.75, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 0 : 0.5) - p2, 0, 0.875).tex(side ? 1 : 0.75, top ? 0.5 : 1).endVertex();
			// left
			wr.pos((side ? 0.5 : 1) - p2, 0, 0.75).tex(side ? 0.0625 : 0.5625, 1).endVertex();
			wr.pos((side ? 0.5 : 1) - p2, 1, 0.75).tex(side ? 0.0625 : 0.5625, 0.9375).endVertex();
			wr.pos((side ? 0.5 : 1) - p2, 1, 0.875).tex(side ? 0 : 0.5, 0.9375).endVertex();
			wr.pos((side ? 0.5 : 1) - p2, 0, 0.875).tex(side ? 0 : 0.5, 1).endVertex();
			// right
			wr.pos((side ? 0 : 0.5) - p2, 0, 0.875).tex(side ? 0.5625 : 0.0625, 1).endVertex();
			wr.pos((side ? 0 : 0.5) - p2, 1, 0.875).tex(side ? 0.5625 : 0.0625, 0.9375).endVertex();
			wr.pos((side ? 0 : 0.5) - p2, 1, 0.75).tex(side ? 0.5 : 0, 0.9375).endVertex();
			wr.pos((side ? 0 : 0.5) - p2, 0, 0.75).tex(side ? 0.5 : 0, 1).endVertex();
			break;
		case 2:
			// front
			wr.pos(0.125, 0, (side ? 0.5 : 0) - p1).tex(side ? 0.25 : 0.5, top ? 0.5 : 1).endVertex();
			wr.pos(0.125, 1, (side ? 0.5 : 0) - p1).tex(side ? 0.25 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos(0.125, 1, (side ? 1 : 0.5) - p1).tex(side ? 0.5 : 0.25, top ? 0 : 0.5).endVertex();
			wr.pos(0.125, 0, (side ? 1 : 0.5) - p1).tex(side ? 0.5 : 0.25, top ? 0.5 : 1).endVertex();
			// back
			wr.pos(0, 0, (side ? 1 : 0.5) - p1).tex(side ? 0.5 : 0.75, top ? 0.5 : 1).endVertex();
			wr.pos(0, 1, (side ? 1 : 0.5) - p1).tex(side ? 0.5 : 0.75, top ? 0 : 0.5).endVertex();
			wr.pos(0, 1, (side ? 0.5 : 0) - p1).tex(side ? 0.75 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos(0, 0, (side ? 0.5 : 0) - p1).tex(side ? 0.75 : 0.5, top ? 0.5 : 1).endVertex();
			// left
			wr.pos(0.125, 0, (side ? 1 : 0.5) - p1).tex(0.5625, 0.5).endVertex();
			wr.pos(0.125, 1, (side ? 1 : 0.5) - p1).tex(0.5625, 0).endVertex();
			wr.pos(0, 1, (side ? 1 : 0.5) - p1).tex(0.5, 0).endVertex();
			wr.pos(0, 0, (side ? 1 : 0.5) - p1).tex(0.5, 0.5).endVertex();
			// right
			wr.pos(0, 0, (side ? 0.5 : 0) - p1).tex(0.5625, 0.5).endVertex();
			wr.pos(0, 1, (side ? 0.5 : 0) - p1).tex(0.5625, 0).endVertex();
			wr.pos(0.125, 1, (side ? 0.5 : 0) - p1).tex(0.5, 0).endVertex();
			wr.pos(0.125, 0, (side ? 0.5 : 0) - p1).tex(0.5, 0.5).endVertex();

			// front
			wr.pos(0.25, 0, (side ? 0 : 0.5) - p2).tex(side ? 0 : 0.25, top ? 0.5 : 1).endVertex();
			wr.pos(0.25, 1, (side ? 0 : 0.5) - p2).tex(side ? 0 : 0.25, top ? 0 : 0.5).endVertex();
			wr.pos(0.25, 1, (side ? 0.5 : 1) - p2).tex(side ? 0.25 : 0, top ? 0 : 0.5).endVertex();
			wr.pos(0.25, 0, (side ? 0.5 : 1) - p2).tex(side ? 0.25 : 0, top ? 0.5 : 1).endVertex();
			// back
			wr.pos(0.125, 0, (side ? 0.5 : 1) - p2).tex(side ? 0.75 : 1, top ? 0.5 : 1).endVertex();
			wr.pos(0.125, 1, (side ? 0.5 : 1) - p2).tex(side ? 0.75 : 1, top ? 0 : 0.5).endVertex();
			wr.pos(0.125, 1, (side ? 0 : 0.5) - p2).tex(side ? 1 : 0.75, top ? 0 : 0.5).endVertex();
			wr.pos(0.125, 0, (side ? 0 : 0.5) - p2).tex(side ? 1 : 0.75, top ? 0.5 : 1).endVertex();
			// left
			wr.pos(0.25, 0, (side ? 0.5 : 1) - p2).tex(side ? 0.0625 : 0.5625, 1).endVertex();
			wr.pos(0.25, 1, (side ? 0.5 : 1) - p2).tex(side ? 0.0625 : 0.5625, 0.9375).endVertex();
			wr.pos(0.125, 1, (side ? 0.5 : 1) - p2).tex(side ? 0 : 0.5, 0.9375).endVertex();
			wr.pos(0.125, 0, (side ? 0.5 : 1) - p2).tex(side ? 0 : 0.5, 1).endVertex();
			// right
			wr.pos(0.125, 0, (side ? 0 : 0.5) - p2).tex(side ? 0.5625 : 0.0625, 1).endVertex();
			wr.pos(0.125, 1, (side ? 0 : 0.5) - p2).tex(side ? 0.5625 : 0.0625, 0.9375).endVertex();
			wr.pos(0.25, 1, (side ? 0 : 0.5) - p2).tex(side ? 0.5 : 0, 0.9375).endVertex();
			wr.pos(0.25, 0, (side ? 0 : 0.5) - p2).tex(side ? 0.5 : 0, 1).endVertex();
			break;
		case 3:
			// front
			wr.pos((side ? 0.5 : 1) + p1, 0, 0.125).tex(side ? 0.25 : 0.5, top ? 0.5 : 1).endVertex();
			wr.pos((side ? 0.5 : 1) + p1, 1, 0.125).tex(side ? 0.25 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 0 : 0.5) + p1, 1, 0.125).tex(side ? 0.5 : 0.25, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 0 : 0.5) + p1, 0, 0.125).tex(side ? 0.5 : 0.25, top ? 0.5 : 1).endVertex();
			// back
			wr.pos((side ? 0 : 0.5) + p1, 0, 0).tex(side ? 0.5 : 0.75, top ? 0.5 : 1).endVertex();
			wr.pos((side ? 0 : 0.5) + p1, 1, 0).tex(side ? 0.5 : 0.75, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 0.5 : 1) + p1, 1, 0).tex(side ? 0.75 : 0.5, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 0.5 : 1) + p1, 0, 0).tex(side ? 0.75 : 0.5, top ? 0.5 : 1).endVertex();
			// left
			wr.pos((side ? 0 : 0.5) + p1, 0, 0.125).tex(0.5625, 0.5).endVertex();
			wr.pos((side ? 0 : 0.5) + p1, 1, 0.125).tex(0.5625, 0).endVertex();
			wr.pos((side ? 0 : 0.5) + p1, 1, 0).tex(0.5, 0).endVertex();
			wr.pos((side ? 0 : 0.5) + p1, 0, 0).tex(0.5, 0.5).endVertex();
			// right
			wr.pos((side ? 0.5 : 1) + p1, 0, 0).tex(0.5625, 0.5).endVertex();
			wr.pos((side ? 0.5 : 1) + p1, 1, 0).tex(0.5625, 0).endVertex();
			wr.pos((side ? 0.5 : 1) + p1, 1, 0.125).tex(0.5, 0).endVertex();
			wr.pos((side ? 0.5 : 1) + p1, 0, 0.125).tex(0.5, 0.5).endVertex();

			// front
			wr.pos((side ? 1 : 0.5) + p2, 0, 0.25).tex(side ? 0 : 0.25, top ? 0.5 : 1).endVertex();
			wr.pos((side ? 1 : 0.5) + p2, 1, 0.25).tex(side ? 0 : 0.25, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 0.5 : 0) + p2, 1, 0.25).tex(side ? 0.25 : 0, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 0.5 : 0) + p2, 0, 0.25).tex(side ? 0.25 : 0, top ? 0.5 : 1).endVertex();
			// back
			wr.pos((side ? 0.5 : 0) + p2, 0, 0.125).tex(side ? 0.75 : 1, top ? 0.5 : 1).endVertex();
			wr.pos((side ? 0.5 : 0) + p2, 1, 0.125).tex(side ? 0.75 : 1, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 1 : 0.5) + p2, 1, 0.125).tex(side ? 1 : 0.75, top ? 0 : 0.5).endVertex();
			wr.pos((side ? 1 : 0.5) + p2, 0, 0.125).tex(side ? 1 : 0.75, top ? 0.5 : 1).endVertex();
			// left
			wr.pos((side ? 0.5 : 0) + p2, 0, 0.25).tex(side ? 0.0625 : 0.5625, 1).endVertex();
			wr.pos((side ? 0.5 : 0) + p2, 1, 0.25).tex(side ? 0.0625 : 0.5625, 0.9375).endVertex();
			wr.pos((side ? 0.5 : 0) + p2, 1, 0.125).tex(side ? 0 : 0.5, 0.9375).endVertex();
			wr.pos((side ? 0.5 : 0) + p2, 0, 0.125).tex(side ? 0 : 0.5, 1).endVertex();
			// right
			wr.pos((side ? 1 : 0.5) + p2, 0, 0.125).tex(side ? 0.5625 : 0.0625, 1).endVertex();
			wr.pos((side ? 1 : 0.5) + p2, 1, 0.125).tex(side ? 0.5625 : 0.0625, 0.9375).endVertex();
			wr.pos((side ? 1 : 0.5) + p2, 1, 0.25).tex(side ? 0.5 : 0, 0.9375).endVertex();
			wr.pos((side ? 1 : 0.5) + p2, 0, 0.25).tex(side ? 0.5 : 0, 1).endVertex();
			break;
		}
	}

	private void renderAPG(TileEntityDoorEntity te, double x, double y, double z, World worldIn, BlockPos pos,
			int facing, boolean side, boolean top) {
		ResourceLocation textures = new ResourceLocation(
				"MTR:textures/blocks/BlockAPGDoor" + (top ? "Top" : "Bottom") + (side ? "R" : "L") + ".png");
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		GlStateManager.translate((float) x + 0.5, (float) y + 1.5, (float) z + 0.5);
		GlStateManager.rotate(180F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(te.getBlockMetadata() * 90 - 90, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(te.position / (side ? 32F : -32F), 0, 0);
		if (top)
			model2.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		else
			model1.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}
}