package org.mtr.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockClock extends Block implements BlockEntityProvider {

	public static final BooleanProperty FACING = BooleanProperty.of("facing");

	public BlockClock(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final boolean facing = ctx.getHorizontalPlayerFacing().getAxis() == Direction.Axis.X;
		return getDefaultState().with(FACING, facing);
	}

	@Nonnull
	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING) ? Direction.EAST : Direction.NORTH;
		return VoxelShapes.union(IBlock.getVoxelShapeByDirection(3, 0, 6, 13, 12, 10, facing), Block.createCuboidShape(7.5, 12, 7.5, 8.5, 16, 8.5));
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new ClockBlockEntity(blockPos, blockState);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public static class ClockBlockEntity extends BlockEntity {

		public ClockBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.CLOCK.get(), pos, state);
		}
	}
}
