package org.mtr.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.mtr.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class ItemBlockClickingBase extends Item {

	public static final String TAG_POS = "pos";

	public ItemBlockClickingBase(Item.Settings settings) {
		super(settings.maxCount(1));
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!context.getWorld().isClient()) {
			if (clickCondition(context)) {
				final NbtComponent nbtComponent = context.getStack().get(DataComponentTypes.CUSTOM_DATA);
				final NbtCompound nbtCompound;

				if (nbtComponent == null) {
					nbtCompound = NbtComponent.DEFAULT.copyNbt();
					nbtCompound.putLong(TAG_POS, context.getBlockPos().asLong());
					onStartClick(context, nbtCompound);
				} else {
					nbtCompound = nbtComponent.copyNbt();
					final BlockPos posEnd = BlockPos.fromLong(nbtCompound.getLong(TAG_POS));
					onEndClick(context, posEnd, nbtCompound);
					nbtCompound.remove(TAG_POS);
				}

				context.getStack().set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbtCompound));
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
		final NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
		if (nbtComponent != null) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_SELECTED_BLOCK.getMutableText(BlockPos.fromLong(nbtComponent.copyNbt().getLong(TAG_POS)).toShortString()).formatted(Formatting.GOLD));
		}
	}

	protected abstract void onStartClick(ItemUsageContext context, NbtCompound nbtCompound);

	protected abstract void onEndClick(ItemUsageContext context, BlockPos posEnd, NbtCompound nbtCompound);

	protected abstract boolean clickCondition(ItemUsageContext context);
}
