package mtr.data;

import mtr.client.ClientData;
import mtr.client.TrainClientRegistry;
import mtr.mappings.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrainClient extends Train {

	private float clientPrevYaw;
	private boolean justMounted;
	private float percentageX = 0.5F;
	private float percentageZ = 0.5F;

	private RenderTrainCallback renderTrainCallback;
	private RenderConnectionCallback renderConnectionCallback;
	private SpeedCallback speedCallback;
	private AnnouncementCallback announcementCallback;
	private AnnouncementCallback lightRailAnnouncementCallback;

	private final Set<Runnable> trainTranslucentRenders = new HashSet<>();
	private final List<Long> routeIds;
	private final List<Double> offset = new ArrayList<>();

	private static final float CONNECTION_HEIGHT = 2.25F;
	private static final float CONNECTION_Z_OFFSET = 0.5F;
	private static final float CONNECTION_X_OFFSET = 0.25F;

	public TrainClient(FriendlyByteBuf packet) {
		super(packet);
		final Siding siding = ClientData.DATA_CACHE.sidingIdMap.get(sidingId);
		final Depot depot = siding == null ? null : ClientData.DATA_CACHE.sidingIdToDepot.get(siding.id);
		routeIds = depot == null ? new ArrayList<>() : depot.routeIds;
	}

	@Override
	protected void simulateCar(
			Level world, int ridingCar, float ticksElapsed,
			double carX, double carY, double carZ, float carYaw, float carPitch,
			double prevCarX, double prevCarY, double prevCarZ, float prevCarYaw, float prevCarPitch,
			boolean doorLeftOpen, boolean doorRightOpen, double realSpacing,
			float doorValueRaw, float oldSpeed, float oldDoorValue, float oldRailProgress
	) {
		final LocalPlayer clientPlayer = Minecraft.getInstance().player;
		if (clientPlayer == null) {
			return;
		}
		final float doorValue = Math.abs(doorValueRaw);
		final boolean opening = doorValueRaw > 0;

		final BlockPos soundPos = new BlockPos(carX, carY, carZ);
		final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId, baseTrainType);
		trainProperties.playSpeedSoundEffect(world, soundPos, oldSpeed, speed);
		if (doorLeftOpen || doorRightOpen) {
			trainProperties.playDoorSoundEffect(world, soundPos, oldDoorValue, doorValue);
		}

		final boolean noOffset = offset.isEmpty();
		final double newX = carX - (noOffset ? 0 : offset.get(0));
		final double newY = carY - (noOffset ? 0 : offset.get(1));
		final double newZ = carZ - (noOffset ? 0 : offset.get(2));
		final Vec3 playerOffset = noOffset ? null : new Vec3(offset.get(3), offset.get(4), offset.get(5));

		if (renderTrainCallback != null) {
			renderTrainCallback.renderTrainCallback(newX, newY, newZ, carYaw, carPitch, trainId, baseTrainType, ridingCar == 0, ridingCar == trainCars - 1, !reversed, doorLeftOpen ? doorValue : 0, doorRightOpen ? doorValue : 0, opening, isOnRoute, false, playerOffset);
		}

		if (renderConnectionCallback != null && ridingCar > 0 && trainProperties.baseTrainType.hasGangwayConnection) {
			final double newPrevCarX = prevCarX - (noOffset ? 0 : offset.get(0));
			final double newPrevCarY = prevCarY - (noOffset ? 0 : offset.get(1));
			final double newPrevCarZ = prevCarZ - (noOffset ? 0 : offset.get(2));

			final float xStart = baseTrainType.width / 2F - CONNECTION_X_OFFSET;
			final float zStart = baseTrainType.getSpacing() / 2F - CONNECTION_Z_OFFSET;

			final Vec3 prevPos1 = new Vec3(xStart, SMALL_OFFSET, zStart).xRot(prevCarPitch).yRot(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);
			final Vec3 prevPos2 = new Vec3(xStart, CONNECTION_HEIGHT + SMALL_OFFSET, zStart).xRot(prevCarPitch).yRot(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);
			final Vec3 prevPos3 = new Vec3(-xStart, CONNECTION_HEIGHT + SMALL_OFFSET, zStart).xRot(prevCarPitch).yRot(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);
			final Vec3 prevPos4 = new Vec3(-xStart, SMALL_OFFSET, zStart).xRot(prevCarPitch).yRot(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);

			final Vec3 thisPos1 = new Vec3(-xStart, SMALL_OFFSET, -zStart).xRot(carPitch).yRot(carYaw).add(newX, newY, newZ);
			final Vec3 thisPos2 = new Vec3(-xStart, CONNECTION_HEIGHT + SMALL_OFFSET, -zStart).xRot(carPitch).yRot(carYaw).add(newX, newY, newZ);
			final Vec3 thisPos3 = new Vec3(xStart, CONNECTION_HEIGHT + SMALL_OFFSET, -zStart).xRot(carPitch).yRot(carYaw).add(newX, newY, newZ);
			final Vec3 thisPos4 = new Vec3(xStart, SMALL_OFFSET, -zStart).xRot(carPitch).yRot(carYaw).add(newX, newY, newZ);

			renderConnectionCallback.renderConnectionCallback(prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, newX, newY, newZ, carYaw, trainId, baseTrainType, isOnRoute, playerOffset);
		}
	}

	@Override
	protected boolean handlePositions(Level world, Vec3[] positions, float ticksElapsed, float doorValueRaw, float oldDoorValue, float oldRailProgress) {
		final LocalPlayer clientPlayer = Minecraft.getInstance().player;
		if (clientPlayer == null) {
			return false;
		}

		if (ticksElapsed > 0) {
			offset.clear();

			if (ridingEntities.contains(clientPlayer.getUUID())) {
				if (clientPlayer.isShiftKeyDown()) {
					ridingEntities.remove(clientPlayer.getUUID());
				}

				final int trainSpacing = baseTrainType.getSpacing();
				final int headIndex = getIndex(0, trainSpacing, false);
				final int stopIndex = path.get(headIndex).stopIndex - 1;

				if (speedCallback != null) {
					speedCallback.speedCallback(speed * 20, stopIndex, routeIds);
				}

				if (announcementCallback != null) {
					float targetProgress = distances.get(getPreviousStoppingIndex(headIndex)) + (trainCars + 1) * trainSpacing;
					if (oldRailProgress < targetProgress && railProgress >= targetProgress) {
						announcementCallback.announcementCallback(stopIndex, routeIds);
					}
				}

				if (lightRailAnnouncementCallback != null && (oldDoorValue <= 0 && doorValueRaw != 0 || justMounted)) {
					lightRailAnnouncementCallback.announcementCallback(stopIndex, routeIds);
				}

				final CalculateCarCallback calculateCarCallback = (x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender) -> {
					clientPlayer.fallDistance = 0;
					clientPlayer.setDeltaMovement(0, 0, 0);
					clientPlayer.setSpeed(0);
					final Vec3 playerOffset = new Vec3(getValueFromPercentage(percentageX, baseTrainType.width), doorLeftOpenRender || doorRightOpenRender ? 0 : baseTrainType.riderOffset, getValueFromPercentage(Mth.frac(percentageZ), realSpacingRender)).xRot(pitch).yRot(yaw);
					clientPlayer.absMoveTo(x + playerOffset.x, y + playerOffset.y, z + playerOffset.z);
					if (speed > 0) {
						Utilities.incrementYaw(clientPlayer, -(float) Math.toDegrees(yaw - clientPrevYaw));
						offset.add(x);
						offset.add(y);
						offset.add(z);
						offset.add(playerOffset.x);
						offset.add(playerOffset.y + clientPlayer.getEyeHeight());
						offset.add(playerOffset.z);
					}
					clientPrevYaw = yaw;
				};

				final int currentRidingCar = (int) Math.floor(percentageZ);
				final float doorValue = Math.abs(doorValueRaw);
				calculateCar(world, positions, currentRidingCar, doorValue, 0, (x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender) -> {
					final boolean hasGangwayConnection = baseTrainType.hasGangwayConnection;
					final Vec3 movement = new Vec3(clientPlayer.xxa * ticksElapsed / 4, 0, clientPlayer.zza * ticksElapsed / 4).yRot((float) -Math.toRadians(Utilities.getYaw(clientPlayer)) - yaw);

					percentageX += movement.x / baseTrainType.width;
					percentageZ += realSpacingRender == 0 ? 0 : movement.z / realSpacingRender;
					percentageX = Mth.clamp(percentageX, doorLeftOpenRender ? -3 : 0, doorRightOpenRender ? 4 : 1);
					percentageZ = Mth.clamp(percentageZ, (hasGangwayConnection ? 0 : currentRidingCar + 0.05F) + 0.01F, (hasGangwayConnection ? trainCars : currentRidingCar + 0.95F) - 0.01F);

					final int newRidingCar = (int) Math.floor(percentageZ);
					if (currentRidingCar == newRidingCar) {
						calculateCarCallback.calculateCarCallback(x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender);
					} else {
						calculateCar(world, positions, newRidingCar, Math.abs(doorValueRaw), 0, calculateCarCallback);
					}
				});
			}
		}

		justMounted = false;
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
	protected boolean openDoors(Level world, Block block, BlockPos checkPos, float doorValue, int dwellTicks) {
		return true;
	}

	@Override
	protected double asin(double value) {
		return Math.asin(value);
	}

	public void simulateTrain(Level world, float ticksElapsed, RenderTrainCallback renderTrainCallback, RenderConnectionCallback renderConnectionCallback, SpeedCallback speedCallback, AnnouncementCallback announcementCallback, AnnouncementCallback lightRailAnnouncementCallback) {
		trainTranslucentRenders.clear();
		this.renderTrainCallback = (x, y, z, yaw, pitch, customId, baseTrainType1, isEnd1Head, isEnd2Head, head1IsFront, doorLeftValue, doorRightValue, opening, lightsOn, isTranslucent, playerOffset) -> {
			renderTrainCallback.renderTrainCallback(x, y, z, yaw, pitch, customId, baseTrainType1, isEnd1Head, isEnd2Head, head1IsFront, doorLeftValue, doorRightValue, opening, lightsOn, false, playerOffset);
			trainTranslucentRenders.add(() -> renderTrainCallback.renderTrainCallback(x, y, z, yaw, pitch, customId, baseTrainType1, isEnd1Head, isEnd2Head, head1IsFront, doorLeftValue, doorRightValue, opening, lightsOn, true, playerOffset));
		};
		this.renderConnectionCallback = renderConnectionCallback;
		this.speedCallback = speedCallback;
		this.announcementCallback = announcementCallback;
		this.lightRailAnnouncementCallback = lightRailAnnouncementCallback;
		simulateTrain(world, ticksElapsed, null);
	}

	public void renderTranslucent() {
		trainTranslucentRenders.forEach(Runnable::run);
		trainTranslucentRenders.clear();
	}

	public void updateClientPercentages(LocalPlayer player, float percentageX, float percentageZ) {
		if (player != null) {
			justMounted = true;
			ridingEntities.add(player.getUUID());
			this.percentageX = percentageX;
			this.percentageZ = percentageZ;
		}
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
		isOnRoute = train.isOnRoute;
	}

	public float getSpeed() {
		return speed;
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
	public interface RenderTrainCallback {
		void renderTrainCallback(double x, double y, double z, float yaw, float pitch, String customId, TrainType baseTrainType, boolean isEnd1Head, boolean isEnd2Head, boolean head1IsFront, float doorLeftValue, float doorRightValue, boolean opening, boolean lightsOn, boolean isTranslucent, Vec3 playerOffset);
	}

	@FunctionalInterface
	public interface RenderConnectionCallback {
		void renderConnectionCallback(Vec3 prevPos1, Vec3 prevPos2, Vec3 prevPos3, Vec3 prevPos4, Vec3 thisPos1, Vec3 thisPos2, Vec3 thisPos3, Vec3 thisPos4, double x, double y, double z, float yaw, String trainId, TrainType baseTrainType, boolean lightsOn, Vec3 playerOffset);
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
