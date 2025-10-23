package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockArrivalProjector1Medium extends BlockArrivalProjectorBase {

	private static final int MAX_ARRIVALS = 12;

	public BlockArrivalProjector1Medium(AbstractBlock.Settings settings) {
		super(settings, MAX_ARRIVALS);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new ArrivalProjector1MediumBlockEntity(blockPos, blockState);
	}

	public static class ArrivalProjector1MediumBlockEntity extends BlockEntityArrivalProjectorBase {

		public ArrivalProjector1MediumBlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM.get(), pos, state);
		}
	}
}
