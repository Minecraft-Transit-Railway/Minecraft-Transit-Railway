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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockStationNameTallBlockDoubleSided extends BlockStationNameTallBase {

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final Tuple<Integer, Integer> bounds = getBounds(state);
		return Shapes.or(IBlock.getVoxelShapeByDirection(2, bounds.getA(), 5, 14, bounds.getB(), 11, IBlock.getStatePropertySafe(state, FACING)), BlockStationColorPole.getStationPoleShape());
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return IBlock.isReplaceable(ctx, Direction.UP, 3) ? defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()).setValue(METAL, true).setValue(THIRD, EnumThird.LOWER) : null;
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityStationNameTallBlockDoubleSided(pos, state);
	}

	public static class TileEntityStationNameTallBlockDoubleSided extends TileEntityStationNameTallBase {

		public TileEntityStationNameTallBlockDoubleSided(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED_TILE_ENTITY.get(), pos, state, 0.6875F, true);
		}
	}
}
