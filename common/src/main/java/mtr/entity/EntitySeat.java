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
	private static final EntityDataAccessor<Boolean> PLAYER_MOUNTED = SynchedEntityData.defineId(EntitySeat.class, EntityDataSerializers.BOOLEAN);

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
			final LocalPlayer localPlayer = getClientPlayer();

			if (clientInterpolationSteps > 0) {
				final double x = getX() + (clientX - getX()) / clientInterpolationSteps;
				final double y = getY() + (clientY - getY()) / clientInterpolationSteps;
				final double z = getZ() + (clientZ - getZ()) / clientInterpolationSteps;
				--clientInterpolationSteps;
				if (localPlayer == null || localPlayer.getVehicle() == this) {
					absMoveTo(x, y, z);
				}
			} else {
				if (localPlayer == null || localPlayer.getVehicle() == this) {
					reapplyPosition();
				}
			}

			final boolean tempMounted = entityData.get(PLAYER_MOUNTED);
			if (localPlayer != null) {
				if (localPlayer.getVehicle() == this) {
					if (!tempMounted) {
						ejectPassengers();
					}
				} else {
					setPos(localPlayer.getX(), localPlayer.getY(), localPlayer.getZ());
					if (tempMounted) {
						localPlayer.startRiding(this);
					}
				}
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
					entityData.set(PLAYER_MOUNTED, false);
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

		if (level.isClientSide && entity instanceof Player) {
			if (playerOffset == null) {
				final Vec3 movement = new Vec3(((Player) entity).xxa / 4, 0, ((Player) entity).zza / 4).yRot((float) -Math.toRadians(Utilities.getYaw(entity)));
				entity.setPos(entity.getX() + movement.x, getY(), entity.getZ() + movement.z);
			} else {
				entity.setPos(playerOffset.x, playerOffset.y, playerOffset.z);
				playerOffset = null;
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
		entityData.define(PLAYER_MOUNTED, false);
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

	public boolean isNotClientPlayer(LocalPlayer checkPlayer) {
		try {
			return entityData == null || !entityData.get(PLAYER_ID).orElse(new UUID(0, 0)).equals(checkPlayer.getUUID());
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
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
				entityData.set(PLAYER_MOUNTED, true);
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
		return player == null || isNotClientPlayer(player) ? null : player;
	}
}
