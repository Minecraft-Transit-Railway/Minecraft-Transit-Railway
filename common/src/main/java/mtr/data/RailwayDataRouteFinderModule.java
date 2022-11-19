package mtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;

public class RailwayDataRouteFinderModule extends RailwayDataModuleBase {

	private Map<BlockPos, Map<BlockPos, Integer>> connectionDensityOld = new HashMap<>();
	private Map<BlockPos, Map<BlockPos, Integer>> connectionDensity = new HashMap<>();
	private BlockPos startPos;
	private BlockPos endPos;
	private int totalTime;
	private int count;
	private TickStage tickStage = TickStage.GET_POS;

	private final RailwayData railwayData;
	private final Map<BlockPos, Integer> globalBlacklist = new HashMap<>();
	private final Map<BlockPos, Integer> localBlacklist = new HashMap<>();
	private final List<BlockPos> tempPositions = new ArrayList<>();
	private final List<Integer> tempDurations = new ArrayList<>();
	private final Set<BlockPos> platformPositions = new HashSet<>();
	private final List<BlockPos> positions = new ArrayList<>();

	private static final int WALKING_SPEED_TICKS_PER_METER = 5;

	public RailwayDataRouteFinderModule(RailwayData railwayData, Level world, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		super(railwayData, world, rails);
		this.railwayData = railwayData;
	}

	public void tick() {
		if (railwayData.dataCache.platformConnections.size() < 4) {
			return;
		}

		final long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < 2) {
			switch (tickStage) {
				case GET_POS:
					final Random random = new Random();
					final int startIndex = random.nextInt(railwayData.dataCache.platformConnections.size());
					final int endIndex = random.nextInt(railwayData.dataCache.platformConnections.size());

					if (startIndex != endIndex) {
						int i = 0;
						for (final BlockPos pos : railwayData.dataCache.platformConnections.keySet()) {
							if (i == startIndex) {
								startPos = pos;
							}
							if (i == endIndex) {
								endPos = pos;
							}
							i++;
						}

						globalBlacklist.clear();
						totalTime = Integer.MAX_VALUE;
						tickStage = TickStage.START_FIND_ROUTE;
					}
					break;
				case START_FIND_ROUTE:
					tempPositions.clear();
					tempDurations.clear();
					platformPositions.clear();
					platformPositions.addAll(railwayData.dataCache.platformConnections.keySet());
					platformPositions.add(endPos);
					localBlacklist.clear();
					tickStage = TickStage.FIND_ROUTE;
					break;
				case FIND_ROUTE:
					if (startPos == null || endPos == null) {
						tickStage = TickStage.GET_POS;
					} else if (findRoutePart()) {
						tickStage = TickStage.END_FIND_ROUTE;
					}
					break;
				case END_FIND_ROUTE:
					if (tempPositions.isEmpty()) {
						for (int i = 0; i < positions.size() - 1; i++) {
							DataCache.put(connectionDensity, positions.get(i), positions.get(i + 1), oldValue -> oldValue == null ? 1 : oldValue + 1);
						}

						count++;
						if (count >= getMaxCount()) {
							connectionDensityOld = connectionDensity;
							connectionDensity = new HashMap<>();
							count = 0;
						}

						tickStage = TickStage.GET_POS;
					} else {
						final int elapsedTime = tempDurations.stream().mapToInt(Integer::intValue).sum();
						if (elapsedTime > 0 && elapsedTime < totalTime) {
							totalTime = elapsedTime;
							positions.clear();
							positions.addAll(tempPositions);
						}

						tickStage = TickStage.START_FIND_ROUTE;
					}
					break;
			}
		}
	}

	public int getConnectionDensity(BlockPos posStart, BlockPos posEnd) {
		return DataCache.tryGet(connectionDensityOld, posStart, posEnd, count == 0 ? 0 : DataCache.tryGet(connectionDensity, posStart, posEnd, 0) * getMaxCount() / count);
	}

	private boolean findRoutePart() {
		final int elapsedTime = tempDurations.stream().mapToInt(Integer::intValue).sum();
		final BlockPos prevPos = tempPositions.isEmpty() ? startPos : tempPositions.get(tempPositions.size() - 1);

		BlockPos bestPosition = null;
		float bestIncrease = -Float.MAX_VALUE;
		int bestDuration = 0;

		for (final BlockPos thisPos : platformPositions) {
			final int duration = DataCache.tryGet(railwayData.dataCache.platformConnections, prevPos, thisPos, prevPos.distManhattan(thisPos) * WALKING_SPEED_TICKS_PER_METER);
			if (elapsedTime + duration < totalTime && (!localBlacklist.containsKey(thisPos) || elapsedTime + duration < localBlacklist.get(thisPos)) && (!globalBlacklist.containsKey(thisPos) || elapsedTime + duration <= globalBlacklist.get(thisPos))) {
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
			if (!tempPositions.isEmpty() && !tempDurations.isEmpty()) {
				tempPositions.remove(tempPositions.size() - 1);
				tempDurations.remove(tempDurations.size() - 1);
			} else {
				return true;
			}
		} else {
			localBlacklist.put(bestPosition, elapsedTime + bestDuration);
			tempPositions.add(bestPosition);
			tempDurations.add(bestDuration);
		}

		return tempPositions.contains(endPos);
	}

	private int getMaxCount() {
		return railwayData.dataCache.platformConnections.size() * 100;
	}

	private enum TickStage {GET_POS, START_FIND_ROUTE, FIND_ROUTE, END_FIND_ROUTE}
}
