package org.mtr.mod.client;

import org.mtr.core.data.Position;
import org.mtr.core.data.*;
import org.mtr.core.operation.ArrivalsResponse;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.Long2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectLongImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.registry.RegistryClient;
import org.mtr.mod.Init;
import org.mtr.mod.KeyBindings;
import org.mtr.mod.data.PersistentVehicleData;
import org.mtr.mod.data.VehicleExtension;
import org.mtr.mod.packet.PacketDriveTrain;
import org.mtr.mod.packet.PacketFetchArrivals;
import org.mtr.mod.screen.DashboardListItem;

import java.util.Comparator;
import java.util.stream.Collectors;

public final class ClientData extends Data {

	public final ObjectAVLTreeSet<VehicleExtension> vehicles = new ObjectAVLTreeSet<>();
	public final Long2ObjectAVLTreeMap<PersistentVehicleData> vehicleIdToPersistentVehicleData = new Long2ObjectAVLTreeMap<>();
	public final ObjectArrayList<DashboardListItem> railActions = new ObjectArrayList<>();
	private final Long2ObjectAVLTreeMap<ObjectLongImmutablePair<ArrivalsResponse>> arrivalRequests = new Long2ObjectAVLTreeMap<>();

	public static ClientData instance = new ClientData();
	public static String DASHBOARD_SEARCH = "";
	public static String ROUTES_PLATFORMS_SEARCH = "";
	public static String ROUTES_PLATFORMS_SELECTED_SEARCH = "";
	public static String TRAINS_SEARCH = "";
	public static String EXIT_PARENTS_SEARCH = "";
	public static String EXIT_DESTINATIONS_SEARCH = "";

	private static boolean pressingAccelerate = false;
	private static boolean pressingBrake = false;
	private static boolean pressingDoors = false;
	private static float shiftHoldingTicks = 0;

	private static final int CACHED_ARRIVAL_REQUESTS_MILLIS = 3000;

	@Override
	public void sync() {
		super.sync();
		final LongAVLTreeSet vehicleIds = vehicles.stream().map(NameColorDataBase::getId).collect(Collectors.toCollection(LongAVLTreeSet::new));
		final LongAVLTreeSet idsToRemove = new LongAVLTreeSet();
		vehicleIdToPersistentVehicleData.keySet().forEach(vehicleId -> {
			if (!vehicleIds.contains(vehicleId)) {
				idsToRemove.add(vehicleId);
			}
		});
		idsToRemove.forEach(vehicleIdToPersistentVehicleData::remove);
	}

	public ArrivalsResponse requestArrivals(long requestKey, LongImmutableList platformIds, int count, int page, boolean realtimeOnly) {
		final ObjectLongImmutablePair<ArrivalsResponse> arrivalData = arrivalRequests.get(requestKey);
		if (arrivalData == null || arrivalData.rightLong() < System.currentTimeMillis()) {
			RegistryClient.sendPacketToServer(new PacketFetchArrivals(requestKey, platformIds, count, page, realtimeOnly));
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

	public static void tick() {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		final boolean tempPressingAccelerate = KeyBindings.TRAIN_ACCELERATE.isPressed();
		final boolean tempPressingBrake = KeyBindings.TRAIN_BRAKE.isPressed();
		final boolean tempPressingDoors = KeyBindings.TRAIN_TOGGLE_DOORS.isPressed();

		if (VehicleExtension.isHoldingKey(clientPlayerEntity) && (pressingAccelerate || pressingBrake || pressingDoors)) {
			RegistryClient.sendPacketToServer(new PacketDriveTrain(
					tempPressingAccelerate && !pressingAccelerate,
					tempPressingBrake && !pressingBrake,
					tempPressingDoors && !pressingDoors
			));
		}

		pressingAccelerate = tempPressingAccelerate;
		pressingBrake = tempPressingBrake;
		pressingDoors = tempPressingDoors;

		if (clientPlayerEntity != null) {
			if (clientPlayerEntity.isSneaking()) {
				shiftHoldingTicks += minecraftClient.getLastFrameDuration();
			} else {
				shiftHoldingTicks = 0;
			}
		}
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

	public static float getShiftHoldingTicks() {
		return shiftHoldingTicks;
	}

	public static Rail getRail(BlockPos blockPos1, BlockPos blockPos2) {
		return tryGet(ClientData.instance.positionsToRail, Init.blockPosToPosition(blockPos1), Init.blockPosToPosition(blockPos2));
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

	public static Platform getClosePlatform(BlockPos blockPos) {
		return getClosePlatform(blockPos, 5, 2);
	}

	public static Platform getClosePlatform(BlockPos blockPos, int radius, int offsetY) {
		for (int i = 1; i <= radius; i++) {
			final int searchRadius = i;
			final Position position = new Position(blockPos.getX(), blockPos.getY() + offsetY, blockPos.getZ());
			final Platform platform = ClientData.instance.platforms.stream()
					.filter(checkPlatform -> checkPlatform.closeTo(position, searchRadius))
					.min(Comparator.comparingLong(checkPlatform -> checkPlatform.getMidPosition().distManhattan(position)))
					.orElse(null);
			if (platform != null) {
				return platform;
			}
		}

		return null;
	}
}
