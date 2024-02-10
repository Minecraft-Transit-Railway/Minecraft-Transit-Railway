package org.mtr.mod.packet;

import org.mtr.core.data.Data;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.data.Rail;
import org.mtr.core.integration.Integration;
import org.mtr.core.integration.Response;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.servlet.Webserver;
import org.mtr.core.tool.EnumHelper;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.*;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.VehicleRidingMovement;
import org.mtr.mod.data.VehicleExtension;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongConsumer;
import java.util.function.ToLongFunction;

public abstract class PacketDataBase extends PacketHandler {

	protected final IntegrationServlet.Operation operation;
	protected final JsonObject integrationObject;
	private final boolean updateClientDataInstance;
	private final boolean updateClientDataDashboardInstance;

	public PacketDataBase(IntegrationServlet.Operation operation, JsonObject integrationObject, boolean updateClientDataInstance, boolean updateClientDataDashboardInstance) {
		this.operation = operation;
		this.integrationObject = integrationObject;
		this.updateClientDataInstance = updateClientDataInstance;
		this.updateClientDataDashboardInstance = updateClientDataDashboardInstance;
	}

	protected static <T extends PacketDataBase> T create(PacketBufferReceiver packetBufferReceiver, PacketDataBaseInstance<T> packetDataBaseInstance) {
		final IntegrationServlet.Operation operation = EnumHelper.valueOf(IntegrationServlet.Operation.UPDATE, packetBufferReceiver.readString());
		final JsonObject integrationObject = Utilities.parseJson(packetBufferReceiver.readString());
		final boolean updateClientDataInstance = packetBufferReceiver.readBoolean();
		final boolean updateClientDataDashboardInstance = packetBufferReceiver.readBoolean();
		return packetDataBaseInstance.create(
				operation,
				integrationObject,
				updateClientDataInstance,
				updateClientDataDashboardInstance
		);
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeString(operation.toString());
		packetBufferSender.writeString(integrationObject.toString());
		packetBufferSender.writeBoolean(updateClientDataInstance);
		packetBufferSender.writeBoolean(updateClientDataDashboardInstance);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		sendHttpRequestAndBroadcastResultToAllPlayers(serverPlayerEntity.getServerWorld());
	}

	@Override
	public void runClient() {
		if (updateClientDataInstance) {
			updateClientForClientData(ClientData.getInstance());
		}
		if (updateClientDataDashboardInstance) {
			updateClientForClientData(ClientData.getDashboardInstance());
		}
	}

	protected void sendHttpRequestAndBroadcastResultToAllPlayers(ServerWorld serverWorld) {
		sendHttpDataRequest(operation, integrationObject, newIntegrationObject -> {
			// Check if there are any rail nodes that need to be reset
			new Integration(new JsonReader(newIntegrationObject), new Data()).iterateRailNodePositions(railNodePosition -> BlockNode.resetRailNode(serverWorld, Init.positionToBlockPos(railNodePosition)));
			// Broadcast result to all players
			MinecraftServerHelper.iteratePlayers(serverWorld, worldPlayer -> Init.REGISTRY.sendPacketToClient(worldPlayer, new PacketData(operation, newIntegrationObject, updateClientDataInstance, updateClientDataDashboardInstance)));
		});
	}

	private void updateClientForClientData(ClientData clientData) {
		final Integration integration = new Integration(new JsonReader(integrationObject), clientData);
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

		if (integration.hasVehicleOrLift()) {
			updateVehicles(clientData.vehicles, integration::iterateVehiclesToKeep, integration::iterateVehiclesToUpdate, vehicleUpdate -> vehicleUpdate.getVehicle().getId(), vehicleUpdate -> new VehicleExtension(vehicleUpdate, clientData));
			updateVehicles(clientData.lifts, integration::iterateLiftsToKeep, integration::iterateLiftsToUpdate, NameColorDataBase::getId, lift -> lift);
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

	public static void sendHttpRequest(String endpoint, JsonObject contentObject, @Nullable Consumer<JsonObject> consumer) {
		Webserver.sendPostRequest(String.format("http://localhost:%s/mtr/api/%s", Init.getServerPort(), endpoint), contentObject, consumer);
	}

	protected static void sendHttpDataRequest(IntegrationServlet.Operation operation, JsonObject integrationObject, Consumer<JsonObject> consumer) {
		sendHttpRequest("data/" + operation.getEndpoint(), integrationObject, data -> consumer.accept(Response.create(data).data));
	}

	private static <T extends NameColorDataBase, U> void updateVehicles(ObjectAVLTreeSet<T> dataSet, Consumer<LongConsumer> iterateKeep, Consumer<Consumer<U>> iterateUpdate, ToLongFunction<U> getId, Function<U, T> createInstance) {
		final LongAVLTreeSet keepIds = new LongAVLTreeSet();
		iterateKeep.accept(keepIds::add);
		VehicleRidingMovement.writeVehicleId(keepIds);

		final LongAVLTreeSet updateIds = new LongAVLTreeSet();
		final ObjectArrayList<U> dataSetToUpdate = new ObjectArrayList<>();
		iterateUpdate.accept(dataToUpdate -> {
			dataSetToUpdate.add(dataToUpdate);
			updateIds.add(getId.applyAsLong(dataToUpdate));
		});

		dataSet.removeIf(data -> !keepIds.contains(data.getId()) || updateIds.contains(data.getId()));
		dataSetToUpdate.forEach(dataToUpdate -> dataSet.add(createInstance.apply(dataToUpdate)));
	}

	@FunctionalInterface
	protected interface PacketDataBaseInstance<T extends PacketDataBase> {
		T create(IntegrationServlet.Operation operation, JsonObject integrationObject, boolean updateClientDataInstance, boolean updateClientDataDashboardInstance);
	}
}
