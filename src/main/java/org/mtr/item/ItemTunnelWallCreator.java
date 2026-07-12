package org.mtr.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.MTR;
import org.mtr.core.data.Rail;
import org.mtr.registry.DataComponentTypes;

public class ItemTunnelWallCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemTunnelWallCreator(int height, int width, Item.Properties settings) {
		super(true, height, width, settings);
	}

	@Override
	protected boolean hasWallSideMode() {
		return true;
	}

	@Override
	public void onConnect(Rail rail, ServerPlayer serverPlayerEntity, ItemStack itemStack, int radius, int height, int batchIndex, int batchTotal) {
		final BlockState blockState = getSavedState(itemStack);
		final int wallSide = itemStack.getOrDefault(DataComponentTypes.WALL_SIDE.get(), 0);
		MTR.getRailActionModule(serverPlayerEntity.serverLevel(), railActionModule -> railActionModule.markRailForTunnelWall(rail, serverPlayerEntity, radius, height, blockState, batchIndex, batchTotal, wallSide));
	}
}
