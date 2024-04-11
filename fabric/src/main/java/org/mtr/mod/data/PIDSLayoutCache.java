package org.mtr.mod.data;

import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.packet.PacketGetPIDSLayoutData;
import org.mtr.mod.render.pids.PIDSRenderController;

import java.util.HashMap;
import java.util.Map;

public class PIDSLayoutCache {
    private final Map<String, PIDSRenderController> pidsLayoutData = new HashMap<>();
    private final Map<String, RequestStatus> pidsRequestStatus = new HashMap<>();

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

    public void setLayout(String key, String layout) {
        pidsLayoutData.put(key, new PIDSRenderController(layout));
        pidsRequestStatus.remove(key);
    }

    public void setFailed(String key) {
        pidsRequestStatus.put(key, RequestStatus.FAILED);
    }

    private enum RequestStatus {
        PENDING,
        FAILED
    }
}
