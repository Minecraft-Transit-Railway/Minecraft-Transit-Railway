package mtr.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mtr.data.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

public class DataServletHandler extends HttpServlet {

	public static MinecraftServer SERVER;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		final AsyncContext asyncContext = request.startAsync();

		SERVER.execute(() -> {
			final JsonArray dataArray = new JsonArray();

			SERVER.getAllLevels().forEach(world -> {
				final RailwayData railwayData = RailwayData.getInstance(world);
				final JsonArray routesArray = new JsonArray();
				final JsonObject stationPositionsObject = new JsonObject();
				final JsonObject stationsObject = new JsonObject();
				final JsonArray typesObject = new JsonArray();
				final Set<String> types = new HashSet<>();

				if (railwayData != null) {
					final DataCache dataCache = railwayData.dataCache;

					railwayData.routes.forEach(route -> {
						if (route.isHidden) {
							return;
						}

						final JsonObject routeObject = new JsonObject();
						routeObject.addProperty("color", route.color);
						routeObject.addProperty("name", route.name);
						routeObject.addProperty("number", route.isLightRailRoute ? route.lightRailRouteNumber : "");
						final String type = createKey(route.transportMode, route.routeType);
						routeObject.addProperty("type", type);
						types.add(type);
						final JsonArray routeStationsArray = new JsonArray();
						routeObject.add("stations", routeStationsArray);
						final JsonArray routeDurationsArray = new JsonArray();
						routeObject.add("durations", routeDurationsArray);
						routeObject.addProperty("circular", route.circularState == Route.CircularState.NONE ? "" : route.circularState == Route.CircularState.CLOCKWISE ? "cw" : "ccw");

						final Depot depot = dataCache.routeIdToOneDepot.get(route.id);
						float accumulatedTime = 0;
						for (int i = 0; i < route.platformIds.size(); i++) {
							final long platformId = route.platformIds.get(i);

							float time = 0;
							if (i > 0) {
								if (depot != null) {
									final long prevPlatformId = route.platformIds.get(i - 1);
									if (depot.platformTimes.containsKey(prevPlatformId) && depot.platformTimes.get(prevPlatformId).containsKey(platformId)) {
										time = depot.platformTimes.get(prevPlatformId).get(platformId);
									}
								}
							}

							final Station station = dataCache.platformIdToStation.get(platformId);
							boolean addedStation = false;
							if (station != null) {
								final Platform platform = dataCache.platformIdMap.get(platformId);
								if (platform != null) {
									try {
										final String newId = station.id + "_" + route.color;
										routeStationsArray.add(newId);

										final BlockPos pos = platform.getMidPos();
										if (stationPositionsObject.has(newId)) {
											final JsonObject stationPositionObject = stationPositionsObject.getAsJsonObject(newId);
											final int existingX = stationPositionObject.get("x").getAsInt();
											final int existingZ = stationPositionObject.get("y").getAsInt();
											stationPositionObject.addProperty("x", (existingX + pos.getX()) / 2);
											stationPositionObject.addProperty("y", (existingZ + pos.getZ()) / 2);
										} else {
											final JsonObject stationPositionObject = new JsonObject();
											stationPositionObject.addProperty("x", pos.getX());
											stationPositionObject.addProperty("y", pos.getZ());
											stationPositionObject.addProperty("vertical", platform.getAxis() == Direction.Axis.Z);
											stationPositionsObject.add(newId, stationPositionObject);
										}

										final JsonObject stationObject = new JsonObject();
										stationObject.addProperty("name", station.name);
										stationObject.addProperty("color", station.color);
										stationObject.addProperty("zone", station.zone);
										final BlockPos stationCenter = station.getCenter();
										stationObject.addProperty("x", stationCenter == null ? 0 : stationCenter.getX());
										stationObject.addProperty("z", stationCenter == null ? 0 : stationCenter.getZ());
										stationsObject.add(String.valueOf(station.id), stationObject);

										addedStation = true;
									} catch (Exception ignored) {
									}
								}
							}

							accumulatedTime += time;
							if (i > 0 && addedStation) {
								routeDurationsArray.add(accumulatedTime);
								accumulatedTime = 0;
							}
						}

						routesArray.add(routeObject);
					});
				}

				for (final TransportMode transportMode : TransportMode.values()) {
					for (final RouteType routeType : RouteType.values()) {
						final String type = createKey(transportMode, routeType);
						if (types.contains(type)) {
							typesObject.add(type);
						}
					}
				}

				final JsonObject dataObject = new JsonObject();
				dataObject.add("routes", routesArray);
				dataObject.add("positions", stationPositionsObject);
				dataObject.add("stations", stationsObject);
				dataObject.add("types", typesObject);
				dataArray.add(dataObject);
			});

			IServletHandler.sendResponse(response, asyncContext, dataArray.toString());
		});
	}

	private static String createKey(TransportMode transportMode, RouteType routeType) {
		return (transportMode.toString() + "_" + routeType.toString()).toLowerCase();
	}
}
