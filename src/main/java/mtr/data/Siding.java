package mtr.data;

import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPlatform;
import mtr.config.CustomResources;
import mtr.gui.IGui;
import mtr.packet.IPacket;
import mtr.path.PathData;
import mtr.path.PathData2;
import mtr.path.PathFinder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Siding extends SavedRailBase implements IGui, IPacket {

	private Depot depot;
	private CustomResources.TrainMapping trainTypeMapping;
	private int trainLength;

	private float speed;
	private float railProgress;
	private float stopCounter;
	private int nextStoppingIndex;
	private long lastNanos;
	private boolean isOnRoute = false;

	private final float railLength;
	private final List<PathData2> path = new ArrayList<>();
	private final List<Float> distances = new ArrayList<>();

	private static final String KEY_RAIL_LENGTH = "rail_length";
	private static final String KEY_TRAIN_TYPE = "train_type";
	private static final String KEY_TRAIN_CUSTOM_ID = "train_custom_id";
	private static final String KEY_SPEED = "speed";
	private static final String KEY_RAIL_PROGRESS = "rail_progress";
	private static final String KEY_STOP_COUNTER = "stop_counter";
	private static final String KEY_NEXT_STOPPING_INDEX = "next_stopping_index";
	private static final String KEY_IS_ON_ROUTE = "is_on_route";

	private static final int TICK_DURATION_NANOS = 50000000;
	private static final float ACCELERATION = 0.01F;
	private static final int DOOR_DELAY = 20;
	private static final int DOOR_MOVE_TIME = 64;
	private static final float CONNECTION_HEIGHT = 2.25F;
	private static final float CONNECTION_Z_OFFSET = 0.5F;
	private static final float CONNECTION_X_OFFSET = 0.25F;

	public Siding(BlockPos pos1, BlockPos pos2, int railLength) {
		super(pos1, pos2);
		this.railLength = railLength;
		setTrainDetails("", TrainType.values()[0]);
		lastNanos = System.nanoTime();
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

		speed = tag.getFloat(KEY_SPEED);
		railProgress = tag.getFloat(KEY_RAIL_PROGRESS);
		stopCounter = tag.getFloat(KEY_STOP_COUNTER);
		nextStoppingIndex = tag.getInt(KEY_NEXT_STOPPING_INDEX);
		isOnRoute = tag.getBoolean(KEY_IS_ON_ROUTE);

		lastNanos = System.nanoTime();
	}

	public Siding(PacketByteBuf packet) {
		super(packet);
		railLength = packet.readFloat();
		setTrainDetails(packet.readString(PACKET_STRING_READ_LENGTH), TrainType.values()[packet.readInt()]);

		speed = packet.readFloat();
		railProgress = packet.readFloat();
		stopCounter = packet.readFloat();
		nextStoppingIndex = packet.readInt();
		isOnRoute = packet.readBoolean();

		lastNanos = System.nanoTime();
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = super.toCompoundTag();
		tag.putFloat(KEY_RAIL_LENGTH, railLength);
		tag.putString(KEY_TRAIN_CUSTOM_ID, trainTypeMapping.customId);
		tag.putString(KEY_TRAIN_TYPE, trainTypeMapping.trainType.toString());

		tag.putFloat(KEY_SPEED, speed);
		tag.putFloat(KEY_RAIL_PROGRESS, railProgress);
		tag.putFloat(KEY_STOP_COUNTER, stopCounter);
		tag.putInt(KEY_NEXT_STOPPING_INDEX, nextStoppingIndex);
		tag.putBoolean(KEY_IS_ON_ROUTE, isOnRoute);

		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeFloat(railLength);
		packet.writeString(trainTypeMapping.customId);
		packet.writeInt(trainTypeMapping.trainType.ordinal());

		packet.writeFloat(speed);
		packet.writeFloat(railProgress);
		packet.writeFloat(stopCounter);
		packet.writeInt(nextStoppingIndex);
		packet.writeBoolean(isOnRoute);
	}

	@Override
	public void update(String key, PacketByteBuf packet) {
		switch (key) {
			case KEY_TRAIN_TYPE:
				setTrainDetails(packet.readString(PACKET_STRING_READ_LENGTH), TrainType.values()[packet.readInt()]);
				break;
			case KEY_RAIL_PROGRESS:
				speed = packet.readFloat();
				float tempRailProgress = packet.readFloat();
				if (Math.abs(tempRailProgress - railProgress) >= 1) {
					railProgress = tempRailProgress;
				}
				stopCounter = packet.readFloat();
				nextStoppingIndex = packet.readInt();
				isOnRoute = packet.readBoolean();
				break;
			default:
				super.update(key, packet);
				break;
		}
	}

	public void generateRoute(Map<BlockPos, Map<BlockPos, Rail>> rails, Set<Platform> platforms, Set<Route> routes, Set<Depot> depots) {
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
						if (platform != null) {
							platformsInRoute.add(platform);
						}
					});
				}
			});
		}
		platformsInRoute.add(this);

		path.addAll(PathFinder.findPath(rails, platformsInRoute));
		if (path.isEmpty()) {
			final List<BlockPos> orderedPositions = getOrderedPositions(new BlockPos(0, 0, 0), false);
			final BlockPos pos1 = orderedPositions.get(0);
			final BlockPos pos2 = orderedPositions.get(1);
			if (RailwayData.containsRail(rails, pos1, pos2)) {
				path.add(new PathData2(rails.get(pos1).get(pos2), 0, pos1, pos2));
			}
		}

		distances.clear();
		float sum = 0;
		for (final PathData2 pathData : path) {
			sum += pathData.rail.getLength();
			distances.add(sum);
		}
	}

	public void simulateTrain(WorldAccess world, RenderTrainCallback renderTrainCallback, RenderConnectionCallback renderConnectionCallback, Runnable generateRoute) {
		try {
			final long currentNanos = System.nanoTime();
			final float doorValue;

			if (!isOnRoute) {
				railProgress = (railLength + trainLength * getTrainSpacing()) / 2;
				doorValue = 0;

				if (path.size() > 1 && depot != null && depot.deployTrain(world)) {
					generateRoute.run();
					if (!world.isClient()) {
						isOnRoute = true;
						nextStoppingIndex = 0;
						startUp();
					}
					syncTrainToClient(world);
				}
			} else {
				doorValue = moveTrain(world, world.isClient() ? (float) (currentNanos - lastNanos) / TICK_DURATION_NANOS : 1);
			}

			if (!path.isEmpty()) {
				render(world, doorValue, renderTrainCallback, renderConnectionCallback);
			}
			lastNanos = currentNanos;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private float moveTrain(WorldAccess world, float ticksElapsed) {
		final float newAcceleration = ACCELERATION * ticksElapsed;
		if (path.isEmpty()) {
			return 0;
		}

		if (railProgress >= distances.get(distances.size() - 1) - (railLength - trainLength * getTrainSpacing()) / 2) {
			isOnRoute = false;
			syncTrainToClient(world);
			return 0;
		} else {
			final float doorValue;

			if (speed <= 0) {
				speed = 0;
				final float stopCounterOld = stopCounter;
				stopCounter += ticksElapsed;

				final int dwellTicks = path.get(nextStoppingIndex).dwellTime * 10;
				doorValue = (stopCounter < dwellTicks / 2F ? 1 : -1) * getDoorValue(dwellTicks, stopCounter);

				if (stopCounterOld < dwellTicks / 2F && stopCounter >= dwellTicks / 2F) {
					syncTrainToClient(world);
				}

				if (stopCounter > dwellTicks) {
					startUp();
				}
			} else {
				if (distances.get(nextStoppingIndex) - railProgress < 0.5 * speed * speed / ACCELERATION) {
					speed -= newAcceleration;
				} else {
					final int headIndex = getIndex(0);
					final float railSpeed = path.get(headIndex).rail.railType.maxBlocksPerTick;
					if (speed < railSpeed && (speed < RailType.WOODEN.maxBlocksPerTick || headIndex < nextStoppingIndex)) {
						speed += newAcceleration;
					} else if (speed > railSpeed) {
						speed -= newAcceleration;
					}
				}

				doorValue = 0;
			}

			railProgress += speed * ticksElapsed;
			return doorValue;
		}
	}

	private void startUp() {
		stopCounter = 0;
		speed = ACCELERATION;
		if (path.size() > nextStoppingIndex + 1 && path.get(nextStoppingIndex).isOppositeRail(path.get(nextStoppingIndex + 1))) {
			railProgress += trainLength * getTrainSpacing();
		}
		nextStoppingIndex = getNextStoppingIndex();
	}

	private void render(WorldAccess world, float doorValueRaw, RenderTrainCallback renderTrainCallback, RenderConnectionCallback renderConnectionCallback) {
		final Pos3f[] positions = new Pos3f[trainLength + 1];
		for (int i = 0; i < trainLength + 1; i++) {
			positions[i] = getRoutePosition(i);
		}

		final float doorValue = Math.abs(doorValueRaw);
		final boolean opening = doorValueRaw > 0;

		float prevCarX = 0, prevCarY = 0, prevCarZ = 0, prevCarYaw = 0, prevCarPitch = 0;
		int previousRendered = 0;
		for (int i = 0; i < trainLength; i++) {
			final Pos3f pos1 = positions[i];
			final Pos3f pos2 = positions[i + 1];

			if (pos1 != null && pos2 != null) {
				final float x = getAverage(pos1.x, pos2.x);
				final float y = getAverage(pos1.y, pos2.y) + 1;
				final float z = getAverage(pos1.z, pos2.z);

				final TrainType trainType = trainTypeMapping.trainType;
				final float realSpacing = pos2.getDistanceTo(pos1);
				final float halfSpacing = realSpacing / 2;
				final float halfWidth = trainType.width / 2F;
				final float yaw = (float) MathHelper.atan2(pos2.x - pos1.x, pos2.z - pos1.z);
				final float pitch = realSpacing == 0 ? 0 : (float) Math.asin((pos2.y - pos1.y) / realSpacing);

				final boolean doorLeftOpen = openDoors(world, x, y, z, (float) Math.PI + yaw, halfSpacing, doorValue) && doorValue > 0;
				final boolean doorRightOpen = openDoors(world, x, y, z, yaw, halfSpacing, doorValue) && doorValue > 0;

				if (renderTrainCallback != null) {
					renderTrainCallback.renderTrainCallback(x, y, z, yaw, pitch, "", trainType, i == 0, i == trainLength - 1, doorLeftOpen ? doorValue : 0, doorRightOpen ? doorValue : 0, opening, isOnRoute, false);
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

					renderConnectionCallback.renderConnectionCallback(prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, x, y, z, trainType, isOnRoute, false);
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

	private float getRailProgress(int car) {
		return railProgress - car * getTrainSpacing();
	}

	private int getIndex(int car) {
		for (int i = 0; i < path.size(); i++) {
			if (getRailProgress(car) < distances.get(i)) {
				return i;
			}
		}
		return path.size() - 1;
	}

	private Pos3f getRoutePosition(int car) {
		final int index = getIndex(car);
		return path.get(index).rail.getPosition(getRailProgress(car) - (index == 0 ? 0 : distances.get(index - 1)));
	}

	private int getNextStoppingIndex() {
		final int headIndex = getIndex(0);
		for (int i = headIndex; i < path.size(); i++) {
			if (path.get(i).dwellTime > 0) {
				return i;
			}
		}
		return path.size() - 1;
	}

	private int getTrainSpacing() {
		return trainTypeMapping.trainType.getSpacing();
	}

	private void syncTrainToClient(WorldAccess world) {
		if (!world.isClient()) {
			final PacketByteBuf packet = PacketByteBufs.create();
			packet.writeLong(id);
			packet.writeString(KEY_RAIL_PROGRESS);
			packet.writeFloat(speed);
			packet.writeFloat(railProgress);
			packet.writeFloat(stopCounter);
			packet.writeInt(nextStoppingIndex);
			packet.writeBoolean(isOnRoute);
			world.getPlayers().forEach(player -> ServerPlayNetworking.send((ServerPlayerEntity) player, PACKET_UPDATE_SIDING, packet));
		}
	}

	private void setTrainDetails(String customId, TrainType trainType) {
		trainTypeMapping = new CustomResources.TrainMapping(customId, trainType);
		trainLength = (int) Math.floor(railLength / getTrainSpacing());
	}

	private static float getDoorValue(int dwellTicks, float value) {
		final float maxDoorMoveTime = Math.min(DOOR_MOVE_TIME, dwellTicks / 2 - DOOR_DELAY);
		final float stage1 = DOOR_DELAY;
		final float stage2 = DOOR_DELAY + maxDoorMoveTime;
		final float stage3 = dwellTicks - DOOR_DELAY - maxDoorMoveTime;
		final float stage4 = dwellTicks - DOOR_DELAY;
		if (value < stage1 || value >= stage4) {
			return 0;
		} else if (value >= stage2 && value < stage3) {
			return 1;
		} else if (value >= stage1 && value < stage2) {
			return (value - stage1) / DOOR_MOVE_TIME;
		} else if (value >= stage3 && value < stage4) {
			return (stage4 - value) / DOOR_MOVE_TIME;
		} else {
			return 0;
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
									((World) world).setBlockState(checkPos.up(i), state.with(BlockPSDAPGDoorBase.OPEN, (int) MathHelper.clamp(doorValue * PathData.DOOR_MOVE_TIME, 0, BlockPSDAPGDoorBase.MAX_OPEN_VALUE)));
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
	public interface RenderTrainCallback {
		void renderTrainCallback(float x, float y, float z, float yaw, float pitch, String customId, TrainType trainType, boolean isEnd1Head, boolean isEnd2Head, float doorLeftValue, float doorRightValue, boolean opening, boolean lightsOn, boolean shouldOffsetRender);
	}

	@FunctionalInterface
	public interface RenderConnectionCallback {
		void renderConnectionCallback(Pos3f prevPos1, Pos3f prevPos2, Pos3f prevPos3, Pos3f prevPos4, Pos3f thisPos1, Pos3f thisPos2, Pos3f thisPos3, Pos3f thisPos4, float x, float y, float z, TrainType trainType, boolean lightsOn, boolean shouldOffsetRender);
	}
}
