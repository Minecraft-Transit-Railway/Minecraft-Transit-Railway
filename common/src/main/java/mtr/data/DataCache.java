package mtr.data;

import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataCache {

	public final Map<Long, Station> stationIdMap = new HashMap<>();
	public final Map<Long, Platform> platformIdMap = new HashMap<>();
	public final Map<Long, Siding> sidingIdMap = new HashMap<>();
	public final Map<Long, Route> routeIdMap = new HashMap<>();
	public final Map<Long, Depot> depotIdMap = new HashMap<>();

	public final Map<Long, Station> platformIdToStation = new HashMap<>();
	public final Map<Long, Depot> sidingIdToDepot = new HashMap<>();
	public final Map<BlockPos, Station> blockPosToStation = new HashMap<>();
	public final Map<BlockPos, Long> blockPosToPlatformId = new HashMap<>();

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

			routes.forEach(route -> route.platformIds.removeIf(platformId -> platformIdMap.get(platformId) == null));
			depots.forEach(depot -> depot.routeIds.removeIf(routeId -> routeIdMap.get(routeId) == null));

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
