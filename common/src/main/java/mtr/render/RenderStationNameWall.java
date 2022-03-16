package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockStationNameBase;
import mtr.block.BlockStationNameWall;
import mtr.block.IBlock;
import mtr.client.IDrawing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class RenderStationNameWall extends RenderStationNameBase<BlockStationNameWall.TileEntityStationNameWall> {

	public RenderStationNameWall(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	protected void drawStationName(BlockStationNameBase.TileEntityStationNameBase entity, PoseStack matrices, MultiBufferSource vertexConsumers, MultiBufferSource.BufferSource immediate, String stationName, int color, int light) {
		final BlockGetter world = entity.getLevel();
		final BlockPos pos = entity.getBlockPos();

		if (world == null) {
			return;
		}

		final int length = getLength(world, pos);
		IDrawing.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, stationName, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, (length / 2.0F) - 0.5F, 0, length, 0.6F, 50, color, false, light, ((x1, y1, x2, y2) -> {
		}));
	}

	private int getLength(BlockGetter world, BlockPos pos) {
		if (world == null) {
			return 1;
		}
		final Direction facing = IBlock.getStatePropertySafe(world, pos, BlockStationNameWall.FACING);

		int length = 1;
		while (true) {
			final BlockState state = world.getBlockState(pos.relative(facing.getClockWise(), length));
			final boolean merged = IBlock.getStatePropertySafe(state, BlockStationNameWall.MERGED);
			if (state.getBlock() instanceof BlockStationNameWall && merged) {
				length++;
			} else {
				break;
			}
		}

		return length;
	}
}