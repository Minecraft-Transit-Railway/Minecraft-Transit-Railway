package mtr.screen;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Button;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;

public class WidgetColorSelector extends Button implements IGui {

	private int color;
	private final ScreenMapper screen;
	private final Runnable callback;

	public WidgetColorSelector(ScreenMapper screen, Runnable callback) {
		super(0, 0, 0, SQUARE_SIZE, Text.literal(""), button -> {
		});
		this.screen = screen;
		this.callback = callback;
	}

	@Override
	public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
		super.renderButton(matrices, mouseX, mouseY, delta);
		Gui.fill(matrices, x - 1, y - 1, x + width + 1, y + height + 1, ARGB_BLACK | color);
	}

	@Override
	public void onPress() {
		UtilitiesClient.setScreen(Minecraft.getInstance(), new ColorSelectorScreen(color, color -> {
			UtilitiesClient.setScreen(Minecraft.getInstance(), screen);
			setColor(color);
			callback.run();
		}));
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

	private static class ColorSelectorScreen extends ScreenMapper {

		private float hue;
		private float saturation;
		private float brightness;
		private DraggingState draggingState = DraggingState.NONE;

		private final int oldColor;
		private final Consumer<Integer> colorCallback;
		private final WidgetBetterTextField textFieldColor;
		private final WidgetBetterTextField textFieldRed;
		private final WidgetBetterTextField textFieldGreen;
		private final WidgetBetterTextField textFieldBlue;
		private final Button buttonReset;

		private static final int RIGHT_WIDTH = 60;

		private ColorSelectorScreen(int oldColor, Consumer<Integer> colorCallback) {
			super(Text.literal(""));
			this.oldColor = oldColor;
			this.colorCallback = colorCallback;
			textFieldColor = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.HEX, Text.literal(Integer.toHexString(oldColor).toUpperCase()).getString(), 6);
			textFieldRed = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.POSITIVE_INTEGER, Text.literal(String.valueOf((oldColor >> 16) & 0xFF)).getString(), 3);
			textFieldGreen = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.POSITIVE_INTEGER, Text.literal(String.valueOf((oldColor >> 8) & 0xFF)).getString(), 3);
			textFieldBlue = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.POSITIVE_INTEGER, Text.literal(String.valueOf(oldColor & 0xFF)).getString(), 3);
			buttonReset = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.reset_sign"), button -> {
				setHsb(oldColor, true);
				button.active = false;
			});
		}

		@Override
		protected void init() {
			super.init();

			final int startX = SQUARE_SIZE * 4 + getMainWidth();
			final int startY = SQUARE_SIZE + TEXT_HEIGHT + TEXT_PADDING + TEXT_FIELD_PADDING / 2;
			IDrawing.setPositionAndWidth(textFieldColor, startX + TEXT_FIELD_PADDING / 2, startY, RIGHT_WIDTH - TEXT_FIELD_PADDING);
			IDrawing.setPositionAndWidth(textFieldRed, startX + TEXT_FIELD_PADDING / 2, startY + SQUARE_SIZE * 2 + TEXT_FIELD_PADDING, RIGHT_WIDTH - TEXT_FIELD_PADDING);
			IDrawing.setPositionAndWidth(textFieldGreen, startX + TEXT_FIELD_PADDING / 2, startY + SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 2, RIGHT_WIDTH - TEXT_FIELD_PADDING);
			IDrawing.setPositionAndWidth(textFieldBlue, startX + TEXT_FIELD_PADDING / 2, startY + SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 3, RIGHT_WIDTH - TEXT_FIELD_PADDING);
			IDrawing.setPositionAndWidth(buttonReset, startX, getMainHeight(), RIGHT_WIDTH);

			setHsb(oldColor, true);

			textFieldColor.setResponder(text -> textCallback(text, -1));
			textFieldRed.setResponder(text -> textCallback(text, 16));
			textFieldGreen.setResponder(text -> textCallback(text, 8));
			textFieldBlue.setResponder(text -> textCallback(text, 0));

			addDrawableChild(textFieldColor);
			addDrawableChild(textFieldRed);
			addDrawableChild(textFieldGreen);
			addDrawableChild(textFieldBlue);
			addDrawableChild(buttonReset);
		}

		@Override
		public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
			try {
				renderBackground(matrices);
				super.render(matrices, mouseX, mouseY, delta);

				final int mainWidth = getMainWidth();
				final int mainHeight = getMainHeight();

				drawCenteredString(matrices, font, Text.translatable("gui.mtr.color"), SQUARE_SIZE * 4 + mainWidth + RIGHT_WIDTH / 2, SQUARE_SIZE, ARGB_WHITE);
				drawCenteredString(matrices, font, "RGB", SQUARE_SIZE * 4 + mainWidth + RIGHT_WIDTH / 2, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING, ARGB_WHITE);

				final Tesselator tesselator = Tesselator.getInstance();
				final BufferBuilder buffer = tesselator.getBuilder();
				UtilitiesClient.beginDrawingRectangle(buffer);

				final int selectedColor = Color.HSBtoRGB(hue, saturation, brightness);
				IDrawing.drawRectangle(buffer, SQUARE_SIZE * 4 + mainWidth + 1, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 4 + 1, SQUARE_SIZE * 4 + mainWidth + RIGHT_WIDTH - 1, mainHeight - 1, selectedColor);

				for (int drawHue = 0; drawHue < mainHeight; drawHue++) {
					final int color = Color.HSBtoRGB((float) drawHue / (mainHeight - 1), 1, 1);
					IDrawing.drawRectangle(buffer, SQUARE_SIZE * 2 + mainWidth, SQUARE_SIZE + drawHue, SQUARE_SIZE * 3 + mainWidth, SQUARE_SIZE + drawHue + 1, color);
				}

				for (int drawSaturation = 0; drawSaturation < mainWidth; drawSaturation++) {
					for (int drawBrightness = 0; drawBrightness < mainHeight; drawBrightness++) {
						final int color = Color.HSBtoRGB(hue, (float) drawSaturation / (mainWidth - 1), (float) drawBrightness / (mainHeight - 1));
						IDrawing.drawRectangle(buffer, SQUARE_SIZE + drawSaturation, SQUARE_SIZE + mainHeight - drawBrightness - 1, SQUARE_SIZE + drawSaturation + 1, SQUARE_SIZE + mainHeight - drawBrightness, color);
					}
				}

				final int selectedHueInt = Math.round(hue * (mainHeight - 1));
				final int selectedSaturationInt = Math.round(saturation * (mainWidth - 1));
				final int selectedBrightnessInt = Math.round(brightness * (mainHeight - 1));
				IDrawing.drawRectangle(buffer, SQUARE_SIZE * 2 + mainWidth, SQUARE_SIZE + selectedHueInt - 1, SQUARE_SIZE * 3 + mainWidth, SQUARE_SIZE + selectedHueInt + 2, ARGB_BLACK);
				IDrawing.drawRectangle(buffer, SQUARE_SIZE * 2 + mainWidth, SQUARE_SIZE + selectedHueInt, SQUARE_SIZE * 3 + mainWidth, SQUARE_SIZE + selectedHueInt + 1, ARGB_WHITE);
				IDrawing.drawRectangle(buffer, SQUARE_SIZE + selectedSaturationInt - 1, SQUARE_SIZE + mainHeight - selectedBrightnessInt - 1, SQUARE_SIZE + selectedSaturationInt + 2, SQUARE_SIZE + mainHeight - selectedBrightnessInt, ARGB_BLACK);
				IDrawing.drawRectangle(buffer, SQUARE_SIZE + selectedSaturationInt, SQUARE_SIZE + mainHeight - selectedBrightnessInt - 2, SQUARE_SIZE + selectedSaturationInt + 1, SQUARE_SIZE + mainHeight - selectedBrightnessInt + 1, ARGB_BLACK);
				IDrawing.drawRectangle(buffer, SQUARE_SIZE + selectedSaturationInt, SQUARE_SIZE + mainHeight - selectedBrightnessInt - 1, SQUARE_SIZE + selectedSaturationInt + 1, SQUARE_SIZE + mainHeight - selectedBrightnessInt, ARGB_WHITE);

				tesselator.end();
				UtilitiesClient.finishDrawingRectangle();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void tick() {
			textFieldRed.tick();
			textFieldGreen.tick();
			textFieldBlue.tick();
			textFieldColor.tick();
		}

		@Override
		public void onClose() {
			colorCallback.accept(Color.HSBtoRGB(hue, saturation, brightness) & RGB_WHITE);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
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
			return super.mouseClicked(mouseX, mouseY, button);
		}

		@Override
		public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
			selectColor(mouseX, mouseY);
			return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		}

		private void selectColor(double mouseX, double mouseY) {
			final int mainWidth = getMainWidth();
			final int mainHeight = getMainHeight();
			switch (draggingState) {
				case SATURATION_BRIGHTNESS:
					saturation = (float) Mth.clamp((mouseX - SQUARE_SIZE) / mainWidth, 0, 1);
					brightness = 1 - (float) Mth.clamp((mouseY - SQUARE_SIZE) / mainHeight, 0, 1);
					setColorText(Color.HSBtoRGB(hue, saturation, brightness), true);
					break;
				case HUE:
					hue = (float) Mth.clamp((mouseY - SQUARE_SIZE) / mainHeight, 0, 1);
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
			final String colorString = Integer.toHexString(color & RGB_WHITE).toUpperCase();
			textFieldColor.setValue(padZero ? StringUtils.leftPad(colorString, 6, "0") : colorString);
			textFieldRed.setValue(String.valueOf((color >> 16) & 0xFF));
			textFieldGreen.setValue(String.valueOf((color >> 8) & 0xFF));
			textFieldBlue.setValue(String.valueOf(color & 0xFF));
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
