package mtr.entity;

import mtr.EntityTypes;
import mtr.block.BlockLiftTrackFloor;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.data.LiftInstructions;
import mtr.data.RailwayData;
import mtr.data.Train;
import mtr.mappings.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public abstract class EntityLift extends EntityBase {

	private LiftDirection liftDirection = LiftDirection.NONE;
	private double speed;
	private BlockPos trackPos = new BlockPos(0, 0, 0);
	private int scanCoolDown;

	private boolean doorOpen = true;
	private int doorValue = DOOR_MAX;
	private int trackCoolDown = TRACK_COOL_DOWN_DEFAULT;
	private int mountCoolDown;

	public final LiftInstructions liftInstructions = new LiftInstructions(instructionsString -> entityData.set(STOPPING_FLOORS, instructionsString));
	public final Map<Integer, String> floors = new HashMap<>();
	public final EntityTypes.LiftType liftType;

	private static final int DOOR_MAX = 24;
	private static final int TRACK_COOL_DOWN_DEFAULT = 10;
	private static final int SCAN_COOL_DOWN_DEFAULT = 60;
	private static final float LIFT_WALKING_SPEED_MULTIPLIER = 0.25F;
	private static final EntityDataAccessor<Integer> DOOR_VALUE = SynchedEntityData.defineId(EntityLift.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> FRONT_OPEN = SynchedEntityData.defineId(EntityLift.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> BACK_OPEN = SynchedEntityData.defineId(EntityLift.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> DIRECTION = SynchedEntityData.defineId(EntityLift.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<String> STOPPING_FLOORS = SynchedEntityData.defineId(EntityLift.class, EntityDataSerializers.STRING);

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
		if (!playerVerticallyNearby(level, blockPosition().getX(), blockPosition().getZ())) {
			return;
		}

		final BlockEntity blockEntity = level.getBlockEntity(trackPos);
		if (blockEntity instanceof BlockLiftTrackFloor.TileEntityLiftTrackFloor) {
			((BlockLiftTrackFloor.TileEntityLiftTrackFloor) blockEntity).setEntityLift(this);
			if (floors.isEmpty() || scanCoolDown >= SCAN_COOL_DOWN_DEFAULT) {
				((BlockLiftTrackFloor.TileEntityLiftTrackFloor) blockEntity).scanFloors(floors);
				scanCoolDown = 0;
			}
		}
		scanCoolDown++;

		if (level.isClientSide) {
			setClientPosition();
		} else {
			if (liftInstructions.hasInstructions() && doorValue == DOOR_MAX * 2) {
				doorOpen = false;
				liftInstructions.getTargetFloor(targetFloor -> entityData.set(DIRECTION, (targetFloor > getY() ? LiftDirection.UP : LiftDirection.DOWN).ordinal()));
			} else if (!liftInstructions.hasInstructions()) {
				entityData.set(DIRECTION, LiftDirection.NONE.ordinal());
			}

			if (floors.isEmpty() && scanCoolDown >= SCAN_COOL_DOWN_DEFAULT) {
				doorOpen = false;
				doorValue = 0;
				entityData.set(DOOR_VALUE, 0);
				final int currentFloor = (int) Math.round(getY());
				liftInstructions.addInstruction(currentFloor, true, currentFloor + 1);
			}

			if (!doorOpen && doorValue == 0) {
				liftInstructions.getTargetFloor(targetFloor -> {
					final double stoppingDistance = Math.abs(targetFloor - getY());
					liftDirection = stoppingDistance < Train.ACCELERATION_DEFAULT ? LiftDirection.NONE : targetFloor > getY() ? LiftDirection.UP : LiftDirection.DOWN;

					if (liftDirection == LiftDirection.NONE) {
						speed = 0;
						doorOpen = true;
						setDeltaMovement(0, 0, 0);
						setPos(getX(), targetFloor, getZ());
						liftInstructions.arrived();

						if (blockEntity instanceof BlockLiftTrackFloor.TileEntityLiftTrackFloor && ((BlockLiftTrackFloor.TileEntityLiftTrackFloor) blockEntity).getShouldDing()) {
							level.playSound(null, blockPosition(), SoundEvents.NOTE_BLOCK_PLING, SoundSource.BLOCKS, 16, 2);
						}
					} else {
						if (stoppingDistance < 0.5 * speed * speed / Train.ACCELERATION_DEFAULT) {
							speed = Math.max(speed - 0.5 * speed * speed / stoppingDistance, Train.ACCELERATION_DEFAULT);
						} else {
							speed = Math.min(speed + Train.ACCELERATION_DEFAULT, 1);
						}

						final double velocity = speed * liftDirection.speedMultiplier;
						setDeltaMovement(0, velocity, 0);
						setPos(getX(), getY() + velocity, getZ());
					}
				});
			} else {
				if (!doorOpen && doorValue > 0 || doorOpen && doorValue < DOOR_MAX * 2 || level.getNearestPlayer(this, 8) != null) {
					if (doorOpen) {
						doorValue = Math.min(doorValue + 1, DOOR_MAX * 2);
					} else {
						doorValue = Math.max(doorValue - 1, 0);
					}

					entityData.set(DOOR_VALUE, doorValue);
					entityData.set(FRONT_OPEN, checkDoor(true));
					if (liftType.isDoubleSided) {
						entityData.set(BACK_OPEN, checkDoor(false));
					}
				}
			}

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
	public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
		return livingEntity.position().add(0, playerInBounds(livingEntity) ? (liftDirection == LiftDirection.UP ? speed : 0) + 3 : 0, 0);
	}

	@Override
	protected boolean canAddPassenger(Entity entity) {
		return !hasPassenger(entity);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(DOOR_VALUE, DOOR_MAX);
		entityData.define(FRONT_OPEN, false);
		entityData.define(BACK_OPEN, false);
		entityData.define(DIRECTION, 0);
		entityData.define(STOPPING_FLOORS, "");
	}

	public LiftDirection getLiftDirection() {
		return liftDirection;
	}

	public void pressLiftButton(int floor) {
		liftInstructions.addInstruction((int) Math.round(getY()), liftDirection == LiftDirection.UP, floor);
	}

	public int getFrontDoorValueClient() {
		return entityData.get(FRONT_OPEN) ? Math.min(DOOR_MAX, entityData.get(DOOR_VALUE)) : 0;
	}

	public int getBackDoorValueClient() {
		return liftType.isDoubleSided && entityData.get(BACK_OPEN) ? Math.min(DOOR_MAX, entityData.get(DOOR_VALUE)) : 0;
	}

	public LiftDirection getLiftDirectionClient() {
		return LiftDirection.values()[Math.abs(entityData.get(DIRECTION)) % LiftDirection.values().length];
	}

	public boolean hasStoppingFloorsClient(int floor, boolean movingUp) {
		return entityData.get(STOPPING_FLOORS).contains(LiftInstructions.getStringPart(floor, movingUp));
	}

	public void updateByTrack(BlockPos pos) {
		trackCoolDown = TRACK_COOL_DOWN_DEFAULT;
		trackPos = new BlockPos(pos.getX(), Math.round(getY()), pos.getZ());
	}

	public void hasButton(int floor, boolean[] hasButton) {
		floors.keySet().forEach(checkFloor -> {
			if (checkFloor > floor) {
				hasButton[0] = true;
			}
			if (checkFloor < floor) {
				hasButton[1] = true;
			}
		});
	}

	public String[] getCurrentFloorDisplay() {
		final String[] result = {"", ""};
		final String[] resultTemp = floors.keySet().stream().min(Comparator.comparingInt(a -> (int) Math.abs(getY() - a))).map(floors::get).orElse("").split("\\|\\|");
		System.arraycopy(resultTemp, 0, result, 0, Math.min(resultTemp.length, 2));
		return result;
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

	private boolean checkDoor(boolean front) {
		final Direction direction = Direction.fromYRot(-Utilities.getYaw(this));
		final Direction directionClockwise = direction.getClockWise();
		final int sign = front ? 1 : -1;
		boolean hasDoor = false;
		for (int i = -1; i <= 1; i++) {
			final BlockPos checkPos = new BlockPos(position().add(-direction.getStepX() * sign * (liftType.depth / 2F + 0.5) + directionClockwise.getStepX() * i, 0, -direction.getStepZ() * sign * (liftType.depth / 2F + 0.5) + directionClockwise.getStepZ() * i));
			final BlockState checkState1 = level.getBlockState(checkPos);
			final BlockState checkState2 = level.getBlockState(checkPos.above());
			if (checkState1.getBlock() instanceof BlockPSDAPGDoorBase && checkState2.getBlock() instanceof BlockPSDAPGDoorBase) {
				level.setBlockAndUpdate(checkPos, checkState1.setValue(BlockPSDAPGDoorBase.OPEN, Math.min(doorValue, DOOR_MAX)));
				level.setBlockAndUpdate(checkPos.above(), checkState2.setValue(BlockPSDAPGDoorBase.OPEN, Math.min(doorValue, DOOR_MAX)));
				hasDoor = true;
			}
		}
		return hasDoor;
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

	public enum LiftDirection {
		NONE(0), UP(1), DOWN(-1);

		private final int speedMultiplier;

		LiftDirection(int speedMultiplier) {
			this.speedMultiplier = speedMultiplier;
		}
	}
}
