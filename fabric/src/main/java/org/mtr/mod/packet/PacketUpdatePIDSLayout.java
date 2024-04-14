package org.mtr.mod.packet;

import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;

public final class PacketUpdatePIDSLayout extends PacketHandler {
    private final String key;
    private final String layout;

    public PacketUpdatePIDSLayout(PacketBufferReceiver packetBufferReceiver) {
        key = packetBufferReceiver.readString();
        layout = packetBufferReceiver.readString();
    }

    public PacketUpdatePIDSLayout(String key, String layout) {
        this.key = key;
        this.layout = layout;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeString(key);
        packetBufferSender.writeString(layout);
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        Init.pidsLayoutData.setLayoutData(key, layout);
        // send metadata update to all clients
        for (String name : minecraftServer.getPlayerNames()) {
            ServerPlayerEntity player = minecraftServer.getPlayerManager().getPlayer(name);
            if (player != null) {
                Init.pidsLayoutData.sendMetadata(player);
            }
        }
        Init.LOGGER.info("New PIDS Layout Recieved: '" + key + "'");
    }
}
