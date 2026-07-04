package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockSignalLight3Aspect1 extends BlockSignalLightBase {

	public BlockSignalLight3Aspect1(BlockBehaviour.Properties blockSettings) {
		super(blockSettings, 3, 16);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SignalLight3Aspect1BlockEntity(blockPos, blockState);
	}

	public static class SignalLight3Aspect1BlockEntity extends BlockEntityBase {

		public SignalLight3Aspect1BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_3_ASPECT_1.get(), false, pos, state);
		}
	}
}
