package org.mtr.mod.data;

import org.mtr.core.integration.Response;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.operation.ArrivalsRequest;
import org.mtr.core.operation.ArrivalsResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.mapping.holder.World;
import org.mtr.mod.Init;

import java.util.function.Consumer;

public final class ArrivalsCacheServer extends ArrivalsCache {

	private long millisOffset = 0;

	public static final ArrivalsCacheServer INSTANCE = new ArrivalsCacheServer();

	@Override
	public long getMillisOffset() {
		return millisOffset;
	}

	@Override
	protected void requestArrivalsFromServer(World world, LongAVLTreeSet platformIds, Consumer<ObjectList<ArrivalResponse>> callback) {
		Init.sendHttpRequest(
				"operation/arrivals",
				world,
				Utilities.getJsonObjectFromData(new ArrivalsRequest(new LongImmutableList(platformIds), 10, platformIds.size() * 10)).toString(),
				responseString -> {
					final Response response = Response.create(Utilities.parseJson(responseString));
					millisOffset = response.getCurrentTime() - System.currentTimeMillis();
					callback.accept(response.getData(ArrivalsResponse::new).getArrivals());
				}
		);
	}
}
