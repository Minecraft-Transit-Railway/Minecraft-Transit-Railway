package mtr.block;

import net.minecraft.block.*;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class BlockPlatform extends HorizontalFacingBlock {

	public static final EnumProperty<EnumDoorType> DOOR_TYPE = EnumProperty.of("door_type", EnumDoorType.class);
	public static final IntProperty SIDE = IntProperty.of("side", 0, 4);

	public BlockPlatform(Settings settings) {
		super(settings);
		setDefaultState(stateManager.getDefaultState().with(DOOR_TYPE, EnumDoorType.NONE));
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		Direction facing = searchBlock(world, pos, AbstractRailBlock.class, 3);
		if (facing == null) {
			facing = searchBlock(world, pos, AirBlock.class, 1);
		}
		if (facing == null) {
			facing = searchBlock(world, pos.up(), AbstractRailBlock.class, 3);
		}
		if (facing == null) {
			facing = searchBlock(world, pos.up(), AirBlock.class, 1);
		}
		if (facing == null) {
			facing = Direction.NORTH;
		}

		final Block blockAbove = world.getBlockState(pos.up()).getBlock();

		EnumDoorType doorType;
		if (blockAbove instanceof BlockPSDDoor || blockAbove instanceof BlockPSDGlass || blockAbove instanceof BlockPSDGlassEnd) {
			doorType = EnumDoorType.PSD;
		} else if (blockAbove instanceof BlockAPGDoor || blockAbove instanceof BlockAPGGlass || blockAbove instanceof BlockAPGGlassEnd) {
			doorType = EnumDoorType.APG;
		} else {
			doorType = EnumDoorType.NONE;
		}

		final boolean aboveIsDoor = blockAbove instanceof BlockPSDAPGDoorBase;
		final boolean leftAboveIsDoor = world.getBlockState(pos.up().offset(facing.rotateYCounterclockwise())).getBlock() instanceof BlockPSDAPGDoorBase;
		final boolean rightAboveIsDoor = world.getBlockState(pos.up().offset(facing.rotateYClockwise())).getBlock() instanceof BlockPSDAPGDoorBase;

		int side;
		if (aboveIsDoor && rightAboveIsDoor) {
			side = 2;
		} else if (aboveIsDoor && leftAboveIsDoor) {
			side = 3;
		} else if (rightAboveIsDoor) {
			side = 1;
		} else if (leftAboveIsDoor) {
			side = 4;
		} else {
			side = 0;
		}

		return state.with(FACING, facing).with(DOOR_TYPE, doorType).with(SIDE, side);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, DOOR_TYPE, SIDE);
	}

	private Direction searchBlock(BlockView world, BlockPos pos, Class<? extends Block> blockClass, int maxRadius) {
		for (int radius = 1; radius <= maxRadius; radius++) {
			for (final Direction facing : new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}) {
				if (blockClass.isInstance(world.getBlockState(pos.offset(facing, radius)).getBlock())) {
					return facing;
				}
			}
		}

		return null;
	}

	private enum EnumDoorType implements StringIdentifiable {

		NONE("none"), PSD("psd"), APG("apg");

		private final String name;

		EnumDoorType(String nameIn) {
			name = nameIn;
		}

		@Override
		public String asString() {
			return name;
		}
	}
}
