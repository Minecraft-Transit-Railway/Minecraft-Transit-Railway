package org.mtr.widget;

import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.constraints.ChildBasedSizeConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import gg.essential.universal.UMatrixStack;
import lombok.Getter;
import org.mtr.core.tool.Utilities;
import org.mtr.tool.ReleasedDynamicTextureManager;

import javax.annotation.Nullable;
import java.util.function.DoubleConsumer;

public final class NumberInputComponent extends UIContainer {

	@Getter
	private double value;
	private boolean isDragging;
	private boolean disabled;
	private float oldWidth;

	private final double min;
	private final double max;
	private final double step;
	private final boolean allowDecimal;
	private final boolean allowNegative;

	private final TextInputComponent textInputComponent;
	private final StitchedImageComponent sliderHandleComponent;

	private static final int SLIDER_HANDLE_WIDTH = 6;

	public NumberInputComponent(double min, double max, double step, boolean allowDecimal, @Nullable DoubleConsumer onChange) {
		this.step = step <= 0 ? 1 : step;
		this.min = min;
		this.max = Math.max(max, min + this.step);
		this.allowDecimal = allowDecimal;
		allowNegative = min < 0;

		textInputComponent = (TextInputComponent) new TextInputComponent()
				.setChildOf(this)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));

		final StitchedImageComponent sliderBackgroundComponent = (StitchedImageComponent) new StitchedImageComponent(200, 20, 4, 0, ReleasedDynamicTextureManager.BUTTON_DISABLED_TEXTURE.get())
				.setChildOf(this)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(8));

		sliderHandleComponent = (StitchedImageComponent) new StitchedImageComponent(200, 20, 3, 0, ReleasedDynamicTextureManager.BUTTON_TEXTURE.get(), ReleasedDynamicTextureManager.BUTTON_DISABLED_TEXTURE.get(), ReleasedDynamicTextureManager.BUTTON_HIGHLIGHTED_TEXTURE.get())
				.setChildOf(sliderBackgroundComponent)
				.setWidth(new PixelConstraint(SLIDER_HANDLE_WIDTH))
				.setHeight(new RelativeConstraint());

		sliderBackgroundComponent.onMouseEnterRunnable(() -> setHighlighted(true));
		sliderBackgroundComponent.onMouseLeaveRunnable(() -> setHighlighted(false));
		setHighlighted(false);
		setDisabled(false);
		setHeight(new ChildBasedSizeConstraint());

		textInputComponent.onChange(() -> {
			final String originalText = textInputComponent.getText();
			String newText = originalText.replaceAll("[^\\d-.]", "");

			if (!allowNegative) {
				newText = newText.replaceAll("-", "");
			}

			if (!allowDecimal) {
				newText = newText.replaceAll("\\.", "");
			}

			if (!newText.equals(originalText)) {
				textInputComponent.setText(newText);
			}

			try {
				setValue(Double.parseDouble(textInputComponent.getText()), false);
			} catch (NumberFormatException ignored) {
			}
		});

		textInputComponent.onFocusLost(() -> {
			try {
				setValue(Double.parseDouble(textInputComponent.getText()), true);
			} catch (NumberFormatException ignored) {
				setValue(value, true);
			}
			if (onChange != null) {
				onChange.accept(value);
			}
		});

		sliderBackgroundComponent.onMouseClickConsumer(clickEvent -> {
			isDragging = true;
			setValueFromPosition(clickEvent.getRelativeX());
		});

		sliderBackgroundComponent.onMouseDragConsumer((x, y, mouseButton) -> {
			if (isDragging) {
				setValueFromPosition(x);
			}
		});

		sliderBackgroundComponent.onMouseReleaseRunnable(() -> {
			isDragging = false;
			if (onChange != null) {
				onChange.accept(value);
			}
		});
	}

	@Override
	public void beforeDraw(UMatrixStack matrixStack) {
		super.beforeDraw(matrixStack);
		final float newWidth = getWidth();

		if (oldWidth != newWidth) {
			oldWidth = newWidth;
			setValue(value, false);
		}
	}

	public void setPrefix(String text) {
		textInputComponent.setPrefix(text);
	}

	public void setSuffix(String text) {
		textInputComponent.setSuffix(text);
	}

	public void setValue(double value) {
		setValue(value, true);
	}

	private void setValue(double value, boolean updateTextInput) {
		if (!disabled) {
			this.value = Utilities.clampSafe(Utilities.round(Math.round((Utilities.clampSafe(value, min, max) - min) / step) * step + min, 4), min, max);
			sliderHandleComponent.setX(new PixelConstraint((float) ((getWidth() - SLIDER_HANDLE_WIDTH) * (this.value - min) / (max - min))));
			if (updateTextInput) {
				textInputComponent.setText(allowDecimal ? String.valueOf(this.value) : String.valueOf(Math.round(this.value)));
			}
		}
	}

	private void setValueFromPosition(float position) {
		setValue((position - SLIDER_HANDLE_WIDTH / 2F) / (getWidth() - SLIDER_HANDLE_WIDTH) * (max - min) + min, true);
	}

	private void setHighlighted(boolean highlighted) {
		sliderHandleComponent.setActiveTexture(disabled ? 1 : (highlighted ? 2 : 0));
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		textInputComponent.setDisabled(disabled);
		setHighlighted(false);
	}
}
