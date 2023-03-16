package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.IGui;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Consumer;
import java.util.function.Function;

public class WidgetShorterSlider extends AbstractSliderButton implements IGui {

	private final int maxValue;
	private final int markerFrequency;
	private final int markerDisplayedRatio;
	private final Function<Integer, String> setMessage;
	private final Consumer<Integer> shiftClickAction;

	private static final int SLIDER_WIDTH = 6;
	private static final int TICK_HEIGHT = SQUARE_SIZE / 2;

	public WidgetShorterSlider(int x, int width, int maxValue, int markerFrequency, int markerDisplayedRatio, Function<Integer, String> setMessage, Consumer<Integer> shiftClickAction) {
		super(x, 0, width, 0, Text.literal(""), 0);
		this.maxValue = maxValue;
		this.setMessage = setMessage;
		this.shiftClickAction = shiftClickAction;
		this.markerFrequency = markerFrequency;
		this.markerDisplayedRatio = markerDisplayedRatio;
	}

	public WidgetShorterSlider(int x, int width, int maxValue, Function<Integer, String> setMessage, Consumer<Integer> shiftClickAction) {
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

	public void renderWidget(PoseStack matrices, int mouseX, int mouseY, float delta) {
		render(matrices);
	}

	public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
		render(matrices);
	}

	public void setValue(int valueInt) {
		value = (double) valueInt / maxValue;
		updateMessage();
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getIntValue() {
		return (int) Math.round(value * maxValue);
	}

	private void render(PoseStack matrices) {
		final Minecraft client = Minecraft.getInstance();
		UtilitiesClient.beginDrawingTexture(WIDGETS_LOCATION);

		blit(matrices, UtilitiesClient.getWidgetX(this), UtilitiesClient.getWidgetY(this), 0, 46, width / 2, height / 2);
		blit(matrices, UtilitiesClient.getWidgetX(this), UtilitiesClient.getWidgetY(this) + height / 2, 0, 66 - height / 2, width / 2, height / 2);
		blit(matrices, UtilitiesClient.getWidgetX(this) + width / 2, UtilitiesClient.getWidgetY(this), 200 - width / 2, 46, width / 2, height / 2);
		blit(matrices, UtilitiesClient.getWidgetX(this) + width / 2, UtilitiesClient.getWidgetY(this) + height / 2, 200 - width / 2, 66 - height / 2, width / 2, height / 2);

		final int v = UtilitiesClient.isHovered(this) ? 86 : 66;
		final int xOffset = (width - SLIDER_WIDTH) * getIntValue() / maxValue;
		blit(matrices, UtilitiesClient.getWidgetX(this) + xOffset, UtilitiesClient.getWidgetY(this), 0, v, SLIDER_WIDTH / 2, height / 2);
		blit(matrices, UtilitiesClient.getWidgetX(this) + xOffset, UtilitiesClient.getWidgetY(this) + height / 2, 0, v + 20 - height / 2, SLIDER_WIDTH / 2, height / 2);
		blit(matrices, UtilitiesClient.getWidgetX(this) + xOffset + SLIDER_WIDTH / 2, UtilitiesClient.getWidgetY(this), 200 - SLIDER_WIDTH / 2, v, SLIDER_WIDTH / 2, height / 2);
		blit(matrices, UtilitiesClient.getWidgetX(this) + xOffset + SLIDER_WIDTH / 2, UtilitiesClient.getWidgetY(this) + height / 2, 200 - SLIDER_WIDTH / 2, v + 20 - height / 2, SLIDER_WIDTH / 2, height / 2);

		drawString(matrices, client.font, getMessage().getString(), UtilitiesClient.getWidgetX(this) + width + TEXT_PADDING, UtilitiesClient.getWidgetY(this) + (height - TEXT_HEIGHT) / 2, ARGB_WHITE);

		if (markerFrequency > 0) {
			for (int i = 1; i <= maxValue / markerFrequency; i++) {
				UtilitiesClient.beginDrawingTexture(WIDGETS_LOCATION);
				final int xOffset1 = (width - SLIDER_WIDTH) * i * markerFrequency / maxValue;
				blit(matrices, UtilitiesClient.getWidgetX(this) + xOffset1 + SLIDER_WIDTH / 3, UtilitiesClient.getWidgetY(this) + height, 10, 68, 2, TICK_HEIGHT);
				drawCenteredString(matrices, client.font, String.valueOf(i * markerFrequency / markerDisplayedRatio), UtilitiesClient.getWidgetX(this) + xOffset1 + SLIDER_WIDTH / 2, UtilitiesClient.getWidgetY(this) + height + TICK_HEIGHT + 2, ARGB_WHITE);
			}
		}
	}

	private void checkShiftClick() {
		if (shiftClickAction != null && Screen.hasShiftDown()) {
			shiftClickAction.accept(getIntValue());
		}
	}
}
