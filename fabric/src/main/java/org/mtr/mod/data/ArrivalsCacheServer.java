package org.mtr.mod.data;

import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.operation.ArrivalsRequest;
import org.mtr.core.operation.ArrivalsResponse;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mod.Init;

import java.util.function.Consumer;

public final class ArrivalsCacheServer extends ArrivalsCache {

	private long millisOffset = 0;
	private final World world;

	private static final Object2ObjectAVLTreeMap<String, ArrivalsCacheServer> INSTANCES = new Object2ObjectAVLTreeMap<>();

	private ArrivalsCacheServer(World world) {
		super(1000);
		this.world = world;
	}

	@Override
	public long getMillisOffset() {
		return millisOffset;
	}

	@Override
	protected void requestArrivalsFromServer(LongAVLTreeSet platformIds, Consumer<ObjectList<ArrivalResponse>> callback) {
		Init.sendMessageC2S(
				OperationProcessor.ARRIVALS,
				world.getServer(),
				world,
				new ArrivalsRequest(new LongImmutableList(platformIds), 10, -1),
				arrivalsResponse -> {
					millisOffset = arrivalsResponse.getCurrentTime() - System.currentTimeMillis();
					callback.accept(arrivalsResponse.getArrivals());
				},
				ArrivalsResponse.class
		);
	}

	public static ArrivalsCacheServer getInstance(ServerWorld serverWorld) {
		final World world = new World(serverWorld.data);
		return INSTANCES.computeIfAbsent(MinecraftServerHelper.getWorldId(world).data.toString(), worldId -> new ArrivalsCacheServer(world));
	}

	public static void tickAll() {
		INSTANCES.forEach((worldId, arrivalsCacheServer) -> arrivalsCacheServer.tick());
	}
}
