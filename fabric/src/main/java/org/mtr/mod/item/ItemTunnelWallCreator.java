package org.mtr.mod.item;

import org.mtr.mapping.holder.*;
import org.mtr.mod.Init;

public class ItemTunnelWallCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemTunnelWallCreator(int height, int width, ItemSettings itemSettings) {
		super(true, height, width, itemSettings);
	}

	@Override
	protected boolean onConnect(ServerPlayerEntity serverPlayerEntity, ItemStack stack, BlockPos posStart, BlockPos posEnd, int radius, int height) {
		final BlockState state = getSavedState(stack);
		if (state == null) {
			return true;
		}
		final boolean[] success = {false};
		Init.getRailActionModule(serverPlayerEntity.getServerWorld(), railActionModule -> success[0] = railActionModule.markRailForTunnelWall(serverPlayerEntity, posStart, posEnd, radius, height, state));
		return success[0];
	}
}
