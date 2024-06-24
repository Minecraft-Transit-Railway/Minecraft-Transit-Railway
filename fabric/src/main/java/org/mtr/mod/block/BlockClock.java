package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockClock extends BlockExtension implements BlockWithEntity {

	public static final BooleanProperty FACING = BooleanProperty.of("facing");

	public BlockClock(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		final boolean facing = ctx.getPlayerFacing().getAxis() == Axis.X;
		return getDefaultState2().with(new Property<>(FACING.data), facing);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING) ? Direction.EAST : Direction.NORTH;
		return VoxelShapes.union(IBlock.getVoxelShapeByDirection(3, 0, 6, 13, 12, 10, facing), Block.createCuboidShape(7.5, 12, 7.5, 8.5, 16, 8.5));
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
	}

	public static class BlockEntity extends BlockEntityExtension {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.CLOCK.get(), pos, state);
		}
	}
}
