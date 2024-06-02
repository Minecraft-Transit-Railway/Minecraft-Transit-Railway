package org.mtr.mod.packet;

import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.Long2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.data.ArrivalsCacheServer;

import java.util.Random;
import java.util.stream.Collectors;

public final class PacketFetchArrivals extends PacketHandler {

	private final LongAVLTreeSet platformIds;
	private final long responseTime;
	private final ObjectArrayList<String> responses = new ObjectArrayList<>();
	private final long callbackId;

	private static final Long2ObjectAVLTreeMap<Callback> CALLBACKS = new Long2ObjectAVLTreeMap<>();

	public PacketFetchArrivals(PacketBufferReceiver packetBufferReceiver) {
		platformIds = new LongAVLTreeSet();
		final int platformIdCount = packetBufferReceiver.readInt();
		for (int i = 0; i < platformIdCount; i++) {
			platformIds.add(packetBufferReceiver.readLong());
		}

		responseTime = packetBufferReceiver.readLong();

		final int responseCount = packetBufferReceiver.readInt();
		for (int i = 0; i < responseCount; i++) {
			responses.add(packetBufferReceiver.readString());
		}

		callbackId = packetBufferReceiver.readLong();
	}

	public PacketFetchArrivals(LongAVLTreeSet platformIds, Callback callback) {
		this.platformIds = platformIds;
		responseTime = 0;
		callbackId = new Random().nextLong();
		CALLBACKS.put(callbackId, callback);
	}

	private PacketFetchArrivals(long responseTime, ObjectArrayList<ArrivalResponse> arrivalResponses, long callbackId) {
		platformIds = new LongAVLTreeSet();
		this.responseTime = responseTime;
		arrivalResponses.forEach(arrivalResponse -> responses.add(Utilities.getJsonObjectFromData(arrivalResponse).toString()));
		this.callbackId = callbackId;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeInt(platformIds.size());
		platformIds.forEach(packetBufferSender::writeLong);
		packetBufferSender.writeLong(responseTime);
		packetBufferSender.writeInt(responses.size());
		responses.forEach(packetBufferSender::writeString);
		packetBufferSender.writeLong(callbackId);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final ArrivalsCacheServer instance = ArrivalsCacheServer.getInstance(serverPlayerEntity.getServerWorld());
		Init.REGISTRY.sendPacketToClient(serverPlayerEntity, new PacketFetchArrivals(instance.getMillisOffset() + System.currentTimeMillis(), instance.requestArrivals(platformIds), callbackId));
	}

	@Override
	public void runClient() {
		final Callback callback = CALLBACKS.remove(callbackId);
		if (callback != null) {
			callback.accept(responseTime, responses.stream().map(response -> new ArrivalResponse(new JsonReader(Utilities.parseJson(response)))).collect(Collectors.toCollection(ObjectArrayList::new)));
		}
	}

	@FunctionalInterface
	public interface Callback {
		void accept(long responseTime, ObjectList<ArrivalResponse> arrivalResponseList);
	}
}
