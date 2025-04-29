package org.mtr.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.mtr.mixin.TextFieldSelectionEndAccessor;
import org.mtr.screen.TextCase;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiAnimation;
import org.mtr.tool.GuiHelper;

import javax.annotation.Nullable;
import javax.annotation.RegEx;
import java.util.function.Consumer;

public final class BetterTextFieldWidget extends ClickableWidget {

	int lastCursorPosition;
	long lastCursorChangeTime;

	private final int maxLength;
	private final TextCase textCase;
	@Nullable
	private final String filter;
	private final String label;
	private final Consumer<String> callback;
	private final TextFieldWidget textFieldWidget = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 0, 0, Text.empty());

	private final GuiAnimation guiAnimationLabelY = new GuiAnimation(ANIMATION_DURATION);
	private final GuiAnimation guiAnimationLabelScale = new GuiAnimation(ANIMATION_DURATION);

	private static final int MAIN_TEXT_START = GuiHelper.DEFAULT_LINE_SIZE - 2 - GuiHelper.MINECRAFT_FONT_SIZE;
	private static final int LABEL_TEXT_START = 1;
	private static final int CURSOR_START = MAIN_TEXT_START - 1;
	private static final int CURSOR_HEIGHT = GuiHelper.MINECRAFT_FONT_SIZE + 2;
	private static final int CURSOR_FLASH_TIME = 1000;
	private static final int ANIMATION_DURATION = 200;

	public BetterTextFieldWidget(int maxLength, TextCase textCase, @RegEx @Nullable String filter, String label, Consumer<String> callback) {
		this("", maxLength, textCase, filter, label, callback);
	}

	public BetterTextFieldWidget(String text, int maxLength, TextCase textCase, @RegEx @Nullable String filter, String label, Consumer<String> callback) {
		super(0, 0, 0, GuiHelper.DEFAULT_LINE_SIZE, Text.empty());
		this.maxLength = maxLength;
		this.textCase = textCase;
		this.filter = filter;
		this.label = label;
		this.callback = callback;
		setText(text);
	}

	@Override
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		final MatrixStack matrixStack = context.getMatrices();
		final Drawing drawing = new Drawing(matrixStack.peek().getPositionMatrix(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui()));

		// Draw background
		drawing.setVerticesWH(getX(), getY(), width, height).setColor(GuiHelper.BLACK_COLOR).draw();
		drawing.setVerticesWH(getX(), getY() + height - 1, width, 1).setColor(GuiHelper.BACKGROUND_ACCENT_COLOR).draw();

		// Draw hover
		if (isMouseOver(mouseX, mouseY)) {
			drawing.setVerticesWH(getX(), getY(), width, height).setColor(GuiHelper.HOVER_COLOR).draw();
		}

		final String text = getText();
		final int cursorPosition = textFieldWidget.getCursor();
		final long currentTime = System.currentTimeMillis();
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final TextRenderer textRenderer = minecraftClient.textRenderer;
		final int cursorPixel = textRenderer.getWidth(text.substring(0, Math.min(text.length(), cursorPosition)));
		final double pixelWidth = 1 / minecraftClient.getWindow().getScaleFactor();

		// Track cursor movement
		if (cursorPosition != lastCursorPosition) {
			lastCursorChangeTime = currentTime;
			lastCursorPosition = cursorPosition;
		}

		// Draw text selection
		final int selectionEndPixel = textRenderer.getWidth(text.substring(0, Math.min(text.length(), ((TextFieldSelectionEndAccessor) textFieldWidget).getSelectionEnd())));
		drawing.setVerticesWH(
				getX() + GuiHelper.DEFAULT_PADDING + Math.min(cursorPixel, selectionEndPixel),
				getY() + CURSOR_START,
				Math.abs(selectionEndPixel - cursorPixel),
				CURSOR_HEIGHT
		).setColor(GuiHelper.TEXT_SELECTION_COLOR).draw();

		// Draw cursor
		if (isFocused() && (currentTime - lastCursorChangeTime < CURSOR_FLASH_TIME || currentTime % CURSOR_FLASH_TIME < CURSOR_FLASH_TIME / 2)) {
			drawing.setVerticesWH(
					getX() + GuiHelper.DEFAULT_PADDING + cursorPixel - pixelWidth,
					getY() + CURSOR_START,
					pixelWidth * 2,
					CURSOR_HEIGHT
			).setColor(GuiHelper.WHITE_COLOR).draw();
		}

		// Handle animation
		guiAnimationLabelY.tick();
		guiAnimationLabelScale.tick();
		if (!isFocused() && text.isEmpty()) {
			guiAnimationLabelY.animate((height - GuiHelper.MINECRAFT_FONT_SIZE) / 2F - LABEL_TEXT_START);
			guiAnimationLabelScale.animate(0.5);
		} else {
			guiAnimationLabelY.animate(0);
			guiAnimationLabelScale.animate(0);
		}

		// Draw label
		matrixStack.push();
		matrixStack.translate(getX() + GuiHelper.DEFAULT_PADDING, getY() + LABEL_TEXT_START + guiAnimationLabelY.getCurrentValue(), 0);
		final float scale = (float) guiAnimationLabelScale.getCurrentValue() + 0.5F;
		if (scale != 1) {
			matrixStack.scale(scale, scale, 1);
		}
		context.drawText(textRenderer, label, 0, 0, GuiHelper.LIGHT_GRAY_COLOR, false);
		matrixStack.pop();

		// Draw text
		if (!text.isEmpty()) {
			context.drawText(textRenderer, text, getX() + GuiHelper.DEFAULT_PADDING, getY() + MAIN_TEXT_START, GuiHelper.WHITE_COLOR, false);
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (visible) {
			final String oldText = getText();
			refreshTextFieldWidget();
			final boolean result = textFieldWidget.keyPressed(keyCode, scanCode, modifiers);
			setText(oldText, getText());
			return result;
		} else {
			return false;
		}
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		if (visible) {
			final String oldText = getText();
			refreshTextFieldWidget();
			final boolean result = textFieldWidget.charTyped(chr, modifiers);
			setText(oldText, getText());
			return result;
		} else {
			return false;
		}
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		refreshTextFieldWidget();
		textFieldWidget.onClick(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (visible) {
			if (isMouseOver(mouseX, mouseY)) {
				setFocused(true);
				if (button == 1) {
					setText("");
					return true;
				} else {
					return super.mouseClicked(mouseX, mouseY, button);
				}
			} else {
				setFocused(false);
				return false;
			}
		}
		return false;
	}

	@Override
	public void setFocused(boolean focused) {
		super.setFocused(focused);
		lastCursorPosition = -1;
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	}

	public String getText() {
		return textFieldWidget.getText();
	}

	public void setText(String text) {
		setText(getText(), text);
	}

	private void setText(String oldText, String text) {
		final String tempText = getText();
		final String newText;

		if (filter == null || filter.isEmpty()) {
			newText = trySetLength(textCase.convert.apply(text));
		} else {
			newText = trySetLength(textCase.convert.apply(text).replaceAll(filter, ""));
		}

		if (!tempText.equals(newText)) {
			refreshTextFieldWidget();
			textFieldWidget.setText(newText);
		}

		if (!oldText.equals(newText)) {
			callback.accept(newText);
		}
	}

	private String trySetLength(String text) {
		return text.length() > maxLength ? text.substring(0, maxLength) : text;
	}

	private void refreshTextFieldWidget() {
		textFieldWidget.setMaxLength(Integer.MAX_VALUE);
		textFieldWidget.setDrawsBackground(false);
		textFieldWidget.setX(getX());
		textFieldWidget.setY(getY());
		textFieldWidget.setWidth(width);
		textFieldWidget.setHeight(height);
		textFieldWidget.setFocused(true);
		textFieldWidget.setVisible(true);
		textFieldWidget.active = true;
	}
}
