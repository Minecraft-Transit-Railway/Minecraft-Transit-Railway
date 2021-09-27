package mtr;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mtr.data.DataCache;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.data.Station;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataServletHandler extends HttpServlet {

	private final MinecraftServer server;

	public DataServletHandler(MinecraftServer server) {
		this.server = server;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		final AsyncContext asyncContext = request.startAsync();

		server.execute(() -> {
			final JsonArray dataArray = new JsonArray();

			server.getWorlds().forEach(world -> {
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
						final JsonArray routeStationsArray = new JsonArray();
						routeObject.add("stations", routeStationsArray);

						route.platformIds.forEach(platformId -> {
							final Station station = dataCache.platformIdToStation.get(platformId);
							if (station != null) {
								final Platform platform = dataCache.platformIdMap.get(platformId);
								if (platform != null) {
									try {
										final String newId = station.id + "_" + route.color;

										final JsonObject platformObject = new JsonObject();
										platformObject.addProperty("name", station.name);
										platformObject.addProperty("position", newId);
										routeStationsArray.add(platformObject);

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

			final ByteBuffer content = ByteBuffer.wrap(dataArray.toString().getBytes(StandardCharsets.UTF_16));

			try {
				final ServletOutputStream servletOutputStream = response.getOutputStream();
				servletOutputStream.setWriteListener(new WriteListener() {
					@Override
					public void onWritePossible() throws IOException {
						while (servletOutputStream.isReady()) {
							if (!content.hasRemaining()) {
								response.setStatus(200);
								asyncContext.complete();
								return;
							}
							servletOutputStream.write(content.get());
						}
					}

					@Override
					public void onError(Throwable t) {
						getServletContext().log("Async Error", t);
						asyncContext.complete();
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
