package org.mtr.mod.packet;

import org.mtr.core.data.Data;
import org.mtr.core.data.TransportMode;
import org.mtr.core.integration.Integration;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.tool.EnumHelper;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.PlayerEntity;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.Registry;
import org.mtr.mod.screen.DashboardScreen;

public final class PacketOpenDashboardScreen extends PacketDataBase {

	private final TransportMode transportMode;
	private final boolean useTimeAndWindSync;

	public static PacketOpenDashboardScreen create(PacketBuffer packetBuffer) {
		return create(packetBuffer, (operation, integration, updateClientDataInstance, updateClientDataDashboardInstance) -> new PacketOpenDashboardScreen(integration, EnumHelper.valueOf(TransportMode.TRAIN, readString(packetBuffer)), packetBuffer.readBoolean()));
	}

	private PacketOpenDashboardScreen(Integration integration, TransportMode transportMode, boolean useTimeAndWindSync) {
		super(IntegrationServlet.Operation.LIST, integration, false, true);
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
		sendHttpDataRequest(IntegrationServlet.Operation.LIST, new Integration(new Data()), integration -> Registry.sendPacketToClient(ServerPlayerEntity.cast(playerEntity), new PacketOpenDashboardScreen(integration, transportMode, false)));
	}
}
