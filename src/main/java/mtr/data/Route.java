package mtr.data;

import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPlatform;
import mtr.entity.EntitySeat;
import mtr.gui.IGui;
import mtr.path.PathData;
import mtr.path.PathFinder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.*;
import java.util.stream.Collectors;

public final class Route extends NameColorDataBase implements IGui {

	public String customDestination;
	public boolean shuffleTrains;

	public final List<Long> platformIds;
	public final List<TrainType> trainTypes;

	private final List<PathData> path;
	private final Map<Integer, TrainType> schedule;

	private final int[] frequencies;

	public static final int HOURS_IN_DAY = 24;
	public static final int TICKS_PER_HOUR = 1000;
	public static final int TICKS_PER_DAY = HOURS_IN_DAY * TICKS_PER_HOUR;

	private static final int SIMULATE_RADIUS = 128;
	private static final float INNER_PADDING = 0.5F;

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
		super();
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
		final int[] trainTypesIndices = tag.getIntArray(KEY_TRAIN_TYPES);
		for (final int trainTypeIndex : trainTypesIndices) {
			trainTypes.add(TrainType.values()[trainTypeIndex]);
		}

		frequencies = new int[HOURS_IN_DAY];
		for (int i = 0; i < HOURS_IN_DAY; i++) {
			frequencies[i] = tag.getInt(KEY_FREQUENCIES + i);
		}

		customDestination = tag.getString(KEY_CUSTOM_DESTINATION);
		shuffleTrains = tag.getBoolean(KEY_SHUFFLE_TRAINS);

		path = new ArrayList<>();
		final CompoundTag tagPath = tag.getCompound(KEY_PATH);
		for (final String key : tagPath.getKeys()) {
			path.add(new PathData(tagPath.getCompound(key)));
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
		tag.putIntArray(KEY_TRAIN_TYPES, trainTypes.stream().map(Enum::ordinal).collect(Collectors.toList()));

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

	public void generateGraph(WorldAccess world, Set<Platform> platforms) {
		System.out.println("generated");
		final PathFinder routePathFinder = new PathFinder(world, platformIds.stream().map(platformId -> RailwayData.getDataById(platforms, platformId)).collect(Collectors.toList()));
		path.clear();
		path.addAll(routePathFinder.findPath());
	}

	public int getFrequency(int index) {
		if (index >= 0 && index < frequencies.length) {
			return frequencies[index];
		} else {
			return 0;
		}
	}

	public void setFrequencies(int frequency, int index) {
		if (index >= 0 && index < frequencies.length) {
			frequencies[index] = frequency;
		}
	}

	public void getPositionYaw(WorldAccess world, float worldTime) {
		getPositionYaw(world, worldTime, 1, SIMULATE_RADIUS, null, null, null);
	}

	public void getPositionYaw(WorldAccess world, float worldTime, float lastFrameDuration, int simulateRadius, EntitySeat entitySeat, PositionYawCallback positionYawCallback, RenderConnectionCallback renderConnectionCallback) {
		schedule.forEach((scheduleTime, trainType) -> {
			final float worldTimeOffset = worldTime + 6000 + TICKS_PER_DAY;
			final List<Pos3f> positions = getPositions((worldTimeOffset - scheduleTime) % TICKS_PER_DAY, trainType.getSpacing());
			final List<Pos3f> futurePositions = getPositions((worldTimeOffset - scheduleTime + 1) % TICKS_PER_DAY, trainType.getSpacing());
			final float doorValue = getDoorValue((worldTimeOffset - scheduleTime) % TICKS_PER_DAY);

			float prevCarX = 0, prevCarY = 0, prevCarZ = 0, prevCarYaw = 0, prevCarPitch = 0;
			int previousRendered = 0;

			for (int i = 0; i < positions.size() - 1; i++) {
				final Pos3f pos1 = positions.get(i);
				final Pos3f pos2 = positions.get(i + 1);

				final Pos3f futurePos1;
				final Pos3f futurePos2;
				if (i + 1 >= futurePositions.size() || futurePositions.get(i) == null || futurePositions.get(i + 1) == null) {
					futurePos1 = pos1;
					futurePos2 = pos2;
				} else {
					futurePos1 = futurePositions.get(i);
					futurePos2 = futurePositions.get(i + 1);
				}

				if (pos1 != null && pos2 != null) {
					final float x = getAverage(pos1.x, pos2.x);
					final float y = getAverage(pos1.y, pos2.y) + 1;
					final float z = getAverage(pos1.z, pos2.z);

					final PlayerEntity player = world.isClient() && entitySeat != null ? entitySeat.getPlayer() : world.getClosestPlayer(x, y, z, simulateRadius, entity -> true);
					if (player != null) {
						final float realSpacing = pos2.getDistanceTo(pos1);
						final float halfSpacing = realSpacing / 2;
						final float yaw = (float) MathHelper.atan2(pos2.x - pos1.x, pos2.z - pos1.z);
						final double playerDistance = player.getPos().distanceTo(new Vec3d(x, y, z));

						final boolean doorLeftOpen = openDoors(world, x, y, z, (float) Math.PI + yaw, halfSpacing, playerDistance < simulateRadius / 2F ? doorValue : 0) && doorValue > 0;
						final boolean doorRightOpen = openDoors(world, x, y, z, yaw, halfSpacing, playerDistance < simulateRadius / 2F ? doorValue : 0) && doorValue > 0;

						if (world.isClient() && playerDistance < simulateRadius) {
							final float pitch = (float) Math.asin((pos2.y - pos1.y) / realSpacing);
							final float halfWidth = trainType.width / 2F;
							final boolean isEnd1Head = i == 0;
							final boolean isEnd2Head = i == positions.size() - 2;

							if (!player.isSpectator() && entitySeat != null) {
								final Vec3d positionRotated = new Vec3d(player.getX() - x, player.getY() - y, player.getZ() - z).rotateY(-yaw).rotateX(-pitch);
								if (Math.abs(positionRotated.x) <= halfWidth + INNER_PADDING && Math.abs(positionRotated.y) <= 1.5 && Math.abs(positionRotated.z) <= halfSpacing + INNER_PADDING) {
									if (doorLeftOpen || doorRightOpen) {
										entitySeat.resetSeatCoolDown();
									}

									if (entitySeat.getIsRiding()) {
										final float futureX = getAverage(futurePos1.x, futurePos2.x);
										final float futureY = getAverage(futurePos1.y, futurePos2.y) + 1;
										final float futureZ = getAverage(futurePos1.z, futurePos2.z);
										final float futureYaw = (float) MathHelper.atan2(futurePos2.x - futurePos1.x, futurePos2.z - futurePos1.z);
										final float futurePitch = (float) Math.asin((futurePos2.y - futurePos1.y) / futurePos2.getDistanceTo(futurePos1));

										Vec3d velocity = positionRotated.add(new Vec3d(player.sidewaysSpeed / 3, 0, player.forwardSpeed / 3).rotateY((float) -Math.toRadians(player.yaw) - yaw));

										final double xClamp = MathHelper.clamp(velocity.x, doorLeftOpen ? velocity.x : -halfWidth, doorRightOpen ? velocity.x : halfWidth);
										final double zClamp = MathHelper.clamp(velocity.z, isEnd1Head ? -halfSpacing : velocity.z, isEnd2Head ? halfSpacing : velocity.z);

										velocity = new Vec3d(xClamp, 0, zClamp);
										velocity = velocity.rotateX(futurePitch).rotateY(futureYaw).add(futureX, futureY, futureZ).subtract(player.getPos());

										player.setVelocity(velocity);
										player.yaw += (float) MathHelper.wrapDegrees(Math.toDegrees(yaw - futureYaw)) * lastFrameDuration;
										player.fallDistance = 0;

										entitySeat.resetSeatCoolDown();
									}
								}
							}

							if (positionYawCallback != null) {
								positionYawCallback.positionYawCallback(x, y, z, (float) Math.toDegrees(yaw), (float) Math.toDegrees(pitch), trainType, isEnd1Head, isEnd2Head, doorLeftOpen ? doorValue : 0, doorRightOpen ? doorValue : 0);
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

								renderConnectionCallback.renderConnectionCallback(prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, pos1.x, pos1.y, pos1.z, trainType);
							}

							prevCarX = x;
							prevCarY = y;
							prevCarZ = z;
							prevCarYaw = yaw;
							prevCarPitch = pitch;
							previousRendered = 2;
						}
					}
				}
			}
		});
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

					schedule.put(i, trainType);
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

	@FunctionalInterface
	public interface PositionYawCallback {
		void positionYawCallback(float x, float y, float z, float yaw, float pitch, TrainType trainType, boolean isEnd1Head, boolean isEnd2Head, float doorLeftValue, float doorRightValue);
	}

	@FunctionalInterface
	public interface RenderConnectionCallback {
		void renderConnectionCallback(Pos3f prevPos1, Pos3f prevPos2, Pos3f prevPos3, Pos3f prevPos4, Pos3f thisPos1, Pos3f thisPos2, Pos3f thisPos3, Pos3f thisPos4, float x, float y, float z, TrainType trainType);
	}
}
