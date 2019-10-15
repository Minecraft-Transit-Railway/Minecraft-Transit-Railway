package mtr.item;

import mtr.entity.EntityLightRail1;
import mtr.entity.EntityTrain;
import net.minecraft.world.World;

public class ItemLightRail1 extends ItemSpawnTrain {

	@Override
	protected EntityTrain getTrain(World worldIn, double x, double y, double z, int trainType) {
		return new EntityLightRail1(worldIn, x, y, z, trainType);
	}

	@Override
	protected int getSubtypeCount() {
		return 1;
	}
}
