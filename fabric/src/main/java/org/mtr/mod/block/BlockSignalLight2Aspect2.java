package org.mtr.mod.block;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockSettings;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockSignalLight2Aspect2 extends BlockSignalLightBase {

	public BlockSignalLight2Aspect2(BlockSettings blockSettings) {
		super(blockSettings, 2, 14);
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockSignalBase.BlockEntityBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_2.get(), true, pos, state);
		}
	}
}
