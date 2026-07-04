package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockSignalLight2Aspect4 extends BlockSignalLightBase {

	public BlockSignalLight2Aspect4(BlockBehaviour.Properties blockSettings) {
		super(blockSettings, 2, 14);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SignalLight2Aspect4BlockEntity(blockPos, blockState);
	}

	public static class SignalLight2Aspect4BlockEntity extends BlockEntityBase {

		public SignalLight2Aspect4BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_4.get(), true, pos, state);
		}
	}
}
