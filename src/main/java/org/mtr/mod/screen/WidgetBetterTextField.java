package mtr.screen;

import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;

import java.util.Locale;
import java.util.function.Consumer;

public class WidgetBetterTextField extends EditBox implements IGui {

	private final String filter;
	private final String suggestion;
	private final int newMaxLength;

	private static final int DEFAULT_MAX_LENGTH = 128;

	public WidgetBetterTextField(String suggestion) {
		this("", suggestion, DEFAULT_MAX_LENGTH);
	}

	public WidgetBetterTextField(String suggestion, int maxLength) {
		this("", suggestion, maxLength);
	}

	public WidgetBetterTextField(TextFieldFilter textFieldFilter, String suggestion, int maxLength) {
		this(textFieldFilter.filter, suggestion, maxLength);
	}

	public WidgetBetterTextField(String filter, String suggestion, int maxLength) {
		super(Minecraft.getInstance().font, 0, 0, 0, SQUARE_SIZE, Text.literal(""));
		this.filter = filter;
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
			if (filter.isEmpty()) {
				newText = trySetLength(text);
			} else {
				newText = trySetLength(text.toUpperCase(Locale.ENGLISH).replaceAll(filter, ""));
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
		if (isVisible() && RailwayData.isBetween(mouseX, UtilitiesClient.getWidgetX(this), UtilitiesClient.getWidgetX(this) + width) && RailwayData.isBetween(mouseY, UtilitiesClient.getWidgetY(this), UtilitiesClient.getWidgetY(this) + height)) {
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
		POSITIVE_INTEGER("\\D"), INTEGER("[^-\\d]"), HEX("[^\\dA-F]"), LETTER("[^A-Z]");

		private final String filter;

		TextFieldFilter(String filter) {
			this.filter = filter;
		}
	}
}
