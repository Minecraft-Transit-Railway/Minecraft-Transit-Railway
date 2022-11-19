package mtr.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mtr.data.RailwayData;
import mtr.data.Route;
import mtr.data.Station;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InfoServletHandler extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		final AsyncContext asyncContext = request.startAsync();

		Webserver.callback.accept(() -> {
			final JsonArray dataArray = new JsonArray();

			Webserver.getWorlds.get().forEach(world -> {
				final RailwayData railwayData = RailwayData.getInstance(world);
				final JsonArray playersArray = new JsonArray();

				if (railwayData != null) {
					world.players().forEach(player -> {
						final JsonObject dataObject = new JsonObject();
						dataObject.addProperty("player", player.getName().getString());

						final String routeName;
						final String routeNumber;
						final String destination;
						final String circular;
						final int color;
						final Route route = railwayData.railwayDataCoolDownModule.getRidingRoute(player);
						if (route == null) {
							routeName = "";
							routeNumber = "";
							destination = "";
							circular = "";
							color = 0;
						} else {
							routeName = route.name;
							routeNumber = route.isLightRailRoute ? route.lightRailRouteNumber : "";
							final Station station = railwayData.dataCache.platformIdToStation.get(route.platformIds.get(route.platformIds.size() - 1));
							destination = station == null ? "" : station.name;
							circular = route.circularState == Route.CircularState.NONE ? "" : route.circularState == Route.CircularState.CLOCKWISE ? "cw" : "ccw";
							color = route.color;
						}
						dataObject.addProperty("name", routeName);
						dataObject.addProperty("number", routeNumber);
						dataObject.addProperty("destination", destination);
						dataObject.addProperty("circular", circular);
						dataObject.addProperty("color", color);

						playersArray.add(dataObject);
					});
				}

				dataArray.add(playersArray);
			});

			IServletHandler.sendResponse(response, asyncContext, dataArray.toString());
		});
	}
}
