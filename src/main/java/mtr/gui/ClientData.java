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

	public static Map<Long, String> stationNames = new HashMap<>();
	public static Map<BlockPos, List<PlatformRouteDetails>> platformToRoute = new HashMap<>();

	public static class PlatformRouteDetails {

		public final int routeColor;
		public final int currentStationIndex;
		public final List<String> stationNames;

		public PlatformRouteDetails(int routeColor, int currentStationIndex, List<String> stationNames) {
			this.routeColor = routeColor;
			this.currentStationIndex = currentStationIndex;
			this.stationNames = stationNames;
		}
	}
}
