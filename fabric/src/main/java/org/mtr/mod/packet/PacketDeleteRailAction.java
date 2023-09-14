package org.mtr.mod.packet;

import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.Init;

public class PacketDeleteRailAction extends PacketHandler {

	private final long id;

	public PacketDeleteRailAction(PacketBuffer packetBuffer) {
		id = packetBuffer.readLong();
	}

	public PacketDeleteRailAction(long id) {
		this.id = id;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeLong(id);
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		Init.getRailActionModule(serverPlayerEntity.getServerWorld(), railActionModule -> railActionModule.removeRailAction(id));
	}
}
