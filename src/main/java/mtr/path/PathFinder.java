package mtr.path;

import mtr.block.BlockRail;
import mtr.data.Platform;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.*;

public class PathFinder {

	private final WorldAccess world;
	private final List<Platform> platforms;
	private final List<PathData> path;

	public PathFinder(WorldAccess world, List<Platform> platforms) {
		this.world = world;
		this.platforms = platforms;
		path = new ArrayList<>();
	}

	public List<PathData> findPath() {
		if (platforms.size() < 2) {
			return new ArrayList<>();
		}

		path.clear();
		float tOffset = 0;

		for (int i = 1; i < platforms.size(); i++) {
			final Platform platform1 = platforms.get(i - 1);
			final Platform platform2 = platforms.get(i);

			final List<BlockPos> tempPath = getTempPath(platform1, platform2, true);
			if (tempPath.isEmpty()) {
				tempPath.addAll(getTempPath(platform1, platform2, false));
				if (tempPath.isEmpty()) {
					return path;
				}
			}

			if (i == 1) {
				tOffset = generatePathData(tempPath.subList(0, 2), tOffset, 1);
			}
			tempPath.remove(0);

			tOffset = generatePathData(tempPath, tOffset, 0);
		}

		return path;
	}

	private float generatePathData(List<BlockPos> tempPath, float tOffset, float initialSpeed) {
		float speed = initialSpeed;
		for (int j = 1; j < tempPath.size(); j++) {
			final BlockPos tempPos = tempPath.get(j - 1);
			final BlockEntity entity = world.getBlockEntity(tempPos);
			if (entity instanceof BlockRail.TileEntityRail) {
				final PathData pathData = new PathData(((BlockRail.TileEntityRail) entity).railMap.get(tempPath.get(j)), speed, j == tempPath.size() - 1, tOffset);
				path.add(pathData);
				tOffset += pathData.getTime();
				speed = pathData.finalSpeed;
			}
		}
		return tOffset;
	}

	private List<BlockPos> getTempPath(Platform platform1, Platform platform2, boolean reverse) {
		final Set<BlockPos> blacklist = new HashSet<>();
		final List<BlockPos> tempPath = platform1.getOrderedPositions(platform2.getMidPos(), reverse);
		final BlockPos pos2 = platform2.getMidPos();

		while (tempPath.size() >= 2) {
			final BlockPos lastPos = tempPath.get(tempPath.size() - 1);
			final BlockPos secondLastPos = tempPath.get(tempPath.size() - 2);

			if (platform2.containsPos(lastPos)) {
				tempPath.add(platform2.getOtherPosition(lastPos));
				return tempPath;
			} else {
				Optional<BlockPosWeighted> blockPosWeighted = getConnectedPositions(lastPos, secondLastPos).stream().filter(blockPos -> !blacklist.contains(blockPos)).map(blockPos -> new BlockPosWeighted(blockPos, blockPos.getSquaredDistance(pos2))).min(BlockPosWeighted::compareTo);
				if (blockPosWeighted.isPresent()) {
					blacklist.add(blockPosWeighted.get().pos);
					tempPath.add(blockPosWeighted.get().pos);
				} else {
					tempPath.remove(tempPath.size() - 1);
				}
			}
		}

		return new ArrayList<>();
	}

	private Set<BlockPos> getConnectedPositions(BlockPos thisPos, BlockPos lastPos) {
		final BlockEntity entity = world.getBlockEntity(thisPos);
		if (entity instanceof BlockRail.TileEntityRail) {
			return ((BlockRail.TileEntityRail) entity).getConnectedPositions(lastPos);
		} else {
			return new HashSet<>();
		}
	}

	protected static class BlockPosWeighted implements Comparable<BlockPosWeighted> {

		protected final BlockPos pos;
		protected final double weight;

		protected BlockPosWeighted(BlockPos pos, double weight) {
			this.pos = pos;
			this.weight = weight;
		}

		@Override
		public int compareTo(BlockPosWeighted o) {
			return Double.compare(weight, o.weight);
		}
	}
}
