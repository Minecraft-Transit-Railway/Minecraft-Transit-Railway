package org.mtr.mod.item;

import org.mtr.mapping.holder.*;
import org.mtr.mod.block.BlockLiftButtons;
import org.mtr.mod.block.BlockLiftPanelBase;
import org.mtr.mod.block.BlockLiftTrackFloor;

public class ItemLiftButtonsLinkModifier extends ItemBlockClickingBase {

	private final boolean isConnector;

	public ItemLiftButtonsLinkModifier(boolean isConnector, ItemSettings itemSettings) {
		super(itemSettings.maxCount(1));
		this.isConnector = isConnector;
	}

	@Override
	protected void onStartClick(ItemUsageContext context, CompoundTag compoundTag) {
	}

	@Override
	protected void onEndClick(ItemUsageContext context, BlockPos posEnd, CompoundTag compoundTag) {
		// TODO
	}

	@Override
	protected boolean clickCondition(ItemUsageContext context) {
		final Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
		return block.data instanceof BlockLiftTrackFloor || block.data instanceof BlockLiftButtons || block.data instanceof BlockLiftPanelBase;
	}
}
