package mtr.gui;

import mtr.data.Platform;
import mtr.data.Route;
import mtr.data.Station;
import mtr.data.Train;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

public final class ClientData {

	public static Set<Station> stations = new HashSet<>();
	public static Set<Platform> platforms = new HashSet<>();
	public static Set<Route> routes = new HashSet<>();
	public static Set<Train> trains = new HashSet<>();

	public static Map<Long, String> stationNames = new HashMap<>();
	public static Map<BlockPos, List<Triple<Integer, Integer, List<String>>>> platformToRoute = new HashMap<>();
}
