package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.gui.components.Button;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DashboardListSelectorScreen extends ScreenMapper implements IGui {

	private final DashboardList availableList;
	private final DashboardList selectedList;
	private final Button buttonDone;

	private final ScreenMapper previousScreen;
	private final Runnable onClose;
	private final List<NameColorDataBase> allData;
	private final Collection<Long> selectedIds;
	private final boolean isSingleSelect;
	private final boolean canRepeat;

	public DashboardListSelectorScreen(Runnable onClose, List<NameColorDataBase> allData, Collection<Long> selectedIds, boolean isSingleSelect, boolean canRepeat) {
		this(null, onClose, allData, selectedIds, isSingleSelect, canRepeat);
	}

	public DashboardListSelectorScreen(ScreenMapper previousScreen, List<NameColorDataBase> allData, Collection<Long> selectedIds, boolean isSingleSelect, boolean canRepeat) {
		this(previousScreen, null, allData, selectedIds, isSingleSelect, canRepeat);
	}

	private DashboardListSelectorScreen(ScreenMapper previousScreen, Runnable onClose, List<NameColorDataBase> allData, Collection<Long> selectedIds, boolean isSingleSelect, boolean canRepeat) {
		super(Text.literal(""));
		this.previousScreen = previousScreen;
		this.onClose = onClose;
		this.allData = allData;
		this.selectedIds = selectedIds;
		this.isSingleSelect = isSingleSelect;
		this.canRepeat = canRepeat;

		availableList = new DashboardList(null, null, null, null, this::onAdd, null, null, () -> ClientData.ROUTES_PLATFORMS_SEARCH, text -> ClientData.ROUTES_PLATFORMS_SEARCH = text);
		selectedList = new DashboardList(null, null, null, this::updateList, null, this::onDelete, () -> selectedIds instanceof ArrayList ? (List<Long>) selectedIds : new ArrayList<>(), () -> ClientData.ROUTES_PLATFORMS_SELECTED_SEARCH, text -> ClientData.ROUTES_PLATFORMS_SELECTED_SEARCH = text);
		buttonDone = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.done"), button -> onClose());
	}

	@Override
	protected void init() {
		super.init();
		availableList.x = width / 2 - PANEL_WIDTH - SQUARE_SIZE;
		selectedList.x = width / 2 + SQUARE_SIZE;
		availableList.y = selectedList.y = SQUARE_SIZE * 2;
		availableList.height = selectedList.height = height - SQUARE_SIZE * 5;
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
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			availableList.render(matrices, font);
			selectedList.render(matrices, font);
			super.render(matrices, mouseX, mouseY, delta);
			drawCenteredString(matrices, font, Text.translatable("gui.mtr.available"), width / 2 - PANEL_WIDTH / 2 - SQUARE_SIZE, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			drawCenteredString(matrices, font, Text.translatable("gui.mtr.selected"), width / 2 + PANEL_WIDTH / 2 + SQUARE_SIZE, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		availableList.mouseMoved(mouseX, mouseY);
		selectedList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		availableList.mouseScrolled(mouseX, mouseY, amount);
		selectedList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public void onClose() {
		super.onClose();
		if (onClose != null) {
			onClose.run();
		}
		if (minecraft != null && previousScreen != null) {
			UtilitiesClient.setScreen(minecraft, previousScreen);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void updateList() {
		final List<NameColorDataBase> availableData = new ArrayList<>();
		for (final NameColorDataBase data : allData) {
			if (canRepeat || !selectedIds.contains(data.id)) {
				availableData.add(data);
			}
		}

		final List<NameColorDataBase> selectedData = new ArrayList<>();
		for (final long selectedId : selectedIds) {
			allData.stream().filter(data -> data.id == selectedId).findFirst().ifPresent(selectedData::add);
		}

		availableList.setData(availableData, false, false, false, false, true, false);
		selectedList.setData(selectedData, false, false, false, canRepeat, false, true);
	}

	private void onAdd(NameColorDataBase data, int index) {
		if (isSingleSelect) {
			selectedIds.clear();
		}
		selectedIds.add(data.id);
		updateList();
	}

	private void onDelete(NameColorDataBase data, int index) {
		if (canRepeat && selectedIds instanceof ArrayList) {
			((ArrayList<Long>) selectedIds).remove(index);
		} else {
			selectedIds.remove(data.id);
		}
		updateList();
	}
}
