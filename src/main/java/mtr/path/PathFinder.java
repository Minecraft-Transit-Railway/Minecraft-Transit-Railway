package mtr.path;

import mtr.data.Platform;
import mtr.data.Rail;
import mtr.data.RailwayData;
import mtr.data.SavedRailBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PathFinder {

	public static List<PathData> findPath(Map<BlockPos, Map<BlockPos, Rail>> rails, List<SavedRailBase> savedRailBases) {
		if (savedRailBases.size() < 2) {
			return new ArrayList<>();
		}

		final List<PathData> path = new ArrayList<>();
		for (int i = 0; i < savedRailBases.size() - 1; i++) {
			final SavedRailBase savedRailBaseStart = savedRailBases.get(i);
			final SavedRailBase savedRailBaseEnd = savedRailBases.get(i + 1);

			final List<PathData> partialPath = findPath(rails, savedRailBaseStart, savedRailBaseEnd);
			if (partialPath.isEmpty()) {
				return new ArrayList<>();
			} else {
				final boolean sameFirstRail = !path.isEmpty() && path.get(path.size() - 1).isSameRail(partialPath.get(0));
				for (int j = 0; j < partialPath.size(); j++) {
					if (!(j == 0 && sameFirstRail)) {
						path.add(partialPath.get(j));
					}
				}
			}
		}

		return path;
	}

	private static List<PathData> findPath(Map<BlockPos, Map<BlockPos, Rail>> rails, SavedRailBase savedRailBaseStart, SavedRailBase savedRailBaseEnd) {
		final BlockPos savedRailBaseEndMidPos = savedRailBaseEnd.getMidPos();
		final Function<Map<BlockPos, Rail>, Comparator<BlockPos>> comparator = newConnections -> (pos1, pos2) -> {
			if (newConnections.get(pos1).railType.speedLimit == newConnections.get(pos2).railType.speedLimit) {
				return pos1.getSquaredDistance(savedRailBaseEndMidPos) > pos2.getSquaredDistance(savedRailBaseEndMidPos) ? 1 : -1;
			} else {
				return newConnections.get(pos2).railType.speedLimit - newConnections.get(pos1).railType.speedLimit;
			}
		};

		for (int i = 0; i < 2; i++) {
			final List<PathPart> path = new ArrayList<>();
			final List<BlockPos> startPositions = savedRailBaseStart.getOrderedPositions(savedRailBaseEndMidPos, i == 0);
			path.add(new PathPart(Direction.UP, startPositions.get(0), new ArrayList<>()));
			addPathPart(rails, startPositions.get(1), startPositions.get(0), path, comparator);

			while (path.size() >= 2) {
				final PathPart lastPathPart = path.get(path.size() - 1);

				if (lastPathPart.otherOptions.isEmpty()) {
					path.remove(lastPathPart);
				} else {
					final BlockPos newPos = lastPathPart.otherOptions.remove(0);
					addPathPart(rails, newPos, lastPathPart.pos, path, comparator);

					if (savedRailBaseEnd.containsPos(newPos)) {
						final List<PathData> railPath = new ArrayList<>();
						for (int j = 0; j < path.size() - 1; j++) {
							final BlockPos pos1 = path.get(j).pos;
							final BlockPos pos2 = path.get(j + 1).pos;
							if (RailwayData.containsRail(rails, pos1, pos2)) {
								railPath.add(new PathData(rails.get(pos1).get(pos2), 0, pos1, pos2));
							} else {
								return new ArrayList<>();
							}
						}

						final BlockPos endPos = savedRailBaseEnd.getOtherPosition(newPos);
						if (RailwayData.containsRail(rails, newPos, endPos)) {
							railPath.add(new PathData(rails.get(newPos).get(endPos), savedRailBaseEnd instanceof Platform ? ((Platform) savedRailBaseEnd).getDwellTime() : 0, newPos, endPos));
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

	private static void addPathPart(Map<BlockPos, Map<BlockPos, Rail>> rails, BlockPos newPos, BlockPos lastPos, List<PathPart> path, Function<Map<BlockPos, Rail>, Comparator<BlockPos>> comparator) {
		final Map<BlockPos, Rail> newConnections = rails.get(newPos);
		final Direction newDirection = rails.get(lastPos).get(newPos).facingEnd.getOpposite();
		final List<BlockPos> otherOptions = new ArrayList<>();

		newConnections.forEach((connectedPos, rail) -> {
			if (rail.facingStart != newDirection.getOpposite() && path.stream().noneMatch(pathPart -> pathPart.isSame(newPos, newDirection))) {
				otherOptions.add(connectedPos);
			}
		});

		if (!otherOptions.isEmpty()) {
			otherOptions.sort(comparator.apply(newConnections));
			path.add(new PathPart(newDirection, newPos, otherOptions));
		}
	}

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
