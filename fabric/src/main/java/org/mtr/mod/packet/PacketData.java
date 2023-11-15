package org.mtr.mod.packet;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mtr.core.data.Position;
import org.mtr.core.data.*;
import org.mtr.core.integration.Integration;
import org.mtr.core.integration.Response;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.tool.EnumHelper;
import org.mtr.core.tool.Utilities;
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
	private final Integration integration;

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

	private PacketData(
			IntegrationServlet.Operation operation,
			@Nullable ObjectSet<Station> stations,
			@Nullable ObjectSet<Platform> platforms,
			@Nullable ObjectSet<Siding> sidings,
			@Nullable ObjectSet<Route> routes,
			@Nullable ObjectSet<Depot> depots,
			@Nullable ObjectSet<Rail> rails,
			@Nullable ObjectSet<Position> positions,
			@Nullable ObjectSet<SignalModification> signalModifications
	) {
		this.operation = operation;
		integration = new Integration();
		integration.add(stations, platforms, sidings, routes, depots);
		integration.add(rails, positions);
		integration.add(signalModifications);
	}

	public PacketData(IntegrationServlet.Operation operation, Integration integration) {
		this.operation = operation;
		this.integration = integration;
	}

	public PacketData(PacketBuffer packetBuffer) {
		this(EnumHelper.valueOf(IntegrationServlet.Operation.UPDATE, readString(packetBuffer)), new Integration(new JsonReader(parseJson(readString(packetBuffer)))));
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		writeString(packetBuffer, operation.toString());
		writeString(packetBuffer, Utilities.getJsonObjectFromData(integration).toString());
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		sendHttpRequestAndBroadcastResultToAllPlayers(serverPlayerEntity.getServerWorld());
	}

	@Override
	public void runClientQueued() {
		final Screen screen = MinecraftClient.getInstance().getCurrentScreenMapped();
		if (operation != IntegrationServlet.Operation.LIST || screen == null || !(screen.data instanceof ScreenExtension)) {
			if (integration.hasData()) {
				writeJsonObjectToDataSet(ClientData.instance.stations, integration::iterateStations, NameColorDataBase::getId);
				writeJsonObjectToDataSet(ClientData.instance.platforms, integration::iteratePlatforms, NameColorDataBase::getId);
				writeJsonObjectToDataSet(ClientData.instance.sidings, integration::iterateSidings, NameColorDataBase::getId);
				writeJsonObjectToDataSet(ClientData.instance.routes, integration::iterateRoutes, NameColorDataBase::getId);
				writeJsonObjectToDataSet(ClientData.instance.depots, integration::iterateDepots, NameColorDataBase::getId);
				writeJsonObjectToDataSet(ClientData.instance.rails, integration::iterateRails, Rail::getHexId);
			}

			if (integration.hasVehicle()) {
				final LongAVLTreeSet keepVehicleIds = new LongAVLTreeSet();
				integration.iterateVehiclesToKeep(keepVehicleIds::add);
				ClientData.instance.vehicles.removeIf(vehicle -> !keepVehicleIds.contains(vehicle.getId()));
				integration.iterateVehiclesToUpdate(vehicleUpdate -> ClientData.instance.vehicles.add(new VehicleExtension(vehicleUpdate, ClientData.instance)));
			}

			ClientData.instance.vehicles.forEach(vehicle -> vehicle.vehicleExtraData.immutablePath.forEach(pathData -> pathData.writePathCache(ClientData.instance)));
			ClientData.instance.sync();
		}
	}

	private void sendHttpRequestAndBroadcastResultToAllPlayers(ServerWorld serverWorld) {
		sendHttpDataRequest(operation, integration, newIntegration -> {
			// Check if there are any rail nodes that need to be reset
			integration.iteratePositions(position -> BlockNode.resetRailNode(serverWorld, Init.positionToBlockPos(position)));
			// Broadcast result to all players
			MinecraftServerHelper.iteratePlayers(serverWorld, worldPlayer -> Registry.sendPacketToClient(worldPlayer, new PacketData(operation, newIntegration)));
		});
	}

	private <T extends SerializedDataBase, U> void writeJsonObjectToDataSet(ObjectSet<T> dataSet, Consumer<Consumer<T>> iterator, Function<T, U> getId) {
		final ObjectArraySet<T> newData = new ObjectArraySet<>();
		final ObjectOpenHashSet<U> idList = new ObjectOpenHashSet<>();

		iterator.accept(data -> {
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

	public static void sendHttpRequest(String endpoint, JsonObject contentObject, Consumer<JsonObject> consumer) {
		final HttpPost request = new HttpPost("http://localhost:8888/mtr/api/" + endpoint);
		request.addHeader("content-type", "application/json");

		try {
			request.setEntity(new StringEntity(contentObject.toString()));
		} catch (Exception e) {
			Init.logException(e);
		}

		try (final CloseableHttpClient closeableHttpClient = HttpClients.createDefault(); final CloseableHttpResponse response = closeableHttpClient.execute(request)) {
			final String result = EntityUtils.toString(response.getEntity());
			consumer.accept(parseJson(result));
		} catch (Exception e) {
			Init.logException(e);
		}
	}

	public static JsonObject parseJson(String data) {
		try {
			return JsonParser.parseString(data).getAsJsonObject();
		} catch (Exception ignored) {
			return new JsonObject();
		}
	}

	protected static void sendHttpDataRequest(IntegrationServlet.Operation operation, Integration integration, Consumer<Integration> consumer) {
		sendHttpRequest("data/" + operation.getEndpoint(), Utilities.getJsonObjectFromData(integration), data -> consumer.accept(Response.create(data).getData(Integration::new)));
	}
}
