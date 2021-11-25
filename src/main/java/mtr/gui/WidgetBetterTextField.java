package mtr.gui;

import mtr.data.IGui;
import mtr.data.RailwayData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.LiteralText;

import java.util.function.Consumer;

public class WidgetBetterTextField extends TextFieldWidget implements IGui {

	private final TextFieldFilter textFieldFilter;
	private final String suggestion;

	public WidgetBetterTextField(TextFieldFilter textFieldFilter, String suggestion) {
		super(MinecraftClient.getInstance().textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		this.textFieldFilter = textFieldFilter;
		this.suggestion = suggestion;
		setChangedListener(text -> {
		});
	}

	@Override
	public void setChangedListener(Consumer<String> changedListener) {
		super.setChangedListener(text -> {
			final String newText;
			if (textFieldFilter == null) {
				newText = text;
			} else {
				newText = text.toUpperCase().replaceAll(textFieldFilter.filter, "");
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
		if (isVisible() && RailwayData.isBetween(mouseX, x, x + width) && RailwayData.isBetween(mouseY, y, y + height)) {
			if (button == 1) {
				setText("");
			}
			return super.mouseClicked(mouseX, mouseY, 0);
		} else {
			setTextFieldFocused(false);
			return false;
		}
	}

	public enum TextFieldFilter {
		POSITIVE_INTEGER("[^-0-9]"), INTEGER("[^0-9]"), HEX("[^0-9A-F]"), LETTER("[^A-Z]");

		private final String filter;

		TextFieldFilter(String filter) {
			this.filter = filter;
		}
	}
}
