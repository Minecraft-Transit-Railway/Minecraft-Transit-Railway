package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockArrivalProjector1Medium extends BlockArrivalProjectorBase {

	private static final int MAX_ARRIVALS = 12;

	public BlockArrivalProjector1Medium(BlockBehaviour.Properties settings) {
		super(settings, MAX_ARRIVALS);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new ArrivalProjector1MediumBlockEntity(blockPos, blockState);
	}

	public static class ArrivalProjector1MediumBlockEntity extends BlockEntityArrivalProjectorBase {

		public ArrivalProjector1MediumBlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM.get(), pos, state);
		}
	}
}
