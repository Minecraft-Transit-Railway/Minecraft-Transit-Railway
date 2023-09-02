package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mod.Items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.Consumer;

public interface IBlock {

	EnumProperty<DoubleBlockHalf> HALF = EnumProperty.of("half", DoubleBlockHalf.class);
	EnumProperty<EnumThird> THIRD = EnumProperty.of("third", EnumThird.class);
	EnumProperty<EnumSide> SIDE_EXTENDED = EnumProperty.of("side", EnumSide.class);
	EnumProperty<EnumSide> SIDE = EnumProperty.of("side", EnumSide.class, side -> side != EnumSide.MIDDLE && side != EnumSide.SINGLE);

	default <T extends Comparable<T>> void propagate(World world, BlockPos pos, Direction direction, Property<T> property, int maxBlocksAway) {
		final T originalPropertyValue = IBlock.getStatePropertySafe(new BlockView(world.data), pos, property);
		propagate(world, pos, direction, offsetPos -> world.setBlockState(offsetPos, world.getBlockState(offsetPos).with(property, originalPropertyValue)), maxBlocksAway);
	}

	default void propagate(World world, BlockPos pos, Direction direction, Consumer<BlockPos> callback, int maxBlocksAway) {
		for (int i = 1; i <= maxBlocksAway; i++) {
			final BlockPos offsetPos = pos.offset(direction, i);
			if (this == world.getBlockState(offsetPos).getBlock().data) {
				callback.accept(offsetPos);
				propagate(world, offsetPos, direction, callback, maxBlocksAway);
				return;
			}
		}
	}

	static ActionResult checkHoldingBrush(World world, PlayerEntity player, Runnable callbackBrush, @Nullable Runnable callbackNoBrush) {
		return checkHoldingItem(world, player, item -> callbackBrush.run(), callbackNoBrush, Items.BRUSH.get().asItem());
	}

	static ActionResult checkHoldingItem(World world, PlayerEntity player, Consumer<Item> callbackItem, @Nullable Runnable callbackNoItem, Item... items) {
		Item holdingItem = null;
		for (final Item item : items) {
			if (player.isHolding(item)) {
				holdingItem = item;
				break;
			}
		}

		if (holdingItem != null) {
			if (!world.isClient()) {
				callbackItem.accept(holdingItem);
			}
			return ActionResult.SUCCESS;
		} else {
			if (callbackNoItem == null) {
				return ActionResult.FAIL;
			} else {
				if (!world.isClient()) {
					callbackNoItem.run();
					return ActionResult.CONSUME;
				} else {
					return ActionResult.SUCCESS;
				}
			}
		}
	}

	static ActionResult checkHoldingBrush(World world, PlayerEntity player, Runnable callbackBrush) {
		return checkHoldingBrush(world, player, callbackBrush, null);
	}

	static VoxelShape getVoxelShapeByDirection(double x1, double y1, double z1, double x2, double y2, double z2, Direction facing) {
		switch (facing) {
			case NORTH:
				return Block.createCuboidShape(x1, y1, z1, x2, y2, z2);
			case EAST:
				return Block.createCuboidShape(16 - z2, y1, x1, 16 - z1, y2, x2);
			case SOUTH:
				return Block.createCuboidShape(16 - x2, y1, 16 - z2, 16 - x1, y2, 16 - z1);
			case WEST:
				return Block.createCuboidShape(z1, y1, 16 - x2, z2, y2, 16 - x1);
			default:
				return VoxelShapes.fullCube();
		}
	}

	static boolean isReplaceable(ItemPlacementContext context, Direction direction, int totalLength) {
		for (int i = 0; i < totalLength; i++) {
			if (!context.getWorld().getBlockState(context.getBlockPos().offset(direction, i)).canReplace(context)) {
				return false;
			}
		}
		return true;
	}

	static void onBreakCreative(World world, @Nullable PlayerEntity player, BlockPos pos) {
		if (!world.isClient() && (player == null || player.isCreative())) {
			world.setBlockState(pos, Blocks.getAirMapped().getDefaultState(), 35);
			final BlockState state = world.getBlockState(pos);
			if (player != null) {
				world.syncWorldEvent(player, 2001, pos, Block.getRawIdFromState(state));
			}
		}
	}

	static Direction getSideDirection(BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, DirectionHelper.FACING);
		return IBlock.getStatePropertySafe(state, SIDE) == EnumSide.LEFT ? facing.rotateYClockwise() : facing.rotateYCounterclockwise();
	}

	static <T extends Comparable<T>> T getStatePropertySafe(BlockView world, BlockPos pos, Property<T> property) {
		return getStatePropertySafe(world.getBlockState(pos), property);
	}

	static <T extends Comparable<T>> T getStatePropertySafe(World world, BlockPos pos, Property<T> property) {
		return getStatePropertySafe(world.getBlockState(pos), property);
	}

	static boolean getStatePropertySafe(World world, BlockPos pos, BooleanProperty property) {
		return getStatePropertySafe(world.getBlockState(pos), new Property<>(property.data));
	}

	static Direction getStatePropertySafe(World world, BlockPos pos, DirectionProperty property) {
		return Direction.convert(getStatePropertySafe(world.getBlockState(pos), new Property<>(property.data)));
	}

	static <T extends Enum<T> & StringIdentifiable> T getStatePropertySafe(World world, BlockPos pos, EnumProperty<T> property) {
		return getStatePropertySafe(world.getBlockState(pos), new Property<>(property.data));
	}

	static int getStatePropertySafe(World world, BlockPos pos, IntegerProperty property) {
		return getStatePropertySafe(world.getBlockState(pos), new Property<>(property.data));
	}

	static boolean getStatePropertySafe(BlockState state, BooleanProperty property) {
		return getStatePropertySafe(state, new Property<>(property.data));
	}

	static Direction getStatePropertySafe(BlockState state, DirectionProperty property) {
		return Direction.convert(getStatePropertySafe(state, new Property<>(property.data)));
	}

	static <T extends Enum<T> & StringIdentifiable> T getStatePropertySafe(BlockState state, EnumProperty<T> property) {
		return getStatePropertySafe(state, new Property<>(property.data));
	}

	static int getStatePropertySafe(BlockState state, IntegerProperty property) {
		return getStatePropertySafe(state, new Property<>(property.data));
	}

	static <T extends Comparable<T>> T getStatePropertySafe(BlockState state, Property<T> property) {
		try {
			return state.contains(property) ? state.get(property) : new ArrayList<>(property.getValues()).get(0);
		} catch (Exception ignored) {
		}
		return new ArrayList<>(property.getValues()).get(0);
	}

	enum DoubleBlockHalf implements StringIdentifiable {
		UPPER("upper"), LOWER("lower");

		private final String name;

		DoubleBlockHalf(String name) {
			this.name = name;
		}

		@Nonnull
		@Override
		public String asString2() {
			return name;
		}
	}

	enum EnumThird implements StringIdentifiable {
		LOWER("lower"), MIDDLE("middle"), UPPER("upper");

		private final String name;

		EnumThird(String name) {
			this.name = name;
		}

		@Nonnull
		@Override
		public String asString2() {
			return name;
		}
	}

	enum EnumSide implements StringIdentifiable {
		LEFT("left"), RIGHT("right"), MIDDLE("middle"), SINGLE("single");

		private final String name;

		EnumSide(String name) {
			this.name = name;
		}

		@Nonnull
		@Override
		public String asString2() {
			return name;
		}
	}
}
