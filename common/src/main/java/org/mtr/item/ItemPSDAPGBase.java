package org.mtr.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.mtr.block.BlockPSDTop;
import org.mtr.block.IBlock;
import org.mtr.block.TripleHorizontalBlock;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.registry.Blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPSDAPGBase extends Item implements IBlock {

	private final EnumPSDAPGItem item;
	private final EnumPSDAPGType type;

	public ItemPSDAPGBase(EnumPSDAPGItem item, EnumPSDAPGType type, Item.Settings settings) {
		super(settings);
		this.item = item;
		this.type = type;
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		final int horizontalBlocks = item.isDoor ? type.isOdd ? 3 : 2 : 1;
		if (blocksNotReplaceable(context, horizontalBlocks, type.isPSD ? 3 : 2, getBlockStateFromItem().getBlock())) {
			return ActionResult.FAIL;
		}

		final World world = context.getWorld();
		final Direction playerFacing = context.getHorizontalPlayerFacing();
		final BlockPos pos = context.getBlockPos().offset(context.getSide());

		for (int x = 0; x < horizontalBlocks; x++) {
			final BlockPos newPos = pos.offset(playerFacing.rotateYClockwise(), x);

			for (int y = 0; y < 2; y++) {
				final BlockState state = getBlockStateFromItem().with(Properties.HORIZONTAL_FACING, playerFacing).with(HALF, y == 1 ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
				if (item.isDoor) {
					BlockState neighborState = state.with(SIDE, x == 0 ? EnumSide.LEFT : EnumSide.RIGHT);
					if (type.isOdd) {
						neighborState = neighborState.with(TripleHorizontalBlock.CENTER, x > 0 && x < horizontalBlocks - 1);
					}
					world.setBlockState(newPos.up(y), neighborState);
				} else {
					world.setBlockState(newPos.up(y), state.with(SIDE_EXTENDED, EnumSide.SINGLE));
				}
			}

			if (type.isPSD) {
				world.setBlockState(newPos.up(2), BlockPSDTop.getActualState(world, newPos.up(2)));
			}
		}

		context.getStack().decrement(1);
		return ActionResult.SUCCESS;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add((this.type.isLift ? this.type.isOdd ? TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_ODD : TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_EVEN : item.translationKey).getMutableText().formatted(Formatting.GRAY));
	}

	private BlockState getBlockStateFromItem() {
		return switch (type) {
			case PSD_1 -> switch (item) {
				case PSD_APG_DOOR -> Blocks.PSD_DOOR_1.createAndGet().getDefaultState();
				case PSD_APG_GLASS -> Blocks.PSD_GLASS_1.createAndGet().getDefaultState();
				case PSD_APG_GLASS_END -> Blocks.PSD_GLASS_END_1.createAndGet().getDefaultState();
			};
			case PSD_2 -> switch (item) {
				case PSD_APG_DOOR -> Blocks.PSD_DOOR_2.createAndGet().getDefaultState();
				case PSD_APG_GLASS -> Blocks.PSD_GLASS_2.createAndGet().getDefaultState();
				case PSD_APG_GLASS_END -> Blocks.PSD_GLASS_END_2.createAndGet().getDefaultState();
			};
			case APG -> switch (item) {
				case PSD_APG_DOOR -> Blocks.APG_DOOR.createAndGet().getDefaultState();
				case PSD_APG_GLASS -> Blocks.APG_GLASS.createAndGet().getDefaultState();
				case PSD_APG_GLASS_END -> Blocks.APG_GLASS_END.createAndGet().getDefaultState();
			};
			case LIFT_DOOR_1 -> Blocks.LIFT_DOOR_EVEN_1.createAndGet().getDefaultState();
			case LIFT_DOOR_ODD_1 -> Blocks.LIFT_DOOR_ODD_1.createAndGet().getDefaultState();
		};
	}

	public static boolean blocksNotReplaceable(ItemUsageContext context, int width, int height, @Nullable Block blacklistBlock) {
		final Direction facing = context.getHorizontalPlayerFacing();
		final World world = context.getWorld();
		final BlockPos startingPos = context.getBlockPos().offset(context.getSide());

		for (int x = 0; x < width; x++) {
			final BlockPos offsetPos = startingPos.offset(facing.rotateYClockwise(), x);

			if (blacklistBlock != null) {
				final boolean isBlacklistedBelow = world.getBlockState(offsetPos.down()).isOf(blacklistBlock);
				final boolean isBlacklistedAbove = world.getBlockState(offsetPos.up(height)).isOf(blacklistBlock);
				if (isBlacklistedBelow || isBlacklistedAbove) {
					return true;
				}
			}

			for (int y = 0; y < height; y++) {
				if (!world.getBlockState(offsetPos.up(y)).getBlock().equals(net.minecraft.block.Blocks.AIR)) {
					return true;
				}
			}
		}

		return false;
	}

	public enum EnumPSDAPGType {
		PSD_1(true, false, false),
		PSD_2(true, false, false),
		APG(false, false, false),
		LIFT_DOOR_1(false, false, true),
		LIFT_DOOR_ODD_1(false, true, true);

		private final boolean isPSD;
		private final boolean isOdd;
		private final boolean isLift;

		EnumPSDAPGType(boolean isPSD, boolean isOdd, boolean isLift) {
			this.isPSD = isPSD;
			this.isOdd = isOdd;
			this.isLift = isLift;
		}
	}

	public enum EnumPSDAPGItem implements StringIdentifiable {

		PSD_APG_DOOR(TranslationProvider.TOOLTIP_MTR_PSD_APG_DOOR, "psd_apg_door", true),
		PSD_APG_GLASS(TranslationProvider.TOOLTIP_MTR_PSD_APG_GLASS, "psd_apg_glass", false),
		PSD_APG_GLASS_END(TranslationProvider.TOOLTIP_MTR_PSD_APG_GLASS_END, "psd_apg_glass_end", false);

		public final TranslationProvider.TranslationHolder translationKey;
		private final String name;
		private final boolean isDoor;

		EnumPSDAPGItem(TranslationProvider.TranslationHolder translationKey, String name, boolean isDoor) {
			this.translationKey = translationKey;
			this.name = name;
			this.isDoor = isDoor;
		}

		@Nonnull
		@Override
		public String asString() {
			return name;
		}
	}
}
