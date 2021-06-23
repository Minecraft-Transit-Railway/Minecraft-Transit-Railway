package mtr.data;

import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPlatform;
import mtr.config.CustomResources;
import mtr.mixin.PlayerTeleportationStateAccessor;
import mtr.packet.IPacket;
import mtr.path.PathData;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

public class Train extends NameColorDataBase implements IPacket, IGui {

	private float speed;
	private float railProgress;
	private float stopCounter;
	private int nextStoppingIndex;
	private boolean reversed;
	private boolean isOnRoute = false;

	private float clientPercentageX;
	private float clientPercentageZ;

	private final long sidingId;
	private final float railLength;
	private final List<PathData> path;
	private final List<Float> distances;
	private final Set<UUID> ridingEntities = new HashSet<>();

	public static final float ACCELERATION = 0.01F;

	private static final String KEY_SPEED = "speed";
	private static final String KEY_RAIL_PROGRESS = "rail_progress";
	private static final String KEY_STOP_COUNTER = "stop_counter";
	private static final String KEY_NEXT_STOPPING_INDEX = "next_stopping_index";
	private static final String KEY_REVERSED = "reversed";
	private static final String KEY_IS_ON_ROUTE = "is_on_route";
	private static final String KEY_RIDING_ENTITIES = "riding_entities";

	private static final int DOOR_DELAY = 20;
	private static final int DOOR_MOVE_TIME = 64;

	private static final float INNER_PADDING = 0.5F;
	private static final int BOX_PADDING = 3;

	private static final float CONNECTION_HEIGHT = 2.25F;
	private static final float CONNECTION_Z_OFFSET = 0.5F;
	private static final float CONNECTION_X_OFFSET = 0.25F;

	public Train(long id, long sidingId, float railLength, List<PathData> path, List<Float> distances) {
		super(id);
		this.sidingId = sidingId;
		this.railLength = railLength;
		this.path = path;
		this.distances = distances;
	}

	public Train(long sidingId, float railLength, List<PathData> path, List<Float> distances, NbtCompound nbtCompound) {
		super(nbtCompound);

		this.sidingId = sidingId;
		this.railLength = railLength;
		this.path = path;
		this.distances = distances;

		speed = nbtCompound.getFloat(KEY_SPEED);
		railProgress = nbtCompound.getFloat(KEY_RAIL_PROGRESS);
		stopCounter = nbtCompound.getFloat(KEY_STOP_COUNTER);
		nextStoppingIndex = nbtCompound.getInt(KEY_NEXT_STOPPING_INDEX);
		reversed = nbtCompound.getBoolean(KEY_REVERSED);
		isOnRoute = nbtCompound.getBoolean(KEY_IS_ON_ROUTE);
		final NbtCompound tagRidingEntities = nbtCompound.getCompound(KEY_RIDING_ENTITIES);
		tagRidingEntities.getKeys().forEach(key -> ridingEntities.add(tagRidingEntities.getUuid(key)));
	}

	public Train(long sidingId, float railLength, List<PathData> path, List<Float> distances, PacketByteBuf packet) {
		super(packet);

		this.sidingId = sidingId;
		this.railLength = railLength;
		this.path = path;
		this.distances = distances;

		speed = packet.readFloat();
		railProgress = packet.readFloat();
		stopCounter = packet.readFloat();
		nextStoppingIndex = packet.readInt();
		reversed = packet.readBoolean();
		isOnRoute = packet.readBoolean();

		final int ridingEntitiesCount = packet.readInt();
		for (int i = 0; i < ridingEntitiesCount; i++) {
			ridingEntities.add(packet.readUuid());
		}
	}

	@Override
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = super.toCompoundTag();

		nbtCompound.putFloat(KEY_SPEED, speed);
		nbtCompound.putFloat(KEY_RAIL_PROGRESS, railProgress);
		nbtCompound.putFloat(KEY_STOP_COUNTER, stopCounter);
		nbtCompound.putInt(KEY_NEXT_STOPPING_INDEX, nextStoppingIndex);
		nbtCompound.putBoolean(KEY_REVERSED, reversed);
		nbtCompound.putBoolean(KEY_IS_ON_ROUTE, isOnRoute);

		final NbtCompound tagRidingEntities = new NbtCompound();
		ridingEntities.forEach(uuid -> tagRidingEntities.putUuid(KEY_RIDING_ENTITIES + uuid, uuid));
		nbtCompound.put(KEY_RIDING_ENTITIES, tagRidingEntities);

		return nbtCompound;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);

		packet.writeFloat(speed);
		packet.writeFloat(railProgress);
		packet.writeFloat(stopCounter);
		packet.writeInt(nextStoppingIndex);
		packet.writeBoolean(reversed);
		packet.writeBoolean(isOnRoute);

		packet.writeInt(ridingEntities.size());
		ridingEntities.forEach(packet::writeUuid);
	}

	@Override
	public void update(String key, PacketByteBuf packet) {
		if (key.equals(Siding.KEY_TRAINS)) {
			speed = packet.readFloat();
			railProgress = packet.readFloat();
			stopCounter = packet.readFloat();
			nextStoppingIndex = packet.readInt();
			reversed = packet.readBoolean();
			isOnRoute = packet.readBoolean();

			final float percentageX = packet.readFloat();
			final float percentageZ = packet.readFloat();
			if (percentageX != 0) {
				clientPercentageX = percentageX;
				clientPercentageZ = percentageZ;
			}

			ridingEntities.clear();
			final int ridingEntitiesCount = packet.readInt();
			for (int i = 0; i < ridingEntitiesCount; i++) {
				ridingEntities.add(packet.readUuid());
			}
		} else {
			super.update(key, packet);
		}
	}

	public boolean closeToDepot(int trainDistance) {
		return !isOnRoute || railProgress < trainDistance + railLength;
	}

	public boolean atDepot() {
		return !isOnRoute;
	}

	public float getRailProgress() {
		return railProgress;
	}

	public void writeTrainPositions(Set<Rail> trainPositions, Map<BlockPos, Map<BlockPos, Rail>> rails, CustomResources.TrainMapping trainTypeMapping, int trainLength) {
		if (!path.isEmpty()) {
			final int trainSpacing = trainTypeMapping.trainType.getSpacing();
			final int headIndex = getIndex(0, trainSpacing, true);
			final int tailIndex = getIndex(trainLength, trainSpacing, false);
			for (int i = tailIndex; i <= headIndex; i++) {
				if (i > 0) {
					final PathData pathData = path.get(i);
					trainPositions.add(pathData.rail);
					trainPositions.add(pathData.getOppositeRail(rails));
				}
			}
		}
	}

	public void simulateTrain(World world, PlayerEntity clientPlayer, float ticksElapsed, Depot depot, CustomResources.TrainMapping trainTypeMapping, int trainLength, Set<Rail> trainPositions, Map<Long, Set<Route.ScheduleEntry>> schedulesForPlatform, RenderTrainCallback renderTrainCallback, RenderConnectionCallback renderConnectionCallback, SpeedCallback speedCallback, AnnouncementCallback announcementCallback, Runnable generateRoute) {
		if (world == null) {
			return;
		}
		if (path.isEmpty()) {
			generateRoute.run();
		}

		try {
			final int trainSpacing = trainTypeMapping.trainType.getSpacing();
			final float oldRailProgress = railProgress;
			final float oldSpeed = speed;
			final float oldDoorValue;
			final float doorValueRaw;

			if (!isOnRoute) {
				railProgress = (railLength + trainLength * trainSpacing) / 2;
				oldDoorValue = 0;
				doorValueRaw = 0;
				speed = 0;

				if (path.size() > 1 && depot != null && depot.deployTrain(world)) {
					generateRoute.run();
					if (!world.isClient()) {
						isOnRoute = true;
						nextStoppingIndex = 0;
						startUpAndSync(world, trainLength, trainSpacing);
					}
				}
			} else {
				oldDoorValue = nextStoppingIndex < path.size() ? Math.abs(getDoorValue()) : 0;
				final float newAcceleration = ACCELERATION * ticksElapsed;

				if (railProgress >= distances.get(distances.size() - 1) - (railLength - trainLength * trainSpacing) / 2) {
					isOnRoute = false;
					ridingEntities.forEach(uuid -> {
						final PlayerEntity player = world.getPlayerByUuid(uuid);
						if (player != null) {
							player.fallDistance = 0;
							((PlayerTeleportationStateAccessor) player).setInTeleportationState(false);
						}
					});
					ridingEntities.clear();
					syncTrainToClient(world);
					doorValueRaw = 0;
				} else {
					if (speed <= 0) {
						speed = 0;
						final int dwellTicks = path.get(nextStoppingIndex).dwellTime * 10;

						if (dwellTicks == 0) {
							doorValueRaw = 0;
						} else {
							stopCounter += ticksElapsed;
							doorValueRaw = getDoorValue();
						}

						if (stopCounter >= dwellTicks && !railBlocked(trainPositions, getIndex(0, trainSpacing, true) + (isOppositeRail() ? 2 : 1))) {
							startUpAndSync(world, trainLength, trainSpacing);
						}
					} else {
						if (!world.isClient()) {
							final int checkIndex = getIndex(0, trainSpacing, true) + 1;
							if (railBlocked(trainPositions, checkIndex)) {
								final int oldStoppingIndex = nextStoppingIndex;
								nextStoppingIndex = checkIndex - 1;
								if (oldStoppingIndex != nextStoppingIndex) {
									syncTrainToClient(world);
								}
							}
						}

						final float stoppingDistance = distances.get(nextStoppingIndex) - railProgress;
						if (stoppingDistance < 0.5F * speed * speed / ACCELERATION) {
							speed = Math.max(speed - (0.5F * speed * speed / stoppingDistance) * ticksElapsed, ACCELERATION);
						} else {
							final float railSpeed = path.get(getIndex(0, trainSpacing, false)).rail.railType.maxBlocksPerTick;
							if (speed < railSpeed) {
								speed = Math.min(speed + newAcceleration, railSpeed);
							} else if (speed > railSpeed) {
								speed = Math.max(speed - newAcceleration, railSpeed);
							}
						}

						doorValueRaw = 0;
					}

					railProgress += speed * ticksElapsed;
					if (railProgress > distances.get(nextStoppingIndex)) {
						railProgress = distances.get(nextStoppingIndex);
						speed = 0;
						syncTrainToClient(world);
					}
				}
			}

			final Pos3f[] positions = new Pos3f[trainLength + 1];
			for (int i = 0; i <= trainLength; i++) {
				positions[i] = getRoutePosition(reversed ? trainLength - i : i, trainSpacing);
			}

			if (!path.isEmpty() && depot != null) {
				final List<Vec3d> offset = new ArrayList<>();

				if (clientPlayer != null && ridingEntities.contains(clientPlayer.getUuid())) {
					if (speedCallback != null) {
						speedCallback.speedCallback(speed * 20, path.get(getIndex(0, trainSpacing, false)).stopIndex - 1, depot.routeIds);
					}

					if (announcementCallback != null) {
						// TODO announcements don't work when train is stuck
						float targetProgress = distances.get(getPreviousStoppingIndex(trainSpacing)) + trainLength * trainSpacing;
						if (oldRailProgress < targetProgress && railProgress >= targetProgress) {
							announcementCallback.announcementCallback(path.get(getIndex(0, trainSpacing, false)).stopIndex - 1, depot.routeIds);
						}
					}

					calculateRender(world, positions, (int) Math.floor(clientPercentageZ), Math.abs(doorValueRaw), (x, y, z, yaw, pitch, realSpacing, doorLeftOpen, doorRightOpen) -> {
						final Vec3d movement = new Vec3d(clientPlayer.sidewaysSpeed * ticksElapsed / 4, 0, clientPlayer.forwardSpeed * ticksElapsed / 4).rotateY((float) -Math.toRadians(clientPlayer.yaw) - yaw);
						clientPercentageX += movement.x / trainTypeMapping.trainType.width;
						clientPercentageZ += movement.z / realSpacing;
						clientPercentageX = MathHelper.clamp(clientPercentageX, doorLeftOpen ? -1 : 0, doorRightOpen ? 2 : 1);
						clientPercentageZ = MathHelper.clamp(clientPercentageZ, 0.01F, trainLength - 0.01F);

						clientPlayer.fallDistance = 0;
						clientPlayer.setVelocity(0, 0, 0);
						final Vec3d playerOffset = new Vec3d(getValueFromPercentage(clientPercentageX, trainTypeMapping.trainType.width), 0, getValueFromPercentage(MathHelper.fractionalPart(clientPercentageZ), realSpacing)).rotateX(pitch).rotateY(yaw).add(x, y, z);
						clientPlayer.move(MovementType.SELF, playerOffset.subtract(clientPlayer.getPos()));

						if (speed > 0) {
							offset.add(playerOffset.add(0, clientPlayer.getStandingEyeHeight(), 0));
						}
					});
				}

				render(world, positions, doorValueRaw, oldSpeed, oldDoorValue, trainTypeMapping, trainLength, renderTrainCallback, renderConnectionCallback, offset.isEmpty() ? null : offset.get(0));
			}

			if (world.isClient()) {
				writeArrivalTimes(schedulesForPlatform, trainTypeMapping, trainSpacing);
			}
		} catch (Exception ignored) {
			generateRoute.run();
		}
	}

	private void startUpAndSync(World world, int trainLength, int trainSpacing) {
		if (!world.isClient()) {
			stopCounter = 0;
			speed = ACCELERATION;
			if (isOppositeRail()) {
				railProgress += trainLength * trainSpacing;
				reversed = !reversed;
			}
			nextStoppingIndex = getNextStoppingIndex(trainSpacing);
			syncTrainToClient(world);
		}
	}

	private void render(World world, Pos3f[] positions, float doorValueRaw, float oldSpeed, float oldDoorValue, CustomResources.TrainMapping trainTypeMapping, int trainLength, RenderTrainCallback renderTrainCallback, RenderConnectionCallback renderConnectionCallback, Vec3d offset) {
		final TrainType trainType = trainTypeMapping.trainType;
		final float doorValue = Math.abs(doorValueRaw);
		final boolean opening = doorValueRaw > 0;

		final float[] xList = new float[trainLength];
		final float[] yList = new float[trainLength];
		final float[] zList = new float[trainLength];
		final float[] yawList = new float[trainLength];
		final float[] pitchList = new float[trainLength];
		final float[] realSpacingList = new float[trainLength];
		final boolean[] doorLeftOpenList = new boolean[trainLength];
		final boolean[] doorRightOpenList = new boolean[trainLength];

		for (int i = 0; i < trainLength; i++) {
			final int ridingCar = i;
			calculateRender(world, positions, i, doorValue, (x, y, z, yaw, pitch, realSpacing, doorLeftOpen, doorRightOpen) -> {
				xList[ridingCar] = x;
				yList[ridingCar] = y;
				zList[ridingCar] = z;
				yawList[ridingCar] = yaw;
				pitchList[ridingCar] = pitch;
				realSpacingList[ridingCar] = realSpacing;
				doorLeftOpenList[ridingCar] = doorLeftOpen;
				doorRightOpenList[ridingCar] = doorRightOpen;
			});
		}

		for (int i = 0; i < xList.length; i++) {
			final int ridingCar = i;
			final float x = xList[i];
			final float y = yList[i];
			final float z = zList[i];
			final float yaw = yawList[i];
			final float pitch = pitchList[i];
			final float realSpacing = realSpacingList[i];
			final boolean doorLeftOpen = doorLeftOpenList[i];
			final boolean doorRightOpen = doorRightOpenList[i];

			final float halfSpacing = realSpacing / 2;
			final float halfWidth = trainType.width / 2F;

			if (world.isClient()) {
				final float newX = x - (offset == null ? 0 : (float) offset.x);
				final float newY = y - (offset == null ? 0 : (float) offset.y);
				final float newZ = z - (offset == null ? 0 : (float) offset.z);

				if (renderTrainCallback != null) {
					renderTrainCallback.renderTrainCallback(newX, newY, newZ, yaw, pitch, trainTypeMapping.customId, trainType, i == 0, i == trainLength - 1, !reversed, doorLeftOpen ? doorValue : 0, doorRightOpen ? doorValue : 0, opening, isOnRoute, offset != null);
				}

				if (renderConnectionCallback != null && i > 0 && trainType.shouldRenderConnection) {
					final float prevCarX = xList[i - 1] - (offset == null ? 0 : (float) offset.x);
					final float prevCarY = yList[i - 1] - (offset == null ? 0 : (float) offset.y);
					final float prevCarZ = zList[i - 1] - (offset == null ? 0 : (float) offset.z);
					final float prevCarYaw = yawList[i - 1];
					final float prevCarPitch = pitchList[i - 1];

					final float xStart = halfWidth - CONNECTION_X_OFFSET;
					final float zStart = trainType.getSpacing() / 2F - CONNECTION_Z_OFFSET;

					final Pos3f prevPos1 = new Pos3f(xStart, SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(prevCarX, prevCarY, prevCarZ);
					final Pos3f prevPos2 = new Pos3f(xStart, CONNECTION_HEIGHT + SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(prevCarX, prevCarY, prevCarZ);
					final Pos3f prevPos3 = new Pos3f(-xStart, CONNECTION_HEIGHT + SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(prevCarX, prevCarY, prevCarZ);
					final Pos3f prevPos4 = new Pos3f(-xStart, SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(prevCarX, prevCarY, prevCarZ);

					final Pos3f thisPos1 = new Pos3f(-xStart, SMALL_OFFSET, -zStart).rotateX(pitch).rotateY(yaw).add(newX, newY, newZ);
					final Pos3f thisPos2 = new Pos3f(-xStart, CONNECTION_HEIGHT + SMALL_OFFSET, -zStart).rotateX(pitch).rotateY(yaw).add(newX, newY, newZ);
					final Pos3f thisPos3 = new Pos3f(xStart, CONNECTION_HEIGHT + SMALL_OFFSET, -zStart).rotateX(pitch).rotateY(yaw).add(newX, newY, newZ);
					final Pos3f thisPos4 = new Pos3f(xStart, SMALL_OFFSET, -zStart).rotateX(pitch).rotateY(yaw).add(newX, newY, newZ);

					renderConnectionCallback.renderConnectionCallback(prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, newX, newY, newZ, trainType, isOnRoute, offset != null);
				}
			} else {
				final BlockPos soundPos = new BlockPos(x, y, z);
				trainType.playSpeedSoundEffect(world, soundPos, oldSpeed, speed);

				if (doorLeftOpen || doorRightOpen) {
					if (oldDoorValue <= 0 && doorValue > 0 && trainType.doorOpenSoundEvent != null) {
						world.playSound(null, soundPos, trainType.doorOpenSoundEvent, SoundCategory.BLOCKS, 1, 1);
					} else if (oldDoorValue >= trainType.doorCloseSoundTime && doorValue < trainType.doorCloseSoundTime && trainType.doorCloseSoundEvent != null) {
						world.playSound(null, soundPos, trainType.doorCloseSoundEvent, SoundCategory.BLOCKS, 1, 1);
					}

					final float margin = halfSpacing + BOX_PADDING;
					world.getEntitiesByClass(PlayerEntity.class, new Box(x + margin, y + margin, z + margin, x - margin, y - margin, z - margin), player -> !player.isSpectator() && !ridingEntities.contains(player.getUuid())).forEach(player -> {
						final Vec3d positionRotated = player.getPos().subtract(x, y, z).rotateY(-yaw).rotateX(-pitch);
						if (Math.abs(positionRotated.x) < halfWidth + INNER_PADDING && Math.abs(positionRotated.y) < 1.5 && Math.abs(positionRotated.z) <= halfSpacing) {
							ridingEntities.add(player.getUuid());
							player.fallDistance = 0;
							((PlayerTeleportationStateAccessor) player).setInTeleportationState(true);
							syncTrainToClient(world, player, (float) (positionRotated.x / trainType.width + 0.5), (float) (positionRotated.z / realSpacing + 0.5) + ridingCar);
						}
					});
				}

				final Set<UUID> entitiesToRemove = new HashSet<>();
				ridingEntities.forEach(uuid -> {
					final PlayerEntity player = world.getPlayerByUuid(uuid);
					if (player != null) {
						final Vec3d positionRotated = player.getPos().subtract(x, y, z).rotateY(-yaw).rotateX(-pitch);
						if (player.isSpectator() || player.isSneaking() || (doorLeftOpen || doorRightOpen) && Math.abs(positionRotated.z) <= halfSpacing && (Math.abs(positionRotated.x) > halfWidth + INNER_PADDING || Math.abs(positionRotated.y) > 1.5)) {
							player.fallDistance = 0;
							((PlayerTeleportationStateAccessor) player).setInTeleportationState(false);
							entitiesToRemove.add(uuid);
						}
					}
				});
				if (!entitiesToRemove.isEmpty()) {
					entitiesToRemove.forEach(ridingEntities::remove);
					syncTrainToClient(world);
				}
			}
		}
	}

	private void calculateRender(World world, Pos3f[] positions, int index, float doorValue, CalculateRenderCallback calculateRenderCallback) {
		final Pos3f pos1 = positions[index];
		final Pos3f pos2 = positions[index + 1];

		if (pos1 != null && pos2 != null) {
			final float x = getAverage(pos1.x, pos2.x);
			final float y = getAverage(pos1.y, pos2.y) + 1;
			final float z = getAverage(pos1.z, pos2.z);

			final float realSpacing = pos2.getDistanceTo(pos1);
			final float yaw = (float) MathHelper.atan2(pos2.x - pos1.x, pos2.z - pos1.z);
			final float pitch = realSpacing == 0 ? 0 : (float) Math.asin((pos2.y - pos1.y) / realSpacing);
			final boolean doorLeftOpen = openDoors(world, x, y, z, (float) Math.PI + yaw, realSpacing / 2, doorValue) && doorValue > 0;
			final boolean doorRightOpen = openDoors(world, x, y, z, yaw, realSpacing / 2, doorValue) && doorValue > 0;

			calculateRenderCallback.calculateRenderCallback(x, y, z, yaw, pitch, realSpacing, doorLeftOpen, doorRightOpen);
		}
	}

	private boolean railBlocked(Set<Rail> trainPositions, int checkIndex) {
		if (trainPositions != null && checkIndex < path.size()) {
			return trainPositions.contains(path.get(checkIndex).rail);
		} else {
			return false;
		}
	}

	private boolean isOppositeRail() {
		return path.size() > nextStoppingIndex + 1 && path.get(nextStoppingIndex).isOppositeRail(path.get(nextStoppingIndex + 1));
	}

	private float getRailProgress(int car, int trainSpacing) {
		return railProgress - car * trainSpacing;
	}

	private int getIndex(int car, int trainSpacing, boolean roundDown) {
		for (int i = 0; i < path.size(); i++) {
			final float tempRailProgress = getRailProgress(car, trainSpacing);
			final float tempDistance = distances.get(i);
			if (tempRailProgress < tempDistance || roundDown && tempRailProgress == tempDistance) {
				return i;
			}
		}
		return path.size() - 1;
	}

	private Pos3f getRoutePosition(int car, int trainSpacing) {
		final int index = getIndex(car, trainSpacing, false);
		return path.get(index).rail.getPosition(getRailProgress(car, trainSpacing) - (index == 0 ? 0 : distances.get(index - 1)));
	}

	private int getNextStoppingIndex(int trainSpacing) {
		final int headIndex = getIndex(0, trainSpacing, false);
		for (int i = headIndex; i < path.size(); i++) {
			if (path.get(i).dwellTime > 0) {
				return i;
			}
		}
		return path.size() - 1;
	}

	private int getPreviousStoppingIndex(int trainSpacing) {
		final int headIndex = getIndex(0, trainSpacing, false);
		for (int i = headIndex; i >= 0; i--) {
			if (path.get(i).dwellTime > 0) {
				return i;
			}
		}
		return 0;
	}

	private void syncTrainToClient(World world) {
		syncTrainToClient(world, null, 0, 0);
	}

	private void syncTrainToClient(World world, PlayerEntity player, float percentageX, float percentageZ) {
		if (world != null && !world.isClient()) {
			final PacketByteBuf packet = PacketByteBufs.create();
			packet.writeLong(sidingId);
			packet.writeString(Siding.KEY_TRAINS);
			packet.writeLong(id);
			packet.writeFloat(speed);
			packet.writeFloat(railProgress);
			packet.writeFloat(stopCounter);
			packet.writeInt(nextStoppingIndex);
			packet.writeBoolean(reversed);
			packet.writeBoolean(isOnRoute);

			packet.writeFloat(percentageX);
			packet.writeFloat(percentageZ);

			packet.writeInt(ridingEntities.size());
			ridingEntities.forEach(packet::writeUuid);

			final RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				railwayData.markDirty();
			}

			if (player == null) {
				world.getPlayers().forEach(serverPlayer -> ServerPlayNetworking.send((ServerPlayerEntity) serverPlayer, PACKET_UPDATE_SIDING, packet));
			} else {
				ServerPlayNetworking.send((ServerPlayerEntity) player, PACKET_UPDATE_SIDING, packet);
			}
		}
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

	private boolean openDoors(World world, float trainX, float trainY, float trainZ, float checkYaw, float halfSpacing, float doorValue) {
		if (!world.isClient() && !world.isChunkLoaded((int) trainX / 16, (int) trainZ / 16)) {
			return false;
		}

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

	private void writeArrivalTimes(Map<Long, Set<Route.ScheduleEntry>> schedulesForPlatform, CustomResources.TrainMapping trainTypeMapping, int trainSpacing) {
		final int index = getIndex(0, trainSpacing, true);
		final Pair<Float, Float> firstTimeAndSpeed = writeArrivalTime(schedulesForPlatform, trainTypeMapping, index, index == 0 ? railProgress : railProgress - distances.get(index - 1), 0, speed);

		float currentTicks = firstTimeAndSpeed.getLeft();
		float currentSpeed = firstTimeAndSpeed.getRight();
		for (int i = index + 1; i < path.size(); i++) {
			final Pair<Float, Float> timeAndSpeed = writeArrivalTime(schedulesForPlatform, trainTypeMapping, i, 0, currentTicks, currentSpeed);
			currentTicks += timeAndSpeed.getLeft();
			currentSpeed = timeAndSpeed.getRight();
		}
	}

	private Pair<Float, Float> writeArrivalTime(Map<Long, Set<Route.ScheduleEntry>> schedulesForPlatform, CustomResources.TrainMapping trainTypeMapping, int index, float progress, float currentTicks, float currentSpeed) {
		final PathData pathData = path.get(index);
		final Pair<Float, Float> timeAndSpeed = calculateTicksAndSpeed(pathData.rail, progress, currentSpeed, pathData.dwellTime > 0 || index == nextStoppingIndex);

		if (pathData.dwellTime > 0) {
			final float stopTicksRemaining = Math.max(pathData.dwellTime * 10 - (index == nextStoppingIndex ? stopCounter : 0), 0);

			if (pathData.savedRailBaseId > 0) {
				if (!schedulesForPlatform.containsKey(pathData.savedRailBaseId)) {
					schedulesForPlatform.put(pathData.savedRailBaseId, new HashSet<>());
				}

				final float arrivalTime = (currentTicks + timeAndSpeed.getLeft()) * 50;
				// TODO destination name
				schedulesForPlatform.get(pathData.savedRailBaseId).add(new Route.ScheduleEntry(arrivalTime, arrivalTime + stopTicksRemaining * 50, trainTypeMapping.trainType, pathData.savedRailBaseId, pathData.savedRailBaseId));
			}
			return new Pair<>(timeAndSpeed.getLeft() + stopTicksRemaining, timeAndSpeed.getRight());
		} else {
			return timeAndSpeed;
		}
	}

	public static float getAverage(float a, float b) {
		return (a + b) / 2;
	}

	public static float getValueFromPercentage(float percentage, float total) {
		return (percentage - 0.5F) * total;
	}

	private static Pair<Float, Float> calculateTicksAndSpeed(Rail rail, float progress, float initialSpeed, boolean shouldStop) {
		final float distance = rail.getLength() - progress;

		if (distance <= 0) {
			return new Pair<>(0F, initialSpeed);
		}

		if (shouldStop) {
			if (initialSpeed * initialSpeed / (2 * distance) >= ACCELERATION) {
				return new Pair<>(2 * distance / initialSpeed, 0F);
			}

			final float maxSpeed = Math.min(rail.railType.maxBlocksPerTick, (float) Math.sqrt(ACCELERATION * distance + initialSpeed * initialSpeed / 2));
			final float ticks = (2 * ACCELERATION * distance + initialSpeed * initialSpeed - 2 * initialSpeed * maxSpeed + 2 * maxSpeed * maxSpeed) / (2 * ACCELERATION * maxSpeed);
			return new Pair<>(ticks, 0F);
		} else {
			final float railSpeed = rail.railType.maxBlocksPerTick;

			if (initialSpeed == railSpeed) {
				return new Pair<>(distance / initialSpeed, initialSpeed);
			} else {
				final float accelerationDistance = (railSpeed * railSpeed - initialSpeed * initialSpeed) / (2 * ACCELERATION);

				if (accelerationDistance > distance) {
					final float finalSpeed = (float) Math.sqrt(2 * ACCELERATION * distance + initialSpeed * initialSpeed);
					return new Pair<>((finalSpeed - initialSpeed) / ACCELERATION, finalSpeed);
				} else {
					final float accelerationTicks = (railSpeed - initialSpeed) / ACCELERATION;
					final float coastingTicks = (distance - accelerationDistance) / railSpeed;
					return new Pair<>(accelerationTicks + coastingTicks, railSpeed);
				}
			}
		}
	}

	@FunctionalInterface
	public interface RenderTrainCallback {
		void renderTrainCallback(float x, float y, float z, float yaw, float pitch, String customId, TrainType trainType, boolean isEnd1Head, boolean isEnd2Head, boolean head1IsFront, float doorLeftValue, float doorRightValue, boolean opening, boolean lightsOn, boolean offsetRender);
	}

	@FunctionalInterface
	public interface RenderConnectionCallback {
		void renderConnectionCallback(Pos3f prevPos1, Pos3f prevPos2, Pos3f prevPos3, Pos3f prevPos4, Pos3f thisPos1, Pos3f thisPos2, Pos3f thisPos3, Pos3f thisPos4, float x, float y, float z, TrainType trainType, boolean lightsOn, boolean offsetRender);
	}

	@FunctionalInterface
	public interface SpeedCallback {
		void speedCallback(float speed, int stopIndex, List<Long> routeIds);
	}

	@FunctionalInterface
	public interface AnnouncementCallback {
		void announcementCallback(int stopIndex, List<Long> routeIds);
	}

	@FunctionalInterface
	private interface CalculateRenderCallback {
		void calculateRenderCallback(float x, float y, float z, float yaw, float pitch, float realSpacing, boolean doorLeftOpen, boolean doorRightOpen);
	}
}
