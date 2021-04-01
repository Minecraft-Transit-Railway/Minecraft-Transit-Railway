package mtr.data;

import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPlatform;
import mtr.entity.EntitySeat;
import mtr.gui.IGui;
import mtr.path.PathData;
import mtr.path.PathFinder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class Route extends NameColorDataBase implements IGui {

	private PathFinder routePathFinder;
	private String customDestination;
	private boolean shuffleTrains;

	public final List<Long> platformIds;
	public final List<TrainType> trainTypes;

	private final List<PathData> path;
	private final Map<Integer, TrainType> schedule;

	private final int[] frequencies;

	public static final int HOURS_IN_DAY = 24;
	public static final int TICKS_PER_HOUR = 1000;
	public static final int TICKS_PER_DAY = HOURS_IN_DAY * TICKS_PER_HOUR;

	private static final float INNER_PADDING = 0.5F;
	private static final int BOX_PADDING = 3;

	private static final float CONNECTION_HEIGHT = 2.25F;
	private static final float CONNECTION_Z_OFFSET = 0.5F;
	private static final float CONNECTION_X_OFFSET = 0.25F;

	private static final String KEY_PLATFORM_IDS = "platform_ids";
	private static final String KEY_TRAIN_TYPES = "train_types";
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
		trainTypes = new ArrayList<>();
		path = new ArrayList<>();
		schedule = new HashMap<>();
		frequencies = new int[HOURS_IN_DAY];
		customDestination = "";
		shuffleTrains = true;
	}

	public Route(CompoundTag tag) {
		super(tag);

		platformIds = new ArrayList<>();
		final long[] platformIdsArray = tag.getLongArray(KEY_PLATFORM_IDS);
		for (final long platformId : platformIdsArray) {
			platformIds.add(platformId);
		}

		trainTypes = new ArrayList<>();
		final int trainTypesLength = tag.getInt(KEY_TRAIN_TYPES);
		for (int i = 0; i < trainTypesLength; i++) {
			try {
				trainTypes.add(TrainType.valueOf(tag.getString(KEY_TRAIN_TYPES + i)));
			} catch (Exception ignored) {
			}
		}

		frequencies = new int[HOURS_IN_DAY];
		for (int i = 0; i < HOURS_IN_DAY; i++) {
			frequencies[i] = tag.getInt(KEY_FREQUENCIES + i);
		}

		customDestination = tag.getString(KEY_CUSTOM_DESTINATION);
		shuffleTrains = tag.getBoolean(KEY_SHUFFLE_TRAINS);

		path = new ArrayList<>();
		final CompoundTag tagPath = tag.getCompound(KEY_PATH);
		for (int i = 0; i < tagPath.getKeys().size(); i++) {
			path.add(new PathData(tagPath.getCompound(KEY_PATH + i)));
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

		trainTypes = new ArrayList<>();
		final int trainTypeCount = packet.readInt();
		for (int i = 0; i < trainTypeCount; i++) {
			trainTypes.add(TrainType.values()[packet.readInt()]);
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
			path.add(new PathData(packet));
		}

		schedule = new HashMap<>();
		generateSchedule();
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = super.toCompoundTag();
		tag.putLongArray(KEY_PLATFORM_IDS, platformIds);

		tag.putInt(KEY_TRAIN_TYPES, trainTypes.size());
		for (int i = 0; i < trainTypes.size(); i++) {
			tag.putString(KEY_TRAIN_TYPES + i, trainTypes.get(i).toString());
		}

		for (int i = 0; i < HOURS_IN_DAY; i++) {
			tag.putInt(KEY_FREQUENCIES + i, frequencies[i]);
		}

		tag.putString(KEY_CUSTOM_DESTINATION, customDestination);
		tag.putBoolean(KEY_SHUFFLE_TRAINS, shuffleTrains);

		final CompoundTag tagPath = new CompoundTag();
		for (int i = 0; i < path.size(); i++) {
			tagPath.put(KEY_PATH + i, path.get(i).toCompoundTag());
		}
		tag.put(KEY_PATH, tagPath);

		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(platformIds.size());
		platformIds.forEach(packet::writeLong);
		packet.writeInt(trainTypes.size());
		trainTypes.forEach(trainType -> packet.writeInt(trainType.ordinal()));

		for (final int frequency : frequencies) {
			packet.writeInt(frequency);
		}

		packet.writeString(customDestination);
		packet.writeBoolean(shuffleTrains);

		packet.writeInt(path.size());
		path.forEach(pathData -> pathData.writePacket(packet));
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
				trainTypes.clear();
				final int trainTypeCount = packet.readInt();
				for (int i = 0; i < trainTypeCount; i++) {
					trainTypes.add(TrainType.values()[packet.readInt()]);
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

	public void setTrainTypes(Consumer<PacketByteBuf> sendPacket) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_TRAIN_TYPES);
		packet.writeInt(trainTypes.size());
		trainTypes.forEach(trainType -> packet.writeInt(trainType.ordinal()));
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

	public void startFindingPath(WorldAccess world, Set<Platform> platforms) {
		if (routePathFinder == null) {
			routePathFinder = new PathFinder(world, platformIds.stream().map(platformId -> RailwayData.getDataById(platforms, platformId)).collect(Collectors.toList()));
		}
	}

	public boolean findPath() {
		if (routePathFinder != null) {
			try {
				final List<PathData> result = routePathFinder.findPath();
				if (result != null) {
					path.clear();
					path.addAll(result);
					routePathFinder = null;
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				path.clear();
				return true;
			}
		} else {
			path.clear();
			return true;
		}
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
		packet.writeInt(frequencies.length);
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

	public void getPositionYaw(WorldAccess world, long worldTime) {
		getPositionYaw(world, worldTime + 1, 0, null, null, null, null);
	}

	public void getPositionYaw(WorldAccess world, float worldTime, float tickDelta, EntitySeat clientSeat, PositionYawCallback positionYawCallback, RenderConnectionCallback renderConnectionCallback, SpeedCallback speedCallback) {
		schedule.forEach((scheduleTime, trainType) -> {
			final float ticks = wrapTime(worldTime, scheduleTime);
			final List<Pos3f> positions = getPositions(ticks, trainType.getSpacing());
			final float doorValue = getDoorValue(ticks);
			final float speed = getSpeed(ticks);

			float renderOffsetX = 0, renderOffsetY = 0, renderOffsetZ = 0;
			boolean shouldOffsetRender = false;
			if (world.isClient() && clientSeat != null && clientSeat.hasPassengers() && clientSeat.isScheduleTimeAndRouteId(scheduleTime, id)) {
				final float ridingPercentageZ = clientSeat.getRidingPercentageZ(tickDelta);
				final int ridingCar = (int) Math.floor(ridingPercentageZ);
				if (ridingCar < positions.size() - 1) {
					final Pos3f pos1 = positions.get(ridingCar);
					final Pos3f pos2 = positions.get(ridingCar + 1);
					if (pos1 != null && pos2 != null) {
						final float yaw = (float) MathHelper.atan2(pos2.x - pos1.x, pos2.z - pos1.z);
						final float pitch = (float) Math.asin((pos2.y - pos1.y) / pos2.getDistanceTo(pos1));
						final Vec3d ridingOffset = new Vec3d(getValueFromPercentage(clientSeat.getRidingPercentageX(tickDelta), trainType.width), 0, getValueFromPercentage(MathHelper.fractionalPart(ridingPercentageZ), pos2.getDistanceTo(pos1))).rotateX(pitch).rotateY(yaw);
						final float absoluteX = getAverage(pos1.x, pos2.x);
						final float absoluteY = getAverage(pos1.y, pos2.y);
						final float absoluteZ = getAverage(pos1.z, pos2.z);

						if (speed > 0) {
							renderOffsetX = absoluteX + (float) ridingOffset.x;
							renderOffsetY = absoluteY + (float) ridingOffset.y + 1;
							renderOffsetZ = absoluteZ + (float) ridingOffset.z;
							shouldOffsetRender = true;
							clientSeat.getPlayer().yaw -= Math.toDegrees(yaw - clientSeat.prevTrainYaw);
						}

						clientSeat.prevTrainYaw = yaw;

						if (speedCallback != null) {
							speedCallback.speedCallback(speed * 20, (int) absoluteX, (int) absoluteY, (int) absoluteZ);
						}
					}
				}
			}

			float prevCarX = 0, prevCarY = 0, prevCarZ = 0, prevCarYaw = 0, prevCarPitch = 0;
			int previousRendered = 0;

			for (int i = 0; i < positions.size() - 1; i++) {
				final Pos3f pos1 = positions.get(i);
				final Pos3f pos2 = positions.get(i + 1);

				if (pos1 != null && pos2 != null) {
					final float absoluteX = getAverage(pos1.x, pos2.x);
					final float absoluteY = getAverage(pos1.y, pos2.y);
					final float absoluteZ = getAverage(pos1.z, pos2.z);
					final float x = absoluteX - renderOffsetX;
					final float y = absoluteY - renderOffsetY + 1;
					final float z = absoluteZ - renderOffsetZ;

					final float realSpacing = pos2.getDistanceTo(pos1);
					final float halfSpacing = realSpacing / 2;
					final float halfWidth = trainType.width / 2F;
					final float yaw = (float) MathHelper.atan2(pos2.x - pos1.x, pos2.z - pos1.z);
					final float pitch = (float) Math.asin((pos2.y - pos1.y) / realSpacing);

					final boolean isEnd1Head = i == 0;
					final boolean isEnd2Head = i == positions.size() - 2;

					if (world.isClient()) {
						final boolean doorLeftOpen = openDoors(world, absoluteX, absoluteY, absoluteZ, (float) Math.PI + yaw, halfSpacing, doorValue) && doorValue > 0;
						final boolean doorRightOpen = openDoors(world, absoluteX, absoluteY, absoluteZ, yaw, halfSpacing, doorValue) && doorValue > 0;

						if (positionYawCallback != null) {
							positionYawCallback.positionYawCallback(x, y, z, (float) Math.toDegrees(yaw), (float) Math.toDegrees(pitch), trainType, isEnd1Head, isEnd2Head, doorLeftOpen ? doorValue : 0, doorRightOpen ? doorValue : 0, shouldOffsetRender);
						}

						previousRendered--;
						if (renderConnectionCallback != null && i > 0 && trainType.shouldRenderConnection && previousRendered > 0) {
							final float xStart = halfWidth - CONNECTION_X_OFFSET;
							final float zStart = trainType.getSpacing() / 2F - CONNECTION_Z_OFFSET;

							final Pos3f prevPos1 = new Pos3f(xStart, SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(prevCarX, prevCarY, prevCarZ);
							final Pos3f prevPos2 = new Pos3f(xStart, CONNECTION_HEIGHT + SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(prevCarX, prevCarY, prevCarZ);
							final Pos3f prevPos3 = new Pos3f(-xStart, CONNECTION_HEIGHT + SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(prevCarX, prevCarY, prevCarZ);
							final Pos3f prevPos4 = new Pos3f(-xStart, SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(prevCarX, prevCarY, prevCarZ);

							final Pos3f thisPos1 = new Pos3f(-xStart, SMALL_OFFSET, -zStart).rotateX(pitch).rotateY(yaw).add(x, y, z);
							final Pos3f thisPos2 = new Pos3f(-xStart, CONNECTION_HEIGHT + SMALL_OFFSET, -zStart).rotateX(pitch).rotateY(yaw).add(x, y, z);
							final Pos3f thisPos3 = new Pos3f(xStart, CONNECTION_HEIGHT + SMALL_OFFSET, -zStart).rotateX(pitch).rotateY(yaw).add(x, y, z);
							final Pos3f thisPos4 = new Pos3f(xStart, SMALL_OFFSET, -zStart).rotateX(pitch).rotateY(yaw).add(x, y, z);

							renderConnectionCallback.renderConnectionCallback(prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, x, y, z, trainType, shouldOffsetRender);
						}

						prevCarX = x;
						prevCarY = y;
						prevCarZ = z;
						prevCarYaw = yaw;
						prevCarPitch = pitch;
						previousRendered = 2;
					} else {
						final PlayerEntity closestPlayer = world.getClosestPlayer(x, y, z, EntitySeat.DETAIL_RADIUS, false);
						if (closestPlayer != null) {
							final boolean doorLeftOpen = openDoors(world, x, y, z, (float) Math.PI + yaw, halfSpacing, doorValue) && doorValue > 0;
							final boolean doorRightOpen = openDoors(world, x, y, z, yaw, halfSpacing, doorValue) && doorValue > 0;

							final int ridingCar = i;
							final float margin = halfSpacing + BOX_PADDING + speed * 2;
							world.getEntitiesByClass(EntitySeat.class, new Box(x + margin, y + margin, z + margin, x - margin, y - margin, z - margin), entitySeat -> true).forEach(entitySeat -> {
								final PlayerEntity serverPlayer = entitySeat.getPlayer();
								if (serverPlayer == null) {
									return;
								}
								final Vec3d positionRotated = entitySeat.getPos().subtract(x, y, z).rotateY(-yaw).rotateX(-pitch);

								if (Math.abs(positionRotated.x) <= halfWidth + INNER_PADDING && Math.abs(positionRotated.y) <= 1.5) {
									if ((doorLeftOpen || doorRightOpen) && !entitySeat.getIsRiding() && Math.abs(positionRotated.z) <= halfSpacing) {
										entitySeat.resetSeatCoolDown();
										serverPlayer.startRiding(entitySeat);
										entitySeat.setScheduleTimeAndRouteId(scheduleTime, id);
										entitySeat.ridingPercentageX = (float) (positionRotated.x / trainType.width + 0.5);
										entitySeat.ridingPercentageZ = (float) (positionRotated.z / realSpacing + 0.5) + ridingCar;
									}

									if (entitySeat.getIsRiding() && ridingCar == Math.floor(entitySeat.ridingPercentageZ) && entitySeat.isScheduleTimeAndRouteId(scheduleTime, id)) {
										final Vec3d velocity = new Vec3d(getValueFromPercentage(entitySeat.ridingPercentageX, trainType.width), 0, getValueFromPercentage(MathHelper.fractionalPart(entitySeat.ridingPercentageZ), realSpacing)).rotateX(pitch).rotateY(yaw).add(x, y, z);
										entitySeat.updatePositionAndAngles(velocity.x, velocity.y, velocity.z, 0, 0);
										entitySeat.fallDistance = 0;
										entitySeat.resetSeatCoolDown();

										final Vec3d movement = new Vec3d(serverPlayer.sidewaysSpeed / 3, 0, serverPlayer.forwardSpeed / 3).rotateY((float) -Math.toRadians(serverPlayer.yaw) - yaw);
										entitySeat.ridingPercentageX += movement.x / trainType.width;
										entitySeat.ridingPercentageZ += movement.z / realSpacing;
										entitySeat.ridingPercentageX = MathHelper.clamp(entitySeat.ridingPercentageX, doorLeftOpen ? -1 : 0, doorRightOpen ? 2 : 1);
										entitySeat.ridingPercentageZ = MathHelper.clamp(entitySeat.ridingPercentageZ, 0, positions.size() - 1.01F);
										entitySeat.updateRidingPercentage();
										entitySeat.setScheduleTimeAndRouteId(scheduleTime, id);
									}
								}
							});

							final BlockPos soundPos = new BlockPos(x, y, z);
							trainType.playSpeedSoundEffect(world, worldTime, soundPos, speed, getSpeed(ticks + 1));

							final float newDoorValue = getDoorValue(ticks + 1);
							if (doorValue == 0 && newDoorValue > 0 && trainType.doorOpenSoundEvent != null) {
								world.playSound(null, soundPos, trainType.doorOpenSoundEvent, SoundCategory.BLOCKS, 1, 1);
							} else if (doorValue == 1 && newDoorValue < 1 && trainType.doorCloseSoundEvent != null) {
								world.playSound(null, soundPos, trainType.doorCloseSoundEvent, SoundCategory.BLOCKS, 1, 1);
							}
						}
					}
				}
			}
		});
	}

	public Map<Long, Set<ScheduleEntry>> getTimeOffsets(Set<Platform> platforms) {
		final Map<Long, Set<ScheduleEntry>> timeOffsets = new HashMap<>();

		int platformIdIndex = 0, pathIndex = 0;
		while (platformIdIndex < platformIds.size() && pathIndex < path.size()) {
			final PathData pathData = path.get(pathIndex);
			final long platformId = platformIds.get(platformIdIndex);

			if (pathData.isPlatform(platformId, platforms)) {
				final float arrivalTime = pathData.tOffset + pathData.tEnd;
				final float departureTime = pathData.tOffset + pathData.getTime();
				timeOffsets.put(platformId, new HashSet<>());

				schedule.forEach((scheduleTime, trainType) -> timeOffsets.get(platformId).add(new ScheduleEntry(arrivalTime + scheduleTime, departureTime + scheduleTime, trainType, platformIds.get(platformIds.size() - 1))));
				platformIdIndex++;
			}

			pathIndex++;
		}

		return timeOffsets;
	}

	public static float wrapTime(float time1, float time2) {
		return (time1 - time2 + Route.TICKS_PER_DAY) % Route.TICKS_PER_DAY;
	}

	private List<Pos3f> getPositions(float value, int trainSpacing) {
		final List<Pos3f> positions = new ArrayList<>();

		int pathDataIndex = getPathDataIndex(value);
		if (pathDataIndex < 0) {
			return new ArrayList<>();
		}

		final int trainCars = (int) Math.floor(path.get(0).length / trainSpacing);

		float positionIndex = path.get(pathDataIndex).getPositionIndex(value);
		if (positionIndex < 0) {
			return new ArrayList<>();
		}

		int segmentsCreated = 0;
		float length = path.get(pathDataIndex).length;

		while (true) {
			final PathData pathData = path.get(pathDataIndex);
			length -= pathData.length;

			while (positionIndex >= length) {
				positions.add(pathData.getPosition(positionIndex - length));
				positionIndex -= trainSpacing;
				segmentsCreated++;
				if (segmentsCreated > trainCars) {
					return positions;
				}
			}

			pathDataIndex--;
			if (pathDataIndex < 0) {
				return new ArrayList<>();
			}
		}
	}

	private float getDoorValue(float value) {
		final int pathDataIndex = getPathDataIndex(value);
		return pathDataIndex < 0 ? 0 : path.get(pathDataIndex).getDoorValue(value);
	}

	private int getPathDataIndex(float value) {
		for (int i = 0; i < path.size(); i++) {
			final float thisTPrevious = path.get(i).tOffset;
			final float nextTPrevious = i + 1 < path.size() ? path.get(i + 1).tOffset : value + 1;
			if (value >= thisTPrevious && value < nextTPrevious) {
				return i;
			}
		}
		return -1;
	}

	private float getSpeed(float value) {
		final int pathDataIndex = getPathDataIndex(value);
		return pathDataIndex < 0 ? 0 : path.get(pathDataIndex).getSpeed(value);
	}

	private float getHeadway(int hour) {
		return frequencies[hour] == 0 ? 0 : 2F * TICKS_PER_HOUR / frequencies[hour];
	}

	private void generateSchedule() {
		schedule.clear();

		if (trainTypes.size() > 0) {
			int lastTime = -TICKS_PER_DAY;
			int lastTrainTypeIndex = -1;

			for (int i = 0; i < TICKS_PER_DAY; i++) {
				final float headway = getHeadway(i / TICKS_PER_HOUR);

				if (headway > 0 && i >= headway + lastTime) {
					final TrainType trainType;
					if (shuffleTrains) {
						trainType = trainTypes.get(new Random().nextInt(trainTypes.size()));
					} else {
						lastTrainTypeIndex++;
						if (lastTrainTypeIndex >= trainTypes.size()) {
							lastTrainTypeIndex = 0;
						}
						trainType = trainTypes.get(lastTrainTypeIndex);
					}

					schedule.put((int) wrapTime(i, 6000), trainType);
					lastTime = i;
				}
			}
		}
	}

	private static boolean openDoors(WorldAccess world, float trainX, float trainY, float trainZ, float checkYaw, float halfSpacing, float doorValue) {
		boolean hasPlatform = false;
		final Vec3d offsetVec = new Vec3d(1, 0, 0).rotateY(checkYaw);
		final Vec3d traverseVec = new Vec3d(0, 0, 1).rotateY(checkYaw);

		for (int checkX = 1; checkX <= 3; checkX++) {
			for (int checkY = -1; checkY <= 0; checkY++) {
				for (float checkZ = -halfSpacing; checkZ <= halfSpacing; checkZ++) {
					final BlockPos checkPos = new BlockPos(trainX + offsetVec.x * checkX + traverseVec.x * checkZ, trainY + checkY, trainZ + offsetVec.z * checkX + traverseVec.z * checkZ);
					final Block block = world.getBlockState(checkPos).getBlock();

					if (block instanceof BlockPlatform || block instanceof BlockPSDAPGBase) {
						if (world.isClient()) {
							return true;
						} else if (block instanceof BlockPSDAPGDoorBase) {
							for (int i = -1; i <= 1; i++) {
								final BlockState state = world.getBlockState(checkPos.up(i));
								if (state.getBlock() instanceof BlockPSDAPGDoorBase) {
									((World) world).setBlockState(checkPos.up(i), state.with(BlockPSDAPGDoorBase.OPEN, (int) (doorValue * BlockPSDAPGDoorBase.MAX_OPEN_VALUE)));
								}
							}
						}

						hasPlatform = true;
					}
				}
			}
		}

		return hasPlatform;
	}

	private static float getAverage(float a, float b) {
		return (a + b) / 2;
	}

	private static float getValueFromPercentage(float percentage, float total) {
		return (percentage - 0.5F) * total;
	}

	public static class ScheduleEntry {

		public final float arrivalTime;
		public final float departureTime;
		public final TrainType trainType;
		public final long lastStationId;

		public ScheduleEntry(float arrivalTime, float departureTime, TrainType trainType, long lastStationId) {
			this.arrivalTime = arrivalTime % TICKS_PER_DAY;
			this.departureTime = departureTime % TICKS_PER_DAY;
			this.trainType = trainType;
			this.lastStationId = lastStationId;
		}
	}

	@FunctionalInterface
	public interface PositionYawCallback {
		void positionYawCallback(float x, float y, float z, float yaw, float pitch, TrainType trainType, boolean isEnd1Head, boolean isEnd2Head, float doorLeftValue, float doorRightValue, boolean shouldOffsetRender);
	}

	@FunctionalInterface
	public interface RenderConnectionCallback {
		void renderConnectionCallback(Pos3f prevPos1, Pos3f prevPos2, Pos3f prevPos3, Pos3f prevPos4, Pos3f thisPos1, Pos3f thisPos2, Pos3f thisPos3, Pos3f thisPos4, float x, float y, float z, TrainType trainType, boolean shouldOffsetRender);
	}

	@FunctionalInterface
	public interface SpeedCallback {
		void speedCallback(float speed, int x, int y, int z);
	}
}
