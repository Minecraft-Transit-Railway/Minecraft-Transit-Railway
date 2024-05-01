package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.Items;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockLiftDoorOdd extends BlockPSDAPGDoorBase implements TripleHorizontalBlock {

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return TripleHorizontalBlock.getStateForNeighborUpdate(state, direction, neighborState.isOf(new Block(this)), super.getStateForNeighborUpdate2(state, direction, neighborState, world, pos, neighborPos));
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		TripleHorizontalBlock.onBreak(world, pos.down(IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 1 : 0), state, player);
		super.onBreak2(world, pos, state, player);
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType2(BlockState state) {
		return BlockRenderType.getModelMapped();
	}

	@Nonnull
	@Override
	public Item asItem2() {
		return Items.LIFT_DOOR_ODD_1.get();
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(END);
		properties.add(FACING);
		properties.add(HALF);
		properties.add(CENTER);
		properties.add(SIDE);
		properties.add(UNLOCKED);
	}

	public static class BlockEntity extends BlockEntityBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_DOOR_ODD_1.get(), pos, state);
		}
	}
}
