package org.mtr.mod.data;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.packet.PacketGetPIDSLayoutData;
import org.mtr.mod.render.pids.PIDSRenderController;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PIDSLayoutCache {
    private final Map<String, PIDSRenderController> pidsLayoutData = new HashMap<>();
    private final Map<String, RequestStatus> pidsRequestStatus = new HashMap<>();
    private final ArrayList<PIDSLayoutEntry.PIDSLayoutMetadata> pidsMetadata = new ArrayList<>();

    @Nullable
    public PIDSRenderController getController(String layout) {
        // If the layout exists in the cache, return it
        if (pidsLayoutData.containsKey(layout)) {
            return pidsLayoutData.get(layout);
        }
        // If a request was sent out, return null
        if (pidsRequestStatus.containsKey(layout)) {
            return null;
        }
        // Send a request to the server for the layout
        pidsRequestStatus.put(layout, RequestStatus.PENDING);
        InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketGetPIDSLayoutData(layout));
        return null;
    }

    public ObjectImmutableList<PIDSLayoutEntry.PIDSLayoutMetadata> getMetadata() {
        return new ObjectImmutableList<>(pidsMetadata);
    }

    public void setMetadata(ArrayList<PIDSLayoutEntry.PIDSLayoutMetadata> metadata) {
        pidsMetadata.clear();
        pidsMetadata.addAll(metadata);
    }

    public void addMetadata(ArrayList<PIDSLayoutEntry.PIDSLayoutMetadata> metadata) {
        pidsMetadata.addAll(metadata);
    }

    public void setLayout(String key, String layout) {
        pidsLayoutData.put(key, new PIDSRenderController(layout));
        pidsRequestStatus.remove(key);
        Init.LOGGER.info(String.format("PIDS layout data received for '%s' with %s modules", key, pidsLayoutData.get(key).getModules().size()));
    }

    public void setFailed(String key) {
        pidsRequestStatus.put(key, RequestStatus.FAILED);
        Init.LOGGER.info(String.format("PIDS layout data request failed for '%s'", key));
    }

    private enum RequestStatus {
        PENDING,
        FAILED
    }
}
