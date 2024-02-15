package org.mtr.mod.packet;

import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftFloor;
import org.mtr.core.data.Rail;
import org.mtr.core.data.SignalModification;
import org.mtr.core.integration.Response;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.operation.UpdateDataResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mod.client.MinecraftClientData;

import javax.annotation.Nonnull;

public final class PacketUpdateData extends PacketRequestResponseBase {

	public PacketUpdateData(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
	}

	public PacketUpdateData(UpdateDataRequest updateDataRequest) {
		super(Utilities.getJsonObjectFromData(updateDataRequest).toString());
	}

	private PacketUpdateData(String content) {
		super(content);
	}

	@Override
	protected void runClient(Response response) {
		response.getData(jsonReader -> new UpdateDataResponse(jsonReader, MinecraftClientData.getInstance())).write();
		response.getData(jsonReader -> new UpdateDataResponse(jsonReader, MinecraftClientData.getDashboardInstance())).write();
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketUpdateData(content);
	}

	@Nonnull
	@Override
	protected String getEndpoint() {
		return "operation/update-data";
	}

	@Override
	protected PacketRequestResponseBase.ResponseType responseType() {
		return PacketRequestResponseBase.ResponseType.ALL;
	}

	public static void sendDirectlyToServerRail(ServerWorld serverWorld, Rail rail) {
		new PacketUpdateData(new UpdateDataRequest(new MinecraftClientData()).addRail(rail)).runServer(serverWorld, null);
	}

	public static void sendDirectlyToServerSignalModification(ServerWorld serverWorld, SignalModification signalModification) {
		new PacketUpdateData(new UpdateDataRequest(new MinecraftClientData()).addSignalModification(signalModification)).runServer(serverWorld, null);
	}

	public static void sendDirectlyToServerLift(ServerWorld serverWorld, ObjectArrayList<LiftFloor> liftFloors) {
		final Lift lift = new Lift(new MinecraftClientData());
		lift.setFloors(liftFloors);
		lift.setDimensions(3, 2, 2, 0, 0, 0);
		new PacketUpdateData(new UpdateDataRequest(new MinecraftClientData()).addLift(lift)).runServer(serverWorld, null);
	}
}
