package org.mtr.mod.screen;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.OrderedText;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.PIDSLayoutData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PIDSLayoutSelectorScreen extends DashboardListSelectorScreen implements IGui {
    protected final ObjectImmutableList<LayoutListItem> allData;

    public PIDSLayoutSelectorScreen(Runnable onClose, ObjectImmutableList<LayoutListItem> allLayouts, LongCollection selectedLayouts) {
        super(onClose, new ObjectImmutableList<>(allLayouts), selectedLayouts, true, false);
        this.allData = allLayouts;
    }

    @Override
    protected void init2() {
        super.init2();
        final int spareSpace = Math.max(0, width - SQUARE_SIZE * 4 - PANEL_WIDTH * 2);
        availableList.x = SQUARE_SIZE * 2 + spareSpace;
        selectedList.x = SQUARE_SIZE * 3 + spareSpace + PANEL_WIDTH;
    }

    @Override
    public void renderAdditional(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        final int spareSpace = Math.max(0, width - SQUARE_SIZE * 4 - PANEL_WIDTH * 2);
        graphicsHolder.drawCenteredText(TextHelper.translatable("gui.mtr.available"), SQUARE_SIZE * 2 + spareSpace + PANEL_WIDTH / 2, SQUARE_SIZE, ARGB_WHITE);
        graphicsHolder.drawCenteredText(TextHelper.translatable("gui.mtr.selected"), SQUARE_SIZE * 3 + spareSpace + PANEL_WIDTH * 3 / 2, SQUARE_SIZE, ARGB_WHITE);

        final int index = availableList.getHoverItemIndex();
        if (index < 0 || index >= allData.size()) {
            return;
        }
        PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata metadata = allData.get(index).metadata;
        int y = SQUARE_SIZE;
        y = drawWrappedText(graphicsHolder, TextHelper.literal(metadata.name), y, ARGB_WHITE);
        y = drawWrappedText(graphicsHolder, TextHelper.translatable("gui.mtr.layout_author", metadata.author.isBlank() ? TextHelper.translatable("gui.mtr.anonymous").getString() : metadata.author), y, ARGB_WHITE);
        y = drawWrappedText(graphicsHolder, TextHelper.translatable("gui.mtr.layout_arrivals", metadata.arrivalCount), y, ARGB_WHITE);
        y = drawWrappedText(graphicsHolder, TextHelper.translatable("gui.mtr.layout_modules", metadata.moduleCount), y, ARGB_WHITE);
        drawWrappedText(graphicsHolder, TextHelper.literal(metadata.description), y, ARGB_LIGHT_GRAY);
    }

    public static Map<Long, PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> getLayouts() {
        final ObjectImmutableList<PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> metadata = InitClient.pidsLayoutCache.getMetadata();
        final HashMap<Long, PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> layouts = new HashMap<>();
        // assign numerical IDs to each layout
        long id = 0;
        for (PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata data : metadata) {
            layouts.put(id, data);
            id++;
        }
        return layouts;
    }

    private int drawWrappedText(GraphicsHolder graphicsHolder, MutableText component, int y, int color) {
        final List<OrderedText> splitText = GraphicsHolder.wrapLines(component, Math.max(0, width - SQUARE_SIZE * 4 - PANEL_WIDTH * 2));
        int newY = y;
        for (final OrderedText formattedCharSequence : splitText) {
            final int nextY = newY + TEXT_HEIGHT + 2;
            if (nextY > height - SQUARE_SIZE - TEXT_HEIGHT) {
                graphicsHolder.drawText("...", SQUARE_SIZE, newY, color, false, GraphicsHolder.getDefaultLight());
                return height;
            } else {
                graphicsHolder.drawText(formattedCharSequence, SQUARE_SIZE, newY, color, false, GraphicsHolder.getDefaultLight());
            }
            newY = nextY;
        }
        return newY + TEXT_PADDING;
    }

    public static class LayoutListItem extends DashboardListItem {
        public final PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata metadata;

        public LayoutListItem(long id, String name, int color, PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata metadata) {
            super(id, name, color);
            this.metadata = metadata;
        }
    }
}
