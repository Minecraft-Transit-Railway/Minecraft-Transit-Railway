package org.mtr.mod.packet;

import org.mtr.core.data.TransportMode;
import org.mtr.core.integration.Response;
import org.mtr.core.operation.ListDataResponse;
import org.mtr.core.tool.EnumHelper;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.client.MinecraftClientData;

import javax.annotation.Nonnull;

public final class PacketOpenDashboardScreen extends PacketRequestResponseBase {

	private final TransportMode transportMode;
	private final boolean useTimeAndWindSync;

	public PacketOpenDashboardScreen(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
		transportMode = EnumHelper.valueOf(TransportMode.TRAIN, packetBufferReceiver.readString());
		useTimeAndWindSync = packetBufferReceiver.readBoolean();
	}

	private PacketOpenDashboardScreen(String content, TransportMode transportMode, boolean useTimeAndWindSync) {
		super(content);
		this.transportMode = transportMode;
		this.useTimeAndWindSync = useTimeAndWindSync;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		super.write(packetBufferSender);
		packetBufferSender.writeString(transportMode.toString());
		packetBufferSender.writeBoolean(useTimeAndWindSync);
	}

	@Override
	protected void runClientInbound(Response response) {
		response.getData(jsonReader -> new ListDataResponse(jsonReader, MinecraftClientData.getDashboardInstance())).write();
		ClientPacketHelper.openDashboardScreen(transportMode, useTimeAndWindSync);
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketOpenDashboardScreen(content, transportMode, useTimeAndWindSync);
	}

	@Nonnull
	@Override
	protected String getEndpoint() {
		return "operation/list-data";
	}

	@Override
	protected PacketRequestResponseBase.ResponseType responseType() {
		return PacketRequestResponseBase.ResponseType.PLAYER;
	}

	public static void sendDirectlyToServer(ServerWorld serverWorld, ServerPlayerEntity serverPlayerEntity, TransportMode transportMode, boolean useTimeAndWindSync) {
		new PacketOpenDashboardScreen(new JsonObject().toString(), transportMode, useTimeAndWindSync).runServerOutbound(serverWorld, serverPlayerEntity);
	}
}
