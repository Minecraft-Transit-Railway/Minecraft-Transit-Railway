package mtr.data;

import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPlatform;
import mtr.config.CustomResources;
import mtr.entity.EntitySeat;
import mtr.gui.IGui;
import mtr.packet.IPacket;
import mtr.path.PathData;
import mtr.path.PathFinder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Siding extends SavedRailBase implements IGui, IPacket {

	private World world;
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
	private final List<PathData> path = new ArrayList<>();
	private final List<Float> distances = new ArrayList<>();

	private static final String KEY_RAIL_LENGTH = "rail_length";
	private static final String KEY_TRAIN_TYPE = "train_type";
	private static final String KEY_TRAIN_CUSTOM_ID = "train_custom_id";
	private static final String KEY_SPEED = "speed";
	private static final String KEY_RAIL_PROGRESS = "rail_progress";
	private static final String KEY_STOP_COUNTER = "stop_counter";
	private static final String KEY_NEXT_STOPPING_INDEX = "next_stopping_index";
	private static final String KEY_IS_ON_ROUTE = "is_on_route";

	public static final float ACCELERATION = 0.01F;
	private static final int TICK_DURATION_NANOS = 50000000;
	private static final int DOOR_DELAY = 20;
	private static final int DOOR_MOVE_TIME = 64;

	private static final float INNER_PADDING = 0.5F;
	private static final int BOX_PADDING = 3;

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
				if (Math.abs(tempRailProgress - railProgress) > 0.5) {
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

	public void generateRoute(World world, Map<BlockPos, Map<BlockPos, Rail>> rails, Set<Platform> platforms, Set<Route> routes, Set<Depot> depots) {
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
				path.add(new PathData(rails.get(pos1).get(pos2), 0, pos1, pos2, -1));
			}
		}

		distances.clear();
		float sum = 0;
		for (final PathData pathData : path) {
			sum += pathData.rail.getLength();
			distances.add(sum);
		}
	}

	public void simulateTrain(float tickDelta, EntitySeat clientSeat, RenderTrainCallback renderTrainCallback, RenderConnectionCallback renderConnectionCallback, SpeedCallback speedCallback, AnnouncementCallback announcementCallback, Runnable generateRoute) {
		if (world == null) {
			return;
		}

		try {
			final long currentNanos = System.nanoTime();
			final float oldRailProgress = railProgress;
			final float oldSpeed = speed;
			final float oldDoorValue;
			final float doorValueRaw;
			final boolean reversed;

			if (!isOnRoute) {
				railProgress = (railLength + trainLength * getTrainSpacing()) / 2;
				oldDoorValue = 0;
				doorValueRaw = 0;

				if (path.size() > 1 && depot != null && depot.deployTrain(world)) {
					generateRoute.run();
					if (!world.isClient()) {
						isOnRoute = true;
						nextStoppingIndex = 0;
						reversed = startUp();
					} else {
						reversed = false;
					}
					syncTrainToClient();
				} else {
					reversed = false;
				}
			} else {
				oldDoorValue = Math.abs(getDoorValue());

				final float ticksElapsed = world.isClient() ? (float) (currentNanos - lastNanos) / TICK_DURATION_NANOS : 1;
				final float newAcceleration = ACCELERATION * ticksElapsed;
				if (path.isEmpty()) {
					doorValueRaw = 0;
					reversed = false;
				} else {
					if (railProgress >= distances.get(distances.size() - 1) - (railLength - trainLength * getTrainSpacing()) / 2) {
						isOnRoute = false;
						syncTrainToClient();
						doorValueRaw = 0;
						reversed = false;
					} else {
						if (speed <= 0) {
							speed = 0;
							final float stopCounterOld = stopCounter;
							stopCounter += ticksElapsed;
							doorValueRaw = getDoorValue();

							final int dwellTicks = path.get(nextStoppingIndex).dwellTime * 10;

							if (stopCounterOld < dwellTicks / 2F && stopCounter >= dwellTicks / 2F) {
								syncTrainToClient();
							}

							reversed = stopCounter > dwellTicks && startUp();
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

							doorValueRaw = 0;
							reversed = false;
						}

						railProgress += speed * ticksElapsed;
					}
				}
			}

			if (!path.isEmpty()) {
				render(doorValueRaw, oldRailProgress, oldSpeed, oldDoorValue, tickDelta, reversed, clientSeat, renderTrainCallback, renderConnectionCallback, speedCallback, announcementCallback);
			}
			lastNanos = currentNanos;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean startUp() {
		stopCounter = 0;
		speed = ACCELERATION;
		final boolean reversed = path.size() > nextStoppingIndex + 1 && path.get(nextStoppingIndex).isOppositeRail(path.get(nextStoppingIndex + 1));
		if (reversed) {
			railProgress += trainLength * getTrainSpacing();
		}
		nextStoppingIndex = getNextStoppingIndex();
		return reversed;
	}

	private void render(float doorValueRaw, float oldRailProgress, float oldSpeed, float oldDoorValue, float tickDelta, boolean reversed, EntitySeat clientSeat, RenderTrainCallback renderTrainCallback, RenderConnectionCallback renderConnectionCallback, SpeedCallback speedCallback, AnnouncementCallback announcementCallback) {
		final Pos3f[] positions = new Pos3f[trainLength + 1];
		for (int i = 0; i < trainLength + 1; i++) {
			positions[i] = getRoutePosition(i);
		}
		final TrainType trainType = trainTypeMapping.trainType;

		float renderOffsetX = 0, renderOffsetY = 0, renderOffsetZ = 0;
		boolean shouldOffsetRender = false;
		if (world.isClient() && clientSeat != null && clientSeat.hasPassengers() && clientSeat.isSidingId(id)) {
			final float ridingPercentageZ = clientSeat.getRidingPercentageZ(tickDelta);
			final int ridingCar = (int) Math.floor(ridingPercentageZ);
			if (ridingCar < trainLength) {
				final Pos3f pos1 = positions[ridingCar];
				final Pos3f pos2 = positions[ridingCar + 1];
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
						speedCallback.speedCallback(speed * 20, (int) absoluteX, (int) absoluteY, (int) absoluteZ, path.get(getIndex(0)).stopIndex - 1, depot.routeIds);
					}

					if (announcementCallback != null) {
						float targetProgress = distances.get(getPreviousStoppingIndex()) + trainLength;
						if (oldRailProgress < targetProgress && railProgress >= targetProgress) {
							announcementCallback.announcementCallback(path.get(getIndex(0)).stopIndex - 1, depot.routeIds);
						}
					}
				}
			}
		}

		final float doorValue = Math.abs(doorValueRaw);
		final boolean opening = doorValueRaw > 0;

		float prevCarX = 0, prevCarY = 0, prevCarZ = 0, prevCarYaw = 0, prevCarPitch = 0;
		int previousRendered = 0;
		for (int i = 0; i < trainLength; i++) {
			final Pos3f pos1 = positions[i];
			final Pos3f pos2 = positions[i + 1];

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
				final float pitch = realSpacing == 0 ? 0 : (float) Math.asin((pos2.y - pos1.y) / realSpacing);

				if (world.isClient()) {
					final boolean doorLeftOpen = openDoors(absoluteX, absoluteY, absoluteZ, (float) Math.PI + yaw, halfSpacing, doorValue) && doorValue > 0;
					final boolean doorRightOpen = openDoors(absoluteX, absoluteY, absoluteZ, yaw, halfSpacing, doorValue) && doorValue > 0;

					if (renderTrainCallback != null) {
						renderTrainCallback.renderTrainCallback(x, y, z, yaw, pitch, trainTypeMapping.customId, trainType, i == 0, i == trainLength - 1, doorLeftOpen ? doorValue : 0, doorRightOpen ? doorValue : 0, opening, isOnRoute, shouldOffsetRender);
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

						renderConnectionCallback.renderConnectionCallback(prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, x, y, z, trainType, isOnRoute, shouldOffsetRender);
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
						final boolean doorLeftOpen = openDoors(x, y, z, (float) Math.PI + yaw, halfSpacing, doorValue) && doorValue > 0;
						final boolean doorRightOpen = openDoors(x, y, z, yaw, halfSpacing, doorValue) && doorValue > 0;

						final int ridingCar = i;
						final float margin = halfSpacing + BOX_PADDING + speed * 4;
						world.getEntitiesByClass(EntitySeat.class, new Box(x + margin, y + margin, z + margin, x - margin, y - margin, z - margin), entitySeat -> true).forEach(entitySeat -> {
							final PlayerEntity serverPlayer = entitySeat.getPlayer();
							if (serverPlayer == null) {
								return;
							}
							final Vec3d positionRotated = entitySeat.getPos().subtract(x, y, z).rotateY(-yaw).rotateX(-pitch);

							// TODO player falls off when train is reversing
							if (Math.abs(positionRotated.x) <= halfWidth + INNER_PADDING && Math.abs(positionRotated.y) <= 1.5) {
								if ((doorLeftOpen || doorRightOpen) && !entitySeat.getIsRiding() && Math.abs(positionRotated.z) <= halfSpacing) {
									entitySeat.resetSeatCoolDown();
									serverPlayer.startRiding(entitySeat);
									entitySeat.setSidingId(id);
									entitySeat.ridingPercentageX = (float) (positionRotated.x / trainType.width + 0.5);
									entitySeat.ridingPercentageZ = (float) (positionRotated.z / realSpacing + 0.5) + ridingCar;
								}

								if (entitySeat.getIsRiding() && ridingCar == Math.floor(entitySeat.ridingPercentageZ) && entitySeat.isSidingId(id)) {
									final Vec3d velocity = new Vec3d(getValueFromPercentage(entitySeat.ridingPercentageX, trainType.width), 0, getValueFromPercentage(MathHelper.fractionalPart(entitySeat.ridingPercentageZ), realSpacing)).rotateX(pitch).rotateY(yaw).add(x, y, z);
									entitySeat.updatePositionAndAngles(velocity.x, velocity.y, velocity.z, 0, 0);
									entitySeat.fallDistance = 0;
									entitySeat.resetSeatCoolDown();

									final Vec3d movement = new Vec3d(serverPlayer.sidewaysSpeed / 3, 0, serverPlayer.forwardSpeed / 3).rotateY((float) -Math.toRadians(serverPlayer.yaw) - yaw);
									entitySeat.ridingPercentageX += movement.x / trainType.width;
									entitySeat.ridingPercentageZ += movement.z / realSpacing;
									entitySeat.ridingPercentageX = MathHelper.clamp(entitySeat.ridingPercentageX, doorLeftOpen ? -1 : 0, doorRightOpen ? 2 : 1);
									entitySeat.ridingPercentageZ = MathHelper.clamp(entitySeat.ridingPercentageZ, 0, trainLength - 0.01F);
									entitySeat.updateRidingPercentage();
									entitySeat.setSidingId(id);
								}
							}
						});

						final BlockPos soundPos = new BlockPos(x, y, z);
						trainType.playSpeedSoundEffect(world, soundPos, oldSpeed, speed);

						if (oldDoorValue <= 0 && doorValue > 0 && trainType.doorOpenSoundEvent != null) {
							world.playSound(null, soundPos, trainType.doorOpenSoundEvent, SoundCategory.BLOCKS, 1, 1);
						} else if (oldDoorValue >= trainType.doorCloseSoundTime && doorValue < trainType.doorCloseSoundTime && trainType.doorCloseSoundEvent != null) {
							world.playSound(null, soundPos, trainType.doorCloseSoundEvent, SoundCategory.BLOCKS, 1, 1);
						}
					}
				}
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

	private int getPreviousStoppingIndex() {
		final int headIndex = getIndex(0);
		for (int i = headIndex; i >= 0; i--) {
			if (path.get(i).dwellTime > 0) {
				return i;
			}
		}
		return 0;
	}

	private int getTrainSpacing() {
		return trainTypeMapping.trainType.getSpacing();
	}

	private void syncTrainToClient() {
		if (world != null && !world.isClient()) {
			final PacketByteBuf packet = PacketByteBufs.create();
			packet.writeLong(id);
			packet.writeString(KEY_RAIL_PROGRESS);
			packet.writeFloat(speed);
			packet.writeFloat(railProgress);
			packet.writeFloat(stopCounter);
			packet.writeInt(nextStoppingIndex);
			packet.writeBoolean(isOnRoute);
			final RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				railwayData.markDirty();
			}
			world.getPlayers().forEach(player -> ServerPlayNetworking.send((ServerPlayerEntity) player, PACKET_UPDATE_SIDING, packet));
		}
	}

	private void setTrainDetails(String customId, TrainType trainType) {
		trainTypeMapping = new CustomResources.TrainMapping(customId, trainType);
		trainLength = (int) Math.floor(railLength / getTrainSpacing());
	}

	private float getDoorValue() {
		final int dwellTicks = path.get(nextStoppingIndex).dwellTime * 10;
		final float maxDoorMoveTime = Math.min(DOOR_MOVE_TIME, dwellTicks / 2 - DOOR_DELAY);
		final float stage1 = DOOR_DELAY;
		final float stage2 = DOOR_DELAY + maxDoorMoveTime;
		final float stage3 = dwellTicks - DOOR_DELAY - maxDoorMoveTime;
		final float stage4 = dwellTicks - DOOR_DELAY;
		if (stopCounter < stage1 || stopCounter >= stage4) {
			return 0;
		} else if (stopCounter >= stage2 && stopCounter < stage3) {
			return 1;
		} else if (stopCounter >= stage1 && stopCounter < stage2) {
			return (stopCounter - stage1) / DOOR_MOVE_TIME;
		} else if (stopCounter >= stage3 && stopCounter < stage4) {
			return -(stage4 - stopCounter) / DOOR_MOVE_TIME;
		} else {
			return 0;
		}
	}

	private boolean openDoors(float trainX, float trainY, float trainZ, float checkYaw, float halfSpacing, float doorValue) {
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
									world.setBlockState(checkPos.up(i), state.with(BlockPSDAPGDoorBase.OPEN, (int) MathHelper.clamp(doorValue * DOOR_MOVE_TIME, 0, BlockPSDAPGDoorBase.MAX_OPEN_VALUE)));
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

	public static float getAverage(float a, float b) {
		return (a + b) / 2;
	}

	public static float getValueFromPercentage(float percentage, float total) {
		return (percentage - 0.5F) * total;
	}

	@FunctionalInterface
	public interface RenderTrainCallback {
		void renderTrainCallback(float x, float y, float z, float yaw, float pitch, String customId, TrainType trainType, boolean isEnd1Head, boolean isEnd2Head, float doorLeftValue, float doorRightValue, boolean opening, boolean lightsOn, boolean shouldOffsetRender);
	}

	@FunctionalInterface
	public interface RenderConnectionCallback {
		void renderConnectionCallback(Pos3f prevPos1, Pos3f prevPos2, Pos3f prevPos3, Pos3f prevPos4, Pos3f thisPos1, Pos3f thisPos2, Pos3f thisPos3, Pos3f thisPos4, float x, float y, float z, TrainType trainType, boolean lightsOn, boolean shouldOffsetRender);
	}

	@FunctionalInterface
	public interface SpeedCallback {
		void speedCallback(float speed, int x, int y, int z, int stopIndex, List<Long> routeIds);
	}

	@FunctionalInterface
	public interface AnnouncementCallback {
		void announcementCallback(int stopIndex, List<Long> routeIds);
	}
}
