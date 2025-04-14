package org.mtr.mod.screen;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ButtonWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.PIDSLayoutData;
import org.mtr.mod.packet.PacketDeletePIDSLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PIDSDashboardScreen extends ScreenExtension implements IGui {

    private final DashboardList layoutList;
    private final ButtonWidgetExtension newLayoutButton;
    private final ArrayList<PIDSLayoutSelectorScreen.LayoutListItem> allLayouts;

    public PIDSDashboardScreen() {
        this.layoutList = new DashboardList(null, null, this::onEdit, null, null, this::onDelete, null, () -> MinecraftClientData.PIDS_LAYOUT_SEARCH, text -> MinecraftClientData.PIDS_LAYOUT_SEARCH = text);
        this.newLayoutButton = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.mtr.new_layout"), button -> MinecraftClient.getInstance().openScreen(new Screen(new PIDSLayoutEditScreen(""))));
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
        this.allLayouts = list;
        updateList();
    }

    @Override
    protected void init2() {
        super.init2();
        final int spareSpace = Math.max(0, width - SQUARE_SIZE * 4 - PANEL_WIDTH * 2);
        layoutList.x = SQUARE_SIZE * 3 + spareSpace + PANEL_WIDTH;
        layoutList.y = SQUARE_SIZE * 2 - TEXT_PADDING;
        layoutList.height = height - SQUARE_SIZE * 5 + TEXT_PADDING;
        layoutList.width = PANEL_WIDTH;
        layoutList.init(this::addChild);

        IDrawing.setPositionAndWidth(newLayoutButton, SQUARE_SIZE * 3 + spareSpace + PANEL_WIDTH, height - SQUARE_SIZE - TEXT_FIELD_PADDING, PANEL_WIDTH);
        newLayoutButton.setMessage2(new Text(TextHelper.translatable("gui.mtr.new_layout").data));
        addChild(new ClickableWidget(newLayoutButton));

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
    public boolean mouseScrolled2(double mouseX, double mouseY, double amount) {
        layoutList.mouseScrolled(mouseX, mouseY, amount);
        return super.mouseScrolled2(mouseX, mouseY, amount);
    }

    private void updateList() {
        final ObjectArrayList<DashboardListItem> availableLayouts = new ObjectArrayList<>();
        availableLayouts.addAll(allLayouts);
        layoutList.setData(availableLayouts, false, false, true, false, false, true);
    }

    private void onEdit(DashboardListItem item, int index) {
        // find item id in allLayouts
        String id = null;
        for (PIDSLayoutSelectorScreen.LayoutListItem listItem : allLayouts) {
            if (listItem.id == item.id) {
                id = listItem.metadata.id;
                break;
            }
        }
        if (id == null) {
            return;
        }
        final String layout = InitClient.pidsLayoutCache.getData(id);
        if (layout != null) {
            MinecraftClient.getInstance().openScreen(new Screen(new PIDSLayoutEditScreen(layout)));
        } else {
            InitClient.pidsLayoutCache.setEditPending(id);
        }
    }

    private void onDelete(DashboardListItem item, int index) {
        // find item id in allLayouts
        String id = null;
        PIDSLayoutSelectorScreen.LayoutListItem entry = null;
        for (PIDSLayoutSelectorScreen.LayoutListItem listItem : allLayouts) {
            if (listItem.id == item.id) {
                id = listItem.metadata.id;
                entry = listItem;
                break;
            }
        }
        if (id == null) {
            return;
        }
        final String toDelete = id;
        MinecraftClient.getInstance().openScreen(new Screen(new DeleteConfirmationScreen(() -> {
            InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketDeletePIDSLayout(toDelete));
            InitClient.pidsLayoutCache.removeLayout(toDelete);
            MinecraftClient.getInstance().openScreen(new Screen(new PIDSDashboardScreen()));
        }, entry.metadata.name, null)));
    }
}
