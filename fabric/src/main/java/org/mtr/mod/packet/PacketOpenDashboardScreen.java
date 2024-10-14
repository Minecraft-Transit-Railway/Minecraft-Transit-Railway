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
	public enum ScreenType {
		DEFAULT,
		STATION,
		DEPOT,
		PLATFORM,
		SIDING;
	}

	private final TransportMode transportMode;
	private final ScreenType screen;
	private final long id;

	public PacketOpenDashboardScreen(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
		transportMode = EnumHelper.valueOf(TransportMode.TRAIN, packetBufferReceiver.readString());
		screen = EnumHelper.valueOf(ScreenType.DEFAULT, packetBufferReceiver.readString());
		id = packetBufferReceiver.readLong();
	}

	private PacketOpenDashboardScreen(String content, TransportMode transportMode) {
		super(content);
		this.transportMode = transportMode;
		this.screen = ScreenType.DEFAULT;
		this.id = 0;
	}

	private PacketOpenDashboardScreen(String content, TransportMode transportMode, ScreenType screenType, long id) {
		super(content);
		this.transportMode = transportMode;
		this.screen = screenType;
		this.id = id;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		super.write(packetBufferSender);
		packetBufferSender.writeString(transportMode.toString());
		packetBufferSender.writeString(screen.toString());
		packetBufferSender.writeLong(id);
	}

	@Override
	protected void runClientInbound(Response response) {
		response.getData(jsonReader -> new ListDataResponse(jsonReader, MinecraftClientData.getDashboardInstance())).write();
		ClientPacketHelper.openDashboardScreen(transportMode, screen, id);
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketOpenDashboardScreen(content, transportMode, screen, id);
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

	public static void sendDirectlyToServer(ServerWorld serverWorld, ServerPlayerEntity serverPlayerEntity, TransportMode transportMode) {
		new PacketOpenDashboardScreen(new JsonObject().toString(), transportMode).runServerOutbound(serverWorld, serverPlayerEntity);
	}

	public static void sendDirectlyToServer(ServerWorld serverWorld, ServerPlayerEntity serverPlayerEntity, TransportMode transportMode, ScreenType screenType, long id) {
		new PacketOpenDashboardScreen(new JsonObject().toString(), transportMode, screenType, id).runServerOutbound(serverWorld, serverPlayerEntity);
	}
}
