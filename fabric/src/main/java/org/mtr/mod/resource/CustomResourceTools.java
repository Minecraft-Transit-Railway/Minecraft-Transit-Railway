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
		} else {
			return new Identifier(String.format("%s.%s", newIdentifierString.split("\\.")[0], extension));
		}
	}

	static Identifier formatIdentifierWithDefault(String identifierString, String extension) {
		final Identifier identifier = formatIdentifier(identifierString, extension);
		return identifier == null ? new Identifier(Init.MOD_ID, "textures/block/transparent.png") : identifier;
	}

	static String formatIdentifierString(String text) {
		return Arrays.stream(text.toLowerCase(Locale.ENGLISH).split(":")).map(textPart -> textPart.replaceAll("[^a-z0-9/._-]", "_")).collect(Collectors.joining(":"));
	}

	static Identifier getResourceFromSamePath(String basePath, String resource, String extension) {
		if (resource.contains(":")) { // Assume it is already an identifier
			return formatIdentifierWithDefault(resource, extension);
		} else {
			final String[] resourceSplit = basePath.split("/");
			resourceSplit[resourceSplit.length - 1] = resource;
			return formatIdentifierWithDefault(String.join("/", resourceSplit), extension);
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
