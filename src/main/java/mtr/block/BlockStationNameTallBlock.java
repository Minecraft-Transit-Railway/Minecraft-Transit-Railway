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
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BlockStationNameTallBlock extends BlockStationNameTallBase {

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Pair<Integer, Integer> bounds = getBounds(state);
		return VoxelShapes.union(IBlock.getVoxelShapeByDirection(2, bounds.getLeft(), 5, 14, bounds.getRight(), 11, IBlock.getStatePropertySafe(state, FACING)), BlockStationColorPole.getStationPoleShape());
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return IBlock.isReplaceable(ctx, Direction.UP, 3) ? getDefaultState().with(FACING, ctx.getPlayerFacing()).with(METAL, true).with(THIRD, EnumThird.LOWER) : null;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityStationNameTallBlock(null, null);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityStationNameTallBlock(pos, state);
	}

	public static class TileEntityStationNameTallBlock extends TileEntityStationNameTallBase {

		public TileEntityStationNameTallBlock(BlockPos pos, BlockState state) {
			super(MTR.STATION_NAME_TALL_BLOCK_TILE_ENTITY, pos, state, 0.6875F);
		}
	}
}
