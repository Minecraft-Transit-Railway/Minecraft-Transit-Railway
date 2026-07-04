package org.mtr.packet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public abstract class PacketHandler {

	public abstract void write(PacketBufferSender packetBufferSender);

	public void runServer(MinecraftServer minecraftServer, ServerPlayer serverPlayerEntity) {
	}

	public void runClient() {
	}
}
