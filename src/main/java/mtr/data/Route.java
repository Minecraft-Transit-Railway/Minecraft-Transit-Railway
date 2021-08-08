package mtr.data;

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

	private String customDestination;
	private boolean shuffleTrains;

	public final List<Long> platformIds;

	private final List<PathDataDeleteThisLater> path;

	public static final int HOURS_IN_DAY = 24;
	public static final int TICKS_PER_HOUR = 1000;
	public static final int TICKS_PER_DAY = HOURS_IN_DAY * TICKS_PER_HOUR;

	private static final String KEY_PLATFORM_IDS = "platform_ids";
	private static final String KEY_CUSTOM_DESTINATION = "custom_destination";
	private static final String KEY_SHUFFLE_TRAINS = "shuffle_trains";
	private static final String KEY_PATH = "path";

	public Route() {
		this(0);
	}

	public Route(long id) {
		super(id);
		platformIds = new ArrayList<>();
		path = new ArrayList<>();
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

		customDestination = nbtCompound.getString(KEY_CUSTOM_DESTINATION);
		shuffleTrains = nbtCompound.getBoolean(KEY_SHUFFLE_TRAINS);

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

		customDestination = packet.readString(PACKET_STRING_READ_LENGTH);
		shuffleTrains = packet.readBoolean();

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
	}

	public void setPlatformIds(Consumer<PacketByteBuf> sendPacket) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_PLATFORM_IDS);
		packet.writeInt(platformIds.size());
		platformIds.forEach(packet::writeLong);
		sendPacket.accept(packet);
	}

	// TODO temporary code start
	public void generateRails(World world, RailwayData railwayData) {
		path.forEach(pathDataDeleteThisLater -> {
			final Pos3f pos3f = pathDataDeleteThisLater.getPosition(0);
			final BlockEntity entity = world.getBlockEntity(new BlockPos(pos3f.x, pos3f.y, pos3f.z));
			if (entity instanceof BlockRail.TileEntityRail) {
				((BlockRail.TileEntityRail) entity).railMap.forEach((blockPos, rail) -> {
					railwayData.addRail(entity.getPos(), blockPos, rail, false);
					railwayData.addRail(blockPos, entity.getPos(), new Rail(blockPos, rail.facingEnd, entity.getPos(),
							rail.facingStart, rail.railType, rail.ballastTexture), false);
				});
			}
		});
	}
	// TODO temporary code end

	public static class ScheduleEntry {

		public final float arrivalMillis;
		public final float departureMillis;
		public final TrainType trainType;
		public final long platformId;
		public final String destination;
		public final boolean isTerminating;

		public ScheduleEntry(float arrivalMillis, float departureMillis, TrainType trainType, long platformId, String destination, boolean isTerminating) {
			this.arrivalMillis = arrivalMillis;
			this.departureMillis = departureMillis;
			this.trainType = trainType;
			this.platformId = platformId;
			this.destination = destination;
			this.isTerminating = isTerminating;
		}
	}
}
