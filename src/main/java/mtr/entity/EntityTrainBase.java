package mtr.entity;

import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPlatform;
import mtr.data.Pos3f;
import mtr.data.RailwayData;
import mtr.data.Train;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("EntityConstructor")
public abstract class EntityTrainBase extends Entity {

	public int stationCoolDown;

	private int clientInterpolationSteps;
	private double clientX;
	private double clientY;
	private double clientZ;
	private double clientYaw;
	private double clientPitch;
	private int killTimer;

	private final Map<Integer, Pos3f> passengerOffsets;

	private static final TrackedData<Float> POS_X = DataTracker.registerData(EntityTrainBase.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> POS_Y = DataTracker.registerData(EntityTrainBase.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> POS_Z = DataTracker.registerData(EntityTrainBase.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> YAW = DataTracker.registerData(EntityTrainBase.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> PITCH = DataTracker.registerData(EntityTrainBase.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> SPEED = DataTracker.registerData(EntityTrainBase.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Integer> DOOR_VALUE = DataTracker.registerData(EntityTrainBase.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> DOOR_LEFT = DataTracker.registerData(EntityTrainBase.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> DOOR_RIGHT = DataTracker.registerData(EntityTrainBase.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> IS_END_1_HEAD = DataTracker.registerData(EntityTrainBase.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> IS_END_2_HEAD = DataTracker.registerData(EntityTrainBase.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> HEAD_1_IS_FRONT = DataTracker.registerData(EntityTrainBase.class, TrackedDataHandlerRegistry.BOOLEAN);

	protected EntityTrainBase(EntityType<?> type, World world) {
		super(type, world);
		setNoGravity(true);
		noClip = true;
		passengerOffsets = new HashMap<>();
	}

	protected EntityTrainBase(EntityType<?> type, World world, double x, double y, double z) {
		this(type, world);
		updatePosition(x, y, z);
		setVelocity(Vec3d.ZERO);
		prevX = x;
		prevY = y;
		prevZ = z;
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isClient) {
			final float dataTrackerPosX = dataTracker.get(POS_X);
			final float dataTrackerPosY = dataTracker.get(POS_Y);
			final float dataTrackerPosZ = dataTracker.get(POS_Z);
			final float dataTrackerYaw = dataTracker.get(YAW);
			final float dataTrackerPitch = dataTracker.get(PITCH);

			if (clientInterpolationSteps > 0) {
				final double x = dataTrackerPosX + (clientX - dataTrackerPosX) / clientInterpolationSteps;
				final double y = dataTrackerPosY + (clientY - dataTrackerPosY) / clientInterpolationSteps;
				final double z = dataTrackerPosZ + (clientZ - dataTrackerPosZ) / clientInterpolationSteps;

				yaw = (float) (dataTrackerYaw + MathHelper.wrapDegrees(clientYaw - dataTrackerYaw) / clientInterpolationSteps);
				pitch = (float) (dataTrackerPitch + (clientPitch - dataTrackerPitch) / clientInterpolationSteps);
				--clientInterpolationSteps;

				updatePosition(x, y, z);
				setRotation(yaw, pitch);
			} else {
				updatePosition(dataTrackerPosX, dataTrackerPosY, dataTrackerPosZ);
			}
		} else {
			if (stationCoolDown > 0) {
				checkBlockCollision();
				mountCollidingLivingEntities();

				if (stationCoolDown < RailwayData.TRAIN_STOP_TIME || stationCoolDown >= RailwayData.STATION_COOL_DOWN - RailwayData.TRAIN_STOP_TIME) {
					setDoorValue(0);
				} else if (stationCoolDown < RailwayData.TRAIN_STOP_TIME + BlockPSDAPGDoorBase.MAX_OPEN_VALUE) {
					setDoorValue(stationCoolDown - RailwayData.TRAIN_STOP_TIME);
				} else if (stationCoolDown >= RailwayData.STATION_COOL_DOWN - RailwayData.TRAIN_STOP_TIME - BlockPSDAPGDoorBase.MAX_OPEN_VALUE) {
					setDoorValue(RailwayData.STATION_COOL_DOWN - RailwayData.TRAIN_STOP_TIME - stationCoolDown);
				} else {
					setDoorValue(BlockPSDAPGDoorBase.MAX_OPEN_VALUE);
				}
			}

			if (getDoorValue() > 0) {
				final int width = getTrainType().getWidth();
				final Vec3d offsetVec = new Vec3d(width, 0, 0).rotateY((float) Math.toRadians(yaw));
				final BlockPos checkPosLeft = new BlockPos(getPos().add(offsetVec));
				final BlockPos checkPosRight = new BlockPos(getPos().subtract(offsetVec));
				setDoors(isPlatformOrDoor(checkPosLeft) || isPlatformOrDoor(checkPosLeft.up()) || isPlatformOrDoor(checkPosLeft.down()), isPlatformOrDoor(checkPosRight) || isPlatformOrDoor(checkPosRight.up()) || isPlatformOrDoor(checkPosRight.down()));
			} else {
				setDoors(false, false);
			}

			killTimer++;
			if (killTimer > 2) {
				kill();
			}
		}
	}

	@Override
	public void updatePositionAndAngles(double x, double y, double z, float yaw, float pitch) {
		super.updatePositionAndAngles(x, y, z, yaw, pitch);
		dataTracker.set(POS_X, (float) x);
		dataTracker.set(POS_Y, (float) y);
		dataTracker.set(POS_Z, (float) z);
		dataTracker.set(YAW, yaw);
		dataTracker.set(PITCH, pitch);
		killTimer = 0;
	}

	@Override
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
		clientX = dataTracker.get(POS_X);
		clientY = dataTracker.get(POS_Y);
		clientZ = dataTracker.get(POS_Z);
		clientYaw = dataTracker.get(YAW);
		clientPitch = dataTracker.get(PITCH);
		clientInterpolationSteps = interpolationSteps + 2;
	}

	@Override
	public void updatePassengerPosition(Entity passenger) {
		if (passenger instanceof LivingEntity) {
			final LivingEntity mob = (LivingEntity) passenger;
			final int entityId = passenger.getEntityId();

			if (passengerOffsets.containsKey(passenger.getEntityId())) {
				final Pos3f offset = passengerOffsets.get(entityId);
				offset.add(new Pos3f(mob.sidewaysSpeed / 5, 0, mob.forwardSpeed / 5).rotateY((float) Math.toRadians(-yaw - passenger.yaw)));

				final float length = getTrainType().getLength() / 2F;
				final float width = getTrainType().getWidth() / 2F;

				passengerOffsets.put(entityId, new Pos3f(MathHelper.clamp(offset.getX(), -width, width), 0, MathHelper.clamp(offset.getZ(), -length, length)));
			} else {
				passengerOffsets.put(entityId, new Pos3f(0, 0, 0));
			}

			final Pos3f offset = passengerOffsets.get(entityId).rotateX((float) Math.toRadians(pitch)).rotateY((float) Math.toRadians(yaw));
			passenger.updatePosition(getX() + offset.getX(), getY() + getMountedHeightOffset() + offset.getY(), getZ() + offset.getZ());
		} else {
			passenger.updatePosition(getX(), getY() + getMountedHeightOffset(), getZ());
		}

		final float yawChange = MathHelper.wrapDegrees(prevYaw - yaw);
		passenger.yaw += yawChange;
		passenger.setHeadYaw(passenger.getHeadYaw() + yawChange);
	}

	@Override
	public Vec3d updatePassengerForDismount(LivingEntity passenger) {
		final Vec3d offsetVec = new Vec3d(1, 0, 0).rotateY((float) Math.toRadians(yaw));
		final int[] checkHeights = {0, -1, 1};

		if (getDoorValue() > 0 && (getDoorLeft() || getDoorRight())) {
			for (final int height : checkHeights) {
				for (int offset = 1; offset <= 3; offset++) {
					final Vec3d checkPos = passenger.getPos().add(offsetVec.multiply(offset * (getDoorLeft() ? 1 : -1)).subtract(0, height, 0));
					if (canDismountHere(checkPos)) {
						return checkPos;
					}
				}
			}
		}

		final int[] checkOffsets = {1, -1, 2, -2, 3, -3};
		for (final int height : checkHeights) {
			for (final int offset : checkOffsets) {
				final Vec3d checkPos = passenger.getPos().add(offsetVec.multiply(offset).subtract(0, height, 0));
				if (canDismountHere(checkPos)) {
					return checkPos;
				}
			}
		}

		return super.updatePassengerForDismount(passenger);
	}

	@Override
	public double getMountedHeightOffset() {
		return 1;
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}


	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return getPassengerList().size() < getTrainType().getCapacity();
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
	}

	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(POS_X, 0F);
		dataTracker.startTracking(POS_Y, 0F);
		dataTracker.startTracking(POS_Z, 0F);
		dataTracker.startTracking(YAW, 0F);
		dataTracker.startTracking(PITCH, 0F);
		dataTracker.startTracking(SPEED, 0F);
		dataTracker.startTracking(DOOR_VALUE, 0);
		dataTracker.startTracking(DOOR_LEFT, false);
		dataTracker.startTracking(DOOR_RIGHT, false);
		dataTracker.startTracking(IS_END_1_HEAD, true);
		dataTracker.startTracking(IS_END_2_HEAD, true);
		dataTracker.startTracking(HEAD_1_IS_FRONT, true);
	}

	public boolean inTrain(Vec3d pos) {
		final float length = getTrainType().getLength() / 2F;
		final float widthBigger = getTrainType().getWidth() / 2F + 0.5F;
		final Vec3d posRelative = pos.subtract(getPos()).rotateX((float) Math.toRadians(-pitch)).rotateY((float) Math.toRadians(-yaw));
		return RailwayData.isBetween(posRelative.x, -widthBigger, widthBigger) && RailwayData.isBetween(posRelative.y, -widthBigger, widthBigger) && RailwayData.isBetween(posRelative.z, -length, length);
	}

	protected void mountCollidingLivingEntities() {
		if (!world.isClient && getDoorValue() > 0) {
			final float length = getTrainType().getLength() / 2F;
			world.getNonSpectatingEntities(LivingEntity.class, new Box(getPos().subtract(length, length, length), getPos().add(length, length, length))).stream().filter(entity -> !entity.hasVehicle()).forEach(entity -> {
				if (inTrain(entity.getPos())) {
					final Vec3d passengerOffsetRotated = entity.getPos().subtract(getPos()).rotateY((float) Math.toRadians(-yaw)).rotateX((float) Math.toRadians(-pitch));
					passengerOffsets.put(entity.getEntityId(), new Pos3f(0, 0, (float) passengerOffsetRotated.z));
					entity.startRiding(this);
				}
			});
		}
	}

	public void setSpeed(float speed) {
		dataTracker.set(SPEED, speed);
	}

	public float getSpeed() {
		return dataTracker.get(SPEED);
	}

	public void setDoorValue(int doorValue) {
		dataTracker.set(DOOR_VALUE, doorValue);
	}

	public int getDoorValue() {
		return dataTracker.get(DOOR_VALUE);
	}

	public void setDoors(boolean doorLeft, boolean doorRight) {
		dataTracker.set(DOOR_LEFT, doorLeft);
		dataTracker.set(DOOR_RIGHT, doorRight);
	}

	public boolean getDoorLeft() {
		return dataTracker.get(DOOR_LEFT);
	}

	public boolean getDoorRight() {
		return dataTracker.get(DOOR_RIGHT);
	}

	public void setIsEndHead(boolean isEnd1Head, boolean isEnd2Head) {
		dataTracker.set(IS_END_1_HEAD, isEnd1Head);
		dataTracker.set(IS_END_2_HEAD, isEnd2Head);
	}

	public boolean getIsEnd1Head() {
		return dataTracker.get(IS_END_1_HEAD);
	}

	public boolean getIsEnd2Head() {
		return dataTracker.get(IS_END_2_HEAD);
	}

	public void setHead1IsFront(boolean head1IsFront) {
		dataTracker.set(HEAD_1_IS_FRONT, head1IsFront);
	}

	public boolean getHead1IsFront() {
		return dataTracker.get(HEAD_1_IS_FRONT);
	}

	protected abstract Train.TrainType getTrainType();

	private boolean isPlatformOrDoor(BlockPos pos) {
		final Block block = world.getBlockState(pos).getBlock();
		return block instanceof BlockPlatform || block instanceof BlockPSDAPGBase;
	}

	private boolean canDismountHere(Vec3d vec3d) {
		final BlockPos pos = new BlockPos(vec3d);
		final boolean flatTop = world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP);
		final boolean middleNotSolid = !world.getBlockState(pos).isSolidBlock(world, pos);
		final boolean topNotSolid = !world.getBlockState(pos.up()).isSolidBlock(world, pos.up());
		return flatTop && middleNotSolid && topNotSolid;
	}
}
