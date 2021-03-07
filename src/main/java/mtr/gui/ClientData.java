package mtr.gui;

import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.data.Route;
import mtr.data.Station;
import mtr.packet.PacketTrainDataBase;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public final class ClientData {

	public static Set<Station> stations = new HashSet<>();
	public static Set<Platform> platforms = new HashSet<>();
	public static Set<Route> routes = new HashSet<>();

	public static Map<Long, Station> platformIdToStation = new HashMap<>();
	public static Map<Long, List<Platform>> platformsInStation = new HashMap<>();
	public static Map<Long, List<ColorNamePair>> routesInStation = new HashMap<>();
	public static Map<Long, String> stationNames = new HashMap<>();
	public static Map<Platform, List<PlatformRouteDetails>> platformToRoute = new HashMap<>();
	public static Map<Long, Set<Route.ScheduleEntry>> schedulesForPlatform = new HashMap<>();

	public static void receivePacket(PacketByteBuf packet) {
		stations = PacketTrainDataBase.deserializeData(packet, Station::new);
		platforms = PacketTrainDataBase.deserializeData(packet, Platform::new);
		routes = PacketTrainDataBase.deserializeData(packet, Route::new);

		platformIdToStation = platforms.stream().map(platform -> new Pair<>(platform.id, RailwayData.getStationByPlatform(stations, platform))).filter(pair -> pair.getRight() != null).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

		platformsInStation.clear();
		platforms.forEach(platform -> {
			final Station station = RailwayData.getStationByPlatform(stations, platform);
			if (station != null) {
				if (!platformsInStation.containsKey(station.id)) {
					platformsInStation.put(station.id, new ArrayList<>());
				}
				platformsInStation.get(station.id).add(platform);
			}
		});
		platformsInStation.forEach((stationId, posList) -> Collections.sort(posList));

		routesInStation.clear();
		schedulesForPlatform.clear();
		routes.forEach(route -> {
			route.platformIds.forEach(platformId -> {
				final Station station = platformIdToStation.get(platformId);
				if (station != null) {
					if (!routesInStation.containsKey(station.id)) {
						routesInStation.put(station.id, new ArrayList<>());
					}
					if (routesInStation.get(station.id).stream().noneMatch(colorNamePair -> route.color == colorNamePair.color)) {
						routesInStation.get(station.id).add(new ColorNamePair(route.color, route.name.split("\\|\\|")[0]));
					}
				}
			});
			route.getTimeOffsets(platforms).forEach((platformId, scheduleEntry) -> {
				if (!schedulesForPlatform.containsKey(platformId)) {
					schedulesForPlatform.put(platformId, new HashSet<>());
				}
				schedulesForPlatform.get(platformId).addAll(scheduleEntry);
			});
		});
		routesInStation.forEach((stationId, routeList) -> routeList.sort(Comparator.comparingInt(a -> a.color)));

		stationNames = stations.stream().collect(Collectors.toMap(station -> station.id, station -> station.name));
		platformToRoute = platforms.stream().collect(Collectors.toMap(platform -> platform, platform -> routes.stream().filter(route -> route.platformIds.contains(platform.id)).map(route -> {
			final List<PlatformRouteDetails.StationDetails> stationDetails = route.platformIds.stream().map(platformId -> {
				final Station station = platformIdToStation.get(platformId);
				if (station == null) {
					return new PlatformRouteDetails.StationDetails("", new ArrayList<>());
				} else {
					return new PlatformRouteDetails.StationDetails(station.name, routesInStation.get(station.id).stream().filter(colorNamePair -> colorNamePair.color != route.color).collect(Collectors.toList()));
				}
			}).collect(Collectors.toList());
			return new PlatformRouteDetails(route.name.split("\\|\\|")[0], route.color, route.platformIds.indexOf(platform.id), stationDetails);
		}).collect(Collectors.toList())));
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
