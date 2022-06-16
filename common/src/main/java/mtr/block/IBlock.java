package mtr.block;

import mtr.Items;
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
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Consumer;

public interface IBlock {

	EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
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

	static InteractionResult checkHoldingBrush(Level world, Player player, Runnable callbackBrush, Runnable callbackNoBrush) {
		return checkHoldingItem(world, player, item -> callbackBrush.run(), callbackNoBrush, Items.BRUSH.get());
	}

	static InteractionResult checkHoldingItem(Level world, Player player, Consumer<Item> callbackItem, Runnable callbackNoItem, Item... items) {
		Item holdingItem = null;
		for (final Item item : items) {
			if (player.isHolding(item)) {
				holdingItem = item;
				break;
			}
		}

		if (holdingItem != null) {
			if (!world.isClientSide) {
				callbackItem.accept(holdingItem);
			}
			return InteractionResult.SUCCESS;
		} else {
			if (callbackNoItem == null) {
				return InteractionResult.FAIL;
			} else {
				if (!world.isClientSide) {
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
		switch (facing) {
			case NORTH:
				return Block.box(x1, y1, z1, x2, y2, z2);
			case EAST:
				return Block.box(16 - z2, y1, x1, 16 - z1, y2, x2);
			case SOUTH:
				return Block.box(16 - x2, y1, 16 - z2, 16 - x1, y2, 16 - z1);
			case WEST:
				return Block.box(z1, y1, 16 - x2, z2, y2, 16 - x1);
			default:
				return Shapes.block();
		}
	}

	static boolean isReplaceable(BlockPlaceContext ctx, Direction direction, int totalLength) {
		for (int i = 0; i < totalLength; i++) {
			if (!ctx.getLevel().getBlockState(ctx.getClickedPos().relative(direction, i)).canBeReplaced(ctx)) {
				return false;
			}
		}
		return true;
	}

	static void onBreakCreative(Level world, Player player, BlockPos pos) {
		if (!world.isClientSide && (player == null || player.isCreative())) {
			world.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
			final BlockState state = world.getBlockState(pos);
			world.levelEvent(player, 2001, pos, Block.getId(state));
		}
	}

	static Direction getSideDirection(BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, HorizontalDirectionalBlock.FACING);
		return IBlock.getStatePropertySafe(state, SIDE) == EnumSide.LEFT ? facing.getClockWise() : facing.getCounterClockWise();
	}

	static <T extends Comparable<T>> T getStatePropertySafe(BlockGetter world, BlockPos pos, Property<T> property) {
		return getStatePropertySafe(world.getBlockState(pos), property);
	}

	@SuppressWarnings("unchecked")
	static <T extends Comparable<T>> T getStatePropertySafe(BlockState state, Property<T> property) {
		final T defaultProperty = (T) property.getPossibleValues().toArray()[0];
		try {
			return state.hasProperty(property) ? state.getValue(property) : defaultProperty;
		} catch (Exception ignored) {
		}
		return defaultProperty;
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
