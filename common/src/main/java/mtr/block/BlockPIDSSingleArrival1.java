package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPIDSSingleArrival1 extends BlockPIDSBaseVertical {
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityBlockPIDSSingleArrival1(pos, state);
	}

	public static class TileEntityBlockPIDSSingleArrival1 extends BlockPIDSBaseVertical.TileEntityBlockPIDSBaseVertical {

		public static final int MAX_ARRIVALS = 1;
		public static final int LINES_PER_ARRIVAL = 16;
		private final BlockState state;

		public TileEntityBlockPIDSSingleArrival1(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.PIDS_SINGLE_ARRIVAL_1_TILE_ENTITY.get(), pos, state);
			this.state = state;
		}

		@Override
		public int getMaxArrivals() {
			final boolean isBottom = IBlock.getStatePropertySafe(this.state, HALF) == DoubleBlockHalf.LOWER;
			return isBottom ? 0 : MAX_ARRIVALS;
		}

		@Override
		public int getLinesPerArrival() {
			return LINES_PER_ARRIVAL;
		}
	}
}
