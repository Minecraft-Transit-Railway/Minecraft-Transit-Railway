package mtr.item;

import mtr.block.BlockRail;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemRailConnector extends Item {

	private static final String TAG_POS = "pos";

	public ItemRailConnector(Settings settings) {
		super(settings);
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
					((BlockRail.TileEntityRail) entity).addRail(pos2);
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
			tooltip.add(new TranslatableText("tooltip.mtr.connecting_to").append(BlockPos.fromLong(posLong).toShortString()));
		}
	}
}
