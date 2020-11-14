package mtr.gui;

import mtr.data.*;

import java.util.HashSet;
import java.util.Set;

public final class ClientData {

	public static Set<Station> stations = new HashSet<>();
	public static Set<Platform> platforms = new HashSet<>();
	public static Set<Route> routes = new HashSet<>();
	public static Set<Train> trains = new HashSet<>();
	public static Set<TrainSpawner> trainSpawners = new HashSet<>();
}
