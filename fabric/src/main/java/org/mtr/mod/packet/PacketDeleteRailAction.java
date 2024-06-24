package org.mtr.mod.packet;

import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;

public final class PacketDeleteRailAction extends PacketHandler {

	private final long id;

	public PacketDeleteRailAction(PacketBufferReceiver packetBufferReceiver) {
		id = packetBufferReceiver.readLong();
	}

	public PacketDeleteRailAction(long id) {
		this.id = id;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(id);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		Init.getRailActionModule(serverPlayerEntity.getServerWorld(), railActionModule -> railActionModule.removeRailAction(id));
	}
}
