package org.mtr.packet;

import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.PersistentState;
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
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final PersistentStateData persistentStateData = serverPlayerEntity.getServerWorld().getPersistentStateManager().getOrCreate(new PersistentState.Type<>(PersistentStateData::new, (nbt, wrapperLookup) -> new PersistentStateData(nbt), DataFixTypes.LEVEL), MTR.MOD_ID);
		persistentStateData.setRouteIdHasDisabledAnnouncements(routeId, isDisabled);
	}
}
