package org.mtr.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.mtr.core.tool.Utilities;
import org.mtr.screen.TextCase;

import javax.annotation.Nullable;
import javax.annotation.RegEx;
import java.util.function.Consumer;

public final class BetterTextFieldWidget extends TextFieldWidget {

	private final int maxLength;
	private final TextCase textCase;
	private final String filter;
	private final String suggestion;

	public BetterTextFieldWidget(int maxLength, TextCase textCase, @RegEx @Nullable String filter, @Nullable String suggestion) {
		this("", maxLength, textCase, filter, suggestion);
	}

	public BetterTextFieldWidget(String text, int maxLength, TextCase textCase, @RegEx @Nullable String filter, @Nullable String suggestion) {
		super(MinecraftClient.getInstance().textRenderer, 0, 0, 0, 8, Text.literal(""));
		this.maxLength = maxLength;
		this.textCase = textCase;
		this.filter = filter;
		this.suggestion = suggestion;
		setText(text);
		setDrawsBackground(false);
		setChangedListener(value -> {
		});
		setMaxLength(0);
	}

	@Override
	public void setChangedListener(Consumer<String> changedListener) {
		super.setChangedListener(text -> {
			final String newText;
			if (filter == null || filter.isEmpty()) {
				newText = trySetLength(textCase.convert.apply(text));
			} else {
				newText = trySetLength(textCase.convert.apply(text).replaceAll(filter, ""));
				if (!newText.equals(text)) {
					setText(newText);
				}
			}
			setSuggestion(newText.isEmpty() && suggestion != null ? suggestion : "");
			changedListener.accept(newText);
		});
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (isVisible() && Utilities.isBetween(mouseX, getX(), getX() + width) && Utilities.isBetween(mouseY, getY(), getY() + height)) {
			if (button == 1) {
				setText("");
			}
			return super.mouseClicked(mouseX, mouseY, 0);
		} else {
			setFocused(false);
			return false;
		}
	}

	@Override
	public void setMaxLength(int maxLength) {
		super.setMaxLength(Integer.MAX_VALUE);
	}

	private String trySetLength(String text) {
		return text.isEmpty() ? "" : text.substring(0, Math.min(maxLength, text.length()));
	}
}
