package org.mtr.mod.packet;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.Long2ObjectAVLTreeMap;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.mapper.PersistenceStateExtension;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.PersistentStateData;

import java.util.Random;
import java.util.function.Consumer;

public final class PacketCheckRouteIdHasDisabledAnnouncements extends PacketHandler {

	private final long routeId;
	private final boolean isDisabled;
	private final long callbackId;

	private static final Long2ObjectAVLTreeMap<Consumer<Boolean>> CALLBACKS = new Long2ObjectAVLTreeMap<>();

	public PacketCheckRouteIdHasDisabledAnnouncements(PacketBufferReceiver packetBufferReceiver) {
		routeId = packetBufferReceiver.readLong();
		isDisabled = packetBufferReceiver.readBoolean();
		callbackId = packetBufferReceiver.readLong();
	}

	public PacketCheckRouteIdHasDisabledAnnouncements(long routeId, Consumer<Boolean> callback) {
		this.routeId = routeId;
		isDisabled = false;
		callbackId = new Random().nextLong();
		CALLBACKS.put(callbackId, callback);
	}

	private PacketCheckRouteIdHasDisabledAnnouncements(long routeId, boolean isDisabled, long callbackId) {
		this.routeId = routeId;
		this.isDisabled = isDisabled;
		this.callbackId = callbackId;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(routeId);
		packetBufferSender.writeBoolean(isDisabled);
		packetBufferSender.writeLong(callbackId);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final PersistentStateData persistentStateData = (PersistentStateData) PersistenceStateExtension.register(serverPlayerEntity.getServerWorld(), PersistentStateData::new, Init.MOD_ID);
		Init.REGISTRY.sendPacketToClient(serverPlayerEntity, new PacketCheckRouteIdHasDisabledAnnouncements(routeId, persistentStateData.getRouteIdHasDisabledAnnouncements(routeId), callbackId));
	}

	@Override
	public void runClient() {
		MinecraftClientData.getInstance().setRouteIdHasDisabledAnnouncements(routeId, isDisabled);
		final Consumer<Boolean> callback = CALLBACKS.remove(callbackId);
		if (callback != null) {
			callback.accept(isDisabled);
		}
	}
}
