package mtr.gui;

import mtr.data.Platform;
import mtr.data.Route;
import mtr.data.Station;
import mtr.data.Train;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public final class ClientData {

	public static Set<Station> stations = new HashSet<>();
	public static Set<Platform> platforms = new HashSet<>();
	public static Set<Route> routes = new HashSet<>();
	public static Set<Train> trains = new HashSet<>();

	public static Map<Long, Station> platformIdToStation = new HashMap<>();
	public static Map<Long, List<BlockPos>> platformPositionsInStation = new HashMap<>();
	public static Map<Long, List<ColorNamePair>> routesInStation = new HashMap<>();
	public static Map<Long, String> stationNames = new HashMap<>();
	public static Map<BlockPos, List<PlatformRouteDetails>> platformToRoute = new HashMap<>();

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
