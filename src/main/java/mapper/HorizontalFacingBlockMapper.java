package mapper;

import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class HorizontalFacingBlockMapper extends HorizontalFacingBlock {

	public HorizontalFacingBlockMapper(Settings settings) {
		super(settings);
	}

	@Override
	public final void onLandedUpon(World world, BlockPos pos, Entity entity, float distance) {
		super.onLandedUpon(world, pos, entity, distance * (softenLanding() ? 0.5F : 1));
	}

	public boolean softenLanding() {
		return false;
	}
}
