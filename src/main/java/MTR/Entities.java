package mtr;

import mtr.entity.EntityLightRail1;
import mtr.entity.EntityMTrain;
import mtr.entity.EntitySP1900;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

public class Entities {
	public static final EntityEntry light_rail_1 = EntityEntryBuilder.create().entity(EntityLightRail1.class).id(new ResourceLocation(MTR.MODID, "light_rail_1"), 0).name("light_rail_1").tracker(80, 3, false).build();
	public static final EntityEntry m_train = EntityEntryBuilder.create().entity(EntityMTrain.class).id(new ResourceLocation(MTR.MODID, "m_train"), 1).name("m_train").tracker(80, 3, false).build();
	public static final EntityEntry sp1900 = EntityEntryBuilder.create().entity(EntitySP1900.class).id(new ResourceLocation(MTR.MODID, "sp1900"), 2).name("sp1900").tracker(80, 3, false).build();
}
