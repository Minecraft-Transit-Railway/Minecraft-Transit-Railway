package mtr.gui;

import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;

public class WidgetBetterCheckbox extends CheckboxWidget {

	private final OnClick onClick;

	public WidgetBetterCheckbox(int x, int y, int width, int height, Text text, OnClick onClick) {
		super(x, y, width, height, text, false);
		this.onClick = onClick;
	}

	@Override
	public void onPress() {
		super.onPress();
		onClick.onClick(isChecked());
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
