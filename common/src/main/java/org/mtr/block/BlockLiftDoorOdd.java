package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;

import javax.annotation.Nonnull;

public class BlockLiftDoorOdd extends BlockPSDAPGDoorBase implements TripleHorizontalBlock {

	public BlockLiftDoorOdd(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		return TripleHorizontalBlock.getStateForNeighborUpdate(state, direction, neighborState.isOf(this), super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random));
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		TripleHorizontalBlock.onBreak(world, pos.down(IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 1 : 0), state, player);
		return super.onBreak(world, pos, state, player);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new LiftDoorOddBlockEntity(blockPos, blockState);
	}

	@Nonnull
	@Override
	public Item asItem() {
		return Items.LIFT_DOOR_ODD_1.get();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(END);
		builder.add(Properties.HORIZONTAL_FACING);
		builder.add(HALF);
		builder.add(CENTER);
		builder.add(SIDE);
		builder.add(UNLOCKED);
	}

	public static class LiftDoorOddBlockEntity extends BlockEntityBase {

		public LiftDoorOddBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_DOOR_ODD_1.get(), pos, state);
		}
	}
}
