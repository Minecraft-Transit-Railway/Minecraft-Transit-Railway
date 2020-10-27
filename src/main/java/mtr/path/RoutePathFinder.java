package mtr.path;

import mtr.block.BlockPlatformRail;
import mtr.data.Pos3f;
import mtr.data.Station;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoutePathFinder extends PathFinderBase {

	private final Station destinationStation;
	private final BlockPos destinationPos;

	public RoutePathFinder(WorldAccess world, BlockPos start, Station destinationStation) {
		super(world, start);
		this.destinationStation = destinationStation;
		destinationPos = destinationStation.getCenter();
	}

	@Override
	public List<Pos3f> findPath() {
		boolean enteredPlatform = false;
		while (!path.isEmpty()) {
			final BlockPos lastPos = path.get(path.size() - 1);
			final boolean inStation = destinationStation.inStation(lastPos.getX(), lastPos.getZ());
			final boolean inStationPlatform = inStation && world.getBlockState(lastPos).getBlock() instanceof BlockPlatformRail;

			if (enteredPlatform && !inStationPlatform) {
				removeLastItem();
				return smoothPath();
			} else {
				if (inStationPlatform) {
					enteredPlatform = true;
				}

				Optional<BlockPosWeighted> blockPosWeighted = getConnectedPositions(lastPos).filter(blockPos -> !blacklist.contains(blockPos)).map(blockPos -> new BlockPosWeighted(blockPos, distanceBetween(blockPos, destinationPos))).min(BlockPosWeighted::compareTo);
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
