package mtr.entity;

import mtr.MTR;
import mtr.data.Train;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityMTrain extends EntityTrainBase {

	public EntityMTrain(World world, double x, double y, double z, float yaw, float pitch) {
		super(MTR.M_TRAIN, world, x, y, z, yaw, pitch);
	}

	public EntityMTrain(EntityType<? extends EntityTrainBase> type, World world) {
		super(type, world);
	}

	@Override
	protected Train.TrainType getTrainType() {
		return Train.TrainType.M_TRAIN;
	}
}
