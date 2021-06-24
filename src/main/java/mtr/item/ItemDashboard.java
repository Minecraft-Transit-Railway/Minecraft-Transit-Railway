package mtr.item;

import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemDashboard extends Item {

	public ItemDashboard(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient()) {
			PacketTrainDataGuiServer.openDashboardScreenS2C((ServerPlayerEntity) user);
		}
		return super.use(world, user, hand);
	}
}
