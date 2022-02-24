package mtr.data;

import mtr.client.ClientData;
import mtr.client.TrainClientRegistry;
import mtr.entity.EntitySeat;
import mtr.mappings.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
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

		if (renderTrainCallback != null) {
			renderTrainCallback.renderTrainCallback(newX, newY, newZ, carYaw, carPitch, trainId, baseTrainType, ridingCar == 0, ridingCar == trainCars - 1, !reversed, doorLeftOpen ? doorValue : 0, doorRightOpen ? doorValue : 0, opening, isOnRoute, false, noOffset);
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

			renderConnectionCallback.renderConnectionCallback(prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, newX, newY, newZ, carYaw, trainId, baseTrainType, isOnRoute, noOffset);
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
				final int trainSpacing = baseTrainType.getSpacing();
				final int headIndex = getIndex(0, trainSpacing, false);
				final int stopIndex = path.get(headIndex).stopIndex - 1;
				final Entity vehicle = clientPlayer.getVehicle();

				if (vehicle instanceof EntitySeat) {
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

					final EntitySeat seat = (EntitySeat) vehicle;
					final float testRailProgress = seat.getClientRailProgress();
					if (testRailProgress > 0 && Math.abs(testRailProgress - railProgress) > 10) {
						railProgress = testRailProgress;
					}

					calculateCar(world, positions, (int) Math.floor(speed == 0 ? seat.getClientPercentageZ() : seat.getInterpolatedPercentageZ()), 0, 0, (x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender) -> {
						if (speed > 0) {
							Utilities.incrementYaw(clientPlayer, -(float) Math.toDegrees(yaw - clientPrevYaw));
							final Vec3 playerOffset2 = new Vec3(getValueFromPercentage(seat.getInterpolatedPercentageX(), baseTrainType.width), baseTrainType.riderOffset, getValueFromPercentage(Mth.frac(seat.getInterpolatedPercentageZ()), realSpacingRender)).xRot(pitch).yRot(yaw).add(x, y, z);
							offset.add(playerOffset2.x);
							offset.add(playerOffset2.y);
							offset.add(playerOffset2.z);
						}

						clientPrevYaw = yaw;
					});
				} else if (speed > 0) {
					ridingEntities.remove(clientPlayer.getUUID());
				}
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
		this.renderTrainCallback = (x, y, z, yaw, pitch, customId, baseTrainType1, isEnd1Head, isEnd2Head, head1IsFront, doorLeftValue, doorRightValue, opening, lightsOn, isTranslucent, noOffset) -> {
			renderTrainCallback.renderTrainCallback(x, y, z, yaw, pitch, customId, baseTrainType1, isEnd1Head, isEnd2Head, head1IsFront, doorLeftValue, doorRightValue, opening, lightsOn, false, noOffset);
			trainTranslucentRenders.add(() -> renderTrainCallback.renderTrainCallback(x, y, z, yaw, pitch, customId, baseTrainType1, isEnd1Head, isEnd2Head, head1IsFront, doorLeftValue, doorRightValue, opening, lightsOn, true, noOffset));
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

	public void updateClientPercentages(LocalPlayer player) {
		if (player != null) {
			justMounted = true;
			ridingEntities.add(player.getUUID());
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
		void renderTrainCallback(double x, double y, double z, float yaw, float pitch, String customId, TrainType baseTrainType, boolean isEnd1Head, boolean isEnd2Head, boolean head1IsFront, float doorLeftValue, float doorRightValue, boolean opening, boolean lightsOn, boolean isTranslucent, boolean noOffset);
	}

	@FunctionalInterface
	public interface RenderConnectionCallback {
		void renderConnectionCallback(Vec3 prevPos1, Vec3 prevPos2, Vec3 prevPos3, Vec3 prevPos4, Vec3 thisPos1, Vec3 thisPos2, Vec3 thisPos3, Vec3 thisPos4, double x, double y, double z, float yaw, String trainId, TrainType baseTrainType, boolean lightsOn, boolean noOffset);
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
