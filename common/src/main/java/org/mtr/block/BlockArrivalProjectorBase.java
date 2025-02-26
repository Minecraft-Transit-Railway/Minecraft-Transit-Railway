package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public abstract class BlockArrivalProjectorBase extends BlockPIDSBase {

	private static final BiPredicate<World, BlockPos> CAN_STORE_DATA = (world, blockPos) -> true;
	private static final BiFunction<World, BlockPos, BlockPos> GET_BLOCK_POS_WITH_DATA = (world, blockPos) -> blockPos;

	public BlockArrivalProjectorBase(int maxArrivals) {
		super(maxArrivals, CAN_STORE_DATA, GET_BLOCK_POS_WITH_DATA);
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		final Direction side = ctx.getSide();
		if (side != Direction.UP && side != Direction.DOWN) {
			return getDefaultState2().with(new Property<>(FACING.data), side.getOpposite().data);
		} else {
			return null;
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, facing);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
	}

	public static abstract class BlockEntityArrivalProjectorBase extends BlockEntityBase {

		public BlockEntityArrivalProjectorBase(int maxArrivals, BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(maxArrivals, CAN_STORE_DATA, GET_BLOCK_POS_WITH_DATA, type, pos, state);
		}

		@Override
		public boolean showArrivalNumber() {
			return false;
		}

		@Override
		public boolean alternateLines() {
			return false;
		}

		@Override
		public int textColorArrived() {
			return 0xFF9900;
		}

		@Override
		public int textColor() {
			return 0xFF9900;
		}
	}
}
