package org.mtr.mod.packet;

import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;

public final class PacketGetPIDSLayoutData extends PacketHandler {
    private final String key;

    public PacketGetPIDSLayoutData(PacketBufferReceiver packetBufferReceiver) {
        key = packetBufferReceiver.readString();
    }

    public PacketGetPIDSLayoutData(String key) {
        this.key = key;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeString(key);
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        final String layout = Init.pidsLayoutData.getLayoutData(key);
        if (layout == null) {
            Init.REGISTRY.sendPacketToClient(serverPlayerEntity, new PacketSendPIDSLayoutFailed(key));
        } else {
            Init.REGISTRY.sendPacketToClient(serverPlayerEntity, new PacketSendPIDSLayoutData(key, layout));
        }
    }
}
