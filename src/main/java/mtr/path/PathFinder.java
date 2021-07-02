package mtr.path;

import mtr.data.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.*;
import java.util.function.Function;

public class PathFinder {

	private SavedRailBase savedRailBaseStart;
	private SavedRailBase savedRailBaseEnd;
	private int index;
	private PathFinderState state;

	private final List<PathData> finalPath;
	private final Map<BlockPos, Map<BlockPos, Rail>> rails;
	private final List<SavedRailBase> savedRailBases;

	private final List<PathPart> path = new ArrayList<>();
	private final Set<BlockPos> turnBacks = new HashSet<>();

	public PathFinder(List<PathData> finalPath, Map<BlockPos, Map<BlockPos, Rail>> rails, List<SavedRailBase> savedRailBases) {
		finalPath.clear();
		this.finalPath = finalPath;
		this.rails = rails;
		this.savedRailBases = savedRailBases;
		state = PathFinderState.SETUP;
		index = 0;
	}

	public int findPath() {
		if (savedRailBases.size() < 2) {
			return 0;
		} else if (index >= savedRailBases.size() - 1) {
			return savedRailBases.size() == 2 ? 0 : savedRailBases.size();
		}

		switch (state) {
			case SETUP:
				savedRailBaseStart = savedRailBases.get(index);
				savedRailBaseEnd = savedRailBases.get(index + 1);
				state = PathFinderState.INITIALIZE_1;
				break;
			case INITIALIZE_1:
				initializePathSection(true);
				state = PathFinderState.FIND_PATH_1;
				break;
			case INITIALIZE_2:
				initializePathSection(false);
				state = PathFinderState.FIND_PATH_2;
				break;
			case FIND_PATH_1:
			case FIND_PATH_2:
				final List<PathData> partialPath = findPartialPath();
				if (partialPath != null) {
					if (partialPath.isEmpty()) {
						if (state == PathFinderState.FIND_PATH_1) {
							state = PathFinderState.INITIALIZE_2;
						} else {
							finalPath.clear();
							return savedRailBases.size() == 2 ? 0 : index + 1;
						}
					} else {
						final boolean sameFirstRail = !finalPath.isEmpty() && finalPath.get(finalPath.size() - 1).isSameRail(partialPath.get(0));

						for (int j = 0; j < partialPath.size(); j++) {
							if (!(j == 0 && sameFirstRail)) {
								finalPath.add(partialPath.get(j));
							}
						}

						index++;
						state = PathFinderState.SETUP;
					}
				}
				break;
		}

		return -1;
	}

	private Comparator<BlockPos> compareRails(Map<BlockPos, Rail> newConnections) {
		return (pos1, pos2) -> {
			if (newConnections.get(pos1).railType.speedLimit == newConnections.get(pos2).railType.speedLimit) {
				final BlockPos savedRailBaseEndMidPos = savedRailBaseEnd.getMidPos();
				return pos1.getSquaredDistance(savedRailBaseEndMidPos) > pos2.getSquaredDistance(savedRailBaseEndMidPos) ? 1 : -1;
			} else {
				return newConnections.get(pos2).railType.speedLimit - newConnections.get(pos1).railType.speedLimit;
			}
		};
	}

	private void initializePathSection(boolean reverse) {
		path.clear();
		turnBacks.clear();
		final List<BlockPos> startPositions = savedRailBaseStart.getOrderedPositions(savedRailBaseEnd.getMidPos(), reverse);
		path.add(new PathPart(Direction.UP, startPositions.get(0), new ArrayList<>()));
		addPathPart(rails, startPositions.get(1), startPositions.get(0), path, turnBacks, this::compareRails);
	}

	private List<PathData> findPartialPath() {
		final PathPart lastPathPart = path.get(path.size() - 1);

		if (lastPathPart.otherOptions.isEmpty()) {
			path.remove(lastPathPart);
		} else {
			final BlockPos newPos = lastPathPart.otherOptions.remove(0);
			addPathPart(rails, newPos, lastPathPart.pos, path, turnBacks, this::compareRails);

			if (savedRailBaseEnd.containsPos(newPos)) {
				final List<PathData> railPath = new ArrayList<>();
				for (int j = 0; j < path.size() - 1; j++) {
					final BlockPos pos1 = path.get(j).pos;
					final BlockPos pos2 = path.get(j + 1).pos;
					if (RailwayData.containsRail(rails, pos1, pos2)) {
						final Rail rail = rails.get(pos1).get(pos2);
						final boolean turningBack = rail.railType == RailType.TURN_BACK && j < path.size() - 2 && path.get(j + 2).pos.equals(pos1);
						railPath.add(new PathData(rail, j == 0 ? savedRailBaseStart.id : 0, turningBack ? 1 : 0, pos1, pos2, index));
					} else {
						return new ArrayList<>();
					}
				}

				final BlockPos endPos = savedRailBaseEnd.getOtherPosition(newPos);
				if (RailwayData.containsRail(rails, newPos, endPos)) {
					railPath.add(new PathData(rails.get(newPos).get(endPos), savedRailBaseEnd.id, savedRailBaseEnd instanceof Platform ? ((Platform) savedRailBaseEnd).getDwellTime() : 0, newPos, endPos, index + 1));
					return railPath;
				} else {
					return new ArrayList<>();
				}
			}
		}

		return path.size() >= 2 ? null : new ArrayList<>();
	}

	private static void addPathPart(Map<BlockPos, Map<BlockPos, Rail>> rails, BlockPos newPos, BlockPos lastPos, List<PathPart> path, Set<BlockPos> turnBacks, Function<Map<BlockPos, Rail>, Comparator<BlockPos>> comparator) {
		final Map<BlockPos, Rail> newConnections = rails.get(newPos);
		final Rail oldRail = rails.get(lastPos).get(newPos);

		if (oldRail == null) {
			return;
		}

		final Direction newDirection = oldRail.facingEnd.getOpposite();
		final List<BlockPos> otherOptions = new ArrayList<>();

		if (newConnections != null) {
			final boolean canTurnBack = oldRail.railType == RailType.TURN_BACK && !turnBacks.contains(newPos);
			newConnections.forEach((connectedPos, rail) -> {
				if (canTurnBack || rail.railType != RailType.NONE && rail.facingStart != newDirection.getOpposite() && path.stream().noneMatch(pathPart -> pathPart.isSame(newPos, newDirection))) {
					otherOptions.add(connectedPos);
					if (canTurnBack) {
						turnBacks.add(newPos);
					}
				}
			});
		}

		if (!otherOptions.isEmpty()) {
			otherOptions.sort(comparator.apply(newConnections));
			path.add(new PathPart(newDirection, newPos, otherOptions));
		}
	}

	private enum PathFinderState {SETUP, INITIALIZE_1, INITIALIZE_2, FIND_PATH_1, FIND_PATH_2}

	private static class PathPart {

		private final Direction direction;
		private final BlockPos pos;
		private final List<BlockPos> otherOptions;

		private PathPart(Direction direction, BlockPos pos, List<BlockPos> otherOptions) {
			this.direction = direction;
			this.pos = pos;
			this.otherOptions = otherOptions;
		}

		private boolean isSame(BlockPos newPos, Direction newDirection) {
			return newPos.equals(pos) && newDirection == direction;
		}
	}
}
