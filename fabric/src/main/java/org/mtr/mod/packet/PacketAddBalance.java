package org.mtr.mod.packet;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.InventoryHelper;
import org.mtr.mapping.mapper.PlayerHelper;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.data.TicketSystem;

public class PacketAddBalance extends PacketHandler {

	private final int index;

	private static final int EMERALD_TO_DOLLAR = 10;

	public PacketAddBalance(PacketBuffer packetBuffer) {
		index = packetBuffer.readInt();
	}

	public PacketAddBalance(int index) {
		this.index = index;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeInt(index);
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final ServerWorld serverWorld = serverPlayerEntity.getServerWorld();
		TicketSystem.addBalance(new World(serverWorld.data), new PlayerEntity(serverPlayerEntity.data), getAddAmount(index));
		InventoryHelper.remove(new Inventory(PlayerHelper.getPlayerInventory(new PlayerEntity(serverPlayerEntity.data)).data), itemStack -> itemStack.getItem().equals(Items.getEmeraldMapped()), (int) Math.pow(2, index), false);
//		serverWorld.playSound(null, serverPlayerEntity.getBlockPos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundCategory.getBlocksMapped(), 1, 1);
	}

	public static int getAddAmount(int index) {
		return (int) Math.ceil(Math.pow(2, index) * (EMERALD_TO_DOLLAR + index));
	}
}
