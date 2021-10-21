package mtr.data;

import mtr.gui.ClientData;
import mtr.model.TrainClientRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TrainClient extends Train {

	private float clientPercentageX;
	private float clientPercentageZ;
	private float clientPrevYaw;
	private boolean justMounted;

	private RenderTrainCallback renderTrainCallback;
	private RenderConnectionCallback renderConnectionCallback;
	private SpeedCallback speedCallback;
	private AnnouncementCallback announcementCallback;
	private AnnouncementCallback lightRailAnnouncementCallback;

	private final List<Long> routeIds;
	private final List<Double> offset = new ArrayList<>();

	private static final float CONNECTION_HEIGHT = 2.25F;
	private static final float CONNECTION_Z_OFFSET = 0.5F;
	private static final float CONNECTION_X_OFFSET = 0.25F;

	public TrainClient(PacketByteBuf packet) {
		super(packet);
		final Siding siding = ClientData.DATA_CACHE.sidingIdMap.get(sidingId);
		final Depot depot = siding == null ? null : ClientData.DATA_CACHE.sidingIdToDepot.get(siding.id);
		routeIds = depot == null ? new ArrayList<>() : depot.routeIds;
	}

	@Override
	protected void simulateCar(
			World world, int ridingCar, float ticksElapsed,
			double carX, double carY, double carZ, float carYaw, float carPitch,
			double prevCarX, double prevCarY, double prevCarZ, float prevCarYaw, float prevCarPitch,
			boolean doorLeftOpen, boolean doorRightOpen, double realSpacing,
			float doorValueRaw, float oldSpeed, float oldDoorValue, float oldRailProgress
	) {
		final ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
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

		final double newX = carX - (offset.isEmpty() ? 0 : offset.get(0));
		final double newY = carY - (offset.isEmpty() ? 0 : offset.get(1));
		final double newZ = carZ - (offset.isEmpty() ? 0 : offset.get(2));
		final Vec3d playerOffset = offset.isEmpty() ? null : new Vec3d(offset.get(3), offset.get(4), offset.get(5));

		if (renderTrainCallback != null) {
			renderTrainCallback.renderTrainCallback(newX, newY, newZ, carYaw, carPitch, trainId, baseTrainType, ridingCar == 0, ridingCar == trainCars - 1, !reversed, doorLeftOpen ? doorValue : 0, doorRightOpen ? doorValue : 0, opening, isOnRoute, playerOffset);
		}

		if (renderConnectionCallback != null && ridingCar > 0 && TrainClientRegistry.getTrainProperties(trainId, baseTrainType).hasGangwayConnection) {
			final double newPrevCarX = prevCarX - (offset.isEmpty() ? 0 : offset.get(0));
			final double newPrevCarY = prevCarY - (offset.isEmpty() ? 0 : offset.get(1));
			final double newPrevCarZ = prevCarZ - (offset.isEmpty() ? 0 : offset.get(2));

			final float xStart = baseTrainType.width / 2F - CONNECTION_X_OFFSET;
			final float zStart = baseTrainType.getSpacing() / 2F - CONNECTION_Z_OFFSET;

			final Vec3d prevPos1 = new Vec3d(xStart, SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);
			final Vec3d prevPos2 = new Vec3d(xStart, CONNECTION_HEIGHT + SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);
			final Vec3d prevPos3 = new Vec3d(-xStart, CONNECTION_HEIGHT + SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);
			final Vec3d prevPos4 = new Vec3d(-xStart, SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);

			final Vec3d thisPos1 = new Vec3d(-xStart, SMALL_OFFSET, -zStart).rotateX(carPitch).rotateY(carYaw).add(newX, newY, newZ);
			final Vec3d thisPos2 = new Vec3d(-xStart, CONNECTION_HEIGHT + SMALL_OFFSET, -zStart).rotateX(carPitch).rotateY(carYaw).add(newX, newY, newZ);
			final Vec3d thisPos3 = new Vec3d(xStart, CONNECTION_HEIGHT + SMALL_OFFSET, -zStart).rotateX(carPitch).rotateY(carYaw).add(newX, newY, newZ);
			final Vec3d thisPos4 = new Vec3d(xStart, SMALL_OFFSET, -zStart).rotateX(carPitch).rotateY(carYaw).add(newX, newY, newZ);

			renderConnectionCallback.renderConnectionCallback(prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, newX, newY, newZ, carYaw, trainId, baseTrainType, isOnRoute, playerOffset);
		}
		if (renderConnectionCallback != null && ridingCar > 0 && TrainClientRegistry.getTrainProperties(trainId, baseTrainType).trainBarrierOption) {
			final double newPrevCarX = prevCarX - (offset.isEmpty() ? 0 : offset.get(0));
			final double newPrevCarY = prevCarY - (offset.isEmpty() ? 0 : offset.get(1));
			final double newPrevCarZ = prevCarZ - (offset.isEmpty() ? 0 : offset.get(2));

			final float xStart = baseTrainType.width / 1.5F - CONNECTION_X_OFFSET;
			final float zStart = baseTrainType.getSpacing() / 2F - CONNECTION_Z_OFFSET;

			final Vec3d prevPos1 = new Vec3d(xStart + 0.20f, SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);
			final Vec3d prevPos2 = new Vec3d(xStart + 0.20f, CONNECTION_HEIGHT - 0.5f + SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);
			final Vec3d prevPos3 = new Vec3d(-xStart - 0.20f, CONNECTION_HEIGHT - 0.5f + SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);
			final Vec3d prevPos4 = new Vec3d(-xStart - 0.20f, SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(newPrevCarX, newPrevCarY, newPrevCarZ);

			final Vec3d thisPos1 = new Vec3d(-xStart - 0.20f, SMALL_OFFSET, -zStart).rotateX(carPitch).rotateY(carYaw).add(newX, newY, newZ);
			final Vec3d thisPos2 = new Vec3d(-xStart - 0.20f, CONNECTION_HEIGHT - 0.5f + SMALL_OFFSET, -zStart).rotateX(carPitch).rotateY(carYaw).add(newX, newY, newZ);
			final Vec3d thisPos3 = new Vec3d(xStart + 0.20f, CONNECTION_HEIGHT - 0.5f + SMALL_OFFSET, -zStart).rotateX(carPitch).rotateY(carYaw).add(newX, newY, newZ);
			final Vec3d thisPos4 = new Vec3d(xStart + 0.20f, SMALL_OFFSET, -zStart).rotateX(carPitch).rotateY(carYaw).add(newX, newY, newZ);

			renderConnectionCallback.renderConnectionCallback(prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, newX, newY, newZ, carYaw, trainId, baseTrainType, isOnRoute, playerOffset);
		}

	}

	@Override
	protected void handlePositions(World world, Vec3d[] positions, float ticksElapsed, float doorValueRaw, float oldDoorValue, float oldRailProgress) {
		offset.clear();
		final ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
		if (clientPlayer == null) {
			return;
		}

		final float trainSpacing = baseTrainType.getSpacing();
		final int headIndex = getIndex(0, trainSpacing, false);
		final int stopIndex = path.get(headIndex).stopIndex - 1;

		if (ridingEntities.contains(clientPlayer.getUuid())) {
			if (speedCallback != null) {
				speedCallback.speedCallback(speed * 20, stopIndex, routeIds);
			}

			if (announcementCallback != null) {
				float targetProgress = distances.get(getPreviousStoppingIndex(headIndex)) + (trainCars + 1) * trainSpacing;
				if (oldRailProgress < targetProgress && railProgress >= targetProgress) {
					announcementCallback.announcementCallback(stopIndex, routeIds);
				}
			}

			final float doorValue = Math.abs(doorValueRaw);
			if (lightRailAnnouncementCallback != null && (oldDoorValue <= 0 && doorValueRaw != 0 || justMounted)) {
				lightRailAnnouncementCallback.announcementCallback(stopIndex, routeIds);
			}

			final CalculateCarCallback moveClient = (x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender) -> {
				clientPlayer.fallDistance = 0;
				clientPlayer.setVelocity(0, 0, 0);
				final Vec3d playerOffset = new Vec3d(getValueFromPercentage(clientPercentageX, baseTrainType.width), 0, getValueFromPercentage(MathHelper.fractionalPart(clientPercentageZ), realSpacingRender)).rotateX(pitch).rotateY(yaw);
				clientPlayer.move(MovementType.SELF, playerOffset.add(x - clientPlayer.getX(), y - clientPlayer.getY(), z - clientPlayer.getZ()));

				if (speed > 0) {
					clientPlayer.yaw -= Math.toDegrees(yaw - clientPrevYaw);
					offset.add(x);
					offset.add(y);
					offset.add(z);
					offset.add(playerOffset.x);
					offset.add(playerOffset.y + clientPlayer.getStandingEyeHeight());
					offset.add(playerOffset.z);
				}

				clientPrevYaw = yaw;
			};

			final int currentRidingCar = (int) Math.floor(clientPercentageZ);
			calculateCar(world, positions, currentRidingCar, doorValue, 0, (x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender) -> {
				final boolean hasGangwayConnection = TrainClientRegistry.getTrainProperties(trainId, baseTrainType).hasGangwayConnection;
				final boolean trainBarrierOption = TrainClientRegistry.getTrainProperties(trainId, baseTrainType).trainBarrierOption;
				final Vec3d movement = new Vec3d(clientPlayer.sidewaysSpeed * ticksElapsed / 4, 0, clientPlayer.forwardSpeed * ticksElapsed / 4).rotateY((float) -Math.toRadians(clientPlayer.yaw) - yaw);
				clientPercentageX += movement.x / baseTrainType.width;
				clientPercentageZ += movement.z / realSpacingRender;
				clientPercentageX = MathHelper.clamp(clientPercentageX, doorLeftOpenRender ? -1 : 0, doorRightOpenRender ? 2 : 1);
				clientPercentageZ = MathHelper.clamp(clientPercentageZ, (hasGangwayConnection && trainBarrierOption ? 0 : currentRidingCar + 0.05F) + 0.01F, (hasGangwayConnection && trainBarrierOption ? trainCars : currentRidingCar + 0.95F) - 0.01F);
				final int newRidingCar = (int) Math.floor(clientPercentageZ);
				if (currentRidingCar == newRidingCar) {
					moveClient.calculateCarCallback(x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender);
				} else {
					calculateCar(world, positions, newRidingCar, doorValue, 0, moveClient);
				}
			});
		}

		justMounted = false;
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
	protected boolean skipScanBlocks(World world, double trainX, double trainY, double trainZ) {
		return false;
	}

	@Override
	protected boolean openDoors(World world, Block block, BlockPos checkPos, float doorValue, int dwellTicks) {
		return true;
	}

	public void render(World world, float ticksElapsed, RenderTrainCallback renderTrainCallback, RenderConnectionCallback renderConnectionCallback, SpeedCallback speedCallback, AnnouncementCallback announcementCallback, AnnouncementCallback lightRailAnnouncementCallback) {
		this.renderTrainCallback = renderTrainCallback;
		this.renderConnectionCallback = renderConnectionCallback;
		this.speedCallback = speedCallback;
		this.announcementCallback = announcementCallback;
		this.lightRailAnnouncementCallback = lightRailAnnouncementCallback;
		simulateTrain(world, ticksElapsed, null);
	}

	public void updateClientPercentages(ClientPlayerEntity player, float clientPercentageX, float clientPercentageZ) {
		if (player != null) {
			this.clientPercentageX = clientPercentageX;
			this.clientPercentageZ = clientPercentageZ;
			justMounted = true;
			ridingEntities.add(player.getUuid());
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
		void renderTrainCallback(double x, double y, double z, float yaw, float pitch, String customId, TrainType baseTrainType, boolean isEnd1Head, boolean isEnd2Head, boolean head1IsFront, float doorLeftValue, float doorRightValue, boolean opening, boolean lightsOn, Vec3d playerOffset);
	}

	@FunctionalInterface
	public interface RenderConnectionCallback {
		void renderConnectionCallback(Vec3d prevPos1, Vec3d prevPos2, Vec3d prevPos3, Vec3d prevPos4, Vec3d thisPos1, Vec3d thisPos2, Vec3d thisPos3, Vec3d thisPos4, double x, double y, double z, float yaw, String trainId, TrainType baseTrainType, boolean lightsOn, Vec3d playerOffset);
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
