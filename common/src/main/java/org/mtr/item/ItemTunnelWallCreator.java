package org.mtr.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.mtr.MTR;
import org.mtr.core.data.Rail;

public class ItemTunnelWallCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemTunnelWallCreator(int height, int width, Item.Settings settings) {
		super(true, height, width, settings);
	}

	@Override
	protected void onConnect(Rail rail, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack, int radius, int height) {
		final BlockState blockState = getSavedState(itemStack);
		if (blockState != null) {
			MTR.getRailActionModule(serverPlayerEntity.getServerWorld(), railActionModule -> railActionModule.markRailForTunnelWall(rail, serverPlayerEntity, radius, height, blockState));
		}
	}
}
