package mtr.item;

import mtr.Blocks;
import mtr.ItemGroups;
import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDTop;
import mtr.block.IBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class ItemPSDAPGBase extends Item implements IBlock {

	private final EnumPSDAPGItem item;
	private final EnumPSDAPGType type;

	public ItemPSDAPGBase(EnumPSDAPGItem item, EnumPSDAPGType type) {
		super(new Item.Settings().group(ItemGroups.RAILWAY_FACILITIES));
		this.item = item;
		this.type = type;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		final boolean isPSD = type == EnumPSDAPGType.PSD_1 || type == EnumPSDAPGType.PSD_2;
		final boolean isDoor = item == EnumPSDAPGItem.PSD_APG_DOOR;

		if (blocksNotReplaceable(context, isDoor ? 2 : 1, isPSD ? 3 : 2, getBlockStateFromItem().getBlock())) {
			return ActionResult.FAIL;
		}

		final World world = context.getWorld();
		final Direction playerFacing = context.getPlayerFacing();
		final BlockPos pos = context.getBlockPos().offset(context.getSide());

		for (int x = 0; x < (isDoor ? 2 : 1); x++) {
			final BlockPos newPos = pos.offset(playerFacing.rotateYClockwise(), x);

			for (int y = 0; y < 2; y++) {
				final BlockState state = getBlockStateFromItem().with(BlockPSDAPGBase.FACING, playerFacing).with(HALF, y == 1 ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
				if (isDoor) {
					final EnumSide side = x == 0 ? EnumSide.LEFT : EnumSide.RIGHT;
					world.setBlockState(newPos.up(y), state.with(SIDE, side));
				} else {
					final EnumSide side = EnumSide.SINGLE;
					world.setBlockState(newPos.up(y), state.with(SIDE_EXTENDED, side));
				}
			}

			if (isPSD) {
				world.setBlockState(newPos.up(2), BlockPSDTop.getActualState(world, newPos.up(2)));
			}
		}

		context.getStack().decrement(1);
		return ActionResult.SUCCESS;
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText("tooltip.mtr." + item.asString()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
	}

	private BlockState getBlockStateFromItem() {
		switch (type) {
			case PSD_1:
				switch (item) {
					case PSD_APG_DOOR:
						return Blocks.PSD_DOOR_1.getDefaultState();
					case PSD_APG_GLASS:
						return Blocks.PSD_GLASS_1.getDefaultState();
					case PSD_APG_GLASS_END:
						return Blocks.PSD_GLASS_END_1.getDefaultState();
				}
			case PSD_2:
				switch (item) {
					case PSD_APG_DOOR:
						return Blocks.PSD_DOOR_2.getDefaultState();
					case PSD_APG_GLASS:
						return Blocks.PSD_GLASS_2.getDefaultState();
					case PSD_APG_GLASS_END:
						return Blocks.PSD_GLASS_END_2.getDefaultState();
				}
			case APG:
				switch (item) {
					case PSD_APG_DOOR:
						return Blocks.APG_DOOR.getDefaultState();
					case PSD_APG_GLASS:
						return Blocks.APG_GLASS.getDefaultState();
					case PSD_APG_GLASS_END:
						return Blocks.APG_GLASS_END.getDefaultState();
				}
		}
		return net.minecraft.block.Blocks.AIR.getDefaultState();
	}

	public static boolean blocksNotReplaceable(ItemUsageContext context, int width, int height, Block blacklistBlock) {
		final Direction facing = context.getPlayerFacing();
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
				if (!world.getBlockState(offsetPos.up(y)).getMaterial().isReplaceable()) {
					return true;
				}
			}
		}

		return false;
	}

	public enum EnumPSDAPGType {PSD_1, PSD_2, APG}

	public enum EnumPSDAPGItem implements StringIdentifiable {

		PSD_APG_DOOR("psd_apg_door"), PSD_APG_GLASS("psd_apg_glass"), PSD_APG_GLASS_END("psd_apg_glass_end");
		private final String name;

		EnumPSDAPGItem(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return name;
		}
	}
}
