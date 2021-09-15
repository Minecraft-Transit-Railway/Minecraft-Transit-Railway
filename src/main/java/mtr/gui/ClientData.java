package mtr.gui;

import mtr.data.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class ClientData {

	public static final Set<Station> STATIONS = new HashSet<>();
	public static final Set<Platform> PLATFORMS = new HashSet<>();
	public static final Set<Siding> SIDINGS = new HashSet<>();
	public static final Set<Route> ROUTES = new HashSet<>();
	public static final Set<Depot> DEPOTS = new HashSet<>();
	public static final Map<BlockPos, Map<BlockPos, Rail>> RAILS = new HashMap<>();
	public static final Set<TrainClient> TRAINS = new HashSet<>();
	public static final Map<Long, Set<Route.ScheduleEntry>> SCHEDULES_FOR_PLATFORM = new HashMap<>();

	public static final ClientCache DATA_CACHE = new ClientCache(STATIONS, PLATFORMS, SIDINGS, ROUTES, DEPOTS);

	public static void writeRails(MinecraftClient client, PacketByteBuf packet) {
		final Map<BlockPos, Map<BlockPos, Rail>> railsTemp = new HashMap<>();

		final int railsCount = packet.readInt();
		for (int i = 0; i < railsCount; i++) {
			final BlockPos startPos = packet.readBlockPos();
			final Map<BlockPos, Rail> railMap = new HashMap<>();
			final int railCount = packet.readInt();
			for (int j = 0; j < railCount; j++) {
				railMap.put(packet.readBlockPos(), new Rail(packet));
			}
			railsTemp.put(startPos, railMap);
		}

		client.execute(() -> clearAndAddAll(RAILS, railsTemp));
	}

	public static void updateTrains(MinecraftClient client, PacketByteBuf packet) {
		final Set<TrainClient> trainsToUpdate = new HashSet<>();

		final int trainsCount = packet.readInt();
		for (int i = 0; i < trainsCount; i++) {
			final TrainClient train = new TrainClient(packet);
			trainsToUpdate.add(train);
		}

		client.execute(() -> trainsToUpdate.forEach(newTrain -> {
			final TrainClient existingTrain = getTrainById(newTrain.id);
			if (existingTrain == null) {
				TRAINS.add(newTrain);
			} else {
				existingTrain.copyFromTrain(newTrain);
			}
		}));
	}

	public static void deleteTrains(MinecraftClient client, PacketByteBuf packet) {
		final Set<Long> trainIdsToDelete = new HashSet<>();

		final int trainsCount = packet.readInt();
		for (int i = 0; i < trainsCount; i++) {
			trainIdsToDelete.add(packet.readLong());
		}

		client.execute(() -> TRAINS.removeIf(train -> trainIdsToDelete.contains(train.id)));
	}

	public static void updateTrainRidingPosition(MinecraftClient client, PacketByteBuf packet) {
		final TrainClient train = getTrainById(packet.readLong());
		final float clientPercentageX = packet.readFloat();
		final float clientPercentageZ = packet.readFloat();
		if (train != null) {
			client.execute(() -> train.updateClientPercentages(client.player, clientPercentageX, clientPercentageZ));
		}
	}

	public static void updateSchedule(MinecraftClient client, PacketByteBuf packet) {
		final Map<Long, Set<Route.ScheduleEntry>> tempSchedulesForPlatform = new HashMap<>();
		final int platformCount = packet.readInt();
		for (int i = 0; i < platformCount; i++) {
			final long platformId = packet.readLong();
			final int scheduleCount = packet.readInt();
			for (int j = 0; j < scheduleCount; j++) {
				if (!tempSchedulesForPlatform.containsKey(platformId)) {
					tempSchedulesForPlatform.put(platformId, new HashSet<>());
				}
				tempSchedulesForPlatform.get(platformId).add(new Route.ScheduleEntry(packet));
			}
		}
		client.execute(() -> clearAndAddAll(SCHEDULES_FOR_PLATFORM, tempSchedulesForPlatform));
	}

	public static void receivePacket(PacketByteBuf packet) {
		final PacketByteBuf packetCopy = new PacketByteBuf(packet.copy());
		clearAndAddAll(STATIONS, deserializeData(packetCopy, Station::new));
		clearAndAddAll(PLATFORMS, deserializeData(packetCopy, Platform::new));
		clearAndAddAll(SIDINGS, deserializeData(packetCopy, Siding::new));
		clearAndAddAll(ROUTES, deserializeData(packetCopy, Route::new));
		clearAndAddAll(DEPOTS, deserializeData(packetCopy, Depot::new));

		TRAINS.clear();
		ClientData.DATA_CACHE.sync();
	}

	public static Station getStation(BlockPos pos) {
		try {
			return ClientData.STATIONS.stream().filter(station -> station.inArea(pos.getX(), pos.getZ())).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Platform getClosePlatform(BlockPos pos) {
		try {
			return ClientData.PLATFORMS.stream().filter(platform -> platform.isCloseToSavedRail(pos)).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static <T extends SerializedDataBase> Set<T> deserializeData(PacketByteBuf packet, Function<PacketByteBuf, T> supplier) {
		final Set<T> objects = new HashSet<>();
		final int dataCount = packet.readInt();
		for (int i = 0; i < dataCount; i++) {
			objects.add(supplier.apply(packet));
		}
		return objects;
	}

	private static <U> void clearAndAddAll(Set<U> target, Set<U> source) {
		target.clear();
		target.addAll(source);
	}

	private static <U, V> void clearAndAddAll(Map<U, V> target, Map<U, V> source) {
		target.clear();
		target.putAll(source);
	}

	private static TrainClient getTrainById(long id) {
		try {
			return TRAINS.stream().filter(item -> item.id == id).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
