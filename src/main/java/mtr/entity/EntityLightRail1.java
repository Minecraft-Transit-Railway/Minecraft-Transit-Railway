package mtr.entity;

import mtr.MTR;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityLightRail1 extends EntityTrainBase {

	public EntityLightRail1(World world, double x, double y, double z) {
		super(MTR.LIGHT_RAIL_1, world, x, y, z);
	}

	public EntityLightRail1(EntityType<?> type, World world) {
		super(type, world);
	}
}
