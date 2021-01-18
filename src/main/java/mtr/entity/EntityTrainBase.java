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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public abstract class EntityTrainBase extends Entity {

	public int stationCoolDown;

	private float prevYaw;
	private int clientInterpolationSteps;
	private double clientX;
	private double clientY;
	private double clientZ;
	private double clientYaw;
	private double clientPitch;
	private int killTimer;

	private final Map<Integer, Pos3f> passengerOffsets;

	private static final TrackedData<Float> YAW = DataTracker.registerData(EntityTrainBase.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> PITCH = DataTracker.registerData(EntityTrainBase.class, TrackedDataHandlerRegistry.FLOAT);
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
		if (world.isClient) {
			final float dataTrackerYaw = dataTracker.get(YAW);
			final float dataTrackerPitch = dataTracker.get(PITCH);

			if (clientInterpolationSteps > 0) {
				final double x = getX() + (clientX - getX()) / clientInterpolationSteps;
				final double y = getY() + (clientY - getY()) / clientInterpolationSteps;
				final double z = getZ() + (clientZ - getZ()) / clientInterpolationSteps;

				yaw = (float) (dataTrackerYaw + MathHelper.wrapDegrees(clientYaw - dataTrackerYaw) / clientInterpolationSteps);
				pitch = (float) (dataTrackerPitch + (clientPitch - dataTrackerPitch) / clientInterpolationSteps);
				--clientInterpolationSteps;
				updatePosition(x, y, z);
			} else {
				yaw = dataTrackerYaw;
				pitch = dataTrackerPitch;
				refreshPosition();
			}
			setRotation(yaw, pitch);
		} else {
			if (stationCoolDown > 0) {
				checkBlockCollision();
				mountCollidingLivingEntities();
			}

			if (stationCoolDown < RailwayData.TRAIN_STOP_TIME || stationCoolDown >= RailwayData.STATION_COOL_DOWN - RailwayData.TRAIN_STOP_TIME) {
				setDoorValue(0);
			} else if (stationCoolDown < RailwayData.TRAIN_STOP_TIME + BlockPSDAPGDoorBase.MAX_OPEN_VALUE) {
				setDoorValue(stationCoolDown - RailwayData.TRAIN_STOP_TIME);
			} else if (stationCoolDown >= RailwayData.STATION_COOL_DOWN - RailwayData.TRAIN_STOP_TIME - BlockPSDAPGDoorBase.MAX_OPEN_VALUE) {
				setDoorValue(RailwayData.STATION_COOL_DOWN - RailwayData.TRAIN_STOP_TIME - stationCoolDown);
			} else {
				setDoorValue(BlockPSDAPGDoorBase.MAX_OPEN_VALUE);
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
		dataTracker.set(YAW, yaw);
		dataTracker.set(PITCH, pitch);
		killTimer = 0;
	}

	@Override
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
		clientX = x;
		clientY = y;
		clientZ = z;
		clientYaw = yaw;
		clientPitch = pitch;
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
		prevYaw = yaw;
	}

	@Override
	public Vec3d updatePassengerForDismount(LivingEntity passenger) {
		if (getDoorValue() > 0) {
			final float offsetX = getTrainType().getWidth() / 2F + 2;
			final Vec3d offsetVec = new Vec3d(getDoorLeft() ? offsetX : -offsetX, 0.5, 0).rotateY((float) Math.toRadians(yaw));
			return passenger.getPos().add(offsetVec);
		} else {
			return super.updatePassengerForDismount(passenger);
		}
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
		dataTracker.startTracking(YAW, 0F);
		dataTracker.startTracking(PITCH, 0F);
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
}
