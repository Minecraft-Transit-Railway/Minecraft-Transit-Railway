package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.mtr.registry.BlockEntityTypes;

public class BlockLiftPanelEven1 extends BlockLiftPanelBase {

	public BlockLiftPanelEven1(BlockBehaviour.Properties settings) {
		super(settings, false, false);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(SIDE);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new LiftPanelEven1BlockEntity(blockPos, blockState);
	}

	public static class LiftPanelEven1BlockEntity extends BlockEntityBase {

		public LiftPanelEven1BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_PANEL_EVEN_1.get(), pos, state, false);
		}
	}
}
