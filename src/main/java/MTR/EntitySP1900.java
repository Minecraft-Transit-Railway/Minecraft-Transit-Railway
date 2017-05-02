package MTR;

import net.minecraft.world.World;

public class EntitySP1900 extends EntityTrainBase {

	public EntitySP1900(World world) {
		super(world);
		name = "SP1900";
	}

	public EntitySP1900(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
		name = "SP1900";
	}

	@Override
	protected int getTrainLength() {
		return 25;
	}
}
