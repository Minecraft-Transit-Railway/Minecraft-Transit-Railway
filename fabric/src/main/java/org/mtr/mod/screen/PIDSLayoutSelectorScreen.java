package org.mtr.mod.screen;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.OrderedText;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.PIDSLayoutData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PIDSLayoutSelectorScreen extends ScreenExtension implements IGui {
    private final DashboardList layoutList;
    private final ArrayList<LayoutListItem> allLayouts;
    private Map<Long, PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> layoutIDMap = new HashMap<>();

    public PIDSLayoutSelectorScreen(Consumer<PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> onClose) {
        this.layoutList = new DashboardList(null, null, null, null, (item, index) -> onClose.accept(this.layoutIDMap.get(item.id)), null, null, () -> MinecraftClientData.PIDS_LAYOUT_SEARCH, text -> MinecraftClientData.PIDS_LAYOUT_SEARCH = text);
        final ObjectImmutableList<PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> metadata = InitClient.pidsLayoutCache.getMetadata();
        final HashMap<Long, PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> layouts = new HashMap<>();
        // assign numerical IDs to each layout
        long id = 0;
        for (PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata data : metadata) {
            layouts.put(id, data);
            id++;
        }
        ArrayList<PIDSLayoutSelectorScreen.LayoutListItem> list = new ArrayList<>();
        for (Map.Entry<Long, PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata> entry : layouts.entrySet()) {
            list.add(new PIDSLayoutSelectorScreen.LayoutListItem(entry.getKey(), entry.getValue().name, 0xFFFFFF, entry.getValue()));
        }
        this.layoutIDMap = layouts;
        this.allLayouts = list;
        updateList();
    }

    @Override
    protected void init2() {
        super.init2();
        final int spareSpace = Math.max(0, width - SQUARE_SIZE * 4 - PANEL_WIDTH * 3);
        layoutList.x = SQUARE_SIZE * 3 + spareSpace + PANEL_WIDTH;
        layoutList.y = SQUARE_SIZE * 2 - TEXT_PADDING;
        layoutList.height = height - SQUARE_SIZE * 5 + TEXT_PADDING;
        layoutList.width = PANEL_WIDTH * 2;
        layoutList.init(this::addChild);

        updateList();
    }

    @Override
    public void tick2() {
        layoutList.tick();
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        renderBackground(graphicsHolder);
        layoutList.render(graphicsHolder);
        super.render(graphicsHolder, mouseX, mouseY, delta);
        renderAdditional(graphicsHolder, mouseX, mouseY, delta);
    }

    public void renderAdditional(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        final int index = layoutList.getHoverItemIndex();
        if (index < 0 || index >= allLayouts.size()) {
            return;
        }
        PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata metadata = allLayouts.get(index).metadata;
        int y = SQUARE_SIZE;
        y = drawWrappedText(graphicsHolder, TextHelper.literal(metadata.name), y, ARGB_WHITE);
        y = drawWrappedText(graphicsHolder, TextHelper.translatable("gui.mtr.layout_author", metadata.author.isEmpty() ? TextHelper.translatable("gui.mtr.anonymous").getString() : metadata.author), y, ARGB_WHITE);
        y = drawWrappedText(graphicsHolder, TextHelper.translatable("gui.mtr.layout_arrivals", metadata.arrivalCount), y, ARGB_WHITE);
        y = drawWrappedText(graphicsHolder, TextHelper.translatable("gui.mtr.layout_modules", metadata.moduleCount), y, ARGB_WHITE);
        drawWrappedText(graphicsHolder, TextHelper.literal(metadata.description), y, ARGB_LIGHT_GRAY);
    }

    private int drawWrappedText(GraphicsHolder graphicsHolder, MutableText component, int y, int color) {
        final List<OrderedText> splitText = GraphicsHolder.wrapLines(component, Math.max(0, width - SQUARE_SIZE * 4 - PANEL_WIDTH));
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

    @Override
    public boolean isPauseScreen2() {
        return false;
    }

    @Override
    public void mouseMoved2(double mouseX, double mouseY) {
        layoutList.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled3(double mouseX, double mouseY, double amount) {
        layoutList.mouseScrolled(mouseX, mouseY, amount);
        return super.mouseScrolled3(mouseX, mouseY, amount);
    }

    private void updateList() {
        final ObjectArrayList<DashboardListItem> availableLayouts = new ObjectArrayList<>();
        availableLayouts.addAll(allLayouts);
        layoutList.setData(availableLayouts, false, false, false, false, true, false);
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

    public static class LayoutListItem extends DashboardListItem {
        public final PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata metadata;

        public LayoutListItem(long id, String name, int color, PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata metadata) {
            super(id, name, color);
            this.metadata = metadata;
        }
    }
}
