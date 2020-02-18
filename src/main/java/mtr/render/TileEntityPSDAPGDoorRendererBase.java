package mtr.render;

import mtr.block.BlockPSDAPGBase.EnumPSDAPGSide;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPSDAPGDoorBase.EnumPSDAPGDoor;
import mtr.tile.TileEntityPSDAPGDoorBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileEntityPSDAPGDoorRendererBase extends TileEntitySpecialRenderer<TileEntityPSDAPGDoorBase> {

	private BlockRendererDispatcher blockRenderer;

	@Override
	public void render(TileEntityPSDAPGDoorBase te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (blockRenderer == null)
			blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();

		final BlockPos pos = te.getPos();
		final World world = getWorld();
		IBlockState state = world.getBlockState(pos);
		final Block block = state.getBlock();
		if (!(block instanceof BlockPSDAPGDoorBase) || !((BlockPSDAPGDoorBase) block).isOpen(world, pos))
			return;

		final EnumPSDAPGDoor doorState = ((BlockPSDAPGDoorBase) block).getActualDoorState(world, state, pos);
		final boolean isTop = ((BlockPSDAPGDoorBase) block).isTop(world, pos);
		state = state.withProperty(BlockPSDAPGDoorBase.SIDE_DOOR, doorState).withProperty(BlockPSDAPGDoorBase.TOP, isTop);

		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferBuilder = tessellator.getBuffer();

		RenderHelper.disableStandardItemLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableBlend();
		GlStateManager.disableCull();
		GlStateManager.shadeModel(Minecraft.isAmbientOcclusionEnabled() ? 7425 : 7424);

		bufferBuilder.begin(7, DefaultVertexFormats.BLOCK);
		final EnumFacing facing = state.getValue(BlockPSDAPGDoorBase.FACING);
		final EnumPSDAPGSide side = state.getValue(BlockPSDAPGDoorBase.SIDE);
		final float door = te.getDoorClient() * (side == EnumPSDAPGSide.LEFT ? -1 : 1);
		bufferBuilder.setTranslation(x - pos.getX() - facing.getFrontOffsetZ() * door, y - pos.getY(), z - pos.getZ() + facing.getFrontOffsetX() * door);

		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		blockRenderer.getBlockModelRenderer().renderModel(world, blockRenderer.getModelForState(state), state, pos, bufferBuilder, true);

		bufferBuilder.setTranslation(0.0D, 0.0D, 0.0D);
		tessellator.draw();
		RenderHelper.enableStandardItemLighting();
	}
}
