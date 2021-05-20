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
import java.util.stream.Collectors;

public class PathFinder {

	public static List<BlockPos> findPath(Map<BlockPos, Map<BlockPos, Rail>> rails, List<Platform> platforms) {
		if (platforms.size() < 2) {
			return new ArrayList<>();
		}

		final List<BlockPos> path = new ArrayList<>();
		for (int i = 0; i < platforms.size() - 1; i++) {
			final Platform platformStart = platforms.get(i);
			final Platform platformEnd = platforms.get(i + 1);

			final List<BlockPos> partialPath1 = findPath(rails, platformStart, platformEnd, true);
			if (partialPath1.isEmpty()) {
				final List<BlockPos> partialPath2 = findPath(rails, platformStart, platformEnd, false);
				if (partialPath2.isEmpty()) {
					return path;
				} else {
					path.addAll(partialPath2);
				}
			} else {
				path.addAll(partialPath1);
			}
		}

		return path;
	}

	private static List<BlockPos> findPath(Map<BlockPos, Map<BlockPos, Rail>> rails, Platform platformStart, Platform platformEnd, boolean reverse) {
		final BlockPos platformEndMidPos = platformEnd.getMidPos();
		final Function<Map<BlockPos, Rail>, Comparator<BlockPos>> comparator = newConnections -> (pos1, pos2) -> {
			if (newConnections.get(pos1).railType.speedLimit == newConnections.get(pos2).railType.speedLimit) {
				return pos1.getSquaredDistance(platformEndMidPos) > pos2.getSquaredDistance(platformEndMidPos) ? 1 : -1;
			} else {
				return newConnections.get(pos2).railType.speedLimit - newConnections.get(pos1).railType.speedLimit;
			}
		};

		final List<PathPart> path = new ArrayList<>();
		platformStart.getOrderedPositions(platformEndMidPos, reverse).forEach(startPos -> {
			final List<BlockPos> startOptions = new ArrayList<>(rails.get(startPos).keySet());
			startOptions.sort(comparator.apply(rails.get(startPos)));
			path.add(new PathPart(Direction.UP, startPos, startOptions));
		});

		while (!path.isEmpty()) {
			final PathPart lastPathPart = path.get(path.size() - 1);

			if (lastPathPart.otherOptions.isEmpty()) {
				path.remove(lastPathPart);
			} else {
				final BlockPos newPos = lastPathPart.otherOptions.remove(0);
				final Map<BlockPos, Rail> newConnections = rails.get(newPos);
				final Direction newDirection = rails.get(lastPathPart.pos).get(newPos).facingEnd.getOpposite();
				final List<BlockPos> otherOptions = new ArrayList<>();

				newConnections.forEach((connectedPos, rail) -> {
					if (rail.facingStart != newDirection.getOpposite() && path.stream().noneMatch(pathPart -> pathPart.isSame(newPos, newDirection))) {
						otherOptions.add(connectedPos);
					}//-263 81 -460, -264 81 -330
				});

				if (!otherOptions.isEmpty()) {
					otherOptions.sort(comparator.apply(newConnections));
					path.add(new PathPart(newDirection, newPos, otherOptions));
				}

				if (platformEnd.containsPos(newPos)) {
					return path.stream().map(pathPart -> pathPart.pos).collect(Collectors.toList());
				}
			}
		}

		return new ArrayList<>();
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
