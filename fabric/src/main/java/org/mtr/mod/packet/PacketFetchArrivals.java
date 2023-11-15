package org.mtr.mod.packet;

import org.mtr.core.integration.Response;
import org.mtr.core.operation.ArrivalsRequest;
import org.mtr.core.operation.ArrivalsResponse;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongImmutableList;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.registry.Registry;
import org.mtr.mod.client.ClientData;

public class PacketFetchArrivals extends PacketHandler implements Utilities {

	private final long requestKey;
	private final ArrivalsRequest arrivalsRequest;
	private final Response response;

	public static long millisOffset;

	public PacketFetchArrivals(PacketBuffer packetBuffer) {
		requestKey = packetBuffer.readLong();
		final boolean isResponse = packetBuffer.readBoolean();
		final JsonObject jsonObject = PacketData.parseJson(readString(packetBuffer));
		arrivalsRequest = isResponse ? null : new ArrivalsRequest(new JsonReader(jsonObject));
		response = isResponse ? Response.create(jsonObject) : null;
	}

	public PacketFetchArrivals(long requestKey, LongImmutableList platformIds, int count, int page, boolean realtimeOnly) {
		this.requestKey = requestKey;
		arrivalsRequest = new ArrivalsRequest(platformIds, count, page, realtimeOnly);
		response = null;
	}

	private PacketFetchArrivals(long requestKey, Response response) {
		this.requestKey = requestKey;
		arrivalsRequest = null;
		this.response = response;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeLong(requestKey);
		packetBuffer.writeBoolean(arrivalsRequest == null);
		packetBuffer.writeString((arrivalsRequest == null ? response.getJson() : Utilities.getJsonObjectFromData(arrivalsRequest)).toString());
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		if (arrivalsRequest != null) {
			PacketData.sendHttpRequest("operation/arrivals", Utilities.getJsonObjectFromData(arrivalsRequest), data -> Registry.sendPacketToClient(serverPlayerEntity, new PacketFetchArrivals(requestKey, Response.create(data))));
		}
	}

	@Override
	public void runClientQueued() {
		if (response != null) {
			ClientData.instance.writeArrivalRequest(requestKey, response.getData(ArrivalsResponse::new));
			millisOffset = response.getCurrentTime() - System.currentTimeMillis();
		}
	}
}
