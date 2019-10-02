package mtr.entity;

import net.minecraft.world.World;

public class EntitySP1900 extends EntityTrain {

	public EntitySP1900(World worldIn) {
		super(worldIn);
	}

	public EntitySP1900(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	@Override
	public int getSpacing() {
		return 3;
	}
}
