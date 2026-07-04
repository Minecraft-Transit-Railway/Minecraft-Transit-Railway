package org.mtr.widget;

import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMatrixStack;
import gg.essential.universal.vertex.UVertexConsumer;
import org.jspecify.annotations.Nullable;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.tool.GuiHelper;

import java.awt.*;
import java.util.Random;
import java.util.function.DoubleConsumer;

public final class ColorInputComponent extends UIContainer {

	@Nullable
	private Color oldColor;
	private float hue;
	private float saturation;
	private float brightness;

	private boolean draggingSaturationBrightness = false;
	private boolean draggingHue = false;

	@Nullable
	private InputSource inputSource;
	private int inputSourceCooldown;

	private final SlotBackgroundComponent saturationBrightnessBoxComponent;
	private final SlotBackgroundComponent hueBoxComponent;
	private final SlotBackgroundComponent colorPreviewComponent;
	private final ButtonComponent resetButton;
	private final TextInputComponent colorInputComponent;
	private final NumberInputComponent redInputComponent;
	private final NumberInputComponent greenInputComponent;
	private final NumberInputComponent blueInputComponent;

	private static final int RIGHT_PANEL_WIDTH = 96;
	private static final int INPUT_SOURCE_COOLDOWN_LIMIT = 2;
	private static final Random RANDOM = new Random();

	public ColorInputComponent() {
		saturationBrightnessBoxComponent = (SlotBackgroundComponent) new SlotBackgroundComponent()
			.setChildOf(this)
			.setWidth(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING * 2)))
			.setHeight(new RelativeConstraint())
			.onMouseClickConsumer(clickEvent -> clickSaturationBrightness(clickEvent.getRelativeX(), clickEvent.getRelativeY()))
			.onMouseDragConsumer((x, y, mouseButton) -> {
				if (draggingSaturationBrightness) {
					mouseClickHSB(x, y);
				}
			})
			.onMouseReleaseRunnable(() -> draggingSaturationBrightness = false);

		saturationBrightnessBoxComponent.setBackgroundColor(Color.BLACK);

		hueBoxComponent = (SlotBackgroundComponent) new SlotBackgroundComponent()
			.setChildOf(this)
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new PixelConstraint(GuiHelper.DEFAULT_LINE_SIZE))
			.setHeight(new RelativeConstraint())
			.onMouseClickConsumer(clickEvent -> clickHue(clickEvent.getRelativeY()))
			.onMouseDragConsumer((x, y, mouseButton) -> {
				if (draggingHue) {
					mouseClickHSB(0, y);
				}
			})
			.onMouseReleaseRunnable(() -> draggingHue = false);

		hueBoxComponent.setBackgroundColor(Color.BLACK);

		final UIContainer outerContainer = (UIContainer) new UIContainer()
			.setChildOf(this)
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new PixelConstraint(RIGHT_PANEL_WIDTH))
			.setHeight(new RelativeConstraint());

		colorPreviewComponent = (SlotBackgroundComponent) new SlotBackgroundComponent()
			.setChildOf(outerContainer)
			.setWidth(new PixelConstraint(RIGHT_PANEL_WIDTH))
			.setHeight(new PixelConstraint(RIGHT_PANEL_WIDTH / 4F));

		final ScrollComponent scrollComponent = ((ScrollPanelComponent) new ScrollPanelComponent(true)
			.setChildOf(outerContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)))).contentContainer;

		resetButton = (ButtonComponent) (new ButtonComponent(true)
			.setChildOf(scrollComponent)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint()));

		resetButton.setDisabled(true);
		resetButton.setText(TranslationProvider.GUI_MTR_RESET.getString());
		resetButton.onClick(() -> {
			if (oldColor != null) {
				setSelectedColor(oldColor, InputSource.EXTERNAL);
			}
		});

		final ButtonComponent randomButton = (ButtonComponent) (new ButtonComponent(true)
			.setChildOf(scrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint()));

		randomButton.setText(TranslationProvider.GUI_MTR_COLOR_RANDOM.getString());
		randomButton.onClick(this::setRandomColor);

		GuiHelper.createSpacing(scrollComponent);
		GuiHelper.createLabel(scrollComponent, TranslationProvider.GUI_MTR_COLOR_HEX.getString());

		colorInputComponent = (TextInputComponent) new TextInputComponent()
			.setChildOf(scrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		colorInputComponent.setForceUpperCase(true);
		colorInputComponent.setFilter("[^\\dA-F]");
		colorInputComponent.setMaxLength(6);
		colorInputComponent.onChange(() -> colorInputChanged(false));
		colorInputComponent.onFocusLost(() -> colorInputChanged(true));

		GuiHelper.createSpacing(scrollComponent);
		GuiHelper.createLabel(scrollComponent, TranslationProvider.GUI_MTR_COLOR_RGB.getString());

		final UIContainer rgbContainer = (UIContainer) new UIContainer()
			.setChildOf(scrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(NumberInputComponent.HEIGHT));

		redInputComponent = addColorInputComponents(rgbContainer, value -> setSelectedColor((int) value, null, null));
		greenInputComponent = addColorInputComponents(rgbContainer, value -> setSelectedColor(null, (int) value, null));
		blueInputComponent = addColorInputComponents(rgbContainer, value -> setSelectedColor(null, null, (int) value));
	}

	@Override
	public void draw(UMatrixStack matrixStack) {
		super.draw(matrixStack);
		ImageComponentBase.drawRectangle(vertexConsumer -> {
			drawSlot(matrixStack, vertexConsumer, hueBoxComponent, 1, 0, 0F, hue, (relativeX, relativeY, indexX) -> new Color(Color.HSBtoRGB(getHue(relativeY), 1, 1)));
			drawSlot(matrixStack, vertexConsumer, saturationBrightnessBoxComponent, 0, 0, brightness, saturation, (relativeX, relativeY, indexX) -> new Color(Color.HSBtoRGB(hue, getSaturation(relativeY), getBrightness(relativeX))));
			drawSlot(matrixStack, vertexConsumer, colorPreviewComponent, 2, 1, null, null, (relativeX, relativeY, indexX) -> indexX > 0 ? oldColor == null ? Color.BLACK : oldColor : getSelectedColor());
		}, false);

		if (inputSourceCooldown <= INPUT_SOURCE_COOLDOWN_LIMIT) {
			inputSourceCooldown++;
		}
	}

	public Color getSelectedColor() {
		return new Color(Color.HSBtoRGB(hue, saturation, brightness));
	}

	public void setSelectedColor(Color color) {
		setSelectedColor(color, InputSource.EXTERNAL);
	}

	public void setRandomColor() {
		setSelectedColor(new Color(RANDOM.nextInt(0xFFFFFF)), InputSource.EXTERNAL);
	}

	private void setSelectedColor(@Nullable Integer red, @Nullable Integer green, @Nullable Integer blue) {
		final Color color = getSelectedColor();
		setSelectedColor(new Color(red == null ? color.getRed() : red, green == null ? color.getGreen() : green, blue == null ? color.getBlue() : blue), InputSource.RGB);
	}

	private void setSelectedColor(Color color, InputSource inputSource) {
		if (inputSourceCooldown > INPUT_SOURCE_COOLDOWN_LIMIT) {
			this.inputSource = inputSource;
		}

		if (this.inputSource != InputSource.HSB) {
			final float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
			hue = hsb[0];
			saturation = hsb[1];
			brightness = hsb[2];
		}

		if (this.inputSource != InputSource.COLOR) {
			final String colorHexString = Utilities.numberToPaddedHexString(color.getRGB() & 0xFFFFFF, 6);
			if (!colorInputComponent.getText().equals(colorHexString)) {
				colorInputComponent.setText(colorHexString);
			}
		}

		if (this.inputSource != InputSource.RGB) {
			if (redInputComponent.getValue() != color.getRed()) {
				redInputComponent.setValue(color.getRed());
			}

			if (greenInputComponent.getValue() != color.getGreen()) {
				greenInputComponent.setValue(color.getGreen());
			}

			if (blueInputComponent.getValue() != color.getBlue()) {
				blueInputComponent.setValue(color.getBlue());
			}
		}

		if (oldColor == null) {
			oldColor = color;
		}

		resetButton.setDisabled(oldColor.getRGB() == color.getRGB());
		inputSourceCooldown = 0;
	}

	private void colorInputChanged(boolean updateColorInput) {
		try {
			setSelectedColor(new Color(Integer.parseInt(colorInputComponent.getText(), 16)), updateColorInput ? InputSource.EXTERNAL : InputSource.COLOR);
		} catch (Exception ignored) {
		}
	}

	private void clickHue(float y) {
		if (Utilities.isBetween(y, 0, hueBoxComponent.getHeight())) {
			draggingHue = true;
			mouseClickHSB(0, y);
		}
	}

	private void clickSaturationBrightness(float x, float y) {
		if (Utilities.isBetween(x, 0, saturationBrightnessBoxComponent.getWidth()) && Utilities.isBetween(y, 0, saturationBrightnessBoxComponent.getHeight())) {
			draggingSaturationBrightness = true;
			mouseClickHSB(x, y);
		}
	}

	private void mouseClickHSB(float x, float y) {
		if (draggingHue) {
			hue = getHue(y);
			setSelectedColor(getSelectedColor(), InputSource.HSB);
		}

		if (draggingSaturationBrightness) {
			saturation = getSaturation(y);
			brightness = getBrightness(x);
			setSelectedColor(getSelectedColor(), InputSource.HSB);
		}
	}

	private float getHue(float y) {
		return Utilities.clampSafe((y - 1.5F) / (hueBoxComponent.getHeight() - 3), 0, 1);
	}

	private float getSaturation(float y) {
		return Utilities.clampSafe((y - 1.5F) / (saturationBrightnessBoxComponent.getHeight() - 3), 0, 1);
	}

	private float getBrightness(float x) {
		return Utilities.clampSafe((x - 1.5F) / (saturationBrightnessBoxComponent.getWidth() - 3), 0, 1);
	}

	private static NumberInputComponent addColorInputComponents(UIContainer container, DoubleConsumer callback) {
		return (NumberInputComponent) new NumberInputComponent(0, 0xFF, 1, false, callback)
			.setChildOf(container)
			.setX(new SiblingConstraint())
			.setWidth(new ScaleConstraint(new RelativeConstraint(), 1F / 3));
	}

	private static void drawSlot(UMatrixStack matrixStack, UVertexConsumer vertexConsumer, SlotBackgroundComponent slotBackgroundComponent, int xDimensionCount, int yDimensionCount, @Nullable Float selectionX, @Nullable Float selectionY, DrawColor drawColor) {
		final float left = slotBackgroundComponent.getLeft() + 1;
		final float top = slotBackgroundComponent.getTop() + 1;
		final float width = slotBackgroundComponent.getWidth() - 2;
		final float height = slotBackgroundComponent.getHeight() - 2;
		final int widthCount = xDimensionCount == 0 ? (int) Math.ceil(width * 2) : xDimensionCount;
		final int heightCount = yDimensionCount == 0 ? (int) Math.ceil(height * 2) : yDimensionCount;
		final float pixelSizeX = width / widthCount;
		final float pixelSizeY = height / heightCount;

		for (int x = 0; x < widthCount; x++) {
			for (int y = 0; y < heightCount; y++) {
				final float drawX1 = left + x * pixelSizeX;
				final float drawX2 = drawX1 + pixelSizeX;
				final float drawY1 = top + y * pixelSizeY;
				final float drawY2 = drawY1 + pixelSizeY;
				final Color color = drawColor.accept(drawX1 + pixelSizeX / 2 - left, drawY1 + pixelSizeY / 2 - top, x);

				vertexConsumer.pos(matrixStack, drawX1, drawY1, 0).color(color).endVertex();
				vertexConsumer.pos(matrixStack, drawX1, drawY2, 0).color(color).endVertex();
				vertexConsumer.pos(matrixStack, drawX2, drawY2, 0).color(color).endVertex();
				vertexConsumer.pos(matrixStack, drawX2, drawY1, 0).color(color).endVertex();
			}
		}

		if (selectionX != null && selectionY != null) {
			final float drawX1 = left + selectionX * (width - 1);
			final float drawX2 = drawX1 + (xDimensionCount == 0 ? 1 : width / xDimensionCount);
			final float drawY1 = top + selectionY * (height - 1);
			final float drawY2 = drawY1 + (yDimensionCount == 0 ? 1 : height / yDimensionCount);

			vertexConsumer.pos(matrixStack, Utilities.clampSafe(drawX1 - 1, left, left + width), Utilities.clampSafe(drawY1 - 1, top, top + height), 0).color(Color.BLACK).endVertex();
			vertexConsumer.pos(matrixStack, Utilities.clampSafe(drawX1 - 1, left, left + width), Utilities.clampSafe(drawY2 + 1, top, top + height), 0).color(Color.BLACK).endVertex();
			vertexConsumer.pos(matrixStack, Utilities.clampSafe(drawX2 + 1, left, left + width), Utilities.clampSafe(drawY2 + 1, top, top + height), 0).color(Color.BLACK).endVertex();
			vertexConsumer.pos(matrixStack, Utilities.clampSafe(drawX2 + 1, left, left + width), Utilities.clampSafe(drawY1 - 1, top, top + height), 0).color(Color.BLACK).endVertex();

			vertexConsumer.pos(matrixStack, drawX1, drawY1, 0).color(Color.WHITE).endVertex();
			vertexConsumer.pos(matrixStack, drawX1, drawY2, 0).color(Color.WHITE).endVertex();
			vertexConsumer.pos(matrixStack, drawX2, drawY2, 0).color(Color.WHITE).endVertex();
			vertexConsumer.pos(matrixStack, drawX2, drawY1, 0).color(Color.WHITE).endVertex();
		}
	}

	@FunctionalInterface
	private interface DrawColor {
		Color accept(float relativeX, float relativeY, int indexX);
	}

	private enum InputSource {EXTERNAL, HSB, COLOR, RGB}
}
