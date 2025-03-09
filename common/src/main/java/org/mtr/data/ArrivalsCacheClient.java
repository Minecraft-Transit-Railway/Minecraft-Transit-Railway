package org.mtr.data;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.packet.PacketFetchArrivals;
import org.mtr.registry.RegistryClient;

import java.util.function.Consumer;

public final class ArrivalsCacheClient extends ArrivalsCache {

	private long millisOffset = 0;

	public static final ArrivalsCacheClient INSTANCE = new ArrivalsCacheClient();

	private ArrivalsCacheClient() {
		super(3000);
	}

	@Override
	public long getMillisOffset() {
		return millisOffset;
	}

	@Override
	protected void requestArrivalsFromServer(LongAVLTreeSet platformIds, Consumer<ObjectList<ArrivalResponse>> callback) {
		RegistryClient.sendPacketToServer(new PacketFetchArrivals(platformIds, (responseTime, arrivalResponseList) -> {
			if (responseTime > 0) {
				millisOffset = responseTime - System.currentTimeMillis();
			}
			callback.accept(arrivalResponseList);
		}));
	}
}
