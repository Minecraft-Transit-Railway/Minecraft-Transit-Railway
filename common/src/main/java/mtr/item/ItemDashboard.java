package mtr.item;

import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemDashboard extends Item {

	public ItemDashboard(Item.Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand interactionHand) {
		if (!world.isClientSide()) {
			PacketTrainDataGuiServer.openDashboardScreenS2C((ServerPlayer) player);
		}
		return super.use(world, player, interactionHand);
	}
}
