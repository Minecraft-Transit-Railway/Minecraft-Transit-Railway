package mtr.item;

import mtr.data.RailwayData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ItemTunnelWallCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemTunnelWallCreator(int height, int width) {
		super(true, height, width);
	}

	@Override
	protected boolean onConnect(Player player, ItemStack stack, RailwayData railwayData, BlockPos posStart, BlockPos posEnd, int radius, int height) {
		final BlockState state = getSavedState(stack);
		if (state == null || state.isAir()) {
			player.displayClientMessage(getShiftClickReminder(), true);
			return true;
		} else {
			return railwayData.markRailForTunnelWall(posStart, posEnd, radius, height, state);
		}
	}
}
