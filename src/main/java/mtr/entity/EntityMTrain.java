package mtr.entity;

import mtr.MTR;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityMTrain extends EntityTrainBase {

	public EntityMTrain(World world, double x, double y, double z) {
		super(MTR.M_TRAIN, world, x, y, z);
	}

	public EntityMTrain(EntityType<?> type, World world) {
		super(type, world);
	}
}
