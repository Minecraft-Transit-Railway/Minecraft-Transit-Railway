package mtr.entity;

import mtr.data.Pos3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
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
			killTimer++;
			if (killTimer > 2) {
				kill();
			}
		}
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
		// TODO mover around in train but with bounds
		if (passenger instanceof LivingEntity) {
			final LivingEntity mob = (LivingEntity) passenger;
			final int entityId = passenger.getEntityId();

			if (passengerOffsets.containsKey(passenger.getEntityId())) {
				passengerOffsets.get(entityId).add(new Pos3f(mob.sidewaysSpeed / 5, 0, mob.forwardSpeed / 5).rotateY((float) Math.toRadians(-passenger.yaw)));
			} else {
				passengerOffsets.put(entityId, new Pos3f(0, 0, 0));
			}

			final Pos3f offset = passengerOffsets.get(entityId);
			passenger.updatePosition(getX() + offset.getX(), getY(), getZ() + offset.getZ());
		} else {
			passenger.updatePosition(getX(), getY(), getZ());
		}
		final float yawChange = MathHelper.wrapDegrees(prevYaw - yaw);
		passenger.yaw += yawChange;
		passenger.setHeadYaw(passenger.getHeadYaw() + yawChange);
		prevYaw = yaw;
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
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
}
