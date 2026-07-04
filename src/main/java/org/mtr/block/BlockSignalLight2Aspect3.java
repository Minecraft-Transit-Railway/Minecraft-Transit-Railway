package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockSignalLight2Aspect3 extends BlockSignalLightBase {

	public BlockSignalLight2Aspect3(BlockBehaviour.Properties blockSettings) {
		super(blockSettings, 2, 14);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SignalLight2Aspect3BlockEntity(blockPos, blockState);
	}

	public static class SignalLight2Aspect3BlockEntity extends BlockEntityBase {

		public SignalLight2Aspect3BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_3.get(), false, pos, state);
		}
	}
}
