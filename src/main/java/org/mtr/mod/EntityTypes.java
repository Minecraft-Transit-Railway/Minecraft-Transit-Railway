package mtr;

import mtr.entity.EntityLift;
import mtr.entity.EntitySeat;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;

import java.util.function.BiFunction;

public interface EntityTypes {

	RegistryObject<EntityType<EntitySeat>> SEAT = new RegistryObject<>(() -> EntityType.Builder.<EntitySeat>of(EntitySeat::new, MobCategory.MISC).sized(EntitySeat.SIZE, EntitySeat.SIZE).clientTrackingRange(8).build("seat"));

	@Deprecated
	enum LiftType {
		SIZE_2_2(2, 2, false, EntityLift.EntityLift22::new, EntityLift.EntityLift22::new),
		SIZE_2_2_DOUBLE_SIDED(2, 2, true, EntityLift.EntityLift22DoubleSided::new, EntityLift.EntityLift22DoubleSided::new),
		SIZE_3_2(3, 2, false, EntityLift.EntityLift32::new, EntityLift.EntityLift32::new),
		SIZE_3_2_DOUBLE_SIDED(3, 2, true, EntityLift.EntityLift32DoubleSided::new, EntityLift.EntityLift32DoubleSided::new),
		SIZE_3_3(3, 3, false, EntityLift.EntityLift33::new, EntityLift.EntityLift33::new),
		SIZE_3_3_DOUBLE_SIDED(3, 3, true, EntityLift.EntityLift33DoubleSided::new, EntityLift.EntityLift33DoubleSided::new),
		SIZE_4_3(4, 3, false, EntityLift.EntityLift43::new, EntityLift.EntityLift43::new),
		SIZE_4_3_DOUBLE_SIDED(4, 3, true, EntityLift.EntityLift43DoubleSided::new, EntityLift.EntityLift43DoubleSided::new),
		SIZE_4_4(4, 4, false, EntityLift.EntityLift44::new, EntityLift.EntityLift44::new),
		SIZE_4_4_DOUBLE_SIDED(4, 4, true, EntityLift.EntityLift44DoubleSided::new, EntityLift.EntityLift44DoubleSided::new);

		public final int width;
		public final int depth;
		public final boolean isDoubleSided;
		public final String key;
		public final RegistryObject<EntityType<EntityLift>> registryObject;
		public final LiftSupplier liftSupplier;

		LiftType(int width, int depth, boolean isDoubleSided, BiFunction<EntityType<?>, Level, EntityLift> function, LiftSupplier liftSupplier) {
			this.width = width;
			this.depth = depth;
			this.isDoubleSided = isDoubleSided;
			key = String.format("lift_%s_%s" + (isDoubleSided ? "_double_sided" : ""), width, depth);
			registryObject = new RegistryObject<>(() -> EntityType.Builder.of(function::apply, MobCategory.MISC).sized(Math.max(width, depth) - 0.5F, 1).clientTrackingRange(8).build(key));
			this.liftSupplier = liftSupplier;
		}
	}

	@FunctionalInterface
	interface LiftSupplier {
		EntityLift liftSupplier(Level world, double x, double y, double z);
	}
}
