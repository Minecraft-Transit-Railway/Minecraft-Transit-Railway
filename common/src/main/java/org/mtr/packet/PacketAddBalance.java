package org.mtr.packet;

import net.minecraft.inventory.Inventories;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.mtr.data.TicketSystem;

public final class PacketAddBalance extends PacketHandler {

	private final int index;

	private static final int EMERALD_TO_DOLLAR = 10;

	public PacketAddBalance(PacketBufferReceiver packetBufferReceiver) {
		index = packetBufferReceiver.readInt();
	}

	public PacketAddBalance(int index) {
		this.index = index;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeInt(index);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final ServerWorld serverWorld = serverPlayerEntity.getServerWorld();
		TicketSystem.addBalance(serverWorld, serverPlayerEntity, getAddAmount(index));
		Inventories.remove(serverPlayerEntity.getInventory(), itemStack -> itemStack.getItem().equals(Items.EMERALD), (int) Math.pow(2, index), false);
		serverWorld.playSound(null, serverPlayerEntity.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1, 1);
	}

	public static int getAddAmount(int index) {
		return (int) Math.ceil(Math.pow(2, index) * (EMERALD_TO_DOLLAR + index));
	}
}
