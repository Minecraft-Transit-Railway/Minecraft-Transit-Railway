package org.mtr.mod.data;

import org.jetbrains.annotations.NotNull;
import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.mapping.holder.CompoundTag;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.mapper.PersistenceStateExtension;
import org.mtr.mod.Init;
import org.mtr.mod.packet.PacketSendPIDSMetadata;
import org.mtr.mod.packet.PacketUpdatePIDSMetadata;
import org.mtr.mod.render.pids.PIDSRenderController;
import org.mtr.mod.resource.PIDSData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PIDSLayoutData extends PersistenceStateExtension {
    private final Map<String, PIDSLayoutEntry> pidsLayoutData = new HashMap<>();

    public PIDSLayoutData(String key) {
        super(key);
    }

    /**
     * Get the layout data of a key
     * @param key the ID of the layout
     * @return the raw JSON data of the layout
     */
    public String getLayoutData(String key) {
        if (!pidsLayoutData.containsKey(key)) {
            return null;
        }
        return pidsLayoutData.get(key).data;
    }

    /**
     * Add a new unique key-value pair to the layout data
     * @param key the ID of the layout
     * @param value the raw JSON data to be stored
     * @return false if the key already exists, true otherwise
     */
    public boolean addLayoutData(String key, String value) {
        if (pidsLayoutData.containsKey(key)) {
            return false;
        }
        pidsLayoutData.put(key, new PIDSLayoutEntry(value));
        return true;
    }

    /**
     * Sets or overwrites the layout data of a key
     * This can potentially overwrite existing data and should be used with caution
     * @param key the ID of the layout
     * @param value the raw JSON data to be stored
     */
    public void setLayoutData(String key, String value) {
        pidsLayoutData.put(key, new PIDSLayoutEntry(value));
    }

    public void removeLayout(String key) {
        pidsLayoutData.remove(key);
    }

    /**
     * @return a map of all the layout metadata
     */
    public Map<String, PIDSLayoutEntry.PIDSLayoutMetadata> getAllLayouts() {
        Map<String, PIDSLayoutEntry.PIDSLayoutMetadata> shareableMap = new HashMap<>();
        for (Map.Entry<String, PIDSLayoutEntry> entry : pidsLayoutData.entrySet()) {
            shareableMap.put(entry.getKey(), entry.getValue().metadata);
        }
        return shareableMap;
    }

    @Override
    public void readNbt(CompoundTag compoundTag) {
        for (String key : compoundTag.getKeys()) {
            pidsLayoutData.put(key, new PIDSLayoutEntry(compoundTag.getString(key)));
        }
    }

    @NotNull
    @Override
    public CompoundTag writeNbt2(CompoundTag compoundTag) {
        for (Map.Entry<String, PIDSLayoutEntry> entry : pidsLayoutData.entrySet()) {
            compoundTag.putString(entry.getKey(), entry.getValue().data);
        }
        return compoundTag;
    }

    /**
     * Send all of the layout data to the client
     */
    public void sendMetadata(ServerPlayerEntity player) {
        ArrayList<PIDSLayoutEntry.PIDSLayoutMetadata> metadata = new ArrayList<>();
        for (PIDSLayoutEntry entry : pidsLayoutData.values()) {
            metadata.add(entry.metadata);
        }
        Init.REGISTRY.sendPacketToClient(player, new PacketSendPIDSMetadata(metadata));
    }

    public void sendUpdate(ServerPlayerEntity player, PIDSLayoutEntry.PIDSLayoutMetadata metadata) {
        Init.REGISTRY.sendPacketToClient(player, new PacketUpdatePIDSMetadata(metadata));
    }

    public static class PIDSLayoutEntry {
        public final String data;
        public final PIDSLayoutMetadata metadata;

        public PIDSLayoutEntry(String data) {
            PIDSData pidsData = new PIDSData(new JsonReader(JsonParser.parseString(data)));
            this.metadata = new PIDSLayoutMetadata(pidsData.getID(), pidsData.getName(), pidsData.getDescription(), pidsData.getAuthor(), pidsData.getModules().size(), new PIDSRenderController(data).arrivals);
            this.data = data;
        }

        public static class PIDSLayoutMetadata {
            public final String id;
            public final String name;
            public final String description;
            public final String author;
            public final int moduleCount;
            public final int arrivalCount;

            public PIDSLayoutMetadata(String id, String name, String description, String author, int moduleCount, int arrivalCount) {
                this.id = id;
                this.name = name;
                this.description = description;
                this.author = author;
                this.moduleCount = moduleCount;
                this.arrivalCount = arrivalCount;
            }
        }
    }
}
