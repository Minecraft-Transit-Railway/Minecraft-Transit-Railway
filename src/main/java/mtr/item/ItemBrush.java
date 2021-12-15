package mtr.item;

import mtr.block.BlockPSDTop;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class ItemBrush extends Item {

	public ItemBrush(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();

		if (block instanceof BlockPSDTop && context.getPlayer().isSneaking() && !context.getWorld().isClient) {
			PacketTrainDataGuiServer.openPSDFilterScreenS2C((ServerPlayerEntity) context.getPlayer(), context.getBlockPos());
			return ActionResult.SUCCESS;
		}

		return super.useOnBlock(context);
	}
}
