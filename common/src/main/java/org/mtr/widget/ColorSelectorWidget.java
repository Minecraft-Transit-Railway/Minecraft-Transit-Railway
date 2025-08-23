package org.mtr.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.apache.commons.lang3.StringUtils;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.screen.TextCase;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Locale;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public final class ColorSelectorWidget extends PopupWidgetBase {

	private float hue;
	private float saturation;
	private float brightness;
	private MouseZone hoverMouseZone = MouseZone.NONE;
	private MouseZone draggingMouseZone = MouseZone.NONE;

	private Color oldColor;
	private IntConsumer colorCallback;

	private final Runnable onDismiss;
	private final BetterTextFieldWidget colorTextField;
	private final BetterTextFieldWidget redTextField;
	private final BetterTextFieldWidget greenTextField;
	private final BetterTextFieldWidget blueTextField;

	private static final int CONTROLS_SIZE = 60;

	public ColorSelectorWidget(int minWidth, Runnable onDismiss, Runnable applyBlur) {
		super(Math.max(minWidth, GuiHelper.DEFAULT_PADDING * 3 + GuiHelper.DEFAULT_LINE_SIZE + CONTROLS_SIZE), applyBlur, Text.translatable("selectWorld.edit.save").getString(), TranslationProvider.GUI_MTR_COLOR_RANDOM.getString(), TranslationProvider.GUI_MTR_RESET.getString(), TranslationProvider.GUI_MTR_CLOSE.getString());
		this.onDismiss = onDismiss;
		colorTextField = new BetterTextFieldWidget(6, TextCase.UPPER, "[^\\dA-F]", TranslationProvider.GUI_MTR_COLOR.getString(), CONTROLS_SIZE, text -> textCallback(text, 16, (existingColor, component) -> setColor(new Color(component))));
		redTextField = new BetterTextFieldWidget(3, TextCase.DEFAULT, "\\D", TranslationProvider.GUI_MTR_COLOR_RED.getString(), CONTROLS_SIZE, text -> textCallback(text, 10, (existingColor, component) -> setColor(component, existingColor.getGreen(), existingColor.getBlue())));
		greenTextField = new BetterTextFieldWidget(3, TextCase.DEFAULT, "\\D", TranslationProvider.GUI_MTR_COLOR_GREEN.getString(), CONTROLS_SIZE, text -> textCallback(text, 10, (existingColor, component) -> setColor(existingColor.getRed(), component, existingColor.getBlue())));
		blueTextField = new BetterTextFieldWidget(3, TextCase.DEFAULT, "\\D", TranslationProvider.GUI_MTR_COLOR_BLUE.getString(), CONTROLS_SIZE, text -> textCallback(text, 10, (existingColor, component) -> setColor(existingColor.getRed(), existingColor.getGreen(), component)));
	}

	@Override
	public void init(Consumer<ClickableWidgetBase> addDrawableChild) {
		super.init(addDrawableChild);
		addDrawableChild.accept(colorTextField);
		addDrawableChild.accept(redTextField);
		addDrawableChild.accept(greenTextField);
		addDrawableChild.accept(blueTextField);
	}

	@Override
	protected void render(DrawContext context, int mouseX, int mouseY) {
		visible = colorCallback != null;

		final int controlsX = getX() + width - GuiHelper.DEFAULT_PADDING - CONTROLS_SIZE;
		colorTextField.setPosition(controlsX, getY() + GuiHelper.DEFAULT_PADDING);
		redTextField.setPosition(controlsX, getY() + GuiHelper.DEFAULT_PADDING * 2 + GuiHelper.DEFAULT_LINE_SIZE);
		greenTextField.setPosition(controlsX, getY() + GuiHelper.DEFAULT_PADDING * 3 + GuiHelper.DEFAULT_LINE_SIZE * 2);
		blueTextField.setPosition(controlsX, getY() + GuiHelper.DEFAULT_PADDING * 4 + GuiHelper.DEFAULT_LINE_SIZE * 3);

		final Drawing drawing = new Drawing(context.getMatrices(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui()));
		final int mainHeight = height - GuiHelper.DEFAULT_PADDING * 2 - GuiHelper.DEFAULT_LINE_SIZE;
		final int mainWidth = width - GuiHelper.DEFAULT_PADDING * 4 - GuiHelper.DEFAULT_LINE_SIZE - CONTROLS_SIZE;
		hoverMouseZone = MouseZone.NONE;

		// Draw the selected colour
		drawing.setVerticesWH(controlsX, getY() + GuiHelper.DEFAULT_PADDING * 5 + GuiHelper.DEFAULT_LINE_SIZE * 4, CONTROLS_SIZE, CONTROLS_SIZE).setColor(ColorHelper.fullAlpha(Color.HSBtoRGB(hue, saturation, brightness))).draw();

		// Draw hue bar
		final int hueBarX = controlsX - GuiHelper.DEFAULT_PADDING - GuiHelper.DEFAULT_LINE_SIZE;
		for (int drawHue = 0; drawHue < mainHeight; drawHue++) {
			final float currentHue = (float) drawHue / (mainHeight - 1);

			final int hueBarStartY = getY() + GuiHelper.DEFAULT_PADDING;
			final int hueBarY = hueBarStartY + drawHue;

			drawing.setVerticesWH(hueBarX, hueBarY, GuiHelper.DEFAULT_LINE_SIZE, 1).setColor(ColorHelper.fullAlpha(Color.HSBtoRGB(currentHue, 1, 1))).draw();

			final boolean isDragging = draggingMouseZone == MouseZone.HUE && Math.clamp(mouseY, hueBarStartY, hueBarStartY + mainHeight - 1) == hueBarY;
			final boolean isOver = mouseY == hueBarY && Utilities.isBetween(mouseX, hueBarX, hueBarX + GuiHelper.DEFAULT_LINE_SIZE - 1);
			if (isDragging || isOver) {
				if (isDragging) {
					hue = currentHue;
					updateTextFields();
				}
				hoverMouseZone = MouseZone.HUE;
			}
		}

		// Draw saturation and brightness rectangle
		for (int drawSaturation = 0; drawSaturation < mainWidth; drawSaturation++) {
			for (int drawBrightness = 0; drawBrightness < mainHeight; drawBrightness++) {
				final float currentSaturation = (float) drawSaturation / (mainWidth - 1);
				final float currentBrightness = (float) drawBrightness / (mainHeight - 1);

				final int saturationRectangleStartX = getX() + GuiHelper.DEFAULT_PADDING;
				final int saturationRectangleX = saturationRectangleStartX + drawSaturation;

				final int brightnessRectangleStartY = getY() + GuiHelper.DEFAULT_PADDING;
				final int brightnessRectangleY = getY() + height - GuiHelper.DEFAULT_LINE_SIZE - GuiHelper.DEFAULT_PADDING - drawBrightness - 1;

				drawing.setVerticesWH(saturationRectangleX, brightnessRectangleY, 1, 1).setColor(ColorHelper.fullAlpha(Color.HSBtoRGB(hue, currentSaturation, currentBrightness))).draw();

				final boolean isDragging = draggingMouseZone == MouseZone.SATURATION_BRIGHTNESS && Math.clamp(mouseX, saturationRectangleStartX, saturationRectangleStartX + mainWidth - 1) == saturationRectangleX && Math.clamp(mouseY, brightnessRectangleStartY, brightnessRectangleStartY + mainHeight - 1) == brightnessRectangleY;
				final boolean isOver = mouseX == saturationRectangleX && mouseY == brightnessRectangleY;
				if (isDragging || isOver) {
					if (isDragging) {
						saturation = currentSaturation;
						brightness = currentBrightness;
						updateTextFields();
					}
					hoverMouseZone = MouseZone.SATURATION_BRIGHTNESS;
				}
			}
		}

		final double selectedHuePosition = getY() + GuiHelper.DEFAULT_PADDING + hue * (mainHeight - 1);
		final double selectedSaturationPosition = getX() + GuiHelper.DEFAULT_PADDING + saturation * (mainWidth - 1);
		final double selectedBrightnessPosition = getY() + height - GuiHelper.DEFAULT_LINE_SIZE - GuiHelper.DEFAULT_PADDING - brightness * (mainHeight - 1) - 1;

		// Draw hue selector
		drawing.setVerticesWH(hueBarX, selectedHuePosition - 1, GuiHelper.DEFAULT_LINE_SIZE, 3).setColor(GuiHelper.BACKGROUND_COLOR).draw();
		drawing.setVerticesWH(hueBarX, selectedHuePosition, GuiHelper.DEFAULT_LINE_SIZE, 1).setColor(GuiHelper.WHITE_COLOR).draw();

		// Draw saturation and brightness selector
		drawing.setVerticesWH(selectedSaturationPosition - 1, selectedBrightnessPosition, 3, 1).setColor(GuiHelper.BACKGROUND_COLOR).draw();
		drawing.setVerticesWH(selectedSaturationPosition, selectedBrightnessPosition - 1, 1, 3).setColor(GuiHelper.BACKGROUND_COLOR).draw();
		drawing.setVerticesWH(selectedSaturationPosition, selectedBrightnessPosition, 1, 1).setColor(GuiHelper.WHITE_COLOR).draw();

		// Render text fields
		colorTextField.renderWidget(context, mouseX, mouseY, 0);
		redTextField.renderWidget(context, mouseX, mouseY, 0);
		greenTextField.renderWidget(context, mouseX, mouseY, 0);
		blueTextField.renderWidget(context, mouseX, mouseY, 0);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		draggingMouseZone = hoverMouseZone;
	}

	@Override
	public void onRelease(double mouseX, double mouseY) {
		draggingMouseZone = MouseZone.NONE;
	}

	@Override
	protected void onClickAction(int index) {
		switch (index) {
			case 0 -> {
				if (colorCallback != null) {
					colorCallback.accept(ColorHelper.zeroAlpha(new Color(Color.HSBtoRGB(hue, saturation, brightness)).getRGB()));
				}
				setColorCallbackInternal(null);
			}
			case 1 -> setColor(new Random().nextInt(0xFF), new Random().nextInt(0xFF), new Random().nextInt(0xFF));
			case 2 -> setColor(oldColor);
			case 3 -> setColorCallbackInternal(null);
		}
	}

	@Override
	protected void setWidgetHeight() {
		setHeight(GuiHelper.DEFAULT_PADDING * 6 + GuiHelper.DEFAULT_LINE_SIZE * 5 + CONTROLS_SIZE);
	}

	public void setColorCallback(IntConsumer colorCallback, int oldColorRGB) {
		setColorCallbackInternal(colorCallback);
		oldColor = new Color(ColorHelper.zeroAlpha(oldColorRGB), true);
		setColor(oldColor);
	}

	private void setColorCallbackInternal(@Nullable IntConsumer colorCallback) {
		if (colorCallback == null) {
			onDismiss.run();
			this.colorCallback = null;
		} else {
			this.colorCallback = colorCallback;
		}
		visible = this.colorCallback != null;
	}

	private void setColor(Color color) {
		setColor(color.getRed(), color.getGreen(), color.getBlue());
	}

	private void setColor(int red, int green, int blue) {
		final float[] hsb = Color.RGBtoHSB(red, green, blue, null);
		hue = hsb[0];
		saturation = hsb[1];
		brightness = hsb[2];
		updateTextFields();
	}

	private void updateTextFields() {
		final Color color = new Color(ColorHelper.zeroAlpha(Color.HSBtoRGB(hue, saturation, brightness)), true);
		setTextField(colorTextField, color.getRGB(), 16);
		setTextField(redTextField, color.getRed(), 10);
		setTextField(greenTextField, color.getGreen(), 10);
		setTextField(blueTextField, color.getBlue(), 10);
		buttonGroup.buttons[2].active = color.getRGB() != oldColor.getRGB();
	}

	private void textCallback(String text, int base, ColorCallback colorCallback) {
		try {
			final int value = Integer.parseInt(text, base);
			colorCallback.accept(new Color(ColorHelper.zeroAlpha(Color.HSBtoRGB(hue, saturation, brightness))), base == 10 ? Math.clamp(value, 0, 0xFF) : value);
		} catch (Exception ignored) {
		}
	}

	private static void setTextField(BetterTextFieldWidget textField, int value, int base) {
		try {
			if (Integer.parseInt(textField.getText(), base) == value) {
				return;
			}
		} catch (Exception ignored) {
		}
		textField.setText(base == 10 ? String.valueOf(value) : StringUtils.leftPad(Integer.toHexString(value).toUpperCase(Locale.ENGLISH), 6, "0"));
	}

	private enum MouseZone {
		NONE, SATURATION_BRIGHTNESS, HUE
	}

	@FunctionalInterface
	private interface ColorCallback {
		void accept(Color existingColor, int component);
	}
}
