package org.mtr.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mtr.MTR;
import org.mtr.core.data.Rail;

public class ItemTunnelCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemTunnelCreator(int height, int width, Item.Properties settings) {
		super(false, height, width, settings);
	}

	@Override
	protected void onConnect(Rail rail, ServerPlayer serverPlayerEntity, ItemStack itemStack, int radius, int height) {
		MTR.getRailActionModule(serverPlayerEntity.serverLevel(), railActionModule -> railActionModule.markRailForTunnel(rail, serverPlayerEntity, radius, height));
	}
}
