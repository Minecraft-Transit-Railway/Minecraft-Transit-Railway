package mtr.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.data.Route;
import mtr.data.ScheduleEntry;
import net.minecraft.server.MinecraftServer;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class ArrivalsServletHandler extends HttpServlet {

	public static MinecraftServer SERVER;
	private static final int DEFAULT_MAX_ARRIVALS = 10;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		long stationId = 0;
		long worldIndex = 0;
		int maxArrivals = DEFAULT_MAX_ARRIVALS;
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

		SERVER.execute(() -> {
			final JsonArray dataArray = new JsonArray();

			final int[] worldIndexCounter = {0};
			SERVER.getAllLevels().forEach(world -> {
				if (worldIndexCounter[0] == worldIndexFinal) {
					final RailwayData railwayData = RailwayData.getInstance(world);

					if (railwayData != null) {
						final Map<Long, List<ScheduleEntry>> schedulesForStation = new HashMap<>();
						railwayData.getSchedulesForStation(schedulesForStation, stationIdFinal);

						final List<ScheduleEntry> scheduleEntries = new ArrayList<>();
						schedulesForStation.values().forEach(scheduleEntries::addAll);
						Collections.sort(scheduleEntries);

						for (final ScheduleEntry scheduleEntry : scheduleEntries) {
							if (!scheduleEntry.isTerminating) {
								final JsonObject scheduleObject = new JsonObject();
								scheduleObject.addProperty("arrival", scheduleEntry.arrivalMillis);
								scheduleObject.addProperty("destination", scheduleEntry.destination);
								final Platform platform = railwayData.dataCache.platformIdMap.get(scheduleEntry.platformId);
								scheduleObject.addProperty("platform", platform == null ? "" : platform.name);
								final Route route = railwayData.dataCache.routeIdMap.get(scheduleEntry.routeId);
								scheduleObject.addProperty("color", route == null ? 0 : route.color);
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
