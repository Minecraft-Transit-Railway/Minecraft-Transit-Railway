package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class BlockLiftPanelOdd1 extends BlockLiftPanel1 {

	public BlockLiftPanelOdd1(boolean isFlat) {
		super(true, isFlat);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, ODD, SIDE);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityLiftPanelOdd1(pos, state);
	}

	public static class TileEntityLiftPanelOdd1 extends TileEntityLiftPanel1Base {

		public TileEntityLiftPanelOdd1(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_PANEL_ODD_1_TILE_ENTITY.get(), pos, state, true);
		}

		@Override
		protected void convert() {
			if (!converted) {
				converted = true;
				setChanged();
				syncData();
			}
		}
	}
}
