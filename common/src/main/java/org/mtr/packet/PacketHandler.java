package org.mtr.packet;


import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class PacketHandler {

	public abstract void write(PacketBufferSender packetBufferSender);

	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
	}

	public void runClient() {
	}
}
