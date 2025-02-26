package org.mtr.mod.block;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockLiftPanelOdd1 extends BlockLiftPanelBase {

	public BlockLiftPanelOdd1() {
		super(true, false);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(CENTER);
		properties.add(SIDE);
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_PANEL_ODD_1.get(), pos, state, true);
		}
	}
}
