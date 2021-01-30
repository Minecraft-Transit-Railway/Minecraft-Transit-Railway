package mtr.entity;

import mtr.MTR;
import mtr.data.Train;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityMTrainMini extends EntityTrainBase {

	public EntityMTrainMini(World world, double x, double y, double z, float yaw, float pitch) {
		super(MTR.M_TRAIN_MINI, world, x, y, z, yaw, pitch);
	}

	public EntityMTrainMini(EntityType<? extends EntityTrainBase> type, World world) {
		super(type, world);
	}

	@Override
	protected Train.TrainType getTrainType() {
		return Train.TrainType.M_TRAIN_MINI;
	}
}
