package mtr.gui;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import mtr.MTR;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class WidgetNameColor extends WPlainPanel implements IGui {

	private final String name;
	private final int color;
	private final WidgetScrollableButton button1, button2, buttonDelete;

	public WidgetNameColor(int width, String name, int color, String button1Path, String button2Path) {
		this.name = name;
		this.color = color;

		button1 = new WidgetScrollableButton(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/" + button1Path + ".png")));
		if (button1Path != null) {
			add(button1, width - SQUARE_SIZE * 3, 0, SQUARE_SIZE, SQUARE_SIZE);
		}

		button2 = new WidgetScrollableButton(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/" + button2Path + ".png")));
		if (button2Path != null) {
			add(button2, width - SQUARE_SIZE * 2, 0, SQUARE_SIZE, SQUARE_SIZE);
		}

		buttonDelete = new WidgetScrollableButton(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/icon_delete.png")));
		add(buttonDelete, width - SQUARE_SIZE, 0, SQUARE_SIZE, SQUARE_SIZE);
	}

	@Override
	public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
		if (color >= 0) {
			ScreenDrawing.coloredRect(x + TEXT_PADDING, y + TEXT_PADDING, SQUARE_SIZE - TEXT_PADDING * 2, SQUARE_SIZE - TEXT_PADDING * 2, ARGB_BLACK + color);
		}
		ScreenDrawing.drawString(matrices, IGui.formatStationName(name), HorizontalAlignment.LEFT, x + SQUARE_SIZE, y + TEXT_PADDING, 0, WLabel.DEFAULT_TEXT_COLOR);
		if (isWithinBounds(mouseX, mouseY)) {
			super.paint(matrices, x, y, mouseX, mouseY);
		}
	}

	@Override
	public void onMouseScroll(int x, int y, double amount) {
		if (parent != null && parent.getParent() != null) {
			parent.getParent().onMouseScroll(x, y, amount);
		}
	}

	public void setOnClick(Runnable onButton1, Runnable onButton2, Runnable onDelete) {
		if (onButton1 == null) {
			button1.setEnabled(false);
		} else {
			button1.setOnClick(onButton1);
		}
		if (onButton2 == null) {
			button2.setEnabled(false);
		} else {
			button2.setOnClick(onButton2);
		}
		buttonDelete.setOnClick(onDelete);
	}

	private static class WidgetScrollableButton extends WButton {

		public WidgetScrollableButton(Icon icon) {
			super(icon);
		}

		@Override
		public void onMouseScroll(int x, int y, double amount) {
			if (parent != null) {
				parent.onMouseScroll(x, y, amount);
			}
		}
	}
}
