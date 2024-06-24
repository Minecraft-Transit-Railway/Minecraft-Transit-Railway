package mtr.path;

import mtr.data.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Function;

public class PathFinder {

	private static final int MAX_AIRPLANE_TURN_ARC = 128;

	public static int findPath(List<PathData> path, Map<BlockPos, Map<BlockPos, Rail>> rails, List<SavedRailBase> savedRailBases, int stopIndexOffset, int cruisingAltitude, boolean useFastSpeed) {
		path.clear();
		if (savedRailBases.size() < 2) {
			return 0;
		}

		for (int i = 0; i < savedRailBases.size() - 1; i++) {
			final SavedRailBase savedRailBaseStart = savedRailBases.get(i);
			final SavedRailBase savedRailBaseEnd = savedRailBases.get(i + 1);

			final Set<BlockPos> runways = new HashSet<>();
			if (savedRailBaseStart.transportMode == TransportMode.AIRPLANE) {
				rails.forEach((startPos, railMap) -> {
					if (railMap.size() == 1 && railMap.values().stream().allMatch(rail -> rail.railType == RailType.RUNWAY)) {
						runways.add(startPos);
					}
				});
			}

			final List<PathData> partialPath = findPath(rails, runways, savedRailBaseStart, savedRailBaseEnd, i + stopIndexOffset, cruisingAltitude, useFastSpeed);
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

	private static List<PathData> findPath(Map<BlockPos, Map<BlockPos, Rail>> rails, Set<BlockPos> runways, SavedRailBase savedRailBaseStart, SavedRailBase savedRailBaseEnd, int stopIndex, int cruisingAltitude, boolean useFastSpeed) {
		final BlockPos savedRailBaseEndMidPos = savedRailBaseEnd.getMidPos();
		final Function<Map<BlockPos, Rail>, Comparator<BlockPos>> comparator = newConnections -> (pos1, pos2) -> {
			if (pos1 == pos2) {
				return 0;
			} else {
				final Rail connection1 = newConnections.get(pos1);
				final Rail connection2 = newConnections.get(pos2);
				if (connection1 == null || connection2 == null || connection1.railType.speedLimit == connection2.railType.speedLimit) {
					return pos1.distSqr(savedRailBaseEndMidPos) > pos2.distSqr(savedRailBaseEndMidPos) ? 1 : -1;
				} else {
					return connection2.railType.speedLimit - connection1.railType.speedLimit;
				}
			}
		};

		for (int i = 0; i < 2; i++) {
			final List<PathPart> path = new ArrayList<>();
			final Set<BlockPos> turnBacks = new HashSet<>();
			final List<BlockPos> startPositions = savedRailBaseStart.getOrderedPositions(savedRailBaseEndMidPos, i == 0);
			path.add(new PathPart(null, startPositions.get(0), new ArrayList<>()));
			addPathPart(rails, runways, startPositions.get(1), startPositions.get(0), path, turnBacks, comparator);

			while (path.size() >= 2) {
				final PathPart lastPathPart = path.get(path.size() - 1);

				if (lastPathPart.otherOptions.isEmpty()) {
					path.remove(lastPathPart);
				} else {
					final BlockPos newPos = lastPathPart.otherOptions.remove(0);
					addPathPart(rails, runways, newPos, lastPathPart.pos, path, turnBacks, comparator);

					if (savedRailBaseEnd.containsPos(newPos)) {
						final List<PathData> railPath = new ArrayList<>();
						for (int j = 0; j < path.size() - 1; j++) {
							final PathPart pathPart1 = path.get(j);
							final PathPart pathPart2 = path.get(j + 1);
							final BlockPos pos1 = pathPart1.pos;
							final BlockPos pos2 = pathPart2.pos;
							final Rail rail = DataCache.tryGet(rails, pos1, pos2);

							if (rail == null) {
								if (runways.isEmpty()) {
									return new ArrayList<>();
								} else {
									final int heightDifference1 = cruisingAltitude - pos1.getY();
									final int heightDifference2 = cruisingAltitude - pos2.getY();
									final BlockPos cruisingPos1 = RailwayData.offsetBlockPos(pos1, pathPart1.direction.cos * Math.abs(heightDifference1) * 4, heightDifference1, pathPart1.direction.sin * Math.abs(heightDifference1) * 4);
									final BlockPos cruisingPos4 = RailwayData.offsetBlockPos(pos2, -pathPart2.direction.cos * Math.abs(heightDifference2) * 4, heightDifference2, -pathPart2.direction.sin * Math.abs(heightDifference2) * 4);
									final int turnArc = Math.min(MAX_AIRPLANE_TURN_ARC, cruisingPos1.distManhattan(cruisingPos4) / 8);
									final RailType dummyRailType = useFastSpeed ? RailType.AIRPLANE_DUMMY : RailType.RUNWAY;

									railPath.add(new PathData(new Rail(pos1, pathPart1.direction, cruisingPos1, pathPart1.direction.getOpposite(), dummyRailType, TransportMode.AIRPLANE), 0, 0, pos1, cruisingPos1, stopIndex));

									final RailAngle expectedAngle = RailAngle.fromAngle((float) Math.toDegrees(Math.atan2(cruisingPos4.getZ() - cruisingPos1.getZ(), cruisingPos4.getX() - cruisingPos1.getX())));
									final BlockPos cruisingPos2 = addAirplanePath(pathPart1.direction, cruisingPos1, expectedAngle, turnArc, railPath, dummyRailType, stopIndex, false);
									final List<PathData> tempRailData = new ArrayList<>();
									final BlockPos cruisingPos3 = addAirplanePath(pathPart2.direction.getOpposite(), cruisingPos4, expectedAngle.getOpposite(), turnArc, tempRailData, dummyRailType, stopIndex, true);

									railPath.add(new PathData(new Rail(cruisingPos2, expectedAngle, cruisingPos3, expectedAngle.getOpposite(), dummyRailType, TransportMode.AIRPLANE), 0, 0, cruisingPos2, cruisingPos3, stopIndex));
									railPath.addAll(tempRailData);

									railPath.add(new PathData(new Rail(cruisingPos4, pathPart2.direction, pos2, pathPart2.direction.getOpposite(), dummyRailType, TransportMode.AIRPLANE), 0, 0, cruisingPos4, pos2, stopIndex));
								}
							} else {
								final boolean turningBack = rail.railType == RailType.TURN_BACK && j < path.size() - 2 && path.get(j + 2).pos.equals(pos1);
								railPath.add(new PathData(rail, j == 0 ? savedRailBaseStart.id : 0, turningBack ? 1 : 0, pos1, pos2, stopIndex));
							}
						}

						final BlockPos endPos = savedRailBaseEnd.getOtherPosition(newPos);
						final Rail rail = DataCache.tryGet(rails, newPos, endPos);
						if (rail == null) {
							return new ArrayList<>();
						} else {
							railPath.add(new PathData(rail, savedRailBaseEnd.id, savedRailBaseEnd instanceof Platform ? savedRailBaseEnd.getDwellTime() : 0, newPos, endPos, stopIndex + 1));
							return railPath;
						}
					}
				}
			}
		}

		return new ArrayList<>();
	}

	private static void addPathPart(Map<BlockPos, Map<BlockPos, Rail>> rails, Set<BlockPos> runways, BlockPos newPos, BlockPos lastPos, List<PathPart> path, Set<BlockPos> turnBacks, Function<Map<BlockPos, Rail>, Comparator<BlockPos>> comparator) {
		final Map<BlockPos, Rail> newConnections = rails.get(newPos);
		final Rail oldRail = rails.get(lastPos).get(newPos);

		if (oldRail == null && runways.isEmpty()) {
			return;
		}

		final RailAngle newDirection = oldRail == null ? newConnections.values().stream().map(rail -> rail.facingStart).findFirst().orElse(RailAngle.E) : oldRail.facingEnd.getOpposite();
		final List<BlockPos> otherOptions = new ArrayList<>();

		if (newConnections != null) {
			final boolean canTurnBack = oldRail != null && oldRail.railType == RailType.TURN_BACK && !turnBacks.contains(newPos);
			if (oldRail != null && oldRail.railType == RailType.RUNWAY && newConnections.size() <= 1) {
				otherOptions.addAll(runways);
			} else {
				newConnections.forEach((connectedPos, rail) -> {
					if (canTurnBack || rail.railType != RailType.NONE && rail.facingStart != newDirection.getOpposite() && path.stream().noneMatch(pathPart -> pathPart.isSame(newPos, newDirection))) {
						otherOptions.add(connectedPos);
						if (canTurnBack) {
							turnBacks.add(newPos);
						}
					}
				});
			}
		}

		if (!otherOptions.isEmpty()) {
			otherOptions.sort(comparator.apply(newConnections));
			path.add(new PathPart(newDirection, newPos, otherOptions));
		}
	}

	private static BlockPos addAirplanePath(RailAngle startAngle, BlockPos startPos, RailAngle expectedAngle, int turnArc, List<PathData> tempRailPath, RailType railType, int stopIndex, boolean reverse) {
		final RailAngle angleDifference = expectedAngle.sub(startAngle);
		final boolean turnRight = angleDifference.angleRadians > 0;
		RailAngle tempAngle = startAngle;
		BlockPos tempPos = startPos;

		for (int i = 0; i < RailAngle.values().length; i++) {
			if (tempAngle == expectedAngle) {
				break;
			}

			final RailAngle oldTempAngle = tempAngle;
			final BlockPos oldTempPos = tempPos;
			final RailAngle rotateAngle = turnRight ? RailAngle.SEE : RailAngle.NEE;
			tempAngle = tempAngle.add(rotateAngle);
			final Vec3 posOffset = new Vec3(turnArc, 0, 0).yRot((float) -oldTempAngle.angleRadians - (float) rotateAngle.angleRadians / 2);
			tempPos = RailwayData.offsetBlockPos(oldTempPos, posOffset.x, posOffset.y, posOffset.z);

			if (reverse) {
				tempRailPath.add(0, new PathData(new Rail(tempPos, tempAngle.getOpposite(), oldTempPos, oldTempAngle, railType, TransportMode.AIRPLANE), 0, 0, tempPos, oldTempPos, stopIndex));
			} else {
				tempRailPath.add(new PathData(new Rail(oldTempPos, oldTempAngle, tempPos, tempAngle.getOpposite(), railType, TransportMode.AIRPLANE), 0, 0, oldTempPos, tempPos, stopIndex));
			}
		}

		return tempPos;
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
