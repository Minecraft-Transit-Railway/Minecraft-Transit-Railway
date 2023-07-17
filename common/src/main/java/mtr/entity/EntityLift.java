package mtr.entity;

import mtr.EntityTypes;
import mtr.block.BlockLiftTrack;
import mtr.item.ItemLiftRefresher;
import mtr.mappings.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@Deprecated
public abstract class EntityLift extends EntityBase {

	private int removeCoolDown = 0;

	public final EntityTypes.LiftType liftType;

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
		if (level().isClientSide) {
			setClientPosition();
		} else {
			scanTrack();
			if (removeCoolDown > 200) {
				System.out.println(liftType + " lift at " + blockPosition() + " not converted");
				kill();
			} else {
				removeCoolDown++;
			}
		}

		checkInsideBlocks();
	}

	@Override
	protected boolean canAddPassenger(Entity entity) {
		return !hasPassenger(entity);
	}

	@Override
	protected void defineSynchedData() {
	}

	private void scanTrack() {
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				final BlockPos trackPos = blockPosition().offset(x, 0, z);
				if (level().getBlockState(trackPos).getBlock() instanceof BlockLiftTrack) {
					ItemLiftRefresher.refreshLift(level(), trackPos, (int) Math.round(getX() * 2) - trackPos.getX() * 2, (int) Math.round(getZ() * 2) - trackPos.getZ() * 2, liftType.width, liftType.depth, liftType.isDoubleSided, Direction.fromYRot(-Utilities.getYaw(this)));
					System.out.println(liftType + " lift at " + blockPosition() + " converted");
					kill();
					return;
				}
			}
		}
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
