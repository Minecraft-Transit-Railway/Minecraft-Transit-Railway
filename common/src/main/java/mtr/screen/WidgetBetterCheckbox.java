package mtr.screen;

import mtr.data.IGui;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;

public class WidgetBetterCheckbox extends Checkbox implements IGui {

	private final OnClick onClick;

	public WidgetBetterCheckbox(int x, int y, int width, int height, Component text, OnClick onClick) {
		super(x, y, width, height, text, false, false);
		this.onClick = onClick;
	}

	@Override
	public void onPress() {
		super.onPress();
		onClick.onClick(selected());
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		super.render(guiGraphics, mouseX, mouseY, delta);
		if (visible) {
			guiGraphics.drawString(Minecraft.getInstance().font, getMessage(), UtilitiesClient.getWidgetX(this) + 24, UtilitiesClient.getWidgetY(this) + (height - 8) / 2, ARGB_WHITE);
		}
	}

	public void setChecked(boolean checked) {
		if (checked != selected()) {
			super.onPress();
		}
	}

	@FunctionalInterface
	public interface OnClick {
		void onClick(boolean checked);
	}
}
