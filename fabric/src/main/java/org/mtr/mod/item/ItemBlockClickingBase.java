package org.mtr.mod.item;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemBlockClickingBase extends ItemExtension {

	public static final String TAG_POS = "pos";

	public ItemBlockClickingBase(ItemSettings itemSettings) {
		super(itemSettings);
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock2(ItemUsageContext context) {
		if (!context.getWorld().isClient()) {
			if (clickCondition(context)) {
				final CompoundTag compoundTag = context.getStack().getOrCreateTag();

				if (compoundTag.contains(TAG_POS)) {
					final BlockPos posEnd = BlockPos.fromLong(compoundTag.getLong(TAG_POS));
					onEndClick(context, posEnd, compoundTag);
					compoundTag.remove(TAG_POS);
				} else {
					compoundTag.putLong(TAG_POS, context.getBlockPos().asLong());
					onStartClick(context, compoundTag);
				}

				return ActionResult.SUCCESS;
			} else {
				return ActionResult.FAIL;
			}
		} else {
			return super.useOnBlock2(context);
		}
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable World world, List<MutableText> tooltip, TooltipContext options) {
		final CompoundTag compoundTag = stack.getOrCreateTag();
		final long posLong = compoundTag.getLong(TAG_POS);
		if (posLong != 0) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_SELECTED_BLOCK.getMutableText(BlockPos.fromLong(posLong).toShortString()).formatted(TextFormatting.GOLD));
		}
	}

	protected abstract void onStartClick(ItemUsageContext context, CompoundTag compoundTag);

	protected abstract void onEndClick(ItemUsageContext context, BlockPos posEnd, CompoundTag compoundTag);

	protected abstract boolean clickCondition(ItemUsageContext context);
}
