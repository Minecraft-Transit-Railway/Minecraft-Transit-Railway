package org.mtr.packet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.mtr.MTR;
import org.mtr.data.PersistentStateData;

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
	public void runServer(MinecraftServer minecraftServer, ServerPlayer serverPlayerEntity) {
		final PersistentStateData persistentStateData = serverPlayerEntity.serverLevel().getDataStorage().computeIfAbsent(new SavedData.Factory<>(PersistentStateData::new, (nbt, wrapperLookup) -> new PersistentStateData(nbt), DataFixTypes.LEVEL), MTR.MOD_ID);
		persistentStateData.setRouteIdHasDisabledAnnouncements(routeId, isDisabled);
	}
}
