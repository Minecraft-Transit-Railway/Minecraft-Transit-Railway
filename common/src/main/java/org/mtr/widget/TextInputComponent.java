package org.mtr.widget;

import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.input.UITextInput;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMatrixStack;
import kotlin.Unit;
import net.minecraft.client.MinecraftClient;
import org.mtr.tool.ReleasedDynamicTextureManager;

import java.awt.*;

public final class TextInputComponent extends StitchedImageComponent {

	private boolean disabled;

	private final UIText prefixText;
	private final NewTextInput newTextInput;
	private final UIText suffixText;

	private static final int PADDING = 8;
	private static final int CURSOR_FLASH_TIME = 1000;

	public TextInputComponent() {
		super(200, 20, 1, 0, ReleasedDynamicTextureManager.BUTTON_DISABLED_TEXTURE.get());

		final UIContainer container = (UIContainer) new UIContainer()
				.setChildOf(this)
				.setX(new CenterConstraint())
				.setY(new CenterConstraint())
				.setWidth(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(PADDING)))
				.setHeight(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(PADDING)));

		prefixText = (UIText) new UIText("", false)
				.setChildOf(container)
				.setX(new PixelConstraint(0))
				.setY(new CenterConstraint());

		newTextInput = (NewTextInput) new NewTextInput()
				.setChildOf(container)
				.setX(new SiblingConstraint())
				.setY(new CenterConstraint())
				.setWidth(new FillConstraint(true));

		suffixText = (UIText) new UIText("", false)
				.setChildOf(container)
				.setX(new PixelConstraint(0, true))
				.setY(new CenterConstraint());

		onMouseClickConsumer(clickEvent -> {
			if (disabled) {
				newTextInput.releaseWindowFocus();
			} else {
				if (clickEvent.getMouseButton() == 1) {
					newTextInput.setText("");
				}
				newTextInput.grabWindowFocus();
			}
		});

		onMouseEnterRunnable(() -> setHighlighted(true));
		onMouseLeaveRunnable(() -> setHighlighted(false));
		setHighlighted(false);
		setDisabled(false);
	}

	public void onChange(Runnable runnable) {
		newTextInput.onKeyTypeConsumer((character, integer) -> runnable.run());
	}

	public void onFocusLost(Runnable runnable) {
		newTextInput.onFocusLost(component -> {
			runnable.run();
			return Unit.INSTANCE;
		});
	}

	public String getText() {
		return newTextInput.getText();
	}

	public void setText(String text) {
		newTextInput.setText(text);
	}

	public void setPrefix(String text) {
		prefixText.setText(text);
	}

	public void setSuffix(String text) {
		suffixText.setText(text);
	}

	private void setHighlighted(boolean highlighted) {
		setBackgroundColor(!disabled && highlighted ? null : Color.BLACK);
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		newTextInput.setColor(disabled ? Color.DARK_GRAY : Color.WHITE);
		prefixText.setColor(disabled ? Color.DARK_GRAY : Color.LIGHT_GRAY);
		suffixText.setColor(disabled ? Color.DARK_GRAY : Color.LIGHT_GRAY);
		setHighlighted(false);
		if (disabled) {
			newTextInput.releaseWindowFocus();
		}
	}

	private static class NewTextInput extends UITextInput {

		private long lastCursorChangeTime;

		public NewTextInput() {
			super("", false);
			onKeyTypeConsumer((character, integer) -> lastCursorChangeTime = System.currentTimeMillis());
			getMouseScrollListeners().clear();
		}

		@Override
		public void beforeDraw(UMatrixStack matrixStack) {
			super.beforeDraw(matrixStack);
			final long currentTime = System.currentTimeMillis();

			if (isActive() && (currentTime - lastCursorChangeTime < CURSOR_FLASH_TIME || currentTime % CURSOR_FLASH_TIME < CURSOR_FLASH_TIME / 2)) {
				getCursorComponent().setColor(getCursorColor()).setWidth(new PixelConstraint(1 / (float) MinecraftClient.getInstance().getWindow().getScaleFactor()));
			} else {
				getCursorComponent().setColor(new Color(0, 0, 0, 0));
			}
		}

		@Override
		protected void animateCursor() {
		}
	}
}
