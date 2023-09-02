package mtr.block;

import mtr.mappings.BlockDirectionalMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPlatform extends BlockDirectionalMapper {

	private final boolean isIndented;

	public static final EnumProperty<EnumDoorType> DOOR_TYPE = EnumProperty.create("door_type", EnumDoorType.class);
	public static final IntegerProperty SIDE = IntegerProperty.create("side", 0, 4);

	public BlockPlatform(Properties settings, boolean isIndented) {
		super(settings);
		this.isIndented = isIndented;
		registerDefaultState(defaultBlockState().setValue(DOOR_TYPE, EnumDoorType.NONE));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		return getActualState(world, pos, state);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (isIndented) {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			return Shapes.or(IBlock.getVoxelShapeByDirection(0, 0, 6, 16, 13, 16, facing), Block.box(0, 13, 0, 16, 16, 16));
		} else {
			return super.getShape(state, world, pos, context);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, DOOR_TYPE, SIDE);
	}

	private BlockState getActualState(BlockGetter world, BlockPos pos, BlockState state) {
		Direction facing = IBlock.getStatePropertySafe(state, FACING);

		final BlockState stateAbove = world.getBlockState(pos.above());
		final Block blockAbove = stateAbove.getBlock();

		EnumDoorType doorType;
		if (blockAbove instanceof BlockPSDDoor || blockAbove instanceof BlockPSDGlass || blockAbove instanceof BlockPSDGlassEnd) {
			doorType = EnumDoorType.PSD;
			facing = IBlock.getStatePropertySafe(stateAbove, FACING);
		} else if (blockAbove instanceof BlockAPGDoor || blockAbove instanceof BlockAPGGlass || blockAbove instanceof BlockAPGGlassEnd) {
			doorType = EnumDoorType.APG;
			facing = IBlock.getStatePropertySafe(stateAbove, FACING);
		} else {
			doorType = EnumDoorType.NONE;
		}

		final boolean aboveIsDoor = blockAbove instanceof BlockPSDAPGDoorBase;

		final BlockState stateLeftAbove = world.getBlockState(pos.above().relative(facing.getCounterClockWise()));
		final boolean leftAboveIsDoor = stateLeftAbove.getBlock() instanceof BlockPSDAPGDoorBase;

		final BlockState stateRightAbove = world.getBlockState(pos.above().relative(facing.getClockWise()));
		final boolean rightAboveIsDoor = stateRightAbove.getBlock() instanceof BlockPSDAPGDoorBase;

		final int side;
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

		return state.setValue(FACING, facing).setValue(DOOR_TYPE, doorType).setValue(SIDE, side);
	}

	private enum EnumDoorType implements StringRepresentable {

		NONE("none"), PSD("psd"), APG("apg");
		private final String name;

		EnumDoorType(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
