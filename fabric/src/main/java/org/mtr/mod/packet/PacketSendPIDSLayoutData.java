package org.mtr.mod.packet;

import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.InitClient;

public final class PacketSendPIDSLayoutData extends PacketHandler {
    private final String key;
    private final String layout;

    public PacketSendPIDSLayoutData(PacketBufferReceiver packetBufferReceiver) {
        key = packetBufferReceiver.readString();
        layout = packetBufferReceiver.readString();
    }

    public PacketSendPIDSLayoutData(String key, String layout) {
        this.key = key;
        this.layout = layout;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeString(key);
        packetBufferSender.writeString(layout);
    }

    @Override
    public void runClient() {
        InitClient.pidsLayoutCache.setLayout(key, layout);
    }
}
