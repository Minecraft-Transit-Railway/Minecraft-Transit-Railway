package org.mtr.packet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.mtr.MTR;

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
		MTR.getRailActionModule(serverPlayerEntity.getServerWorld(), railActionModule -> railActionModule.removeRailAction(id));
	}
}
