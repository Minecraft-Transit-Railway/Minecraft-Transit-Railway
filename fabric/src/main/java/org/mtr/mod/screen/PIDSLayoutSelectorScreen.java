package org.mtr.mod.screen;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mod.InitClient;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.PIDSLayoutEntry;

import java.util.HashMap;
import java.util.Map;

public class PIDSLayoutSelectorScreen extends DashboardListSelectorScreen implements IGui {

    public PIDSLayoutSelectorScreen(Runnable onClose, ObjectImmutableList<DashboardListItem> allLayouts, LongCollection selectedLayouts) {
        super(onClose, allLayouts, selectedLayouts, true, false);
    }

    public static Map<Long, PIDSLayoutEntry.PIDSLayoutMetadata> getLayouts() {
        final ObjectImmutableList<PIDSLayoutEntry.PIDSLayoutMetadata> metadata = InitClient.pidsLayoutCache.getMetadata();
        final HashMap<Long, PIDSLayoutEntry.PIDSLayoutMetadata> layouts = new HashMap<>();
        // assign numerical IDs to each layout
        long id = 0;
        for (PIDSLayoutEntry.PIDSLayoutMetadata data : metadata) {
            layouts.put(id, data);
            id++;
        }
        return layouts;
    }

    public static class LayoutListItem extends DashboardListItem {
        public final PIDSLayoutEntry.PIDSLayoutMetadata metadata;

        public LayoutListItem(long id, String name, int color, PIDSLayoutEntry.PIDSLayoutMetadata metadata) {
            super(id, name, color);
            this.metadata = metadata;
        }
    }
}
