package org.mtr.mod.packet;

import org.mtr.core.data.TransportMode;
import org.mtr.core.integration.Integration;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.tool.EnumHelper;
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

	private PacketOpenDashboardScreen(Integration integration, TransportMode transportMode, boolean useTimeAndWindSync) {
		super(IntegrationServlet.Operation.LIST, integration);
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
		sendHttpDataRequest(IntegrationServlet.Operation.LIST, new Integration(), integration -> Registry.sendPacketToClient(ServerPlayerEntity.cast(playerEntity), new PacketOpenDashboardScreen(integration, transportMode, false)));
	}
}
