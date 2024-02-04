package org.mtr.mod.packet;

import org.mtr.core.integration.Response;
import org.mtr.core.operation.ArrivalsRequest;
import org.mtr.core.operation.ArrivalsResponse;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongImmutableList;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.client.ClientData;

public final class PacketFetchArrivals extends PacketRequestResponseBase<ArrivalsRequest> implements Utilities {

	private final long requestKey;

	private static long millisOffset;

	public PacketFetchArrivals(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
		requestKey = packetBufferReceiver.readLong();
	}

	public PacketFetchArrivals(long requestKey, LongImmutableList platformIds, int count, int page, boolean realtimeOnly) {
		super(new ArrivalsRequest(platformIds, count, page, realtimeOnly));
		this.requestKey = requestKey;
	}

	private PacketFetchArrivals(long requestKey, Response response) {
		super(response);
		this.requestKey = requestKey;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		super.write(packetBufferSender);
		packetBufferSender.writeLong(requestKey);
	}

	@Override
	protected PacketRequestResponseBase<ArrivalsRequest> createInstance(Response response) {
		return new PacketFetchArrivals(requestKey, response);
	}

	@Override
	protected ArrivalsRequest createRequest(JsonReader jsonReader) {
		return new ArrivalsRequest(jsonReader);
	}

	@Override
	protected String getEndpoint() {
		return "operation/arrivals";
	}

	@Override
	protected void runClient(Response response) {
		ClientData.getInstance().writeArrivalRequest(requestKey, response.getData(ArrivalsResponse::new));
		millisOffset = response.getCurrentTime() - System.currentTimeMillis();
	}

	public static long getMillisOffset() {
		return millisOffset;
	}
}
