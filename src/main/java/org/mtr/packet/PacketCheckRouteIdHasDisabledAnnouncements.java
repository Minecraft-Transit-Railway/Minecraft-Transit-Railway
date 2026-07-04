package org.mtr.packet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.mtr.MTR;
import org.mtr.client.MinecraftClientData;
import org.mtr.data.PersistentStateData;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.Long2ObjectAVLTreeMap;
import org.mtr.registry.RegistryServer;

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
	public void runServer(MinecraftServer minecraftServer, ServerPlayer serverPlayerEntity) {
		final PersistentStateData persistentStateData = serverPlayerEntity.serverLevel().getDataStorage().computeIfAbsent(new SavedData.Factory<>(PersistentStateData::new, (nbt, wrapperLookup) -> new PersistentStateData(nbt), DataFixTypes.LEVEL), MTR.MOD_ID);
		RegistryServer.sendPacketToClient(serverPlayerEntity, new PacketCheckRouteIdHasDisabledAnnouncements(routeId, persistentStateData.getRouteIdHasDisabledAnnouncements(routeId), callbackId));
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
