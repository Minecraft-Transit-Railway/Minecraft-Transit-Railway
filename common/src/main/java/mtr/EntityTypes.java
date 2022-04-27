package mtr;

import mtr.entity.EntitySeat;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public interface EntityTypes {

	RegistryObject<EntityType<EntitySeat>> SEAT = new RegistryObject<>(() -> EntityType.Builder.<EntitySeat>of(EntitySeat::new, MobCategory.MISC).sized(EntitySeat.SIZE, EntitySeat.SIZE).clientTrackingRange(8).build("seat"));
}
