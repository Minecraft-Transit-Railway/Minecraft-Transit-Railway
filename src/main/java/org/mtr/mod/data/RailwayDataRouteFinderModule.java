package mtr.data;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.function.BiConsumer;

public class RailwayDataRouteFinderModule extends RailwayDataModuleBase {

	private Long2ObjectOpenHashMap<Long2IntOpenHashMap> connectionDensityOld = new Long2ObjectOpenHashMap<>();
	private Long2ObjectOpenHashMap<Long2IntOpenHashMap> connectionDensity = new Long2ObjectOpenHashMap<>();
	private BlockPos startPos;
	private BlockPos endPos;
	private RouteFinderRequest currentRouteFinderRequest;
	private int totalTime;
	private int count;
	private long startMillis;
	private TickStage tickStage = TickStage.GET_POS;

	private final RailwayData railwayData;
	private final Long2IntOpenHashMap globalBlacklist = new Long2IntOpenHashMap();
	private final Long2IntOpenHashMap localBlacklist = new Long2IntOpenHashMap();
	private final List<RouteFinderData> tempData = new ArrayList<>();
	private final LongAVLTreeSet platformPositions = new LongAVLTreeSet();
	private final List<RouteFinderData> data = new ArrayList<>();
	private final List<RouteFinderRequest> routeFinderQueue = new ArrayList<>();

	private static final int MAX_REQUESTS = 10;
	private static final int WALKING_SPEED_TICKS_PER_METER = 5;
	private static final boolean DEBUG_DISABLE_DENSITY = false;

	public RailwayDataRouteFinderModule(RailwayData railwayData, Level world, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		super(railwayData, world, rails);
		this.railwayData = railwayData;
	}

	public void tick() {
		final int platformCount = railwayData.dataCache.platformConnections.size();
		if (platformCount < 4) {
			return;
		}

		final long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < (currentRouteFinderRequest == null ? 2 : currentRouteFinderRequest.maxTickTime)) {
			switch (tickStage) {
				case GET_POS:
					if (routeFinderQueue.isEmpty()) {
						if (DEBUG_DISABLE_DENSITY) {
							return;
						}

						final Random random = new Random();
						int shortestDistance = Integer.MAX_VALUE;
						BlockPos tempStartPos = null;
						BlockPos tempEndPos = null;
						currentRouteFinderRequest = null;
						final List<Long> platformConnectionKeys = new ArrayList<>(railwayData.dataCache.platformConnections.keySet());

						for (int i = 0; i <= Math.min(200, platformCount * platformCount / 10000); i++) {
							while (true) {
								final int startIndex = random.nextInt(platformCount);
								final int endIndex = random.nextInt(platformCount);

								if (startIndex != endIndex) {
									final BlockPos tempStartPos2 = BlockPos.of(platformConnectionKeys.get(startIndex));
									final BlockPos tempEndPos2 = BlockPos.of(platformConnectionKeys.get(endIndex));
									final int tempDistance = tempStartPos2.distManhattan(tempEndPos2);
									if (tempDistance < shortestDistance) {
										shortestDistance = tempDistance;
										tempStartPos = tempStartPos2;
										tempEndPos = tempEndPos2;
									}
									break;
								}
							}
						}

						startPos = tempStartPos;
						endPos = tempEndPos;
					} else {
						currentRouteFinderRequest = routeFinderQueue.remove(0);
						startPos = currentRouteFinderRequest.startPos;
						endPos = currentRouteFinderRequest.endPos;
					}

					globalBlacklist.clear();
					startMillis = System.currentTimeMillis();
					totalTime = Integer.MAX_VALUE;
					tickStage = TickStage.START_FIND_ROUTE;
					break;
				case START_FIND_ROUTE:
					tempData.clear();
					platformPositions.clear();
					platformPositions.addAll(railwayData.dataCache.platformConnections.keySet());
					platformPositions.add(endPos.asLong());
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
							DataCache.put2(connectionDensity, data.get(i).pos.asLong(), data.get(i + 1).pos.asLong(), oldValue -> oldValue == null ? 1 : oldValue + 1);
						}

						count++;
						if (count >= getMaxCount()) {
							connectionDensityOld = connectionDensity;
							connectionDensity = new Long2ObjectOpenHashMap<>();
							count = 0;
						}

						if (currentRouteFinderRequest != null) {
							final List<RouteFinderData> newDataList = new ArrayList<>();
							for (final RouteFinderData routeFinderData : data) {
								RouteFinderData.append(newDataList, routeFinderData, railwayData);
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

	public boolean findRoute(BlockPos posStart, BlockPos posEnd, int maxTickTime, BiConsumer<List<RouteFinderData>, Integer> callback) {
		if (routeFinderQueue.size() < MAX_REQUESTS) {
			routeFinderQueue.add(new RouteFinderRequest(posStart, posEnd, maxTickTime, callback));
			tickStage = TickStage.GET_POS;
			return true;
		} else {
			return false;
		}
	}

	public int getConnectionDensity(BlockPos posStart, BlockPos posEnd) {
		return DataCache.tryGet2(connectionDensityOld, posStart.asLong(), posEnd.asLong(), count == 0 ? 0 : DataCache.tryGet2(connectionDensity, posStart.asLong(), posEnd.asLong(), 0) * getMaxCount() / count);
	}

	private boolean findRoutePart() {
		final int elapsedTime = tempData.stream().mapToInt(data -> data.duration).sum();
		final BlockPos prevPos = tempData.isEmpty() ? startPos : tempData.get(tempData.size() - 1).pos;

		BlockPos bestPosition = null;
		float bestIncrease = -Float.MAX_VALUE;
		int bestDuration = 0;
		long bestRouteId = 0;
		int bestWaitingTime = 0;

		for (final long thisPosLong : platformPositions) {
			final ConnectionDetails connectionDetails = DataCache.tryGet(railwayData.dataCache.platformConnections, prevPos.asLong(), thisPosLong);
			final BlockPos thisPos = BlockPos.of(thisPosLong);
			int duration = prevPos.distManhattan(thisPos) * WALKING_SPEED_TICKS_PER_METER;
			long routeId = 0;
			int waitingTime = 0;

			if (connectionDetails != null && verifyTime(thisPosLong, elapsedTime + connectionDetails.shortestDuration)) {
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

			if (verifyTime(thisPosLong, elapsedTime + duration)) {
				final float increase = (float) (prevPos.distManhattan(endPos) - thisPos.distManhattan(endPos)) / duration;
				globalBlacklist.put(thisPosLong, elapsedTime + duration);
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
			localBlacklist.put(bestPosition.asLong(), elapsedTime + bestDuration);
			tempData.add(new RouteFinderData(bestPosition, bestDuration, bestRouteId, bestWaitingTime));
		}

		return !tempData.isEmpty() && tempData.get(tempData.size() - 1).pos.equals(endPos);
	}

	private boolean verifyTime(long posLong, int time) {
		return time < totalTime && compareBlacklist(localBlacklist, posLong, time, false) && compareBlacklist(globalBlacklist, posLong, time, true);
	}

	private int getMaxCount() {
		return railwayData.dataCache.platformConnections.size() * 100;
	}

	private static boolean compareBlacklist(Long2IntOpenHashMap blacklist, long posLong, int time, boolean lessThanOrEqualTo) {
		return !blacklist.containsKey(posLong) || (lessThanOrEqualTo ? time <= blacklist.get(posLong) : time < blacklist.get(posLong));
	}

	public static class ConnectionDetails {

		private int shortestDuration = Integer.MAX_VALUE;
		private final Platform platformStart;
		private final Map<Long, Integer> durationInfo = new HashMap<>();

		public ConnectionDetails(Platform platformStart) {
			this.platformStart = platformStart;
		}

		public void addDurationInfo(long routeId, int duration) {
			durationInfo.put(routeId, duration);
			shortestDuration = Math.min(duration, shortestDuration);
		}
	}

	public static class RouteFinderData {

		public final BlockPos pos;
		public final int duration;
		public final long routeId;
		public final int waitingTime;
		public final List<Long> stationIds = new ArrayList<>();

		private RouteFinderData(BlockPos pos, int duration, long routeId, int waitingTime) {
			this.pos = pos;
			this.duration = duration;
			this.routeId = routeId;
			this.waitingTime = waitingTime;
		}

		private static void append(List<RouteFinderData> routeFinderDataList, RouteFinderData newRouteFinderData, RailwayData railwayData) {
			final Station station = RailwayData.getStation(railwayData.stations, railwayData.dataCache, newRouteFinderData.pos);
			final long stationId = station == null ? 0 : station.id;
			if (routeFinderDataList.isEmpty()) {
				newRouteFinderData.stationIds.add(stationId);
				routeFinderDataList.add(newRouteFinderData);
			} else {
				final int lastIndex = routeFinderDataList.size() - 1;
				final RouteFinderData lastRouteFinderData = routeFinderDataList.get(lastIndex);
				if (lastRouteFinderData.routeId == newRouteFinderData.routeId) {
					routeFinderDataList.remove(lastIndex);
					final RouteFinderData routeFinderDataToAdd = new RouteFinderData(newRouteFinderData.pos, lastRouteFinderData.duration + newRouteFinderData.duration, lastRouteFinderData.routeId, lastRouteFinderData.waitingTime);
					routeFinderDataToAdd.stationIds.addAll(lastRouteFinderData.stationIds);
					routeFinderDataToAdd.stationIds.add(stationId);
					routeFinderDataList.add(routeFinderDataToAdd);
				} else {
					newRouteFinderData.stationIds.add(stationId);
					routeFinderDataList.add(newRouteFinderData);
				}
			}
		}
	}

	private static class RouteFinderRequest {

		private final BlockPos startPos;
		private final BlockPos endPos;
		private final int maxTickTime;
		private final BiConsumer<List<RouteFinderData>, Integer> callback;

		private RouteFinderRequest(BlockPos startPos, BlockPos endPos, int maxTickTime, BiConsumer<List<RouteFinderData>, Integer> callback) {
			this.startPos = startPos;
			this.endPos = endPos;
			this.maxTickTime = Math.max(2, maxTickTime);
			this.callback = callback;
		}
	}

	private enum TickStage {GET_POS, START_FIND_ROUTE, FIND_ROUTE, END_FIND_ROUTE}
}
