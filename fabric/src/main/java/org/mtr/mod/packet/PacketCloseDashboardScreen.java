package org.mtr.mod.packet;

import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.Init;

public class PacketCloseDashboardScreen extends PacketHandler {

	@Override
	public void write(PacketBuffer packetBuffer) {
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		Init.updatePlayerPosition(minecraftServer, serverPlayerEntity);
	}
}
