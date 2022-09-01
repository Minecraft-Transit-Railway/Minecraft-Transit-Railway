package mtr.data;

import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class DataCache {

	public final Map<Long, Station> stationIdMap = new HashMap<>();
	public final Map<Long, Platform> platformIdMap = new HashMap<>();
	public final Map<Long, Siding> sidingIdMap = new HashMap<>();
	public final Map<Long, Route> routeIdMap = new HashMap<>();
	public final Map<Long, Depot> depotIdMap = new HashMap<>();

	public final Map<Long, Station> platformIdToStation = new HashMap<>();
	public final Map<Long, Depot> sidingIdToDepot = new HashMap<>();
	public final Map<Long, Depot> routeIdToOneDepot = new HashMap<>();
	public final Map<BlockPos, Station> blockPosToStation = new HashMap<>();
	public final Map<BlockPos, Long> blockPosToPlatformId = new HashMap<>();
	public final Map<BlockPos, Map<BlockPos, Integer>> platformConnections = new HashMap<>();

	protected final Set<Station> stations;
	protected final Set<Platform> platforms;
	protected final Set<Siding> sidings;
	protected final Set<Route> routes;
	protected final Set<Depot> depots;

	public DataCache(Set<Station> stations, Set<Platform> platforms, Set<Siding> sidings, Set<Route> routes, Set<Depot> depots) {
		this.stations = stations;
		this.platforms = platforms;
		this.sidings = sidings;
		this.routes = routes;
		this.depots = depots;
	}

	public final void sync() {
		try {
			mapIds(stationIdMap, stations);
			mapIds(platformIdMap, platforms);
			mapIds(sidingIdMap, sidings);
			mapIds(routeIdMap, routes);
			mapIds(depotIdMap, depots);

			routeIdToOneDepot.clear();
			routes.forEach(route -> route.platformIds.removeIf(platformId -> platformIdMap.get(platformId) == null));
			depots.forEach(depot -> {
				depot.routeIds.removeIf(routeId -> routeIdMap.get(routeId) == null);
				depot.routeIds.forEach(routeId -> routeIdToOneDepot.put(routeId, depot));
			});

			platformConnections.clear();
			routes.forEach(route -> {
				final Depot depot = routeIdToOneDepot.get(route.id);
				if (depot != null) {
					for (int i = 1; i < route.platformIds.size(); i++) {
						final long prevPlatformId = route.platformIds.get(i - 1);
						final long thisPlatformId = route.platformIds.get(i);
						final Platform prevPlatform = platformIdMap.get(prevPlatformId);
						final Platform thisPlatform = platformIdMap.get(thisPlatformId);
						if (prevPlatform != null && thisPlatform != null) {
							final float duration = tryGet(depot.platformTimes, prevPlatformId, thisPlatformId, 0F);
							if (duration > 0) {
								put(platformConnections, prevPlatform.getMidPos(), thisPlatform.getMidPos(), oldValue -> {
									final int newValue = Math.round(duration);
									if (oldValue == null) {
										return newValue;
									} else {
										return Math.min(oldValue, newValue);
									}
								});
							}
						}
					}
				}
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

	public static <T, U> void put(Map<T, Map<T, U>> map, T key1, T key2, Function<U, U> putValue) {
		final Map<T, U> innerMap = map.get(key1);
		final Map<T, U> newInnerMap;
		if (innerMap == null) {
			newInnerMap = new HashMap<>();
			map.put(key1, newInnerMap);
		} else {
			newInnerMap = innerMap;
		}
		newInnerMap.put(key2, putValue.apply(newInnerMap.get(key2)));
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

	private static <U extends NameColorDataBase> void mapIds(Map<Long, U> map, Set<U> source) {
		map.clear();
		source.forEach(data -> map.put(data.id, data));
	}
}
