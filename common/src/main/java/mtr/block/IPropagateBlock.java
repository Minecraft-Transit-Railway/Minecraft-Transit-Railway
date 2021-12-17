package mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public interface IPropagateBlock {

	IntegerProperty PROPAGATE_PROPERTY = IntegerProperty.create("propagate_property", 0, 3);

	default void propagate(Level world, BlockPos pos, Direction direction, int maxBlocksAway) {
		for (int i = 1; i <= maxBlocksAway; i++) {
			final BlockPos offsetPos = pos.relative(direction, i);
			final BlockState offsetState = world.getBlockState(offsetPos);
			if (this == offsetState.getBlock()) {
				world.setBlockAndUpdate(offsetPos, offsetState.setValue(PROPAGATE_PROPERTY, IBlock.getStatePropertySafe(world, pos, PROPAGATE_PROPERTY)));
				propagate(world, offsetPos, direction, maxBlocksAway);
				return;
			}
		}
	}
}
