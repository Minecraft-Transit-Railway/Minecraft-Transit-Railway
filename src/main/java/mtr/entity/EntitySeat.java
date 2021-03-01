package mtr.entity;

import mtr.MTR;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("EntityConstructor")
public class EntitySeat extends Entity {

	public int carRiding;
	public double xRidingOffset, zRidingOffset;

	private PlayerEntity player;
	private int seatCoolDown;

	public static final int DETAIL_RADIUS = 32;
	public static final int MAX_SEAT_COOL_DOWN = 10;

	private static final TrackedData<Optional<UUID>> PLAYER_ID = DataTracker.registerData(EntitySeat.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
	private static final String KEY_PLAYER_ID = "player_id";

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
				final Vec3d newPos = new Vec3d(0, 0, 10).rotateX((float) Math.toRadians(-player.pitch)).rotateY((float) Math.toRadians(-player.yaw)).add(player.getX(), player.getEyeY(), player.getZ());
				updatePosition(newPos.x, newPos.y, newPos.z);
			}
		} else {
			if (player != null) {
				updatePosition(player.getX(), player.getY(), player.getZ());
			} else {
				kill();
			}
		}
	}

	@Override
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
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
		dataTracker.startTracking(PLAYER_ID, null);
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
		setPlayerId(tag.getUuid(KEY_PLAYER_ID));
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
		tag.putUuid(KEY_PLAYER_ID, getPlayerId());
	}

	public void clientRenderTick() {
		if (seatCoolDown > 0) {
			seatCoolDown--;
		}
	}

	public PlayerEntity getPlayer() {
		if (player == null) {
			player = world.getPlayerByUuid(getPlayerId());
		}
		return player;
	}

	public void setPlayerId(UUID playerId) {
		dataTracker.set(PLAYER_ID, Optional.of(playerId));
	}

	public boolean getIsRiding() {
		return seatCoolDown > 0;
	}

	public void resetSeatCoolDown() {
		seatCoolDown = MAX_SEAT_COOL_DOWN;
	}

	private UUID getPlayerId() {
		return dataTracker.get(PLAYER_ID).orElse(new UUID(0, 0));
	}
}
