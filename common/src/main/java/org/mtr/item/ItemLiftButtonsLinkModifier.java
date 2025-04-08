package org.mtr.item;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.block.BlockLiftButtons;
import org.mtr.block.BlockLiftPanelBase;
import org.mtr.block.BlockLiftTrackFloor;

public class ItemLiftButtonsLinkModifier extends ItemBlockClickingBase {

	private final boolean isConnector;

	public ItemLiftButtonsLinkModifier(boolean isConnector, Item.Settings settings) {
		super(settings);
		this.isConnector = isConnector;
	}

	@Override
	protected void onStartClick(ItemUsageContext context) {
	}

	@Override
	protected void onEndClick(ItemUsageContext context, BlockPos posEnd) {
		final World world = context.getWorld();
		final BlockPos posStart = context.getBlockPos();
		connect(world, posStart, posEnd, isConnector);
		connect(world, posEnd, posStart, isConnector);
	}

	@Override
	protected boolean clickCondition(ItemUsageContext context) {
		final Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
		return block instanceof BlockLiftTrackFloor || block instanceof BlockLiftButtons || block instanceof BlockLiftPanelBase;
	}

	private static void connect(World world, BlockPos blockPos1, BlockPos blockPos2, boolean isAdd) {
		final BlockEntity blockEntity1 = world.getBlockEntity(blockPos1);
		final BlockEntity blockEntity2 = world.getBlockEntity(blockPos2);
		if (blockEntity1 != null && blockEntity2 != null && blockEntity2 instanceof BlockLiftTrackFloor.LiftTrackFloorBlockEntity) {
			if (blockEntity1 instanceof BlockLiftButtons.LiftButtonsBlockEntity) {
				((BlockLiftButtons.LiftButtonsBlockEntity) blockEntity1).registerFloor(blockPos2, isAdd);
			}
			if (blockEntity1 instanceof BlockLiftPanelBase.BlockEntityBase) {
				((BlockLiftPanelBase.BlockEntityBase) blockEntity1).registerFloor(world, blockPos2, isAdd);
			}
		}
	}
}
