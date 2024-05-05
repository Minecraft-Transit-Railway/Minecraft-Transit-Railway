package org.mtr.mod.packet;

import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;

public final class PacketOpenCustomRailScreen extends PacketHandler {

    private final int speed;
    private final boolean isOneWay;

    public PacketOpenCustomRailScreen(PacketBufferReceiver packetBufferReceiver) {
        speed = packetBufferReceiver.readInt();
        isOneWay = packetBufferReceiver.readBoolean();
    }

    public PacketOpenCustomRailScreen(int speed, boolean isOneWay) {
        this.speed = speed;
        this.isOneWay = isOneWay;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeInt(speed);
        packetBufferSender.writeBoolean(isOneWay);
    }

    @Override
    public void runClient() {
        ClientPacketHelper.openCustomRailScreen(speed, isOneWay);
    }
}
