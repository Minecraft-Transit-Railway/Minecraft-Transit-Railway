package mtr.data;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class DataCache {

	public final Map<Long, Station> stationIdMap = new HashMap<>();
	public final Map<Long, Platform> platformIdMap = new HashMap<>();
	public final Map<Long, Siding> sidingIdMap = new HashMap<>();
	public final Map<Long, Route> routeIdMap = new HashMap<>();
	public final Map<Long, Depot> depotIdMap = new HashMap<>();
	public final Map<Long, LiftServer> liftsServerIdMap = new HashMap<>();

	public final Map<Long, Station> platformIdToStation = new HashMap<>();
	public final Map<Long, Depot> sidingIdToDepot = new HashMap<>();
	public final Map<Long, Depot> routeIdToOneDepot = new HashMap<>();
	public final Map<Station, Set<Station>> stationIdToConnectingStations = new HashMap<>();
	public final Map<BlockPos, Station> blockPosToStation = new HashMap<>();
	public final Map<BlockPos, Long> blockPosToPlatformId = new HashMap<>();
	public final Long2ObjectOpenHashMap<Long2ObjectOpenHashMap<RailwayDataRouteFinderModule.ConnectionDetails>> platformConnections = new Long2ObjectOpenHashMap<>();

	protected final Set<Station> stations;
	protected final Set<Platform> platforms;
	protected final Set<Siding> sidings;
	protected final Set<Route> routes;
	protected final Set<Depot> depots;
	private final Set<LiftServer> liftsServer;

	public DataCache(Set<Station> stations, Set<Platform> platforms, Set<Siding> sidings, Set<Route> routes, Set<Depot> depots, Set<LiftServer> lifts) {
		this.stations = stations;
		this.platforms = platforms;
		this.sidings = sidings;
		this.routes = routes;
		this.depots = depots;
		liftsServer = lifts;
	}

	public final void sync() {
		try {
			mapIds(stationIdMap, stations);
			mapIds(platformIdMap, platforms);
			mapIds(sidingIdMap, sidings);
			mapIds(routeIdMap, routes);
			mapIds(depotIdMap, depots);
			mapIds(liftsServerIdMap, liftsServer);

			routeIdToOneDepot.clear();
			routes.forEach(route -> route.platformIds.removeIf(platformId -> !platformIdMap.containsKey(platformId.platformId)));
			depots.forEach(depot -> {
				depot.routeIds.removeIf(routeId -> routeIdMap.get(routeId) == null);
				depot.routeIds.forEach(routeId -> routeIdToOneDepot.put(routeId, depot));
			});

			platformConnections.clear();
			routes.forEach(route -> {
				final Depot depot = routeIdToOneDepot.get(route.id);
				if (depot != null) {
					for (int i = 1; i < route.platformIds.size(); i++) {
						final long prevPlatformId = route.platformIds.get(i - 1).platformId;
						final long thisPlatformId = route.platformIds.get(i).platformId;
						final Platform prevPlatform = platformIdMap.get(prevPlatformId);
						final Platform thisPlatform = platformIdMap.get(thisPlatformId);
						if (prevPlatform != null && thisPlatform != null) {
							final float duration = tryGet(depot.platformTimes, prevPlatformId, thisPlatformId, 0F);
							if (duration > 0) {
								final long thisPlatformPosLong = thisPlatform.getMidPos().asLong();
								put(platformConnections, prevPlatform.getMidPos().asLong(), thisPlatformPosLong, oldValue -> {
									final int newValue = Math.round(duration);
									if (oldValue == null) {
										final RailwayDataRouteFinderModule.ConnectionDetails connectionDetails = new RailwayDataRouteFinderModule.ConnectionDetails(prevPlatform);
										connectionDetails.addDurationInfo(route.id, newValue);
										return connectionDetails;
									} else {
										oldValue.addDurationInfo(route.id, newValue);
										return oldValue;
									}
								});
								if (i == route.platformIds.size() - 1 && !platformConnections.containsKey(thisPlatformPosLong)) {
									platformConnections.put(thisPlatformPosLong, new Long2ObjectOpenHashMap<>());
								}
							}
						}
					}
				}
			});

			stationIdToConnectingStations.clear();
			stations.forEach(station1 -> {
				stationIdToConnectingStations.put(station1, new HashSet<>());
				stations.forEach(station2 -> {
					if (station1 != station2 && station1.intersecting(station2)) {
						stationIdToConnectingStations.get(station1).add(station2);
					}
				});
			});

			mapSavedRailIdToStation(platformIdToStation, platforms, stations);
			mapSavedRailIdToStation(sidingIdToDepot, sidings, depots);

			blockPosToPlatformId.clear();
			blockPosToStation.clear();
			syncAdditional();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void syncAdditional() {
	}

	public static <T, U> U tryGet(Map<T, Map<T, U>> map, T key1, T key2, U defaultValue) {
		final U result = tryGet(map, key1, key2);
		return result == null ? defaultValue : result;
	}

	public static <T, U> U tryGet(Map<T, Map<T, U>> map, T key1, T key2) {
		final Map<T, U> innerMap = map.get(key1);
		if (innerMap == null) {
			return null;
		} else {
			return innerMap.get(key2);
		}
	}

	public static <U> U tryGet(Long2ObjectOpenHashMap<Long2ObjectOpenHashMap<U>> map, long key1, long key2) {
		final Long2ObjectOpenHashMap<U> innerMap = map.get(key1);
		if (innerMap == null) {
			return null;
		} else {
			return innerMap.get(key2);
		}
	}

	public static int tryGet2(Long2ObjectOpenHashMap<Long2IntOpenHashMap> map, long key1, long key2, int defaultValue) {
		final Long2IntOpenHashMap innerMap = map.get(key1);
		if (innerMap == null) {
			return defaultValue;
		} else {
			return innerMap.getOrDefault(key2, defaultValue);
		}
	}

	public static <U> void put(Long2ObjectOpenHashMap<Long2ObjectOpenHashMap<U>> map, long key1, long key2, Function<U, U> putValue) {
		final Long2ObjectOpenHashMap<U> innerMap = map.get(key1);
		final Long2ObjectOpenHashMap<U> newInnerMap;
		if (innerMap == null) {
			newInnerMap = new Long2ObjectOpenHashMap<>();
			map.put(key1, newInnerMap);
		} else {
			newInnerMap = innerMap;
		}
		newInnerMap.put(key2, putValue.apply(newInnerMap.get(key2)));
	}

	public static void put2(Long2ObjectOpenHashMap<Long2IntOpenHashMap> map, long key1, long key2, Function<Integer, Integer> putValue) {
		final Long2IntOpenHashMap innerMap = map.get(key1);
		final Long2IntOpenHashMap newInnerMap;
		if (innerMap == null) {
			newInnerMap = new Long2IntOpenHashMap();
			map.put(key1, newInnerMap);
		} else {
			newInnerMap = innerMap;
		}
		newInnerMap.put(key2, putValue.apply(newInnerMap.get(key2)).intValue());
	}

	protected static <U extends NameColorDataBase> void mapIds(Map<Long, U> map, Set<U> source) {
		map.clear();
		source.forEach(data -> map.put(data.id, data));
	}

	private static <U extends SavedRailBase, V extends AreaBase> void mapSavedRailIdToStation(Map<Long, V> map, Set<U> savedRails, Set<V> areas) {
		map.clear();
		savedRails.forEach(savedRail -> {
			final BlockPos pos = savedRail.getMidPos();
			for (final V area : areas) {
				if (area.isTransportMode(savedRail.transportMode) && area.inArea(pos.getX(), pos.getZ())) {
					map.put(savedRail.id, area);
					break;
				}
			}
		});
	}
}
