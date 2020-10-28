package mtr.entity;

import mtr.MTR;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntitySP1900 extends EntityTrainBase {

	public EntitySP1900(World world, double x, double y, double z) {
		super(MTR.SP1900, world, x, y, z);
	}

	public EntitySP1900(EntityType<?> type, World world) {
		super(type, world);
	}
}
