package org.mtr.mod.block;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockSettings;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockSignalLight4Aspect1 extends BlockSignalLightBase {

	public BlockSignalLight4Aspect1(BlockSettings blockSettings) {
		super(blockSettings, 3, 16);
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockSignalBase.BlockEntityBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_4_ASPECT_1.get(), false, pos, state);
		}
	}
}
