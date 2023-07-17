package mtr.screen;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DashboardList implements IGui {

	public int x;
	public int y;
	public int width;
	public int height;

	private final WidgetBetterTextField textFieldSearch;

	private final ImageButton buttonPrevPage;
	private final ImageButton buttonNextPage;

	private final ImageButton buttonFind;
	private final ImageButton buttonDrawArea;
	private final ImageButton buttonEdit;
	private final ImageButton buttonUp;
	private final ImageButton buttonDown;
	private final ImageButton buttonAdd;
	private final ImageButton buttonDelete;

	private final Supplier<String> getSearch;
	private final Consumer<String> setSearch;

	private List<NameColorDataBase> dataSorted = new ArrayList<>();
	private final Map<Integer, NameColorDataBase> dataFiltered = new HashMap<>();
	private int hoverIndex, page, totalPages;

	private boolean hasFind;
	private boolean hasDrawArea;
	private boolean hasEdit;
	private boolean hasSort;
	private boolean hasAdd;
	private boolean hasDelete;

	private static final int TOP_OFFSET = SQUARE_SIZE + TEXT_FIELD_PADDING;

	public <T> DashboardList(BiConsumer<NameColorDataBase, Integer> onFind, BiConsumer<NameColorDataBase, Integer> onDrawArea, BiConsumer<NameColorDataBase, Integer> onEdit, Runnable onSort, BiConsumer<NameColorDataBase, Integer> onAdd, BiConsumer<NameColorDataBase, Integer> onDelete, Supplier<List<T>> getList, Supplier<String> getSearch, Consumer<String> setSearch) {
		this(onFind, onDrawArea, onEdit, onSort, onAdd, onDelete, getList, getSearch, setSearch, true);
	}

	public <T> DashboardList(BiConsumer<NameColorDataBase, Integer> onFind, BiConsumer<NameColorDataBase, Integer> onDrawArea, BiConsumer<NameColorDataBase, Integer> onEdit, Runnable onSort, BiConsumer<NameColorDataBase, Integer> onAdd, BiConsumer<NameColorDataBase, Integer> onDelete, Supplier<List<T>> getList, Supplier<String> getSearch, Consumer<String> setSearch, boolean playSound) {
		this.getSearch = getSearch;
		this.setSearch = setSearch;
		textFieldSearch = new WidgetBetterTextField(Text.translatable("gui.mtr.search").getString());
		buttonPrevPage = new ImageButton(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new ResourceLocation("mtr:textures/gui/icon_left.png"), 20, 40, button -> setPage(page - 1));
		buttonNextPage = new ImageButton(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new ResourceLocation("mtr:textures/gui/icon_right.png"), 20, 40, button -> setPage(page + 1));
		buttonFind = new WidgetSilentImageButton(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new ResourceLocation("mtr:textures/gui/icon_find.png"), 20, 40, button -> onClick(onFind), playSound);
		buttonDrawArea = new ImageButton(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new ResourceLocation("mtr:textures/gui/icon_draw_area.png"), 20, 40, button -> onClick(onDrawArea));
		buttonEdit = new ImageButton(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new ResourceLocation("mtr:textures/gui/icon_edit.png"), 20, 40, button -> onClick(onEdit));
		buttonUp = new ImageButton(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new ResourceLocation("mtr:textures/gui/icon_up.png"), 20, 40, button -> {
			onUp(getList);
			onSort.run();
		});
		buttonDown = new ImageButton(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new ResourceLocation("mtr:textures/gui/icon_down.png"), 20, 40, button -> {
			onDown(getList);
			onSort.run();
		});
		buttonAdd = new ImageButton(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new ResourceLocation("mtr:textures/gui/icon_add.png"), 20, 40, button -> onClick(onAdd));
		buttonDelete = new ImageButton(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new ResourceLocation("mtr:textures/gui/icon_delete.png"), 20, 40, button -> onClick(onDelete));
	}

	public void init(Consumer<AbstractWidget> addDrawableChild) {
		IDrawing.setPositionAndWidth(buttonPrevPage, x, y + TEXT_FIELD_PADDING / 2, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonNextPage, x + SQUARE_SIZE * 3, y + TEXT_FIELD_PADDING / 2, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(textFieldSearch, x + SQUARE_SIZE * 4 + TEXT_FIELD_PADDING / 2, y + TEXT_FIELD_PADDING / 2, width - SQUARE_SIZE * 4 - TEXT_FIELD_PADDING);

		textFieldSearch.setResponder(setSearch);
		textFieldSearch.setValue(getSearch.get());

		buttonFind.visible = false;
		buttonDrawArea.visible = false;
		buttonEdit.visible = false;
		buttonUp.visible = false;
		buttonDown.visible = false;
		buttonAdd.visible = false;
		buttonDelete.visible = false;

		addDrawableChild.accept(buttonPrevPage);
		addDrawableChild.accept(buttonNextPage);

		addDrawableChild.accept(buttonFind);
		addDrawableChild.accept(buttonDrawArea);
		addDrawableChild.accept(buttonEdit);
		addDrawableChild.accept(buttonUp);
		addDrawableChild.accept(buttonDown);
		addDrawableChild.accept(buttonAdd);
		addDrawableChild.accept(buttonDelete);

		addDrawableChild.accept(textFieldSearch);
	}

	public void tick() {
		textFieldSearch.tick();
		UtilitiesClient.setWidgetX(buttonPrevPage, x);
		UtilitiesClient.setWidgetX(buttonNextPage, x + SQUARE_SIZE * 3);
		UtilitiesClient.setWidgetX(textFieldSearch, x + SQUARE_SIZE * 4 + TEXT_FIELD_PADDING / 2);

		final String text = textFieldSearch.getValue();
		dataFiltered.clear();
		for (int i = 0; i < dataSorted.size(); i++) {
			if (dataSorted.get(i).name.toLowerCase(Locale.ENGLISH).contains(text.toLowerCase(Locale.ENGLISH))) {
				dataFiltered.put(i, dataSorted.get(i));
			}
		}

		final int dataSize = dataFiltered.size();
		totalPages = dataSize == 0 ? 1 : (int) Math.ceil((double) dataSize / itemsToShow());
		setPage(page);
	}

	public void setData(Set<? extends NameColorDataBase> dataSet, boolean hasFind, boolean hasDrawArea, boolean hasEdit, boolean hasSort, boolean hasAdd, boolean hasDelete) {
		List<? extends NameColorDataBase> dataList = new ArrayList<>(dataSet);
		Collections.sort(dataList);
		setData(dataList, hasFind, hasDrawArea, hasEdit, hasSort, hasAdd, hasDelete);
	}

	public void setData(List<? extends NameColorDataBase> dataList, boolean hasFind, boolean hasDrawArea, boolean hasEdit, boolean hasSort, boolean hasAdd, boolean hasDelete) {
		dataSorted = new ArrayList<>(dataList);
		this.hasFind = hasFind;
		final boolean hasPermission = ClientData.hasPermission();
		this.hasDrawArea = hasPermission && hasDrawArea;
		this.hasEdit = hasPermission && hasEdit;
		this.hasSort = hasPermission && hasSort;
		this.hasAdd = hasPermission && hasAdd;
		this.hasDelete = hasPermission && hasDelete;
	}

	public void render(GuiGraphics guiGraphics, Font textRenderer) {
		guiGraphics.drawCenteredString(textRenderer, String.format("%s/%s", page + 1, totalPages), x + SQUARE_SIZE * 2, y + TEXT_PADDING + TEXT_FIELD_PADDING / 2, ARGB_WHITE);
		final int itemsToShow = itemsToShow();
		for (int i = 0; i < itemsToShow; i++) {
			if (i + itemsToShow * page < dataFiltered.size()) {
				final int drawY = SQUARE_SIZE * i + TEXT_PADDING + TOP_OFFSET;
				final List<Integer> sortedKeys = new ArrayList<>(dataFiltered.keySet());
				Collections.sort(sortedKeys);
				final NameColorDataBase data = dataFiltered.get(sortedKeys.get(i + itemsToShow * page));

				Tesselator tesselator = Tesselator.getInstance();
				BufferBuilder buffer = tesselator.getBuilder();
				UtilitiesClient.beginDrawingRectangle(buffer);
				IDrawing.drawRectangle(buffer, x + TEXT_PADDING, y + drawY, x + TEXT_PADDING + TEXT_HEIGHT, y + drawY + TEXT_HEIGHT, ARGB_BLACK | data.color);
				tesselator.end();
				UtilitiesClient.finishDrawingRectangle();

				final String drawString = IGui.formatStationName(data.name);
				final int textStart = TEXT_PADDING * 2 + TEXT_HEIGHT;
				final int textWidth = textRenderer.width(drawString);
				final int availableSpace = width - textStart;
				guiGraphics.pose().pushPose();
				guiGraphics.pose().translate(x + textStart, 0, 0);
				if (textWidth > availableSpace) {
					guiGraphics.pose().scale((float) availableSpace / textWidth, 1, 1);
				}
				guiGraphics.drawString(textRenderer, drawString, 0, y + drawY, ARGB_WHITE);
				guiGraphics.pose().popPose();
			}
		}
	}

	public void mouseMoved(double mouseX, double mouseY) {
		buttonFind.visible = false;
		buttonDrawArea.visible = false;
		buttonEdit.visible = false;
		buttonUp.visible = false;
		buttonDown.visible = false;
		buttonAdd.visible = false;
		buttonDelete.visible = false;

		if (mouseX >= x && mouseX < x + width && mouseY >= y + TOP_OFFSET && mouseY < y + TOP_OFFSET + SQUARE_SIZE * itemsToShow()) {
			hoverIndex = ((int) mouseY - y - TOP_OFFSET) / SQUARE_SIZE;
			final int dataSize = dataFiltered.size();
			final int itemsToShow = itemsToShow();
			final boolean hasSortFiltered = hasSort && dataSize == dataSorted.size();
			if (hoverIndex >= 0 && hoverIndex + page * itemsToShow < dataSize) {
				buttonFind.visible = hasFind;
				buttonDrawArea.visible = hasDrawArea;
				buttonEdit.visible = hasEdit;
				buttonUp.visible = hasSortFiltered;
				buttonDown.visible = hasSortFiltered;
				buttonAdd.visible = hasAdd;
				buttonDelete.visible = hasDelete;

				buttonUp.active = hoverIndex + itemsToShow * page > 0;
				buttonDown.active = hoverIndex + itemsToShow * page < dataSize - 1;

				final int renderOffset = y + hoverIndex * SQUARE_SIZE + TOP_OFFSET;
				IDrawing.setPositionAndWidth(buttonFind, x + width - SQUARE_SIZE * (1 + (hasDelete ? 1 : 0) + (hasAdd ? 1 : 0) + (hasSortFiltered ? 2 : 0) + (hasEdit ? 1 : 0) + (hasDrawArea ? 1 : 0)), renderOffset, SQUARE_SIZE);
				IDrawing.setPositionAndWidth(buttonDrawArea, x + width - SQUARE_SIZE * (1 + (hasDelete ? 1 : 0) + (hasAdd ? 1 : 0) + (hasSortFiltered ? 2 : 0) + (hasEdit ? 1 : 0)), renderOffset, SQUARE_SIZE);
				IDrawing.setPositionAndWidth(buttonEdit, x + width - SQUARE_SIZE * (1 + (hasDelete ? 1 : 0) + (hasAdd ? 1 : 0) + (hasSortFiltered ? 2 : 0)), renderOffset, SQUARE_SIZE);
				IDrawing.setPositionAndWidth(buttonUp, x + width - SQUARE_SIZE * (2 + (hasDelete ? 1 : 0) + (hasAdd ? 1 : 0)), renderOffset, SQUARE_SIZE);
				IDrawing.setPositionAndWidth(buttonDown, x + width - SQUARE_SIZE * (1 + (hasDelete ? 1 : 0) + (hasAdd ? 1 : 0)), renderOffset, SQUARE_SIZE);
				IDrawing.setPositionAndWidth(buttonAdd, x + width - SQUARE_SIZE * (1 + (hasDelete ? 1 : 0)), renderOffset, SQUARE_SIZE);
				IDrawing.setPositionAndWidth(buttonDelete, x + width - SQUARE_SIZE, renderOffset, SQUARE_SIZE);
			}
		}
	}

	public void mouseScrolled(double mouseX, double mouseY, double amount) {
		if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
			setPage(page + (int) Math.signum(-amount));
		}
	}

	public void clearSearch() {
		textFieldSearch.setValue("");
	}

	public int getHoverItemIndex() {
		final List<Integer> sortedKeys = new ArrayList<>(dataFiltered.keySet());
		Collections.sort(sortedKeys);
		final int sortedIndex = hoverIndex + itemsToShow() * page;
		if (sortedIndex >= 0 && sortedIndex < sortedKeys.size()) {
			return sortedKeys.get(sortedIndex);
		} else {
			return -1;
		}
	}

	private void setPage(int newPage) {
		page = Mth.clamp(newPage, 0, totalPages - 1);
		buttonPrevPage.visible = page > 0;
		buttonNextPage.visible = page < totalPages - 1;
	}

	private void onClick(BiConsumer<NameColorDataBase, Integer> onClick) {
		final int index = getHoverItemIndex();
		if (index >= 0 && index < dataSorted.size()) {
			onClick.accept(dataSorted.get(index), index);
		}
	}

	private <T> void onUp(Supplier<List<T>> getList) {
		if (textFieldSearch.getValue().isEmpty()) {
			final int index = hoverIndex + itemsToShow() * page;
			final List<T> list = getList.get();
			if (Screen.hasShiftDown()) {
				list.add(0, list.remove(index));
			} else {
				final T aboveItem = list.get(index - 1);
				final T thisItem = list.get(index);
				list.set(index - 1, thisItem);
				list.set(index, aboveItem);
			}
		}
	}

	private <T> void onDown(Supplier<List<T>> getList) {
		if (textFieldSearch.getValue().isEmpty()) {
			final int index = hoverIndex + itemsToShow() * page;
			final List<T> list = getList.get();
			if (Screen.hasShiftDown()) {
				list.add(list.remove(index));
			} else {
				final T thisItem = list.get(index);
				final T belowItem = list.get(index + 1);
				list.set(index, belowItem);
				list.set(index + 1, thisItem);
			}
		}
	}

	private int itemsToShow() {
		return (height - TOP_OFFSET) / SQUARE_SIZE;
	}
}
