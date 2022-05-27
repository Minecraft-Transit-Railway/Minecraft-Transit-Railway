package mtr.entity;

import mtr.EntityTypes;
import mtr.block.BlockLiftTrackFloor;
import mtr.data.Train;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public abstract class EntityLift extends EntityBase {

	private long targetFloor = Long.MIN_VALUE;
	private long lastScannedY = Long.MIN_VALUE;
	private double speed;
	private BlockPos trackPos = new BlockPos(0, 0, 0);

	private double movementCoolDown = MOVEMENT_COOLDOWN_DEFAULT;
	private double trackCoolDown = TRACK_COOLDOWN_DEFAULT;

	private final Map<Integer, String> floors = new HashMap<>();
	private final EntityTypes.LiftType liftType;

	private static final int MOVEMENT_COOLDOWN_DEFAULT = 40;
	private static final int TRACK_COOLDOWN_DEFAULT = 60;

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
		final BlockEntity blockEntity = level.getBlockEntity(trackPos);
		final boolean atFloor = blockEntity instanceof BlockLiftTrackFloor.TileEntityLiftTrackFloor;
		final long currentFloor = trackPos.getY();

		if (atFloor) {
			if (targetFloor == Long.MIN_VALUE) {
				targetFloor = currentFloor;
			}
			if (currentFloor != lastScannedY) {
				System.out.println("Arrived at " + ((BlockLiftTrackFloor.TileEntityLiftTrackFloor) blockEntity).getFloorNumber());
				((BlockLiftTrackFloor.TileEntityLiftTrackFloor) blockEntity).scanFloors(floors);
				lastScannedY = currentFloor;
			}
		}

		if (level.isClientSide) {
			setClientPosition();
		} else {
			if (movementCoolDown == 0) {
				final boolean outOfBounds = trackCoolDown < TRACK_COOLDOWN_DEFAULT - 2;

				if (outOfBounds) {
					speed = (currentFloor - getY()) * 2;
				} else {
					final double stoppingDistance = targetFloor - getY();
					final double absStoppingDistance = Math.abs(stoppingDistance);
					if (absStoppingDistance < Train.ACCELERATION_DEFAULT) {
						if (atFloor) {
							speed = stoppingDistance;
							movementCoolDown = MOVEMENT_COOLDOWN_DEFAULT;
						} else {
							speed = Train.ACCELERATION_DEFAULT;
							targetFloor = Long.MIN_VALUE;
						}
					} else {
						if (absStoppingDistance < 0.5 * speed * speed / Train.ACCELERATION_DEFAULT) {
							speed = Math.copySign(Math.max(Math.abs(speed) - 0.5 * speed * speed / absStoppingDistance, Train.ACCELERATION_DEFAULT), stoppingDistance);
						} else {
							speed = Math.copySign(Math.min(Math.abs(speed) + Train.ACCELERATION_DEFAULT, 1), speed);
						}
					}
				}

				if (speed != 0) {
					setDeltaMovement(0, speed, 0);
					setPos(getX(), getY() + speed, getZ());
					if (outOfBounds) {
						speed = Math.copySign(Train.ACCELERATION_DEFAULT, speed);
					}
				}
			} else {
				movementCoolDown--;
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
	protected void defineSynchedData() {
	}

	public void updateByTrack(BlockPos pos) {
		trackCoolDown = TRACK_COOLDOWN_DEFAULT;
		trackPos = new BlockPos(pos.getX(), Math.round(getY()), pos.getZ());
	}

	public static class EntityLift22 extends EntityLift {

		public EntityLift22(EntityType<?> type, Level world) {
			super(EntityTypes.LiftType.SIZE_2_2, type, world);
		}

		public EntityLift22(Level world, double x, double y, double z) {
			super(EntityTypes.LiftType.SIZE_2_2, world, x, y, z);
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

	public static class EntityLift33 extends EntityLift {

		public EntityLift33(EntityType<?> type, Level world) {
			super(EntityTypes.LiftType.SIZE_3_3, type, world);
		}

		public EntityLift33(Level world, double x, double y, double z) {
			super(EntityTypes.LiftType.SIZE_3_3, world, x, y, z);
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

	public static class EntityLift44 extends EntityLift {

		public EntityLift44(EntityType<?> type, Level world) {
			super(EntityTypes.LiftType.SIZE_4_4, type, world);
		}

		public EntityLift44(Level world, double x, double y, double z) {
			super(EntityTypes.LiftType.SIZE_4_4, world, x, y, z);
		}
	}
}
