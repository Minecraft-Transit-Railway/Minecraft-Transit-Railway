package mtr.gui;

import mtr.data.IGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class WidgetBetterCheckbox extends CheckboxWidget implements IGui {

	private final OnClick onClick;

	public WidgetBetterCheckbox(int x, int y, int width, int height, Text text, OnClick onClick) {
		super(x, y, width, height, text, false, false);
		this.onClick = onClick;
	}

	@Override
	public void onPress() {
		super.onPress();
		onClick.onClick(isChecked());
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		super.renderButton(matrices, mouseX, mouseY, delta);
		drawTextWithShadow(matrices, MinecraftClient.getInstance().textRenderer, getMessage(), x + 24, y + (height - 8) / 2, ARGB_WHITE);
	}

	public void setChecked(boolean checked) {
		if (checked != isChecked()) {
			super.onPress();
		}
	}

	@FunctionalInterface
	public interface OnClick {
		void onClick(boolean checked);
	}
}
