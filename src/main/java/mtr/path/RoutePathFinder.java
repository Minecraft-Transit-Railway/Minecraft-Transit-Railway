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

	public RoutePathFinder(WorldAccess world, BlockPos start1, BlockPos start2, Station destinationStation) {
		super(world);
		this.destinationStation = destinationStation;
		destinationPos = new BlockPos((destinationStation.corner1.getLeft() + destinationStation.corner2.getLeft()) / 2, 0, (destinationStation.corner1.getRight() + destinationStation.corner2.getRight()) / 2);

		if (distanceBetween(start1, destinationPos) > distanceBetween(start2, destinationPos)) {
			path.add(start1);
			blacklist.add(start1);
		} else {
			path.add(start2);
			blacklist.add(start2);
		}
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
