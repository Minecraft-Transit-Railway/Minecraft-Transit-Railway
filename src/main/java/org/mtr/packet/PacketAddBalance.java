package org.mtr.packet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.Items;
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
	public void runServer(MinecraftServer minecraftServer, ServerPlayer serverPlayerEntity) {
		final ServerLevel serverWorld = serverPlayerEntity.serverLevel();
		TicketSystem.addBalance(serverWorld, serverPlayerEntity, getAddBalanceAmount(index));
		ContainerHelper.clearOrCountMatchingItems(serverPlayerEntity.getInventory(), itemStack -> itemStack.getItem().equals(Items.EMERALD), getEmeraldAmount(index), false);
		serverWorld.playSound(null, serverPlayerEntity.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1, 1);
	}

	public static int getAddBalanceAmount(int index) {
		return (int) Math.ceil(Math.pow(2, index) * (EMERALD_TO_DOLLAR + index));
	}

	public static int getEmeraldAmount(int index) {
		return (int) Math.pow(2, index);
	}
}
