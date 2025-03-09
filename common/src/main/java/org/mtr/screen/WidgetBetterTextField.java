package org.mtr.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;

import javax.annotation.Nullable;
import javax.annotation.RegEx;
import java.util.function.Consumer;

public class WidgetBetterTextField extends TextFieldWidget implements IGui {

	private final int maxLength;
	private final TextCase textCase;
	private final String filter;
	private final String suggestion;

	public WidgetBetterTextField(int maxLength, TextCase textCase, @RegEx @Nullable String filter, @Nullable String suggestion) {
		this("", maxLength, textCase, filter, suggestion);
	}

	public WidgetBetterTextField(String message, int maxLength, TextCase textCase, @RegEx @Nullable String filter, @Nullable String suggestion) {
		this(Text.literal(message), maxLength, textCase, filter, suggestion);
	}

	public WidgetBetterTextField(MutableText text, int maxLength, TextCase textCase, @RegEx @Nullable String filter, @Nullable String suggestion) {
		super(MinecraftClient.getInstance().textRenderer, 0, 0, 0, SQUARE_SIZE, text);
		this.maxLength = maxLength;
		this.textCase = textCase;
		this.filter = filter;
		this.suggestion = suggestion;
		setChangedListener(value -> {
		});
		setMaxLength(0);
	}

	@Override
	public final void setChangedListener(Consumer<String> changedListener) {
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
	public final void setMaxLength(int maxLength) {
		super.setMaxLength(Integer.MAX_VALUE);
	}

	private String trySetLength(String text) {
		return text.isEmpty() ? "" : text.substring(0, Math.min(maxLength, text.length()));
	}
}
