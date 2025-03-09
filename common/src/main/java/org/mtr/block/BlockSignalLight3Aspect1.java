package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockSignalLight3Aspect1 extends BlockSignalLightBase {

	public BlockSignalLight3Aspect1(AbstractBlock.Settings blockSettings) {
		super(blockSettings, 3, 16);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SignalLight3Aspect1BlockEntity(blockPos, blockState);
	}

	public static class SignalLight3Aspect1BlockEntity extends BlockSignalBase.BlockEntityBase {

		public SignalLight3Aspect1BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_3_ASPECT_1.createAndGet(), false, pos, state);
		}
	}
}
