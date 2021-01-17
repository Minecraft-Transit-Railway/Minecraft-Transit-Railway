package mtr.block;

import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class BlockStationNameTallWall extends BlockStationNameTallBase {

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Pair<Integer, Integer> bounds = getBounds(state);
		return IBlock.getVoxelShapeByDirection(2, bounds.getLeft(), 0, 14, bounds.getRight(), 0.5, state.get(FACING));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final Direction blockSide = ctx.getSide();
		final Direction facing = blockSide == Direction.UP || blockSide == Direction.DOWN ? ctx.getPlayerFacing() : blockSide.getOpposite();
		return IBlock.isReplaceable(ctx, Direction.UP, 3) ? getDefaultState().with(FACING, facing).with(METAL, true).with(THIRD, EnumThird.LOWER) : null;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityStationNameTallWall();
	}

	public static class TileEntityStationNameTallWall extends TileEntityStationNameBase {

		public TileEntityStationNameTallWall() {
			super(MTR.STATION_NAME_TALL_WALL_TILE_ENTITY, true, false, 80, 0.25F, 0.03125F);
		}

		@Override
		public boolean shouldRender() {
			if (world == null) {
				return false;
			}
			final BlockState state = world.getBlockState(pos);
			return state.getBlock() instanceof BlockStationNameTallWall && state.get(THIRD) == EnumThird.MIDDLE;
		}
	}
}
