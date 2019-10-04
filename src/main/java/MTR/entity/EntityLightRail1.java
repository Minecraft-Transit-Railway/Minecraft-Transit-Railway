package mtr.entity;

import net.minecraft.world.World;

public class EntityLightRail1 extends EntityTrain {

	public EntityLightRail1(World worldIn) {
		super(worldIn);
	}

	public EntityLightRail1(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	@Override
	public int getSpacing() {
		return 3;
	}
}
