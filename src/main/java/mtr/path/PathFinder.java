package mtr.path;

import mtr.block.BlockRail;
import mtr.data.Platform;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.*;

public class PathFinder {

	private int platformIndex;
	private float tOffset;
	private Platform platform1Temp;
	private Platform platform2Temp;
	private float currentSpeed;
	private List<BlockPos> tempPath;
	private int tempPathIndex;
	private FindStage findStage;

	private final WorldAccess world;
	private final List<Platform> platforms;
	private final List<PathData> path;
	private final Set<BlockPos> blacklist;

	public PathFinder(WorldAccess world, List<Platform> platforms) {
		platformIndex = 1;
		tOffset = 0;
		platform1Temp = null;
		platform2Temp = null;
		currentSpeed = 1;
		tempPath = new ArrayList<>();
		tempPathIndex = 1;
		findStage = FindStage.INIT_FORWARD;

		this.world = world;
		this.platforms = platforms;
		path = new ArrayList<>();
		blacklist = new HashSet<>();
	}

	public List<PathData> findPath() {
		if (platforms.size() < 2) {
			return new ArrayList<>();
		}

		switch (findStage) {
			case INIT_FORWARD:
				platform1Temp = platforms.get(platformIndex - 1);
				platform2Temp = platforms.get(platformIndex);
				tempPath = platform1Temp.getOrderedPositions(platform2Temp.getMidPos(), true);
				findStage = FindStage.TEMP_PATH_FORWARD;
				break;
			case INIT_BACKWARD:
				tempPath = platform1Temp.getOrderedPositions(platform2Temp.getMidPos(), false);
				findStage = FindStage.TEMP_PATH_BACKWARD;
				break;
			case TEMP_PATH_FORWARD:
			case TEMP_PATH_BACKWARD:
				final BlockPos lastPos = tempPath.get(tempPath.size() - 1);
				final BlockPos secondLastPos = tempPath.get(tempPath.size() - 2);
				tempPathIndex = 1;

				if (platform2Temp.containsPos(lastPos)) {
					tempPath.add(platform2Temp.getOtherPosition(lastPos));
					if (!path.isEmpty()) {
						tempPath.remove(0);
					}
					findStage = FindStage.CONVERTING;
				} else {
					final BlockPos pos2 = platform2Temp.getMidPos();
					Optional<BlockPosWeighted> blockPosWeighted = getConnectedPositions(lastPos, secondLastPos).stream().filter(blockPos -> !blacklist.contains(blockPos)).map(blockPos -> new BlockPosWeighted(blockPos, blockPos.getSquaredDistance(pos2))).min(BlockPosWeighted::compareTo);
					if (blockPosWeighted.isPresent()) {
						blacklist.add(blockPosWeighted.get().pos);
						tempPath.add(blockPosWeighted.get().pos);
					} else {
						tempPath.remove(tempPath.size() - 1);
					}
				}

				if (tempPath.size() < 2) {
					tempPath.clear();
					if (findStage == FindStage.TEMP_PATH_FORWARD) {
						findStage = FindStage.INIT_BACKWARD;
					} else {
						return path;
					}
				}
				break;
			case CONVERTING:
				if (path.isEmpty()) {
					generatePathData(tempPath.subList(0, 2), platform1Temp.getDwellTime());
					tempPath.remove(0);
				}

				generatePathData(tempPath, platform2Temp.getDwellTime());
				tempPathIndex++;

				if (tempPathIndex >= tempPath.size()) {
					platformIndex++;
					findStage = FindStage.INIT_FORWARD;
					if (platformIndex >= platforms.size()) {
						return path;
					}
				}
				break;
		}

		return null;
	}

	private void generatePathData(List<BlockPos> tempPathSubList, int dwellTime) {
		final BlockPos tempPos = tempPathSubList.get(tempPathIndex - 1);
		final BlockEntity entity = world.getBlockEntity(tempPos);
		if (entity instanceof BlockRail.TileEntityRail) {
			final PathData pathData = new PathData(((BlockRail.TileEntityRail) entity).railMap.get(tempPathSubList.get(tempPathIndex)), currentSpeed, tempPathIndex == tempPathSubList.size() - 1 ? dwellTime : 0, tOffset);
			path.add(pathData);
			tOffset += pathData.getTime();
			currentSpeed = pathData.finalSpeed;
		}
	}

	private Set<BlockPos> getConnectedPositions(BlockPos thisPos, BlockPos lastPos) {
		final BlockEntity entity = world.getBlockEntity(thisPos);
		if (entity instanceof BlockRail.TileEntityRail) {
			return ((BlockRail.TileEntityRail) entity).getConnectedPositions(lastPos);
		} else {
			return new HashSet<>();
		}
	}

	private static class BlockPosWeighted implements Comparable<BlockPosWeighted> {

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

	private enum FindStage {INIT_FORWARD, INIT_BACKWARD, TEMP_PATH_FORWARD, TEMP_PATH_BACKWARD, CONVERTING}
}
