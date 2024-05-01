package org.mtr.mod.data;

import org.mtr.core.operation.ArrivalResponse;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.mapping.holder.World;

import java.util.function.Consumer;

public abstract class ArrivalsCache {

	private long nextRequest;
	private final LongAVLTreeSet queuedPlatformIds = new LongAVLTreeSet();
	private final ObjectArrayList<ArrivalResponse> arrivalResponseCache = new ObjectArrayList<>();

	private static final int CACHED_ARRIVAL_REQUESTS_MILLIS = 3000;

	public ObjectArrayList<ArrivalResponse> requestArrivals(World world, LongCollection platformIds) {
		queuedPlatformIds.addAll(platformIds);

		if (System.currentTimeMillis() >= nextRequest) {
			requestArrivalsFromServer(world, queuedPlatformIds, arrivalResponseList -> {
				arrivalResponseCache.clear();
				arrivalResponseCache.addAll(arrivalResponseList);
			});
			queuedPlatformIds.clear();
			nextRequest = System.currentTimeMillis() + CACHED_ARRIVAL_REQUESTS_MILLIS;
		}

		final ObjectArrayList<ArrivalResponse> arrivals = new ObjectArrayList<>();
		arrivalResponseCache.forEach(arrivalResponse -> {
			if (platformIds.contains(arrivalResponse.getPlatformId())) {
				arrivals.add(arrivalResponse);
			}
		});

		return arrivals;
	}

	public abstract long getMillisOffset();

	protected abstract void requestArrivalsFromServer(World world, LongAVLTreeSet platformIds, Consumer<ObjectList<ArrivalResponse>> callback);
}
