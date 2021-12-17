package mtr.item;

import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemResourcePackCreator extends Item {

	public ItemResourcePackCreator(Item.Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		if (!world.isClientSide()) {
			PacketTrainDataGuiServer.openResourcePackCreatorScreenS2C((ServerPlayer) user);
		}
		return super.use(world, user, hand);
	}
}
