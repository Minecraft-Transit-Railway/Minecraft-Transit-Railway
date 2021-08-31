package mtr.data;

import mtr.config.CustomResources;
import mtr.packet.IPacket;
import mtr.path.PathData;
import mtr.path.PathFinder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Consumer;

public class Siding extends SavedRailBase implements IPacket {

	private World world;
	private Depot depot;
	private CustomResources.TrainMapping trainTypeMapping;
	private int trainLength;
	private boolean unlimitedTrains;

	private final float railLength;
	private final List<PathData> path = new ArrayList<>();
	private final List<Float> distances = new ArrayList<>();
	private final Set<Train> trains = new HashSet<>();

	public static final String KEY_TRAINS = "trains";
	private static final String KEY_RAIL_LENGTH = "rail_length";
	private static final String KEY_TRAIN_TYPE = "train_type";
	private static final String KEY_TRAIN_CUSTOM_ID = "train_custom_id";
	private static final String KEY_UNLIMITED_TRAINS = "unlimited_trains";
	private static final String KEY_PATH = "path";

	public Siding(long id, BlockPos pos1, BlockPos pos2, float railLength) {
		super(id, pos1, pos2);
		this.railLength = railLength;
		setTrainDetails("", TrainType.values()[0]);
	}

	public Siding(BlockPos pos1, BlockPos pos2, float railLength) {
		super(pos1, pos2);
		this.railLength = railLength;
		setTrainDetails("", TrainType.values()[0]);
	}

	public Siding(NbtCompound nbtCompound) {
		super(nbtCompound);

		railLength = nbtCompound.getFloat(KEY_RAIL_LENGTH);
		TrainType trainType = TrainType.values()[0];
		try {
			trainType = TrainType.valueOf(nbtCompound.getString(KEY_TRAIN_TYPE));
		} catch (Exception ignored) {
		}
		setTrainDetails(nbtCompound.getString(KEY_TRAIN_CUSTOM_ID), trainType);
		unlimitedTrains = nbtCompound.getBoolean(KEY_UNLIMITED_TRAINS);

		final NbtCompound tagPath = nbtCompound.getCompound(KEY_PATH);
		final int pathCount = tagPath.getKeys().size();
		for (int i = 0; i < pathCount; i++) {
			path.add(new PathData(tagPath.getCompound(KEY_PATH + i)));
		}
		generateDistances();

		final NbtCompound tagTrains = nbtCompound.getCompound(KEY_TRAINS);
		tagTrains.getKeys().forEach(key -> trains.add(new Train(id, railLength, path, distances, tagTrains.getCompound(key))));
	}

	public Siding(PacketByteBuf packet) {
		super(packet);

		railLength = packet.readFloat();
		setTrainDetails(packet.readString(PACKET_STRING_READ_LENGTH), TrainType.values()[packet.readInt()]);
		unlimitedTrains = packet.readBoolean();

		final int pathCount = packet.readInt();
		for (int i = 0; i < pathCount; i++) {
			path.add(new PathData(packet));
		}
		generateDistances();

		final int trainCount = packet.readInt();
		for (int i = 0; i < trainCount; i++) {
			trains.add(new Train(id, railLength, path, distances, packet));
		}
	}

	@Override
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = super.toCompoundTag();

		nbtCompound.putFloat(KEY_RAIL_LENGTH, railLength);
		nbtCompound.putString(KEY_TRAIN_CUSTOM_ID, trainTypeMapping.customId);
		nbtCompound.putString(KEY_TRAIN_TYPE, trainTypeMapping.trainType.toString());
		nbtCompound.putBoolean(KEY_UNLIMITED_TRAINS, unlimitedTrains);

		RailwayData.writeTag(nbtCompound, path, KEY_PATH);
		RailwayData.writeTag(nbtCompound, trains, KEY_TRAINS);

		return nbtCompound;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);

		packet.writeFloat(railLength);
		packet.writeString(trainTypeMapping.customId);
		packet.writeInt(trainTypeMapping.trainType.ordinal());
		packet.writeBoolean(unlimitedTrains);

		packet.writeInt(path.size());
		path.forEach(pathData -> pathData.writePacket(packet));

		packet.writeInt(trains.size());
		trains.forEach(train -> train.writePacket(packet));
	}

	@Override
	public void update(String key, PacketByteBuf packet) {
		switch (key) {
			case KEY_TRAIN_TYPE:
				setTrainDetails(packet.readString(PACKET_STRING_READ_LENGTH), TrainType.values()[packet.readInt()]);
				break;
			case KEY_UNLIMITED_TRAINS:
				unlimitedTrains = packet.readBoolean();
				break;
			case KEY_TRAINS:
				final boolean isRemove = packet.readBoolean();
				if (isRemove) {
					final int trainCount = packet.readInt();
					final Set<Long> trainIds = new HashSet<>();
					for (int i = 0; i < trainCount; i++) {
						trainIds.add(packet.readLong());
					}
					trains.removeIf(train -> !trainIds.contains(train.id));
				} else {
					final long startUpMillis = packet.readLong();
					if (depot != null && startUpMillis > 0) {
						depot.lastDeployedMillis = startUpMillis;
					}
					final long trainId = packet.readLong();
					boolean updated = false;
					for (final Train train : trains) {
						if (train.id == trainId) {
							train.update(KEY_TRAINS, packet);
							updated = true;
							break;
						}
					}
					if (!updated) {
						final Train newTrain = new Train(null, trainId, id, railLength, path, distances);
						trains.add(newTrain);
						newTrain.update(KEY_TRAINS, packet);
					}
				}
				break;
			case KEY_PATH:
				final int pathSize = packet.readInt();
				path.clear();
				for (int i = 0; i < pathSize; i++) {
					path.add(new PathData(packet));
				}
				generateDistances();
				break;
			default:
				super.update(key, packet);
				break;
		}
	}

	public void setTrainTypeMapping(String customId, TrainType trainType, Consumer<PacketByteBuf> sendPacket) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_TRAIN_TYPE);
		packet.writeString(customId);
		packet.writeInt(trainType.ordinal());
		sendPacket.accept(packet);
		setTrainDetails(customId, trainType);
	}

	public void setUnlimitedTrains(boolean unlimitedTrains, Consumer<PacketByteBuf> sendPacket) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_UNLIMITED_TRAINS);
		packet.writeBoolean(unlimitedTrains);
		sendPacket.accept(packet);
		this.unlimitedTrains = unlimitedTrains;
	}

	public CustomResources.TrainMapping getTrainTypeMapping() {
		return trainTypeMapping;
	}

	public void setSidingData(World world, Depot depot, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		this.world = world;
		this.depot = depot;

		if (depot == null) {
			trains.clear();
			path.clear();
			distances.clear();
		} else if (path.isEmpty()) {
			generateDefaultPath(rails);
			generateDistances();
		}
	}

	public int generateRoute(MinecraftServer minecraftServer, List<PathData> mainPath, int successfulSegmentsMain, Map<BlockPos, Map<BlockPos, Rail>> rails, SavedRailBase firstPlatform, SavedRailBase lastPlatform) {
		final List<PathData> tempPath = new ArrayList<>();
		final int successfulSegments;
		if (firstPlatform == null || lastPlatform == null) {
			successfulSegments = 0;
		} else {
			final List<SavedRailBase> depotAndFirstPlatform = new ArrayList<>();
			depotAndFirstPlatform.add(this);
			depotAndFirstPlatform.add(firstPlatform);
			PathFinder.findPath(tempPath, rails, depotAndFirstPlatform, 0);

			if (tempPath.isEmpty()) {
				successfulSegments = 1;
			} else if (mainPath.isEmpty()) {
				tempPath.clear();
				successfulSegments = successfulSegmentsMain + 1;
			} else {
				PathFinder.appendPath(tempPath, mainPath);

				final List<SavedRailBase> lastPlatformAndDepot = new ArrayList<>();
				lastPlatformAndDepot.add(lastPlatform);
				lastPlatformAndDepot.add(this);
				final List<PathData> pathLastPlatformToDepot = new ArrayList<>();
				PathFinder.findPath(pathLastPlatformToDepot, rails, lastPlatformAndDepot, successfulSegmentsMain);

				if (pathLastPlatformToDepot.isEmpty()) {
					successfulSegments = successfulSegmentsMain + 1;
					tempPath.clear();
				} else {
					PathFinder.appendPath(tempPath, pathLastPlatformToDepot);
					successfulSegments = successfulSegmentsMain + 2;
				}
			}
		}

		minecraftServer.execute(() -> {
			try {
				if (tempPath.isEmpty()) {
					generateDefaultPath(rails);
				}

				generateDistances();

				path.clear();
				path.addAll(tempPath);

				final PacketByteBuf packet = PacketByteBufs.create();
				packet.writeLong(id);
				packet.writeString(KEY_PATH);
				packet.writeInt(tempPath.size());
				tempPath.forEach(pathData -> pathData.writePacket(packet));
				if (packet.readableBytes() <= MAX_PACKET_BYTES) {
					world.getPlayers().forEach(player -> ServerPlayNetworking.send((ServerPlayerEntity) player, PACKET_UPDATE_SIDING, packet));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		return successfulSegments;
	}

	public void simulateTrain(PlayerEntity clientPlayer, float ticksElapsed, List<Set<UUID>> trainPositions, Train.RenderTrainCallback renderTrainCallback, Train.RenderConnectionCallback renderConnectionCallback, Train.SpeedCallback speedCallback, Train.AnnouncementCallback announcementCallback, Train.AnnouncementCallback lightRailAnnouncementCallback, Train.WriteScheduleCallback writeScheduleCallback) {
		if (depot == null) {
			return;
		}

		int trainsAtDepot = 0;
		boolean spawnTrain = true;

		final Set<Integer> railProgressSet = new HashSet<>();
		final Set<Train> trainsToRemove = new HashSet<>();
		for (final Train train : trains) {
			train.simulateTrain(world, clientPlayer, ticksElapsed, depot, trainTypeMapping, trainLength, trainPositions == null ? null : trainPositions.get(0), renderTrainCallback, renderConnectionCallback, speedCallback, announcementCallback, lightRailAnnouncementCallback, writeScheduleCallback);

			if (train.closeToDepot(trainTypeMapping.trainType.getSpacing() * trainLength)) {
				spawnTrain = false;
			}

			if (!train.getIsOnRoute()) {
				trainsAtDepot++;
				if (trainsAtDepot > 1) {
					trainsToRemove.add(train);
				}
			}

			final int roundedRailProgress = Math.round(train.getRailProgress() * 10);
			if (railProgressSet.contains(roundedRailProgress)) {
				trainsToRemove.add(train);
			}
			railProgressSet.add(roundedRailProgress);

			if (trainPositions != null) {
				train.writeTrainPositions(trainPositions.get(1), trainTypeMapping, trainLength);
			}
		}

		if (world != null && !world.isClient()) {
			if (trains.isEmpty() || unlimitedTrains && spawnTrain) {
				trains.add(new Train(world, new Random().nextLong(), id, railLength, path, distances));
			}

			if (!trainsToRemove.isEmpty()) {
				trainsToRemove.forEach(trains::remove);
				final PacketByteBuf packet = PacketByteBufs.create();
				packet.writeLong(id);
				packet.writeString(KEY_TRAINS);
				packet.writeBoolean(true);
				packet.writeInt(trains.size());
				trains.forEach(train -> packet.writeLong(train.id));
				world.getPlayers().forEach(serverPlayer -> ServerPlayNetworking.send((ServerPlayerEntity) serverPlayer, PACKET_UPDATE_SIDING, packet));
			}
		}
	}

	public boolean getUnlimitedTrains() {
		return unlimitedTrains;
	}

	public void clearTrains() {
		trains.clear();
	}

	private void setTrainDetails(String customId, TrainType trainType) {
		trainTypeMapping = new CustomResources.TrainMapping(customId, trainType);
		trainLength = (int) Math.floor(railLength / trainTypeMapping.trainType.getSpacing());
	}

	private void generateDefaultPath(Map<BlockPos, Map<BlockPos, Rail>> rails) {
		trains.clear();

		final List<BlockPos> orderedPositions = getOrderedPositions(new BlockPos(0, 0, 0), false);
		final BlockPos pos1 = orderedPositions.get(0);
		final BlockPos pos2 = orderedPositions.get(1);
		if (RailwayData.containsRail(rails, pos1, pos2)) {
			path.add(new PathData(rails.get(pos1).get(pos2), id, 0, pos1, pos2, -1));
		}

		trains.add(new Train(null, 0, id, railLength, path, distances));
	}

	private void generateDistances() {
		distances.clear();
		float sum = 0;
		for (final PathData pathData : path) {
			sum += pathData.rail.getLength();
			distances.add(sum);
		}
		if (path.size() != 1) {
			trains.removeIf(train -> (train.id == 0) == unlimitedTrains);
		}
	}

	@Override
	public int compareTo(NameColorDataBase compare) {
		final int superCompare = super.compareTo(compare);
		return superCompare == 0 ? new Random().nextBoolean() ? 1 : -1 : superCompare;
	}
}
