package mtr.render;

import mtr.block.BlockAPGGlass;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class RenderAPGGlass extends RenderRouteBase<BlockAPGGlass.TileEntityAPGGlass> {

	private static final float COLOR_STRIP_START = 0.75F;
	private static final float COLOR_STRIP_END = 0.78125F;

	public RenderAPGGlass(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher, 4, 8, 4, 8, false, BlockAPGGlass.ARROW_DIRECTION);
	}

	@Override
	protected RenderType getRenderType(BlockGetter world, BlockPos pos, BlockState state) {
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER) {
			return RenderType.NONE;
		} else if ((Math.floorMod(pos.getX(), 8) < 4) == (Math.floorMod(pos.getZ(), 8) < 4)) {
			return RenderType.ARROW;
		} else {
			return RenderType.ROUTE;
		}
	}

	@Override
	protected void renderAdditional(StoredMatrixTransformations storedMatrixTransformations, long platformId, BlockState state, int leftBlocks, int rightBlocks, Direction facing, int color, int light) {
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER && IBlock.getStatePropertySafe(state, SIDE_EXTENDED) != EnumSide.SINGLE) {
			final boolean isLeft = isLeft(state);
			final boolean isRight = isRight(state);
			RenderTrains.scheduleRender(ClientData.DATA_CACHE.getColorStrip(platformId).resourceLocation, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (matrices, vertexConsumer) -> {
				storedMatrixTransformations.transform(matrices);
				IDrawing.drawTexture(matrices, vertexConsumer, isLeft ? sidePadding : 0, COLOR_STRIP_START, 0, isRight ? 1 - sidePadding : 1, COLOR_STRIP_END, 0, facing, color, light);
				IDrawing.drawTexture(matrices, vertexConsumer, isRight ? 1 - sidePadding : 1, COLOR_STRIP_START, 0.125F, isLeft ? sidePadding : 0, COLOR_STRIP_END, 0.125F, facing, color, light);
				matrices.popPose();
			});

			final float width = leftBlocks + rightBlocks + 1 - sidePadding * 2;
			final float height = 1 - topPadding - bottomPadding;
			RenderTrains.scheduleRender(ClientData.DATA_CACHE.getSingleRowStationName(platformId, width / height).resourceLocation, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (matrices, vertexConsumer) -> {
				storedMatrixTransformations.transform(matrices);
				IDrawing.drawTexture(matrices, vertexConsumer, 1 - (rightBlocks == 0 ? sidePadding : 0), topPadding, 0.125F, leftBlocks == 0 ? sidePadding : 0, 1 - bottomPadding, 0.125F, (rightBlocks - (rightBlocks == 0 ? 0 : sidePadding)) / width, 0, (width - leftBlocks + (leftBlocks == 0 ? 0 : sidePadding)) / width, 1, facing, color, light);
				matrices.popPose();
			});
		}
	}
}
