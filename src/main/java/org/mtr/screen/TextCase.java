package org.mtr.screen;

import java.util.Locale;
import java.util.function.Function;

public enum TextCase {
	DEFAULT(text -> text), UPPER(text -> text.toUpperCase(Locale.ENGLISH)), LOWER(text -> text.toLowerCase(Locale.ENGLISH));

	public final Function<String, String> convert;

	TextCase(Function<String, String> convert) {
		this.convert = convert;
	}
}
