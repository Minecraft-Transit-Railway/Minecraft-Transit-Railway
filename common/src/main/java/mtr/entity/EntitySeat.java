package mtr.entity;

import mtr.EntityTypes;
import mtr.Registry;
import mtr.data.RailwayData;
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

	private int seatRefresh;
	private Player player;
	private Player clientPlayer;

	private int clientInterpolationSteps;
	private double clientX;
	private double clientY;
	private double clientZ;

	public static final float SIZE = 0.5F;
	private static final int SEAT_REFRESH = 10;
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
		if (level.isClientSide) {
			if (clientPlayer == null) {
				clientPlayer = entityData == null ? null : entityData.get(PLAYER_ID).map(value -> level.getPlayerByUUID(value)).orElse(null);
			}

			if (clientPlayer == null) {
				if (clientInterpolationSteps > 0) {
					final double x = getX() + (clientX - getX()) / clientInterpolationSteps;
					final double y = getY() + (clientY - getY()) / clientInterpolationSteps;
					final double z = getZ() + (clientZ - getZ()) / clientInterpolationSteps;
					--clientInterpolationSteps;
					absMoveTo(x, y, z);
				} else {
					reapplyPosition();
				}
			} else {
				setPos(clientPlayer.getX(), clientPlayer.getY(), clientPlayer.getZ());
			}
		} else {
			if (player == null || seatRefresh <= 0) {
				kill();
			} else {
				setPos(player.getX(), player.getY(), player.getZ());

				final RailwayData railwayData = RailwayData.getInstance(level);
				if (railwayData != null) {
					railwayData.railwayDataCoolDownModule.updatePlayerSeatCoolDown(player);
				}

				seatRefresh--;
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
}
