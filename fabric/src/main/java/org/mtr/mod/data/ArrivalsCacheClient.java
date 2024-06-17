package org.mtr.mod.data;

import org.mtr.core.operation.ArrivalResponse;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.mod.InitClient;
import org.mtr.mod.packet.PacketFetchArrivals;

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
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketFetchArrivals(platformIds, (responseTime, arrivalResponseList) -> {
			if (responseTime > 0) {
				millisOffset = responseTime - System.currentTimeMillis();
			}
			callback.accept(arrivalResponseList);
		}));
	}
}
