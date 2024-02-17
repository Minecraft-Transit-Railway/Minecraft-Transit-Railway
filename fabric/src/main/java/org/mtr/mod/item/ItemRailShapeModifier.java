package org.mtr.mod.item;

import org.mtr.core.data.Rail;
import org.mtr.mapping.holder.ActionResult;
import org.mtr.mapping.holder.ItemSettings;
import org.mtr.mapping.holder.ItemUsageContext;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.packet.ClientPacketHelper;

import javax.annotation.Nonnull;

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
				ClientPacketHelper.openRailShapeModifierScreen(rail.getHexId());
				return ActionResult.SUCCESS;
			}
		} else {
			return ActionResult.FAIL;
		}
	}
}
