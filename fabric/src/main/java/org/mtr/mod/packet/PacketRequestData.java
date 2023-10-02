package org.mtr.mod.packet;

import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.Init;
import org.mtr.mod.client.ClientData;

public class PacketRequestData extends PacketHandler {

	private final boolean forceUpdate;

	public PacketRequestData(PacketBuffer packetBuffer) {
		forceUpdate = packetBuffer.readBoolean();
	}

	public PacketRequestData(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
		if (forceUpdate) {
			ClientData.instance.vehicles.clear();
		}
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeBoolean(forceUpdate);
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		Init.schedulePlayerUpdate(serverPlayerEntity, forceUpdate);
	}
}
