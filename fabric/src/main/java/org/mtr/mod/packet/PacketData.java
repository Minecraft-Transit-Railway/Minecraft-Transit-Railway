package org.mtr.mod.packet;

import org.mtr.core.data.*;
import org.mtr.core.integration.Integration;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.ServerWorld;

import javax.annotation.Nullable;

public final class PacketData extends PacketDataBase {

	public static PacketData fromStations(IntegrationServlet.Operation operation, ObjectSet<Station> dataSet) {
		return PacketData.create(operation, dataSet, null, null, null, null, null, null, null, true, true);
	}

	public static PacketData fromPlatforms(IntegrationServlet.Operation operation, ObjectSet<Platform> dataSet) {
		return PacketData.create(operation, null, dataSet, null, null, null, null, null, null, true, true);
	}

	public static PacketData fromSidings(IntegrationServlet.Operation operation, ObjectSet<Siding> dataSet) {
		return PacketData.create(operation, null, null, dataSet, null, null, null, null, null, true, true);
	}

	public static PacketData fromRoutes(IntegrationServlet.Operation operation, ObjectSet<Route> dataSet) {
		return PacketData.create(operation, null, null, null, dataSet, null, null, null, null, true, true);
	}

	public static PacketData fromDepots(IntegrationServlet.Operation operation, ObjectSet<Depot> dataSet) {
		return PacketData.create(operation, null, null, null, null, dataSet, null, null, null, true, true);
	}

	private static PacketData fromRail(IntegrationServlet.Operation operation, Rail rail) {
		return PacketData.create(operation, null, null, null, null, null, ObjectSet.of(rail), null, null, true, false);
	}

	private static PacketData fromRailNode(Position position) {
		return PacketData.create(IntegrationServlet.Operation.DELETE, null, null, null, null, null, null, ObjectSet.of(position), null, true, false);
	}

	private static PacketData fromSignalModification(SignalModification signalModification) {
		return PacketData.create(IntegrationServlet.Operation.UPDATE, null, null, null, null, null, null, null, ObjectSet.of(signalModification), true, false);
	}

	private static PacketData create(
			IntegrationServlet.Operation operation,
			@Nullable ObjectSet<Station> stations,
			@Nullable ObjectSet<Platform> platforms,
			@Nullable ObjectSet<Siding> sidings,
			@Nullable ObjectSet<Route> routes,
			@Nullable ObjectSet<Depot> depots,
			@Nullable ObjectSet<Rail> rails,
			@Nullable ObjectSet<Position> positions,
			@Nullable ObjectSet<SignalModification> signalModifications,
			boolean updateClientDataInstance,
			boolean updateClientDataDashboardInstance
	) {
		final Integration integration = new Integration();
		integration.add(stations, platforms, sidings, routes, depots, null);
		integration.add(rails, positions);
		integration.add(signalModifications);
		return new PacketData(operation, integration, updateClientDataInstance, updateClientDataDashboardInstance);
	}

	public PacketData(IntegrationServlet.Operation operation, Integration integration, boolean updateClientDataInstance, boolean updateClientDataDashboardInstance) {
		super(operation, integration, updateClientDataInstance, updateClientDataDashboardInstance);
	}

	public PacketData(PacketBuffer packetBuffer) {
		super(packetBuffer);
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
