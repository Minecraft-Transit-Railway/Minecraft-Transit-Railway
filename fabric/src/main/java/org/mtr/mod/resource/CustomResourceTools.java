package org.mtr.mod.resource;

import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mod.Init;

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

	static Identifier formatIdentifierWithDefault(String identifierString, String extension) {
		final Identifier identifier = formatIdentifier(identifierString, extension);
		return identifier == null ? new Identifier(Init.MOD_ID, "textures/block/transparent.png") : identifier;
	}

	static int colorStringToInt(String color) {
		try {
			return Integer.parseInt(color.toUpperCase(Locale.ENGLISH).replaceAll("[^\\dA-F]", ""), 16);
		} catch (Exception ignored) {
			return 0;
		}
	}
}
