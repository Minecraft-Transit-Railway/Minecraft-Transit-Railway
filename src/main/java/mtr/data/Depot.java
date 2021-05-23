package mtr.data;

import mtr.path.PathData2;
import mtr.path.PathFinder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Depot extends AreaBase {

	public final List<Long> routeIds;
	public final List<PathData2> path;
	public final List<Train> trains;

	private static final String KEY_ROUTE_IDS = "route_ids";

	public Depot() {
		super();
		routeIds = new ArrayList<>();
		path = new ArrayList<>();
		trains = new ArrayList<>();
	}

	public Depot(long id) {
		super(id);
		routeIds = new ArrayList<>();
		path = new ArrayList<>();
		trains = new ArrayList<>();
	}

	public Depot(CompoundTag tag) {
		super(tag);
		routeIds = new ArrayList<>();
		final long[] routeIdsArray = tag.getLongArray(KEY_ROUTE_IDS);
		for (final long routeId : routeIdsArray) {
			routeIds.add(routeId);
		}
		path = new ArrayList<>();
		trains = new ArrayList<>();
	}

	public Depot(PacketByteBuf packet) {
		super(packet);
		routeIds = new ArrayList<>();
		final int routeIdCount = packet.readInt();
		for (int i = 0; i < routeIdCount; i++) {
			routeIds.add(packet.readLong());
		}
		path = new ArrayList<>();
		trains = new ArrayList<>();
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = super.toCompoundTag();
		tag.putLongArray(KEY_ROUTE_IDS, routeIds);
		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(routeIds.size());
		routeIds.forEach(packet::writeLong);
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
			default:
				super.update(key, packet);
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

	public void generateRoute(Map<BlockPos, Map<BlockPos, Rail>> rails, Set<Platform> platforms, Set<Route> routes) {
		final List<Platform> platformsInRoute = new ArrayList<>();
		routeIds.forEach(routeId -> {
			final Route route = RailwayData.getDataById(routes, routeId);
			if (route != null) {
				route.platformIds.forEach(platformId -> {
					final Platform platform = RailwayData.getDataById(platforms, platformId);
					if (platform != null) {
						platformsInRoute.add(platform);
					}
				});
			}
		});

		path.clear();
		path.addAll(PathFinder.findPath(rails, platformsInRoute));

		// TODO
		trains.clear();
		trains.add(new Train(TrainType.M_TRAIN, 2, 0, path));
	}
}
