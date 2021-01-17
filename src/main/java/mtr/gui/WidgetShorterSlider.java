package mtr.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class WidgetShorterSlider extends SliderWidget implements IGui {

	private final int maxValue;
	private final OnSlide onSlide;
	private final SetMessage setMessage;

	private static final int SLIDER_WIDTH = 6;

	public WidgetShorterSlider(int x, int width, int maxValue, OnSlide onSlide, SetMessage setMessage) {
		super(x, 0, width, 0, new LiteralText(""), 0);
		this.maxValue = maxValue;
		this.onSlide = onSlide;
		this.setMessage = setMessage;
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		final MinecraftClient client = MinecraftClient.getInstance();
		client.getTextureManager().bindTexture(WIDGETS_LOCATION);

		drawTexture(matrices, x, y, 0, 46, width / 2, height / 2);
		drawTexture(matrices, x, y + height / 2, 0, 66 - height / 2, width / 2, height / 2);
		drawTexture(matrices, x + width / 2, y, 200 - width / 2, 46, width / 2, height / 2);
		drawTexture(matrices, x + width / 2, y + height / 2, 200 - width / 2, 66 - height / 2, width / 2, height / 2);

		final int v = isHovered() ? 86 : 66;
		final int xOffset = (width - SLIDER_WIDTH) * getIntValue() / maxValue;
		drawTexture(matrices, x + xOffset, y, 0, v, SLIDER_WIDTH / 2, height / 2);
		drawTexture(matrices, x + xOffset, y + height / 2, 0, v + 20 - height / 2, SLIDER_WIDTH / 2, height / 2);
		drawTexture(matrices, x + xOffset + SLIDER_WIDTH / 2, y, 200 - SLIDER_WIDTH / 2, v, SLIDER_WIDTH / 2, height / 2);
		drawTexture(matrices, x + xOffset + SLIDER_WIDTH / 2, y + height / 2, 200 - SLIDER_WIDTH / 2, v + 20 - height / 2, SLIDER_WIDTH / 2, height / 2);

		drawStringWithShadow(matrices, client.textRenderer, getMessage().getString(), x + width + TEXT_PADDING, y + (height - TEXT_HEIGHT) / 2, ARGB_WHITE);
	}

	@Override
	protected void updateMessage() {
		setMessage(new LiteralText(setMessage.setMessage(getIntValue())));
	}

	@Override
	protected void applyValue() {
		onSlide.onSlide(getIntValue());
	}

	public void setValue(int valueInt) {
		value = (double) valueInt / maxValue;
		updateMessage();
	}

	public void setHeight(int height) {
		this.height = height;
	}

	private int getIntValue() {
		return (int) Math.round(value * maxValue);
	}

	@FunctionalInterface
	public interface OnSlide {
		void onSlide(int value);
	}

	@FunctionalInterface
	public interface SetMessage {
		String setMessage(int value);
	}
}
