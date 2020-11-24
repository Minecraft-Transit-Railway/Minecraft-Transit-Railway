package mtr.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import mtr.data.DataBase;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DashboardList implements IGui {

	public int x;
	public int y;
	public int width;
	public int height;

	private final TexturedButtonWidget buttonFind;
	private final TexturedButtonWidget buttonEdit;
	private final TexturedButtonWidget buttonUp;
	private final TexturedButtonWidget buttonDown;
	private final TexturedButtonWidget buttonAdd;
	private final TexturedButtonWidget buttonDelete;

	private final RegisterButton registerButton;

	private List<DataBase> dataSorted = new ArrayList<>();
	private int hoverIndex, scrollOffset;

	private boolean hasFind;
	private boolean hasEdit;
	private boolean hasSort;
	private boolean hasAdd;
	private boolean hasDelete;

	public <T> DashboardList(RegisterButton registerButton, OnClick onFind, OnClick onEdit, OnClick onAdd, OnClick onDelete, GetList<T> getList, Runnable onUpDownCallback) {
		this.registerButton = registerButton;
		buttonFind = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_find.png"), 20, 40, button -> onClick(onFind));
		buttonEdit = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_edit.png"), 20, 40, button -> onClick(onEdit));
		buttonUp = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_up.png"), 20, 40, button -> onUp(getList, onUpDownCallback));
		buttonDown = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_down.png"), 20, 40, button -> onDown(getList, onUpDownCallback));
		buttonAdd = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_add.png"), 20, 40, button -> onClick(onAdd));
		buttonDelete = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_delete.png"), 20, 40, button -> onClick(onDelete));
	}

	public void init() {
		buttonFind.visible = false;
		buttonEdit.visible = false;
		buttonUp.visible = false;
		buttonDown.visible = false;
		buttonAdd.visible = false;
		buttonDelete.visible = false;

		registerButton.registerButton(buttonFind);
		registerButton.registerButton(buttonEdit);
		registerButton.registerButton(buttonUp);
		registerButton.registerButton(buttonDown);
		registerButton.registerButton(buttonAdd);
		registerButton.registerButton(buttonDelete);
	}

	public void setData(Set<? extends DataBase> dataSet, boolean hasFind, boolean hasEdit, boolean hasSort, boolean hasAdd, boolean hasDelete) {
		List<? extends DataBase> dataList = new ArrayList<>(dataSet);
		Collections.sort(dataList);
		setData(dataList, hasFind, hasEdit, hasSort, hasAdd, hasDelete);
	}

	public void setData(List<? extends DataBase> dataList, boolean hasFind, boolean hasEdit, boolean hasSort, boolean hasAdd, boolean hasDelete) {
		dataSorted = new ArrayList<>(dataList);
		this.hasFind = hasFind;
		this.hasEdit = hasEdit;
		this.hasSort = hasSort;
		this.hasAdd = hasAdd;
		this.hasDelete = hasDelete;

		final int maxScrollOffset = dataSorted.size() - itemsToShow();
		if (scrollOffset > maxScrollOffset) {
			scrollOffset = Math.max(maxScrollOffset, 0);
		}
	}

	public void render(MatrixStack matrices, TextRenderer textRenderer) {
		for (int i = 0; i < itemsToShow(); i++) {
			if (i + scrollOffset < dataSorted.size()) {
				final int drawY = SQUARE_SIZE * i + TEXT_PADDING;
				final DataBase data = dataSorted.get(i + scrollOffset);

				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				RenderSystem.enableBlend();
				RenderSystem.disableTexture();
				RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
				buffer.begin(7, VertexFormats.POSITION_COLOR);
				IGui.drawRectangle(buffer, x + TEXT_PADDING, y + drawY, x + TEXT_PADDING + TEXT_HEIGHT, y + drawY + TEXT_HEIGHT, ARGB_BLACK + data.color);
				tessellator.draw();
				RenderSystem.enableTexture();
				RenderSystem.disableBlend();

				textRenderer.draw(matrices, IGui.formatStationName(data.name), x + TEXT_PADDING * 2 + TEXT_HEIGHT, y + drawY, ARGB_WHITE);
			}
		}
	}

	public void mouseMoved(double mouseX, double mouseY) {
		buttonFind.visible = false;
		buttonEdit.visible = false;
		buttonUp.visible = false;
		buttonDown.visible = false;
		buttonAdd.visible = false;
		buttonDelete.visible = false;

		if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + SQUARE_SIZE * itemsToShow()) {
			hoverIndex = ((int) mouseY - y) / SQUARE_SIZE;
			final int dataSize = dataSorted.size();
			if (hoverIndex < dataSize) {
				buttonFind.visible = hasFind;
				buttonEdit.visible = hasEdit;
				buttonUp.visible = hasSort;
				buttonDown.visible = hasSort;
				buttonAdd.visible = hasAdd;
				buttonDelete.visible = hasDelete;

				buttonUp.active = hoverIndex + scrollOffset > 0;
				buttonDown.active = hoverIndex + scrollOffset < dataSize - 1;

				IGui.setPositionAndWidth(buttonFind, x + width - SQUARE_SIZE * (1 + (hasDelete ? 1 : 0) + (hasAdd ? 1 : 0) + (hasSort ? 2 : 0) + (hasEdit ? 1 : 0)), y + hoverIndex * SQUARE_SIZE, SQUARE_SIZE);
				IGui.setPositionAndWidth(buttonEdit, x + width - SQUARE_SIZE * (1 + (hasDelete ? 1 : 0) + (hasAdd ? 1 : 0) + (hasSort ? 2 : 0)), y + hoverIndex * SQUARE_SIZE, SQUARE_SIZE);
				IGui.setPositionAndWidth(buttonUp, x + width - SQUARE_SIZE * (2 + (hasDelete ? 1 : 0) + (hasAdd ? 1 : 0)), y + hoverIndex * SQUARE_SIZE, SQUARE_SIZE);
				IGui.setPositionAndWidth(buttonDown, x + width - SQUARE_SIZE * (1 + (hasDelete ? 1 : 0) + (hasAdd ? 1 : 0)), y + hoverIndex * SQUARE_SIZE, SQUARE_SIZE);
				IGui.setPositionAndWidth(buttonAdd, x + width - SQUARE_SIZE * (1 + (hasDelete ? 1 : 0)), y + hoverIndex * SQUARE_SIZE, SQUARE_SIZE);
				IGui.setPositionAndWidth(buttonDelete, x + width - SQUARE_SIZE, y + hoverIndex * SQUARE_SIZE, SQUARE_SIZE);
			}
		}
	}

	public void mouseScrolled(double amount) {
		if (scrollOffset > 0 && amount > 0) {
			scrollOffset--;
		} else if (scrollOffset < dataSorted.size() - itemsToShow() && amount < 0) {
			scrollOffset++;
		}
	}

	private void onClick(OnClick onClick) {
		final int index = hoverIndex + scrollOffset;
		if (index < dataSorted.size()) {
			onClick.onClick(dataSorted.get(index), index);
		}
	}

	private <T> void onUp(GetList<T> getList, Runnable callback) {
		final int index = hoverIndex + scrollOffset;
		final List<T> list = getList.getList();
		final T aboveItem = list.get(index - 1);
		final T thisItem = list.get(index);
		list.set(index - 1, thisItem);
		list.set(index, aboveItem);
		callback.run();
	}

	private <T> void onDown(GetList<T> getList, Runnable callback) {
		final int index = hoverIndex + scrollOffset;
		final List<T> list = getList.getList();
		final T thisItem = list.get(index);
		final T belowItem = list.get(index + 1);
		list.set(index, belowItem);
		list.set(index + 1, thisItem);
		callback.run();
	}

	private int itemsToShow() {
		return height / SQUARE_SIZE;
	}

	@FunctionalInterface
	public interface RegisterButton {
		void registerButton(AbstractButtonWidget button);
	}

	@FunctionalInterface
	public interface OnClick {
		void onClick(DataBase data, int index);
	}

	@FunctionalInterface
	public interface GetList<T> {
		List<T> getList();
	}
}
