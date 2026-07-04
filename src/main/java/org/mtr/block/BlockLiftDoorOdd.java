package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;

//? if >= 1.21.4 {
import net.minecraft.world.level.ScheduledTickAccess;
//? } else {
/*import net.minecraft.world.level.LevelAccessor;
 *///? }

public class BlockLiftDoorOdd extends BlockPSDAPGDoorBase implements TripleHorizontalBlock {

	public BlockLiftDoorOdd(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
//? if >= 1.21.4 {
	protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		return TripleHorizontalBlock.updateShape(state, direction, neighborState.is(this), super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random));
//? } else {
	/*protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return TripleHorizontalBlock.updateShape(state, direction, neighborState.is(this), super.updateShape(state, direction, neighborState, world, pos, neighborPos));
*///? }
	}

	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		TripleHorizontalBlock.playerWillDestroy(world, pos.below(IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 1 : 0), state, player);
		return super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new LiftDoorOddBlockEntity(blockPos, blockState);
	}

	@Override
	public Item asItem() {
		return Items.LIFT_DOOR_ODD_1.get();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(END);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
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
