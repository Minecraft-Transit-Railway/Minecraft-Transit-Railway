package org.mtr.core.data;

import org.mtr.mapping.holder.Identifier;

import javax.annotation.Nullable;
import java.util.Locale;

public interface CustomResourceTools extends SerializedDataBase {

	@Nullable
	static Identifier formatIdentifier(String identifierString, String extension) {
		if (identifierString.isEmpty()) {
			return null;
		} else if (identifierString.endsWith("." + extension)) {
			return new Identifier(identifierString);
		} else {
			return new Identifier(String.format("%s.%s", identifierString, extension));
		}
	}

	static int colorStringToInt(String color) {
		try {
			return Integer.parseInt(color.toUpperCase(Locale.ENGLISH).replaceAll("[^\\dA-F]", ""), 16);
		} catch (Exception ignored) {
			return 0;
		}
	}
}
