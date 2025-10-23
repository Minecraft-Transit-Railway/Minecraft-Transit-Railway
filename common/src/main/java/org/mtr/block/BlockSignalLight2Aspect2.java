package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockSignalLight2Aspect2 extends BlockSignalLightBase {

	public BlockSignalLight2Aspect2(AbstractBlock.Settings blockSettings) {
		super(blockSettings, 2, 14);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SignalLight2Aspect2BlockEntity(blockPos, blockState);
	}

	public static class SignalLight2Aspect2BlockEntity extends BlockSignalBase.BlockEntityBase {

		public SignalLight2Aspect2BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_2.get(), true, pos, state);
		}
	}
}
