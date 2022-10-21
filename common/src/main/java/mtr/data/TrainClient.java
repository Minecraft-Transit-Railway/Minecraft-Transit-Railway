package mtr.data;

import mtr.MTRClient;
import mtr.client.ClientData;
import mtr.client.Config;
import mtr.client.TrainClientRegistry;
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

public class TrainClient extends Train implements IGui {

	public boolean isRemoved = false;
	private boolean justMounted;
	private float oldSpeed;
	private double oldRailProgress;
	private float oldDoorValue;
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
	private final VehicleRidingClient vehicleRidingClient = new VehicleRidingClient(ridingEntities, PACKET_UPDATE_TRAIN_PASSENGER_POSITION);

	private static final float CONNECTION_HEIGHT = 2.25F;
	private static final float CONNECTION_Z_OFFSET = 0.5F;
	private static final float CONNECTION_X_OFFSET = 0.25F;

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

		final Vec3 offset = vehicleRidingClient.renderPlayerAndGetOffset();
		final double newX = carX - offset.x;
		final double newY = carY - offset.y;
		final double newZ = carZ - offset.z;

		final boolean opening = doorValue > oldDoorValue;
		final int stopIndex = path.get(getIndex(0, spacing, false)).stopIndex - 1;
		trainRenderer.renderCar(ridingCar, newX, newY, newZ, carYaw, carPitch, false, doorLeftOpen ? doorValue : 0, doorRightOpen ? doorValue : 0, opening, !reversed, stopIndex, routeIds);
		trainTranslucentRenders.add(() -> trainRenderer.renderCar(ridingCar, newX, newY, newZ, carYaw, carPitch, true, doorLeftOpen ? doorValue : 0, doorRightOpen ? doorValue : 0, opening, !reversed, stopIndex, routeIds));

		if (ridingCar > 0) {
			final double newPrevCarX = prevCarX - offset.x;
			final double newPrevCarY = prevCarY - offset.y;
			final double newPrevCarZ = prevCarZ - offset.z;

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

		vehicleRidingClient.begin();

		if (ticksElapsed > 0) {
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
			vehicleRidingClient.movePlayer(uuid -> {
				final CalculateCarCallback calculateCarCallback = (x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender) -> vehicleRidingClient.setOffsets(uuid, x, y, z, yaw, pitch, realSpacingRender, width, doorLeftOpenRender, doorRightOpenRender, transportMode.hasPitch, trainProperties.riderOffset, speed > 0, doorValue == 0, () -> {
					final boolean isShifting = clientPlayer.isShiftKeyDown();
					if (Config.shiftToToggleSitting() && !MTRClient.isVivecraft()) {
						if (isShifting && !previousShifting) {
							isSitting = !isSitting;
						}
						clientPlayer.setPose(isSitting && !client.gameRenderer.getMainCamera().isDetached() ? Pose.CROUCHING : Pose.STANDING);
					}
					previousShifting = isShifting;
				});

				final int currentRidingCar = Mth.clamp((int) Math.floor(vehicleRidingClient.getPercentageZ(uuid)), 0, positions.length - 2);
				calculateCar(world, positions, currentRidingCar, 0, (x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender) -> {
					vehicleRidingClient.moveSelf(id, uuid, realSpacingRender, width, yaw, currentRidingCar, trainCars, doorLeftOpenRender, doorRightOpenRender, !trainProperties.hasGangwayConnection, ticksElapsed);

					final int newRidingCar = Mth.clamp((int) Math.floor(vehicleRidingClient.getPercentageZ(uuid)), 0, positions.length - 2);
					if (currentRidingCar == newRidingCar) {
						calculateCarCallback.calculateCarCallback(x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender);
					} else {
						calculateCar(world, positions, newRidingCar, 0, calculateCarCallback);
					}
				});
			});
		}

		vehicleRidingClient.end();
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
		return vehicleRidingClient.getViewOffset();
	}

	public void startRidingClient(UUID uuid, float percentageX, float percentageZ) {
		final LocalPlayer player = Minecraft.getInstance().player;
		if (player != null && player.getUUID().equals(uuid)) {
			justMounted = true;
			isSitting = false;
		}
		vehicleRidingClient.startRiding(uuid, percentageX, percentageZ);
	}

	public void updateRiderPercentages(UUID uuid, float percentageX, float percentageZ) {
		vehicleRidingClient.updateRiderPercentages(uuid, percentageX, percentageZ);
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
