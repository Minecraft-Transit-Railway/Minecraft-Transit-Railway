package org.mtr.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.registry.DataComponentTypes;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class ItemBlockClickingBase extends Item {

	public ItemBlockClickingBase(Item.Settings settings) {
		super(settings.maxCount(1));
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!context.getWorld().isClient()) {
			if (clickCondition(context)) {
				final BlockPos startPos = context.getStack().get(DataComponentTypes.START_POS.createAndGet());

				if (startPos == null) {
					context.getStack().set(DataComponentTypes.START_POS.createAndGet(), context.getBlockPos());
					onStartClick(context);
				} else {
					onEndClick(context, startPos);
					context.getStack().remove(DataComponentTypes.START_POS.createAndGet());
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
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		final BlockPos blockPos = stack.get(DataComponentTypes.START_POS.createAndGet());
		if (blockPos != null) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_SELECTED_BLOCK.getMutableText(blockPos.toShortString()).formatted(Formatting.GOLD));
		}
	}

	protected abstract void onStartClick(ItemUsageContext context);

	protected abstract void onEndClick(ItemUsageContext context, BlockPos posEnd);

	protected abstract boolean clickCondition(ItemUsageContext context);
}
