package org.mtr.mod.item;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.ItemSettings;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mod.Init;

public class ItemTunnelCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemTunnelCreator(int height, int width, ItemSettings itemSettings) {
		super(false, height, width, itemSettings);
	}

	@Override
	protected boolean onConnect(ServerPlayerEntity serverPlayerEntity, ItemStack stack, BlockPos posStart, BlockPos posEnd, int radius, int height) {
		final boolean[] success = {false};
		Init.getRailActionModule(serverPlayerEntity.getServerWorld(), railActionModule -> success[0] = railActionModule.markRailForTunnel(serverPlayerEntity, posStart, posEnd, radius, height));
		return success[0];
	}
}
