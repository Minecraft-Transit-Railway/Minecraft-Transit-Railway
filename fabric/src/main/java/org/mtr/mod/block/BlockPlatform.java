package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockPlatform extends BlockExtension implements DirectionHelper {

	private final boolean isIndented;

	public static final EnumProperty<EnumDoorType> DOOR_TYPE = EnumProperty.of("door_type", EnumDoorType.class);
	public static final IntegerProperty SIDE = IntegerProperty.of("side", 0, 4);

	public BlockPlatform(BlockSettings blockSettings, boolean isIndented) {
		super(blockSettings);
		this.isIndented = isIndented;
	}

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return getActualState(BlockView.cast(world), pos, state);
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (isIndented) {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			return VoxelShapes.union(IBlock.getVoxelShapeByDirection(0, 0, 6, 16, 13, 16, facing), Block.createCuboidShape(0, 13, 0, 16, 16, 16));
		} else {
			return super.getOutlineShape2(state, world, pos, context);
		}
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(DOOR_TYPE);
		properties.add(SIDE);
	}

	private BlockState getActualState(BlockView world, BlockPos pos, BlockState state) {
		Direction facing = IBlock.getStatePropertySafe(state, FACING);

		final BlockState stateAbove = world.getBlockState(pos.up());
		final Block blockAbove = stateAbove.getBlock();

		EnumDoorType doorType;
		if (blockAbove.data instanceof BlockPSDDoor || blockAbove.data instanceof BlockPSDGlass || blockAbove.data instanceof BlockPSDGlassEnd) {
			doorType = EnumDoorType.PSD;
			facing = IBlock.getStatePropertySafe(stateAbove, FACING);
		} else if (blockAbove.data instanceof BlockAPGDoor || blockAbove.data instanceof BlockAPGGlass || blockAbove.data instanceof BlockAPGGlassEnd) {
			doorType = EnumDoorType.APG;
			facing = IBlock.getStatePropertySafe(stateAbove, FACING);
		} else {
			doorType = EnumDoorType.NONE;
		}

		final boolean aboveIsDoor = blockAbove.data instanceof BlockPSDAPGDoorBase;

		final BlockState stateLeftAbove = world.getBlockState(pos.up().offset(facing.rotateYCounterclockwise()));
		final boolean leftAboveIsDoor = stateLeftAbove.getBlock().data instanceof BlockPSDAPGDoorBase;

		final BlockState stateRightAbove = world.getBlockState(pos.up().offset(facing.rotateYClockwise()));
		final boolean rightAboveIsDoor = stateRightAbove.getBlock().data instanceof BlockPSDAPGDoorBase;

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

		return state.with(new Property<>(FACING.data), facing.data).with(new Property<>(DOOR_TYPE.data), doorType).with(new Property<>(SIDE.data), side);
	}

	private enum EnumDoorType implements StringIdentifiable {

		NONE("none"), PSD("psd"), APG("apg");
		private final String name;

		EnumDoorType(String nameIn) {
			name = nameIn;
		}

		@Nonnull
		@Override
		public String asString2() {
			return name;
		}
	}
}
