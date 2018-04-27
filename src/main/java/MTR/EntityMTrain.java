package MTR;

import net.minecraft.world.World;

public class EntityMTrain extends EntityTrainBase {

	public EntityMTrain(World world) {
		super(world);
		doorOpen = MTRSounds.sp1900Dooropen;
		doorClose = MTRSounds.sp1900Doorclose;
	}

	public EntityMTrain(World worldIn, double x, double y, double z, boolean f, int h) {
		super(worldIn, x, y, z, f, h);
		doorOpen = MTRSounds.sp1900Dooropen;
		doorClose = MTRSounds.sp1900Doorclose;
	}

	@Override
	public int getTrainLength() {
		return 16; // 25;
	}
}
