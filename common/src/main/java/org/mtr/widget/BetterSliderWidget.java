package org.mtr.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;

import javax.annotation.Nullable;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;

public class BetterSliderWidget extends SliderWidget {

	private final int maxValue;
	private final IntFunction<String> setMessage;
	private final String label;
	private final int fixedWidth;
	private final IntConsumer shiftClickAction;

	private static final int SLIDER_WIDTH = 4;

	public BetterSliderWidget(int maxValue, IntFunction<String> setMessage, String label, int width, @Nullable IntConsumer shiftClickAction) {
		super(0, 0, 0, 0, Text.empty(), 0);
		this.maxValue = Math.max(maxValue, 1);
		this.setMessage = setMessage;
		this.label = label;
		fixedWidth = width;
		this.shiftClickAction = shiftClickAction;
		setDimensions();
	}

	@Override
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		setDimensions();
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final TextRenderer textRenderer = minecraftClient.textRenderer;
		final MatrixStack matrixStack = context.getMatrices();
		final Drawing drawing = new Drawing(matrixStack, RenderLayer.getGui());

		// Draw background
		drawing.setVerticesWH(getX(), getY(), width, height).setColor(GuiHelper.BLACK_COLOR).draw();
		drawing.setVerticesWH(getX(), getY() + height - 1, width, 1).setColor(GuiHelper.BACKGROUND_ACCENT_COLOR).draw();

		// Draw hover
		if (isMouseOver(mouseX, mouseY)) {
			drawing.setVerticesWH(getX(), getY(), width, height).setColor(GuiHelper.HOVER_COLOR).draw();
		}

		// Draw slider
		drawing.setVerticesWH(getX() + (double) (width - SLIDER_WIDTH) * Math.clamp((double) getIntValue() / maxValue, 0, 1), getY(), SLIDER_WIDTH, height).setColor(isMouseOver(mouseX, mouseY) ? GuiHelper.SCROLL_BAR_HOVER_COLOR : GuiHelper.SCROLL_BAR_COLOR).draw();

		// Draw label
		matrixStack.push();
		matrixStack.translate(getX() + GuiHelper.DEFAULT_PADDING, getY() + BetterTextFieldWidget.LABEL_TEXT_START, 0);
		matrixStack.scale(0.5F, 0.5F, 1);
		context.drawText(textRenderer, label, 0, 0, GuiHelper.LIGHT_GRAY_COLOR, false);
		matrixStack.pop();

		// Draw text
		final String text = getMessage().getString();
		context.drawText(textRenderer, text, getX() + (width - textRenderer.getWidth(text)) / 2, getY() + BetterTextFieldWidget.MAIN_TEXT_START, active ? GuiHelper.WHITE_COLOR : GuiHelper.DISABLED_TEXT_COLOR, false);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
		checkShiftClick();
	}

	@Override
	protected void updateMessage() {
		setMessage(Text.literal(setMessage.apply(getIntValue())));
	}

	@Override
	protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
		super.onDrag(mouseX, mouseY, deltaX, deltaY);
		checkShiftClick();
	}

	@Override
	protected void applyValue() {
	}

	public void setValue(int valueInt) {
		value = (double) valueInt / maxValue;
		updateMessage();
	}

	public int getIntValue() {
		return (int) Math.round(value * maxValue);
	}

	private void setDimensions() {
		setDimensions(fixedWidth, GuiHelper.DEFAULT_LINE_SIZE);
	}

	private void checkShiftClick() {
		if (shiftClickAction != null && Screen.hasShiftDown()) {
			shiftClickAction.accept(getIntValue());
		}
	}
}
