package org.mtr.mod.packet;

import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;

public final class PacketOpenPIDSDashboardScreen extends PacketHandler {

    public PacketOpenPIDSDashboardScreen(PacketBufferReceiver packetBufferReceiver) {}

    public PacketOpenPIDSDashboardScreen() {}

    @Override
    public void write(PacketBufferSender packetBufferSender) {}

    @Override
    public void runClient() {
        ClientPacketHelper.openPIDSDashboardScreen();
    }
}
