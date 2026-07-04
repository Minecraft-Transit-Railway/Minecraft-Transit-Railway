package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockArrivalProjector1Large extends BlockArrivalProjectorBase {

	private static final int MAX_ARRIVALS = 16;

	public BlockArrivalProjector1Large(BlockBehaviour.Properties settings) {
		super(settings, MAX_ARRIVALS);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new ArrivalProjector1LargeBlockEntity(blockPos, blockState);
	}

	public static class ArrivalProjector1LargeBlockEntity extends BlockEntityArrivalProjectorBase {

		public ArrivalProjector1LargeBlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, BlockEntityTypes.ARRIVAL_PROJECTOR_1_LARGE.get(), pos, state);
		}
	}
}
