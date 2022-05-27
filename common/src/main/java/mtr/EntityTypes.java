package mtr;

import mtr.entity.EntityLift;
import mtr.entity.EntitySeat;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;

import java.util.function.BiFunction;

public interface EntityTypes {

	RegistryObject<EntityType<EntitySeat>> SEAT = new RegistryObject<>(() -> EntityType.Builder.<EntitySeat>of(EntitySeat::new, MobCategory.MISC).sized(EntitySeat.SIZE, EntitySeat.SIZE).clientTrackingRange(8).build("seat"));

	enum LiftType {
		SIZE_2_2(2, 2, EntityLift.EntityLift22::new, EntityLift.EntityLift22::new),
		SIZE_3_2(3, 2, EntityLift.EntityLift32::new, EntityLift.EntityLift32::new),
		SIZE_3_3(3, 3, EntityLift.EntityLift33::new, EntityLift.EntityLift33::new),
		SIZE_4_3(4, 3, EntityLift.EntityLift43::new, EntityLift.EntityLift43::new),
		SIZE_4_4(4, 4, EntityLift.EntityLift44::new, EntityLift.EntityLift44::new);

		public final int width;
		public final int depth;
		public final String key;
		public final RegistryObject<EntityType<EntityLift>> registryObject;
		public final LiftSupplier liftSupplier;

		LiftType(int width, int depth, BiFunction<EntityType<?>, Level, EntityLift> function, LiftSupplier liftSupplier) {
			this.width = width;
			this.depth = depth;
			this.key = String.format("lift_%s_%s", width, depth);
			this.registryObject = new RegistryObject<>(() -> EntityType.Builder.of(function::apply, MobCategory.MISC).sized(Math.max(width, depth), EntityLift.HEIGHT).clientTrackingRange(8).build(key));
			this.liftSupplier = liftSupplier;
		}
	}

	@FunctionalInterface
	interface LiftSupplier {
		EntityLift liftSupplier(Level world, double x, double y, double z);
	}
}
