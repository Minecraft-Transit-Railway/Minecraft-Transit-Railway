package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.mtr.registry.BlockEntityTypes;

public class BlockClock extends Block implements EntityBlock {

	public static final BooleanProperty FACING = BooleanProperty.create("facing");

	public BlockClock(BlockBehaviour.Properties blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final boolean facing = ctx.getHorizontalDirection().getAxis() == Direction.Axis.X;
		return defaultBlockState().setValue(FACING, facing);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING) ? Direction.EAST : Direction.NORTH;
		return Shapes.or(IBlock.getVoxelShapeByDirection(3, 0, 6, 13, 12, 10, facing), Block.box(7.5, 12, 7.5, 8.5, 16, 8.5));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new ClockBlockEntity(blockPos, blockState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public static class ClockBlockEntity extends BlockEntityExtension {

		public ClockBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.CLOCK.get(), pos, state);
		}
	}
}
