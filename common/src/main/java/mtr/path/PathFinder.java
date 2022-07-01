package mtr.path;

import mtr.data.*;
import net.minecraft.core.BlockPos;

import java.util.*;
import java.util.function.Function;

public class PathFinder {

	public static int findPath(List<PathData> path, Map<BlockPos, Map<BlockPos, Rail>> rails, List<SavedRailBase> savedRailBases, int stopIndexOffset) {
		path.clear();
		if (savedRailBases.size() < 2) {
			return 0;
		}

		for (int i = 0; i < savedRailBases.size() - 1; i++) {
			final SavedRailBase savedRailBaseStart = savedRailBases.get(i);
			final SavedRailBase savedRailBaseEnd = savedRailBases.get(i + 1);

			final List<PathData> partialPath = findPath(rails, savedRailBaseStart, savedRailBaseEnd, i + stopIndexOffset);
			if (partialPath.isEmpty()) {
				path.clear();
				return i + 1;
			}

			appendPath(path, partialPath);
		}

		return savedRailBases.size();
	}

	public static void appendPath(List<PathData> path, List<PathData> partialPath) {
		if (partialPath.isEmpty()) {
			path.clear();
		} else {
			final boolean sameFirstRail = !path.isEmpty() && path.get(path.size() - 1).isSameRail(partialPath.get(0));
			for (int j = 0; j < partialPath.size(); j++) {
				if (!(j == 0 && sameFirstRail)) {
					path.add(partialPath.get(j));
				}
			}
		}
	}

	private static List<PathData> findPath(Map<BlockPos, Map<BlockPos, Rail>> rails, SavedRailBase savedRailBaseStart, SavedRailBase savedRailBaseEnd, int stopIndex) {
		final BlockPos savedRailBaseEndMidPos = savedRailBaseEnd.getMidPos();
		final Function<Map<BlockPos, Rail>, Comparator<BlockPos>> comparator = newConnections -> (pos1, pos2) -> {
			if (newConnections.get(pos1).railType.speedLimit == newConnections.get(pos2).railType.speedLimit) {
				return pos1.distSqr(savedRailBaseEndMidPos) > pos2.distSqr(savedRailBaseEndMidPos) ? 1 : -1;
			} else {
				return newConnections.get(pos2).railType.speedLimit - newConnections.get(pos1).railType.speedLimit;
			}
		};

		for (int i = 0; i < 2; i++) {
			final List<PathPart> path = new ArrayList<>();
			final Set<BlockPos> turnBacks = new HashSet<>();
			final List<BlockPos> startPositions = savedRailBaseStart.getOrderedPositions(savedRailBaseEndMidPos, i == 0);
			path.add(new PathPart(null, startPositions.get(0), new ArrayList<>()));
			addPathPart(rails, startPositions.get(1), startPositions.get(0), path, turnBacks, comparator);

			while (path.size() >= 2) {
				final PathPart lastPathPart = path.get(path.size() - 1);

				if (lastPathPart.otherOptions.isEmpty()) {
					path.remove(lastPathPart);
				} else {
					final BlockPos newPos = lastPathPart.otherOptions.remove(0);
					addPathPart(rails, newPos, lastPathPart.pos, path, turnBacks, comparator);

					if (savedRailBaseEnd.containsPos(newPos)) {
						final List<PathData> railPath = new ArrayList<>();
						for (int j = 0; j < path.size() - 1; j++) {
							final BlockPos pos1 = path.get(j).pos;
							final BlockPos pos2 = path.get(j + 1).pos;
							if (RailwayData.containsRail(rails, pos1, pos2)) {
								final Rail rail = rails.get(pos1).get(pos2);
								final boolean turningBack = rail.railType == RailType.TURN_BACK && j < path.size() - 2 && path.get(j + 2).pos.equals(pos1);
								railPath.add(new PathData(rail, j == 0 ? savedRailBaseStart.id : 0, turningBack ? 1 : 0, pos1, pos2, stopIndex));
							} else {
								return new ArrayList<>();
							}
						}

						final BlockPos endPos = savedRailBaseEnd.getOtherPosition(newPos);
						if (RailwayData.containsRail(rails, newPos, endPos)) {
							railPath.add(new PathData(rails.get(newPos).get(endPos), savedRailBaseEnd.id, savedRailBaseEnd instanceof Platform ? savedRailBaseEnd.getDwellTime() : 0, newPos, endPos, stopIndex + 1));
							return railPath;
						} else {
							return new ArrayList<>();
						}
					}
				}
			}
		}

		return new ArrayList<>();
	}

	private static void addPathPart(Map<BlockPos, Map<BlockPos, Rail>> rails, BlockPos newPos, BlockPos lastPos, List<PathPart> path, Set<BlockPos> turnBacks, Function<Map<BlockPos, Rail>, Comparator<BlockPos>> comparator) {
		final Map<BlockPos, Rail> newConnections = rails.get(newPos);
		final Rail oldRail = rails.get(lastPos).get(newPos);

		if (oldRail == null) {
			return;
		}

		final RailAngle newDirection = oldRail.facingEnd.getOpposite();
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

	private static class PathPart {

		private final RailAngle direction;
		private final BlockPos pos;
		private final List<BlockPos> otherOptions;

		private PathPart(RailAngle direction, BlockPos pos, List<BlockPos> otherOptions) {
			this.direction = direction;
			this.pos = pos;
			this.otherOptions = otherOptions;
		}

		private boolean isSame(BlockPos newPos, RailAngle newDirection) {
			return newPos.equals(pos) && newDirection == direction;
		}
	}
}
