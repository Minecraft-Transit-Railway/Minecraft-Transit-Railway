package mtr.entity;

import mtr.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityLightRail1 extends EntityTrain {

	public EntityLightRail1(World worldIn) {
		super(worldIn);
	}

	public EntityLightRail1(World worldIn, double x, double y, double z, int trainType) {
		super(worldIn, x, y, z, trainType);
	}

	@Override
	public int getSiblingSpacing() {
		return 15;
	}

	@Override
	public int getEndSpacing() {
		return 5;
	}

	@Override
	protected Item getTrainItem() {
		return Items.sp1900;
	}
}
