package mtr;

import mtr.entity.EntitySP1900;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

public class Entities {
	public static final EntityEntry sp1900 = EntityEntryBuilder.create().entity(EntitySP1900.class).id(new ResourceLocation(MTR.MODID, "sp1900"), 0).name("sp1900").tracker(80, 3, false).build();
}
