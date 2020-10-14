package mtr.gui;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import mtr.MTR;
import mtr.data.Station;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class WidgetStationName extends WPlainPanel implements IGui {

	private final String name;
	private final int color;
	private final WButton buttonFind, buttonEdit, buttonDelete;

	public WidgetStationName(int width, Station station) {
		this.name = station.name;
		this.color = station.color;

		buttonFind = new WButton(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/icon_find.png")));
		add(buttonFind, width - SQUARE_SIZE * 3, 0, SQUARE_SIZE, SQUARE_SIZE);

		buttonEdit = new WButton(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/icon_edit.png")));
		add(buttonEdit, width - SQUARE_SIZE * 2, 0, SQUARE_SIZE, SQUARE_SIZE);

		buttonDelete = new WButton(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/icon_delete.png")));
		add(buttonDelete, width - SQUARE_SIZE, 0, SQUARE_SIZE, SQUARE_SIZE);
	}

	@Override
	public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
		ScreenDrawing.coloredRect(x + TEXT_PADDING, y + TEXT_PADDING, SQUARE_SIZE - TEXT_PADDING * 2, SQUARE_SIZE - TEXT_PADDING * 2, ARGB_BLACK + color);
		ScreenDrawing.drawString(matrices, name, HorizontalAlignment.LEFT, x + SQUARE_SIZE, y + TEXT_PADDING, 0, WLabel.DEFAULT_TEXT_COLOR);
		if (isWithinBounds(mouseX, mouseY)) {
			super.paint(matrices, x, y, mouseX, mouseY);
		}
	}

	public void setOnClick(Runnable onFind, Runnable onEdit, Runnable onDelete) {
		buttonFind.setOnClick(onFind);
		buttonEdit.setOnClick(onEdit);
		buttonDelete.setOnClick(onDelete);
	}
}
