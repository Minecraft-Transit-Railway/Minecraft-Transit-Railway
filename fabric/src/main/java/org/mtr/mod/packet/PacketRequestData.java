package org.mtr.mod.packet;

import org.mtr.core.integration.Response;
import org.mtr.core.operation.DataRequest;
import org.mtr.core.operation.DataResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.QrCodeHelper;
import org.mtr.mod.client.MinecraftClientData;

import javax.annotation.Nonnull;

public final class PacketRequestData extends PacketRequestResponseBase {

	private final String tunnelUrl;

	public PacketRequestData(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
		tunnelUrl = packetBufferReceiver.readString();
	}

	public PacketRequestData(DataRequest dataRequest) {
		super(Utilities.getJsonObjectFromData(dataRequest).toString());
		tunnelUrl = "";
	}

	private PacketRequestData(String content) {
		super(content);
		tunnelUrl = Init.getTunnelUrl();
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		super.write(packetBufferSender);
		packetBufferSender.writeString(tunnelUrl);
	}

	@Override
	protected void runClientInbound(Response response) {
		response.getData(jsonReader -> new DataResponse(jsonReader, MinecraftClientData.getInstance())).write();
		QrCodeHelper.INSTANCE.setServerTunnelUrl(tunnelUrl);
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
