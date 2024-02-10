package org.mtr.mod.packet;

import org.mtr.core.data.TransportMode;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.tool.EnumHelper;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mapping.holder.PlayerEntity;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;

public final class PacketOpenDashboardScreen extends PacketDataBase {

	private final TransportMode transportMode;
	private final boolean useTimeAndWindSync;

	public static PacketOpenDashboardScreen create(PacketBufferReceiver packetBufferReceiver) {
		return create(packetBufferReceiver, (operation, integrationObject, updateClientDataInstance, updateClientDataDashboardInstance) -> new PacketOpenDashboardScreen(integrationObject, EnumHelper.valueOf(TransportMode.TRAIN, packetBufferReceiver.readString()), packetBufferReceiver.readBoolean()));
	}

	private PacketOpenDashboardScreen(JsonObject integrationObject, TransportMode transportMode, boolean useTimeAndWindSync) {
		super(IntegrationServlet.Operation.LIST, integrationObject, false, true);
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
	public void runClient() {
		super.runClient();
		ClientPacketHelper.openDashboardScreen(transportMode, useTimeAndWindSync);
	}

	public static void create(PlayerEntity playerEntity, TransportMode transportMode) {
		sendHttpDataRequest(IntegrationServlet.Operation.LIST, new JsonObject(), integrationObject -> Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(playerEntity), new PacketOpenDashboardScreen(integrationObject, transportMode, false)));
	}
}
