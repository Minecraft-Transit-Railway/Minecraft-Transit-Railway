package org.mtr.mod.data;

import org.mtr.core.operation.ArrivalsResponse;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.Long2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectLongImmutablePair;
import org.mtr.mod.InitClient;
import org.mtr.mod.packet.PacketFetchArrivals;

public final class ArrivalsCache {

	private final Long2ObjectAVLTreeMap<ObjectLongImmutablePair<ArrivalsResponse>> arrivalRequests = new Long2ObjectAVLTreeMap<>();

	public static ArrivalsCache INSTANCE = new ArrivalsCache();
	private static final int CACHED_ARRIVAL_REQUESTS_MILLIS = 3000;

	public ArrivalsResponse requestArrivals(long requestKey, LongImmutableList platformIds, int maxCountPerPlatform, int maxCountTotal) {
		final ObjectLongImmutablePair<ArrivalsResponse> arrivalData = arrivalRequests.get(requestKey);
		if (arrivalData == null || arrivalData.rightLong() < System.currentTimeMillis()) {
			if (!platformIds.isEmpty()) {
				InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketFetchArrivals(requestKey, platformIds, maxCountPerPlatform, maxCountTotal));
			}

			final ArrivalsResponse arrivalsResponse;
			if (arrivalData == null) {
				arrivalsResponse = new ArrivalsResponse();
			} else {
				arrivalsResponse = arrivalData.left();
			}

			writeArrivalRequest(requestKey, arrivalsResponse);
			return arrivalsResponse;
		} else {
			return arrivalData.left();
		}
	}

	public void writeArrivalRequest(long requestKey, ArrivalsResponse arrivalsResponse) {
		arrivalRequests.put(requestKey, new ObjectLongImmutablePair<>(arrivalsResponse, System.currentTimeMillis() + CACHED_ARRIVAL_REQUESTS_MILLIS));
	}
}
