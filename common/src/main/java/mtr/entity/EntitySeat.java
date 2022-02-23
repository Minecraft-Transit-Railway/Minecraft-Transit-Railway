package mtr.entity;

import mtr.EntityTypes;
import mtr.Registry;
import mtr.data.RailwayData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
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
	private float interpolatedPercentageX;
	private float interpolatedPercentageZ;
	private float clientRailProgress;

	public static final float SIZE = 0.5F;
	private static final int SEAT_REFRESH = 10;
	private static final EntityDataAccessor<Optional<UUID>> PLAYER_ID = SynchedEntityData.defineId(EntitySeat.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Float> PERCENTAGE_X = SynchedEntityData.defineId(EntitySeat.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> PERCENTAGE_Z = SynchedEntityData.defineId(EntitySeat.class, EntityDataSerializers.FLOAT);
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
			percentageX = getClientPercentageX();
			percentageZ = getClientPercentageZ();

			if (clientInterpolationSteps > 0) {
				final double x = getX() + (clientX - getX()) / clientInterpolationSteps;
				final double y = getY() + (clientY - getY()) / clientInterpolationSteps;
				final double z = getZ() + (clientZ - getZ()) / clientInterpolationSteps;
				interpolatedPercentageX += (percentageX - interpolatedPercentageX) / clientInterpolationSteps;
				interpolatedPercentageZ += (percentageZ - interpolatedPercentageZ) / clientInterpolationSteps;
				--clientInterpolationSteps;
				absMoveTo(x, y, z);
			} else {
				interpolatedPercentageX = percentageX;
				interpolatedPercentageZ = percentageZ;
				reapplyPosition();
			}

			final boolean tempMounted = entityData.get(PLAYER_MOUNTED);
			final LocalPlayer localPlayer = Minecraft.getInstance().player;
			if (localPlayer != null) {
				if (tempMounted && localPlayer.getVehicle() != this) {
					localPlayer.startRiding(this);
				} else if (!tempMounted && localPlayer.getVehicle() == this) {
					ejectPassengers();
				}
			}
		} else {
			if (player == null || seatRefresh <= 0) {
				kill();
			} else {
				if (playerNotRiding()) {
					absMoveTo(player.getX(), player.getY(), player.getZ());
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
		entityData.define(PERCENTAGE_X, 0F);
		entityData.define(PERCENTAGE_Z, 0F);
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

	public boolean isClientPlayer(Player checkPlayer) {
		try {
			return entityData != null && entityData.get(PLAYER_ID).orElse(new UUID(0, 0)).equals(checkPlayer.getUUID());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void updateSeatByRailwayData(Player player) {
		if (player != null) {
			seatRefresh = SEAT_REFRESH;
		}
		this.player = player;
	}

	public boolean updateRidingByTrainServer(long trainId) {
		if (this.trainId == 0 || this.trainId == trainId) {
			this.trainId = trainId;
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
		entityData.set(PERCENTAGE_X, percentageX);
		entityData.set(PERCENTAGE_Z, percentageZ);
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

	public float getClientPercentageX() {
		return entityData.get(PERCENTAGE_X);
	}

	public float getClientPercentageZ() {
		return entityData.get(PERCENTAGE_Z);
	}

	public float getInterpolatedPercentageX() {
		return interpolatedPercentageX;
	}

	public float getInterpolatedPercentageZ() {
		return interpolatedPercentageZ;
	}

	private boolean playerNotRiding() {
		return player != null && player.getVehicle() != this;
	}
}
