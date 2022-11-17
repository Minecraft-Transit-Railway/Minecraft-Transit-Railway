package mtr.block;

import mtr.BlockEntityTypes;
import mtr.Items;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class BlockLiftDoorOdd extends BlockPSDAPGDoorBase implements ITripleBlock {

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		return ITripleBlock.updateShape(state, direction, newState.is(this), () -> super.updateShape(state, direction, newState, world, pos, posFrom));
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		ITripleBlock.playerWillDestroy(world, pos, state, player, IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER);
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityLiftDoorOdd(pos, state);
	}

	@Override
	public Item asItem() {
		return Items.LIFT_DOOR_ODD_1.get();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(END, FACING, HALF, ODD, SIDE, TEMP, UNLOCKED);
	}

	public static class TileEntityLiftDoorOdd extends TileEntityPSDAPGDoorBase {

		public TileEntityLiftDoorOdd(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_DOOR_ODD_1_TILE_ENTITY.get(), pos, state);
		}
	}
}
