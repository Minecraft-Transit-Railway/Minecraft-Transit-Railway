package org.mtr.widget;

import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMatrixStack;
import net.minecraft.client.MinecraftClient;
import org.mtr.tool.ReleasedDynamicTextureRegistry;

import javax.annotation.Nullable;

public final class ButtonComponent extends StitchedImageComponent {

	public boolean disabled = false;
	private float availableTextWidth;
	private String text = "";

	@Nullable
	private final UIText textLabel;
	@Nullable
	private final UIWrappedText wrappedTextLabel;

	private static final int TEXT_PADDING = 10;

	public ButtonComponent(boolean wrapText) {
		super(200, 20, 4, 0, ReleasedDynamicTextureRegistry.BUTTON_TEXTURE.get(), ReleasedDynamicTextureRegistry.BUTTON_DISABLED_TEXTURE.get(), ReleasedDynamicTextureRegistry.BUTTON_HIGHLIGHTED_TEXTURE.get());

		if (wrapText) {
			textLabel = null;
			wrappedTextLabel = (UIWrappedText) new UIWrappedText("", true, null, true)
					.setChildOf(this)
					.setX(new CenterConstraint())
					.setY(new CenterConstraint())
					.setWidth(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(TEXT_PADDING)));
		} else {
			textLabel = (UIText) new UIText("", true)
					.setChildOf(this)
					.setX(new CenterConstraint())
					.setY(new CenterConstraint());
			wrappedTextLabel = null;
		}

		setHeight(new AdditiveConstraint(new ChildBasedSizeConstraint(), new PixelConstraint(TEXT_PADDING)));
		onMouseEnterRunnable(() -> setHighlighted(true));
		onMouseLeaveRunnable(() -> setHighlighted(false));
		setHighlighted(false);
	}

	@Override
	public void beforeDraw(UMatrixStack matrixStack) {
		super.beforeDraw(matrixStack);
		final float newTextWidth = getWidth() - TEXT_PADDING;

		if (textLabel != null && newTextWidth != availableTextWidth) {
			availableTextWidth = newTextWidth;
			final int textLength = text.length();
			int trimIndex = textLength;

			while (trimIndex > 0) {
				final String checkText = text.substring(0, trimIndex).trim() + (trimIndex < textLength ? "..." : "");
				if (MinecraftClient.getInstance().textRenderer.getWidth(checkText) <= newTextWidth) {
					textLabel.setText(checkText);
					break;
				}
				trimIndex--;
			}

			if (trimIndex == 0) {
				textLabel.setText("");
			}
		}
	}

	public void onClick(Runnable runnable) {
		onMouseClickConsumer(clickEvent -> {
			if (!disabled) {
				runnable.run();
			}
		});
	}

	public void setText(String text) {
		this.text = text;
		availableTextWidth = 0;
		if (wrappedTextLabel != null) {
			wrappedTextLabel.setText(text);
		}
	}

	private void setHighlighted(boolean highlighted) {
		setActiveTexture(disabled ? 1 : (highlighted ? 2 : 0));
	}
}
