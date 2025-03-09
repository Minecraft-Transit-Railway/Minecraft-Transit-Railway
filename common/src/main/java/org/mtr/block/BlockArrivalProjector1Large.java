package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockArrivalProjector1Large extends BlockArrivalProjectorBase {

	private static final int MAX_ARRIVALS = 16;

	public BlockArrivalProjector1Large(AbstractBlock.Settings settings) {
		super(settings, MAX_ARRIVALS);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new ArrivalProjector1LargeBlockEntity(blockPos, blockState);
	}

	public static class ArrivalProjector1LargeBlockEntity extends BlockEntityArrivalProjectorBase {

		public ArrivalProjector1LargeBlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, BlockEntityTypes.ARRIVAL_PROJECTOR_1_LARGE.createAndGet(), pos, state);
		}
	}
}
