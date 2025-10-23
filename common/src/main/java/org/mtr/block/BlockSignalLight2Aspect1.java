package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockSignalLight2Aspect1 extends BlockSignalLightBase {

	public BlockSignalLight2Aspect1(AbstractBlock.Settings blockSettings) {
		super(blockSettings, 2, 14);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SignalLight2Aspect1BlockEntity(blockPos, blockState);
	}

	public static class SignalLight2Aspect1BlockEntity extends BlockSignalBase.BlockEntityBase {

		public SignalLight2Aspect1BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_1.get(), false, pos, state);
		}
	}
}
