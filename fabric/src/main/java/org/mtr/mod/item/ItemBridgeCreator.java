package org.mtr.mod.item;

import org.mtr.core.data.Rail;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.ItemSettings;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mod.Init;

public class ItemBridgeCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemBridgeCreator(int width, ItemSettings itemSettings) {
		super(true, 0, width, itemSettings);
	}

	@Override
	protected void onConnect(Rail rail, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack, int radius, int height) {
		final BlockState blockState = getSavedState(itemStack);
		if (blockState != null) {
			Init.getRailActionModule(serverPlayerEntity.getServerWorld(), railActionModule -> railActionModule.markRailForBridge(rail, serverPlayerEntity, radius, blockState));
		}
	}
}
