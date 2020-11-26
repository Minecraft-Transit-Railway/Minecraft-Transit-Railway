package mtr.path;

import mtr.block.BlockPlatformRail;
import mtr.data.Pos3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoutePathFinder extends PathFinderBase {

	public RoutePathFinder(WorldAccess world, BlockPos start, BlockPos destination) {
		super(world, start, destination);
	}

	@Override
	public List<Pos3f> findPath() {
		boolean enteredDestination = false;
		while (!path.isEmpty()) {
			final BlockPos lastPos = path.get(path.size() - 1);

			if (enteredDestination && !(world.getBlockState(lastPos).getBlock() instanceof BlockPlatformRail)) {
				removeLastItem();
				return smoothPath();
			} else {
				if (lastPos.equals(destination)) {
					enteredDestination = true;
				}

				Optional<BlockPosWeighted> blockPosWeighted = getConnectedPositions(lastPos).filter(blockPos -> !blacklist.contains(blockPos)).map(blockPos -> new BlockPosWeighted(blockPos, distanceBetween(blockPos, destination))).min(BlockPosWeighted::compareTo);
				if (blockPosWeighted.isPresent()) {
					blacklist.add(blockPosWeighted.get().pos);
					path.add(blockPosWeighted.get().pos);
				} else {
					removeLastItem();
				}
			}
		}
		return new ArrayList<>();
	}
}
