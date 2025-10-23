package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockSignalLight2Aspect3 extends BlockSignalLightBase {

	public BlockSignalLight2Aspect3(AbstractBlock.Settings blockSettings) {
		super(blockSettings, 2, 14);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SignalLight2Aspect3BlockEntity(blockPos, blockState);
	}

	public static class SignalLight2Aspect3BlockEntity extends BlockSignalBase.BlockEntityBase {

		public SignalLight2Aspect3BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_3.get(), false, pos, state);
		}
	}
}
