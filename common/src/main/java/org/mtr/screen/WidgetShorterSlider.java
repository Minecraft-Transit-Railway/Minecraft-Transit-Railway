package org.mtr.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.mtr.data.IGui;

import javax.annotation.Nullable;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;

public class WidgetShorterSlider extends SliderWidget implements IGui {

	private final int maxValue;
	private final int markerFrequency;
	private final int markerDisplayedRatio;
	private final IntFunction<String> setMessage;
	private final IntConsumer shiftClickAction;

	private static final int SLIDER_WIDTH = 8;
	private static final int TICK_HEIGHT = SQUARE_SIZE / 2;

	private static final Identifier TEXTURE = Identifier.ofVanilla("widget/slider");
	private static final Identifier HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/slider_highlighted");
	private static final Identifier HANDLE_TEXTURE = Identifier.ofVanilla("widget/slider_handle");
	private static final Identifier HANDLE_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/slider_handle_highlighted");

	public WidgetShorterSlider(int x, int width, int maxValue, int markerFrequency, int markerDisplayedRatio, IntFunction<String> setMessage, @Nullable IntConsumer shiftClickAction) {
		super(x, 0, width, 0, Text.empty(), 0);
		this.maxValue = Math.max(maxValue, 1);
		this.setMessage = setMessage;
		this.shiftClickAction = shiftClickAction;
		this.markerFrequency = markerFrequency;
		this.markerDisplayedRatio = markerDisplayedRatio;
	}

	public WidgetShorterSlider(int x, int width, int maxValue, IntFunction<String> setMessage, @Nullable IntConsumer shiftClickAction) {
		this(x, width, maxValue, 0, 0, setMessage, shiftClickAction);
	}

	@Override
	public void onClick(double d, double e) {
		super.onClick(d, e);
		checkShiftClick();
	}

	@Override
	public void setWidth(int width) {
		super.setWidth(Math.min(width, 380));
	}

	@Override
	protected void updateMessage() {
		setMessage(Text.literal(setMessage.apply(getIntValue())));
	}

	@Override
	protected void onDrag(double d, double e, double f, double g) {
		super.onDrag(d, e, f, g);
		checkShiftClick();
	}

	@Override
	protected void applyValue() {
	}

	@Override
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		context.drawGuiTexture(RenderLayer::getGuiTextured, getTexture(), 200, 20, 0, 0, getX(), getY(), width / 2, height / 2);
		context.drawGuiTexture(RenderLayer::getGuiTextured, getTexture(), 200, 20, 0, 20 - height / 2, getX(), getY() + height / 2, width / 2, height / 2);
		context.drawGuiTexture(RenderLayer::getGuiTextured, getTexture(), 200, 20, 200 - width / 2, 0, getX() + width / 2, getY(), width / 2, height / 2);
		context.drawGuiTexture(RenderLayer::getGuiTextured, getTexture(), 200, 20, 200 - width / 2, 20 - height / 2, getX() + width / 2, getY() + height / 2, width / 2, height / 2);

		final int xOffset = (width - SLIDER_WIDTH) * getIntValue() / maxValue;
		context.drawGuiTexture(RenderLayer::getGuiTextured, getHandleTexture(), 8, 20, 0, 0, getX() + xOffset, getY(), SLIDER_WIDTH / 2, height / 2);
		context.drawGuiTexture(RenderLayer::getGuiTextured, getHandleTexture(), 8, 20, 0, 20 - height / 2, getX() + xOffset, getY() + height / 2, SLIDER_WIDTH / 2, height / 2);
		context.drawGuiTexture(RenderLayer::getGuiTextured, getHandleTexture(), 8, 20, 8 - SLIDER_WIDTH / 2, 0, getX() + xOffset + SLIDER_WIDTH / 2, getY(), SLIDER_WIDTH / 2, height / 2);
		context.drawGuiTexture(RenderLayer::getGuiTextured, getHandleTexture(), 8, 20, 8 - SLIDER_WIDTH / 2, 20 - height / 2, getX() + xOffset + SLIDER_WIDTH / 2, getY() + height / 2, SLIDER_WIDTH / 2, height / 2);

		context.drawText(textRenderer, getMessage().getString(), getX() + width + TEXT_PADDING, getY() + (height - TEXT_HEIGHT) / 2, ARGB_WHITE, false);

		if (markerFrequency > 0) {
			for (int i = 1; i <= maxValue / markerFrequency; i++) {
				final int xOffset1 = (width - SLIDER_WIDTH) * i * markerFrequency / maxValue;
				final int x = getX() + xOffset1 + SLIDER_WIDTH / 3;
				final int y = getY() + height;
				context.fill(x, y, x + 2, y + TICK_HEIGHT, ARGB_GRAY);
				context.drawCenteredTextWithShadow(textRenderer, String.valueOf(i * markerFrequency / markerDisplayedRatio), getX() + xOffset1 + SLIDER_WIDTH / 2, getY() + height + TICK_HEIGHT + 2, ARGB_WHITE);
			}
		}
	}

	public void setValue(int valueInt) {
		value = (double) valueInt / maxValue;
		updateMessage();
	}

	public int getIntValue() {
		return (int) Math.round(value * maxValue);
	}

	private void checkShiftClick() {
		if (shiftClickAction != null && Screen.hasShiftDown()) {
			shiftClickAction.accept(getIntValue());
		}
	}

	private Identifier getTexture() {
		return this.isNarratable() && this.isFocused() ? HIGHLIGHTED_TEXTURE : TEXTURE;
	}

	private Identifier getHandleTexture() {
		return !this.isNarratable() || !this.hovered ? HANDLE_TEXTURE : HANDLE_HIGHLIGHTED_TEXTURE;
	}
}
