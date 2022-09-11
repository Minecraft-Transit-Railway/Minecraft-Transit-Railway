package mtr.data;

import io.netty.buffer.Unpooled;
import mtr.MTRClient;
import mtr.RegistryClient;
import mtr.client.ClientData;
import mtr.client.Config;
import mtr.client.TrainClientRegistry;
import mtr.entity.EntitySeat;
import mtr.mappings.Utilities;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.RenderDrivingOverlay;
import mtr.render.TrainRendererBase;
import mtr.sound.TrainSoundBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class TrainClient extends Train {

	public boolean isRemoved = false;

	private float clientPrevYaw;
	private boolean justMounted;
	private int previousInterval;
	private float oldSpeed;
	private double oldRailProgress;
	private float oldDoorValue;
	private float oldPercentageX;
	private float oldPercentageZ;
	private double lastSentX;
	private double lastSentY;
	private double lastSentZ;
	private float lastSentTicks;
	private boolean isSitting;
	private boolean previousShifting;

	private SpeedCallback speedCallback;
	private AnnouncementCallback announcementCallback;
	private AnnouncementCallback lightRailAnnouncementCallback;
	private Depot depot;
	private List<Long> routeIds = new ArrayList<>();

	public final TrainRendererBase trainRenderer;
	private final TrainSoundBase trainSound;

	private final Set<Runnable> trainTranslucentRenders = new HashSet<>();
	private final List<Double> offset = new ArrayList<>();
	private final Map<UUID, Float> percentagesX = new HashMap<>();
	private final Map<UUID, Float> percentagesZ = new HashMap<>();
	private final Map<UUID, Float> newPercentagesX = new HashMap<>();
	private final Map<UUID, Float> newPercentagesZ = new HashMap<>();
	private final Map<UUID, Vec3> riderPositions = new HashMap<>();

	private static final float CONNECTION_HEIGHT = 2.25F;
	private static final float CONNECTION_Z_OFFSET = 0.5F;
	private static final float CONNECTION_X_OFFSET = 0.25F;
	private static final float TRAIN_WALKING_SPEED_MULTIPLIER = 0.25F;
	private static final int TRAIN_PERCENTAGE_UPDATE_INTERVAL = 20;

	public TrainClient(FriendlyByteBuf packet) {
		super(packet);
		final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId);
		trainRenderer = trainProperties.renderer.createTrainInstance(this);
		trainSound = trainProperties.sound.createTrainInstance(this);
	}

	@Override
	protected void simulateCar(
			Level world, int ridingCar, float ticksElapsed,
			double carX, double carY, double carZ, float carYaw, float carPitch,
			double prevCarX, double prevCarY, double prevCarZ, float prevCarYaw, float prevCarPitch,
			boolean doorLeftOpen, boolean doorRightOpen, double realSpacing
	) {
		final LocalPlayer clientPlayer = Minecraft.getInstance().player;
		if (clientPlayer == null) {
			return;
		}

		final BlockPos soundPos = new BlockPos(carX, carY, carZ);
		trainSound.playAllCars(world, soundPos, ridingCar);
		if (doorLeftOpen || doorRightOpen) {
			trainSound.playAllCarsDoorOpening(world, soundPos, ridingCar);
		}

		final boolean noOffset = offset.isEmpty();
		final double newX = carX - (noOffset ? 0 : offset.get(0));
		final double newY = carY - (noOffset ? 0 : offset.get(1));
		final double newZ = carZ - (noOffset ? 0 : offset.get(2));
		riderPositions.forEach((uuid, position) -> {
			if (noOffset) {
				trainRenderer.renderRidingPlayer(uuid, position);
			} else {
				trainRenderer.renderRidingPlayer(uuid, position.subtract(offset.get(0), offset.get(1), offset.get(2)));
			}
		});

		final boolean opening = doorValue > oldDoorValue;
		trainRenderer.renderCar(ridingCar, newX, newY, newZ, carYaw, carPitch, false, doorLeftOpen ? doorValue : 0, doorRightOpen ? doorValue : 0, opening, !reversed);
		trainTranslucentRenders.add(() -> trainRenderer.renderCar(ridingCar, newX, newY, newZ, carYaw, carPitch, true, doorLeftOpen ? doorValue : 0, doorRightOpen ? doorValue : 0, opening, !reversed));

		if (ridingCar > 0) {
			final double newPrevCarX = prevCarX - (noOffset ? 0 : offset.get(0));
			final double newPrevCarY = prevCarY - (noOffset ? 0 : offset.get(1));
			final double newPrevCarZ = prevCarZ - (noOffset ? 0 : offset.get(2));

			final Vec3 prevPos0 = new Vec3(0, 0, spacing / 2D - 1).xRot(prevCarPitch).yRot(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);
			final Vec3 thisPos0 = new Vec3(0, 0, -(spacing / 2D - 1)).xRot(carPitch).yRot(carYaw).add(newX, newY, newZ);
			final Vec3 connectPos = prevPos0.add(thisPos0).scale(0.5);
			final float connectYaw = (float) Mth.atan2(thisPos0.x - prevPos0.x, thisPos0.z - prevPos0.z);
			final float connectPitch = realSpacing == 0 ? 0 : (float) asin((thisPos0.y - prevPos0.y) / realSpacing);

			for (int i = 0; i < 2; i++) {
				final double xStart = width / 2D + (i == 0 ? -1 : 0.5) * CONNECTION_X_OFFSET;
				final double zStart = spacing / 2D - (i == 0 ? 1 : 2) * CONNECTION_Z_OFFSET;

				final Vec3 prevPos1 = new Vec3(xStart, SMALL_OFFSET, zStart).xRot(prevCarPitch).yRot(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);
				final Vec3 prevPos2 = new Vec3(xStart, CONNECTION_HEIGHT + SMALL_OFFSET, zStart).xRot(prevCarPitch).yRot(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);
				final Vec3 prevPos3 = new Vec3(-xStart, CONNECTION_HEIGHT + SMALL_OFFSET, zStart).xRot(prevCarPitch).yRot(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);
				final Vec3 prevPos4 = new Vec3(-xStart, SMALL_OFFSET, zStart).xRot(prevCarPitch).yRot(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);

				final Vec3 thisPos1 = new Vec3(-xStart, SMALL_OFFSET, -zStart).xRot(carPitch).yRot(carYaw).add(newX, newY, newZ);
				final Vec3 thisPos2 = new Vec3(-xStart, CONNECTION_HEIGHT + SMALL_OFFSET, -zStart).xRot(carPitch).yRot(carYaw).add(newX, newY, newZ);
				final Vec3 thisPos3 = new Vec3(xStart, CONNECTION_HEIGHT + SMALL_OFFSET, -zStart).xRot(carPitch).yRot(carYaw).add(newX, newY, newZ);
				final Vec3 thisPos4 = new Vec3(xStart, SMALL_OFFSET, -zStart).xRot(carPitch).yRot(carYaw).add(newX, newY, newZ);

				if (i == 0) {
					trainRenderer.renderConnection(prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, connectPos.x, connectPos.y, connectPos.z, connectYaw, connectPitch);
				} else {
					trainRenderer.renderBarrier(prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, connectPos.x, connectPos.y, connectPos.z, connectYaw, connectPitch);
				}
			}
		}
	}

	@Override
	protected boolean handlePositions(Level world, Vec3[] positions, float ticksElapsed) {
		final Minecraft client = Minecraft.getInstance();
		final LocalPlayer clientPlayer = client.player;
		if (clientPlayer == null) {
			return false;
		}

		final int interval = (int) Math.floor(MTRClient.getGameTick() / TRAIN_PERCENTAGE_UPDATE_INTERVAL);
		if (ticksElapsed > 0) {
			offset.clear();
			riderPositions.clear();

			if (ridingEntities.contains(clientPlayer.getUUID())) {
				final int trainSpacing = spacing;
				final int headIndex = getIndex(0, trainSpacing, false);
				final int stopIndex = path.get(headIndex).stopIndex - 1;

				if (speedCallback != null) {
					speedCallback.speedCallback(speed * 20, stopIndex, routeIds);
				}

				if (announcementCallback != null) {
					final double targetProgress = distances.get(getPreviousStoppingIndex(headIndex)) + (trainCars + 1) * trainSpacing;
					if (oldRailProgress < targetProgress && railProgress >= targetProgress) {
						announcementCallback.announcementCallback(stopIndex, routeIds);
					}
				}

				if (lightRailAnnouncementCallback != null && (justOpening() || justMounted)) {
					lightRailAnnouncementCallback.announcementCallback(stopIndex, routeIds);
				}
			}

			final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId);
			ridingEntities.forEach(uuid -> {
				final boolean isClientPlayer = uuid.equals(clientPlayer.getUUID());
				if (!percentagesX.containsKey(uuid) || !newPercentagesX.containsKey(uuid)) {
					percentagesX.put(uuid, 0.5F);
					newPercentagesX.put(uuid, 0.5F);
				}
				if (!percentagesZ.containsKey(uuid) || !newPercentagesZ.containsKey(uuid)) {
					percentagesZ.put(uuid, 0.5F);
					newPercentagesZ.put(uuid, 0.5F);
				}

				final CalculateCarCallback calculateCarCallback = (x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender) -> {
					final Vec3 playerOffset = new Vec3(getValueFromPercentage(percentagesX.get(uuid), width), doorLeftOpenRender || doorRightOpenRender ? 0 : trainProperties.riderOffset, getValueFromPercentage(Mth.frac(percentagesZ.get(uuid)), realSpacingRender)).xRot(transportMode.hasPitch ? pitch : 0).yRot(yaw);
					ClientData.updatePlayerRidingOffset(uuid);
					riderPositions.put(uuid, playerOffset.add(x, y, z));

					if (isClientPlayer) {
						final double moveX = x + playerOffset.x;
						final double moveY = y + playerOffset.y;
						final double moveZ = z + playerOffset.z;
						final boolean movePlayer;

						if (MTRClient.isVivecraft()) {
							final Entity vehicle = clientPlayer.getVehicle();
							if (vehicle instanceof EntitySeat) {
								((EntitySeat) vehicle).setPosByTrain(moveX, moveY, moveZ);
								movePlayer = false;
							} else {
								movePlayer = true;
							}

							final float tempPercentageX = percentagesX.get(uuid);
							final boolean doorOpen = doorLeftOpenRender && tempPercentageX < 0 || doorRightOpenRender && tempPercentageX > 1;
							final boolean movedFar = Math.abs(lastSentX - moveX) > 2 || Math.abs(lastSentY - moveY) > 2 || Math.abs(lastSentZ - moveZ) > 2;

							if (doorOpen || MTRClient.getGameTick() - lastSentTicks > 60 && movedFar) {
								PacketTrainDataGuiClient.sendUpdateEntitySeatPassengerPosition(moveX, moveY, moveZ);
								lastSentX = moveX;
								lastSentY = moveY;
								lastSentZ = moveZ;
								lastSentTicks = MTRClient.getGameTick();
							}
						} else {
							movePlayer = true;
						}

						if (movePlayer) {
							clientPlayer.fallDistance = 0;
							clientPlayer.setDeltaMovement(0, 0, 0);
							clientPlayer.setSpeed(0);
							if (MTRClient.getGameTick() > 40) {
								clientPlayer.absMoveTo(moveX, moveY, moveZ);
							}
						}

						final boolean isShifting = clientPlayer.isShiftKeyDown();
						if (Config.shiftToToggleSitting() && !MTRClient.isVivecraft()) {
							if (isShifting && !previousShifting) {
								isSitting = !isSitting;
							}
							clientPlayer.setPose(isSitting && client.gameRenderer.getMainCamera().isDetached() ? Pose.CROUCHING : Pose.STANDING);
						}

						if (speed > 0) {
							if (doorValue == 0) {
								float angleDifference = (float) Math.toDegrees(clientPrevYaw - yaw);
								if (angleDifference > 180) {
									angleDifference -= 360;
								} else if (angleDifference < -180) {
									angleDifference += 360;
								}
								Utilities.incrementYaw(clientPlayer, angleDifference);
							}
							offset.add(x);
							offset.add(y);
							offset.add(z);
							offset.add(playerOffset.x);
							offset.add(playerOffset.y + (MTRClient.isVivecraft() ? 0 : clientPlayer.getEyeHeight()));
							offset.add(playerOffset.z);
						}

						clientPrevYaw = yaw;
						previousShifting = isShifting;
					}
				};

				final int currentRidingCar = Mth.clamp((int) Math.floor(percentagesZ.get(uuid)), 0, positions.length - 2);
				calculateCar(world, positions, currentRidingCar, 0, (x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender) -> {
					final boolean noGangwayConnection = !trainProperties.hasGangwayConnection;
					final float speedMultiplier = ticksElapsed * TRAIN_WALKING_SPEED_MULTIPLIER;
					final float newPercentageX;
					final float newPercentageZ;

					if (isClientPlayer) {
						final Vec3 movement = new Vec3(Math.abs(clientPlayer.xxa) > 0.5 ? Math.copySign(speedMultiplier, clientPlayer.xxa) : 0, 0, Math.abs(clientPlayer.zza) > 0.5 ? Math.copySign(speedMultiplier, clientPlayer.zza) : 0).yRot((float) -Math.toRadians(Utilities.getYaw(clientPlayer)) - yaw);
						final float tempPercentageX = percentagesX.get(uuid) + (float) movement.x / width;
						final float tempPercentageZ = percentagesZ.get(uuid) + (float) (realSpacingRender == 0 ? 0 : movement.z / realSpacingRender);
						newPercentageX = Mth.clamp(tempPercentageX, doorLeftOpenRender ? -3 : 0, doorRightOpenRender ? 4 : 1);
						newPercentageZ = Mth.clamp(tempPercentageZ, (noGangwayConnection ? currentRidingCar + 0.05F : 0) + 0.01F, (noGangwayConnection ? currentRidingCar + 0.95F : trainCars) - 0.01F);

						if (interval != previousInterval && (newPercentageX != oldPercentageX || newPercentageZ != oldPercentageZ)) {
							final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
							packet.writeLong(id);
							packet.writeFloat(newPercentageX);
							packet.writeFloat(newPercentageZ);
							packet.writeUUID(uuid);
							RegistryClient.sendToServer(PACKET_UPDATE_TRAIN_PASSENGER_POSITION, packet);
							oldPercentageX = newPercentageX;
							oldPercentageZ = newPercentageZ;
						}
					} else {
						final double distanceX = getValueFromPercentage(newPercentagesX.get(uuid), width) - getValueFromPercentage(percentagesX.get(uuid), width);
						final double distanceZ = getValueFromPercentage(newPercentagesZ.get(uuid), realSpacingRender) - getValueFromPercentage(percentagesZ.get(uuid), realSpacingRender);
						final double manhattanDistance = Math.abs(distanceX + distanceZ);
						if (manhattanDistance == 0 || distanceX * distanceX + distanceZ * distanceZ < speedMultiplier * speedMultiplier) {
							newPercentageX = newPercentagesX.get(uuid);
							newPercentageZ = newPercentagesZ.get(uuid);
						} else {
							newPercentageX = percentagesX.get(uuid) + (float) (distanceX / manhattanDistance * speedMultiplier / width);
							newPercentageZ = percentagesZ.get(uuid) + (float) (realSpacingRender == 0 ? 0 : distanceZ / manhattanDistance * speedMultiplier / realSpacingRender);
						}
					}

					percentagesX.put(uuid, newPercentageX);
					percentagesZ.put(uuid, newPercentageZ);

					final int newRidingCar = Mth.clamp((int) Math.floor(newPercentageZ), 0, positions.length - 2);
					if (currentRidingCar == newRidingCar) {
						calculateCarCallback.calculateCarCallback(x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender);
					} else {
						calculateCar(world, positions, newRidingCar, 0, calculateCarCallback);
					}
				});
			});
		}

		previousInterval = interval;
		justMounted = false;

		final Entity camera = client.cameraEntity;
		final Vec3 cameraPos = camera == null ? Vec3.ZERO : camera.position();
		double nearestDistance = Double.POSITIVE_INFINITY;
		int nearestCar = 0;
		for (int i = 0; i < trainCars; i++) {
			final double checkDistance = cameraPos.distanceToSqr(positions[i]);
			if (checkDistance < nearestDistance) {
				nearestCar = i;
				nearestDistance = checkDistance;
			}
		}
		final BlockPos soundPos = new BlockPos(positions[nearestCar].x, positions[nearestCar].y, positions[nearestCar].z);
		trainSound.playNearestCar(world, soundPos, nearestCar);

		return true;
	}

	@Override
	protected boolean canDeploy(Depot depot) {
		return false;
	}

	@Override
	protected boolean isRailBlocked(int checkIndex) {
		return false;
	}

	@Override
	protected boolean skipScanBlocks(Level world, double trainX, double trainY, double trainZ) {
		return false;
	}

	@Override
	protected boolean openDoors(Level world, Block block, BlockPos checkPos, int dwellTicks) {
		return true;
	}

	@Override
	protected float getModelZOffset() {
		return baseTrainType.startsWith("london_underground_199") || trainId.startsWith("london_underground_199") ? 0.5F : 0; // TODO integrate this into TrainClientRegistry
	}

	@Override
	protected double asin(double value) {
		return Math.asin(value);
	}

	public void simulateTrain(Level world, float ticksElapsed, SpeedCallback speedCallback, AnnouncementCallback announcementCallback, AnnouncementCallback lightRailAnnouncementCallback) {
		trainTranslucentRenders.clear();
		this.speedCallback = speedCallback;
		this.announcementCallback = announcementCallback;
		this.lightRailAnnouncementCallback = lightRailAnnouncementCallback;
		oldSpeed = speed;
		oldRailProgress = railProgress;
		oldDoorValue = doorValue;

		simulateTrain(world, ticksElapsed, null);

		if (depot == null || routeIds.isEmpty()) {
			final Siding siding = ClientData.DATA_CACHE.sidingIdMap.get(sidingId);
			depot = siding == null ? null : ClientData.DATA_CACHE.sidingIdToDepot.get(siding.id);
			routeIds = depot == null ? new ArrayList<>() : depot.routeIds;
			if (depot != null) {
				depot.lastDeployedMillis = System.currentTimeMillis();
			}
		}

		final LocalPlayer player = Minecraft.getInstance().player;
		if (isManual && Train.isHoldingKey(player) && ridingEntities.contains(player.getUUID())) {
			final int stopIndex = path.get(getIndex(0, spacing, false)).stopIndex - 1;
			RenderDrivingOverlay.setData(manualAccelerationSign, doorValue, speed * 20, stopIndex, routeIds);
		}
	}

	public void renderTranslucent() {
		trainTranslucentRenders.forEach(Runnable::run);
		trainTranslucentRenders.clear();
	}

	public Vec3 getViewOffset() {
		return offset.isEmpty() ? null : new Vec3(offset.get(3), offset.get(4), offset.get(5));
	}

	public void startRidingClient(UUID uuid, float percentageX, float percentageZ) {
		final LocalPlayer player = Minecraft.getInstance().player;
		if (player != null && player.getUUID().equals(uuid)) {
			justMounted = true;
			isSitting = false;
		}
		ridingEntities.add(uuid);
		percentagesX.put(uuid, percentageX);
		percentagesZ.put(uuid, percentageZ);
		newPercentagesX.put(uuid, percentageX);
		newPercentagesZ.put(uuid, percentageZ);
	}

	public void updateRiderPercentages(UUID uuid, float percentageX, float percentageZ) {
		newPercentagesX.put(uuid, percentageX);
		newPercentagesZ.put(uuid, percentageZ);
	}

	public void copyFromTrain(Train train) {
		path.clear();
		distances.clear();
		ridingEntities.clear();

		path.addAll(train.path);
		distances.addAll(train.distances);
		ridingEntities.addAll(train.ridingEntities);

		speed = train.speed;
		railProgress = train.railProgress;

		stopCounter = train.stopCounter;
		nextStoppingIndex = train.nextStoppingIndex;
		reversed = train.reversed;
		isCurrentlyManual = train.isCurrentlyManual;
		isOnRoute = train.isOnRoute;
		manualAccelerationSign = train.manualAccelerationSign;
		doorOpen = train.doorOpen;
	}

	public float getSpeed() {
		return speed;
	}

	public final float speedChange() {
		return speed - oldSpeed;
	}

	public boolean justOpening() {
		return oldDoorValue == 0 && doorValue > 0;
	}

	public boolean justClosing(float doorCloseTime) {
		return oldDoorValue >= doorCloseTime && doorValue < doorCloseTime;
	}

	private int getPreviousStoppingIndex(int headIndex) {
		for (int i = headIndex; i >= 0; i--) {
			if (path.get(i).dwellTime > 0 && path.get(i).rail.railType == RailType.PLATFORM) {
				return i;
			}
		}
		return 0;
	}

	@FunctionalInterface
	public interface SpeedCallback {
		void speedCallback(float speed, int stopIndex, List<Long> routeIds);
	}

	@FunctionalInterface
	public interface AnnouncementCallback {
		void announcementCallback(int stopIndex, List<Long> routeIds);
	}
}
