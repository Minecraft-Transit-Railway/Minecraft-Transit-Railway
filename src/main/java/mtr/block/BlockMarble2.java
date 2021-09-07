package mtr.block;

import mtr.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.WorldAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.block.entity.BlockEntity;

public class BlockMarble2 extends Block {
	public BlockMarble2(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		return (state);
	}

	private BlockState getActualState(WorldAccess world, BlockPos pos, BlockState state) {
		return state;
	}

	public static class TileEntityMarble2 extends BlockEntity {

		public TileEntityMarble2() {
			super(MTR.MARBLE2_TILE_ENTITY);
		}
	}
}
