package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockStationNameTallWall extends BlockStationNameTallBase {

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final Tuple<Integer, Integer> bounds = getBounds(state);
		return IBlock.getVoxelShapeByDirection(2, bounds.getA(), 0, 14, bounds.getB(), 0.5, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction blockSide = ctx.getClickedFace();
		final Direction facing = blockSide == Direction.UP || blockSide == Direction.DOWN ? ctx.getHorizontalDirection() : blockSide.getOpposite();
		return IBlock.isReplaceable(ctx, Direction.UP, 3) ? defaultBlockState().setValue(FACING, facing).setValue(METAL, true).setValue(THIRD, EnumThird.LOWER) : null;
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityStationNameTallWall(pos, state);
	}

	public static class TileEntityStationNameTallWall extends TileEntityStationNameTallBase {

		public TileEntityStationNameTallWall(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_TALL_WALL_TILE_ENTITY.get(), pos, state, 0.03125F, false);
		}
	}
}
