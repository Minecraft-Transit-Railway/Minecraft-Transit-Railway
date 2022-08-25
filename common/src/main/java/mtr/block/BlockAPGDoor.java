package mtr.block;

import mtr.BlockEntityTypes;
import mtr.Items;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BlockAPGDoor extends BlockPSDAPGDoorBase {

	public static final BooleanProperty GLASS = BooleanProperty.create("glass");

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		final BlockState superState = super.updateShape(state, direction, newState, world, pos, posFrom);
		if (superState.getBlock() == Blocks.AIR) {
			return superState;
		}

		final Direction facing = IBlock.getStatePropertySafe(superState, FACING);
		final EnumSide side = IBlock.getStatePropertySafe(superState, SIDE);

		if (side == EnumSide.LEFT && facing.getCounterClockWise() == direction || side == EnumSide.RIGHT && facing.getClockWise() == direction) {
			return superState.setValue(GLASS, newState.getBlock() instanceof BlockAPGGlass || newState.getBlock() instanceof BlockAPGGlassEnd);
		} else {
			return superState;
		}
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityAPGDoor(pos, state);
	}

	@Override
	public Item asItem() {
		return Items.APG_DOOR.get();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(END, FACING, GLASS, HALF, SIDE, UNLOCKED);
	}

	public static class TileEntityAPGDoor extends TileEntityPSDAPGDoorBase {

		public TileEntityAPGDoor(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.APG_DOOR_TILE_ENTITY.get(), pos, state);
		}
	}
}
