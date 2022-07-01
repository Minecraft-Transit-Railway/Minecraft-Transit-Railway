package mtr.client;

import mtr.KeyMappings;
import mtr.data.*;
import mtr.entity.EntityLift;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.screen.LiftSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.*;
import java.util.function.Function;

public final class ClientData {

	public static String DASHBOARD_SEARCH = "";
	public static String ROUTES_PLATFORMS_SEARCH = "";
	public static String ROUTES_PLATFORMS_SELECTED_SEARCH = "";
	public static String TRAINS_SEARCH = "";
	public static String EXIT_PARENTS_SEARCH = "";
	public static String EXIT_DESTINATIONS_SEARCH = "";

	private static boolean pressingAccelerate = false;
	private static boolean pressingBrake = false;
	private static boolean pressingDoors = false;

	public static final Set<Station> STATIONS = new HashSet<>();
	public static final Set<Platform> PLATFORMS = new HashSet<>();
	public static final Set<Siding> SIDINGS = new HashSet<>();
	public static final Set<Route> ROUTES = new HashSet<>();
	public static final Set<Depot> DEPOTS = new HashSet<>();
	public static final SignalBlocks SIGNAL_BLOCKS = new SignalBlocks();
	public static final Map<BlockPos, Map<BlockPos, Rail>> RAILS = new HashMap<>();
	public static final Set<TrainClient> TRAINS = new HashSet<>();
	public static final List<DataConverter> RAIL_ACTIONS = new ArrayList<>();
	public static final Map<Long, Set<ScheduleEntry>> SCHEDULES_FOR_PLATFORM = new HashMap<>();

	public static final ClientCache DATA_CACHE = new ClientCache(STATIONS, PLATFORMS, SIDINGS, ROUTES, DEPOTS);

	private static final Map<UUID, Integer> PLAYER_RIDING_COOL_DOWN = new HashMap<>();

	public static void tick() {
		final Set<UUID> playersToRemove = new HashSet<>();
		PLAYER_RIDING_COOL_DOWN.forEach((uuid, coolDown) -> {
			if (coolDown <= 0) {
				playersToRemove.add(uuid);
			}
			PLAYER_RIDING_COOL_DOWN.put(uuid, coolDown - 1);
		});
		playersToRemove.forEach(PLAYER_RIDING_COOL_DOWN::remove);


		final boolean tempPressingAccelerate = KeyMappings.TRAIN_ACCELERATE.isDown();
		final boolean tempPressingBrake = KeyMappings.TRAIN_BRAKE.isDown();
		final boolean tempPressingDoors = KeyMappings.TRAIN_TOGGLE_DOORS.isDown();
		PacketTrainDataGuiClient.sendDriveTrainC2S(
				tempPressingAccelerate && !pressingAccelerate,
				tempPressingBrake && !pressingBrake,
				tempPressingDoors && !pressingDoors
		);
		pressingAccelerate = tempPressingAccelerate;
		pressingBrake = tempPressingBrake;
		pressingDoors = tempPressingDoors;

		final Minecraft minecraftClient = Minecraft.getInstance();
		final Player player = minecraftClient.player;
		if (player != null) {
			final Entity vehicle = player.getVehicle();
			if (vehicle instanceof EntityLift) {
				if (KeyMappings.LIFT_MENU.isDown() && !(minecraftClient.screen instanceof LiftSelectionScreen)) {
					UtilitiesClient.setScreen(minecraftClient, new LiftSelectionScreen((EntityLift) vehicle));
				}
				player.displayClientMessage(Text.translatable("gui.mtr.press_to_select_floor", KeyMappings.LIFT_MENU.getTranslatedKeyMessage()), true);
			}
		}
	}

	public static void writeRails(Minecraft client, FriendlyByteBuf packet) {
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

	public static void updateTrains(Minecraft client, FriendlyByteBuf packet) {
		final Set<TrainClient> trainsToUpdate = new HashSet<>();

		while (packet.isReadable()) {
			trainsToUpdate.add(new TrainClient(packet));
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

	public static void deleteTrains(Minecraft client, FriendlyByteBuf packet) {
		final Set<Long> trainIdsToKeep = new HashSet<>();

		final int trainsCount = packet.readInt();
		for (int i = 0; i < trainsCount; i++) {
			trainIdsToKeep.add(packet.readLong());
		}

		client.execute(() -> {
			TRAINS.forEach(trainClient -> {
				if (!trainIdsToKeep.contains(trainClient.id)) {
					trainClient.isRemoved = true;
				}
			});
			TRAINS.removeIf(trainClient -> trainClient.isRemoved);
		});
	}

	public static void updateTrainPassengers(Minecraft client, FriendlyByteBuf packet) {
		final TrainClient train = getTrainById(packet.readLong());
		final float percentageX = packet.readFloat();
		final float percentageZ = packet.readFloat();
		final UUID uuid = packet.readUUID();
		if (train != null) {
			client.execute(() -> train.startRidingClient(uuid, percentageX, percentageZ));
		}
	}

	public static void updateTrainPassengerPosition(Minecraft client, FriendlyByteBuf packet) {
		final TrainClient train = getTrainById(packet.readLong());
		final float percentageX = packet.readFloat();
		final float percentageZ = packet.readFloat();
		final UUID uuid = packet.readUUID();
		if (train != null) {
			client.execute(() -> train.updateRiderPercentages(uuid, percentageX, percentageZ));
		}
	}

	public static void updateRailActions(Minecraft client, FriendlyByteBuf packet) {
		final List<DataConverter> railActions = new ArrayList<>();
		final int actionCount = packet.readInt();
		for (int i = 0; i < actionCount; i++) {
			final long id = packet.readLong();
			final String player = packet.readUtf();
			final float length = packet.readFloat();
			final String block = Text.translatable(packet.readUtf()).getString();
			final String name = Text.translatable("gui.mtr." + packet.readUtf(), player, length, block).getString();
			final int color = packet.readInt();
			railActions.add(new DataConverter(id, name, color));
		}
		client.execute(() -> {
			RAIL_ACTIONS.clear();
			RAIL_ACTIONS.addAll(railActions);
		});
	}

	public static void updateSchedule(Minecraft client, FriendlyByteBuf packet) {
		final Map<Long, Set<ScheduleEntry>> tempSchedulesForPlatform = new HashMap<>();
		final int platformCount = packet.readInt();
		for (int i = 0; i < platformCount; i++) {
			final long platformId = packet.readLong();
			final int scheduleCount = packet.readInt();
			for (int j = 0; j < scheduleCount; j++) {
				if (!tempSchedulesForPlatform.containsKey(platformId)) {
					tempSchedulesForPlatform.put(platformId, new HashSet<>());
				}
				tempSchedulesForPlatform.get(platformId).add(new ScheduleEntry(packet));
			}
		}

		final Map<Long, Boolean> signalBlockStatus = new HashMap<>();
		final int signalBlockCount = packet.readInt();
		for (int i = 0; i < signalBlockCount; i++) {
			signalBlockStatus.put(packet.readLong(), packet.readBoolean());
		}

		client.execute(() -> {
			clearAndAddAll(SCHEDULES_FOR_PLATFORM, tempSchedulesForPlatform);
			SIGNAL_BLOCKS.writeSignalBlockStatus(signalBlockStatus);
		});
	}

	public static void receivePacket(FriendlyByteBuf packet) {
		final FriendlyByteBuf packetCopy = new FriendlyByteBuf(packet.copy());
		clearAndAddAll(STATIONS, deserializeData(packetCopy, Station::new));
		clearAndAddAll(PLATFORMS, deserializeData(packetCopy, Platform::new));
		clearAndAddAll(SIDINGS, deserializeData(packetCopy, Siding::new));
		clearAndAddAll(ROUTES, deserializeData(packetCopy, Route::new));
		clearAndAddAll(DEPOTS, deserializeData(packetCopy, Depot::new));
		clearAndAddAll(SIGNAL_BLOCKS.signalBlocks, deserializeData(packetCopy, SignalBlocks.SignalBlock::new));

		TRAINS.clear();
		ClientData.DATA_CACHE.sync();
		SIGNAL_BLOCKS.writeCache();
	}

	public static <T extends NameColorDataBase> Set<T> getFilteredDataSet(TransportMode transportMode, Set<T> dataSet) {
		final Set<T> returnData = new HashSet<>();
		dataSet.forEach(data -> {
			if (data.isTransportMode(transportMode)) {
				returnData.add(data);
			}
		});
		return returnData;
	}

	public static void updatePlayerRidingOffset(UUID uuid) {
		PLAYER_RIDING_COOL_DOWN.put(uuid, 2);
	}

	public static boolean isRiding(UUID uuid) {
		return PLAYER_RIDING_COOL_DOWN.containsKey(uuid);
	}

	public static boolean hasPermission() {
		final LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) {
			return false;
		}
		final ClientPacketListener clientPacketListener = Minecraft.getInstance().getConnection();
		if (clientPacketListener == null) {
			return false;
		}
		final PlayerInfo playerInfo = clientPacketListener.getPlayerInfo(player.getUUID());
		if (playerInfo == null) {
			return false;
		}
		return RailwayData.hasPermission(playerInfo.getGameMode());
	}

	private static <T extends SerializedDataBase> Set<T> deserializeData(FriendlyByteBuf packet, Function<FriendlyByteBuf, T> supplier) {
		final Set<T> objects = new HashSet<>();
		final int dataCount = packet.readInt();
		for (int i = 0; i < dataCount; i++) {
			objects.add(supplier.apply(packet));
		}
		return objects;
	}

	private static <U> void clearAndAddAll(Collection<U> target, Collection<U> source) {
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
