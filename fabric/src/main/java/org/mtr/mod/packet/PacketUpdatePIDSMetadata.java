package org.mtr.mod.packet;

import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.data.PIDSLayoutData;

public final class PacketUpdatePIDSMetadata extends PacketHandler {
    private final PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata metadata;

    public PacketUpdatePIDSMetadata(PacketBufferReceiver packetBufferReceiver) {
        metadata = new PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata(packetBufferReceiver.readString(), packetBufferReceiver.readString(), packetBufferReceiver.readString(), packetBufferReceiver.readString(), packetBufferReceiver.readInt(), packetBufferReceiver.readInt());
    }

    public PacketUpdatePIDSMetadata(PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata data) {
        this.metadata = data;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeString(metadata.id);
        packetBufferSender.writeString(metadata.name);
        packetBufferSender.writeString(metadata.description);
        packetBufferSender.writeString(metadata.author);
        packetBufferSender.writeInt(metadata.moduleCount);
        packetBufferSender.writeInt(metadata.arrivalCount);
    }

    @Override
    public void runClient() {
        InitClient.pidsLayoutCache.updateMetadata(metadata);
        Init.LOGGER.info("PIDS Metadata update received, ID: " + metadata.id);
    }
}
