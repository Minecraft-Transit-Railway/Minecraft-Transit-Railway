package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import org.mtr.registry.Items;

import java.util.function.Consumer;

public interface IBlock {

	EnumProperty<DoubleBlockHalf> HALF = EnumProperty.create("half", DoubleBlockHalf.class);
	EnumProperty<EnumThird> THIRD = EnumProperty.create("third", EnumThird.class);
	EnumProperty<EnumSide> SIDE_EXTENDED = EnumProperty.create("side", EnumSide.class);
	EnumProperty<EnumSide> SIDE = EnumProperty.create("side", EnumSide.class, side -> side != EnumSide.MIDDLE && side != EnumSide.SINGLE);

	default <T extends Comparable<T>> void propagate(Level world, BlockPos pos, Direction direction, Property<T> property, int maxBlocksAway) {
		final T originalPropertyValue = IBlock.getStatePropertySafe(world, pos, property);
		propagate(world, pos, direction, offsetPos -> world.setBlockAndUpdate(offsetPos, world.getBlockState(offsetPos).setValue(property, originalPropertyValue)), maxBlocksAway);
	}

	default void propagate(Level world, BlockPos pos, Direction direction, Consumer<BlockPos> callback, int maxBlocksAway) {
		for (int i = 1; i <= maxBlocksAway; i++) {
			final BlockPos offsetPos = pos.relative(direction, i);
			if (this == world.getBlockState(offsetPos).getBlock()) {
				callback.accept(offsetPos);
				propagate(world, offsetPos, direction, callback, maxBlocksAway);
				return;
			}
		}
	}

	static InteractionResult checkHoldingBrush(Level world, Player player, Runnable callbackBrush, @Nullable Runnable callbackNoBrush) {
		return checkHoldingItem(world, player, item -> callbackBrush.run(), callbackNoBrush, Items.BRUSH.get().asItem());
	}

	static InteractionResult checkHoldingItem(Level world, Player player, Consumer<Item> callbackItem, @Nullable Runnable callbackNoItem, Item... items) {
		Item holdingItem = null;
		for (final Item item : items) {
			if (player.isHolding(item)) {
				holdingItem = item;
				break;
			}
		}

		if (holdingItem != null) {
			if (!world.isClientSide()) {
				callbackItem.accept(holdingItem);
			}
			return InteractionResult.SUCCESS;
		} else {
			if (callbackNoItem == null) {
				return InteractionResult.FAIL;
			} else {
				if (!world.isClientSide()) {
					callbackNoItem.run();
					return InteractionResult.CONSUME;
				} else {
					return InteractionResult.SUCCESS;
				}
			}
		}
	}

	static InteractionResult checkHoldingBrush(Level world, Player player, Runnable callbackBrush) {
		return checkHoldingBrush(world, player, callbackBrush, null);
	}

	static VoxelShape getVoxelShapeByDirection(double x1, double y1, double z1, double x2, double y2, double z2, Direction facing) {
		return switch (facing) {
			case NORTH -> Block.box(x1, y1, z1, x2, y2, z2);
			case EAST -> Block.box(16 - z2, y1, x1, 16 - z1, y2, x2);
			case SOUTH -> Block.box(16 - x2, y1, 16 - z2, 16 - x1, y2, 16 - z1);
			case WEST -> Block.box(z1, y1, 16 - x2, z2, y2, 16 - x1);
			default -> Shapes.block();
		};
	}

	static boolean isReplaceable(BlockPlaceContext context, Direction direction, int totalLength) {
		for (int i = 0; i < totalLength; i++) {
			if (!context.getLevel().getBlockState(context.getClickedPos().relative(direction, i)).canBeReplaced(context)) {
				return false;
			}
		}
		return true;
	}

	static void playerWillDestroyCreative(Level world, @Nullable Player player, BlockPos pos) {
		if (!world.isClientSide() && (player == null || player.isCreative())) {
			world.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
			final BlockState state = world.getBlockState(pos);
			if (player != null) {
				world.levelEvent(player, 2001, pos, Block.getId(state));
			}
		}
	}

	static Direction getSideDirection(BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
		return IBlock.getStatePropertySafe(state, SIDE) == EnumSide.LEFT ? facing.getClockWise() : facing.getCounterClockWise();
	}

	static <T extends Comparable<T>> T getStatePropertySafe(BlockGetter world, BlockPos pos, Property<T> property) {
		return getStatePropertySafe(world.getBlockState(pos), property);
	}

	static <T extends Comparable<T>> T getStatePropertySafe(Level world, BlockPos pos, Property<T> property) {
		return getStatePropertySafe(world.getBlockState(pos), property);
	}

	static <T extends Comparable<T>> T getStatePropertySafe(BlockState state, Property<T> property) {
		try {
			return state.hasProperty(property) ? state.getValue(property) : property.getPossibleValues().iterator().next();
		} catch (Exception ignored) {
		}
		return property.getPossibleValues().iterator().next();
	}

	enum DoubleBlockHalf implements StringRepresentable {
		UPPER("upper"), LOWER("lower");

		private final String name;

		DoubleBlockHalf(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}

	enum EnumThird implements StringRepresentable {
		LOWER("lower"), MIDDLE("middle"), UPPER("upper");

		private final String name;

		EnumThird(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}

	enum EnumSide implements StringRepresentable {
		LEFT("left"), RIGHT("right"), MIDDLE("middle"), SINGLE("single");

		private final String name;

		EnumSide(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
