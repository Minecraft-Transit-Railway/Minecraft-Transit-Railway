package mtr.path;

import mtr.data.Platform;
import mtr.data.Rail;
import mtr.data.RailwayData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

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

	private final Set<Rail.RailEntry> rails;
	private final List<Platform> platforms;
	private final List<PathData> path;
	private final Set<BlockPos> blacklist;

	public PathFinder(Set<Rail.RailEntry> rails, List<Platform> platforms) {
		platformIndex = 1;
		tOffset = 0;
		platform1Temp = null;
		platform2Temp = null;
		currentSpeed = 1;
		tempPath = new ArrayList<>();
		tempPathIndex = 1;
		findStage = FindStage.INIT_FORWARD;

		this.rails = rails;
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
					final BlockPos posPlatform = platform2Temp.getMidPos();
					final Map<BlockPos, Rail.RailType> connectedPositions = getConnectedPositions(lastPos, secondLastPos);
					final Optional<BlockPos> blockPosClosest = connectedPositions.keySet().stream().filter(blockPos -> !blacklist.contains(blockPos)).min((pos1, pos2) -> {
						if (connectedPositions.get(pos1).speedLimit == connectedPositions.get(pos2).speedLimit) {
							return pos1.getSquaredDistance(posPlatform) > pos2.getSquaredDistance(posPlatform) ? 1 : -1;
						} else {
							return connectedPositions.get(pos2).speedLimit - connectedPositions.get(pos1).speedLimit;
						}
					});
					if (blockPosClosest.isPresent()) {
						blacklist.add(blockPosClosest.get());
						tempPath.add(blockPosClosest.get());
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
		final Rail.RailEntry railEntry = RailwayData.getRailEntry(rails, tempPos);
		if (railEntry != null) {
			final PathData pathData = new PathData(railEntry.connections.get(tempPathSubList.get(tempPathIndex)), currentSpeed, tempPathIndex == tempPathSubList.size() - 1 ? dwellTime : 0, tOffset);
			path.add(pathData);
			tOffset += pathData.getTime();
			currentSpeed = pathData.finalSpeed;
		}
	}

	private Map<BlockPos, Rail.RailType> getConnectedPositions(BlockPos thisPos, BlockPos lastPos) {
		final Rail.RailEntry railEntry = RailwayData.getRailEntry(rails, thisPos);
		if (railEntry == null || !railEntry.connections.containsKey(lastPos)) {
			return new HashMap<>();
		} else {
			final Direction findDirection = railEntry.connections.get(lastPos).facing.getOpposite();
			final Map<BlockPos, Rail.RailType> connectedPositions = new HashMap<>();
			railEntry.connections.forEach((blockPos, rail) -> {
				if (rail.facing == findDirection) {
					connectedPositions.put(blockPos, rail.railType);
				}
			});
			return connectedPositions;
		}
	}

	private enum FindStage {INIT_FORWARD, INIT_BACKWARD, TEMP_PATH_FORWARD, TEMP_PATH_BACKWARD, CONVERTING}
}
