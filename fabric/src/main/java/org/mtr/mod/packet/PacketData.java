package org.mtr.mod.packet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.longs.Long2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.objects.*;
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
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.registry.Registry;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.data.VehicleExtension;

import java.util.function.Consumer;
import java.util.function.Function;

public class PacketData extends PacketHandler {

	protected final IntegrationServlet.Operation operation;
	private final JsonObject jsonObject;

	public static PacketData fromStations(IntegrationServlet.Operation operation, ObjectSet<Station> dataSet) {
		return new PacketData(operation, dataSet, null, null, null, null, null);
	}

	public static PacketData fromPlatforms(IntegrationServlet.Operation operation, ObjectSet<Platform> dataSet) {
		return new PacketData(operation, null, dataSet, null, null, null, null);
	}

	public static PacketData fromSidings(IntegrationServlet.Operation operation, ObjectSet<Siding> dataSet) {
		return new PacketData(operation, null, null, dataSet, null, null, null);
	}

	public static PacketData fromRoutes(IntegrationServlet.Operation operation, ObjectSet<Route> dataSet) {
		return new PacketData(operation, null, null, null, dataSet, null, null);
	}

	public static PacketData fromDepots(IntegrationServlet.Operation operation, ObjectSet<Depot> dataSet) {
		return new PacketData(operation, null, null, null, null, dataSet, null);
	}

	private static PacketData fromRailNodes(ObjectSet<RailNode> railNodes) {
		return new PacketData(IntegrationServlet.Operation.UPDATE, null, null, null, null, null, railNodes);
	}

	private PacketData(IntegrationServlet.Operation operation, ObjectSet<Station> stations, ObjectSet<Platform> platforms, ObjectSet<Siding> sidings, ObjectSet<Route> routes, ObjectSet<Depot> depots, ObjectSet<RailNode> railNodes) {
		this.operation = operation;
		jsonObject = new JsonObject();
		jsonObject.add("stations", writeDataSetToJsonArray(stations));
		jsonObject.add("platforms", writeDataSetToJsonArray(platforms));
		jsonObject.add("sidings", writeDataSetToJsonArray(sidings));
		jsonObject.add("routes", writeDataSetToJsonArray(routes));
		jsonObject.add("depots", writeDataSetToJsonArray(depots));
		jsonObject.add("rails", writeDataSetToJsonArray(railNodes));
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
		writeJsonObjectToDataSet(ClientData.instance.stations, jsonObject.getAsJsonArray("stations"), jsonReader -> new Station(jsonReader, ClientData.instance));
		writeJsonObjectToDataSet(ClientData.instance.platforms, jsonObject.getAsJsonArray("platforms"), jsonReader -> new Platform(jsonReader, ClientData.instance));
		writeJsonObjectToDataSet(ClientData.instance.sidings, jsonObject.getAsJsonArray("sidings"), jsonReader -> new Siding(jsonReader, ClientData.instance));
		writeJsonObjectToDataSet(ClientData.instance.routes, jsonObject.getAsJsonArray("routes"), jsonReader -> new Route(jsonReader, ClientData.instance));
		writeJsonObjectToDataSet(ClientData.instance.depots, jsonObject.getAsJsonArray("depots"), jsonReader -> new Depot(jsonReader, ClientData.instance));
		writeJsonObjectToDataSet(ClientData.instance.railNodes, jsonObject.getAsJsonArray("rails"));

		final JsonArray vehicleUpdateArray = jsonObject.getAsJsonArray("update");
		if (vehicleUpdateArray != null) {
			final Long2ObjectAVLTreeMap<ObjectObjectImmutablePair<JsonObject, VehicleExtension>> vehiclesToUpdate = new Long2ObjectAVLTreeMap<>();
			vehicleUpdateArray.forEach(vehicleElement -> {
				final VehicleExtension vehicleExtension = new VehicleExtension(vehicleElement.getAsJsonObject(), ClientData.instance);
				vehiclesToUpdate.put(vehicleExtension.getId(), new ObjectObjectImmutablePair<>(vehicleElement.getAsJsonObject(), vehicleExtension));
			});
			ClientData.instance.vehicles.forEach(checkVehicle -> {
				final ObjectObjectImmutablePair<JsonObject, VehicleExtension> existingVehicleData = vehiclesToUpdate.remove(checkVehicle.getId());
				if (existingVehicleData != null) {
					checkVehicle.updateData(existingVehicleData.left());
					checkVehicle.vehicleExtraData.immutablePath.forEach(pathData -> pathData.init(ClientData.instance));
				}
			});
			vehiclesToUpdate.forEach((vehicleId, vehicleData) -> {
				ClientData.instance.vehicles.add(vehicleData.right());
				vehicleData.right().vehicleExtraData.immutablePath.forEach(pathData -> pathData.init(ClientData.instance));
			});
		}

		final JsonArray vehicleRemoveArray = jsonObject.getAsJsonArray("remove");
		if (vehicleRemoveArray != null) {
			final LongAVLTreeSet vehicleIdsToRemove = new LongAVLTreeSet();
			vehicleRemoveArray.forEach(vehicleIdElement -> vehicleIdsToRemove.add(vehicleIdElement.getAsLong()));
			ClientData.instance.vehicles.removeIf(vehicleExtension -> vehicleIdsToRemove.contains(vehicleExtension.getId()));
		}

		ClientData.instance.sync();
	}

	private void sendHttpRequestAndBroadcastResultToAllPlayers(ServerWorld serverWorld) {
		sendHttpRequest(operation, jsonObject, data -> {
			// Check if there are any rail nodes that need to be reset
			final ObjectAVLTreeSet<Position> allPositions = new ObjectAVLTreeSet<>();
			final ObjectAVLTreeSet<Position> connectedPositions = new ObjectAVLTreeSet<>();
			data.getAsJsonArray("rails").forEach(jsonElement -> {
				final RailNode railNode = new RailNode(new JsonReader(jsonElement));
				final Position position = railNode.getPosition();
				final Object2ObjectOpenHashMap<Position, Rail> connections = railNode.getConnectionsAsMap();
				allPositions.add(position);
				if (!connections.isEmpty()) {
					connectedPositions.add(position);
					connectedPositions.addAll(connections.keySet());
				}
			});
			allPositions.forEach(position -> {
				if (connectedPositions.stream().noneMatch(checkPosition -> checkPosition.equals(position))) {
					BlockNode.resetRailNode(serverWorld, Init.positionToBlockPos(position));
				}
			});

			// Broadcast result to all players
			MinecraftServerHelper.iteratePlayers(serverWorld, worldPlayer -> Registry.sendPacketToClient(worldPlayer, new PacketData(operation, data)));
		});
	}

	private <T extends NameColorDataBase> void writeJsonObjectToDataSet(ObjectAVLTreeSet<T> dataSet, JsonArray jsonArray, Function<JsonReader, T> function) {
		if (jsonArray != null) {
			final ObjectArraySet<T> newData = new ObjectArraySet<>();
			final LongAVLTreeSet idList = new LongAVLTreeSet();

			jsonArray.forEach(jsonElement -> {
				final T data = function.apply(new JsonReader(jsonElement.getAsJsonObject()));
				newData.add(data);
				idList.add(data.getId());
			});

			if (operation == IntegrationServlet.Operation.LIST) {
				dataSet.clear();
			}

			if ((operation == IntegrationServlet.Operation.UPDATE || operation == IntegrationServlet.Operation.DELETE) && !idList.isEmpty()) {
				dataSet.removeIf(data -> idList.contains(data.getId()));
			}

			if (operation == IntegrationServlet.Operation.UPDATE || operation == IntegrationServlet.Operation.LIST) {
				dataSet.addAll(newData);
			}
		}
	}

	private void writeJsonObjectToDataSet(ObjectOpenHashBigSet<RailNode> railNodes, JsonArray jsonArray) {
		if (jsonArray != null) {
			final ObjectAVLTreeSet<Position> positionsToRemove = new ObjectAVLTreeSet<>();
			final ObjectArraySet<RailNode> objectsToAdd = new ObjectArraySet<>();

			// If the rail node coming from the server has no rail connections, delete it, otherwise, update it
			jsonArray.forEach(jsonElement -> {
				final RailNode railNode = new RailNode(new JsonReader(jsonElement.getAsJsonObject()));
				if (!railNode.getConnectionsAsMap().isEmpty()) {
					objectsToAdd.add(railNode);
				}
				positionsToRemove.add(railNode.getPosition());
			});

			railNodes.removeIf(railNode -> positionsToRemove.contains(railNode.getPosition()));
			railNodes.addAll(objectsToAdd);
		}
	}

	public static void createOrDeleteRail(ServerWorld serverWorld, Position positionStart, Position positionEnd, Rail rail1, Rail rail2) {
		final RailNode railNode1 = new RailNode(positionStart);
		railNode1.addConnection(positionEnd, rail1);
		final RailNode railNode2 = new RailNode(positionEnd);
		railNode2.addConnection(positionStart, rail2);

		final ObjectArraySet<RailNode> railNodes = new ObjectArraySet<>();
		railNodes.add(railNode1);
		railNodes.add(railNode2);
		fromRailNodes(railNodes).sendHttpRequestAndBroadcastResultToAllPlayers(serverWorld);
	}

	public static void deleteRailNode(ServerWorld serverWorld, Position position) {
		PacketData.fromRailNodes(ObjectSet.of(new RailNode(position))).sendHttpRequestAndBroadcastResultToAllPlayers(serverWorld);
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
