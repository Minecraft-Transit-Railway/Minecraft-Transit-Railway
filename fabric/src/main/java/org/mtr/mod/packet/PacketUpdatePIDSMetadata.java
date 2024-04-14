package org.mtr.mod.packet;

import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.data.PIDSLayoutData;

import java.util.ArrayList;

public final class PacketUpdatePIDSMetadata extends PacketHandler {
    private final boolean firstPacket;
    private final ArrayList<PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> data;

    public PacketUpdatePIDSMetadata(PacketBufferReceiver packetBufferReceiver) {
        this.firstPacket = packetBufferReceiver.readBoolean();
        data = new ArrayList<>();
        int size = packetBufferReceiver.readInt();
        for (int i = 0; i < size; i++) {
            data.add(new PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata(packetBufferReceiver.readString(), packetBufferReceiver.readString(), packetBufferReceiver.readString(), packetBufferReceiver.readString(), packetBufferReceiver.readInt(), packetBufferReceiver.readInt()));
        }
    }

    public PacketUpdatePIDSMetadata(ArrayList<PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> data, boolean firstPacket) {
        this.data = data;
        this.firstPacket = firstPacket;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeBoolean(firstPacket);
        packetBufferSender.writeInt(data.size());
        for (PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata metadata : data) {
            packetBufferSender.writeString(metadata.id);
            packetBufferSender.writeString(metadata.name);
            packetBufferSender.writeString(metadata.description);
            packetBufferSender.writeString(metadata.author);
            packetBufferSender.writeInt(metadata.moduleCount);
            packetBufferSender.writeInt(metadata.arrivalCount);
        }
    }

    @Override
    public void runClient() {
        if (firstPacket) {
            InitClient.pidsLayoutCache.setMetadata(data);
        } else {
            InitClient.pidsLayoutCache.addMetadata(data);
        }
        Init.LOGGER.info("PIDS Metadata recieved, Count: " + data.size());
    }
}
