package mtr.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mtr.data.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RouteFinderServletHandler extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		final AsyncContext asyncContext = request.startAsync();
		asyncContext.setTimeout(-1);

		Webserver.callback.accept(() -> {
			final List<String> errors = new ArrayList<>();
			final String parameterStartPlayer = request.getParameter("startPlayer");
			final String parameterEndPlayer = request.getParameter("endPlayer");
			final String parameterStartStation = request.getParameter("startStation");
			final String parameterEndStation = request.getParameter("endStation");
			final String parameterStartPos = request.getParameter("startPos");
			final String parameterEndPos = request.getParameter("endPos");
			int maxTickTime = 40;
			try {
				maxTickTime = Integer.parseInt(request.getParameter("maxTickTime"));
			} catch (Exception ignored) {
			}
			final Level world = getWorldFromParameter(request.getParameter("dimension"), errors);

			if (world != null) {
				final RailwayData railwayData = RailwayData.getInstance(world);
				if (railwayData != null) {
					final PositionInfo startPositionInfo = getPosition(world, railwayData, parameterStartPlayer, parameterStartStation, parameterStartPos, true, errors);
					final PositionInfo endPositionInfo = getPosition(world, railwayData, parameterEndPlayer, parameterEndStation, parameterEndPos, false, errors);
					if (startPositionInfo != null && endPositionInfo != null && errors.isEmpty()) {
						if (!railwayData.railwayDataRouteFinderModule.findRoute(startPositionInfo.pos, endPositionInfo.pos, maxTickTime, (dataList, duration) -> {
							final JsonObject jsonObject = new JsonObject();
							jsonObject.add("start", startPositionInfo.toJsonObject());
							jsonObject.add("end", endPositionInfo.toJsonObject());
							jsonObject.addProperty("response", duration);

							final JsonArray jsonArrayData = new JsonArray();
							for (final RailwayDataRouteFinderModule.RouteFinderData data : dataList) {
								final PositionInfo positionInfo = new PositionInfo(data.pos, railwayData, null, null, false);
								final JsonObject jsonObjectData = positionInfo.toJsonObject();
								jsonObjectData.addProperty("duration", data.duration);

								final Route route = railwayData.dataCache.routeIdMap.get(data.routeId);
								if (route != null) {
									final JsonObject jsonObjectRoute = new JsonObject();
									jsonObjectRoute.addProperty("wait", data.waitingTime);
									jsonObjectRoute.addProperty("color", route.color);
									jsonObjectRoute.addProperty("name", route.name);
									jsonObjectRoute.addProperty("number", route.isLightRailRoute ? route.lightRailRouteNumber : "");
									jsonObjectRoute.addProperty("type", IServletHandler.createRouteKey(route.transportMode, route.routeType));
									jsonObjectRoute.addProperty("circular", route.circularState == Route.CircularState.NONE ? "" : route.circularState == Route.CircularState.CLOCKWISE ? "cw" : "ccw");
									final JsonArray jsonArrayRouteStations = new JsonArray();
									data.stationIds.forEach(stationId -> jsonArrayRouteStations.add(String.valueOf(stationId)));
									jsonObjectRoute.add("stations", jsonArrayRouteStations);
									jsonObjectData.add("route", jsonObjectRoute);
								}

								jsonArrayData.add(jsonObjectData);

								if (endPositionInfo.isStationParameter && positionInfo.station == endPositionInfo.station) {
									break;
								}
							}

							jsonObject.add("directions", jsonArrayData);
							IServletHandler.sendResponse(response, asyncContext, jsonObject.toString());
						})) {
							errors.add("Too many requests! Please try again.");
						}
					}
				}
			}

			if (!errors.isEmpty()) {
				final JsonObject jsonObject = new JsonObject();
				final JsonArray jsonArray = new JsonArray();
				errors.forEach(jsonArray::add);
				jsonObject.add("errors", jsonArray);
				IServletHandler.sendResponse(response, asyncContext, jsonObject.toString());
			}
		});
	}

	private static Level getWorldFromParameter(String parameterDimension, List<String> errors) {
		if (parameterDimension == null) {
			errors.add("The 'dimension' parameter must be defined.");
		} else {
			final List<Level> worlds = Webserver.getWorlds.get();
			try {
				return worlds.get(Integer.parseInt(parameterDimension));
			} catch (Exception ignored) {
			}
			for (final Level world : worlds) {
				final ResourceLocation dimensionLocation = world.dimension().location();
				if (parameterDimension.equalsIgnoreCase(dimensionLocation.toString()) || parameterDimension.equalsIgnoreCase(dimensionLocation.getPath())) {
					return world;
				}
			}
			if (worlds.size() > 1) {
				errors.add(String.format("The 'dimension' parameter must be a world index (0-%s) or a valid world ID (such as %s).", worlds.size() - 1, worlds.get(0).dimension().location().toString()));
			}
		}

		return null;
	}

	private static PositionInfo getPosition(Level world, RailwayData railwayData, String parameterPlayer, String parameterStation, String parameterPos, boolean isStart, List<String> errors) {
		if (parameterPlayer != null) {
			for (final Player player : world.players()) {
				final String playerName = player.getName().getString();
				if (playerName.equalsIgnoreCase(parameterPlayer)) {
					return new PositionInfo(player.blockPosition(), railwayData, null, playerName, false);
				}
			}
			errors.add(String.format("The player '%s' is not online or in the specified dimension.", parameterPlayer));
		} else if (parameterStation != null) {
			try {
				final Station station = railwayData.dataCache.stationIdMap.get(Long.parseLong(parameterStation));
				return new PositionInfo(station.getCenter(), railwayData, station, null, true);
			} catch (Exception ignored) {
			}
			try {
				for (final Station station : railwayData.stations) {
					final List<String> stationNameSplit = Arrays.asList(station.name.toLowerCase(Locale.ENGLISH).split("\\|"));
					if (Arrays.stream(parameterStation.toLowerCase(Locale.ENGLISH).split("\\|")).allMatch(stationNameSplit::contains)) {
						return new PositionInfo(station.getCenter(), railwayData, station, null, true);
					}
				}
			} catch (Exception ignored) {
			}
			errors.add(String.format("The station '%s' does not exist in the specified dimension.", parameterStation));
		} else if (parameterPos != null) {
			try {
				final String[] coordinates = parameterPos.split(",");
				return new PositionInfo(new BlockPos(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[2])), railwayData, null, null, false);
			} catch (Exception ignored) {
			}
			errors.add(String.format("The block position '%s' is not formatted correctly.", parameterPos));
		} else {
			final String key = isStart ? "start" : "end";
			errors.add(String.format("One of '%sPlayer', '%sStation', or '%sPos' must be defined.", key, key, key));
		}

		return null;
	}

	private static class PositionInfo {

		private final BlockPos pos;
		private final Station station;
		private final String playerName;
		private final Platform platform;
		private final boolean isStationParameter;

		private PositionInfo(BlockPos pos, RailwayData railwayData, Station station, String playerName, boolean isStationParameter) {
			this.pos = pos;
			this.station = station == null ? RailwayData.getStation(railwayData.stations, railwayData.dataCache, pos) : station;
			this.playerName = playerName;
			platform = railwayData.dataCache.platformIdMap.get(RailwayData.getClosePlatformId(railwayData.platforms, railwayData.dataCache, pos));
			this.isStationParameter = isStationParameter;
		}

		private JsonObject toJsonObject() {
			final JsonObject jsonObject = new JsonObject();
			final JsonArray jsonArrayPos = new JsonArray();
			jsonArrayPos.add(pos.getX());
			jsonArrayPos.add(pos.getY());
			jsonArrayPos.add(pos.getZ());
			jsonObject.add("pos", jsonArrayPos);

			if (station != null) {
				final JsonObject jsonObjectStation = new JsonObject();
				jsonObjectStation.addProperty("name", station.name);
				jsonObjectStation.addProperty("id", String.valueOf(station.id));
				jsonObject.add("station", jsonObjectStation);
			}

			if (platform != null) {
				jsonObject.addProperty("platform", platform.name);
			}

			if (playerName != null) {
				jsonObject.addProperty("player", playerName);
			}

			return jsonObject;
		}
	}
}
