package org.mtr.mod.resource;

import org.mtr.core.tool.Utilities;
import org.mtr.mod.data.IGui;

import javax.annotation.Nullable;
import java.util.function.Function;

public enum DisplayOption {

	NONE(null),
	SINGLE_LINE(Utilities::formatName), UPPER_CASE(String::toUpperCase), SPACE_CJK(text -> {
		final String[] textSplit = text.split("\\|");
		for (int i = 0; i < textSplit.length; i++) {
			if (textSplit[i].length() == 2 && IGui.isCjk(textSplit[i])) {
				textSplit[i] = String.format("%s %s", textSplit[i].charAt(0), textSplit[i].charAt(1));
			}
		}
		return String.join("|", textSplit);
	}),
	SCROLL_NORMAL(null), SCROLL_LIGHT_RAIL(null),
	SEVEN_SEGMENT(text -> text.replaceAll("\\D", "")),
	ALIGN_LEFT_CJK(null), ALIGN_RIGHT_CJK(null),
	ALIGN_LEFT(null), ALIGN_RIGHT(null),
	ALIGN_TOP(null), ALIGN_BOTTOM(null),
	CYCLE_LANGUAGES(text -> {
		final String[] textSplit = text.split("\\|");
		return textSplit.length == 0 ? "" : textSplit[(int) ((System.currentTimeMillis() / 1000) % textSplit.length)];
	});

	@Nullable
	private final Function<String, String> format;

	DisplayOption(@Nullable Function<String, String> format) {
		this.format = format;
	}

	public String format(String text) {
		return format == null ? text : format.apply(text);
	}
}
