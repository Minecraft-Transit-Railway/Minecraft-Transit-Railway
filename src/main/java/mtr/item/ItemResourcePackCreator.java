package mtr.item;

import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemResourcePackCreator extends Item {

	public ItemResourcePackCreator(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient()) {
			PacketTrainDataGuiServer.openResourcePackCreatorScreenS2C((ServerPlayerEntity) user);
		}
		return super.use(world, user, hand);
	}
}
