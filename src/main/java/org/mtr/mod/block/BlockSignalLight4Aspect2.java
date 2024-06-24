package org.mtr.mod.block;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockSettings;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

public class BlockSignalLight4Aspect2 extends BlockSignalLightBase {

	public BlockSignalLight4Aspect2(BlockSettings blockSettings) {
		super(blockSettings, 3, 16);
	}

	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityExtension {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_4_ASPECT_2.get(), pos, state);
		}
	}
}
