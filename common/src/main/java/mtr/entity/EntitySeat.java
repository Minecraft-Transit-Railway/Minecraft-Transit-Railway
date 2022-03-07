package mtr.entity;

import mtr.EntityTypes;
import mtr.Registry;
import mtr.data.RailwayData;
import mtr.mappings.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public class EntitySeat extends Entity {

	public Vec3 playerOffset;

	private int seatRefresh;
	private int ridingRefresh;
	private Player player;
	private LocalPlayer localPlayer;
	private long trainId;
	private float playerDismountYOffset;

	private int clientInterpolationSteps;
	private double clientX;
	private double clientY;
	private double clientZ;
	private float clientRailProgress;

	public static final float SIZE = 0.5F;
	private static final int SEAT_REFRESH = 10;
	private static final EntityDataAccessor<Optional<UUID>> PLAYER_ID = SynchedEntityData.defineId(EntitySeat.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Float> RAIL_PROGRESS = SynchedEntityData.defineId(EntitySeat.class, EntityDataSerializers.FLOAT);

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
		if (level.isClientSide) {
			localPlayer = getClientPlayer();
			final boolean followPlayer = localPlayer != null && localPlayer.getVehicle() != this;

			if (clientInterpolationSteps > 0) {
				final double x = getX() + (clientX - getX()) / clientInterpolationSteps;
				final double y = getY() + (clientY - getY()) / clientInterpolationSteps;
				final double z = getZ() + (clientZ - getZ()) / clientInterpolationSteps;
				--clientInterpolationSteps;
				if (!followPlayer) {
					absMoveTo(x, y, z);
				}
			} else {
				if (!followPlayer) {
					reapplyPosition();
				}
			}

			if (followPlayer) {
				setPos(localPlayer.getX(), localPlayer.getY(), localPlayer.getZ());
			}
		} else {
			if (player == null || seatRefresh <= 0) {
				kill();
			} else {
				if (playerNotRiding()) {
					setPos(player.getX(), player.getY(), player.getZ());
				}

				final RailwayData railwayData = RailwayData.getInstance(level);
				if (railwayData != null) {
					railwayData.updatePlayerSeatCoolDown(player);
				}

				if (ridingRefresh <= 0) {
					ejectPassengers();
					trainId = 0;
				}

				seatRefresh--;
				ridingRefresh--;
			}
		}
	}

	@Override
	public void positionRider(Entity entity) {
		if (!hasPassenger(entity)) {
			return;
		}

		if (level.isClientSide && entity == localPlayer) {
			final double moveX;
			final double moveY;
			final double moveZ;

			if (playerOffset == null) {
				final Vec3 movement = new Vec3(((Player) entity).xxa / 4, 0, ((Player) entity).zza / 4).yRot((float) -Math.toRadians(Utilities.getYaw(entity)));
				moveX = entity.getX() + movement.x;
				moveY = getY();
				moveZ = entity.getZ() + movement.z;
			} else {
				moveX = playerOffset.x;
				moveY = playerOffset.y;
				moveZ = playerOffset.z;
				playerOffset = null;
			}

			if (Math.abs(moveX - getX()) < 16 && Math.abs(moveY - getY()) < 16 && Math.abs(moveZ - getZ()) < 16) {
				entity.setPos(moveX, moveY, moveZ);
			} else {
				entity.setPos(getX(), getY(), getZ());
			}
		} else {
			entity.setPos(getX(), getY(), getZ());
		}
	}

	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
		return livingEntity.position().subtract(0, playerDismountYOffset, 0);
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
		entityData.define(RAIL_PROGRESS, 0F);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {
	}

	public void initialize(Player player) {
		entityData.set(PLAYER_ID, Optional.of(player.getUUID()));
	}

	public void updateSeatByRailwayData(Player player) {
		if (player != null) {
			seatRefresh = SEAT_REFRESH;
		}
		this.player = player;
	}

	public boolean updateRidingByTrainServer(long trainId, float playerDismountYOffset) {
		if (this.trainId == 0 || this.trainId == trainId) {
			this.trainId = trainId;
			this.playerDismountYOffset = playerDismountYOffset;
			ridingRefresh = SEAT_REFRESH;
			if (playerNotRiding()) {
				player.startRiding(this);
			}
			return true;
		} else {
			return false;
		}
	}

	public void updateDataToClient(float railProgress) {
		if (railProgress > 0) {
			entityData.set(RAIL_PROGRESS, railProgress);
		}
	}

	public float getClientRailProgress() {
		final float tempRailProgress = entityData.get(RAIL_PROGRESS);
		if (tempRailProgress == clientRailProgress) {
			return 0;
		} else {
			clientRailProgress = tempRailProgress;
			return clientRailProgress;
		}
	}

	private boolean playerNotRiding() {
		return player != null && player.getVehicle() != this;
	}

	private LocalPlayer getClientPlayer() {
		final LocalPlayer player = Minecraft.getInstance().player;
		if (player == null || entityData == null) {
			return null;
		}
		try {
			final Optional<UUID> optionalUUID = entityData.get(PLAYER_ID);
			if (optionalUUID.isPresent() && optionalUUID.get().equals(player.getUUID())) {
				return player;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
