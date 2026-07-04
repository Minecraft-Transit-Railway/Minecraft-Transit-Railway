package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockSignalLight4Aspect1 extends BlockSignalLightBase {

	public BlockSignalLight4Aspect1(BlockBehaviour.Properties blockSettings) {
		super(blockSettings, 3, 16);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SignalLight4Aspect1BlockEntity(blockPos, blockState);
	}

	public static class SignalLight4Aspect1BlockEntity extends BlockEntityBase {

		public SignalLight4Aspect1BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_4_ASPECT_1.get(), false, pos, state);
		}
	}
}
