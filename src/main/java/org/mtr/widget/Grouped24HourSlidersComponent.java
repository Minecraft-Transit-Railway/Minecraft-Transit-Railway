package org.mtr.widget;

import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMatrixStack;
import kotlin.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.mtr.core.tool.Utilities;
import org.mtr.tool.GuiHelper;

import java.awt.*;
import java.util.function.IntFunction;

public final class Grouped24HourSlidersComponent extends UIContainer {

	private boolean isDragging;
	private boolean previousShiftPressed;

	private final int max;
	private final IntFunction<String> formatText;

	private final UIContainer column2;
	private final NumberInputComponent[] numberInputs = new NumberInputComponent[Utilities.HOURS_PER_DAY];
	private final UIText[] labels = new UIText[Utilities.HOURS_PER_DAY];

	public Grouped24HourSlidersComponent(int max, int textWidth, IntFunction<String> formatText) {
		this.max = max;
		this.formatText = formatText;
		setHeight(new PixelConstraint(GuiHelper.MINECRAFT_FONT_SIZE * Utilities.HOURS_PER_DAY));

		final UIContainer column1 = (UIContainer) new UIContainer()
			.setChildOf(this)
			.setWidth(new PixelConstraint(Minecraft.getInstance().font.width("88:88–88:88")))
			.setHeight(new PixelConstraint(GuiHelper.MINECRAFT_FONT_SIZE * Utilities.HOURS_PER_DAY));

		column2 = (UIContainer) new UIContainer()
			.setChildOf(this)
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new SubtractiveConstraint(new FillConstraint(true), new PixelConstraint(GuiHelper.DEFAULT_PADDING * 2)))
			.setHeight(new PixelConstraint(GuiHelper.MINECRAFT_FONT_SIZE * Utilities.HOURS_PER_DAY));

		final UIContainer column3 = (UIContainer) new UIContainer()
			.setChildOf(this)
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new PixelConstraint(textWidth))
			.setHeight(new PixelConstraint(GuiHelper.MINECRAFT_FONT_SIZE * Utilities.HOURS_PER_DAY));

		for (int i = 0; i < Utilities.HOURS_PER_DAY; i++) {
			final int y = i * GuiHelper.MINECRAFT_FONT_SIZE;

			new UIText(String.format("%1$02d:00–%1$02d:59   ", i), false)
				.setChildOf(column1)
				.setY(new PixelConstraint(y))
				.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

			numberInputs[i] = (NumberInputComponent) new NumberInputComponent(max)
				.setChildOf(column2)
				.setY(new PixelConstraint(y))
				.setWidth(new RelativeConstraint());

			labels[i] = (UIText) new UIText("", false)
				.setChildOf(column3)
				.setY(new PixelConstraint(y))
				.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));
		}

		column2.onMouseEnterRunnable(() -> {
			if (Screen.hasShiftDown()) {
				for (final NumberInputComponent numberInput : numberInputs) {
					numberInput.overrideHighlighted(true);
				}
			}
		});

		column2.onMouseLeaveRunnable(() -> {
			for (final NumberInputComponent numberInput : numberInputs) {
				numberInput.overrideHighlighted(false);
			}
		});

		column2.onMouseClickConsumer(clickEvent -> {
			isDragging = true;
			setValueFromPosition(clickEvent.getRelativeX());
		});

		column2.onMouseDragConsumer((x, y, mouseButton) -> {
			if (isDragging) {
				setValueFromPosition(x);
			}
		});

		column2.onMouseReleaseRunnable(() -> isDragging = false);
	}

	@Override
	public void draw(UMatrixStack matrixStack) {
		super.draw(matrixStack);

		for (int i = 0; i < Utilities.HOURS_PER_DAY; i++) {
			labels[i].setText(formatText.apply((int) numberInputs[i].getValue()));
		}

		final Pair<Float, Float> mousePosition = getMousePosition();
		final float mouseX = mousePosition.getFirst();
		final float mouseY = mousePosition.getSecond();
		final boolean shiftPressed = Screen.hasShiftDown();

		if (shiftPressed != previousShiftPressed) {
			for (final NumberInputComponent numberInput : numberInputs) {
				numberInput.overrideHighlighted(shiftPressed && mouseX >= column2.getLeft() && mouseX < column2.getRight() && mouseY >= column2.getTop() && mouseY < column2.getBottom());
			}
		}

		previousShiftPressed = shiftPressed;
	}

	public int getValues(int hour) {
		return (int) numberInputs[hour].getValue();
	}

	public void setValue(int hour, int value) {
		numberInputs[hour].setValue(value);
	}

	private void setValueFromPosition(float position) {
		if (Screen.hasShiftDown()) {
			final float value = (position - NumberInputComponent.SLIDER_HANDLE_WIDTH / 2F) / (column2.getWidth() - NumberInputComponent.SLIDER_HANDLE_WIDTH) * max;
			for (int j = 0; j < Utilities.HOURS_PER_DAY; j++) {
				if (numberInputs[j].getValue() != value) {
					numberInputs[j].setValue(value);
				}
			}
		}
	}
}
