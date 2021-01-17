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
		return VoxelShapes.union(IBlock.getVoxelShapeByDirection(2, bounds.getLeft(), 5, 14, bounds.getRight(), 11, state.get(FACING)), BlockStationPole.getStationPoleShape());
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return IBlock.isReplaceable(ctx, Direction.UP, 3) ? getDefaultState().with(FACING, ctx.getPlayerFacing()).with(METAL, true).with(THIRD, EnumThird.LOWER) : null;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityStationNameTallBlock();
	}

	public static class TileEntityStationNameTallBlock extends TileEntityStationNameBase {

		public TileEntityStationNameTallBlock() {
			super(MTR.STATION_NAME_TALL_BLOCK_TILE_ENTITY, true, false, 80, 0.25F, 0.6875F);
		}

		@Override
		public boolean shouldRender() {
			if (world == null) {
				return false;
			}
			final BlockState state = world.getBlockState(pos);
			return state.getBlock() instanceof BlockStationNameTallBlock && state.get(THIRD) == EnumThird.MIDDLE;
		}
	}
}
