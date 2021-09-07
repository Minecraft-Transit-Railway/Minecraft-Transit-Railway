package mtr.data;

import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPlatform;
import mtr.block.BlockTrainSensor;
import mtr.config.CustomResources;
import mtr.packet.IPacket;
import mtr.path.PathData;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Pair;
import net.minecraft.util.math.*;
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
	private float clientPrevYaw;
	private boolean justMounted;
	private boolean canDeploy;

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
	private static final int DOOR_MAX_DISTANCE = 32;

	private static final float INNER_PADDING = 0.5F;
	private static final int BOX_PADDING = 3;

	private static final float CONNECTION_HEIGHT = 2.25F;
	private static final float CONNECTION_Z_OFFSET = 0.5F;
	private static final float CONNECTION_X_OFFSET = 0.25F;

	public Train(World world, long id, long sidingId, float railLength, List<PathData> path, List<Float> distances) {
		super(id);
		this.sidingId = sidingId;
		this.railLength = railLength;
		this.path = path;
		this.distances = distances;
		syncTrainToClient(world, null, false, 0, 0);
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
		writeMainPacket(packet);
	}

	@Override
	public void update(String key, PacketByteBuf packet) {
		if (key.equals(Siding.KEY_TRAINS)) {
			speed = packet.readFloat();

			final float tempRailProgress = packet.readFloat();
			if (speed <= ACCELERATION) {
				railProgress = tempRailProgress;
			}

			stopCounter = packet.readFloat();
			nextStoppingIndex = packet.readInt();
			reversed = packet.readBoolean();
			isOnRoute = packet.readBoolean();

			ridingEntities.clear();
			final int ridingEntitiesCount = packet.readInt();
			for (int i = 0; i < ridingEntitiesCount; i++) {
				ridingEntities.add(packet.readUuid());
			}

			final float percentageX = packet.readFloat();
			final float percentageZ = packet.readFloat();
			if (percentageX != 0) {
				clientPercentageX = percentageX;
				clientPercentageZ = percentageZ;
				justMounted = true;
			}
		} else {
			super.update(key, packet);
		}
	}

	public boolean getIsOnRoute() {
		return isOnRoute;
	}

	public float getRailProgress() {
		return railProgress;
	}

	public boolean closeToDepot(int trainDistance) {
		return !isOnRoute || railProgress < trainDistance + railLength;
	}

	public void writeTrainPositions(Set<UUID> trainPositions, CustomResources.TrainMapping trainTypeMapping, int trainLength) {
		if (!path.isEmpty()) {
			final int trainSpacing = trainTypeMapping.trainType.getSpacing();
			final int headIndex = getIndex(0, trainSpacing, true);
			final int tailIndex = getIndex(trainLength, trainSpacing, false);
			for (int i = tailIndex; i <= headIndex; i++) {
				if (i > 0 && path.get(i).savedRailBaseId != sidingId) {
					trainPositions.add(path.get(i).getRailProduct());
				}
			}
		}
	}

	public void simulateTrain(World world, PlayerEntity clientPlayer, float ticksElapsed, Depot depot, CustomResources.TrainMapping trainTypeMapping, int trainLength, Set<UUID> trainPositions, RenderTrainCallback renderTrainCallback, RenderConnectionCallback renderConnectionCallback, SpeedCallback speedCallback, AnnouncementCallback announcementCallback, AnnouncementCallback lightRailAnnouncementCallback, WriteScheduleCallback writeScheduleCallback) {
		if (world == null) {
			return;
		}

		final int oldStoppingIndex = nextStoppingIndex;
		final boolean oldIsOnRoute = isOnRoute;
		final int oldPassengerCount = ridingEntities.size();
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

				if (path.size() > 1 && depot != null) {
					depot.requestDeploy(sidingId, this);
				}
				if (canDeploy && !world.isClient()) {
					canDeploy = false;
					isOnRoute = true;
					nextStoppingIndex = 0;
					startUp(world, trainLength, trainSpacing);
				}
			} else {
				oldDoorValue = nextStoppingIndex < path.size() ? Math.abs(getDoorValue()) : 0;
				final float newAcceleration = ACCELERATION * ticksElapsed;

				if (railProgress >= distances.get(distances.size() - 1) - (railLength - trainLength * trainSpacing) / 2) {
					isOnRoute = false;
					ridingEntities.clear();
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
							startUp(world, trainLength, trainSpacing);
						}
					} else {
						if (!world.isClient()) {
							final int checkIndex = getIndex(0, trainSpacing, true) + 1;
							if (railBlocked(trainPositions, checkIndex)) {
								nextStoppingIndex = checkIndex - 1;
							}
						}

						final float stoppingDistance = distances.get(nextStoppingIndex) - railProgress;
						if (stoppingDistance < 0.5F * speed * speed / ACCELERATION) {
							speed = Math.max(speed - (0.5F * speed * speed / stoppingDistance) * ticksElapsed, ACCELERATION);
						} else {
							final float railSpeed = getRailSpeed(getIndex(0, trainSpacing, false));
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
					}
				}
			}

			final Pos3f[] positions = new Pos3f[trainLength + 1];
			for (int i = 0; i <= trainLength; i++) {
				positions[i] = getRoutePosition(reversed ? trainLength - i : i, trainSpacing);
			}

			if (!path.isEmpty() && depot != null) {
				final List<Float> offset = new ArrayList<>();

				final boolean playerRiding = clientPlayer != null && ridingEntities.contains(clientPlayer.getUuid());
				final int headIndex = getIndex(0, trainSpacing, false);
				final int stopIndex = path.get(headIndex).stopIndex - 1;
				if (playerRiding) {
					if (speedCallback != null) {
						speedCallback.speedCallback(speed * 20, stopIndex, depot.routeIds);
					}

					if (announcementCallback != null) {
						float targetProgress = distances.get(getPreviousStoppingIndex(headIndex)) + (trainLength + 1) * trainSpacing;
						if (oldRailProgress < targetProgress && railProgress >= targetProgress) {
							announcementCallback.announcementCallback(stopIndex, depot.routeIds);
						}
					}

					if (lightRailAnnouncementCallback != null && (oldDoorValue <= 0 && doorValueRaw != 0 || justMounted)) {
						lightRailAnnouncementCallback.announcementCallback(stopIndex, depot.routeIds);
					}

					calculateRender(world, positions, (int) Math.floor(clientPercentageZ), Math.abs(doorValueRaw), (x, y, z, yaw, pitch, realSpacing, doorLeftOpen, doorRightOpen) -> {
						final Vec3d movement = new Vec3d(clientPlayer.sidewaysSpeed * ticksElapsed / 4, 0, clientPlayer.forwardSpeed * ticksElapsed / 4).rotateY((float) -Math.toRadians(clientPlayer.yaw) - yaw);
						final boolean shouldRenderConnection = trainTypeMapping.trainType.shouldRenderConnection;
						clientPercentageX += movement.x / trainTypeMapping.trainType.width;
						clientPercentageZ += movement.z / realSpacing;
						clientPercentageX = MathHelper.clamp(clientPercentageX, doorLeftOpen ? -1 : 0, doorRightOpen ? 2 : 1);
						clientPercentageZ = MathHelper.clamp(clientPercentageZ, (shouldRenderConnection ? 0 : (int) Math.floor(clientPercentageZ) + 0.05F) + 0.01F, (shouldRenderConnection ? trainLength : (int) Math.floor(clientPercentageZ) + 0.95F) - 0.01F);

						clientPlayer.fallDistance = 0;
						clientPlayer.setVelocity(0, 0, 0);
						final Vec3d playerOffset = new Vec3d(getValueFromPercentage(clientPercentageX, trainTypeMapping.trainType.width), 0, getValueFromPercentage(MathHelper.fractionalPart(clientPercentageZ), realSpacing)).rotateX(pitch).rotateY(yaw);
						clientPlayer.move(MovementType.SELF, playerOffset.add(x - clientPlayer.getX(), y - clientPlayer.getY(), z - clientPlayer.getZ()));

						if (speed > 0) {
							clientPlayer.yaw -= Math.toDegrees(yaw - clientPrevYaw);
							offset.add(x);
							offset.add(y);
							offset.add(z);
							offset.add((float) playerOffset.x);
							offset.add((float) playerOffset.y + clientPlayer.getStandingEyeHeight());
							offset.add((float) playerOffset.z);
						}

						clientPrevYaw = yaw;
					});
				}

				justMounted = false;
				render(world, positions, doorValueRaw, oldSpeed, oldDoorValue, trainTypeMapping, trainLength, renderTrainCallback, renderConnectionCallback, offset.isEmpty() ? null : offset);
			}

			if (world.isClient() && depot != null && writeScheduleCallback != null) {
				final int offsetTicks = isOnRoute ? 0 : depot.getNextDepartureTicks(Depot.getHour(world));
				if (offsetTicks >= 0) {
					writeArrivalTimes(writeScheduleCallback, depot.routeIds, offsetTicks, trainTypeMapping, trainLength, trainSpacing);
				}
			}
		} catch (Exception ignored) {
		}

		if (oldPassengerCount > ridingEntities.size() || oldStoppingIndex != nextStoppingIndex) {
			syncTrainToClient(world, null, !oldIsOnRoute && isOnRoute, 0, 0);
		}
	}

	public void deployTrain() {
		canDeploy = true;
	}

	private void startUp(World world, int trainLength, int trainSpacing) {
		if (!world.isClient()) {
			stopCounter = 0;
			speed = ACCELERATION;
			if (isOppositeRail()) {
				railProgress += trainLength * trainSpacing;
				reversed = !reversed;
			}
			nextStoppingIndex = getNextStoppingIndex(trainSpacing);
		}
	}

	private void render(World world, Pos3f[] positions, float doorValueRaw, float oldSpeed, float oldDoorValue, CustomResources.TrainMapping trainTypeMapping, int trainLength, RenderTrainCallback renderTrainCallback, RenderConnectionCallback renderConnectionCallback, List<Float> offset) {
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
				final float newX = x - (offset == null ? 0 : offset.get(0));
				final float newY = y - (offset == null ? 0 : offset.get(1));
				final float newZ = z - (offset == null ? 0 : offset.get(2));
				final Vec3d playerOffset = offset == null ? null : new Vec3d(offset.get(3), offset.get(4), offset.get(5));

				if (renderTrainCallback != null) {
					renderTrainCallback.renderTrainCallback(newX, newY, newZ, yaw, pitch, trainTypeMapping.customId, trainType, i == 0, i == trainLength - 1, !reversed, doorLeftOpen ? doorValue : 0, doorRightOpen ? doorValue : 0, opening, isOnRoute, playerOffset);
				}

				if (renderConnectionCallback != null && i > 0 && trainType.shouldRenderConnection) {
					final float prevCarX = xList[i - 1] - (offset == null ? 0 : (float) offset.get(0));
					final float prevCarY = yList[i - 1] - (offset == null ? 0 : (float) offset.get(1));
					final float prevCarZ = zList[i - 1] - (offset == null ? 0 : (float) offset.get(2));
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

					renderConnectionCallback.renderConnectionCallback(prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, newX, newY, newZ, yaw, trainType, isOnRoute, playerOffset);
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
							syncTrainToClient(world, player, false, (float) (positionRotated.x / trainType.width + 0.5), (float) (positionRotated.z / realSpacing + 0.5) + ridingCar);
						}
					});
				}

				final RailwayData railwayData = RailwayData.getInstance(world);
				final Set<UUID> entitiesToRemove = new HashSet<>();
				ridingEntities.forEach(uuid -> {
					final PlayerEntity player = world.getPlayerByUuid(uuid);
					if (player != null) {
						final Vec3d positionRotated = player.getPos().subtract(x, y, z).rotateY(-yaw).rotateX(-pitch);
						if (player.isSpectator() || player.isSneaking() || (doorLeftOpen || doorRightOpen) && Math.abs(positionRotated.z) <= halfSpacing && (Math.abs(positionRotated.x) > halfWidth + INNER_PADDING || Math.abs(positionRotated.y) > 1.5)) {
							entitiesToRemove.add(uuid);
						}
						if (railwayData != null) {
							railwayData.updatePlayerRiding(player);
						}
					}
				});
				if (!entitiesToRemove.isEmpty()) {
					entitiesToRemove.forEach(ridingEntities::remove);
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
			final boolean doorLeftOpen = openDoors(world, x, y, z, (float) Math.PI + yaw, pitch, realSpacing / 2, doorValue) && doorValue > 0;
			final boolean doorRightOpen = openDoors(world, x, y, z, yaw, pitch, realSpacing / 2, doorValue) && doorValue > 0;
			final boolean doTrainSensor = trainSensor(world, x, y, z, positions);


			calculateRenderCallback.calculateRenderCallback(x, y, z, yaw, pitch, realSpacing, doorLeftOpen, doorRightOpen);
		}
	}

	private boolean trainSensor(World world, float trainX, float trainY, float trainZ, Pos3f[] positions) {

			Pos3f front = null;

		    final ClientPlayerEntity player = MinecraftClient.getInstance().player;

			if(!reversed) {
				front = positions[0];
			} else {
				front = positions[positions.length -1];
			}

			if(front != null) {
				final BlockPos checkPos = new BlockPos(front.x, trainY -2, front.z);
				final Block block = world.getBlockState(checkPos).getBlock();
				final BlockState state = world.getBlockState(checkPos);

				final BlockPos checkPos2 = new BlockPos(front.x, trainY -3, front.z);
				final Block block2 = world.getBlockState(checkPos2).getBlock();
				final BlockState state2 = world.getBlockState(checkPos2);
				//player.sendMessage(new LiteralText(String.valueOf(speed)), false);
				if(block instanceof BlockTrainSensor && speed > 0.1) {
					world.setBlockState(checkPos, state.with(BlockTrainSensor.REDSTONE, true));
					world.getBlockTickScheduler().schedule(new BlockPos(checkPos), block, 20);
				} else if (block2 instanceof BlockTrainSensor && speed > 0.1) {
					world.setBlockState(checkPos2, state2.with(BlockTrainSensor.REDSTONE, true));
					world.getBlockTickScheduler().schedule(new BlockPos(checkPos2), block2, 20);
				}
			}

			return true;


	}

	private boolean railBlocked(Set<UUID> trainPositions, int checkIndex) {
		if (trainPositions != null && checkIndex < path.size()) {
			return trainPositions.contains(path.get(checkIndex).getRailProduct());
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

	private int getPreviousStoppingIndex(int headIndex) {
		for (int i = headIndex; i >= 0; i--) {
			if (path.get(i).dwellTime > 0 && path.get(i).rail.railType == RailType.PLATFORM) {
				return i;
			}
		}
		return 0;
	}

	private void syncTrainToClient(World world, PlayerEntity player, boolean isStartUpEvent, float percentageX, float percentageZ) {
		if (world != null && !world.isClient()) {
			final PacketByteBuf packet = PacketByteBufs.create();
			packet.writeLong(sidingId);
			packet.writeString(Siding.KEY_TRAINS);
			packet.writeBoolean(false);
			packet.writeLong(isStartUpEvent ? System.currentTimeMillis() : 0);
			packet.writeLong(id);
			writeMainPacket(packet);
			packet.writeFloat(percentageX);
			packet.writeFloat(percentageZ);
			if (player == null) {
				world.getPlayers().forEach(serverPlayer -> ServerPlayNetworking.send((ServerPlayerEntity) serverPlayer, PACKET_UPDATE_SIDING, packet));
			} else {
				ServerPlayNetworking.send((ServerPlayerEntity) player, PACKET_UPDATE_SIDING, packet);
			}
		}
	}

	private void writeMainPacket(PacketByteBuf packet) {
		packet.writeFloat(speed);
		packet.writeFloat(railProgress);
		packet.writeFloat(stopCounter);
		packet.writeInt(nextStoppingIndex);
		packet.writeBoolean(reversed);
		packet.writeBoolean(isOnRoute);
		packet.writeInt(ridingEntities.size());
		ridingEntities.forEach(packet::writeUuid);
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

	private boolean openDoors(World world, float trainX, float trainY, float trainZ, float checkYaw, float pitch, float halfSpacing, float doorValue) {
		if (!world.isClient() && world.getClosestPlayer(trainX, trainY, trainZ, DOOR_MAX_DISTANCE, entity -> true) == null) {
			return false;
		}

		boolean hasPlatform = false;
		final Vec3d offsetVec = new Vec3d(1, 0, 0).rotateY(checkYaw).rotateX(pitch);
		final Vec3d traverseVec = new Vec3d(0, 0, 1).rotateY(checkYaw).rotateX(pitch);

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
								final BlockPos doorPos = checkPos.up(i);
								final BlockState state = world.getBlockState(doorPos);
								final Block doorBlock = state.getBlock();

								if (doorBlock instanceof BlockPSDAPGDoorBase) {
									final int doorStateValue = (int) MathHelper.clamp(doorValue * DOOR_MOVE_TIME, 0, BlockPSDAPGDoorBase.MAX_OPEN_VALUE);
									world.setBlockState(doorPos, state.with(BlockPSDAPGDoorBase.OPEN, doorStateValue));

									if (doorStateValue > 0 && !world.getBlockTickScheduler().isScheduled(checkPos.up(), doorBlock)) {
										world.getBlockTickScheduler().schedule(new BlockPos(doorPos), doorBlock, 20);
									}
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

	private float getRailSpeed(int railIndex) {
		final RailType thisRail = path.get(railIndex).rail.railType;
		final float railSpeed;
		if (thisRail.canAccelerate) {
			railSpeed = thisRail.maxBlocksPerTick;
		} else {
			final RailType lastRail = railIndex > 0 ? path.get(railIndex - 1).rail.railType : thisRail;
			railSpeed = Math.max(lastRail.canAccelerate ? lastRail.maxBlocksPerTick : RailType.WOODEN.maxBlocksPerTick, speed);
		}
		return railSpeed;
	}

	private void writeArrivalTimes(WriteScheduleCallback writeScheduleCallback, List<Long> routeIds, int ticksOffset, CustomResources.TrainMapping trainTypeMapping, int trainLength, int trainSpacing) {
		final int index = getIndex(0, trainSpacing, true);
		final Pair<Float, Float> firstTimeAndSpeed = writeArrivalTime(writeScheduleCallback, routeIds, trainTypeMapping, trainLength, index, index == 0 ? railProgress : railProgress - distances.get(index - 1), ticksOffset, speed);

		float currentTicks = firstTimeAndSpeed.getLeft() + ticksOffset;
		float currentSpeed = firstTimeAndSpeed.getRight();
		for (int i = index + 1; i < path.size(); i++) {
			final Pair<Float, Float> timeAndSpeed = writeArrivalTime(writeScheduleCallback, routeIds, trainTypeMapping, trainLength, i, 0, currentTicks, currentSpeed);
			currentTicks += timeAndSpeed.getLeft();
			currentSpeed = timeAndSpeed.getRight();
		}
	}

	private Pair<Float, Float> writeArrivalTime(WriteScheduleCallback writeScheduleCallback, List<Long> routeIds, CustomResources.TrainMapping trainTypeMapping, int trainLength, int index, float progress, float currentTicks, float currentSpeed) {
		final PathData pathData = path.get(index);
		final Pair<Float, Float> timeAndSpeed = calculateTicksAndSpeed(getRailSpeed(index), pathData.rail.getLength(), progress, currentSpeed, pathData.dwellTime > 0 || index == nextStoppingIndex);

		if (pathData.dwellTime > 0) {
			final float stopTicksRemaining = Math.max(pathData.dwellTime * 10 - (index == nextStoppingIndex ? stopCounter : 0), 0);

			if (pathData.savedRailBaseId != 0) {
				final float arrivalTicks = currentTicks + timeAndSpeed.getLeft();
				writeScheduleCallback.writeScheduleCallback(pathData.savedRailBaseId, arrivalTicks * 50, (arrivalTicks + stopTicksRemaining) * 50, trainTypeMapping.trainType, trainLength, pathData.stopIndex - 1, routeIds);
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

	private static Pair<Float, Float> calculateTicksAndSpeed(float railSpeed, float railLength, float progress, float initialSpeed, boolean shouldStop) {
		final float distance = railLength - progress;

		if (distance <= 0) {
			return new Pair<>(0F, initialSpeed);
		}

		if (shouldStop) {
			if (initialSpeed * initialSpeed / (2 * distance) >= ACCELERATION) {
				return new Pair<>(2 * distance / initialSpeed, 0F);
			}

			final float maxSpeed = Math.min(railSpeed, (float) Math.sqrt(ACCELERATION * distance + initialSpeed * initialSpeed / 2));
			final float ticks = (2 * ACCELERATION * distance + initialSpeed * initialSpeed - 2 * initialSpeed * maxSpeed + 2 * maxSpeed * maxSpeed) / (2 * ACCELERATION * maxSpeed);
			return new Pair<>(ticks, 0F);
		} else {
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
		void renderTrainCallback(float x, float y, float z, float yaw, float pitch, String customId, TrainType trainType, boolean isEnd1Head, boolean isEnd2Head, boolean head1IsFront, float doorLeftValue, float doorRightValue, boolean opening, boolean lightsOn, Vec3d playerOffset);
	}

	@FunctionalInterface
	public interface RenderConnectionCallback {
		void renderConnectionCallback(Pos3f prevPos1, Pos3f prevPos2, Pos3f prevPos3, Pos3f prevPos4, Pos3f thisPos1, Pos3f thisPos2, Pos3f thisPos3, Pos3f thisPos4, float x, float y, float z, float yaw, TrainType trainType, boolean lightsOn, Vec3d playerOffset);
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
	public interface WriteScheduleCallback {
		void writeScheduleCallback(long platformId, float arrivalMillis, float departureMillis, TrainType trainType, int trainLength, int stopIndex, List<Long> routeIds);
	}

	@FunctionalInterface
	private interface CalculateRenderCallback {
		void calculateRenderCallback(float x, float y, float z, float yaw, float pitch, float realSpacing, boolean doorLeftOpen, boolean doorRightOpen);
	}
}