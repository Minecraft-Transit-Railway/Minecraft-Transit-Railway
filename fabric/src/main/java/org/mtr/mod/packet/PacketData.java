package org.mtr.mod.packet;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mtr.core.data.*;
import org.mtr.core.serializers.JsonReader;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.tools.Position;
import org.mtr.core.tools.Utilities;
import org.mtr.libraries.com.google.gson.JsonArray;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.registry.Registry;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.data.VehicleExtension;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

public class PacketData extends PacketHandler {

	protected final IntegrationServlet.Operation operation;
	private final JsonObject jsonObject;

	public static PacketData fromStations(IntegrationServlet.Operation operation, ObjectSet<Station> dataSet) {
		return new PacketData(operation, dataSet, null, null, null, null, null, null, null);
	}

	public static PacketData fromPlatforms(IntegrationServlet.Operation operation, ObjectSet<Platform> dataSet) {
		return new PacketData(operation, null, dataSet, null, null, null, null, null, null);
	}

	public static PacketData fromSidings(IntegrationServlet.Operation operation, ObjectSet<Siding> dataSet) {
		return new PacketData(operation, null, null, dataSet, null, null, null, null, null);
	}

	public static PacketData fromRoutes(IntegrationServlet.Operation operation, ObjectSet<Route> dataSet) {
		return new PacketData(operation, null, null, null, dataSet, null, null, null, null);
	}

	public static PacketData fromDepots(IntegrationServlet.Operation operation, ObjectSet<Depot> dataSet) {
		return new PacketData(operation, null, null, null, null, dataSet, null, null, null);
	}

	private static PacketData fromRail(IntegrationServlet.Operation operation, Rail rail) {
		return new PacketData(operation, null, null, null, null, null, ObjectSet.of(rail), null, null);
	}

	private static PacketData fromRailNode(Position position) {
		return new PacketData(IntegrationServlet.Operation.DELETE, null, null, null, null, null, null, ObjectSet.of(position), null);
	}

	private static PacketData fromSignalModification(SignalModification signalModification) {
		return new PacketData(IntegrationServlet.Operation.UPDATE, null, null, null, null, null, null, null, ObjectSet.of(signalModification));
	}

	private PacketData(IntegrationServlet.Operation operation, ObjectSet<Station> stations, ObjectSet<Platform> platforms, ObjectSet<Siding> sidings, ObjectSet<Route> routes, ObjectSet<Depot> depots, ObjectSet<Rail> rails, ObjectSet<Position> positions, ObjectSet<SignalModification> signalModifications) {
		this.operation = operation;
		jsonObject = new JsonObject();
		jsonObject.add("stations", writeDataSetToJsonArray(stations));
		jsonObject.add("platforms", writeDataSetToJsonArray(platforms));
		jsonObject.add("sidings", writeDataSetToJsonArray(sidings));
		jsonObject.add("routes", writeDataSetToJsonArray(routes));
		jsonObject.add("depots", writeDataSetToJsonArray(depots));
		jsonObject.add("rails", writeDataSetToJsonArray(rails));
		jsonObject.add("positions", writeDataSetToJsonArray(positions));
		jsonObject.add("signals", writeDataSetToJsonArray(signalModifications));
	}

	public PacketData(IntegrationServlet.Operation operation, JsonObject jsonObject) {
		this.operation = operation;
		this.jsonObject = jsonObject;
	}

	public PacketData(PacketBuffer packetBuffer) {
		this(EnumHelper.valueOf(IntegrationServlet.Operation.UPDATE, readString(packetBuffer)), parseJson(readString(packetBuffer)));
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		writeString(packetBuffer, operation.toString());
		writeString(packetBuffer, jsonObject.toString());
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		sendHttpRequestAndBroadcastResultToAllPlayers(serverPlayerEntity.getServerWorld());
	}

	@Override
	public void runClientQueued() {
		final Screen screen = MinecraftClient.getInstance().getCurrentScreenMapped();
		if (operation != IntegrationServlet.Operation.LIST || screen == null || !(screen.data instanceof ScreenExtension)) {
			writeJsonObjectToDataSet(ClientData.instance.stations, jsonObject.getAsJsonArray("stations"), jsonReader -> new Station(jsonReader, ClientData.instance), NameColorDataBase::getId);
			writeJsonObjectToDataSet(ClientData.instance.platforms, jsonObject.getAsJsonArray("platforms"), jsonReader -> new Platform(jsonReader, ClientData.instance), NameColorDataBase::getId);
			writeJsonObjectToDataSet(ClientData.instance.sidings, jsonObject.getAsJsonArray("sidings"), jsonReader -> new Siding(jsonReader, ClientData.instance), NameColorDataBase::getId);
			writeJsonObjectToDataSet(ClientData.instance.routes, jsonObject.getAsJsonArray("routes"), jsonReader -> new Route(jsonReader, ClientData.instance), NameColorDataBase::getId);
			writeJsonObjectToDataSet(ClientData.instance.depots, jsonObject.getAsJsonArray("depots"), jsonReader -> new Depot(jsonReader, ClientData.instance), NameColorDataBase::getId);
			writeJsonObjectToDataSet(ClientData.instance.rails, jsonObject.getAsJsonArray("rails"), Rail::new, Rail::getHexId);
			writeJsonObjectToDataSet(jsonObject.getAsJsonArray("update"), jsonObject.getAsJsonArray("keep"));
			ClientData.instance.vehicles.forEach(vehicle -> vehicle.vehicleExtraData.immutablePath.forEach(pathData -> pathData.writePathCache(ClientData.instance)));
			ClientData.instance.sync();
		}
	}

	private void sendHttpRequestAndBroadcastResultToAllPlayers(ServerWorld serverWorld) {
		sendHttpRequest(operation, jsonObject, data -> {
			// Check if there are any rail nodes that need to be reset
			final JsonArray positionsArray = data.getAsJsonArray("positions");
			if (positionsArray != null) {
				positionsArray.forEach(jsonElement -> BlockNode.resetRailNode(serverWorld, Init.positionToBlockPos(new Position(new JsonReader(jsonElement)))));
			}

			// Broadcast result to all players
			MinecraftServerHelper.iteratePlayers(serverWorld, worldPlayer -> Registry.sendPacketToClient(worldPlayer, new PacketData(operation, data)));
		});
	}

	private <T extends SerializedDataBase, U> void writeJsonObjectToDataSet(ObjectSet<T> dataSet, @Nullable JsonArray jsonArray, Function<JsonReader, T> createInstance, Function<T, U> getId) {
		if (jsonArray != null) {
			final ObjectArraySet<T> newData = new ObjectArraySet<>();
			final ObjectOpenHashSet<U> idList = new ObjectOpenHashSet<>();

			jsonArray.forEach(jsonElement -> {
				final T data = createInstance.apply(new JsonReader(jsonElement.getAsJsonObject()));
				newData.add(data);
				idList.add(getId.apply(data));
			});

			if (operation == IntegrationServlet.Operation.LIST) {
				dataSet.clear();
			}

			if ((operation == IntegrationServlet.Operation.UPDATE || operation == IntegrationServlet.Operation.DELETE) && !idList.isEmpty()) {
				dataSet.removeIf(data -> idList.contains(getId.apply(data)));
			}

			if (operation == IntegrationServlet.Operation.UPDATE || operation == IntegrationServlet.Operation.LIST) {
				dataSet.addAll(newData);
			}
		}
	}

	private void writeJsonObjectToDataSet(@Nullable JsonArray vehicleUpdateArray, @Nullable JsonArray keepVehicleIdsArray) {
		if (vehicleUpdateArray != null && keepVehicleIdsArray != null) {
			final LongAVLTreeSet keepVehicleIds = new LongAVLTreeSet();
			keepVehicleIdsArray.forEach(vehicleIdElement -> keepVehicleIds.add(vehicleIdElement.getAsLong()));
			ClientData.instance.vehicles.removeIf(vehicle -> !keepVehicleIds.contains(vehicle.getId()));
			vehicleUpdateArray.forEach(vehicleElement -> ClientData.instance.vehicles.add(new VehicleExtension(vehicleElement.getAsJsonObject(), ClientData.instance)));
		}
	}

	public static void updateRail(ServerWorld serverWorld, Rail rail) {
		fromRail(IntegrationServlet.Operation.UPDATE, rail).sendHttpRequestAndBroadcastResultToAllPlayers(serverWorld);
	}

	public static void deleteRail(ServerWorld serverWorld, Rail rail) {
		fromRail(IntegrationServlet.Operation.DELETE, rail).sendHttpRequestAndBroadcastResultToAllPlayers(serverWorld);
	}

	public static void deleteRailNode(ServerWorld serverWorld, Position position) {
		fromRailNode(position).sendHttpRequestAndBroadcastResultToAllPlayers(serverWorld);
	}

	public static void modifySignal(ServerWorld serverWorld, SignalModification signalModification) {
		fromSignalModification(signalModification).sendHttpRequestAndBroadcastResultToAllPlayers(serverWorld);
	}

	protected static void sendHttpRequest(IntegrationServlet.Operation operation, JsonObject contentObject, Consumer<JsonObject> consumer) {
		final HttpPost request = new HttpPost("http://localhost:8888/mtr/api/data/" + operation.getEndpoint());
		request.addHeader("content-type", "application/json");

		try {
			request.setEntity(new StringEntity(contentObject.toString()));
		} catch (Exception e) {
			Init.logException(e);
		}

		try (final CloseableHttpClient closeableHttpClient = HttpClients.createDefault(); final CloseableHttpResponse response = closeableHttpClient.execute(request)) {
			final String result = EntityUtils.toString(response.getEntity());
			consumer.accept(parseJson(result).getAsJsonObject("data"));
		} catch (Exception e) {
			Init.logException(e);
		}
	}

	private static <T extends SerializedDataBase> JsonArray writeDataSetToJsonArray(ObjectSet<T> dataSet) {
		final JsonArray jsonArray = new JsonArray();
		if (dataSet != null) {
			dataSet.forEach(data -> jsonArray.add(Utilities.getJsonObjectFromData(data)));
		}
		return jsonArray;
	}

	private static JsonObject parseJson(String data) {
		try {
			return JsonParser.parseString(data).getAsJsonObject();
		} catch (Exception ignored) {
			return new JsonObject();
		}
	}
}
