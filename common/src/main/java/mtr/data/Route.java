package mtr.data;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class Route extends NameColorDataBase implements IGui {

	public RouteType routeType;
	public boolean isLightRailRoute;
	public CircularState circularState;
	public String lightRailRouteNumber;
	public final List<Long> platformIds;

	private static final String KEY_PLATFORM_IDS = "platform_ids";
	private static final String KEY_ROUTE_TYPE = "route_type";
	private static final String KEY_IS_LIGHT_RAIL_ROUTE = "is_light_rail_route";
	private static final String KEY_LIGHT_RAIL_ROUTE_NUMBER = "light_rail_route_number";
	private static final String KEY_CIRCULAR_STATE = "circular_state";

	public Route(TransportMode transportMode) {
		this(0, transportMode);
	}

	public Route(long id, TransportMode transportMode) {
		super(id, transportMode);
		platformIds = new ArrayList<>();
		routeType = RouteType.NORMAL;
		isLightRailRoute = false;
		circularState = CircularState.NONE;
		lightRailRouteNumber = "";
	}

	public Route(CompoundTag compoundTag) {
		super(compoundTag);

		platformIds = new ArrayList<>();
		final long[] platformIdsArray = compoundTag.getLongArray(KEY_PLATFORM_IDS);
		for (final long platformId : platformIdsArray) {
			platformIds.add(platformId);
		}

		routeType = EnumHelper.valueOf(RouteType.NORMAL, compoundTag.getString(KEY_ROUTE_TYPE));
		isLightRailRoute = compoundTag.getBoolean(KEY_IS_LIGHT_RAIL_ROUTE);
		lightRailRouteNumber = compoundTag.getString(KEY_LIGHT_RAIL_ROUTE_NUMBER);
		circularState = EnumHelper.valueOf(CircularState.NONE, compoundTag.getString(KEY_CIRCULAR_STATE));
	}

	public Route(FriendlyByteBuf packet) {
		super(packet);

		platformIds = new ArrayList<>();
		final int platformCount = packet.readInt();
		for (int i = 0; i < platformCount; i++) {
			platformIds.add(packet.readLong());
		}

		routeType = EnumHelper.valueOf(RouteType.NORMAL, packet.readUtf(PACKET_STRING_READ_LENGTH));
		isLightRailRoute = packet.readBoolean();
		lightRailRouteNumber = packet.readUtf(PACKET_STRING_READ_LENGTH);
		circularState = EnumHelper.valueOf(CircularState.NONE, packet.readUtf(PACKET_STRING_READ_LENGTH));
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag compoundTag = super.toCompoundTag();
		compoundTag.putLongArray(KEY_PLATFORM_IDS, platformIds);

		compoundTag.putString(KEY_ROUTE_TYPE, routeType.toString());
		compoundTag.putBoolean(KEY_IS_LIGHT_RAIL_ROUTE, isLightRailRoute);
		compoundTag.putString(KEY_LIGHT_RAIL_ROUTE_NUMBER, lightRailRouteNumber);
		compoundTag.putString(KEY_CIRCULAR_STATE, circularState.toString());

		return compoundTag;
	}

	@Override
	public void writePacket(FriendlyByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(platformIds.size());
		platformIds.forEach(packet::writeLong);

		packet.writeUtf(routeType.toString());
		packet.writeBoolean(isLightRailRoute);
		packet.writeUtf(lightRailRouteNumber);
		packet.writeUtf(circularState.toString());
	}

	@Override
	public void update(String key, FriendlyByteBuf packet) {
		switch (key) {
			case KEY_PLATFORM_IDS:
				platformIds.clear();
				final int platformCount = packet.readInt();
				for (int i = 0; i < platformCount; i++) {
					platformIds.add(packet.readLong());
				}
				break;
			case KEY_IS_LIGHT_RAIL_ROUTE:
				name = packet.readUtf(PACKET_STRING_READ_LENGTH);
				color = packet.readInt();
				routeType = EnumHelper.valueOf(RouteType.NORMAL, packet.readUtf(PACKET_STRING_READ_LENGTH));
				isLightRailRoute = packet.readBoolean();
				lightRailRouteNumber = packet.readUtf(PACKET_STRING_READ_LENGTH);
				circularState = EnumHelper.valueOf(CircularState.NONE, packet.readUtf(PACKET_STRING_READ_LENGTH));
				break;
			default:
				super.update(key, packet);
				break;
		}
	}

	@Override
	protected boolean hasTransportMode() {
		return true;
	}

	public void setPlatformIds(Consumer<FriendlyByteBuf> sendPacket) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(id);
		packet.writeUtf(transportMode.toString());
		packet.writeUtf(KEY_PLATFORM_IDS);
		packet.writeInt(platformIds.size());
		platformIds.forEach(packet::writeLong);
		sendPacket.accept(packet);
	}

	public void setExtraData(Consumer<FriendlyByteBuf> sendPacket) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(id);
		packet.writeUtf(transportMode.toString());
		packet.writeUtf(KEY_IS_LIGHT_RAIL_ROUTE);
		packet.writeUtf(name);
		packet.writeInt(color);
		packet.writeUtf(routeType.toString());
		packet.writeBoolean(isLightRailRoute);
		packet.writeUtf(lightRailRouteNumber);
		packet.writeUtf(circularState.toString());
		sendPacket.accept(packet);
	}

	public enum CircularState {NONE, CLOCKWISE, ANTICLOCKWISE}
}
