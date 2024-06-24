package org.mtr.mod.packet;

import org.mtr.core.data.EnumHelper;
import org.mtr.core.data.TransportMode;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.screen.DashboardScreen;

public class PacketOpenDashboardScreen extends PacketHandler {

	private final TransportMode transportMode;
	private final boolean useTimeAndWindSync;

	public PacketOpenDashboardScreen(PacketBuffer packetBuffer) {
		transportMode = EnumHelper.valueOf(TransportMode.TRAIN, readString(packetBuffer));
		useTimeAndWindSync = packetBuffer.readBoolean();
	}

	public PacketOpenDashboardScreen(TransportMode transportMode, boolean useTimeAndWindSync) {
		this.transportMode = transportMode;
		this.useTimeAndWindSync = useTimeAndWindSync;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		writeString(packetBuffer, transportMode.toString());
		packetBuffer.writeBoolean(useTimeAndWindSync);
	}

	@Override
	public void runClientQueued() {
		IPacket.openScreen(new DashboardScreen(transportMode, useTimeAndWindSync), screenExtension -> screenExtension instanceof DashboardScreen);
	}
}
