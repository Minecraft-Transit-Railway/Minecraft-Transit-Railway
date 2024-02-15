package org.mtr.mod.packet;

import org.mtr.core.integration.Response;
import org.mtr.core.operation.ArrivalsRequest;
import org.mtr.core.operation.ArrivalsResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongImmutableList;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.client.MinecraftClientData;

import javax.annotation.Nonnull;

public final class PacketFetchArrivals extends PacketRequestResponseBase implements Utilities {

	private final long requestKey;

	private static long millisOffset;

	public PacketFetchArrivals(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
		requestKey = packetBufferReceiver.readLong();
	}

	public PacketFetchArrivals(long requestKey, LongImmutableList platformIds, int count, int page, boolean realtimeOnly) {
		super(Utilities.getJsonObjectFromData(new ArrivalsRequest(platformIds, count, page, realtimeOnly)).toString());
		this.requestKey = requestKey;
	}

	private PacketFetchArrivals(String content, long requestKey) {
		super(content);
		this.requestKey = requestKey;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		super.write(packetBufferSender);
		packetBufferSender.writeLong(requestKey);
	}

	@Override
	protected void runClient(Response response) {
		MinecraftClientData.getInstance().writeArrivalRequest(requestKey, response.getData(ArrivalsResponse::new));
		millisOffset = response.getCurrentTime() - System.currentTimeMillis();
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketFetchArrivals(content, requestKey);
	}

	@Nonnull
	@Override
	protected String getEndpoint() {
		return "operation/arrivals";
	}

	@Override
	protected PacketRequestResponseBase.ResponseType responseType() {
		return PacketRequestResponseBase.ResponseType.PLAYER;
	}

	public static long getMillisOffset() {
		return millisOffset;
	}
}
