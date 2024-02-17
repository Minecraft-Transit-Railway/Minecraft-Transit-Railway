package org.mtr.mod.packet;

import org.mtr.core.integration.Response;
import org.mtr.core.operation.DataRequest;
import org.mtr.core.operation.DataResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mod.client.MinecraftClientData;

import javax.annotation.Nonnull;

public final class PacketRequestData extends PacketRequestResponseBase {

	public PacketRequestData(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
	}

	public PacketRequestData(DataRequest dataRequest) {
		super(Utilities.getJsonObjectFromData(dataRequest).toString());
	}

	private PacketRequestData(String content) {
		super(content);
	}

	@Override
	protected void runClient(Response response) {
		response.getData(jsonReader -> new DataResponse(jsonReader, MinecraftClientData.getInstance())).write();
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketRequestData(content);
	}

	@Nonnull
	@Override
	protected String getEndpoint() {
		return "operation/get-data";
	}

	@Override
	protected PacketRequestResponseBase.ResponseType responseType() {
		return PacketRequestResponseBase.ResponseType.PLAYER;
	}
}
