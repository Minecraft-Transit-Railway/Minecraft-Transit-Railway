package mtr.entity;

import mtr.MTR;
import mtr.data.RailwayData;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("EntityConstructor")
public class EntitySeat extends Entity {

	public float ridingPercentageX;
	public float ridingPercentageZ;
	public float prevTrainYaw;

	private PlayerEntity player;
	private int seatCoolDown;
	private float prevRidingPercentageX;
	private float prevRidingPercentageZ;
	private float scheduleTime;
	private float routeId;

	private int clientInterpolationSteps;
	private double clientX;
	private double clientY;
	private double clientZ;
	private float clientRidingPercentageX;
	private float clientRidingPercentageZ;
	private int clientRefreshCoolDown;

	public static final int DETAIL_RADIUS = 32;
	public static final int MAX_SEAT_COOL_DOWN = 2;

	private static final TrackedData<Optional<UUID>> PLAYER_ID = DataTracker.registerData(EntitySeat.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
	private static final TrackedData<Float> RIDING_PERCENTAGE_X = DataTracker.registerData(EntitySeat.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> RIDING_PERCENTAGE_Z = DataTracker.registerData(EntitySeat.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Integer> SCHEDULE_TIME = DataTracker.registerData(EntitySeat.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> ROUTE_ID = DataTracker.registerData(EntitySeat.class, TrackedDataHandlerRegistry.INTEGER);

	public EntitySeat(EntityType<?> type, World world) {
		super(type, world);
		ignoreCameraFrustum = true;
	}

	public EntitySeat(World world, double x, double y, double z) {
		this(MTR.SEAT, world);
		updatePosition(x, y, z);
		setVelocity(Vec3d.ZERO);
		prevX = x;
		prevY = y;
		prevZ = z;
	}

	@Override
	public void tick() {
		getPlayer();

		if (world.isClient) {
			if (player != null) {
				if (hasPassenger(player)) {
					if (clientInterpolationSteps > 0) {
						double x = getX() + (clientX - getX()) / clientInterpolationSteps;
						double y = getY() + (clientY - getY()) / clientInterpolationSteps;
						double z = getZ() + (clientZ - getZ()) / clientInterpolationSteps;

						prevRidingPercentageX = ridingPercentageX;
						prevRidingPercentageZ = ridingPercentageZ;
						ridingPercentageX = ridingPercentageX + (clientRidingPercentageX - ridingPercentageX) / clientInterpolationSteps;
						ridingPercentageZ = ridingPercentageZ + (clientRidingPercentageZ - ridingPercentageZ) / clientInterpolationSteps;

						--clientInterpolationSteps;
						updatePosition(x, y, z);
					} else {
						refreshPosition();
						ridingPercentageX = clientRidingPercentageX;
						ridingPercentageZ = clientRidingPercentageZ;
					}
				} else {
					final Vec3d newPos = player.getPos().add(player.getVelocity());
					updatePosition(newPos.x, newPos.y, newPos.z);
				}
			}
		} else {
			if (player != null) {
				if (!hasPassenger(player)) {
					updatePosition(player.getX(), player.getY(), player.getZ());
				}
				if (seatCoolDown > 0) {
					seatCoolDown--;
				} else {
					removeAllPassengers();
				}

				if (clientRefreshCoolDown == 20) {
					final RailwayData railwayData = RailwayData.getInstance(world);
					if (railwayData != null) {
						PacketTrainDataGuiServer.broadcastS2C(world, railwayData);
					}
				}
				if (clientRefreshCoolDown <= 20) {
					clientRefreshCoolDown++;
				}
			}
			if (player == null || player.isDead()) {
				kill();
			}
		}
	}

	@Override
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
		clientX = x;
		clientY = y;
		clientZ = z;
		clientRidingPercentageX = dataTracker == null ? 0 : dataTracker.get(RIDING_PERCENTAGE_X);
		clientRidingPercentageZ = dataTracker == null ? 0 : dataTracker.get(RIDING_PERCENTAGE_Z);
		clientInterpolationSteps = interpolationSteps + 2;
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}

	@Override
	public boolean shouldRender(double distance) {
		return true;
	}

	@Override
	public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
		return true;
	}

	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(PLAYER_ID, Optional.of(new UUID(0, 0)));
		dataTracker.startTracking(RIDING_PERCENTAGE_X, 0F);
		dataTracker.startTracking(RIDING_PERCENTAGE_Z, 0F);
		dataTracker.startTracking(SCHEDULE_TIME, 0);
		dataTracker.startTracking(ROUTE_ID, 0);
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
	}

	public PlayerEntity getPlayer() {
		if (player == null) {
			player = world.getPlayerByUuid(getPlayerId());
		}
		return player;
	}

	public void setPlayerId(UUID playerId) {
		if (dataTracker != null && playerId != null) {
			dataTracker.set(PLAYER_ID, Optional.of(playerId));
		}
	}

	public void updateRidingPercentage() {
		if (dataTracker != null) {
			dataTracker.set(RIDING_PERCENTAGE_X, ridingPercentageX);
			dataTracker.set(RIDING_PERCENTAGE_Z, ridingPercentageZ);
		}
	}

	public float getRidingPercentageX(float tickDelta) {
		return MathHelper.lerp(tickDelta, prevRidingPercentageX, ridingPercentageX);
	}

	public float getRidingPercentageZ(float tickDelta) {
		return MathHelper.lerp(tickDelta, prevRidingPercentageZ, ridingPercentageZ);
	}

	public void setScheduleTimeAndRouteId(int scheduleTime, long routeId) {
		if (dataTracker != null) {
			dataTracker.set(SCHEDULE_TIME, scheduleTime);
			dataTracker.set(ROUTE_ID, (int) routeId);
		}
		this.scheduleTime = scheduleTime;
		this.routeId = routeId;
	}

	public boolean isScheduleTimeAndRouteId(int scheduleTime, long routeId) {
		if (world.isClient) {
			return dataTracker != null && dataTracker.get(SCHEDULE_TIME) == scheduleTime && dataTracker.get(ROUTE_ID) == routeId;
		} else {
			return this.scheduleTime == scheduleTime && this.routeId == routeId;
		}
	}

	public boolean getIsRiding() {
		return seatCoolDown > 0;
	}

	public void resetSeatCoolDown() {
		seatCoolDown = MAX_SEAT_COOL_DOWN;
	}

	private UUID getPlayerId() {
		try {
			return dataTracker == null ? new UUID(0, 0) : dataTracker.get(PLAYER_ID).orElse(new UUID(0, 0));
		} catch (Exception e) {
			e.printStackTrace();
			return new UUID(0, 0);
		}
	}
}
