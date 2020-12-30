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
	public int doorValue;

	private float prevYaw;
	private int clientInterpolationSteps;
	private double clientX;
	private double clientY;
	private double clientZ;
	private double clientYaw;
	private double clientPitch;
	private int killTimer;
	private boolean doorLeft, doorRight;

	private final Map<Integer, Pos3f> passengerOffsets;

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
			if (clientInterpolationSteps > 0) {
				final double x = getX() + (clientX - getX()) / clientInterpolationSteps;
				final double y = getY() + (clientY - getY()) / clientInterpolationSteps;
				final double z = getZ() + (clientZ - getZ()) / clientInterpolationSteps;
				yaw = (float) (yaw + MathHelper.wrapDegrees(clientYaw - yaw) / clientInterpolationSteps);
				pitch = (float) (pitch + (clientPitch - pitch) / clientInterpolationSteps);
				--clientInterpolationSteps;
				updatePosition(x, y, z);
			} else {
				refreshPosition();
			}
			setRotation(yaw, pitch);
		} else {
			checkBlockCollision();

			if (stationCoolDown < RailwayData.TRAIN_STOP_TIME || stationCoolDown >= RailwayData.STATION_COOL_DOWN - RailwayData.TRAIN_STOP_TIME) {
				doorValue = 0;
			} else if (stationCoolDown < RailwayData.TRAIN_STOP_TIME + BlockPSDAPGDoorBase.MAX_OPEN_VALUE) {
				doorValue = stationCoolDown - RailwayData.TRAIN_STOP_TIME;
			} else if (stationCoolDown >= RailwayData.STATION_COOL_DOWN - RailwayData.TRAIN_STOP_TIME - BlockPSDAPGDoorBase.MAX_OPEN_VALUE) {
				doorValue = RailwayData.STATION_COOL_DOWN - RailwayData.TRAIN_STOP_TIME - stationCoolDown;
			} else {
				doorValue = BlockPSDAPGDoorBase.MAX_OPEN_VALUE;
			}

			if (doorValue > 0) {
				final int width = getTrainType().getWidth();
				final Vec3d offsetVec = new Vec3d(width, 0, 0).rotateY((float) Math.toRadians(yaw));
				final BlockPos checkPosLeft = new BlockPos(getPos().subtract(offsetVec));
				doorLeft = isPlatformOrDoor(checkPosLeft) || isPlatformOrDoor(checkPosLeft.up()) || isPlatformOrDoor(checkPosLeft.down());
				final BlockPos checkPosRight = new BlockPos(getPos().add(offsetVec));
				doorRight = isPlatformOrDoor(checkPosRight) || isPlatformOrDoor(checkPosRight.up()) || isPlatformOrDoor(checkPosRight.down());
			} else {
				doorLeft = false;
				doorRight = false;
			}

			killTimer++;
			if (killTimer > 2) {
				kill();
			}
		}

		mountCollidingLivingEntities();
	}

	@Override
	public void updatePositionAndAngles(double x, double y, double z, float yaw, float pitch) {
		super.updatePositionAndAngles(x, y, z, yaw, pitch);
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
		final float offsetX = getTrainType().getWidth() / 2F + 2;
		final Vec3d offsetVec = new Vec3d(doorLeft ? -offsetX : offsetX, 0.5, 0).rotateY((float) Math.toRadians(yaw));
		return passenger.getPos().add(offsetVec);
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
	}

	protected void mountCollidingLivingEntities() {
		final float length = getTrainType().getLength() / 2F;
		final float widthBigger = getTrainType().getWidth() / 2F + 0.5F;
		world.getNonSpectatingEntities(LivingEntity.class, new Box(getPos().subtract(length, length, length), getPos().add(length, length, length))).stream().filter(entity -> !entity.hasVehicle()).forEach(entity -> {
			final Vec3d entityPosRelative = entity.getPos().subtract(getPos()).rotateX((float) Math.toRadians(-pitch)).rotateY((float) Math.toRadians(-yaw));
			if (RailwayData.isBetween(entityPosRelative.x, -widthBigger, widthBigger) && RailwayData.isBetween(entityPosRelative.y, -widthBigger, widthBigger) && RailwayData.isBetween(entityPosRelative.z, -length, length)) {
				final Vec3d passengerOffsetRotated = entity.getPos().subtract(getPos()).rotateY((float) Math.toRadians(-yaw)).rotateX((float) Math.toRadians(-pitch));
				passengerOffsets.put(entity.getEntityId(), new Pos3f(0, 0, (float) passengerOffsetRotated.z));
				if (!world.isClient) {
					entity.startRiding(this);
				}
			}
		});
	}

	protected abstract Train.TrainType getTrainType();

	private boolean isPlatformOrDoor(BlockPos pos) {
		final Block block = world.getBlockState(pos).getBlock();
		return block instanceof BlockPlatform || block instanceof BlockPSDAPGBase;
	}
}
