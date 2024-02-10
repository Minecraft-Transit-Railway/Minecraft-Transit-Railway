package org.mtr.mod.packet;

import org.mtr.core.data.*;
import org.mtr.core.integration.Integration;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.PacketBufferReceiver;

import javax.annotation.Nullable;

public final class PacketData extends PacketDataBase {

	public static PacketData fromStations(IntegrationServlet.Operation operation, ObjectSet<Station> dataSet) {
		return PacketData.create(operation, dataSet, null, null, null, null, null, null, null, null, true);
	}

	public static PacketData fromPlatforms(IntegrationServlet.Operation operation, ObjectSet<Platform> dataSet) {
		return PacketData.create(operation, null, dataSet, null, null, null, null, null, null, null, true);
	}

	public static PacketData fromSidings(IntegrationServlet.Operation operation, ObjectSet<Siding> dataSet) {
		return PacketData.create(operation, null, null, dataSet, null, null, null, null, null, null, true);
	}

	public static PacketData fromRoutes(IntegrationServlet.Operation operation, ObjectSet<Route> dataSet) {
		return PacketData.create(operation, null, null, null, dataSet, null, null, null, null, null, true);
	}

	public static PacketData fromDepots(IntegrationServlet.Operation operation, ObjectSet<Depot> dataSet) {
		return PacketData.create(operation, null, null, null, null, dataSet, null, null, null, null, true);
	}

	public static PacketData fromLifts(IntegrationServlet.Operation operation, ObjectSet<Lift> dataSet) {
		return PacketData.create(operation, null, null, null, null, null, dataSet, null, null, null, true);
	}

	private static PacketData fromRail(IntegrationServlet.Operation operation, Rail rail) {
		return PacketData.create(operation, null, null, null, null, null, null, ObjectSet.of(rail), null, null, false);
	}

	private static PacketData fromRailNode(Position position) {
		return PacketData.create(IntegrationServlet.Operation.DELETE, null, null, null, null, null, null, null, ObjectSet.of(position), null, false);
	}

	private static PacketData fromSignalModification(SignalModification signalModification) {
		return PacketData.create(IntegrationServlet.Operation.UPDATE, null, null, null, null, null, null, null, null, ObjectSet.of(signalModification), false);
	}

	private static PacketData create(
			IntegrationServlet.Operation operation,
			@Nullable ObjectSet<Station> stations,
			@Nullable ObjectSet<Platform> platforms,
			@Nullable ObjectSet<Siding> sidings,
			@Nullable ObjectSet<Route> routes,
			@Nullable ObjectSet<Depot> depots,
			@Nullable ObjectSet<Lift> lifts,
			@Nullable ObjectSet<Rail> rails,
			@Nullable ObjectSet<Position> positions,
			@Nullable ObjectSet<SignalModification> signalModifications,
			boolean updateClientDataDashboardInstance
	) {
		final Integration integration = new Integration(new Data());
		integration.add(stations, platforms, sidings, routes, depots, lifts, null);
		integration.add(rails, positions);
		integration.add(signalModifications);
		return new PacketData(operation, Utilities.getJsonObjectFromData(integration), true, updateClientDataDashboardInstance);
	}

	public PacketData(IntegrationServlet.Operation operation, JsonObject integrationObject, boolean updateClientDataInstance, boolean updateClientDataDashboardInstance) {
		super(operation, integrationObject, updateClientDataInstance, updateClientDataDashboardInstance);
	}

	public static PacketData create(PacketBufferReceiver packetBufferReceiver) {
		return create(packetBufferReceiver, PacketData::new);
	}

	public static void generateLiftPath(ServerWorld serverWorld, ObjectArrayList<LiftFloor> liftFloors) {
		final Lift lift = new Lift(new Data());
		lift.setFloors(liftFloors);
		lift.setDimensions(3, 2, 2, 0, 0, 0);
		fromLifts(IntegrationServlet.Operation.GENERATE, ObjectArraySet.of(lift)).sendHttpRequestAndBroadcastResultToAllPlayers(serverWorld);
	}

	public static void deleteLift(ServerWorld serverWorld, LiftFloor liftFloor) {
		final Lift lift = new Lift(new Data());
		lift.setFloors(ObjectArrayList.of(liftFloor));
		fromLifts(IntegrationServlet.Operation.DELETE, ObjectArraySet.of(lift)).sendHttpRequestAndBroadcastResultToAllPlayers(serverWorld);
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
}
