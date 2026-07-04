package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockSignalLight4Aspect2 extends BlockSignalLightBase {

	public BlockSignalLight4Aspect2(BlockBehaviour.Properties blockSettings) {
		super(blockSettings, 3, 16);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SignalLight4Aspect2BlockEntity(blockPos, blockState);
	}

	public static class SignalLight4Aspect2BlockEntity extends BlockEntityBase {

		public SignalLight4Aspect2BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_4_ASPECT_2.get(), true, pos, state);
		}
	}
}
