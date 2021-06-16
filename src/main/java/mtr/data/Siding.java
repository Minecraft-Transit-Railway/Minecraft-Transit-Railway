package mtr.data;

import mtr.config.CustomResources;
import mtr.path.PathData;
import mtr.path.PathFinder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Siding extends SavedRailBase {

	private World world;
	private Depot depot;
	private CustomResources.TrainMapping trainTypeMapping;
	private int trainLength;
	private boolean unlimitedTrains;

	private final float railLength;
	private final List<PathData> path = new ArrayList<>();
	private final List<Float> distances = new ArrayList<>();
	private final List<Train> trains = new ArrayList<>();

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

	public Siding(CompoundTag tag) {
		super(tag);

		railLength = tag.getFloat(KEY_RAIL_LENGTH);
		TrainType trainType = TrainType.values()[0];
		try {
			trainType = TrainType.valueOf(tag.getString(KEY_TRAIN_TYPE));
		} catch (Exception ignored) {
		}
		setTrainDetails(tag.getString(KEY_TRAIN_CUSTOM_ID), trainType);
		unlimitedTrains = tag.getBoolean(KEY_UNLIMITED_TRAINS);

		final CompoundTag tagTrains = tag.getCompound(KEY_TRAINS);
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
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = super.toCompoundTag();

		tag.putFloat(KEY_RAIL_LENGTH, railLength);
		tag.putString(KEY_TRAIN_CUSTOM_ID, trainTypeMapping.customId);
		tag.putString(KEY_TRAIN_TYPE, trainTypeMapping.trainType.toString());
		tag.putBoolean(KEY_UNLIMITED_TRAINS, unlimitedTrains);

		final CompoundTag tagTrains = new CompoundTag();
		for (int i = 0; i < trains.size(); i++) {
			tagTrains.put(KEY_TRAINS + i, trains.get(i).toCompoundTag());
		}
		tag.put(KEY_TRAINS, tagTrains);

		return tag;
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
				final int index = packet.readInt();
				if (trains.size() <= index) {
					trains.add(new Train(id, railLength, path, distances, packet));
				} else {
					trains.get(index).update(packet);
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
				path.add(new PathData(rails.get(pos1).get(pos2), 0, pos1, pos2, -1));
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

	public void simulateTrain(PlayerEntity clientPlayer, float ticksElapsed, Train.RenderTrainCallback renderTrainCallback, Train.RenderConnectionCallback renderConnectionCallback, Train.SpeedCallback speedCallback, Train.AnnouncementCallback announcementCallback, Runnable generateRoute) {
		if (trains.isEmpty()) {
			trains.add(new Train(id, railLength, path, distances));
		}

		int trainsAtDepot = 0;
		final List<Train> trainsToRemove = new ArrayList<>();
		for (int i = 0; i < trains.size(); i++) {
			final Train train = trains.get(i);
			train.simulateTrain(world, i, clientPlayer, ticksElapsed, depot, trainTypeMapping, trainLength, renderTrainCallback, renderConnectionCallback, speedCallback, announcementCallback, generateRoute);
			if (train.closeToDepot(trainTypeMapping.trainType.getSpacing() * trainLength)) {
				trainsAtDepot++;
				if (trainsAtDepot > 1) {
					trainsToRemove.add(train);
				}
			}
		}

		if (unlimitedTrains && trainsAtDepot == 0) {
			trains.add(new Train(id, railLength, path, distances));
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
