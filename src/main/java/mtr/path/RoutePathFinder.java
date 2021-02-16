package mtr.path;

import mtr.data.Platform;
import mtr.data.Pos3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoutePathFinder extends PathFinderBase {

	private final int trimStart;

	public RoutePathFinder(WorldAccess world, Platform platformStart, Platform platformDestination) {
		super(world, platformStart.getMidPos(), platformDestination.getMidPos());
		trimStart = 1;
	}

	@Override
	public List<Pos3f> findPath() {
		if (path.get(0).equals(destination)) {
			return new ArrayList<>();
		}

		boolean enteredDestination = false;
		while (!path.isEmpty()) {
			final BlockPos lastPos = path.get(path.size() - 1);

			if (enteredDestination) {
				removeLastItem();
				if (trimStart < path.size()) {
					path.subList(0, trimStart).clear();
				}
				return smoothPath();
			} else {
				if (lastPos.equals(destination)) {
					enteredDestination = true;
				}

				Optional<BlockPosWeighted> blockPosWeighted = getConnectedPositions(lastPos).filter(blockPos -> !blacklist.contains(blockPos)).map(blockPos -> new BlockPosWeighted(blockPos, distanceSquaredBetween(blockPos, destination))).min(BlockPosWeighted::compareTo);
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
