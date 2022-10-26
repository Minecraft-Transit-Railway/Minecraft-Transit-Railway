package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class BlockLiftPanelOdd2 extends BlockLiftPanelBase {

	public BlockLiftPanelOdd2() {
		super(true, true);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, ODD, SIDE);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityLiftPanelOdd2(pos, state);
	}

	@Override
	public BlockEntityType<? extends BlockEntityMapper> getType() {
		return BlockEntityTypes.LIFT_PANEL_ODD_2_TILE_ENTITY.get();
	}

	public static class TileEntityLiftPanelOdd2 extends TileEntityLiftPanel1Base {

		public TileEntityLiftPanelOdd2(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_PANEL_ODD_2_TILE_ENTITY.get(), pos, state, true);
		}
	}
}
