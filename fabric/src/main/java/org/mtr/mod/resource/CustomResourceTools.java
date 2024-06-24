package org.mtr.mod.resource;

import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mod.Init;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public interface CustomResourceTools extends SerializedDataBase {

	@Nullable
	static Identifier formatIdentifier(String identifierString, String extension) {
		final String newIdentifierString = formatIdentifierString(identifierString);
		if (newIdentifierString.isEmpty()) {
			return null;
		} else if (newIdentifierString.endsWith("." + extension)) {
			return new Identifier(newIdentifierString);
		} else {
			return new Identifier(String.format("%s.%s", newIdentifierString, extension));
		}
	}

	static Identifier formatIdentifierWithDefault(String identifierString, String extension) {
		final Identifier identifier = formatIdentifier(identifierString, extension);
		return identifier == null ? new Identifier(Init.MOD_ID, "textures/block/transparent.png") : identifier;
	}

	static String formatIdentifierString(String text) {
		return Arrays.stream(text.split(":")).map(textPart -> textPart.replaceAll("[^a-z0-9/._-]", "")).collect(Collectors.joining(":"));
	}

	static int colorStringToInt(String color) {
		try {
			return Integer.parseInt(color.toUpperCase(Locale.ENGLISH).replaceAll("[^\\dA-F]", ""), 16);
		} catch (Exception ignored) {
			return 0;
		}
	}
}
