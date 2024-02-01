package org.mtr.mod.packet;

import org.mtr.core.data.Data;
import org.mtr.core.data.TransportMode;
import org.mtr.core.integration.Integration;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.tool.EnumHelper;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.PlayerEntity;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mod.Init;

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
		ClientPacketHelper.openDashboardScreen(transportMode, useTimeAndWindSync);
	}

	public static void create(PlayerEntity playerEntity, TransportMode transportMode) {
		sendHttpDataRequest(IntegrationServlet.Operation.LIST, new Integration(new Data()), integration -> Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(playerEntity), new PacketOpenDashboardScreen(integration, transportMode, false)));
	}
}
