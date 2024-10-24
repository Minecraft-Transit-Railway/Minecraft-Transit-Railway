package org.mtr.mod.packet;

import org.mtr.core.operation.DataRequest;
import org.mtr.core.operation.DataResponse;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.servlet.OperationProcessor;
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
	protected void runClientInbound(JsonReader jsonReader) {
		new DataResponse(jsonReader, MinecraftClientData.getInstance()).write();
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketRequestData(content);
	}

	@Override
	protected SerializedDataBase getDataInstance(JsonReader jsonReader) {
		return new DataRequest(jsonReader);
	}

	@Nonnull
	@Override
	protected String getKey() {
		return OperationProcessor.GET_DATA;
	}

	@Override
	protected PacketRequestResponseBase.ResponseType responseType() {
		return PacketRequestResponseBase.ResponseType.PLAYER;
	}
}
