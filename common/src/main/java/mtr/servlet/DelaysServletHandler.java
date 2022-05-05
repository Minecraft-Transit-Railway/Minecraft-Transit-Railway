package mtr.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mtr.data.DataCache;
import mtr.data.RailwayData;
import mtr.data.Route;
import mtr.data.Station;
import net.minecraft.server.MinecraftServer;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DelaysServletHandler extends HttpServlet {

	public static MinecraftServer SERVER;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		final AsyncContext asyncContext = request.startAsync();

		SERVER.execute(() -> {
			final JsonArray dataArray = new JsonArray();

			SERVER.getAllLevels().forEach(world -> {
				final RailwayData railwayData = RailwayData.getInstance(world);
				final JsonArray delayArray = new JsonArray();

				if (railwayData != null) {
					final DataCache dataCache = railwayData.dataCache;

					railwayData.getTrainDelays().forEach((routeId, trainDelaysForRoute) -> trainDelaysForRoute.forEach((pos, trainDelay) -> {
						final String routeName;
						final String destination;
						final int color;
						final Route route = dataCache.routeIdMap.get(routeId);
						if (route == null) {
							routeName = "";
							destination = "";
							color = 0;
						} else {
							routeName = route.name;
							final Station station = dataCache.platformIdToStation.get(route.platformIds.get(route.platformIds.size() - 1));
							destination = station == null ? "" : station.name;
							color = route.color;
						}

						final JsonObject delayObject = new JsonObject();
						delayObject.addProperty("name", routeName);
						delayObject.addProperty("destination", destination);
						delayObject.addProperty("color", color);
						delayObject.addProperty("delay", trainDelay.getDelayTicks());
						delayObject.addProperty("time", trainDelay.getLastDelayTime());
						delayObject.addProperty("x", pos.getX());
						delayObject.addProperty("y", pos.getY());
						delayObject.addProperty("z", pos.getZ());
						delayArray.add(delayObject);
					}));
				}

				dataArray.add(delayArray);
			});

			IServletHandler.sendResponse(response, asyncContext, dataArray.toString());
		});
	}
}
