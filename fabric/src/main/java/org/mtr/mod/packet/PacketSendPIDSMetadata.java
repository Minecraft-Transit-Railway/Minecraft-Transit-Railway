package org.mtr.mod.packet;

import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.data.PIDSLayoutData;

import java.util.ArrayList;

public final class PacketSendPIDSMetadata extends PacketHandler {
    private final ArrayList<PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> data;

    public PacketSendPIDSMetadata(PacketBufferReceiver packetBufferReceiver) {
        data = new ArrayList<>();
        int size = packetBufferReceiver.readInt();
        for (int i = 0; i < size; i++) {
            data.add(new PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata(packetBufferReceiver.readString(), packetBufferReceiver.readString(), packetBufferReceiver.readString(), packetBufferReceiver.readString(), packetBufferReceiver.readInt(), packetBufferReceiver.readInt()));
        }
    }

    public PacketSendPIDSMetadata(ArrayList<PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> data) {
        this.data = data;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
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
        InitClient.pidsLayoutCache.setMetadata(data);
        Init.LOGGER.info("PIDS Metadata recieved, Count: " + data.size());
    }
}
