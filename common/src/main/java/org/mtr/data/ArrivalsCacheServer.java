package org.mtr.data;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.longs.LongImmutableList;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.mtr.MTR;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.operation.ArrivalsRequest;
import org.mtr.core.operation.ArrivalsResponse;
import org.mtr.core.servlet.OperationProcessor;

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
		MTR.sendMessageC2S(
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
		return INSTANCES.computeIfAbsent(serverWorld.getRegistryKey().getValue().toString(), worldId -> new ArrivalsCacheServer(serverWorld));
	}

	public static void tickAll() {
		INSTANCES.forEach((worldId, arrivalsCacheServer) -> arrivalsCacheServer.tick());
	}
}
