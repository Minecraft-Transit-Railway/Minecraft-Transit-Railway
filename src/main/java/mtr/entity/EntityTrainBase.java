package mtr.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityTrainBase extends Entity {

	private int clientInterpolationSteps;
	private double clientX;
	private double clientY;
	private double clientZ;
	private double clientYaw;
	private double clientPitch;

	protected EntityTrainBase(EntityType<?> type, World world) {
		super(type, world);
		setNoGravity(true);
		noClip = true;
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
		}
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
