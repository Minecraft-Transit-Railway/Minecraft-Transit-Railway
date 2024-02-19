package org.mtr.mod.item;

import org.mtr.core.data.Rail;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.ItemSettings;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mod.Init;

public class ItemTunnelWallCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemTunnelWallCreator(int height, int width, ItemSettings itemSettings) {
		super(true, height, width, itemSettings);
	}

	@Override
	protected void onConnect(Rail rail, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack, int radius, int height) {
		final BlockState blockState = getSavedState(itemStack);
		if (blockState != null) {
			Init.getRailActionModule(serverPlayerEntity.getServerWorld(), railActionModule -> railActionModule.markRailForTunnelWall(rail, serverPlayerEntity, radius, height, blockState));
		}
	}
}
