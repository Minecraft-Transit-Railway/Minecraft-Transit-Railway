package mtr.data;

import mtr.packet.PacketTrainDataGuiServer;
import mtr.path.PathData;
import mtr.path.PathFinder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Depot extends AreaBase {

	public int clientPathGenerationSuccessfulSegments;
	public long lastDeployedMillis;
	private int deployIndex;
	private int departureOffset;

	public final List<Long> routeIds = new ArrayList<>();

	private final int[] frequencies = new int[HOURS_IN_DAY];
	private final Map<Long, TrainServer> deployableSidings = new HashMap<>();

	public static final int HOURS_IN_DAY = 24;
	public static final int TRAIN_FREQUENCY_MULTIPLIER = 4;
	public static final int TICKS_PER_HOUR = 1000;
	public static final int MILLIS_PER_TICK = 50;
	private static final int TICKS_PER_DAY = HOURS_IN_DAY * TICKS_PER_HOUR;
	private static final int MAX_DEPLOYED_MILLIS = 3600000;

	private static final String KEY_ROUTE_IDS = "route_ids";
	private static final String KEY_FREQUENCIES = "frequencies";
	private static final String KEY_LAST_DEPLOYED = "last_deployed";
	private static final String KEY_DEPLOY_INDEX = "deploy_index";

	public Depot() {
		super();
	}

	public Depot(long id) {
		super(id);
	}

	public Depot(NbtCompound nbtCompound) {
		super(nbtCompound);

		final long[] routeIdsArray = nbtCompound.getLongArray(KEY_ROUTE_IDS);
		for (final long routeId : routeIdsArray) {
			routeIds.add(routeId);
		}

		for (int i = 0; i < HOURS_IN_DAY; i++) {
			frequencies[i] = nbtCompound.getInt(KEY_FREQUENCIES + i);
		}

		lastDeployedMillis = System.currentTimeMillis() - nbtCompound.getLong(KEY_LAST_DEPLOYED);
		deployIndex = nbtCompound.getInt(KEY_DEPLOY_INDEX);
	}

	public Depot(PacketByteBuf packet) {
		super(packet);

		final int routeIdCount = packet.readInt();
		for (int i = 0; i < routeIdCount; i++) {
			routeIds.add(packet.readLong());
		}

		for (int i = 0; i < HOURS_IN_DAY; i++) {
			frequencies[i] = packet.readInt();
		}

		lastDeployedMillis = packet.readLong();
		deployIndex = packet.readInt();
	}

	@Override
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = super.toCompoundTag();

		nbtCompound.putLongArray(KEY_ROUTE_IDS, routeIds);

		for (int i = 0; i < HOURS_IN_DAY; i++) {
			nbtCompound.putInt(KEY_FREQUENCIES + i, frequencies[i]);
		}

		nbtCompound.putLong(KEY_LAST_DEPLOYED, System.currentTimeMillis() - lastDeployedMillis);
		nbtCompound.putInt(KEY_DEPLOY_INDEX, deployIndex);

		return nbtCompound;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);

		packet.writeInt(routeIds.size());
		routeIds.forEach(packet::writeLong);

		for (final int frequency : frequencies) {
			packet.writeInt(frequency);
		}

		packet.writeLong(lastDeployedMillis);
		packet.writeInt(deployIndex);
	}

	@Override
	public void update(String key, PacketByteBuf packet) {
		if (KEY_FREQUENCIES.equals(key)) {
			name = packet.readString(PACKET_STRING_READ_LENGTH);
			color = packet.readInt();
			for (int i = 0; i < HOURS_IN_DAY; i++) {
				frequencies[i] = packet.readInt();
			}
			routeIds.clear();
			final int routeIdCount = packet.readInt();
			for (int i = 0; i < routeIdCount; i++) {
				routeIds.add(packet.readLong());
			}
		} else {
			super.update(key, packet);
		}
	}

	public int getFrequency(int index) {
		if (index >= 0 && index < frequencies.length) {
			return frequencies[index];
		} else {
			return 0;
		}
	}

	public void setFrequency(int newFrequency, int index) {
		if (index >= 0 && index < frequencies.length) {
			frequencies[index] = newFrequency;
		}
	}

	public void setData(Consumer<PacketByteBuf> sendPacket) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_FREQUENCIES);
		packet.writeString(name);
		packet.writeInt(color);
		for (final int frequency : frequencies) {
			packet.writeInt(frequency);
		}
		packet.writeInt(routeIds.size());
		routeIds.forEach(packet::writeLong);
		sendPacket.accept(packet);
	}

	public void generateMainRoute(MinecraftServer minecraftServer, World world, DataCache dataCache, Map<BlockPos, Map<BlockPos, Rail>> rails, Set<Siding> sidings, Consumer<Thread> callback) {
		final List<SavedRailBase> platformsInRoute = new ArrayList<>();

		routeIds.forEach(routeId -> {
			final Route route = dataCache.routeIdMap.get(routeId);
			if (route != null) {
				route.platformIds.forEach(platformId -> {
					final Platform platform = dataCache.platformIdMap.get(platformId);
					if (platform != null && (platformsInRoute.isEmpty() || platform.id != platformsInRoute.get(platformsInRoute.size() - 1).id)) {
						platformsInRoute.add(platform);
					}
				});
			}
		});

		final Thread thread = new Thread(() -> {
			try {
				final List<PathData> tempPath = new ArrayList<>();
				final int successfulSegmentsMain = PathFinder.findPath(tempPath, rails, platformsInRoute, 1);
				final int[] successfulSegments = new int[]{Integer.MAX_VALUE};

				sidings.forEach(siding -> {
					final BlockPos sidingMidPos = siding.getMidPos();
					if (inArea(sidingMidPos.getX(), sidingMidPos.getZ())) {
						final SavedRailBase firstPlatform = platformsInRoute.isEmpty() ? null : platformsInRoute.get(0);
						final SavedRailBase lastPlatform = platformsInRoute.isEmpty() ? null : platformsInRoute.get(platformsInRoute.size() - 1);
						final int result = siding.generateRoute(minecraftServer, tempPath, successfulSegmentsMain, rails, firstPlatform, lastPlatform);
						if (result < successfulSegments[0]) {
							successfulSegments[0] = result;
						}
					}
				});

				PacketTrainDataGuiServer.generatePathS2C(world, id, successfulSegments[0]);
				System.out.println("Finished path generation" + (name.isEmpty() ? "" : " for " + name));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		callback.accept(thread);
		thread.start();
	}

	public void requestDeploy(long sidingId, TrainServer train) {
		deployableSidings.put(sidingId, train);
	}

	public void deployTrain(RailwayData railwayData, int hour) {
		if (!deployableSidings.isEmpty() && getMillisUntilDeploy(hour, 1) == 0) {
			final List<Siding> sidingsInDepot = railwayData.sidings.stream().filter(siding -> {
				final BlockPos sidingPos = siding.getMidPos();
				return inArea(sidingPos.getX(), sidingPos.getZ());
			}).sorted().collect(Collectors.toList());

			final int sidingsInDepotSize = sidingsInDepot.size();
			for (int i = deployIndex; i < deployIndex + sidingsInDepotSize; i++) {
				final TrainServer train = deployableSidings.get(sidingsInDepot.get(i % sidingsInDepotSize).id);
				if (train != null) {
					lastDeployedMillis = System.currentTimeMillis();
					deployIndex++;
					if (deployIndex >= sidingsInDepotSize) {
						deployIndex = 0;
					}
					train.deployTrain();
					break;
				}
			}
		}

		departureOffset = 0;
		deployableSidings.clear();
	}

	public int getNextDepartureTicks(int hour) {
		departureOffset++;
		final int millisUntilDeploy = getMillisUntilDeploy(hour, departureOffset);
		return millisUntilDeploy >= 0 ? millisUntilDeploy / MILLIS_PER_TICK : -1;
	}

	private int getMillisUntilDeploy(int hour, int offset) {
		for (int i = hour; i < hour + HOURS_IN_DAY; i++) {
			final int frequency = frequencies[i % HOURS_IN_DAY] > 0 ? TICKS_PER_HOUR * MILLIS_PER_TICK * TRAIN_FREQUENCY_MULTIPLIER / frequencies[i % HOURS_IN_DAY] : 0;
			if (frequency > 0) {
				return Math.max(frequency * offset - (int) Math.min(System.currentTimeMillis() - lastDeployedMillis, MAX_DEPLOYED_MILLIS), 0) + (i - hour) * TICKS_PER_HOUR * MILLIS_PER_TICK;
			}
		}
		return -1;
	}

	public static int getHour(WorldAccess world) {
		return (int) wrapTime(world.getLunarTime()) / TICKS_PER_HOUR;
	}

	private static float wrapTime(float time) {
		return (time + 6000 + TICKS_PER_DAY) % TICKS_PER_DAY;
	}
}
