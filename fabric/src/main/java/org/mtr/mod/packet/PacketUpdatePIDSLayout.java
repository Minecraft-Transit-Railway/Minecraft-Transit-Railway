package org.mtr.mod.packet;

import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.data.PIDSLayoutData;
import org.mtr.mod.render.pids.PIDSRenderController;
import org.mtr.mod.resource.PIDSData;

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
        PIDSData data = new PIDSData(new JsonReader(JsonParser.parseString(layout)));
        PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata metadata = new PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata(key, data.getName(), data.getDescription(), data.getAuthor(), data.getModules().size(), new PIDSRenderController(layout).arrivals);
        for (String name : minecraftServer.getPlayerNames()) {
            ServerPlayerEntity player = minecraftServer.getPlayerManager().getPlayer(name);
            if (player != null) {
                Init.pidsLayoutData.sendUpdate(player, metadata);
            }
        }
        Init.LOGGER.info("New PIDS Layout Recieved: '" + key + "'");
    }
}
