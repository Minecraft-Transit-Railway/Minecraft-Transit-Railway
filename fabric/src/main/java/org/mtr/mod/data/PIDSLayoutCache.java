package org.mtr.mod.data;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Screen;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.packet.PacketGetPIDSLayoutData;
import org.mtr.mod.render.pids.PIDSRenderController;
import org.mtr.mod.screen.PIDSLayoutEditScreen;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PIDSLayoutCache {
    private final Map<String, PIDSLayoutEntry> pidsLayoutData = new HashMap<>();
    private final Map<String, RequestStatus> pidsRequestStatus = new HashMap<>();
    private final ArrayList<PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> pidsMetadata = new ArrayList<>();
    private String openEditPending = null;

    @Nullable
    public PIDSRenderController getController(String layout) {
        PIDSLayoutEntry entry = getEntry(layout);
        return entry == null ? null : entry.getController();
    }

    public String getData(String layout) {
        PIDSLayoutEntry entry = getEntry(layout);
        return entry == null ? null : entry.getData();
    }

    private PIDSLayoutEntry getEntry(String layout) {
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

    public ObjectImmutableList<PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> getMetadata() {
        return new ObjectImmutableList<>(pidsMetadata);
    }

    public void setMetadata(ArrayList<PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> metadata) {
        pidsMetadata.clear();
        pidsMetadata.addAll(metadata);
        // Loop through all metadata and clear the request status for each layout
        for (PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata data : metadata) {
            pidsRequestStatus.remove(data.id);
        }
    }

    public void addMetadata(ArrayList<PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> metadata) {
        pidsMetadata.addAll(metadata);
    }

    public void setLayout(String key, String layout) {
        pidsLayoutData.put(key, new PIDSLayoutEntry(layout, new PIDSRenderController(layout)));
        pidsRequestStatus.remove(key);
        if (openEditPending != null && openEditPending.equals(key)) {
            MinecraftClient.getInstance().openScreen(new Screen(new PIDSLayoutEditScreen(layout)));
        }
        Init.LOGGER.info(String.format("PIDS layout data received for '%s' with %s modules", key, pidsLayoutData.get(key).getController().getModules().size()));
    }

    public void setFailed(String key) {
        pidsRequestStatus.put(key, RequestStatus.FAILED);
        Init.LOGGER.info(String.format("PIDS layout data request failed for '%s'", key));
    }

    public void setEditPending(String key) {
        openEditPending = key;
    }

    private enum RequestStatus {
        PENDING,
        FAILED
    }

    private class PIDSLayoutEntry {
        private final String data;
        private final PIDSRenderController controller;

        public PIDSLayoutEntry(String data, PIDSRenderController controller) {
            this.data = data;
            this.controller = controller;
        }

        public String getData() {
            return data;
        }

        public PIDSRenderController getController() {
            return controller;
        }
    }
}
