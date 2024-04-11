package org.mtr.mod.data;

import org.jetbrains.annotations.NotNull;
import org.mtr.mapping.holder.CompoundTag;
import org.mtr.mapping.mapper.PersistenceStateExtension;

import java.util.HashMap;
import java.util.Map;

public class PIDSLayoutData extends PersistenceStateExtension {
    private final Map<String, String> pidsLayoutData = new HashMap<>();

    public PIDSLayoutData(String key) {
        super(key);
    }

    /**
     * Get the layout data of a key
     * @param key the ID of the layout
     * @return the raw JSON data of the layout
     */
    public String getLayoutData(String key) {
        return pidsLayoutData.get(key);
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
        pidsLayoutData.put(key, value);
        return true;
    }

    /**
     * Sets or overwrites the layout data of a key
     * This can potentially overwrite existing data and should be used with caution
     * @param key the ID of the layout
     * @param value the raw JSON data to be stored
     */
    public void setLayoutData(String key, String value) {
        pidsLayoutData.put(key, value);
    }

    @Override
    public void readNbt(CompoundTag compoundTag) {
        pidsLayoutData.clear();
        for (String key : compoundTag.getKeys()) {
            pidsLayoutData.put(key, compoundTag.getString(key));
        }
    }

    @NotNull
    @Override
    public CompoundTag writeNbt2(CompoundTag compoundTag) {
        for (Map.Entry<String, String> entry : pidsLayoutData.entrySet()) {
            compoundTag.putString(entry.getKey(), entry.getValue());
        }
        return compoundTag;
    }
}
