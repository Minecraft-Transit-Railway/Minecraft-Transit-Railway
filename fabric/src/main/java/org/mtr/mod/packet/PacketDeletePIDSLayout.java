package org.mtr.mod.packet;

import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.data.PIDSLayoutData;
import org.mtr.mod.render.pids.PIDSRenderController;
import org.mtr.mod.resource.PIDSData;

public final class PacketDeletePIDSLayout extends PacketHandler {
    private final String key;

    public PacketDeletePIDSLayout(PacketBufferReceiver packetBufferReceiver) {
        key = packetBufferReceiver.readString();
    }

    public PacketDeletePIDSLayout(String key) {
        this.key = key;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeString(key);
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        Init.pidsLayoutData.removeLayout(key);
        // send metadata update to all clients
        for (String name : minecraftServer.getPlayerNames()) {
            ServerPlayerEntity player = minecraftServer.getPlayerManager().getPlayer(name);
            if (player != null) {
                Init.REGISTRY.sendPacketToClient(player, new PacketDeletePIDSLayout(key));
            }
        }
        Init.LOGGER.info("PIDS Layout Deleted: '" + key + "'");
    }

    @Override
    public void runClient() {
        InitClient.pidsLayoutCache.removeLayout(key);
    }
}
