package mtr.entity;

import mtr.MTR;
import mtr.data.Train;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntitySP1900 extends EntityTrainBase {

	public EntitySP1900(World world, double x, double y, double z, float yaw, float pitch) {
		super(MTR.SP1900, world, x, y, z, yaw, pitch);
	}

	public EntitySP1900(EntityType<? extends EntityTrainBase> type, World world) {
		super(type, world);
	}

	@Override
	protected Train.TrainType getTrainType() {
		return Train.TrainType.SP1900;
	}
}
