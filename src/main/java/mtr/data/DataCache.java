package mtr.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.thread.ReentrantThreadExecutor;

import java.util.*;
import java.util.stream.Collectors;

public class DataCache<T extends ReentrantThreadExecutor<? extends Runnable>> extends Thread {

	public final Map<Long, Station> stationIdMap = new HashMap<>();
	public final Map<Long, Platform> platformIdMap = new HashMap<>();
	public final Map<Long, Siding> sidingIdMap = new HashMap<>();
	public final Map<Long, Route> routeIdMap = new HashMap<>();
	public final Map<Long, Depot> depotIdMap = new HashMap<>();

	public final Map<Long, Station> platformIdToStation = new HashMap<>();
	public final Map<Long, Depot> sidingIdToDepot = new HashMap<>();
	public final Map<Long, Map<Long, Platform>> stationIdToPlatforms = new HashMap<>();
	public final Map<Long, Map<Long, Siding>> depotIdToSidings = new HashMap<>();
	public final Map<BlockPos, List<Platform>> posToPlatforms = new HashMap<>();
	public final Map<BlockPos, List<Siding>> posToSidings = new HashMap<>();
	public final Map<Long, Map<Integer, ColorNamePair>> stationIdToRoutes = new HashMap<>();
	public final Map<Long, List<PlatformRouteDetails>> platformIdToRoutes = new HashMap<>();

	private final T minecraft;

	private final Set<Station> stations;
	private final Set<Platform> platforms;
	private final Set<Siding> sidings;
	private final Set<Route> routes;
	private final Set<Depot> depots;

	private final Set<Station> stationsTemp = new HashSet<>();
	private final Set<Platform> platformsTemp = new HashSet<>();
	private final Set<Siding> sidingsTemp = new HashSet<>();
	private final Set<Route> routesTemp = new HashSet<>();
	private final Set<Depot> depotsTemp = new HashSet<>();

	private final Map<Long, Station> stationIdMapTemp = new HashMap<>();
	private final Map<Long, Platform> platformIdMapTemp = new HashMap<>();
	private final Map<Long, Siding> sidingIdMapTemp = new HashMap<>();
	private final Map<Long, Route> routeIdMapTemp = new HashMap<>();
	private final Map<Long, Depot> depotIdMapTemp = new HashMap<>();

	private final Map<Long, Station> platformIdToStationTemp = new HashMap<>();
	private final Map<Long, Depot> sidingIdToDepotTemp = new HashMap<>();
	private final Map<Long, Map<Long, Platform>> stationIdToPlatformsTemp = new HashMap<>();
	private final Map<Long, Map<Long, Siding>> depotIdToSidingsTemp = new HashMap<>();
	private final Map<BlockPos, List<Platform>> posToPlatformsTemp = new HashMap<>();
	private final Map<BlockPos, List<Siding>> posToSidingsTemp = new HashMap<>();
	private final Map<Long, Map<Integer, ColorNamePair>> stationIdToRoutesTemp = new HashMap<>();
	private final Map<Long, List<PlatformRouteDetails>> platformIdToRoutesTemp = new HashMap<>();

	private final Object monitor = new Object();

	public DataCache(T minecraft, Set<Station> stations, Set<Platform> platforms, Set<Siding> sidings, Set<Route> routes, Set<Depot> depots) {
		this.minecraft = minecraft;
		this.stations = stations;
		this.platforms = platforms;
		this.sidings = sidings;
		this.routes = routes;
		this.depots = depots;
		setDaemon(true);
	}

	@Override
	public void run() {
		System.out.println("Starting cache thread on " + minecraft);

		while (!currentThread().isInterrupted()) {
			try {
				mapIds(stationIdMapTemp, stationsTemp);
				mapIds(platformIdMapTemp, platformsTemp);
				mapIds(sidingIdMapTemp, sidingsTemp);
				mapIds(routeIdMapTemp, routesTemp);
				mapIds(depotIdMapTemp, depotsTemp);

				mapSavedRailIdToStation(platformIdToStationTemp, platformsTemp, stationsTemp);
				mapSavedRailIdToStation(sidingIdToDepotTemp, sidingsTemp, depotsTemp);
				mapAreaIdToSavedRails(stationIdToPlatformsTemp, posToPlatformsTemp, stationsTemp, platformsTemp);
				mapAreaIdToSavedRails(depotIdToSidingsTemp, posToSidingsTemp, depotsTemp, sidingsTemp);

				stationIdToRoutesTemp.clear();
				routesTemp.forEach(route -> route.platformIds.forEach(platformId -> {
					final Station station = platformIdToStationTemp.get(platformId);
					if (station != null) {
						if (!stationIdToRoutesTemp.containsKey(station.id)) {
							stationIdToRoutesTemp.put(station.id, new HashMap<>());
						}
						stationIdToRoutesTemp.get(station.id).put(route.color, new ColorNamePair(route.color, route.name.split("\\|\\|")[0]));
					}
				}));

				platformIdToRoutesTemp.clear();
				platformIdToRoutesTemp.putAll(platformsTemp.stream().collect(Collectors.toMap(platform -> platform.id, platform -> routesTemp.stream().filter(route -> route.platformIds.contains(platform.id)).map(route -> {
					final List<PlatformRouteDetails.StationDetails> stationDetails = route.platformIds.stream().map(platformId -> {
						final Station station = platformIdToStationTemp.get(platformId);
						if (station == null) {
							return new PlatformRouteDetails.StationDetails("", new ArrayList<>());
						} else {
							return new PlatformRouteDetails.StationDetails(station.name, stationIdToRoutesTemp.get(station.id).values().stream().filter(colorNamePair -> colorNamePair.color != route.color).collect(Collectors.toList()));
						}
					}).collect(Collectors.toList());
					return new PlatformRouteDetails(route.name.split("\\|\\|")[0], route.color, route.circularState, route.platformIds.indexOf(platform.id), stationDetails);
				}).collect(Collectors.toList()))));

				if (platformsTemp.size() == platformIdMapTemp.size() && sidingsTemp.size() == sidingIdMapTemp.size() && routesTemp.size() == routeIdMapTemp.size() && depotsTemp.size() == depotIdMapTemp.size()) {
					routesTemp.forEach(route -> route.platformIds.removeIf(platformId -> platformIdMapTemp.get(platformId) == null));
					depotsTemp.forEach(depot -> depot.routeIds.removeIf(routeId -> routeIdMapTemp.get(routeId) == null));
				}

				minecraft.execute(() -> {
					clearAndAddAll(stationsTemp, stations);
					clearAndAddAll(platformsTemp, platforms);
					clearAndAddAll(sidingsTemp, sidings);
					clearAndAddAll(routesTemp, routes);
					clearAndAddAll(depotsTemp, depots);

					clearAndAddAll(stationIdMap, stationIdMapTemp);
					clearAndAddAll(platformIdMap, platformIdMapTemp);
					clearAndAddAll(sidingIdMap, sidingIdMapTemp);
					clearAndAddAll(routeIdMap, routeIdMapTemp);
					clearAndAddAll(depotIdMap, depotIdMapTemp);

					clearAndAddAll(platformIdToStation, platformIdToStationTemp);
					clearAndAddAll(sidingIdToDepot, sidingIdToDepotTemp);
					clearAndAddAll(stationIdToPlatforms, stationIdToPlatformsTemp);
					clearAndAddAll(depotIdToSidings, depotIdToSidingsTemp);
					clearAndAddAll(posToPlatforms, posToPlatformsTemp);
					clearAndAddAll(posToSidings, posToSidingsTemp);
					clearAndAddAll(stationIdToRoutes, stationIdToRoutesTemp);
					clearAndAddAll(platformIdToRoutes, platformIdToRoutesTemp);

					synchronized (monitor) {
						monitor.notifyAll();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

			synchronized (monitor) {
				try {
					monitor.wait();
				} catch (InterruptedException ignored) {
					break;
				}
			}
		}

		System.out.println("Stopping cache thread on " + minecraft);
	}

	public static <U> void clearAndAddAll(Set<U> target, Set<U> source) {
		target.clear();
		target.addAll(source);
	}

	public static <U, V> void clearAndAddAll(Map<U, V> target, Map<U, V> source) {
		target.clear();
		target.putAll(source);
	}

	private static <U extends SavedRailBase, V extends AreaBase> void mapSavedRailIdToStation(Map<Long, V> map, Set<U> savedRails, Set<V> areas) {
		map.clear();
		savedRails.forEach(savedRail -> {
			final BlockPos pos = savedRail.getMidPos();
			for (final V area : areas) {
				if (area.inArea(pos.getX(), pos.getZ())) {
					map.put(savedRail.id, area);
					break;
				}
			}
		});
	}

	private static <U extends AreaBase, V extends SavedRailBase> void mapAreaIdToSavedRails(Map<Long, Map<Long, V>> areaIdToSavedRails, Map<BlockPos, List<V>> posToSavedRails, Set<U> areas, Set<V> savedRails) {
		areaIdToSavedRails.clear();
		posToSavedRails.clear();
		areas.forEach(area -> {
			final Map<Long, V> savedRailMap = new HashMap<>();
			savedRails.forEach(savedRail -> {
				final BlockPos pos = savedRail.getMidPos(true);
				if (area.inArea(pos.getX(), pos.getZ())) {
					savedRailMap.put(savedRail.id, savedRail);
				}
				if (!posToSavedRails.containsKey(pos)) {
					posToSavedRails.put(pos, savedRails.stream().filter(savedRail1 -> savedRail1.getMidPos().getX() == pos.getX() && savedRail1.getMidPos().getZ() == pos.getZ()).sorted(Comparator.comparingInt(savedRail1 -> savedRail1.getMidPos().getY())).collect(Collectors.toList()));
				}
			});
			if (!savedRailMap.isEmpty()) {
				areaIdToSavedRails.put(area.id, savedRailMap);
			}
		});
	}

	private static <U extends NameColorDataBase> void mapIds(Map<Long, U> map, Set<U> source) {
		map.clear();
		source.forEach(data -> map.put(data.id, data));
	}

	public static class PlatformRouteDetails {

		public final String routeName;
		public final int routeColor;
		public final Route.CircularState circularState;
		public final int currentStationIndex;
		public final List<StationDetails> stationDetails;

		public PlatformRouteDetails(String routeName, int routeColor, Route.CircularState circularState, int currentStationIndex, List<StationDetails> stationDetails) {
			this.routeName = routeName;
			this.routeColor = routeColor;
			this.circularState = circularState;
			this.currentStationIndex = currentStationIndex;
			this.stationDetails = stationDetails;
		}

		public static class StationDetails {

			public final String stationName;
			public final List<ColorNamePair> interchangeRoutes;

			public StationDetails(String stationName, List<ColorNamePair> interchangeRoutes) {
				this.stationName = stationName;
				this.interchangeRoutes = interchangeRoutes;
			}
		}
	}

	public static class ColorNamePair {

		public final int color;
		public final String name;

		public ColorNamePair(int color, String name) {
			this.color = color;
			this.name = name;
		}
	}
}
