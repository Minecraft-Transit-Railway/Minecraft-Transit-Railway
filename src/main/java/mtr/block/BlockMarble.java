package mtr.block;

import mtr.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.WorldAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.block.entity.BlockEntity;

public class BlockMarble extends Block {
	public BlockMarble(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		return (state);
	}

	private BlockState getActualState(WorldAccess world, BlockPos pos, BlockState state) {
		return state;
	}

	public static class TileEntityMarble extends BlockEntity {

		public TileEntityMarble() {
			super(MTR.MARBLE_TILE_ENTITY);
		}
	}
}
