package mtr.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mtr.data.DataCache;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.data.Station;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DataServletHandler extends HttpServlet {

	public static MinecraftServer SERVER;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		final AsyncContext asyncContext = request.startAsync();

		SERVER.execute(() -> {
			final JsonArray dataArray = new JsonArray();

			SERVER.getWorlds().forEach(world -> {
				final RailwayData railwayData = RailwayData.getInstance(world);
				final JsonArray routesArray = new JsonArray();
				final JsonObject stationPositionsObject = new JsonObject();
				final JsonObject stationsObject = new JsonObject();

				if (railwayData != null) {
					final DataCache dataCache = railwayData.dataCache;

					railwayData.routes.forEach(route -> {
						final JsonObject routeObject = new JsonObject();
						routeObject.addProperty("color", route.color);
						routeObject.addProperty("name", route.name.split("\\|\\|")[0]);
						routeObject.addProperty("type", route.routeType.toString().toLowerCase());
						final JsonArray routeStationsArray = new JsonArray();
						routeObject.add("stations", routeStationsArray);

						route.platformIds.forEach(platformId -> {
							final Station station = dataCache.platformIdToStation.get(platformId);
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
										stationsObject.add(String.valueOf(station.id), stationObject);
									} catch (Exception ignored) {
									}
								}
							}
						});

						routesArray.add(routeObject);
					});
				}

				final JsonObject dataObject = new JsonObject();
				dataObject.add("routes", routesArray);
				dataObject.add("positions", stationPositionsObject);
				dataObject.add("stations", stationsObject);
				dataArray.add(dataObject);
			});

			IServletHandler.sendResponse(response, asyncContext, dataArray.toString());
		});
	}
}
