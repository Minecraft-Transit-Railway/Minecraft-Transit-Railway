package org.mtr.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.MTR;
import org.mtr.core.data.Rail;

public class ItemBridgeCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemBridgeCreator(int width, Item.Properties settings) {
		super(true, 0, width, settings);
	}

	@Override
	protected void onConnect(Rail rail, ServerPlayer serverPlayerEntity, ItemStack itemStack, int radius, int height) {
		final BlockState blockState = getSavedState(itemStack);
		MTR.getRailActionModule(serverPlayerEntity.serverLevel(), railActionModule -> railActionModule.markRailForBridge(rail, serverPlayerEntity, radius, blockState));
	}
}
