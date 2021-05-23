package mtr.path;

import mtr.data.Platform;
import mtr.data.Rail;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PathFinder {

	public static List<Rail> findPath(Map<BlockPos, Map<BlockPos, Rail>> rails, List<Platform> platforms) {
		if (platforms.size() < 2) {
			return new ArrayList<>();
		}

		final List<Rail> path = new ArrayList<>();
		for (int i = 0; i < platforms.size() - 1; i++) {
			final Platform platformStart = platforms.get(i);
			final Platform platformEnd = platforms.get(i + 1);

			final List<Rail> partialPath = findPath(rails, platformStart, platformEnd);
			if (partialPath.isEmpty()) {
				return path;
			} else {
				path.addAll(partialPath);
			}
		}

		return path;
	}

	private static List<Rail> findPath(Map<BlockPos, Map<BlockPos, Rail>> rails, Platform platformStart, Platform platformEnd) {
		final BlockPos platformEndMidPos = platformEnd.getMidPos();
		final Function<Map<BlockPos, Rail>, Comparator<BlockPos>> comparator = newConnections -> (pos1, pos2) -> {
			if (newConnections.get(pos1).railType.speedLimit == newConnections.get(pos2).railType.speedLimit) {
				return pos1.getSquaredDistance(platformEndMidPos) > pos2.getSquaredDistance(platformEndMidPos) ? 1 : -1;
			} else {
				return newConnections.get(pos2).railType.speedLimit - newConnections.get(pos1).railType.speedLimit;
			}
		};

		for (int i = 0; i < 2; i++) {
			final List<PathPart> path = new ArrayList<>();
			final List<BlockPos> startPositions = platformStart.getOrderedPositions(platformEndMidPos, i == 0);
			path.add(new PathPart(Direction.UP, startPositions.get(0), new ArrayList<>()));
			addPathPart(rails, startPositions.get(1), startPositions.get(0), path, comparator);

			while (path.size() >= 2) {
				final PathPart lastPathPart = path.get(path.size() - 1);

				if (lastPathPart.otherOptions.isEmpty()) {
					path.remove(lastPathPart);
				} else {
					final BlockPos newPos = lastPathPart.otherOptions.remove(0);
					addPathPart(rails, newPos, lastPathPart.pos, path, comparator);

					if (platformEnd.containsPos(newPos)) {
						final List<Rail> railPath = new ArrayList<>();
						for (int j = 0; j < path.size() - 1; j++) {
							try {
								railPath.add(rails.get(path.get(j).pos).get(path.get(j + 1).pos));
							} catch (Exception ignored) {
								return railPath;
							}
						}
						return railPath;
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
