package mtr.screen;

import mtr.data.IGui;
import mtr.data.RailwayData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.TextComponent;

import java.util.function.Consumer;

public class WidgetBetterTextField extends EditBox implements IGui {

	private final TextFieldFilter textFieldFilter;
	private final String suggestion;
	private final int newMaxLength;

	private static final int DEFAULT_MAX_LENGTH = 128;

	public WidgetBetterTextField(TextFieldFilter textFieldFilter, String suggestion) {
		this(textFieldFilter, suggestion, DEFAULT_MAX_LENGTH);
	}

	public WidgetBetterTextField(TextFieldFilter textFieldFilter, String suggestion, int maxLength) {
		super(Minecraft.getInstance().font, 0, 0, 0, SQUARE_SIZE, new TextComponent(""));
		this.textFieldFilter = textFieldFilter;
		this.suggestion = suggestion;
		newMaxLength = maxLength;
		setResponder(text -> {
		});
		setMaxLength(0);
	}

	@Override
	public void setResponder(Consumer<String> changedListener) {
		super.setResponder(text -> {
			final String newText;
			if (textFieldFilter == null) {
				newText = trySetLength(text);
			} else {
				newText = trySetLength(text.toUpperCase().replaceAll(textFieldFilter.filter, ""));
				if (!newText.equals(text)) {
					setValue(newText);
				}
			}
			setSuggestion(newText.isEmpty() && suggestion != null ? suggestion : "");
			changedListener.accept(newText);
		});
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (isVisible() && RailwayData.isBetween(mouseX, x, x + width) && RailwayData.isBetween(mouseY, y, y + height)) {
			if (button == 1) {
				setValue("");
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
		return text.isEmpty() ? "" : text.substring(0, Math.min(newMaxLength, text.length()));
	}

	public enum TextFieldFilter {
		POSITIVE_INTEGER("[^0-9]"), POSITIVE_FLOATING_POINT("[^0-9.]"), INTEGER("[^-0-9]"), HEX("[^0-9A-F]"), LETTER("[^A-Z]");

		private final String filter;

		TextFieldFilter(String filter) {
			this.filter = filter;
		}
	}
}
