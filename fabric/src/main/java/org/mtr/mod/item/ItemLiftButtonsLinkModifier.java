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
		final World world = context.getWorld();
		final BlockPos posStart = context.getBlockPos();
		connect(world, posStart, posEnd, isConnector);
		connect(world, posEnd, posStart, isConnector);
	}

	@Override
	protected boolean clickCondition(ItemUsageContext context) {
		final Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
		return block.data instanceof BlockLiftTrackFloor || block.data instanceof BlockLiftButtons || block.data instanceof BlockLiftPanelBase;
	}

	private static void connect(World world, BlockPos blockPos1, BlockPos blockPos2, boolean isAdd) {
		final BlockEntity blockEntity = world.getBlockEntity(blockPos1);
		if (blockEntity != null && blockEntity.data instanceof BlockLiftButtons.BlockEntity) {
			((BlockLiftButtons.BlockEntity) blockEntity.data).registerFloor(blockPos2, isAdd);
		}
	}
}
