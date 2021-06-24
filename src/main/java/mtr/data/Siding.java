package mtr.data;

import mtr.config.CustomResources;
import mtr.packet.IPacket;
import mtr.path.PathData;
import mtr.path.PathFinder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
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

		final NbtCompound tagTrains = nbtCompound.getCompound(KEY_TRAINS);
		tagTrains.getKeys().forEach(key -> trains.add(new Train(id, railLength, path, distances, tagTrains.getCompound(key))));
	}

	public Siding(PacketByteBuf packet) {
		super(packet);

		railLength = packet.readFloat();
		setTrainDetails(packet.readString(PACKET_STRING_READ_LENGTH), TrainType.values()[packet.readInt()]);
		unlimitedTrains = packet.readBoolean();

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

		final NbtCompound tagTrains = new NbtCompound();
		int i = 0;
		for (final Train train : trains) {
			tagTrains.put(KEY_TRAINS + i, train.toCompoundTag());
			i++;
		}
		nbtCompound.put(KEY_TRAINS, tagTrains);

		return nbtCompound;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);

		packet.writeFloat(railLength);
		packet.writeString(trainTypeMapping.customId);
		packet.writeInt(trainTypeMapping.trainType.ordinal());
		packet.writeBoolean(unlimitedTrains);

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
					final Train newTrain = new Train(trainId, id, railLength, path, distances);
					trains.add(newTrain);
					newTrain.update(KEY_TRAINS, packet);
				}
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

	public int generateRoute(World world, Map<BlockPos, Map<BlockPos, Rail>> rails, Set<Platform> platforms, Set<Route> routes, Set<Depot> depots) {
		this.world = world;
		final BlockPos midPos = getMidPos();
		depot = depots.stream().filter(depot1 -> depot1.inArea(midPos.getX(), midPos.getZ())).findFirst().orElse(null);

		final List<SavedRailBase> platformsInRoute = new ArrayList<>();
		platformsInRoute.add(this);
		if (depot != null) {
			depot.routeIds.forEach(routeId -> {
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
		}
		platformsInRoute.add(this);

		final int successfulSegments = PathFinder.findPath(path, rails, platformsInRoute);
		if (path.isEmpty()) {
			final List<BlockPos> orderedPositions = getOrderedPositions(new BlockPos(0, 0, 0), false);
			final BlockPos pos1 = orderedPositions.get(0);
			final BlockPos pos2 = orderedPositions.get(1);
			if (RailwayData.containsRail(rails, pos1, pos2)) {
				path.add(new PathData(rails.get(pos1).get(pos2), id, 0, pos1, pos2, -1));
			}
		}

		distances.clear();
		float sum = 0;
		for (final PathData pathData : path) {
			sum += pathData.rail.getLength();
			distances.add(sum);
		}

		return successfulSegments;
	}

	public void writeTrainPositions(Set<Rail> trainPositions, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		trains.forEach(train -> train.writeTrainPositions(trainPositions, rails, trainTypeMapping, trainLength));
	}

	public void simulateTrain(PlayerEntity clientPlayer, float ticksElapsed, Set<Rail> trainPositions, Train.RenderTrainCallback renderTrainCallback, Train.RenderConnectionCallback renderConnectionCallback, Train.SpeedCallback speedCallback, Train.AnnouncementCallback announcementCallback, Train.WriteScheduleCallback writeScheduleCallback, Runnable generateRoute) {
		int trainsAtDepot = 0;
		boolean spawnTrain = true;

		final Set<Float> railProgressSet = new HashSet<>();
		final List<Train> trainsToRemove = new ArrayList<>();
		for (final Train train : trains) {
			train.simulateTrain(world, clientPlayer, ticksElapsed, depot, trainTypeMapping, trainLength, trainPositions, renderTrainCallback, renderConnectionCallback, speedCallback, announcementCallback, writeScheduleCallback, generateRoute);
			if (train.closeToDepot(trainTypeMapping.trainType.getSpacing() * trainLength)) {
				spawnTrain = false;
			}
			if (train.atDepot()) {
				trainsAtDepot++;
				if (trainsAtDepot > 1) {
					trainsToRemove.add(train);
				}
			}
			if (railProgressSet.contains(train.getRailProgress())) {
				trainsToRemove.add(train);
				System.out.println("Removed stacked train");
			}
			railProgressSet.add(train.getRailProgress());
		}

		if (!world.isClient() && (trains.isEmpty() || unlimitedTrains && spawnTrain)) {
			final long trainId = new Random().nextLong();
			trains.add(new Train(trainId, id, railLength, path, distances));
		}

		trainsToRemove.forEach(trains::remove);
	}

	public boolean getUnlimitedTrains() {
		return unlimitedTrains;
	}

	private void setTrainDetails(String customId, TrainType trainType) {
		trainTypeMapping = new CustomResources.TrainMapping(customId, trainType);
		trainLength = (int) Math.floor(railLength / trainTypeMapping.trainType.getSpacing());
	}
}
