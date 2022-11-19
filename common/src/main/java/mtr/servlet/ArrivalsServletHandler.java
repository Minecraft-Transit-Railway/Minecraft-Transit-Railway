package mtr.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mtr.data.*;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class ArrivalsServletHandler extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		long stationId = 0;
		long worldIndex = 0;
		try {
			stationId = Long.parseLong(request.getParameter("stationId"));
		} catch (Exception ignored) {
		}
		try {
			worldIndex = Integer.parseInt(request.getParameter("worldIndex"));
		} catch (Exception ignored) {
		}

		final AsyncContext asyncContext = request.startAsync();
		final long stationIdFinal = stationId;
		final long worldIndexFinal = worldIndex;

		Webserver.callback.accept(() -> {
			final JsonArray dataArray = new JsonArray();

			final int[] worldIndexCounter = {0};
			Webserver.getWorlds.get().forEach(world -> {
				if (worldIndexCounter[0] == worldIndexFinal) {
					final RailwayData railwayData = RailwayData.getInstance(world);

					if (railwayData != null) {
						final Map<Long, List<ScheduleEntry>> schedulesForStation = new HashMap<>();
						railwayData.getSchedulesForStation(schedulesForStation, stationIdFinal);

						final List<ScheduleEntry> scheduleEntries = new ArrayList<>();
						schedulesForStation.values().forEach(scheduleEntries::addAll);
						Collections.sort(scheduleEntries);

						for (final ScheduleEntry scheduleEntry : scheduleEntries) {
							final DataCache dataCache = railwayData.dataCache;
							final Route route = dataCache.routeIdMap.get(scheduleEntry.routeId);
							if (route != null && scheduleEntry.currentStationIndex < route.platformIds.size() - 1) {
								final JsonObject scheduleObject = new JsonObject();
								scheduleObject.addProperty("arrival", scheduleEntry.arrivalMillis);
								scheduleObject.addProperty("name", route.name);
								final Station station = railwayData.dataCache.platformIdToStation.get(route.platformIds.get(route.platformIds.size() - 1));
								scheduleObject.addProperty("destination", station == null ? "" : station.name);
								scheduleObject.addProperty("circular", route.circularState == Route.CircularState.NONE ? "" : route.circularState == Route.CircularState.CLOCKWISE ? "cw" : "ccw");
								scheduleObject.addProperty("route", route.isLightRailRoute ? route.lightRailRouteNumber : "");
								final Platform platform = railwayData.dataCache.platformIdMap.get(route.platformIds.get(scheduleEntry.currentStationIndex));
								scheduleObject.addProperty("platform", platform == null ? "" : platform.name);
								scheduleObject.addProperty("color", route.color);
								dataArray.add(scheduleObject);
							}
						}
					}
				}

				worldIndexCounter[0]++;
			});

			IServletHandler.sendResponse(response, asyncContext, dataArray.toString());
		});
	}
}
