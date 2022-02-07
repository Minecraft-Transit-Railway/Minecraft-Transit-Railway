package mtr.entity;

import mtr.EntityTypes;
import mtr.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public class EntitySeat extends Entity {

	public float percentageX;
	public float percentageZ;

	private int seatRefresh;
	private int ridingRefresh;
	private Player player;
	private long trainId;

	private int clientInterpolationSteps;
	private double clientX;
	private double clientY;
	private double clientZ;

	public static final float SIZE = 0.5F;
	private static final EntityDataAccessor<Optional<UUID>> PLAYER_ID = SynchedEntityData.defineId(EntitySeat.class, EntityDataSerializers.OPTIONAL_UUID);

	public EntitySeat(EntityType<?> type, Level world) {
		super(type, world);
		noCulling = true;
	}

	public EntitySeat(Level world, double x, double y, double z) {
		this(EntityTypes.SEAT, world);
		absMoveTo(x, y, z);
		setDeltaMovement(Vec3.ZERO);
		xo = x;
		yo = y;
		zo = z;
	}

	@Override
	public void tick() {
		if (player == null) {
			player = level.getPlayerByUUID(getPlayerId());
		}

		if (level.isClientSide) {
			if (playerNotRiding()) {
				final float speed = player.getSpeed();
				final Vec3 newPos = player.position().add(player.getLookAngle().multiply(speed, speed, speed));
				absMoveTo(newPos.x, newPos.y, newPos.z);
			} else {
				if (clientInterpolationSteps > 0) {
					double x = getX() + (clientX - getX()) / clientInterpolationSteps;
					double y = getY() + (clientY - getY()) / clientInterpolationSteps;
					double z = getZ() + (clientZ - getZ()) / clientInterpolationSteps;
					--clientInterpolationSteps;
					absMoveTo(x, y, z);
				} else {
					reapplyPosition();
				}
			}
		} else {
			if (player == null || seatRefresh <= 0) {
				kill();
			}
			if (ridingRefresh <= 0) {
				ejectPassengers();
				trainId = 0;
			}
			seatRefresh--;
			ridingRefresh--;
		}
	}

	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
		clientX = x;
		clientY = y;
		clientZ = z;
		clientInterpolationSteps = interpolationSteps;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return Registry.createAddEntityPacket(this);
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		return true;
	}

	@Override
	public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
		return true;
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(PLAYER_ID, Optional.of(new UUID(0, 0)));
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {
	}

	public void updateSeat(Player player) {
		if (player != null) {
			entityData.set(PLAYER_ID, Optional.of(player.getUUID()));
			if (playerNotRiding()) {
				absMoveTo(player.getX(), player.getY(), player.getZ());
			}
			seatRefresh = 2;
		}
	}

	public boolean updateRiding(long trainId) {
		if (this.trainId == 0 || this.trainId == trainId) {
			this.trainId = trainId;
			ridingRefresh = 2;
			return true;
		} else {
			return false;
		}
	}

	public boolean isPlayer(Player player) {
		return this.player == player;
	}

	private UUID getPlayerId() {
		try {
			return entityData == null ? new UUID(0, 0) : entityData.get(PLAYER_ID).orElse(new UUID(0, 0));
		} catch (Exception e) {
			e.printStackTrace();
			return new UUID(0, 0);
		}
	}

	private boolean playerNotRiding() {
		return player != null && player.getVehicle() != this;
	}
}
