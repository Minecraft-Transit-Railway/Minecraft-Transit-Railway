package org.mtr.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.mtr.block.BlockLiftButtons;
import org.mtr.block.BlockLiftPanelBase;
import org.mtr.block.BlockLiftTrackFloor;

public class ItemLiftButtonsLinkModifier extends ItemBlockClickingBase {

	private final boolean isConnector;

	public ItemLiftButtonsLinkModifier(boolean isConnector, Item.Properties settings) {
		super(settings);
		this.isConnector = isConnector;
	}

	@Override
	protected void onStartClick(UseOnContext context) {
	}

	@Override
	protected void onEndClick(UseOnContext context, BlockPos posEnd) {
		final Level world = context.getLevel();
		final BlockPos posStart = context.getClickedPos();
		connect(world, posStart, posEnd, isConnector);
		connect(world, posEnd, posStart, isConnector);
	}

	@Override
	protected boolean clickCondition(UseOnContext context) {
		final Block block = context.getLevel().getBlockState(context.getClickedPos()).getBlock();
		return block instanceof BlockLiftTrackFloor || block instanceof BlockLiftButtons || block instanceof BlockLiftPanelBase;
	}

	private static void connect(Level world, BlockPos blockPos1, BlockPos blockPos2, boolean isAdd) {
		final BlockEntity blockEntity1 = world.getBlockEntity(blockPos1);
		final BlockEntity blockEntity2 = world.getBlockEntity(blockPos2);
		if (blockEntity1 != null && blockEntity2 instanceof BlockLiftTrackFloor.LiftTrackFloorBlockEntity) {
			if (blockEntity1 instanceof BlockLiftButtons.LiftButtonsBlockEntity) {
				((BlockLiftButtons.LiftButtonsBlockEntity) blockEntity1).registerFloor(blockPos2, isAdd);
			}
			if (blockEntity1 instanceof BlockLiftPanelBase.BlockEntityBase) {
				((BlockLiftPanelBase.BlockEntityBase) blockEntity1).registerFloor(world, blockPos2, isAdd);
			}
		}
	}
}
