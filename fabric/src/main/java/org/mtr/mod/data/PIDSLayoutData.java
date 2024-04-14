package org.mtr.mod.data;

import org.jetbrains.annotations.NotNull;
import org.mtr.mapping.holder.CompoundTag;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.mapper.PersistenceStateExtension;
import org.mtr.mod.Init;
import org.mtr.mod.packet.PacketUpdatePIDSMetadata;

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
        pidsLayoutData.clear();
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
        Init.REGISTRY.sendPacketToClient(player, new PacketUpdatePIDSMetadata(metadata, true));
    }
}
