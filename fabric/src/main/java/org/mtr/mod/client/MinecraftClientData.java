package org.mtr.mod.client;

import com.logisticscraft.occlusionculling.util.Vec3d;
import org.mtr.core.data.Position;
import org.mtr.core.data.*;
import org.mtr.core.operation.ArrivalsResponse;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.Long2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.*;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.EntityHelper;
import org.mtr.mapping.mapper.MinecraftClientHelper;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.KeyBindings;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.data.PersistentVehicleData;
import org.mtr.mod.data.VehicleExtension;
import org.mtr.mod.packet.PacketDriveTrain;
import org.mtr.mod.packet.PacketFetchArrivals;
import org.mtr.mod.screen.DashboardListItem;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class MinecraftClientData extends ClientData {

	public final ObjectAVLTreeSet<VehicleExtension> vehicles = new ObjectAVLTreeSet<>();
	public final Long2ObjectAVLTreeMap<PersistentVehicleData> vehicleIdToPersistentVehicleData = new Long2ObjectAVLTreeMap<>();
	public final Object2ObjectArrayMap<String, RailWrapper> railWrapperList = new Object2ObjectArrayMap<>();
	public final ObjectArrayList<DashboardListItem> railActions = new ObjectArrayList<>();
	private final Long2ObjectAVLTreeMap<ObjectLongImmutablePair<ArrivalsResponse>> arrivalRequests = new Long2ObjectAVLTreeMap<>();

	private static MinecraftClientData instance = new MinecraftClientData();
	private static MinecraftClientData dashboardInstance = new MinecraftClientData();

	public static String DASHBOARD_SEARCH = "";
	public static String ROUTES_PLATFORMS_SEARCH = "";
	public static String ROUTES_PLATFORMS_SELECTED_SEARCH = "";
	public static String TRAINS_SEARCH = "";
	public static String EXIT_PARENTS_SEARCH = "";
	public static String EXIT_DESTINATIONS_SEARCH = "";

	private static boolean pressingAccelerate = false;
	private static boolean pressingBrake = false;
	private static boolean pressingDoors = false;

	private static final int CACHED_ARRIVAL_REQUESTS_MILLIS = 3000;

	@Override
	public void sync() {
		super.sync();
		checkAndRemoveFromMap(vehicleIdToPersistentVehicleData, vehicles, NameColorDataBase::getId);
		checkAndRemoveFromMap(railWrapperList, rails, Rail::getHexId);
		positionsToRail.forEach((startPosition, railMap) -> railMap.forEach((endPosition, rail) -> {
			final String hexId = rail.getHexId();
			final RailWrapper railWrapper = railWrapperList.get(hexId);
			if (railWrapper == null) {
				railWrapperList.put(hexId, new RailWrapper(rail, hexId, startPosition, endPosition));
			} else {
				railWrapper.rail = rail;
			}
		}));
	}

	public ArrivalsResponse requestArrivals(long requestKey, LongImmutableList platformIds, int count, int page, boolean realtimeOnly) {
		final ObjectLongImmutablePair<ArrivalsResponse> arrivalData = arrivalRequests.get(requestKey);
		if (arrivalData == null || arrivalData.rightLong() < System.currentTimeMillis()) {
			if (!platformIds.isEmpty()) {
				InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketFetchArrivals(requestKey, platformIds, count, page, realtimeOnly));
			}

			final ArrivalsResponse arrivalsResponse;
			if (arrivalData == null) {
				arrivalsResponse = new ArrivalsResponse();
			} else {
				arrivalsResponse = arrivalData.left();
			}

			writeArrivalRequest(requestKey, arrivalsResponse);
			return arrivalsResponse;
		} else {
			return arrivalData.left();
		}
	}

	public void writeArrivalRequest(long requestKey, ArrivalsResponse arrivalsResponse) {
		arrivalRequests.put(requestKey, new ObjectLongImmutablePair<>(arrivalsResponse, System.currentTimeMillis() + CACHED_ARRIVAL_REQUESTS_MILLIS));
	}

	public void clean() {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity != null) {
			final Position position = Init.blockPosToPosition(clientPlayerEntity.getBlockPos());
			final int requestRadius = MinecraftClientHelper.getRenderDistance() * 16;
			stations.removeIf(station -> !station.inArea(position, requestRadius));
			platforms.removeIf(platform -> !platform.closeTo(position, requestRadius));
			sidings.removeIf(siding -> !siding.closeTo(position, requestRadius));
			depots.removeIf(depot -> !depot.inArea(position, requestRadius));
			rails.removeIf(rail -> !rail.closeTo(position, requestRadius));
			sync();
		}
	}

	@Nullable
	public Rail getFacingRail(boolean includeCableType) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		if (clientWorld == null) {
			return null;
		}

		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		if (clientPlayerEntity == null) {
			return null;
		}

		final HitResult hitResult = minecraftClient.getCrosshairTargetMapped();
		if (hitResult == null) {
			return null;
		}

		final Vector3d hitPos = hitResult.getPos();
		final BlockPos blockPos = Init.newBlockPos(hitPos.getXMapped(), hitPos.getYMapped(), hitPos.getZMapped());

		if (clientWorld.getBlockState(blockPos).getBlock().data instanceof BlockNode) {
			final float playerAngle = EntityHelper.getYaw(new Entity(clientPlayerEntity.data)) + 90;
			final Rail[] closestRail = {null};
			final double[] closestAngle = {720};

			positionsToRail.getOrDefault(Init.blockPosToPosition(blockPos), new Object2ObjectOpenHashMap<>()).forEach((endPosition, rail) -> {
				if (includeCableType || rail.railMath.getShape() != Rail.Shape.CABLE) {
					final double angle = Math.abs(Math.toDegrees(Math.atan2(endPosition.getZ() - blockPos.getZ(), endPosition.getX() - blockPos.getX())) - playerAngle) % 360;
					final double clampedAngle = angle > 180 ? 360 - angle : angle;
					if (clampedAngle < closestAngle[0]) {
						closestRail[0] = rail;
						closestAngle[0] = clampedAngle;
					}
				}
			});

			return closestRail[0];
		} else {
			return null;
		}
	}

	public static MinecraftClientData getInstance() {
		return instance;
	}

	public static MinecraftClientData getDashboardInstance() {
		return dashboardInstance;
	}

	public static void reset() {
		MinecraftClientData.instance = new MinecraftClientData();
		MinecraftClientData.dashboardInstance = new MinecraftClientData();
	}

	public static void tick() {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		final boolean tempPressingAccelerate = KeyBindings.TRAIN_ACCELERATE.isPressed();
		final boolean tempPressingBrake = KeyBindings.TRAIN_BRAKE.isPressed();
		final boolean tempPressingDoors = KeyBindings.TRAIN_TOGGLE_DOORS.isPressed();

		if (VehicleExtension.isHoldingKey(clientPlayerEntity) && (pressingAccelerate || pressingBrake || pressingDoors)) {
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketDriveTrain(
					tempPressingAccelerate && !pressingAccelerate,
					tempPressingBrake && !pressingBrake,
					tempPressingDoors && !pressingDoors
			));
		}

		pressingAccelerate = tempPressingAccelerate;
		pressingBrake = tempPressingBrake;
		pressingDoors = tempPressingDoors;
	}

	public static boolean hasPermission() {
		final ClientPlayerEntity player = MinecraftClient.getInstance().getPlayerMapped();
		if (player == null) {
			return false;
		}
		final ClientPlayNetworkHandler clientPlayNetworkHandler = MinecraftClient.getInstance().getNetworkHandler();
		if (clientPlayNetworkHandler == null) {
			return false;
		}
		final PlayerListEntry playerListEntry = clientPlayNetworkHandler.getPlayerListEntry(player.getUuid());
		if (playerListEntry == null) {
			return false;
		}
		final GameMode gameMode = playerListEntry.getGameMode();
		return gameMode == GameMode.getCreativeMapped() || gameMode == GameMode.getSurvivalMapped();
	}

	@Nullable
	public static Lift getLift(long liftId) {
		// Don't use liftIdMap
		for (final Lift lift : MinecraftClientData.getInstance().lifts) {
			if (lift.getId() == liftId) {
				return lift;
			}
		}
		return null;
	}

	public static <T extends NameColorDataBase> ObjectAVLTreeSet<DashboardListItem> getFilteredDataSet(TransportMode transportMode, ObjectAVLTreeSet<T> dataSet) {
		return convertDataSet(dataSet.stream().filter(data -> data.isTransportMode(transportMode)).collect(Collectors.toCollection(ObjectAVLTreeSet::new)));
	}

	public static <T extends NameColorDataBase> ObjectAVLTreeSet<DashboardListItem> convertDataSet(ObjectAVLTreeSet<T> dataSet) {
		return new ObjectAVLTreeSet<>(dataSet.stream().map(DashboardListItem::new).collect(Collectors.toSet()));
	}

	public static <T extends AreaBase<T, U>, U extends SavedRailBase<U, T>> Object2ObjectAVLTreeMap<Position, ObjectArrayList<U>> getFlatPositionToSavedRails(ObjectAVLTreeSet<U> savedRails, TransportMode transportMode) {
		final Object2ObjectAVLTreeMap<Position, ObjectArrayList<U>> map = new Object2ObjectAVLTreeMap<>();
		savedRails.forEach(savedRail -> {
			if (savedRail.isTransportMode(transportMode)) {
				final Position position = savedRail.getMidPosition();
				Data.put(map, new Position(position.getX(), 0, position.getZ()), savedRail, ObjectArrayList::new);
			}
		});
		map.forEach((position, newSavedRails) -> newSavedRails.sort((savedRail1, savedRail2) -> {
			if (savedRail1.getId() == savedRail2.getId()) {
				return 0;
			} else {
				final long y1 = savedRail1.getMidPosition().getY();
				final long y2 = savedRail2.getMidPosition().getY();
				return y1 == y2 ? savedRail1.getId() > savedRail2.getId() ? 1 : -1 : y1 > y2 ? 1 : -1;
			}
		}));
		return map;
	}

	private static <T, U, V> void checkAndRemoveFromMap(Map<T, U> map, ObjectSet<V> dataSet, Function<V, T> getId) {
		final ObjectAVLTreeSet<T> idSet = dataSet.stream().map(getId).collect(Collectors.toCollection(ObjectAVLTreeSet::new));
		final ObjectArrayList<T> idsToRemove = new ObjectArrayList<>();
		map.keySet().forEach(id -> {
			if (!idSet.contains(id)) {
				idsToRemove.add(id);
			}
		});
		idsToRemove.forEach(map::remove);
	}

	public static class RailWrapper {

		public boolean shouldRender;
		public final String hexId;
		public final Vec3d startVector;
		public final Vec3d endVector;
		private Rail rail;

		private RailWrapper(Rail rail, String hexId, Position startPosition, Position endPosition) {
			this.rail = rail;
			this.hexId = hexId;
			startVector = new Vec3d(
					Math.min(startPosition.getX(), endPosition.getX()),
					Math.min(startPosition.getY(), endPosition.getY()),
					Math.min(startPosition.getZ(), endPosition.getZ())
			);
			endVector = new Vec3d(
					Math.max(startPosition.getX(), endPosition.getX()),
					Math.max(startPosition.getY(), endPosition.getY()),
					Math.max(startPosition.getZ(), endPosition.getZ())
			);
		}

		public Rail getRail() {
			return rail;
		}
	}
}
