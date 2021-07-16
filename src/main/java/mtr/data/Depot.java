package mtr.data;

import mtr.packet.PacketTrainDataGuiServer;
import mtr.path.PathData;
import mtr.path.PathFinder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Depot extends AreaBase {

	public int clientPathGenerationSuccessfulSegments;
	private long lastDeployedMillis;

	public final List<Long> routeIds = new ArrayList<>();

	private final int[] frequencies = new int[HOURS_IN_DAY];

	private final List<Siding> sidingsToGenerate = new ArrayList<>();
	private final List<SavedRailBase> platformsInRoute = new ArrayList<>();
	private final List<PathData> tempPath = new ArrayList<>();
	private int successfulSegmentsMain;
	private int successfulSegments;

	public static final int HOURS_IN_DAY = 24;
	public static final int TICKS_PER_HOUR = 1000;
	public static final int TICKS_PER_DAY = HOURS_IN_DAY * TICKS_PER_HOUR;
	private static final int FAILED_DEPLOY_COOL_DOWN = 2000;

	private static final String KEY_ROUTE_IDS = "route_ids";
	private static final String KEY_FREQUENCIES = "frequencies";
	private static final String KEY_LAST_DEPLOYED = "last_deployed";

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
	}

	@Override
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = super.toCompoundTag();

		nbtCompound.putLongArray(KEY_ROUTE_IDS, routeIds);

		for (int i = 0; i < HOURS_IN_DAY; i++) {
			nbtCompound.putInt(KEY_FREQUENCIES + i, frequencies[i]);
		}

		nbtCompound.putLong(KEY_LAST_DEPLOYED, System.currentTimeMillis() - lastDeployedMillis);

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
	}

	@Override
	public void update(String key, PacketByteBuf packet) {
		switch (key) {
			case KEY_ROUTE_IDS:
				routeIds.clear();
				final int routeIdCount = packet.readInt();
				for (int i = 0; i < routeIdCount; i++) {
					routeIds.add(packet.readLong());
				}
				break;
			case KEY_FREQUENCIES:
				for (int i = 0; i < HOURS_IN_DAY; i++) {
					frequencies[i] = packet.readInt();
				}
				break;
			default:
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

	public void setFrequencies(int newFrequency, int index, Consumer<PacketByteBuf> sendPacket) {
		if (index >= 0 && index < frequencies.length) {
			frequencies[index] = newFrequency;
			final PacketByteBuf packet = PacketByteBufs.create();
			packet.writeLong(id);
			packet.writeString(KEY_FREQUENCIES);
			for (final int frequency : frequencies) {
				packet.writeInt(frequency);
			}
			sendPacket.accept(packet);
		}
	}

	public void setRouteIds(Consumer<PacketByteBuf> sendPacket) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_ROUTE_IDS);
		packet.writeInt(routeIds.size());
		routeIds.forEach(packet::writeLong);
		sendPacket.accept(packet);
	}

	public boolean generateSidingRoute(World world, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		if (sidingsToGenerate.isEmpty()) {
			return true;
		}

		final Siding siding = sidingsToGenerate.remove(0);
		final BlockPos sidingMidPos = siding.getMidPos();
		if (inArea(sidingMidPos.getX(), sidingMidPos.getZ())) {
			final SavedRailBase firstPlatform = platformsInRoute.isEmpty() ? null : platformsInRoute.get(0);
			final SavedRailBase lastPlatform = platformsInRoute.isEmpty() ? null : platformsInRoute.get(platformsInRoute.size() - 1);
			final int result = siding.generateRoute(tempPath, successfulSegmentsMain, rails, firstPlatform, lastPlatform);
			if (result < successfulSegments) {
				successfulSegments = result;
			}
		}

		final boolean complete = sidingsToGenerate.isEmpty();
		if (complete) {
			PacketTrainDataGuiServer.generatePathS2C(world, id, successfulSegments);
		}
		return complete;
	}

	public void generateMainRoute(Map<BlockPos, Map<BlockPos, Rail>> rails, Set<Platform> platforms, Set<Siding> sidings, Set<Route> routes) {
		platformsInRoute.clear();
		sidingsToGenerate.clear();
		sidingsToGenerate.addAll(sidings);

		routeIds.forEach(routeId -> {
			final Route route = RailwayData.getDataById(routes, routeId);
			if (route != null) {
				route.platformIds.forEach(platformId -> {
					final Platform platform = RailwayData.getDataById(platforms, platformId);
					if (platform != null && (platformsInRoute.isEmpty() || platform.id != platformsInRoute.get(platformsInRoute.size() - 1).id)) {
						platformsInRoute.add(platform);
					}
				});
			}
		});

		successfulSegments = Integer.MAX_VALUE;
		successfulSegmentsMain = PathFinder.findPath(tempPath, rails, platformsInRoute, 1);
	}

	public boolean deployTrain(World world) {
		final long currentMillis = System.currentTimeMillis();
		final int hour = (int) wrapTime(world.getLunarTime(), -6000) / TICKS_PER_HOUR;
		final int deployInterval = 50 * TICKS_PER_HOUR / frequencies[hour];
		final boolean isSpawnTime = frequencies[hour] > 0 && currentMillis - lastDeployedMillis >= deployInterval;

		if (isSpawnTime) {
			final RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null && railwayData.noTrainsInDepot(this)) {
				lastDeployedMillis = currentMillis;
				return true;
			} else {
				lastDeployedMillis = currentMillis - deployInterval + FAILED_DEPLOY_COOL_DOWN;
			}
		}

		return false;
	}

	public static float wrapTime(float time1, float time2) {
		return (time1 - time2 + TICKS_PER_DAY) % TICKS_PER_DAY;
	}
}
