package mtr.item;

import mtr.block.BlockRail;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemRailModifier extends Item {

	private final boolean isConnector;
	private static final String TAG_POS = "pos";

	public ItemRailModifier(boolean isConnector) {
		super(new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
		this.isConnector = isConnector;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		final World world = context.getWorld();
		if (!world.isClient) {
			final BlockPos pos = context.getBlockPos();
			final BlockEntity entity = world.getBlockEntity(pos);

			if (entity instanceof BlockRail.TileEntityRail) {
				final CompoundTag tag = context.getStack().getOrCreateTag();

				if (tag.contains(TAG_POS)) {
					final BlockPos pos2 = BlockPos.fromLong(tag.getLong(TAG_POS));
					final BlockEntity entity2 = world.getBlockEntity(pos2);

					if (entity2 instanceof BlockRail.TileEntityRail) {
						if (isConnector) {
							((BlockRail.TileEntityRail) entity).addRail(pos2);
							((BlockRail.TileEntityRail) entity2).addRail(pos);
						} else {
							((BlockRail.TileEntityRail) entity).removeRail(pos2);
							((BlockRail.TileEntityRail) entity2).removeRail(pos);
						}
					}

					tag.remove(TAG_POS);
				} else {
					tag.putLong(TAG_POS, pos.asLong());
				}

				return ActionResult.SUCCESS;
			} else {
				return ActionResult.FAIL;
			}
		} else {
			return super.useOnBlock(context);
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		final CompoundTag tag = stack.getOrCreateTag();
		final long posLong = tag.getLong(TAG_POS);
		if (posLong != 0) {
			tooltip.add(new TranslatableText("tooltip.mtr.selected_block").append(BlockPos.fromLong(posLong).toShortString()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
		}
	}
}
