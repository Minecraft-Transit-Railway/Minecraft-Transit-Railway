package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockSignalLight4Aspect2 extends BlockSignalLightBase {

	public BlockSignalLight4Aspect2(AbstractBlock.Settings blockSettings) {
		super(blockSettings, 3, 16);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SignalLight4Aspect2BlockEntity(blockPos, blockState);
	}

	public static class SignalLight4Aspect2BlockEntity extends BlockSignalBase.BlockEntityBase {

		public SignalLight4Aspect2BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_4_ASPECT_2.createAndGet(), true, pos, state);
		}
	}
}
