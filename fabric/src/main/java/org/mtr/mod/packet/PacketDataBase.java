package org.mtr.mod.packet;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.data.Rail;
import org.mtr.core.integration.Integration;
import org.mtr.core.integration.Response;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.tool.EnumHelper;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
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
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.data.VehicleExtension;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class PacketDataBase extends PacketHandler {

	protected final IntegrationServlet.Operation operation;
	protected final Integration integration;
	private final boolean updateClientDataInstance;
	private final boolean updateClientDataDashboardInstance;

	public PacketDataBase(IntegrationServlet.Operation operation, Integration integration, boolean updateClientDataInstance, boolean updateClientDataDashboardInstance) {
		this.operation = operation;
		this.integration = integration;
		this.updateClientDataInstance = updateClientDataInstance;
		this.updateClientDataDashboardInstance = updateClientDataDashboardInstance;
	}

	public PacketDataBase(PacketBuffer packetBuffer) {
		this(EnumHelper.valueOf(IntegrationServlet.Operation.UPDATE, readString(packetBuffer)), new Integration(new JsonReader(Utilities.parseJson(readString(packetBuffer)))), packetBuffer.readBoolean(), packetBuffer.readBoolean());
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		writeString(packetBuffer, operation.toString());
		writeString(packetBuffer, Utilities.getJsonObjectFromData(integration).toString());
		packetBuffer.writeBoolean(updateClientDataInstance);
		packetBuffer.writeBoolean(updateClientDataDashboardInstance);
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		sendHttpRequestAndBroadcastResultToAllPlayers(serverPlayerEntity.getServerWorld());
	}

	@Override
	public void runClientQueued() {
		if (updateClientDataInstance) {
			updateClientForClientData(ClientData.getInstance());
		}
		if (updateClientDataDashboardInstance) {
			updateClientForClientData(ClientData.getDashboardInstance());
		}
	}

	protected void sendHttpRequestAndBroadcastResultToAllPlayers(ServerWorld serverWorld) {
		sendHttpDataRequest(operation, integration, newIntegration -> {
			// Check if there are any rail nodes that need to be reset
			integration.iteratePositions(position -> BlockNode.resetRailNode(serverWorld, Init.positionToBlockPos(position)));
			// Broadcast result to all players
			MinecraftServerHelper.iteratePlayers(serverWorld, worldPlayer -> Registry.sendPacketToClient(worldPlayer, new PacketData(operation, newIntegration, updateClientDataInstance, updateClientDataDashboardInstance)));
		});
	}

	private void updateClientForClientData(ClientData clientData) {
		final int simplifiedRoutesSize = clientData.simplifiedRoutes.size();

		if (integration.hasData()) {
			writeJsonObjectToDataSet(clientData.stations, integration::iterateStations, NameColorDataBase::getId);
			writeJsonObjectToDataSet(clientData.platforms, integration::iteratePlatforms, NameColorDataBase::getId);
			writeJsonObjectToDataSet(clientData.sidings, integration::iterateSidings, NameColorDataBase::getId);
			writeJsonObjectToDataSet(clientData.routes, integration::iterateRoutes, NameColorDataBase::getId);
			writeJsonObjectToDataSet(clientData.depots, integration::iterateDepots, NameColorDataBase::getId);
			writeJsonObjectToDataSet(clientData.rails, integration::iterateRails, Rail::getHexId);
			clientData.simplifiedRoutes.clear();
			integration.iterateSimplifiedRoutes(clientData.simplifiedRoutes::add);
		}

		if (integration.hasVehicle()) {
			final LongAVLTreeSet keepVehicleIds = new LongAVLTreeSet();
			integration.iterateVehiclesToKeep(keepVehicleIds::add);
			clientData.vehicles.removeIf(vehicle -> !keepVehicleIds.contains(vehicle.getId()));
			integration.iterateVehiclesToUpdate(vehicleUpdate -> clientData.vehicles.add(new VehicleExtension(vehicleUpdate, clientData)));
		}

		clientData.vehicles.forEach(vehicle -> vehicle.vehicleExtraData.immutablePath.forEach(pathData -> pathData.writePathCache(clientData)));

		if (integration.hasData()) {
			clientData.sync();
		}

		// TODO better checking if routes have changed
		if (simplifiedRoutesSize != clientData.simplifiedRoutes.size()) {
			DynamicTextureCache.instance.reload();
		}
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
			consumer.accept(Utilities.parseJson(result));
		} catch (Exception e) {
			Init.logException(e);
		}
	}

	protected static void sendHttpDataRequest(IntegrationServlet.Operation operation, Integration integration, Consumer<Integration> consumer) {
		sendHttpRequest("data/" + operation.getEndpoint(), Utilities.getJsonObjectFromData(integration), data -> consumer.accept(Response.create(data).getData(Integration::new)));
	}
}
