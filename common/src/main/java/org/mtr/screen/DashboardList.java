package org.mtr.screen;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.widget.BetterTextFieldWidget;
import org.mtr.widget.BetterTexturedButtonWidget;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DashboardList implements IGui {

	public int x;
	public int y;
	public int width;
	public int height;

	private final TextFieldWidget textFieldSearch;

	private final TexturedButtonWidget buttonPrevPage;
	private final TexturedButtonWidget buttonNextPage;

	private final TexturedButtonWidget buttonFind;
	private final TexturedButtonWidget buttonDrawArea;
	private final TexturedButtonWidget buttonEdit;
	private final TexturedButtonWidget buttonUp;
	private final TexturedButtonWidget buttonDown;
	private final TexturedButtonWidget buttonAdd;
	private final TexturedButtonWidget buttonDelete;

	private final Supplier<String> getSearch;
	private final Consumer<String> setSearch;

	private ObjectArrayList<DashboardListItem> dataSorted = new ObjectArrayList<>();
	private final Int2ObjectAVLTreeMap<DashboardListItem> dataFiltered = new Int2ObjectAVLTreeMap<>();
	private int hoverIndex, page, totalPages;

	private boolean hasFind;
	private boolean hasDrawArea;
	private boolean hasEdit;
	private boolean hasSort;
	private boolean hasAdd;
	private boolean hasDelete;

	private static final int TOP_OFFSET = SQUARE_SIZE + TEXT_FIELD_PADDING;

	public <T> DashboardList(@Nullable Callback onFind, @Nullable Callback onDrawArea, @Nullable Callback onEdit, @Nullable Runnable onSort, @Nullable Callback onAdd, @Nullable Callback onDelete, @Nullable Supplier<List<T>> getList, Supplier<String> getSearch, Consumer<String> setSearch) {
		this(onFind, onDrawArea, onEdit, onSort, onAdd, onDelete, getList, getSearch, setSearch, true);
	}

	public <T> DashboardList(@Nullable Callback onFind, @Nullable Callback onDrawArea, @Nullable Callback onEdit, @Nullable Runnable onSort, @Nullable Callback onAdd, @Nullable Callback onDelete, @Nullable Supplier<List<T>> getList, Supplier<String> getSearch, Consumer<String> setSearch, boolean playSound) {
		this.getSearch = getSearch;
		this.setSearch = setSearch;
		textFieldSearch = new BetterTextFieldWidget(256, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_SEARCH.getString());
		buttonPrevPage = new BetterTexturedButtonWidget(Identifier.of("textures/gui/sprites/mtr/icon_left.png"), Identifier.of("textures/gui/sprites/mtr/icon_left_highlighted.png"), button -> setPage(page - 1), true);
		buttonNextPage = new BetterTexturedButtonWidget(Identifier.of("textures/gui/sprites/mtr/icon_right.png"), Identifier.of("textures/gui/sprites/mtr/icon_right_highlighted.png"), button -> setPage(page + 1), true);
		buttonFind = new BetterTexturedButtonWidget(Identifier.of("textures/gui/sprites/mtr/icon_find.png"), Identifier.of("textures/gui/sprites/mtr/icon_find_highlighted.png"), button -> onClick(onFind), playSound);
		buttonDrawArea = new BetterTexturedButtonWidget(Identifier.of("textures/gui/sprites/mtr/icon_draw_area.png"), Identifier.of("textures/gui/sprites/mtr/icon_draw_area_highlighted.png"), button -> onClick(onDrawArea), true);
		buttonEdit = new BetterTexturedButtonWidget(Identifier.of("textures/gui/sprites/mtr/icon_edit.png"), Identifier.of("textures/gui/sprites/mtr/icon_edit_highlighted.png"), button -> onClick(onEdit), true);
		buttonUp = new BetterTexturedButtonWidget(Identifier.of("textures/gui/sprites/mtr/icon_up.png"), Identifier.of("textures/gui/sprites/mtr/icon_up_highlighted.png"), button -> {
			if (getList != null) {
				onUp(getList);
			}
			if (onSort != null) {
				onSort.run();
			}
		}, true);
		buttonDown = new BetterTexturedButtonWidget(Identifier.of("textures/gui/sprites/mtr/icon_down.png"), Identifier.of("textures/gui/sprites/mtr/icon_down_highlighted.png"), button -> {
			if (getList != null) {
				onDown(getList);
			}
			if (onSort != null) {
				onSort.run();
			}
		}, true);
		buttonAdd = new BetterTexturedButtonWidget(Identifier.of("textures/gui/sprites/mtr/icon_add.png"), Identifier.of("textures/gui/sprites/mtr/icon_add_highlighted.png"), button -> onClick(onAdd), true);
		buttonDelete = new BetterTexturedButtonWidget(Identifier.of("textures/gui/sprites/mtr/icon_delete.png"), Identifier.of("textures/gui/sprites/mtr/icon_delete_highlighted.png"), button -> onClick(onDelete), true);
	}

	public void init(Consumer<ClickableWidget> addDrawableChild) {
		IDrawing.setPositionAndWidth(buttonPrevPage, x, y + TEXT_FIELD_PADDING / 2, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonNextPage, x + SQUARE_SIZE * 3, y + TEXT_FIELD_PADDING / 2, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(textFieldSearch, x + SQUARE_SIZE * 4 + TEXT_FIELD_PADDING / 2, y + TEXT_FIELD_PADDING / 2, width - SQUARE_SIZE * 4 - TEXT_FIELD_PADDING);

		textFieldSearch.setChangedListener(setSearch);
		textFieldSearch.setText(getSearch.get());

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
		buttonPrevPage.setX(x);
		buttonNextPage.setX(x + SQUARE_SIZE * 3);
		textFieldSearch.setX(x + SQUARE_SIZE * 4 + TEXT_FIELD_PADDING / 2);

		final String text = textFieldSearch.getText();
		dataFiltered.clear();
		for (int i = 0; i < dataSorted.size(); i++) {
			if (dataSorted.get(i).getName(true).toLowerCase(Locale.ENGLISH).contains(text.toLowerCase(Locale.ENGLISH))) {
				dataFiltered.put(i, dataSorted.get(i));
			}
		}

		final int dataSize = dataFiltered.size();
		totalPages = dataSize == 0 ? 1 : (int) Math.ceil((double) dataSize / itemsToShow());
		setPage(page);
	}

	public void setData(ObjectSet<DashboardListItem> dataSet, boolean hasFind, boolean hasDrawArea, boolean hasEdit, boolean hasSort, boolean hasAdd, boolean hasDelete) {
		ObjectArrayList<DashboardListItem> dataList = new ObjectArrayList<>(dataSet);
		Collections.sort(dataList);
		setData(dataList, hasFind, hasDrawArea, hasEdit, hasSort, hasAdd, hasDelete);
	}

	public void setData(ObjectArrayList<DashboardListItem> dataList, boolean hasFind, boolean hasDrawArea, boolean hasEdit, boolean hasSort, boolean hasAdd, boolean hasDelete) {
		dataSorted = new ObjectArrayList<>(dataList);
		this.hasFind = hasFind;
		final boolean hasPermission = MinecraftClientData.hasPermission();
		this.hasDrawArea = hasPermission && hasDrawArea;
		this.hasEdit = hasPermission && hasEdit;
		this.hasSort = hasPermission && hasSort;
		this.hasAdd = hasPermission && hasAdd;
		this.hasDelete = hasPermission && hasDelete;
	}

	public void render(DrawContext context) {
		render(context, true);
	}

	public void render(DrawContext context, boolean formatted) {
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		context.drawCenteredTextWithShadow(textRenderer, String.format("%s/%s", page + 1, totalPages), x + SQUARE_SIZE * 2, y + TEXT_PADDING + TEXT_FIELD_PADDING / 2, ARGB_WHITE);

		final int itemsToShow = itemsToShow();
		for (int i = 0; i < itemsToShow; i++) {
			if (i + itemsToShow * page < dataFiltered.size()) {
				final int drawY = SQUARE_SIZE * i + TEXT_PADDING + TOP_OFFSET;
				final IntArrayList sortedKeys = new IntArrayList(dataFiltered.keySet());
				Collections.sort(sortedKeys);
				final DashboardListItem data = dataFiltered.get(sortedKeys.getInt(i + itemsToShow * page));

				context.fill(x + TEXT_PADDING, y + drawY, x + TEXT_PADDING + TEXT_HEIGHT, y + drawY + TEXT_HEIGHT, ARGB_BLACK | data.getColor(formatted));

				final String drawString = IGui.formatStationName(data.getName(formatted));
				final int textStart = TEXT_PADDING * 2 + TEXT_HEIGHT;
				final int textWidth = textRenderer.getWidth(drawString);
				final int availableSpace = width - textStart;
				context.getMatrices().push();
				context.getMatrices().translate(x + textStart, 0, 0);
				if (textWidth > availableSpace) {
					context.getMatrices().scale((float) availableSpace / textWidth, 1, 1);
				}
				context.drawText(textRenderer, drawString, 0, y + drawY, ARGB_WHITE, false);
				context.getMatrices().pop();
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
		textFieldSearch.setText("");
	}

	public int getHoverItemIndex() {
		final IntArrayList sortedKeys = new IntArrayList(dataFiltered.keySet());
		Collections.sort(sortedKeys);
		final int sortedIndex = hoverIndex + itemsToShow() * page;
		if (sortedIndex >= 0 && sortedIndex < sortedKeys.size()) {
			return sortedKeys.getInt(sortedIndex);
		} else {
			return -1;
		}
	}

	private void setPage(int newPage) {
		page = MathHelper.clamp(newPage, 0, totalPages - 1);
		buttonPrevPage.visible = page > 0;
		buttonNextPage.visible = page < totalPages - 1;
	}

	private void onClick(@Nullable Callback onClick) {
		final int index = getHoverItemIndex();
		if (index >= 0 && index < dataSorted.size() && onClick != null) {
			onClick.accept(dataSorted.get(index), index);
		}
	}

	private <T> void onUp(Supplier<List<T>> getList) {
		if (textFieldSearch.getText().isEmpty()) {
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
		if (textFieldSearch.getText().isEmpty()) {
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

	@FunctionalInterface
	public interface Callback {
		void accept(DashboardListItem dashboardListItem, int index);
	}
}
