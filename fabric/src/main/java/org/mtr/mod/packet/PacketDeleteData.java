package org.mtr.mod.packet;

import org.mtr.core.data.Position;
import org.mtr.core.integration.Response;
import org.mtr.core.operation.DeleteDataRequest;
import org.mtr.core.operation.DeleteDataResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mod.client.MinecraftClientData;

import javax.annotation.Nonnull;

public final class PacketDeleteData extends PacketRequestResponseBase {

	public PacketDeleteData(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
	}

	public PacketDeleteData(DeleteDataRequest deleteDataRequest) {
		super(Utilities.getJsonObjectFromData(deleteDataRequest).toString());
	}

	private PacketDeleteData(String content) {
		super(content);
	}

	@Override
	protected void runClient(Response response) {
		final DeleteDataResponse deleteDataResponse = response.getData(DeleteDataResponse::new);
		deleteDataResponse.write(MinecraftClientData.getInstance());
		deleteDataResponse.write(MinecraftClientData.getDashboardInstance());
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketDeleteData(content);
	}

	@Nonnull
	@Override
	protected String getEndpoint() {
		return "operation/delete-data";
	}

	@Override
	protected ResponseType responseType() {
		return ResponseType.ALL;
	}

	public static void sendDirectlyToServerLiftFloorPosition(ServerWorld serverWorld, Position liftFloorPositions) {
		new PacketDeleteData(new DeleteDataRequest().addLiftFloorPosition(liftFloorPositions)).runServer(serverWorld, null);
	}

	public static void sendDirectlyToServerRailNodePosition(ServerWorld serverWorld, Position railNodePosition) {
		new PacketDeleteData(new DeleteDataRequest().addRailNodePosition(railNodePosition)).runServer(serverWorld, null);
	}

	public static void sendDirectlyToServerRailId(ServerWorld serverWorld, String railId) {
		new PacketDeleteData(new DeleteDataRequest().addRailId(railId)).runServer(serverWorld, null);
	}
}
