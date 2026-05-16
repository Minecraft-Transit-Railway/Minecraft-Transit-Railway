package org.mtr.screen;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.jspecify.annotations.Nullable;
import org.mtr.data.IGui;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.ListComponent;
import org.mtr.widget.ListItem;

/**
 * Elementa selector for dashboard-style ID/name/color entries.
 */
public class DashboardListSelectorScreen extends ListSelectorScreen<DashboardListItem, DashboardListItem> implements IGui {

	protected final ObjectImmutableList<DashboardListItem> allData;
	protected final LongCollection selectedIds;
	@Nullable
	private final Runnable onClose;

	public DashboardListSelectorScreen(ObjectImmutableList<DashboardListItem> allData, LongCollection selectedIds, boolean isSingleSelect, boolean canRepeat, @Nullable ScreenBase previousScreenLegacy) {
		this(null, allData, selectedIds, isSingleSelect, canRepeat, previousScreenLegacy);
	}

	@SuppressWarnings("deprecation")
	public DashboardListSelectorScreen(@Nullable Runnable onClose, ObjectImmutableList<DashboardListItem> allData, LongCollection selectedIds, boolean isSingleSelect, boolean canRepeat, @Nullable ScreenBase previousScreenLegacy) {
		super(!isSingleSelect, canRepeat, canRepeat, selectedData -> {
		}, previousScreenLegacy);
		this.allData = allData;
		this.selectedIds = selectedIds;
		this.onClose = onClose;
		setAvailableList(allData);
		for (final long selectedId : selectedIds) {
			allData.stream().filter(data -> data.id == selectedId).findFirst().ifPresent(this::selectData);
		}
		onSelectionChanged();
	}

	public DashboardListSelectorScreen(ObjectImmutableList<DashboardListItem> allData, LongCollection selectedIds, boolean isSingleSelect, boolean canRepeat, @Nullable WindowBase previousScreen) {
		this(null, allData, selectedIds, isSingleSelect, canRepeat, previousScreen);
	}

	public DashboardListSelectorScreen(@Nullable Runnable onClose, ObjectImmutableList<DashboardListItem> allData, LongCollection selectedIds, boolean isSingleSelect, boolean canRepeat, @Nullable WindowBase previousScreen) {
		super(!isSingleSelect, canRepeat, canRepeat, selectedData -> {
		}, previousScreen);
		this.allData = allData;
		this.selectedIds = selectedIds;
		this.onClose = onClose;
		setAvailableList(allData);
		for (final long selectedId : selectedIds) {
			allData.stream().filter(data -> data.id == selectedId).findFirst().ifPresent(this::selectData);
		}
		onSelectionChanged();
	}

	@Override
	protected void onSelectionChanged() {
		updateSelectedIds(selectedIds, selectedData);
	}

	@Override
	public void onScreenClose() {
		updateSelectedIds(selectedIds, selectedData);
		if (onClose != null) {
			onClose.run();
		}
		super.onScreenClose();
	}

	@Override
	protected void setData(ListComponent<DashboardListItem> listComponent, ObjectCollection<DashboardListItem> dataList, ObjectArrayList<ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<DashboardListItem>>> actions) {
		final ObjectArrayList<ListItem<DashboardListItem>> listItems = new ObjectArrayList<>();
		for (final DashboardListItem dashboardListItem : dataList) {
			listItems.add(ListItem.createChild(
				(drawing, x, y) -> drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING, y + GuiHelper.DEFAULT_PADDING, GuiHelper.MINECRAFT_FONT_SIZE, GuiHelper.MINECRAFT_FONT_SIZE).setColor(ColorHelper.fullAlpha(dashboardListItem.getColor(true))).draw(),
				null,
				GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
				dashboardListItem,
				dashboardListItem.getName(true),
				actions
			));
		}
		listComponent.setData(listItems);
	}

	private static void updateSelectedIds(LongCollection selectedIds, ObjectArrayList<DashboardListItem> selectedData) {
		selectedIds.clear();
		selectedData.forEach(selectedItem -> selectedIds.add(selectedItem.id));
	}
}
