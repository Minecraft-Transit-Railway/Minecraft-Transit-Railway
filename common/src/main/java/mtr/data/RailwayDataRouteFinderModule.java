package mtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;

public class RailwayDataRouteFinderModule extends RailwayDataModuleBase {

	final RailwayData railwayData;

	private static final int WALKING_SPEED_TICKS_PER_METER = 5;

	public RailwayDataRouteFinderModule(RailwayData railwayData, Level world, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		super(railwayData, world, rails);
		this.railwayData = railwayData;
	}

	public void findRoute(BlockPos startPos, BlockPos endPos) {
		final Map<BlockPos, Integer> globalBlacklist = new HashMap<>();
		final List<BlockPos> positions = new ArrayList<>();
		final List<Integer> durations = new ArrayList<>();
		final long millis = System.currentTimeMillis();
		int totalTime = Integer.MAX_VALUE;

		for (int i = 0; i < 500; i++) {
			final List<BlockPos> tempPositions = new ArrayList<>();
			final List<Integer> tempDurations = new ArrayList<>();
			final Set<BlockPos> platformPositions = new HashSet<>(railwayData.dataCache.platformConnections.keySet());
			platformPositions.add(endPos);
			findRoutePart(tempPositions, tempDurations, startPos, endPos, platformPositions, totalTime, globalBlacklist);

			if (tempPositions.isEmpty()) {
				System.out.println("Completed in " + i + " tries");
				break;
			} else {
				final int elapsedTime = tempDurations.stream().mapToInt(Integer::intValue).sum();
				if (elapsedTime > 0 && elapsedTime < totalTime) {
					totalTime = elapsedTime;
					positions.clear();
					positions.addAll(tempPositions);
					durations.clear();
					durations.addAll(tempDurations);

				}
			}
		}

		positions.forEach(pos -> {
			if (pos != null) {
				final Station station = RailwayData.getStation(railwayData.stations, railwayData.dataCache, pos);
				if (station != null) {
					System.out.println(station.name);
				}
			}
		});
		System.out.println((System.currentTimeMillis() - millis) / 1000F + " s");
	}

	private void findRoutePart(List<BlockPos> positions, List<Integer> durations, BlockPos startPos, BlockPos endPos, Set<BlockPos> platformPositions, int maxTime, Map<BlockPos, Integer> globalBlacklist) {
		final Map<BlockPos, Integer> localBlacklist = new HashMap<>();
		while (!positions.contains(endPos)) {
			final int elapsedTime = durations.stream().mapToInt(Integer::intValue).sum();
			final BlockPos prevPos = positions.isEmpty() ? startPos : positions.get(positions.size() - 1);

			BlockPos bestPosition = null;
			float bestIncrease = -Float.MAX_VALUE;
			int bestDuration = 0;

			for (final BlockPos thisPos : platformPositions) {
				final int duration;
				if (railwayData.dataCache.platformConnections.containsKey(prevPos) && railwayData.dataCache.platformConnections.get(prevPos).containsKey(thisPos)) {
					duration = railwayData.dataCache.platformConnections.get(prevPos).get(thisPos);
				} else {
					duration = prevPos.distManhattan(thisPos) * WALKING_SPEED_TICKS_PER_METER;
				}

				if (elapsedTime + duration < maxTime && (!localBlacklist.containsKey(thisPos) || elapsedTime + duration < localBlacklist.get(thisPos)) && (!globalBlacklist.containsKey(thisPos) || elapsedTime + duration <= globalBlacklist.get(thisPos))) {
					final float increase = (float) (prevPos.distManhattan(endPos) - thisPos.distManhattan(endPos)) / duration;
					globalBlacklist.put(thisPos, elapsedTime + duration);
					if (increase > bestIncrease) {
						bestPosition = thisPos;
						bestIncrease = increase;
						bestDuration = duration;
					}
				}
			}

			if (bestPosition == null || bestDuration == 0) {
				if (!positions.isEmpty() && !durations.isEmpty()) {
					positions.remove(positions.size() - 1);
					durations.remove(durations.size() - 1);
				} else {
					break;
				}
			} else {
				localBlacklist.put(bestPosition, elapsedTime + bestDuration);
				positions.add(bestPosition);
				durations.add(bestDuration);
			}
		}
	}
}
