package mtr.item;

import mtr.block.BlockStationNameWall;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class ItemBrush extends Item {

	public ItemBrush(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Block block = context.getLevel().getBlockState(context.getClickedPos()).getBlock();

		if (context.getPlayer() != null && context.getPlayer().isCrouching() && !context.getLevel().isClientSide) {
			if(block instanceof BlockStationNameWall) {
				return ((BlockStationNameWall) block).toggleMerge(context.getLevel(), context.getClickedPos());
			}

			return InteractionResult.FAIL;
		}

		return super.useOn(context);
	}
}