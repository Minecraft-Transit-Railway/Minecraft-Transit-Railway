package mtr.data;

import mtr.block.BlockRail;
import mtr.config.CustomResources;
import mtr.path.PathDataDeleteThisLater;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class Route extends NameColorDataBase implements IGui {

	private String customDestination;
	private boolean shuffleTrains;

	public final List<Long> platformIds;
	public final List<CustomResources.TrainMapping> trainTypeMappings;

	private final List<PathDataDeleteThisLater> path;
	private final Map<Integer, CustomResources.TrainMapping> schedule;

	private final int[] frequencies;

	public static final int HOURS_IN_DAY = 24;
	public static final int TICKS_PER_HOUR = 1000;
	public static final int TICKS_PER_DAY = HOURS_IN_DAY * TICKS_PER_HOUR;

	private static final String KEY_PLATFORM_IDS = "platform_ids";
	private static final String KEY_TRAIN_TYPES = "train_types";
	private static final String KEY_TRAIN_CUSTOM_ID = "train_custom_id";
	private static final String KEY_FREQUENCIES = "frequencies";
	private static final String KEY_CUSTOM_DESTINATION = "custom_destination";
	private static final String KEY_SHUFFLE_TRAINS = "shuffle_trains";
	private static final String KEY_PATH = "path";

	public Route() {
		this(0);
	}

	public Route(long id) {
		super(id);
		platformIds = new ArrayList<>();
		trainTypeMappings = new ArrayList<>();
		path = new ArrayList<>();
		schedule = new HashMap<>();
		frequencies = new int[HOURS_IN_DAY];
		customDestination = "";
		shuffleTrains = true;
	}

	public Route(NbtCompound nbtCompound) {
		super(nbtCompound);

		platformIds = new ArrayList<>();
		final long[] platformIdsArray = nbtCompound.getLongArray(KEY_PLATFORM_IDS);
		for (final long platformId : platformIdsArray) {
			platformIds.add(platformId);
		}

		trainTypeMappings = new ArrayList<>();
		final int trainTypesLength = nbtCompound.getInt(KEY_TRAIN_TYPES);
		for (int i = 0; i < trainTypesLength; i++) {
			try {
				trainTypeMappings.add(new CustomResources.TrainMapping(nbtCompound.getString(KEY_TRAIN_CUSTOM_ID + i), TrainType.valueOf(nbtCompound.getString(KEY_TRAIN_TYPES + i))));
			} catch (Exception ignored) {
			}
		}

		frequencies = new int[HOURS_IN_DAY];
		for (int i = 0; i < HOURS_IN_DAY; i++) {
			frequencies[i] = nbtCompound.getInt(KEY_FREQUENCIES + i);
		}

		customDestination = nbtCompound.getString(KEY_CUSTOM_DESTINATION);
		shuffleTrains = nbtCompound.getBoolean(KEY_SHUFFLE_TRAINS);

		path = new ArrayList<>();
		final NbtCompound tagPath = nbtCompound.getCompound(KEY_PATH);
		for (int i = 0; i < tagPath.getKeys().size(); i++) {
			path.add(new PathDataDeleteThisLater(tagPath.getCompound(KEY_PATH + i)));
		}

		schedule = new HashMap<>();
		generateSchedule();
	}

	public Route(PacketByteBuf packet) {
		super(packet);

		platformIds = new ArrayList<>();
		final int platformCount = packet.readInt();
		for (int i = 0; i < platformCount; i++) {
			platformIds.add(packet.readLong());
		}

		trainTypeMappings = new ArrayList<>();
		final int trainTypeCount = packet.readInt();
		for (int i = 0; i < trainTypeCount; i++) {
			trainTypeMappings.add(new CustomResources.TrainMapping(packet.readString(PACKET_STRING_READ_LENGTH), TrainType.values()[packet.readInt()]));
		}

		frequencies = new int[HOURS_IN_DAY];
		for (int i = 0; i < HOURS_IN_DAY; i++) {
			frequencies[i] = packet.readInt();
		}

		customDestination = packet.readString(PACKET_STRING_READ_LENGTH);
		shuffleTrains = packet.readBoolean();

		path = new ArrayList<>();
		final int pathLength = packet.readInt();
		for (int i = 0; i < pathLength; i++) {
			path.add(new PathDataDeleteThisLater(packet));
		}

		schedule = new HashMap<>();
		generateSchedule();
	}

	@Override
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = super.toCompoundTag();
		nbtCompound.putLongArray(KEY_PLATFORM_IDS, platformIds);

		nbtCompound.putInt(KEY_TRAIN_TYPES, trainTypeMappings.size());
		for (int i = 0; i < trainTypeMappings.size(); i++) {
			nbtCompound.putString(KEY_TRAIN_CUSTOM_ID + i, trainTypeMappings.get(i).customId);
			nbtCompound.putString(KEY_TRAIN_TYPES + i, trainTypeMappings.get(i).trainType.toString());
		}

		for (int i = 0; i < HOURS_IN_DAY; i++) {
			nbtCompound.putInt(KEY_FREQUENCIES + i, frequencies[i]);
		}

		nbtCompound.putString(KEY_CUSTOM_DESTINATION, customDestination);
		nbtCompound.putBoolean(KEY_SHUFFLE_TRAINS, shuffleTrains);

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
		packet.writeInt(trainTypeMappings.size());
		trainTypeMappings.forEach(trainMapping -> {
			packet.writeString(trainMapping.customId);
			packet.writeInt(trainMapping.trainType.ordinal());
		});

		for (final int frequency : frequencies) {
			packet.writeInt(frequency);
		}

		packet.writeString(customDestination);
		packet.writeBoolean(shuffleTrains);

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
			case KEY_TRAIN_TYPES:
				trainTypeMappings.clear();
				final int trainTypeCount = packet.readInt();
				for (int i = 0; i < trainTypeCount; i++) {
					trainTypeMappings.add(new CustomResources.TrainMapping(packet.readString(PACKET_STRING_READ_LENGTH), TrainType.values()[packet.readInt()]));
				}
				break;
			case KEY_FREQUENCIES:
				for (int i = 0; i < HOURS_IN_DAY; i++) {
					frequencies[i] = packet.readInt();
				}
				break;
			case KEY_CUSTOM_DESTINATION:
				customDestination = packet.readString(PACKET_STRING_READ_LENGTH);
				break;
			case KEY_SHUFFLE_TRAINS:
				shuffleTrains = packet.readBoolean();
				break;
			default:
				super.update(key, packet);
				break;
		}
		generateSchedule();
	}

	public void setPlatformIds(Consumer<PacketByteBuf> sendPacket) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_PLATFORM_IDS);
		packet.writeInt(platformIds.size());
		platformIds.forEach(packet::writeLong);
		sendPacket.accept(packet);
	}

	public void setTrainTypeMappings(Consumer<PacketByteBuf> sendPacket) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_TRAIN_TYPES);
		packet.writeInt(trainTypeMappings.size());
		trainTypeMappings.forEach(trainMapping -> {
			packet.writeString(trainMapping.customId);
			packet.writeInt(trainMapping.trainType.ordinal());
		});
		sendPacket.accept(packet);
	}

	public void setCustomDestination(String newCustomDestination, Consumer<PacketByteBuf> sendPacket) {
		customDestination = newCustomDestination;

		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_CUSTOM_DESTINATION);
		packet.writeString(customDestination);
		sendPacket.accept(packet);
	}

	public void setShuffleTrains(boolean newShuffleTrains, Consumer<PacketByteBuf> sendPacket) {
		shuffleTrains = newShuffleTrains;

		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_SHUFFLE_TRAINS);
		packet.writeBoolean(shuffleTrains);
		sendPacket.accept(packet);
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
		}

		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_FREQUENCIES);
		for (final int frequency : frequencies) {
			packet.writeInt(frequency);
		}
		sendPacket.accept(packet);
	}

	public String getCustomDestination() {
		return customDestination;
	}

	public boolean getShuffleTrains() {
		return shuffleTrains;
	}

	public static float wrapTime(float time1, float time2) {
		return (time1 - time2 + Route.TICKS_PER_DAY) % Route.TICKS_PER_DAY;
	}

	// TODO temporary code start
	public void generateRails(World world, RailwayData railwayData) {
		path.forEach(pathDataDeleteThisLater -> {
			final Pos3f pos3f = pathDataDeleteThisLater.getPosition(0);
			final BlockEntity entity = world.getBlockEntity(new BlockPos(pos3f.x, pos3f.y, pos3f.z));
			if (entity instanceof BlockRail.TileEntityRail) {
				((BlockRail.TileEntityRail) entity).railMap.forEach((blockPos, rail) -> {
					railwayData.addRail(entity.getPos(), blockPos, rail, false);
					railwayData.addRail(blockPos, entity.getPos(), new Rail(blockPos, rail.facingEnd, entity.getPos(), rail.facingStart, rail.railType), false);
				});
			}
		});
	}
	// TODO temporary code end

	private float getHeadway(int hour) {
		return frequencies[hour] == 0 ? 0 : 2F * TICKS_PER_HOUR / frequencies[hour];
	}

	private void generateSchedule() {
		schedule.clear();

		if (trainTypeMappings.size() > 0) {
			int lastTime = -TICKS_PER_DAY;
			int lastTrainTypeIndex = -1;

			for (int i = 0; i < TICKS_PER_DAY; i++) {
				final float headway = getHeadway(i / TICKS_PER_HOUR);

				if (headway > 0 && i >= headway + lastTime) {
					final CustomResources.TrainMapping trainMapping;
					if (false) {
						// TODO fix shuffle trains
					} else {
						lastTrainTypeIndex++;
						if (lastTrainTypeIndex >= trainTypeMappings.size()) {
							lastTrainTypeIndex = 0;
						}
						trainMapping = trainTypeMappings.get(lastTrainTypeIndex);
					}

					schedule.put((int) wrapTime(i, 6000), trainMapping);
					lastTime = i;
				}
			}
		}
	}

	public static class ScheduleEntry {

		public final float arrivalMillis;
		public final float departureMillis;
		public final TrainType trainType;
		public final long lastPlatformId;
		public final long platformId;

		public ScheduleEntry(float arrivalMillis, float departureMillis, TrainType trainType, long lastPlatformId, long platformId) {
			this.arrivalMillis = arrivalMillis;
			this.departureMillis = departureMillis;
			this.trainType = trainType;
			this.lastPlatformId = lastPlatformId;
			this.platformId = platformId;
		}
	}
}
