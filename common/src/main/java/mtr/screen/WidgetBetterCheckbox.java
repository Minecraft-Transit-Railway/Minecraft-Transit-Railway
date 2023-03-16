package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.IGui;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
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
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
		if (visible) {
			drawString(matrices, Minecraft.getInstance().font, getMessage(), UtilitiesClient.getWidgetX(this) + 24, UtilitiesClient.getWidgetY(this) + (height - 8) / 2, ARGB_WHITE);
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
