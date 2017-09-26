package MTR;

import net.minecraft.world.World;

public class EntitySP1900 extends EntityTrainBase {

	public EntitySP1900(World world) {
		super(world);
		doorOpen = MTRSounds.sp1900Dooropen;
		doorClose = MTRSounds.sp1900Doorclose;
	}

	public EntitySP1900(World worldIn, double x, double y, double z, boolean f, int h) {
		super(worldIn, x, y, z, f, h);
		doorOpen = MTRSounds.sp1900Dooropen;
		doorClose = MTRSounds.sp1900Doorclose;
	}

	@Override
	public int getTrainLength() {
		return 16; // 25;
	}
}
