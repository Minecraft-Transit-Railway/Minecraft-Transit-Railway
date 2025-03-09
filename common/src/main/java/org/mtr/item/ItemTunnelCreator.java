package org.mtr.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.mtr.MTR;
import org.mtr.core.data.Rail;

public class ItemTunnelCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemTunnelCreator(int height, int width, Item.Settings settings) {
		super(false, height, width, settings);
	}

	@Override
	protected void onConnect(Rail rail, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack, int radius, int height) {
		MTR.getRailActionModule(serverPlayerEntity.getServerWorld(), railActionModule -> railActionModule.markRailForTunnel(rail, serverPlayerEntity, radius, height));
	}
}
