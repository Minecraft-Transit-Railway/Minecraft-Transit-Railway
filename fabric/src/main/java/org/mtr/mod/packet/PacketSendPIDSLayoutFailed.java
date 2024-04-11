package org.mtr.mod.packet;

import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.InitClient;

public final class PacketSendPIDSLayoutFailed extends PacketHandler {
    private final String key;

    public PacketSendPIDSLayoutFailed(PacketBufferReceiver packetBufferReceiver) {
        key = packetBufferReceiver.readString();
    }

    public PacketSendPIDSLayoutFailed(String key) {
        this.key = key;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeString(key);
    }

    @Override
    public void runClient() {
        InitClient.pidsLayoutCache.setFailed(key);
    }
}
