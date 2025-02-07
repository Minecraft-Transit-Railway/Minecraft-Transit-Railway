package org.mtr.mod.packet;

import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.mapper.PersistenceStateExtension;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.data.PersistentStateData;

public final class PacketSetRouteIdHasDisabledAnnouncements extends PacketHandler {

	private final long routeId;
	private final boolean isDisabled;

	public PacketSetRouteIdHasDisabledAnnouncements(PacketBufferReceiver packetBufferReceiver) {
		routeId = packetBufferReceiver.readLong();
		isDisabled = packetBufferReceiver.readBoolean();
	}

	public PacketSetRouteIdHasDisabledAnnouncements(long routeId, boolean isDisabled) {
		this.routeId = routeId;
		this.isDisabled = isDisabled;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(routeId);
		packetBufferSender.writeBoolean(isDisabled);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final PersistentStateData persistentStateData = (PersistentStateData) PersistenceStateExtension.register(serverPlayerEntity.getServerWorld(), PersistentStateData::new, Init.MOD_ID);
		persistentStateData.setRouteIdHasDisabledAnnouncements(routeId, isDisabled);
	}
}
