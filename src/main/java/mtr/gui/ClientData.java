package mtr.gui;

import mtr.data.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ClientData {

	public static Set<Station> stations = new HashSet<>();
	public static Set<Platform> platforms = new HashSet<>();
	public static Set<Siding> sidings = new HashSet<>();
	public static Set<Route> routes = new HashSet<>();
	public static Set<Depot> depots = new HashSet<>();
	public static Map<BlockPos, Map<BlockPos, Rail>> rails = new HashMap<>();

	public static Map<Long, Route> routeIdMap = new HashMap<>();
	public static Map<Long, Station> platformIdToStation = new HashMap<>();
	public static Map<Long, Map<Long, Platform>> platformsInStation = new HashMap<>();
	public static Map<Long, Map<Long, Siding>> sidingsInDepot = new HashMap<>();
	public static Map<BlockPos, List<Platform>> platformsWithOffset = new HashMap<>();
	public static Map<BlockPos, List<Siding>> sidingsWithOffset = new HashMap<>();
	public static Map<Long, Map<Integer, ColorNamePair>> routesInStation = new HashMap<>();
	public static Map<Long, String> stationNames = new HashMap<>();
	public static Map<Platform, List<PlatformRouteDetails>> platformToRoute = new HashMap<>();
	public static Map<Long, Set<Route.ScheduleEntry>> schedulesForPlatform = new HashMap<>();

	public static void receivePacket(PacketByteBuf packet) {
		final PacketByteBuf packetCopy = new PacketByteBuf(packet.copy());
		stations = deserializeData(packetCopy, Station::new);
		platforms = deserializeData(packetCopy, Platform::new);
		sidings = deserializeData(packetCopy, Siding::new);
		routes = deserializeData(packetCopy, Route::new);
		depots = deserializeData(packetCopy, Depot::new);

		rails.clear();
		final int railsCount = packetCopy.readInt();
		for (int i = 0; i < railsCount; i++) {
			final BlockPos startPos = packetCopy.readBlockPos();
			final Map<BlockPos, Rail> railMap = new HashMap<>();
			final int railCount = packetCopy.readInt();
			for (int j = 0; j < railCount; j++) {
				railMap.put(packetCopy.readBlockPos(), new Rail(packetCopy));
			}
			rails.put(startPos, railMap);
		}

		updateReferences();
		sidings.forEach(siding -> siding.generateRoute(rails, platforms, routes, depots));
	}

	public static void updateReferences() {
		try {
			routeIdMap = routes.stream().collect(Collectors.toMap(route -> route.id, route -> route));
			platformIdToStation = platforms.stream().map(platform -> new Pair<>(platform.id, RailwayData.getAreaBySavedRail(stations, platform))).filter(pair -> pair.getRight() != null).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

			writeSavedRailMaps(stations, platforms, platformsWithOffset, platformsInStation);
			writeSavedRailMaps(depots, sidings, sidingsWithOffset, sidingsInDepot);

			routesInStation.clear();
			schedulesForPlatform.clear();
			routes.forEach(route -> {
				route.platformIds.forEach(platformId -> {
					final Station station = platformIdToStation.get(platformId);
					if (station != null) {
						if (!routesInStation.containsKey(station.id)) {
							routesInStation.put(station.id, new HashMap<>());
						}
						routesInStation.get(station.id).put(route.color, new ColorNamePair(route.color, route.name.split("\\|\\|")[0]));
					}
				});
				route.getTimeOffsets(platforms).forEach((platformId, scheduleEntry) -> {
					if (!schedulesForPlatform.containsKey(platformId)) {
						schedulesForPlatform.put(platformId, new HashSet<>());
					}
					schedulesForPlatform.get(platformId).addAll(scheduleEntry);
				});
			});

			stationNames = stations.stream().collect(Collectors.toMap(station -> station.id, station -> station.name));
			platformToRoute = platforms.stream().collect(Collectors.toMap(platform -> platform, platform -> routes.stream().filter(route -> route.platformIds.contains(platform.id)).map(route -> {
				final List<PlatformRouteDetails.StationDetails> stationDetails = route.platformIds.stream().map(platformId -> {
					final Station station = platformIdToStation.get(platformId);
					if (station == null) {
						return new PlatformRouteDetails.StationDetails("", new ArrayList<>());
					} else {
						return new PlatformRouteDetails.StationDetails(station.name, routesInStation.get(station.id).values().stream().filter(colorNamePair -> colorNamePair.color != route.color).collect(Collectors.toList()));
					}
				}).collect(Collectors.toList());
				return new PlatformRouteDetails(route.name.split("\\|\\|")[0], route.color, route.platformIds.indexOf(platform.id), stationDetails);
			}).collect(Collectors.toList())));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Station getStation(BlockPos pos) {
		try {
			return ClientData.stations.stream().filter(station -> station.inArea(pos.getX(), pos.getZ())).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Platform getClosePlatform(BlockPos pos) {
		try {
			return ClientData.platforms.stream().filter(platform -> platform.isCloseToSavedRail(pos)).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static <T extends SerializedDataBase> Set<T> deserializeData(PacketByteBuf packet, Function<PacketByteBuf, T> supplier) {
		final Set<T> objects = new HashSet<>();
		final int dataCount = packet.readInt();
		for (int i = 0; i < dataCount; i++) {
			objects.add(supplier.apply(packet));
		}
		return objects;
	}

	private static <T extends AreaBase, U extends SavedRailBase> void writeSavedRailMaps(Set<T> areas, Set<U> savedRails, Map<BlockPos, List<U>> savedRailsWithOffset, Map<Long, Map<Long, U>> savedRailsInAreaMap) {
		savedRailsInAreaMap.clear();
		savedRails.forEach(savedRail -> {
			final T area = RailwayData.getAreaBySavedRail(areas, savedRail);
			if (area != null) {
				if (!savedRailsInAreaMap.containsKey(area.id)) {
					savedRailsInAreaMap.put(area.id, new HashMap<>());
				}
				savedRailsInAreaMap.get(area.id).put(savedRail.id, savedRail);
			}

			final BlockPos midPos = savedRail.getMidPos(true);
			if (!savedRailsWithOffset.containsKey(midPos)) {
				savedRailsWithOffset.put(midPos, savedRails.stream().filter(savedRail1 -> savedRail1.getMidPos().getX() == midPos.getX() && savedRail1.getMidPos().getZ() == midPos.getZ()).sorted(Comparator.comparingInt(savedRail1 -> savedRail1.getMidPos().getY())).collect(Collectors.toList()));
			}
		});
	}

	public static class PlatformRouteDetails {

		public final String routeName;
		public final int routeColor;
		public final int currentStationIndex;
		public final List<StationDetails> stationDetails;

		public PlatformRouteDetails(String routeName, int routeColor, int currentStationIndex, List<StationDetails> stationDetails) {
			this.routeName = routeName;
			this.routeColor = routeColor;
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
