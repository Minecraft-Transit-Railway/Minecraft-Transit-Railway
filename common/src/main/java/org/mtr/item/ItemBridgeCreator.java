package org.mtr.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.mtr.MTR;
import org.mtr.core.data.Rail;

public class ItemBridgeCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemBridgeCreator(int width, Item.Settings settings) {
		super(true, 0, width, settings);
	}

	@Override
	protected void onConnect(Rail rail, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack, int radius, int height) {
		final BlockState blockState = getSavedState(itemStack);
		if (blockState != null) {
			MTR.getRailActionModule(serverPlayerEntity.getServerWorld(), railActionModule -> railActionModule.markRailForBridge(rail, serverPlayerEntity, radius, blockState));
		}
	}
}
