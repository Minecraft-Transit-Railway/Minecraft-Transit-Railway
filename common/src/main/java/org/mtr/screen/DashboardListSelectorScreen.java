package org.mtr.screen;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;

import javax.annotation.Nullable;

public class DashboardListSelectorScreen extends ScreenBase implements IGui {

	protected final ObjectImmutableList<DashboardListItem> allData;
	protected final LongCollection selectedIds;

	protected final DashboardList availableList;
	protected final DashboardList selectedList;
	protected final ButtonWidget buttonDone;

	private final Runnable onClose;
	private final boolean isSingleSelect;
	private final boolean canRepeat;

	public DashboardListSelectorScreen(ObjectImmutableList<DashboardListItem> allData, LongCollection selectedIds, boolean isSingleSelect, boolean canRepeat, @Nullable Screen previousScreen) {
		this(null, allData, selectedIds, isSingleSelect, canRepeat, previousScreen);
	}

	public DashboardListSelectorScreen(@Nullable Runnable onClose, ObjectImmutableList<DashboardListItem> allData, LongCollection selectedIds, boolean isSingleSelect, boolean canRepeat, @Nullable Screen previousScreen) {
		super(previousScreen);
		this.onClose = onClose;
		this.allData = allData;
		this.selectedIds = selectedIds;
		this.isSingleSelect = isSingleSelect;
		this.canRepeat = canRepeat;

		availableList = new DashboardList(null, null, null, null, this::onAdd, null, null, () -> MinecraftClientData.ROUTES_PLATFORMS_SEARCH, text -> MinecraftClientData.ROUTES_PLATFORMS_SEARCH = text);
		selectedList = new DashboardList(null, null, null, this::updateList, null, this::onDelete, () -> selectedIds instanceof LongList ? (LongList) selectedIds : new LongArrayList(), () -> MinecraftClientData.ROUTES_PLATFORMS_SELECTED_SEARCH, text -> MinecraftClientData.ROUTES_PLATFORMS_SELECTED_SEARCH = text);
		buttonDone = ButtonWidget.builder(Text.translatable("gui.done"), button -> close()).build();
	}

	@Override
	protected void init() {
		super.init();
		availableList.x = width / 2 - PANEL_WIDTH - SQUARE_SIZE;
		selectedList.x = width / 2 + SQUARE_SIZE;
		availableList.y = selectedList.y = SQUARE_SIZE * 2 - TEXT_PADDING;
		availableList.height = selectedList.height = height - SQUARE_SIZE * 5 + TEXT_PADDING;
		availableList.width = selectedList.width = PANEL_WIDTH;
		availableList.init(this::addDrawableChild);
		selectedList.init(this::addDrawableChild);
		IDrawing.setPositionAndWidth(buttonDone, (width - PANEL_WIDTH) / 2, height - SQUARE_SIZE * 2, PANEL_WIDTH);
		addDrawableChild(buttonDone);
		updateList();
	}

	@Override
	public void tick() {
		availableList.tick();
		selectedList.tick();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		availableList.render(context);
		selectedList.render(context, false);
		super.render(context, mouseX, mouseY, delta);
		renderAdditional(context, mouseX, mouseY, delta);
	}

	public void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, TranslationProvider.GUI_MTR_AVAILABLE.getMutableText(), width / 2 - PANEL_WIDTH / 2 - SQUARE_SIZE, SQUARE_SIZE, ARGB_WHITE);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, TranslationProvider.GUI_MTR_SELECTED.getMutableText(), width / 2 + PANEL_WIDTH / 2 + SQUARE_SIZE, SQUARE_SIZE, ARGB_WHITE);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		availableList.mouseMoved(mouseX, mouseY);
		selectedList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		availableList.mouseScrolled(mouseX, mouseY, verticalAmount);
		selectedList.mouseScrolled(mouseX, mouseY, verticalAmount);
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public void close() {
		super.close();
		if (onClose != null) {
			onClose.run();
		}
	}

	protected void updateList() {
		final ObjectArrayList<DashboardListItem> availableData = new ObjectArrayList<>();
		for (final DashboardListItem dashboardListItem : allData) {
			if (canRepeat || !selectedIds.contains(dashboardListItem.id)) {
				availableData.add(dashboardListItem);
			}
		}

		final ObjectArrayList<DashboardListItem> selectedData = new ObjectArrayList<>();
		for (final long selectedId : selectedIds) {
			allData.stream().filter(data -> data.id == selectedId).findFirst().ifPresent(selectedData::add);
		}

		availableList.setData(availableData, false, false, false, false, true, false);
		selectedList.setData(selectedData, false, false, false, canRepeat, false, true);
	}

	private void onAdd(DashboardListItem dashboardListItem, int index) {
		if (isSingleSelect) {
			selectedIds.clear();
		}
		selectedIds.add(dashboardListItem.id);
		updateList();
	}

	private void onDelete(DashboardListItem dashboardListItem, int index) {
		if (canRepeat && selectedIds instanceof LongList) {
			((LongList) selectedIds).removeLong(index);
		} else {
			selectedIds.rem(dashboardListItem.id);
		}
		updateList();
	}
}
