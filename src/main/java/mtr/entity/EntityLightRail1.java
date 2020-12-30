package mtr.entity;

import mtr.MTR;
import mtr.data.Train;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityLightRail1 extends EntityTrainBase {

	public EntityLightRail1(World world, double x, double y, double z) {
		super(MTR.LIGHT_RAIL_1, world, x, y, z);
	}

	public EntityLightRail1(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	protected Train.TrainType getTrainType() {
		return Train.TrainType.LIGHT_RAIL_1;
	}
}
