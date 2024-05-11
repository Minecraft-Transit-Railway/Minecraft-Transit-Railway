package org.mtr.mod.item;

import org.jetbrains.annotations.Nullable;
import org.mtr.core.data.Rail;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.packet.ClientPacketHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemRailShapeModifier extends ItemExtension {

	public ItemRailShapeModifier(ItemSettings itemSettings) {
		super(itemSettings);
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock2(ItemUsageContext context) {
		if (context.getWorld().isClient()) {
			final Rail rail = MinecraftClientData.getInstance().getFacingRail(false);
			if (rail == null) {
				return ActionResult.FAIL;
			} else {
				final PlayerEntity playerEntity = context.getPlayer();
				if (playerEntity != null && playerEntity.isSneaking()) {
					return ItemRailModifier.setStyles(rail, true) ? ActionResult.SUCCESS : ActionResult.FAIL;
				} else {
					ClientPacketHelper.openRailShapeModifierScreen(rail.getHexId());
					return ActionResult.SUCCESS;
				}
			}
		} else {
			return ActionResult.FAIL;
		}
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable World world, List<MutableText> tooltip, TooltipContext options) {
		final String[] textSplit = TextHelper.translatable("tooltip.mtr.rail_shape_modifier", InitClient.getShiftText()).getString().split("\\|");
		for (String text : textSplit) {
			tooltip.add(TextHelper.literal(text).formatted(TextFormatting.GRAY).formatted(TextFormatting.ITALIC));
		}
	}
}
