package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockArrivalProjector1Small extends BlockArrivalProjectorBase {

	private static final int MAX_ARRIVALS = 12;

	public BlockArrivalProjector1Small(BlockBehaviour.Properties settings) {
		super(settings, MAX_ARRIVALS);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new ArrivalProjector1SmallBlockEntity(blockPos, blockState);
	}

	public static class ArrivalProjector1SmallBlockEntity extends BlockEntityArrivalProjectorBase {

		public ArrivalProjector1SmallBlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, BlockEntityTypes.ARRIVAL_PROJECTOR_1_SMALL.get(), pos, state);
		}
	}
}
