package org.mtr.widget;

import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiAnimation;
import org.mtr.tool.GuiHelper;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public final class TabGroupWidget extends ClickableWidgetBase {

	@Getter
	private int selectedIndex = -1;

	private final ButtonGroupWidget buttonGroup;
	private final IntConsumer onChangeTab;
	private final GuiAnimation guiAnimation1 = new GuiAnimation();
	private final GuiAnimation guiAnimation2 = new GuiAnimation();

	private static final int ANIMATION_DURATION = 200;

	public TabGroupWidget(int minWidth, IntConsumer onChangeTab, String... messages) {
		this.onChangeTab = onChangeTab;
		buttonGroup = new ButtonGroupWidget(minWidth, this::selectTabInternal, messages);
		setDimensions(buttonGroup.getWidth(), buttonGroup.getHeight());
	}

	@Override
	public void init(Consumer<ClickableWidgetBase> addDrawableChild) {
		addDrawableChild.accept(buttonGroup);
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		buttonGroup.active = active;
		buttonGroup.visible = visible;
		setDimensions(buttonGroup.getWidth(), buttonGroup.getHeight());
		buttonGroup.setPosition(getX(), getY());
		buttonGroup.renderWidget(context, mouseX, mouseY, delta);

		final MatrixStack matrixStack = context.getMatrices();
		final Drawing drawing = new Drawing(matrixStack, RenderLayer.getGui());

		// Handle animation
		guiAnimation1.tick();
		guiAnimation2.tick();

		// Draw the widget background
		drawing.setVerticesWH(getX(), getY() + height - 1, width, 1).setColor(GuiHelper.BACKGROUND_ACCENT_COLOR).draw();

		// Draw the selected tab indicator
		drawing.setVertices(getX() + guiAnimation1.getCurrentValue(), getY() + height - 1, getX() + guiAnimation2.getCurrentValue(), getY() + height).setColor(GuiHelper.WHITE_COLOR).draw();

		// Set the selected tab indicator position
		for (int i = 0; i < buttonGroup.buttons.length; i++) {
			final boolean isSelected = i == selectedIndex;
			final BetterButtonWidget button = buttonGroup.buttons[i];
			if (isSelected) {
				guiAnimation1.animate(button.getX(), ANIMATION_DURATION);
				guiAnimation2.animate(button.getX() + button.getWidth(), ANIMATION_DURATION);
				button.setTextColor(GuiHelper.WHITE_COLOR);
			} else {
				button.setTextColor(GuiHelper.LIGHT_GRAY_COLOR);
			}
		}
	}

	public void selectTab(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	private void selectTabInternal(int selectedIndex) {
		if (active && selectedIndex != this.selectedIndex && selectedIndex >= 0 && selectedIndex < buttonGroup.buttons.length) {
			this.selectedIndex = selectedIndex;
			onChangeTab.accept(selectedIndex);
		}
	}
}
