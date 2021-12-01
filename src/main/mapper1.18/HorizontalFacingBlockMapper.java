package mapper;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class HorizontalFacingBlockMapper extends HorizontalFacingBlock {

	public HorizontalFacingBlockMapper(Settings settings) {
		super(settings);
	}

	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		super.onLandedUpon(world, state, pos, entity, fallDistance * (softenLanding() ? 0.5F : 1));
	}

	public boolean softenLanding() {
		return false;
	}
}
