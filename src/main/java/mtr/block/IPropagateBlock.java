package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface IPropagateBlock {

	IntProperty PROPAGATE_PROPERTY = IntProperty.of("propagate_property", 0, 3);

	default void propagate(World world, BlockPos pos, Direction direction, int maxBlocksAway) {
		for (int i = 1; i <= maxBlocksAway; i++) {
			final BlockPos offsetPos = pos.offset(direction, i);
			final BlockState offsetState = world.getBlockState(offsetPos);
			if (((Block) this).is(offsetState.getBlock())) {
				world.setBlockState(offsetPos, offsetState.with(PROPAGATE_PROPERTY, IBlock.getStatePropertySafe(world, pos, PROPAGATE_PROPERTY)));
				propagate(world, offsetPos, direction, maxBlocksAway);
				return;
			}
		}
	}
}
