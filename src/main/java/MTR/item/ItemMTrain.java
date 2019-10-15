package mtr.item;

import mtr.entity.EntityMTrain;
import mtr.entity.EntityTrain;
import net.minecraft.world.World;

public class ItemMTrain extends ItemSpawnTrain {

	@Override
	protected EntityTrain getTrain(World worldIn, double x, double y, double z, int trainType) {
		return new EntityMTrain(worldIn, x, y, z, trainType);
	}

	@Override
	protected int getSubtypeCount() {
		return 2;
	}
}
