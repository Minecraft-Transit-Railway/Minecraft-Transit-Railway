package org.mtr.mod.packet;

import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;

public final class PacketRequestData extends PacketHandler {

	private final boolean forceUpdate;

	public PacketRequestData(PacketBufferReceiver packetBufferReceiver) {
		forceUpdate = packetBufferReceiver.readBoolean();
	}

	public PacketRequestData(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeBoolean(forceUpdate);
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		Init.schedulePlayerUpdate(serverPlayerEntity, forceUpdate);
	}
}
