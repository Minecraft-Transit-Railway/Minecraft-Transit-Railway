package org.mtr.mod.packet;

import org.mtr.core.data.Position;
import org.mtr.core.operation.DeleteDataRequest;
import org.mtr.core.operation.DeleteDataResponse;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockNode;
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
	protected void runServerInbound(ServerWorld serverWorld, JsonObject jsonObject) {
		// Check if there are any rail nodes that need to be reset
		new DeleteDataResponse(new JsonReader(jsonObject)).iterateRailNodePosition(railNodePosition -> BlockNode.resetRailNode(serverWorld, Init.positionToBlockPos(railNodePosition)));
	}

	@Override
	protected void runClientInbound(JsonReader jsonReader) {
		final DeleteDataResponse deleteDataResponse = new DeleteDataResponse(jsonReader);
		deleteDataResponse.write(MinecraftClientData.getInstance());
		deleteDataResponse.write(MinecraftClientData.getDashboardInstance());
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketDeleteData(content);
	}

	@Override
	protected SerializedDataBase getDataInstance(JsonReader jsonReader) {
		return new DeleteDataRequest(jsonReader);
	}

	@Nonnull
	@Override
	protected String getKey() {
		return OperationProcessor.DELETE_DATA;
	}

	@Override
	protected ResponseType responseType() {
		return ResponseType.ALL;
	}

	public static void sendDirectlyToServerLiftFloorPosition(ServerWorld serverWorld, Position liftFloorPositions) {
		new PacketDeleteData(new DeleteDataRequest().addLiftFloorPosition(liftFloorPositions)).runServerOutbound(serverWorld, null);
	}

	public static void sendDirectlyToServerRailNodePosition(ServerWorld serverWorld, Position railNodePosition) {
		new PacketDeleteData(new DeleteDataRequest().addRailNodePosition(railNodePosition)).runServerOutbound(serverWorld, null);
	}

	public static void sendDirectlyToServerRailId(ServerWorld serverWorld, String railId) {
		new PacketDeleteData(new DeleteDataRequest().addRailId(railId)).runServerOutbound(serverWorld, null);
	}
}
