package org.mtr.mod.packet;

import org.mtr.core.data.EnumHelper;
import org.mtr.core.data.TransportMode;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.PlayerEntity;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.Registry;
import org.mtr.mod.screen.DashboardScreen;

public class PacketOpenDashboardScreen extends PacketData {

	private final TransportMode transportMode;
	private final boolean useTimeAndWindSync;

	public PacketOpenDashboardScreen(PacketBuffer packetBuffer) {
		super(packetBuffer);
		transportMode = EnumHelper.valueOf(TransportMode.TRAIN, readString(packetBuffer));
		useTimeAndWindSync = packetBuffer.readBoolean();
	}

	private PacketOpenDashboardScreen(JsonObject jsonObject, TransportMode transportMode, boolean useTimeAndWindSync) {
		super(IntegrationServlet.Operation.LIST, jsonObject);
		this.transportMode = transportMode;
		this.useTimeAndWindSync = useTimeAndWindSync;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		super.write(packetBuffer);
		writeString(packetBuffer, transportMode.toString());
		packetBuffer.writeBoolean(useTimeAndWindSync);
	}

	@Override
	public void runClientQueued() {
		super.runClientQueued();
		IPacket.openScreen(new DashboardScreen(transportMode, useTimeAndWindSync), screenExtension -> screenExtension instanceof DashboardScreen);
	}

	public static void create(PlayerEntity playerEntity, TransportMode transportMode) {
		sendHttpDataRequest(IntegrationServlet.Operation.LIST, new JsonObject(), data -> Registry.sendPacketToClient(ServerPlayerEntity.cast(playerEntity), new PacketOpenDashboardScreen(data, transportMode, false)));
	}
}
