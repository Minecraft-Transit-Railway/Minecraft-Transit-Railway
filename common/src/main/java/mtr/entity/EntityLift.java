package mtr.entity;

import mtr.EntityTypes;
import mtr.block.BlockLiftTrack;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class EntityLift extends EntityBase {

	private double trackCoolDown;

	private final EntityTypes.LiftType liftType;

	public static final int HEIGHT = 4;

	public EntityLift(EntityTypes.LiftType liftType, EntityType<?> type, Level world) {
		super(type, world);
		this.liftType = liftType;
		blocksBuilding = true;
		trackCoolDown = 2;
	}

	public EntityLift(EntityTypes.LiftType liftType, Level world, double x, double y, double z) {
		this(liftType, liftType.registryObject.get(), world);
		absMoveTo(x, y, z);
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
	protected void onInsideBlock(BlockState blockState) {
		final Block block = blockState.getBlock();
		if (block instanceof BlockLiftTrack) {
			trackCoolDown = 2;
		}
	}

	@Override
	protected void defineSynchedData() {
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
