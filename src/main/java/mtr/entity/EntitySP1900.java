package mtr.entity;

import mtr.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntitySP1900 extends EntityTrain {

	public EntitySP1900(World worldIn) {
		super(worldIn);
	}

	public EntitySP1900(World worldIn, double x, double y, double z, int trainType) {
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
