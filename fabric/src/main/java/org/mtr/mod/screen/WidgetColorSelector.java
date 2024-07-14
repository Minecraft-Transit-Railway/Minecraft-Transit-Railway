package org.mtr.mod.screen;

import org.apache.commons.lang3.StringUtils;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.MathHelper;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;

import java.awt.*;
import java.util.Locale;
import java.util.Random;
import java.util.function.Consumer;

public class WidgetColorSelector extends ButtonWidgetExtension implements IGui {

	private int color;
	private final ScreenExtension screen;
	private final boolean hasMargin;
	private final Runnable callback;


	public WidgetColorSelector(ScreenExtension screen, boolean hasMargin, Runnable callback) {
		super(0, 0, 0, SQUARE_SIZE, TextHelper.literal(""), button -> {
		});
		this.screen = screen;
		this.hasMargin = hasMargin;
		this.callback = callback;
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		super.render(graphicsHolder, mouseX, mouseY, delta);
		if (visible) {
			final int margin = hasMargin ? 1 : 0;
			final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
			guiDrawing.beginDrawingRectangle();
			guiDrawing.drawRectangle(getX2() - margin, getY2() - margin, getX2() + width + margin, getY2() + height + margin, ARGB_BLACK | color);
			guiDrawing.finishDrawingRectangle();
		}
	}

	@Override
	public void onPress2() {
		MinecraftClient.getInstance().openScreen(new Screen(new ColorSelectorScreen(color, color -> {
			MinecraftClient.getInstance().openScreen(new Screen(screen));
			setColor(color);
			callback.run();
		})));
	}

	public int getColor() {
		return color;
	}

	public void setColor(int newColor) {
		final int clampedColor;
		if ((newColor & RGB_WHITE) == 0) {
			clampedColor = (new Random()).nextInt(RGB_WHITE + 1);
		} else {
			clampedColor = newColor & RGB_WHITE;
		}
		color = clampedColor;
	}

	private static class ColorSelectorScreen extends ScreenExtension {

		private float hue;
		private float saturation;
		private float brightness;
		private DraggingState draggingState = DraggingState.NONE;

		private final int oldColor;
		private final Consumer<Integer> colorCallback;
		private final TextFieldWidgetExtension textFieldColor;
		private final TextFieldWidgetExtension textFieldRed;
		private final TextFieldWidgetExtension textFieldGreen;
		private final TextFieldWidgetExtension textFieldBlue;
		private final ButtonWidgetExtension buttonReset;

		private static final int RIGHT_WIDTH = 60;

		private ColorSelectorScreen(int oldColor, Consumer<Integer> colorCallback) {
			super();
			this.oldColor = oldColor;
			this.colorCallback = colorCallback;
			textFieldColor = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 6, TextCase.UPPER, "[^\\dA-F]", TextHelper.literal(Integer.toHexString(oldColor).toUpperCase(Locale.ENGLISH)).getString());
			textFieldRed = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 3, TextCase.DEFAULT, "\\D", TextHelper.literal(String.valueOf((oldColor >> 16) & 0xFF)).getString());
			textFieldGreen = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 3, TextCase.DEFAULT, "\\D", TextHelper.literal(String.valueOf((oldColor >> 8) & 0xFF)).getString());
			textFieldBlue = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 3, TextCase.DEFAULT, "\\D", TextHelper.literal(String.valueOf(oldColor & 0xFF)).getString());
			buttonReset = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_RESET_SIGN.getMutableText(), button -> {
				setHsb(oldColor, true);
				button.setActiveMapped(false);
			});
		}

		@Override
		protected void init2() {
			super.init2();

			final int startX = SQUARE_SIZE * 4 + getMainWidth();
			final int startY = SQUARE_SIZE + TEXT_HEIGHT + TEXT_PADDING + TEXT_FIELD_PADDING / 2;
			IDrawing.setPositionAndWidth(textFieldColor, startX + TEXT_FIELD_PADDING / 2, startY, RIGHT_WIDTH - TEXT_FIELD_PADDING);
			IDrawing.setPositionAndWidth(textFieldRed, startX + TEXT_FIELD_PADDING / 2, startY + SQUARE_SIZE * 2 + TEXT_FIELD_PADDING, RIGHT_WIDTH - TEXT_FIELD_PADDING);
			IDrawing.setPositionAndWidth(textFieldGreen, startX + TEXT_FIELD_PADDING / 2, startY + SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 2, RIGHT_WIDTH - TEXT_FIELD_PADDING);
			IDrawing.setPositionAndWidth(textFieldBlue, startX + TEXT_FIELD_PADDING / 2, startY + SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 3, RIGHT_WIDTH - TEXT_FIELD_PADDING);
			IDrawing.setPositionAndWidth(buttonReset, startX, getMainHeight(), RIGHT_WIDTH);

			setHsb(oldColor, true);

			textFieldColor.setChangedListener2(text -> textCallback(text, -1));
			textFieldRed.setChangedListener2(text -> textCallback(text, 16));
			textFieldGreen.setChangedListener2(text -> textCallback(text, 8));
			textFieldBlue.setChangedListener2(text -> textCallback(text, 0));

			addChild(new ClickableWidget(textFieldColor));
			addChild(new ClickableWidget(textFieldRed));
			addChild(new ClickableWidget(textFieldGreen));
			addChild(new ClickableWidget(textFieldBlue));
			addChild(new ClickableWidget(buttonReset));
		}

		@Override
		public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
			renderBackground(graphicsHolder);
			super.render(graphicsHolder, mouseX, mouseY, delta);

			final int mainWidth = getMainWidth();
			final int mainHeight = getMainHeight();

			graphicsHolder.drawCenteredText(TranslationProvider.GUI_MTR_COLOR.getMutableText(), SQUARE_SIZE * 4 + mainWidth + RIGHT_WIDTH / 2, SQUARE_SIZE, ARGB_WHITE);
			graphicsHolder.drawCenteredText("RGB", SQUARE_SIZE * 4 + mainWidth + RIGHT_WIDTH / 2, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING, ARGB_WHITE);

			final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
			guiDrawing.beginDrawingRectangle();

			final int selectedColor = Color.HSBtoRGB(hue, saturation, brightness);
			guiDrawing.drawRectangle(SQUARE_SIZE * 4 + mainWidth + 1, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 4 + 1, SQUARE_SIZE * 4 + mainWidth + RIGHT_WIDTH - 1, mainHeight - 1, selectedColor);

			for (int drawHue = 0; drawHue < mainHeight; drawHue++) {
				final int color = Color.HSBtoRGB((float) drawHue / (mainHeight - 1), 1, 1);
				guiDrawing.drawRectangle(SQUARE_SIZE * 2 + mainWidth, SQUARE_SIZE + drawHue, SQUARE_SIZE * 3 + mainWidth, SQUARE_SIZE + drawHue + 1, color);
			}

			for (int drawSaturation = 0; drawSaturation < mainWidth; drawSaturation++) {
				for (int drawBrightness = 0; drawBrightness < mainHeight; drawBrightness++) {
					final int color = Color.HSBtoRGB(hue, (float) drawSaturation / (mainWidth - 1), (float) drawBrightness / (mainHeight - 1));
					guiDrawing.drawRectangle(SQUARE_SIZE + drawSaturation, SQUARE_SIZE + mainHeight - drawBrightness - 1, SQUARE_SIZE + drawSaturation + 1, SQUARE_SIZE + mainHeight - drawBrightness, color);
				}
			}

			final int selectedHueInt = Math.round(hue * (mainHeight - 1));
			final int selectedSaturationInt = Math.round(saturation * (mainWidth - 1));
			final int selectedBrightnessInt = Math.round(brightness * (mainHeight - 1));
			guiDrawing.drawRectangle(SQUARE_SIZE * 2 + mainWidth, SQUARE_SIZE + selectedHueInt - 1, SQUARE_SIZE * 3 + mainWidth, SQUARE_SIZE + selectedHueInt + 2, ARGB_BLACK);
			guiDrawing.drawRectangle(SQUARE_SIZE * 2 + mainWidth, SQUARE_SIZE + selectedHueInt, SQUARE_SIZE * 3 + mainWidth, SQUARE_SIZE + selectedHueInt + 1, ARGB_WHITE);
			guiDrawing.drawRectangle(SQUARE_SIZE + selectedSaturationInt - 1, SQUARE_SIZE + mainHeight - selectedBrightnessInt - 1, SQUARE_SIZE + selectedSaturationInt + 2, SQUARE_SIZE + mainHeight - selectedBrightnessInt, ARGB_BLACK);
			guiDrawing.drawRectangle(SQUARE_SIZE + selectedSaturationInt, SQUARE_SIZE + mainHeight - selectedBrightnessInt - 2, SQUARE_SIZE + selectedSaturationInt + 1, SQUARE_SIZE + mainHeight - selectedBrightnessInt + 1, ARGB_BLACK);
			guiDrawing.drawRectangle(SQUARE_SIZE + selectedSaturationInt, SQUARE_SIZE + mainHeight - selectedBrightnessInt - 1, SQUARE_SIZE + selectedSaturationInt + 1, SQUARE_SIZE + mainHeight - selectedBrightnessInt, ARGB_WHITE);

			guiDrawing.finishDrawingRectangle();
		}

		@Override
		public void tick2() {
			textFieldRed.tick2();
			textFieldGreen.tick2();
			textFieldBlue.tick2();
			textFieldColor.tick2();
		}

		@Override
		public void onClose2() {
			colorCallback.accept(Color.HSBtoRGB(hue, saturation, brightness) & RGB_WHITE);
		}

		@Override
		public boolean mouseClicked2(double mouseX, double mouseY, int button) {
			final int mainWidth = getMainWidth();
			final int mainHeight = getMainHeight();
			draggingState = DraggingState.NONE;
			if (mouseY >= SQUARE_SIZE && mouseY < SQUARE_SIZE + mainHeight) {
				if (mouseX >= SQUARE_SIZE && mouseX < SQUARE_SIZE + mainWidth) {
					draggingState = DraggingState.SATURATION_BRIGHTNESS;
				} else if (mouseX >= SQUARE_SIZE * 2 + mainWidth && mouseX < SQUARE_SIZE * 3 + mainWidth) {
					draggingState = DraggingState.HUE;
				}
			}
			selectColor(mouseX, mouseY);
			return super.mouseClicked2(mouseX, mouseY, button);
		}

		@Override
		public boolean mouseDragged2(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
			selectColor(mouseX, mouseY);
			return super.mouseDragged2(mouseX, mouseY, button, deltaX, deltaY);
		}

		private void selectColor(double mouseX, double mouseY) {
			final int mainWidth = getMainWidth();
			final int mainHeight = getMainHeight();
			switch (draggingState) {
				case SATURATION_BRIGHTNESS:
					saturation = (float) MathHelper.clamp((mouseX - SQUARE_SIZE) / mainWidth, 0, 1);
					brightness = 1 - (float) MathHelper.clamp((mouseY - SQUARE_SIZE) / mainHeight, 0, 1);
					setColorText(Color.HSBtoRGB(hue, saturation, brightness), true);
					break;
				case HUE:
					hue = (float) MathHelper.clamp((mouseY - SQUARE_SIZE) / mainHeight, 0, 1);
					setColorText(Color.HSBtoRGB(hue, saturation, brightness), true);
					break;
			}
		}

		private void setHsb(int color, boolean padZero) {
			final float[] hsb = Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, null);
			hue = hsb[0];
			saturation = hsb[1];
			brightness = hsb[2];
			setColorText(color, padZero);
		}

		private void setColorText(int color, boolean padZero) {
			final String colorString = Integer.toHexString(color & RGB_WHITE).toUpperCase(Locale.ENGLISH);
			textFieldColor.setText2(padZero ? StringUtils.leftPad(colorString, 6, "0") : colorString);
			textFieldRed.setText2(String.valueOf((color >> 16) & 0xFF));
			textFieldGreen.setText2(String.valueOf((color >> 8) & 0xFF));
			textFieldBlue.setText2(String.valueOf(color & 0xFF));
			buttonReset.active = (color & RGB_WHITE) != oldColor;
		}

		private void textCallback(String text, int shift) {
			try {
				final boolean isHex = shift < 0;
				final int compare = Integer.parseInt(text, isHex ? 16 : 10);
				final int currentColor = Color.HSBtoRGB(hue, saturation, brightness) & RGB_WHITE;
				if ((isHex ? currentColor : ((currentColor >> shift) & 0xFF)) != compare) {
					setHsb(isHex ? compare : (currentColor & ~(0xFF << shift)) + (compare << shift), !isHex);
				}
			} catch (Exception ignored) {
			}
		}

		private int getMainWidth() {
			return width - SQUARE_SIZE * 5 - RIGHT_WIDTH;
		}

		private int getMainHeight() {
			return height - SQUARE_SIZE * 2;
		}
	}

	private enum DraggingState {
		NONE, SATURATION_BRIGHTNESS, HUE
	}
}
