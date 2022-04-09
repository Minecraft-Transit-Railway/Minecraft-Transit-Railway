package mtr.item;

import mtr.Blocks;
import mtr.ItemGroups;
import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDTop;
import mtr.block.IBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.List;

public class ItemPSDAPGBase extends Item implements IBlock {

	private final EnumPSDAPGItem item;
	private final EnumPSDAPGType type;

	public ItemPSDAPGBase(EnumPSDAPGItem item, EnumPSDAPGType type) {
		super(new Item.Properties().tab(ItemGroups.RAILWAY_FACILITIES));
		this.item = item;
		this.type = type;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		final boolean isPSD = type == EnumPSDAPGType.PSD_1 || type == EnumPSDAPGType.PSD_2;
		final boolean isDoor = item == EnumPSDAPGItem.PSD_APG_DOOR;
		final boolean isTopOnly = item == EnumPSDAPGItem.PSD_ONLY_ROUTE || item == EnumPSDAPGItem.PSD_ONLY_ARROW || item == EnumPSDAPGItem.PSD_ONLY_BLANK;

		if (blocksNotReplaceable(context, isDoor ? 2 : 1, isPSD ? 3 : 2, getBlockStateFromItem().getBlock()) && !isTopOnly) {
			return InteractionResult.FAIL;
		}

		final Level world = context.getLevel();
		final Direction playerFacing = context.getHorizontalDirection();
		final BlockPos pos = context.getClickedPos().relative(context.getClickedFace());

		for (int x = 0; x < (isDoor ? 2 : 1); x++) {
			System.out.println("x: " + x);
			final BlockPos newPos = pos.relative(playerFacing.getClockWise(), x);

			for (int y = 0; y < 2; y++) {
				BlockState state;
				if (isTopOnly) {
					state = getBlockStateFromItem().setValue(BlockPSDTop.FACING, playerFacing).setValue(BlockPSDTop.AIR_LEFT, false).setValue(BlockPSDTop.AIR_RIGHT, false)
							;
					switch (item) {
						case PSD_ONLY_ROUTE:
							System.out.println("PSD_ONLY_ROUTE");
							state = state.setValue(BlockPSDTop.PERSISTENT, BlockPSDTop.EnumPersistent.ROUTE);
							break;
						case PSD_ONLY_ARROW:
							System.out.println("PSD_ONLY_ARROW");
							state = state.setValue(BlockPSDTop.PERSISTENT, BlockPSDTop.EnumPersistent.ARROW);
							break;
						default:
							System.out.println("PSD_ONLY_BLANK");
							state = state.setValue(BlockPSDTop.PERSISTENT, BlockPSDTop.EnumPersistent.BLANK);
					}
				} else {
					state = getBlockStateFromItem().setValue(BlockPSDAPGBase.FACING, playerFacing).setValue(HALF, y == 1 ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
				}
				if (isTopOnly){
					world.setBlockAndUpdate(newPos, state);
				}
				else if (isDoor) {
					final EnumSide side = x == 0 ? EnumSide.LEFT : EnumSide.RIGHT;
					world.setBlockAndUpdate(newPos.above(y), state.setValue(SIDE, side));
				} else {
					final EnumSide side = EnumSide.SINGLE;
					world.setBlockAndUpdate(newPos.above(y), state.setValue(SIDE_EXTENDED, side));
				}
			}

			if (isPSD) {
				world.setBlockAndUpdate(newPos.above(2), BlockPSDTop.getActualState(world, newPos.above(2)));
			}
		}

		context.getItemInHand().shrink(1);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack itemStack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(new TranslatableComponent("tooltip.mtr." + item.getSerializedName()).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
	}

	private BlockState getBlockStateFromItem() {
		switch (type) {
			case PSD_1:
				switch (item) {
					case PSD_APG_DOOR:
						return Blocks.PSD_DOOR_1.defaultBlockState();
					case PSD_APG_GLASS:
						return Blocks.PSD_GLASS_1.defaultBlockState();
					case PSD_APG_GLASS_END:
						return Blocks.PSD_GLASS_END_1.defaultBlockState();
				}
			case PSD_2:
				switch (item) {
					case PSD_APG_DOOR:
						return Blocks.PSD_DOOR_2.defaultBlockState();
					case PSD_APG_GLASS:
						return Blocks.PSD_GLASS_2.defaultBlockState();
					case PSD_APG_GLASS_END:
						return Blocks.PSD_GLASS_END_2.defaultBlockState();
				}
			case APG:
				switch (item) {
					case PSD_APG_DOOR:
						return Blocks.APG_DOOR.defaultBlockState();
					case PSD_APG_GLASS:
						return Blocks.APG_GLASS.defaultBlockState();
					case PSD_APG_GLASS_END:
						return Blocks.APG_GLASS_END.defaultBlockState();
				}
			case TOP:
				return Blocks.PSD_TOP.defaultBlockState();
		}
		return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
	}

	public static boolean blocksNotReplaceable(UseOnContext context, int width, int height, Block blacklistBlock) {
		final Direction facing = context.getHorizontalDirection();
		final Level world = context.getLevel();
		final BlockPos startingPos = context.getClickedPos().relative(context.getClickedFace());

		for (int x = 0; x < width; x++) {
			final BlockPos offsetPos = startingPos.relative(facing.getClockWise(), x);

			if (blacklistBlock != null) {
				final boolean isBlacklistedBelow = world.getBlockState(offsetPos.below()).is(blacklistBlock);
				final boolean isBlacklistedAbove = world.getBlockState(offsetPos.above(height)).is(blacklistBlock);
				if (isBlacklistedBelow || isBlacklistedAbove) {
					return true;
				}
			}

			for (int y = 0; y < height; y++) {
				if (!world.getBlockState(offsetPos.above(y)).getMaterial().isReplaceable()) {
					return true;
				}
			}
		}

		return false;
	}

	public enum EnumPSDAPGType {PSD_1, PSD_2, APG, TOP}

	public enum EnumPSDAPGItem implements StringRepresentable {

		PSD_APG_DOOR("psd_apg_door"), PSD_APG_GLASS("psd_apg_glass"), PSD_APG_GLASS_END("psd_apg_glass_end"),
		PSD_ONLY_BLANK("psd_only_blank"), PSD_ONLY_ARROW("psd_only_arrow"), PSD_ONLY_ROUTE("psd_only_route");
		private final String name;

		EnumPSDAPGItem(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
