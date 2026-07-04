package org.mtr.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.MTR;
import org.mtr.core.data.Rail;

public class ItemTunnelWallCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemTunnelWallCreator(int height, int width, Item.Properties settings) {
		super(true, height, width, settings);
	}

	@Override
	protected void onConnect(Rail rail, ServerPlayer serverPlayerEntity, ItemStack itemStack, int radius, int height) {
		final BlockState blockState = getSavedState(itemStack);
		MTR.getRailActionModule(serverPlayerEntity.serverLevel(), railActionModule -> railActionModule.markRailForTunnelWall(rail, serverPlayerEntity, radius, height, blockState));
	}
}
