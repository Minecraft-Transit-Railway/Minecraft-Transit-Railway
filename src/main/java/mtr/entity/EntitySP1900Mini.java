package mtr.entity;

import mtr.MTR;
import mtr.data.Train;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntitySP1900Mini extends EntityTrainBase {

	public EntitySP1900Mini(World world, double x, double y, double z, float yaw, float pitch) {
		super(MTR.SP1900_MINI, world, x, y, z, yaw, pitch);
	}

	public EntitySP1900Mini(EntityType<? extends EntityTrainBase> type, World world) {
		super(type, world);
	}

	@Override
	protected Train.TrainType getTrainType() {
		return Train.TrainType.SP1900_MINI;
	}
}
