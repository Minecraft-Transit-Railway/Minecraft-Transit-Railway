package mtr.item;

import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class ItemBlockClickingBase extends Item {

	public static final String TAG_POS = "pos";

	public ItemBlockClickingBase(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (!context.getLevel().isClientSide) {
			if (clickCondition(context)) {
				final CompoundTag compoundTag = context.getItemInHand().getOrCreateTag();

				if (compoundTag.contains(TAG_POS)) {
					final BlockPos posEnd = BlockPos.of(compoundTag.getLong(TAG_POS));
					onEndClick(context, posEnd, compoundTag);
					compoundTag.remove(TAG_POS);
				} else {
					compoundTag.putLong(TAG_POS, context.getClickedPos().asLong());
					onStartClick(context, compoundTag);
				}

				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.FAIL;
			}
		} else {
			return super.useOn(context);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
		final CompoundTag compoundTag = stack.getOrCreateTag();
		final long posLong = compoundTag.getLong(TAG_POS);
		if (posLong != 0) {
			tooltip.add(Text.translatable("tooltip.mtr.selected_block", BlockPos.of(posLong).toShortString()).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
		}
	}

	protected abstract void onStartClick(UseOnContext context, CompoundTag compoundTag);

	protected abstract void onEndClick(UseOnContext context, BlockPos posEnd, CompoundTag compoundTag);

	protected abstract boolean clickCondition(UseOnContext context);
}
