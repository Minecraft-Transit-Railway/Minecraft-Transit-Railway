package mtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.function.BiConsumer;

public class RailwayDataRouteFinderModule extends RailwayDataModuleBase {

	private Map<BlockPos, Map<BlockPos, Integer>> connectionDensityOld = new HashMap<>();
	private Map<BlockPos, Map<BlockPos, Integer>> connectionDensity = new HashMap<>();
	private BlockPos startPos;
	private BlockPos endPos;
	private RouteFinderRequest currentRouteFinderRequest;
	private int totalTime;
	private int count;
	private long startMillis;
	private TickStage tickStage = TickStage.GET_POS;

	private final RailwayData railwayData;
	private final Map<BlockPos, Integer> globalBlacklist = new HashMap<>();
	private final Map<BlockPos, Integer> localBlacklist = new HashMap<>();
	private final List<RouteFinderData> tempData = new ArrayList<>();
	private final Set<BlockPos> platformPositions = new HashSet<>();
	private final List<RouteFinderData> data = new ArrayList<>();
	private final List<RouteFinderRequest> routeFinderQueue = new ArrayList<>();

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
		while (System.currentTimeMillis() - startTime < (currentRouteFinderRequest == null ? 2 : 20)) {
			switch (tickStage) {
				case GET_POS:
					if (routeFinderQueue.isEmpty()) {
						final Random random = new Random();
						final int startIndex = random.nextInt(railwayData.dataCache.platformConnections.size());
						final int endIndex = random.nextInt(railwayData.dataCache.platformConnections.size());
						currentRouteFinderRequest = null;

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
							startMillis = System.currentTimeMillis();
							totalTime = Integer.MAX_VALUE;
							tickStage = TickStage.START_FIND_ROUTE;
						}
					} else {
						currentRouteFinderRequest = routeFinderQueue.remove(0);
						startPos = currentRouteFinderRequest.startPos;
						endPos = currentRouteFinderRequest.endPos;
						globalBlacklist.clear();
						startMillis = System.currentTimeMillis();
						totalTime = Integer.MAX_VALUE;
						tickStage = TickStage.START_FIND_ROUTE;
					}
					break;
				case START_FIND_ROUTE:
					tempData.clear();
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
					if (tempData.isEmpty()) {
						for (int i = 0; i < data.size() - 1; i++) {
							DataCache.put(connectionDensity, data.get(i).pos, data.get(i + 1).pos, oldValue -> oldValue == null ? 1 : oldValue + 1);
						}

						count++;
						if (count >= getMaxCount()) {
							connectionDensityOld = connectionDensity;
							connectionDensity = new HashMap<>();
							count = 0;
						}

						if (currentRouteFinderRequest != null) {
							final List<RouteFinderData> newDataList = new ArrayList<>();
							for (final RouteFinderData routeFinderData : data) {
								RouteFinderData.append(newDataList, routeFinderData);
							}
							currentRouteFinderRequest.callback.accept(newDataList, (int) (System.currentTimeMillis() - startMillis));
						}

						tickStage = TickStage.GET_POS;
					} else {
						final int elapsedTime = tempData.stream().mapToInt(data -> data.duration).sum();
						if (elapsedTime > 0 && elapsedTime < totalTime) {
							totalTime = elapsedTime;
							data.clear();
							data.addAll(tempData);
						}

						tickStage = TickStage.START_FIND_ROUTE;
					}
					break;
			}
		}
	}

	public void findRoute(BlockPos posStart, BlockPos posEnd, BiConsumer<List<RouteFinderData>, Integer> callback) {
		routeFinderQueue.add(new RouteFinderRequest(posStart, posEnd, callback));
		tickStage = TickStage.GET_POS;
	}

	public int getConnectionDensity(BlockPos posStart, BlockPos posEnd) {
		return DataCache.tryGet(connectionDensityOld, posStart, posEnd, count == 0 ? 0 : DataCache.tryGet(connectionDensity, posStart, posEnd, 0) * getMaxCount() / count);
	}

	private boolean findRoutePart() {
		final int elapsedTime = tempData.stream().mapToInt(data -> data.duration).sum();
		final BlockPos prevPos = tempData.isEmpty() ? startPos : tempData.get(tempData.size() - 1).pos;

		BlockPos bestPosition = null;
		float bestIncrease = -Float.MAX_VALUE;
		int bestDuration = 0;
		long bestRouteId = 0;
		int bestWaitingTime = 0;

		for (final BlockPos thisPos : platformPositions) {
			final ConnectionDetails connectionDetails = DataCache.tryGet(railwayData.dataCache.platformConnections, prevPos, thisPos);
			int duration = prevPos.distManhattan(thisPos) * WALKING_SPEED_TICKS_PER_METER;
			long routeId = 0;
			int waitingTime = 0;

			if (connectionDetails != null) {
				final List<ScheduleEntry> scheduleEntries = railwayData.getSchedulesAtPlatform(connectionDetails.platformStart.id);
				if (scheduleEntries != null) {
					int lastDeparture = Integer.MIN_VALUE;
					for (final ScheduleEntry scheduleEntry : scheduleEntries) {
						if (connectionDetails.durationInfo.containsKey(scheduleEntry.routeId)) {
							final int delay = (int) ((scheduleEntry.arrivalMillis - startMillis) / Depot.MILLIS_PER_TICK - elapsedTime);
							final int newDuration = connectionDetails.durationInfo.get(scheduleEntry.routeId) + delay;
							lastDeparture = Math.max(lastDeparture, delay);
							if (delay > -100 && newDuration < duration) {
								duration = newDuration;
								routeId = scheduleEntry.routeId;
								waitingTime = Math.max(0, delay);
								lastDeparture = 0;
							}
						}
					}

					if (lastDeparture > Integer.MIN_VALUE && lastDeparture < 0) {
						for (final Map.Entry<Long, Integer> durationInfoEntry : connectionDetails.durationInfo.entrySet()) {
							final Depot depot = railwayData.dataCache.routeIdToOneDepot.get(durationInfoEntry.getKey());
							if (depot != null) {
								final int delay = depot.getMillisUntilDeploy(1, -lastDeparture * Depot.MILLIS_PER_TICK - 100) / Depot.MILLIS_PER_TICK;
								final int newDuration = durationInfoEntry.getValue() + delay;
								if (delay >= 0 && newDuration < duration) {
									duration = newDuration;
									routeId = durationInfoEntry.getKey();
									waitingTime = Math.max(0, delay - 100);
								}
							}
						}
					}
				}
			}

			if (elapsedTime + duration < totalTime && (!localBlacklist.containsKey(thisPos) || elapsedTime + duration < localBlacklist.get(thisPos)) && (!globalBlacklist.containsKey(thisPos) || elapsedTime + duration <= globalBlacklist.get(thisPos))) {
				final float increase = (float) (prevPos.distManhattan(endPos) - thisPos.distManhattan(endPos)) / duration;
				globalBlacklist.put(thisPos, elapsedTime + duration);
				if (increase > bestIncrease) {
					bestPosition = thisPos;
					bestIncrease = increase;
					bestDuration = duration;
					bestRouteId = routeId;
					bestWaitingTime = waitingTime;
				}
			}
		}

		if (bestPosition == null || bestDuration == 0) {
			if (!tempData.isEmpty()) {
				tempData.remove(tempData.size() - 1);
			} else {
				return true;
			}
		} else {
			localBlacklist.put(bestPosition, elapsedTime + bestDuration);
			tempData.add(new RouteFinderData(bestPosition, bestDuration, bestRouteId, bestWaitingTime, 1));
		}

		return !tempData.isEmpty() && tempData.get(tempData.size() - 1).pos.equals(endPos);
	}

	private int getMaxCount() {
		return railwayData.dataCache.platformConnections.size() * 100;
	}

	public static class ConnectionDetails {
		private final Platform platformStart;
		private final Map<Long, Integer> durationInfo = new HashMap<>();

		public ConnectionDetails(Platform platformStart) {
			this.platformStart = platformStart;
		}

		public void addDurationInfo(long routeId, int duration) {
			durationInfo.put(routeId, duration);
		}
	}

	public static class RouteFinderData {

		public final BlockPos pos;
		public final int duration;
		public final long routeId;
		public final int waitingTime;
		public final int stops;

		private RouteFinderData(BlockPos pos, int duration, long routeId, int waitingTime, int stops) {
			this.pos = pos;
			this.duration = duration;
			this.routeId = routeId;
			this.waitingTime = waitingTime;
			this.stops = stops;
		}

		private static void append(List<RouteFinderData> routeFinderDataList, RouteFinderData newRouteFinderData) {
			if (routeFinderDataList.isEmpty()) {
				routeFinderDataList.add(newRouteFinderData);
			} else {
				final int lastIndex = routeFinderDataList.size() - 1;
				final RouteFinderData lastRouteFinderData = routeFinderDataList.get(lastIndex);
				if (lastRouteFinderData.routeId == newRouteFinderData.routeId) {
					routeFinderDataList.remove(lastIndex);
					routeFinderDataList.add(new RouteFinderData(newRouteFinderData.pos, lastRouteFinderData.duration + newRouteFinderData.duration, lastRouteFinderData.routeId, lastRouteFinderData.waitingTime, lastRouteFinderData.routeId == 0 ? 1 : lastRouteFinderData.stops + newRouteFinderData.stops));
				} else {
					routeFinderDataList.add(newRouteFinderData);
				}
			}
		}
	}

	private static class RouteFinderRequest {

		private final BlockPos startPos;
		private final BlockPos endPos;
		private final BiConsumer<List<RouteFinderData>, Integer> callback;

		private RouteFinderRequest(BlockPos startPos, BlockPos endPos, BiConsumer<List<RouteFinderData>, Integer> callback) {
			this.startPos = startPos;
			this.endPos = endPos;
			this.callback = callback;
		}
	}

	private enum TickStage {GET_POS, START_FIND_ROUTE, FIND_ROUTE, END_FIND_ROUTE}
}
