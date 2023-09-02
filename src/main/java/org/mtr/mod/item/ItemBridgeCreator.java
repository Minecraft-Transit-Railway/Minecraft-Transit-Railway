package org.mtr.mod.item;

import org.mtr.mapping.holder.*;
import org.mtr.mod.Init;

public class ItemBridgeCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemBridgeCreator(int width, ItemSettings itemSettings) {
		super(true, 0, width, itemSettings);
	}

	@Override
	protected boolean onConnect(ServerPlayerEntity serverPlayerEntity, ItemStack stack, BlockPos posStart, BlockPos posEnd, int radius, int height) {
		final BlockState state = getSavedState(stack);
		if (state == null) {
			return true;
		}
		final boolean[] success = {false};
		Init.getRailActionModule(serverPlayerEntity.getServerWorld(), railActionModule -> success[0] = railActionModule.markRailForBridge(serverPlayerEntity, posStart, posEnd, radius, state));
		return success[0];
	}
}
