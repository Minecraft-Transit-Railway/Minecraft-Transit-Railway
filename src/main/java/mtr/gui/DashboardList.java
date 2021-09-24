package mtr.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.*;
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

	private final RegisterButton registerButton;
	private final AddChild addChild;
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

	public <T> DashboardList(RegisterButton registerButton, AddChild addChild, OnClick onFind, OnClick onDrawArea, OnClick onEdit, Runnable onSort, OnClick onAdd, OnClick onDelete, GetList<T> getList, Supplier<String> getSearch, Consumer<String> setSearch) {
		this.registerButton = registerButton;
		this.addChild = addChild;
		this.getSearch = getSearch;
		this.setSearch = setSearch;
		textFieldSearch = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		buttonPrevPage = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_left.png"), 20, 40, button -> setPage(page - 1));
		buttonNextPage = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_right.png"), 20, 40, button -> setPage(page + 1));
		buttonFind = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_find.png"), 20, 40, button -> onClick(onFind));
		buttonDrawArea = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_draw_area.png"), 20, 40, button -> onClick(onDrawArea));
		buttonEdit = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_edit.png"), 20, 40, button -> onClick(onEdit));
		buttonUp = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_up.png"), 20, 40, button -> {
			onUp(getList);
			onSort.run();
		});
		buttonDown = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_down.png"), 20, 40, button -> {
			onDown(getList);
			onSort.run();
		});
		buttonAdd = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_add.png"), 20, 40, button -> onClick(onAdd));
		buttonDelete = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_delete.png"), 20, 40, button -> onClick(onDelete));
	}

	public void init() {
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

		registerButton.registerButton(buttonPrevPage);
		registerButton.registerButton(buttonNextPage);

		registerButton.registerButton(buttonFind);
		registerButton.registerButton(buttonDrawArea);
		registerButton.registerButton(buttonEdit);
		registerButton.registerButton(buttonUp);
		registerButton.registerButton(buttonDown);
		registerButton.registerButton(buttonAdd);
		registerButton.registerButton(buttonDelete);

		addChild.addChild(textFieldSearch);
	}

	public void tick() {
		textFieldSearch.tick();
		buttonPrevPage.x = x;
		buttonNextPage.x = x + SQUARE_SIZE * 3;
		textFieldSearch.x = x + SQUARE_SIZE * 4 + TEXT_FIELD_PADDING / 2;

		final String text = textFieldSearch.getText();
		textFieldSearch.setSuggestion(text.isEmpty() ? new TranslatableText("gui.mtr.search").getString() : "");
		dataFiltered.clear();
		for (int i = 0; i < dataSorted.size(); i++) {
			if (dataSorted.get(i).name.toLowerCase().contains(text.toLowerCase())) {
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
		this.hasDrawArea = hasDrawArea;
		this.hasEdit = hasEdit;
		this.hasSort = hasSort;
		this.hasAdd = hasAdd;
		this.hasDelete = hasDelete;
	}

	public void render(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY, float delta) {
		DrawableHelper.drawCenteredText(matrices, textRenderer, String.format("%s/%s", page + 1, totalPages), x + SQUARE_SIZE * 2, y + TEXT_PADDING + TEXT_FIELD_PADDING / 2, ARGB_WHITE);
		textFieldSearch.render(matrices, mouseX, mouseY, delta);
		final int itemsToShow = itemsToShow();
		for (int i = 0; i < itemsToShow; i++) {
			if (i + itemsToShow * page < dataFiltered.size()) {
				final int drawY = SQUARE_SIZE * i + TEXT_PADDING + TOP_OFFSET;
				final List<Integer> sortedKeys = new ArrayList<>(dataFiltered.keySet());
				Collections.sort(sortedKeys);
				final NameColorDataBase data = dataFiltered.get(sortedKeys.get(i + itemsToShow * page));

				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				RenderSystem.enableBlend();
				RenderSystem.disableTexture();
				RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
				buffer.begin(7, VertexFormats.POSITION_COLOR);
				IDrawing.drawRectangle(buffer, x + TEXT_PADDING, y + drawY, x + TEXT_PADDING + TEXT_HEIGHT, y + drawY + TEXT_HEIGHT, ARGB_BLACK + data.color);
				tessellator.draw();
				RenderSystem.enableTexture();
				RenderSystem.disableBlend();

				final String drawString = IGui.formatStationName(data.name);
				final int textStart = TEXT_PADDING * 2 + TEXT_HEIGHT;
				final int textWidth = textRenderer.getWidth(drawString);
				final int availableSpace = width - textStart;
				matrices.push();
				matrices.translate(x + textStart, 0, 0);
				if (textWidth > availableSpace) {
					matrices.scale((float) availableSpace / textWidth, 1, 1);
				}
				textRenderer.draw(matrices, drawString, 0, y + drawY, ARGB_WHITE);
				matrices.pop();
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

	private void setPage(int newPage) {
		page = MathHelper.clamp(newPage, 0, totalPages - 1);
		buttonPrevPage.visible = page > 0;
		buttonNextPage.visible = page < totalPages - 1;
	}

	private void onClick(OnClick onClick) {
		final List<Integer> sortedKeys = new ArrayList<>(dataFiltered.keySet());
		Collections.sort(sortedKeys);

		final int sortedIndex = hoverIndex + itemsToShow() * page;
		if (sortedIndex >= 0 && sortedIndex < sortedKeys.size()) {
			final int index = sortedKeys.get(sortedIndex);
			if (index >= 0 && index < dataSorted.size()) {
				onClick.onClick(dataSorted.get(index), index);
			}
		}
	}

	private <T> void onUp(GetList<T> getList) {
		if (textFieldSearch.getText().isEmpty()) {
			final int index = hoverIndex + itemsToShow() * page;
			final List<T> list = getList.getList();
			final T aboveItem = list.get(index - 1);
			final T thisItem = list.get(index);
			list.set(index - 1, thisItem);
			list.set(index, aboveItem);
		}
	}

	private <T> void onDown(GetList<T> getList) {
		if (textFieldSearch.getText().isEmpty()) {
			final int index = hoverIndex + itemsToShow() * page;
			final List<T> list = getList.getList();
			final T thisItem = list.get(index);
			final T belowItem = list.get(index + 1);
			list.set(index, belowItem);
			list.set(index + 1, thisItem);
		}
	}

	private int itemsToShow() {
		return (height - TOP_OFFSET) / SQUARE_SIZE;
	}

	@FunctionalInterface
	public interface RegisterButton {
		void registerButton(ClickableWidget button);
	}

	@FunctionalInterface
	public interface AddChild {
		void addChild(TextFieldWidget textField);
	}

	@FunctionalInterface
	public interface OnClick {
		void onClick(NameColorDataBase data, int index);
	}

	@FunctionalInterface
	public interface GetList<T> {
		List<T> getList();
	}
}
