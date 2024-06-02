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
	private final int cachedMillis;

	protected ArrivalsCache(int cachedMillis) {
		this.cachedMillis = cachedMillis;
	}

	/**
	 * @deprecated Use {@link ArrivalsCache#requestArrivals(LongCollection)} instead
	 */
	@Deprecated
	public final ObjectArrayList<ArrivalResponse> requestArrivals(World world, LongCollection platformIds) {
		return requestArrivals(platformIds);
	}

	public final ObjectArrayList<ArrivalResponse> requestArrivals(LongCollection platformIds) {
		if (queuedPlatformIds.isEmpty() && canSendRequest()) {
			nextRequest = System.currentTimeMillis() + 100;
		}

		queuedPlatformIds.addAll(platformIds);

		final ObjectArrayList<ArrivalResponse> arrivals = new ObjectArrayList<>();
		arrivalResponseCache.forEach(arrivalResponse -> {
			if (platformIds.contains(arrivalResponse.getPlatformId())) {
				arrivals.add(arrivalResponse);
			}
		});

		return arrivals;
	}

	public final void tick() {
		if (!queuedPlatformIds.isEmpty() && canSendRequest()) {
			requestArrivalsFromServer(queuedPlatformIds, arrivalResponseList -> {
				arrivalResponseCache.clear();
				arrivalResponseCache.addAll(arrivalResponseList);
			});
			queuedPlatformIds.clear();
			nextRequest = System.currentTimeMillis() + cachedMillis;
		}
	}

	private boolean canSendRequest() {
		return System.currentTimeMillis() >= nextRequest;
	}

	public abstract long getMillisOffset();

	protected abstract void requestArrivalsFromServer(LongAVLTreeSet platformIds, Consumer<ObjectList<ArrivalResponse>> callback);
}
