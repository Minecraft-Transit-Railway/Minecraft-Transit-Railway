package mtr.data;

import mtr.EnumHelper;
import mtr.block.BlockRail;
import mtr.path.PathDataDeleteThisLater;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class Route extends NameColorDataBase implements IGui {

	public RouteType routeType;
	public boolean isLightRailRoute;
	public CircularState circularState;
	public String lightRailRouteNumber;

	public final List<Long> platformIds;

	private final List<PathDataDeleteThisLater> path;

	private static final String KEY_PLATFORM_IDS = "platform_ids";
	private static final String KEY_ROUTE_TYPE = "route_type";
	private static final String KEY_IS_LIGHT_RAIL_ROUTE = "is_light_rail_route";
	private static final String KEY_LIGHT_RAIL_ROUTE_NUMBER = "light_rail_route_number";
	private static final String KEY_CIRCULAR_STATE = "circular_state";
	private static final String KEY_PATH = "path";

	public Route() {
		this(0);
	}

	public Route(long id) {
		super(id);
		platformIds = new ArrayList<>();
		path = new ArrayList<>();
		routeType = RouteType.NORMAL;
		isLightRailRoute = false;
		circularState = CircularState.NONE;
		lightRailRouteNumber = "";
	}

	public Route(NbtCompound nbtCompound) {
		super(nbtCompound);

		platformIds = new ArrayList<>();
		final long[] platformIdsArray = nbtCompound.getLongArray(KEY_PLATFORM_IDS);
		for (final long platformId : platformIdsArray) {
			platformIds.add(platformId);
		}

		routeType = EnumHelper.valueOf(RouteType.NORMAL, nbtCompound.getString(KEY_ROUTE_TYPE));
		isLightRailRoute = nbtCompound.getBoolean(KEY_IS_LIGHT_RAIL_ROUTE);
		lightRailRouteNumber = nbtCompound.getString(KEY_LIGHT_RAIL_ROUTE_NUMBER);
		circularState = EnumHelper.valueOf(CircularState.NONE, nbtCompound.getString(KEY_CIRCULAR_STATE));

		path = new ArrayList<>();
		final NbtCompound tagPath = nbtCompound.getCompound(KEY_PATH);
		for (int i = 0; i < tagPath.getKeys().size(); i++) {
			path.add(new PathDataDeleteThisLater(tagPath.getCompound(KEY_PATH + i)));
		}
	}

	public Route(PacketByteBuf packet) {
		super(packet);

		platformIds = new ArrayList<>();
		final int platformCount = packet.readInt();
		for (int i = 0; i < platformCount; i++) {
			platformIds.add(packet.readLong());
		}

		routeType = EnumHelper.valueOf(RouteType.NORMAL, packet.readString(PACKET_STRING_READ_LENGTH));
		isLightRailRoute = packet.readBoolean();
		lightRailRouteNumber = packet.readString(PACKET_STRING_READ_LENGTH);
		circularState = EnumHelper.valueOf(CircularState.NONE, packet.readString(PACKET_STRING_READ_LENGTH));

		path = new ArrayList<>();
		final int pathLength = packet.readInt();
		for (int i = 0; i < pathLength; i++) {
			path.add(new PathDataDeleteThisLater(packet));
		}
	}

	@Override
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = super.toCompoundTag();
		nbtCompound.putLongArray(KEY_PLATFORM_IDS, platformIds);

		nbtCompound.putString(KEY_ROUTE_TYPE, routeType.toString());
		nbtCompound.putBoolean(KEY_IS_LIGHT_RAIL_ROUTE, isLightRailRoute);
		nbtCompound.putString(KEY_LIGHT_RAIL_ROUTE_NUMBER, lightRailRouteNumber);
		nbtCompound.putString(KEY_CIRCULAR_STATE, circularState.toString());

		final NbtCompound tagPath = new NbtCompound();
		for (int i = 0; i < path.size(); i++) {
			tagPath.put(KEY_PATH + i, path.get(i).toCompoundTag());
		}
		nbtCompound.put(KEY_PATH, tagPath);

		return nbtCompound;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(platformIds.size());
		platformIds.forEach(packet::writeLong);

		packet.writeString(routeType.toString());
		packet.writeBoolean(isLightRailRoute);
		packet.writeString(lightRailRouteNumber);
		packet.writeString(circularState.toString());

		packet.writeInt(path.size());
		path.forEach(pathDataDeleteThisLater -> pathDataDeleteThisLater.writePacket(packet));
	}

	@Override
	public void update(String key, PacketByteBuf packet) {
		switch (key) {
			case KEY_PLATFORM_IDS:
				platformIds.clear();
				final int platformCount = packet.readInt();
				for (int i = 0; i < platformCount; i++) {
					platformIds.add(packet.readLong());
				}
				break;
			case KEY_IS_LIGHT_RAIL_ROUTE:
				name = packet.readString(PACKET_STRING_READ_LENGTH);
				color = packet.readInt();
				routeType = EnumHelper.valueOf(RouteType.NORMAL, packet.readString(PACKET_STRING_READ_LENGTH));
				isLightRailRoute = packet.readBoolean();
				lightRailRouteNumber = packet.readString(PACKET_STRING_READ_LENGTH);
				circularState = EnumHelper.valueOf(CircularState.NONE, packet.readString(PACKET_STRING_READ_LENGTH));
				break;
			default:
				super.update(key, packet);
				break;
		}
	}

	public void setPlatformIds(Consumer<PacketByteBuf> sendPacket) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_PLATFORM_IDS);
		packet.writeInt(platformIds.size());
		platformIds.forEach(packet::writeLong);
		sendPacket.accept(packet);
	}

	public void setExtraData(Consumer<PacketByteBuf> sendPacket) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_IS_LIGHT_RAIL_ROUTE);
		packet.writeString(name);
		packet.writeInt(color);
		packet.writeString(routeType.toString());
		packet.writeBoolean(isLightRailRoute);
		packet.writeString(lightRailRouteNumber);
		packet.writeString(circularState.toString());
		sendPacket.accept(packet);
	}

	// TODO temporary code start
	public void generateRails(World world, RailwayData railwayData) {
		path.forEach(pathDataDeleteThisLater -> {
			final BlockEntity entity = world.getBlockEntity(new BlockPos(pathDataDeleteThisLater.getPosition(0)));
			if (entity instanceof BlockRail.TileEntityRail) {
				((BlockRail.TileEntityRail) entity).railMap.forEach((blockPos, rail) -> {
					railwayData.addRail(entity.getPos(), blockPos, rail, false);
					railwayData.addRail(blockPos, entity.getPos(), new Rail(blockPos, rail.facingEnd, entity.getPos(), rail.facingStart, rail.railType), false);
				});
			}
		});
	}
	// TODO temporary code end

	public static class ScheduleEntry implements Comparable<ScheduleEntry> {

		public final long arrivalMillis;
		public final int trainCars;
		public final long platformId;
		public final long routeId;
		public final String destination;
		public final boolean isTerminating;

		public ScheduleEntry(long arrivalMillis, int trainCars, long platformId, long routeId, String destination, boolean isTerminating) {
			this.arrivalMillis = arrivalMillis;
			this.trainCars = trainCars;
			this.platformId = platformId;
			this.routeId = routeId;
			this.destination = destination;
			this.isTerminating = isTerminating;
		}

		public ScheduleEntry(PacketByteBuf packet) {
			arrivalMillis = packet.readLong();
			trainCars = packet.readInt();
			platformId = packet.readLong();
			routeId = packet.readLong();
			destination = packet.readString(PACKET_STRING_READ_LENGTH);
			isTerminating = packet.readBoolean();
		}

		public void writePacket(PacketByteBuf packet) {
			packet.writeLong(arrivalMillis);
			packet.writeInt(trainCars);
			packet.writeLong(platformId);
			packet.writeLong(routeId);
			packet.writeString(destination);
			packet.writeBoolean(isTerminating);
		}

		@Override
		public int compareTo(ScheduleEntry o) {
			if (arrivalMillis == o.arrivalMillis) {
				return destination.compareTo(o.destination);
			} else {
				return arrivalMillis > o.arrivalMillis ? 1 : -1;
			}
		}
	}

	public enum CircularState {NONE, CLOCKWISE, ANTICLOCKWISE}
}
