package mtr.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
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
		return getActualState(world, pos, state);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getActualState(ctx.getWorld(), ctx.getBlockPos(), getDefaultState());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, DOOR_TYPE, SIDE);
	}

	private BlockState getActualState(WorldAccess world, BlockPos pos, BlockState state) {
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

		final BlockState stateAbove = world.getBlockState(pos.up());
		final Block blockAbove = stateAbove.getBlock();

		EnumDoorType doorType;
		if (blockAbove instanceof BlockPSDDoor || blockAbove instanceof BlockPSDGlass || blockAbove instanceof BlockPSDGlassEnd || blockAbove instanceof BlockPSDDoor ||blockAbove instanceof BlockPSDGlass) {
			doorType = EnumDoorType.PSD;
			facing = IBlock.getStatePropertySafe(stateAbove, FACING);
		} else if (blockAbove instanceof BlockAPGDoor || blockAbove instanceof BlockAPGGlass || blockAbove instanceof BlockAPGGlassEnd) {
			doorType = EnumDoorType.APG;
			facing = IBlock.getStatePropertySafe(stateAbove, FACING);
		} else {
			doorType = EnumDoorType.NONE;
		}

		final boolean aboveIsDoor = blockAbove instanceof BlockPSDAPGDoorBase;

		final BlockState stateLeftAbove = world.getBlockState(pos.up().offset(facing.rotateYCounterclockwise()));
		final boolean leftAboveIsDoor = stateLeftAbove.getBlock() instanceof BlockPSDAPGDoorBase;

		final BlockState stateRightAbove = world.getBlockState(pos.up().offset(facing.rotateYClockwise()));
		final boolean rightAboveIsDoor = stateRightAbove.getBlock() instanceof BlockPSDAPGDoorBase;

		int side;
		if (aboveIsDoor && rightAboveIsDoor) {
			side = 2;
		} else if (aboveIsDoor && leftAboveIsDoor) {
			side = 3;
		} else if (rightAboveIsDoor) {
			side = 1;
			facing = IBlock.getStatePropertySafe(stateRightAbove, FACING);
		} else if (leftAboveIsDoor) {
			side = 4;
			facing = IBlock.getStatePropertySafe(stateLeftAbove, FACING);
		} else {
			side = 0;
		}

		return state.with(FACING, facing).with(DOOR_TYPE, doorType).with(SIDE, side);
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
