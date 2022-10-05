package mtr.entity;

import mtr.EntityTypes;
import mtr.data.Lift;
import mtr.data.LiftInstructions;
import mtr.data.RailwayData;
import mtr.mappings.Utilities;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public abstract class EntityLift extends EntityBase {

	private Lift.LiftDirection liftDirection = Lift.LiftDirection.NONE;
	private int trackCoolDown = TRACK_COOL_DOWN_DEFAULT;
	private int mountCoolDown;

	public final LiftInstructions liftInstructions = new LiftInstructions();
	public final Map<Integer, String> floors = new HashMap<>();
	public final EntityTypes.LiftType liftType;

	private static final int DOOR_MAX = 24;
	private static final int TRACK_COOL_DOWN_DEFAULT = 10;
	private static final float LIFT_WALKING_SPEED_MULTIPLIER = 0.25F;

	public EntityLift(EntityTypes.LiftType liftType, EntityType<?> type, Level world) {
		super(type, world);
		this.liftType = liftType;
		blocksBuilding = true;
	}

	public EntityLift(EntityTypes.LiftType liftType, Level world, double x, double y, double z) {
		this(liftType, liftType.registryObject.get(), world);
		setPos(x, y, z);
		setDeltaMovement(Vec3.ZERO);
		xo = x;
		yo = y;
		zo = z;
	}

	@Override
	public void tick() {
		if (level.isClientSide) {
			setClientPosition();
		} else {
			if (trackCoolDown == 0) {
				kill();
			} else {
				trackCoolDown--;
			}
		}

		checkInsideBlocks();
	}

	@Override
	public void playerTouch(Player player) {
		if (!level.isClientSide) {
			final boolean hasPassenger = hasPassenger(player);
			if (playerInBounds(player)) {
				if (!hasPassenger) {
					player.startRiding(this);
					mountCoolDown = TRACK_COOL_DOWN_DEFAULT;
				}
			} else {
				if (mountCoolDown == 0 && hasPassenger) {
					player.stopRiding();
				}
			}
			if (mountCoolDown > 0) {
				mountCoolDown--;
			}
		}
	}

	@Override
	public void positionRider(Entity entity) {
		if (entity instanceof Player && hasPassenger(entity)) {
			final Vec3 movement = new Vec3(Math.signum(((Player) entity).xxa) * LIFT_WALKING_SPEED_MULTIPLIER, 0, Math.signum(((Player) entity).zza) * LIFT_WALKING_SPEED_MULTIPLIER).yRot((float) -Math.toRadians(Utilities.getYaw(entity)));
			final double movementX = Mth.clamp(entity.getX() - getX() + movement.x, getNegativeXBound(true) + 0.5, getPositiveXBound(true) - 0.5);
			final double movementZ = Mth.clamp(entity.getZ() - getZ() + movement.z, getNegativeZBound(true) + 0.5, getPositiveZBound(true) - 0.5);
			entity.setPos(getX() + movementX, getY(), getZ() + movementZ);
		} else {
			entity.setPos(getX(), getY(), getZ());
		}
	}

	@Override
	protected boolean canAddPassenger(Entity entity) {
		return !hasPassenger(entity);
	}

	@Override
	protected void defineSynchedData() {
	}

	public void pressLiftButton(int floor) {
		liftInstructions.addInstruction((int) Math.round(getY()), liftDirection == Lift.LiftDirection.UP, floor);
	}

	public int getFrontDoorValueClient() {
		return 0;
	}

	public int getBackDoorValueClient() {
		return 0;
	}

	public boolean hasStoppingFloorsClient(int floor, boolean movingUp) {
		return false;
	}

	private double getNegativeXBound(boolean includeDoorCheck) {
		switch ((Math.round(Utilities.getYaw(this)) + 360) % 360) {
			case 0:
			case 180:
				return -liftType.width / 2D;
			case 90:
				return (getFrontDoorValueClient() > 0 && includeDoorCheck ? -5 : 0) - liftType.depth / 2D;
			case 270:
				return (getBackDoorValueClient() > 0 && includeDoorCheck ? -5 : 0) - liftType.depth / 2D;
			default:
				return 0;
		}
	}

	private double getNegativeZBound(boolean includeDoorCheck) {
		switch ((Math.round(Utilities.getYaw(this)) + 360) % 360) {
			case 0:
				return (getFrontDoorValueClient() > 0 && includeDoorCheck ? -5 : 0) - liftType.depth / 2D;
			case 180:
				return (getBackDoorValueClient() > 0 && includeDoorCheck ? -5 : 0) - liftType.depth / 2D;
			case 90:
			case 270:
				return -liftType.width / 2D;
			default:
				return 0;
		}
	}

	private double getPositiveXBound(boolean includeDoorCheck) {
		switch ((Math.round(Utilities.getYaw(this)) + 360) % 360) {
			case 0:
			case 180:
				return liftType.width / 2D;
			case 90:
				return (getBackDoorValueClient() > 0 && includeDoorCheck ? 5 : 0) + liftType.depth / 2D;
			case 270:
				return (getFrontDoorValueClient() > 0 && includeDoorCheck ? 5 : 0) + liftType.depth / 2D;
			default:
				return 0;
		}
	}

	private double getPositiveZBound(boolean includeDoorCheck) {
		switch ((Math.round(Utilities.getYaw(this)) + 360) % 360) {
			case 0:
				return (getBackDoorValueClient() > 0 && includeDoorCheck ? 5 : 0) + liftType.depth / 2D;
			case 180:
				return (getFrontDoorValueClient() > 0 && includeDoorCheck ? 5 : 0) + liftType.depth / 2D;
			case 90:
			case 270:
				return liftType.width / 2D;
			default:
				return 0;
		}
	}

	private boolean playerInBounds(Entity entity) {
		return RailwayData.isBetween(entity.getX() - getX(), getNegativeXBound(false), getPositiveXBound(false)) && RailwayData.isBetween(entity.getZ() - getZ(), getNegativeZBound(false), getPositiveZBound(false));
	}

	public static boolean playerVerticallyNearby(Level world, int x, int z) {
		for (final Player player : world.players()) {
			final int differenceX = Math.abs(player.blockPosition().getX() - x);
			final int differenceZ = Math.abs(player.blockPosition().getZ() - z);
			if (differenceX + differenceZ <= 16) {
				return true;
			}
		}
		return false;
	}

	public static class EntityLift22 extends EntityLift {

		public EntityLift22(EntityType<?> type, Level world) {
			super(EntityTypes.LiftType.SIZE_2_2, type, world);
		}

		public EntityLift22(Level world, double x, double y, double z) {
			super(EntityTypes.LiftType.SIZE_2_2, world, x, y, z);
		}
	}

	public static class EntityLift22DoubleSided extends EntityLift {

		public EntityLift22DoubleSided(EntityType<?> type, Level world) {
			super(EntityTypes.LiftType.SIZE_2_2_DOUBLE_SIDED, type, world);
		}

		public EntityLift22DoubleSided(Level world, double x, double y, double z) {
			super(EntityTypes.LiftType.SIZE_2_2_DOUBLE_SIDED, world, x, y, z);
		}
	}

	public static class EntityLift32 extends EntityLift {

		public EntityLift32(EntityType<?> type, Level world) {
			super(EntityTypes.LiftType.SIZE_3_2, type, world);
		}

		public EntityLift32(Level world, double x, double y, double z) {
			super(EntityTypes.LiftType.SIZE_3_2, world, x, y, z);
		}
	}

	public static class EntityLift32DoubleSided extends EntityLift {

		public EntityLift32DoubleSided(EntityType<?> type, Level world) {
			super(EntityTypes.LiftType.SIZE_3_2_DOUBLE_SIDED, type, world);
		}

		public EntityLift32DoubleSided(Level world, double x, double y, double z) {
			super(EntityTypes.LiftType.SIZE_3_2_DOUBLE_SIDED, world, x, y, z);
		}
	}

	public static class EntityLift33 extends EntityLift {

		public EntityLift33(EntityType<?> type, Level world) {
			super(EntityTypes.LiftType.SIZE_3_3, type, world);
		}

		public EntityLift33(Level world, double x, double y, double z) {
			super(EntityTypes.LiftType.SIZE_3_3, world, x, y, z);
		}
	}

	public static class EntityLift33DoubleSided extends EntityLift {

		public EntityLift33DoubleSided(EntityType<?> type, Level world) {
			super(EntityTypes.LiftType.SIZE_3_3_DOUBLE_SIDED, type, world);
		}

		public EntityLift33DoubleSided(Level world, double x, double y, double z) {
			super(EntityTypes.LiftType.SIZE_3_3_DOUBLE_SIDED, world, x, y, z);
		}
	}

	public static class EntityLift43 extends EntityLift {

		public EntityLift43(EntityType<?> type, Level world) {
			super(EntityTypes.LiftType.SIZE_4_3, type, world);
		}

		public EntityLift43(Level world, double x, double y, double z) {
			super(EntityTypes.LiftType.SIZE_4_3, world, x, y, z);
		}
	}

	public static class EntityLift43DoubleSided extends EntityLift {

		public EntityLift43DoubleSided(EntityType<?> type, Level world) {
			super(EntityTypes.LiftType.SIZE_4_3_DOUBLE_SIDED, type, world);
		}

		public EntityLift43DoubleSided(Level world, double x, double y, double z) {
			super(EntityTypes.LiftType.SIZE_4_3_DOUBLE_SIDED, world, x, y, z);
		}
	}

	public static class EntityLift44 extends EntityLift {

		public EntityLift44(EntityType<?> type, Level world) {
			super(EntityTypes.LiftType.SIZE_4_4, type, world);
		}

		public EntityLift44(Level world, double x, double y, double z) {
			super(EntityTypes.LiftType.SIZE_4_4, world, x, y, z);
		}
	}

	public static class EntityLift44DoubleSided extends EntityLift {

		public EntityLift44DoubleSided(EntityType<?> type, Level world) {
			super(EntityTypes.LiftType.SIZE_4_4_DOUBLE_SIDED, type, world);
		}

		public EntityLift44DoubleSided(Level world, double x, double y, double z) {
			super(EntityTypes.LiftType.SIZE_4_4_DOUBLE_SIDED, world, x, y, z);
		}
	}
}
