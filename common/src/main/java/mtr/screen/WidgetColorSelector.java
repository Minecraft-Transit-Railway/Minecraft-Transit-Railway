package mtr.screen;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import mtr.client.IDrawing;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.Random;

public class WidgetColorSelector extends WidgetBetterTextField {

	private boolean showPopup;
	private float hue;
	private float saturation;
	private float brightness;
	private int oldColor;

	private final boolean topPopup;
	private static final int HEIGHT = 120;
	private static final int BAR_WIDTH = 12;
	private static final int PADDING = 8;
	private static final int BAR_HEIGHT = HEIGHT - PADDING - BAR_WIDTH;
	private static final int WIDTH = HEIGHT + PADDING + BAR_WIDTH;

	public WidgetColorSelector(boolean topPopup) {
		super(TextFieldFilter.HEX, "", DashboardScreen.MAX_COLOR_ZONE_LENGTH);
		this.topPopup = topPopup;
		setResponder(text -> {
			final int color = getColor();
			if ((Color.HSBtoRGB(hue, saturation, brightness) & RGB_WHITE) != (color & RGB_WHITE)) {
				setHsb(color);
			}
		});
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
		if (showPopup && isVisible()) {
			matrices.pushPose();
			matrices.translate(0, 0, 5);

			final int startX = getStartX();
			final int startY = getStartY();
			Gui.fill(matrices, startX - PADDING, startY - PADDING, startX + WIDTH + PADDING, startY + HEIGHT + PADDING, ARGB_BLACK);

			final Tesselator tesselator = Tesselator.getInstance();
			final BufferBuilder buffer = tesselator.getBuilder();
			UtilitiesClient.beginDrawingRectangle(buffer);

			IDrawing.drawRectangle(buffer, startX + WIDTH - BAR_WIDTH, startY + HEIGHT - BAR_WIDTH, startX + WIDTH, startY + HEIGHT - BAR_WIDTH / 2F, ARGB_BLACK + oldColor);
			final int selectedColor = Color.HSBtoRGB(hue, saturation, brightness);
			IDrawing.drawRectangle(buffer, startX + WIDTH - BAR_WIDTH, startY + HEIGHT - BAR_WIDTH / 2F, startX + WIDTH, startY + HEIGHT, ARGB_BLACK + selectedColor);

			for (int drawHue = 0; drawHue < BAR_HEIGHT; drawHue++) {
				final int color = Color.HSBtoRGB((float) drawHue / (BAR_HEIGHT - 1), 1, 1);
				IDrawing.drawRectangle(buffer, startX + WIDTH - BAR_WIDTH, startY + drawHue, startX + WIDTH, startY + drawHue + 1, ARGB_BLACK + color);
			}

			for (int drawSaturation = 0; drawSaturation < HEIGHT; drawSaturation++) {
				for (int drawBrightness = 0; drawBrightness < HEIGHT; drawBrightness++) {
					final int color = Color.HSBtoRGB(hue, (float) drawSaturation / (HEIGHT - 1), (float) drawBrightness / (HEIGHT - 1));
					IDrawing.drawRectangle(buffer, startX + drawSaturation, startY + HEIGHT - drawBrightness - 1, startX + drawSaturation + 1, startY + HEIGHT - drawBrightness, ARGB_BLACK + color);
				}
			}

			final int selectedHueInt = Math.round(hue * (BAR_HEIGHT - 1));
			final int selectedSaturationInt = Math.round(saturation * (HEIGHT - 1));
			final int selectedBrightnessInt = Math.round(brightness * (HEIGHT - 1));
			IDrawing.drawRectangle(buffer, startX + WIDTH - BAR_WIDTH, startY + selectedHueInt - 1, startX + WIDTH, startY + selectedHueInt + 2, ARGB_BLACK);
			IDrawing.drawRectangle(buffer, startX + WIDTH - BAR_WIDTH, startY + selectedHueInt, startX + WIDTH, startY + selectedHueInt + 1, ARGB_WHITE);
			IDrawing.drawRectangle(buffer, startX + selectedSaturationInt - 1, startY + HEIGHT - selectedBrightnessInt - 1, startX + selectedSaturationInt + 2, startY + HEIGHT - selectedBrightnessInt, ARGB_BLACK);
			IDrawing.drawRectangle(buffer, startX + selectedSaturationInt, startY + HEIGHT - selectedBrightnessInt - 2, startX + selectedSaturationInt + 1, startY + HEIGHT - selectedBrightnessInt + 1, ARGB_BLACK);
			IDrawing.drawRectangle(buffer, startX + selectedSaturationInt, startY + HEIGHT - selectedBrightnessInt - 1, startX + selectedSaturationInt + 1, startY + HEIGHT - selectedBrightnessInt, ARGB_WHITE);

			tesselator.end();
			UtilitiesClient.finishDrawingRectangle();
			matrices.popPose();
		}
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return showPopup || super.isMouseOver(mouseX, mouseY);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		selectColor(mouseX, mouseY);
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	protected void onLeftClick(double mouseX, double mouseY) {
		if (showPopup) {
			selectColor(mouseX, mouseY);
			showPopup = inBox(mouseX, mouseY) || super.isMouseOver(mouseX, mouseY);
		} else {
			showPopup = super.isMouseOver(mouseX, mouseY);
		}
	}

	public int getColor() {
		return DashboardScreen.colorStringToInt(getValue());
	}

	public void setColor(int color) {
		final int newColor;
		if ((color & RGB_WHITE) == 0) {
			newColor = (new Random()).nextInt(RGB_WHITE + 1);
		} else {
			newColor = color & RGB_WHITE;
		}
		oldColor = newColor;
		setColorText(newColor);
	}

	private void setColorText() {
		setColorText(Color.HSBtoRGB(hue, saturation, brightness));
	}

	private void setColorText(int color) {
		setValue(StringUtils.leftPad(Integer.toHexString(color & RGB_WHITE).toUpperCase(), 6, "0"));
	}

	private void selectColor(double mouseX, double mouseY) {
		final int startX = getStartX();
		final int startY = getStartY();

		if (inBox(mouseX, mouseY)) {
			if (mouseX < startX + HEIGHT + PADDING / 2F) {
				saturation = (float) Mth.clamp((mouseX - startX) / HEIGHT, 0, 1);
				brightness = 1 - (float) Mth.clamp((mouseY - startY) / HEIGHT, 0, 1);
			} else if (mouseY < startY + BAR_HEIGHT + PADDING / 2F) {
				hue = (float) Mth.clamp((mouseY - startY) / BAR_HEIGHT, 0, 1);
			} else {
				setHsb(oldColor);
			}
			setColorText();
		}
	}

	private void setHsb(int color) {
		final float[] hsb = Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, null);
		hue = hsb[0];
		saturation = hsb[1];
		brightness = hsb[2];
	}

	private boolean inBox(double mouseX, double mouseY) {
		final int startX = getStartX();
		final int startY = getStartY();
		return isVisible() && mouseX >= startX - PADDING && mouseX < startX + WIDTH + PADDING && mouseY >= startY - PADDING && mouseY < startY + HEIGHT + PADDING;
	}

	private int getStartX() {
		return x + (width - WIDTH) / 2;
	}

	private int getStartY() {
		return topPopup ? y - TEXT_FIELD_PADDING - HEIGHT - PADDING : y + SQUARE_SIZE + TEXT_FIELD_PADDING + PADDING;
	}
}
