package org.mtr.mod.screen;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.PressAction;
import org.mtr.mapping.mapper.TexturedButtonWidgetExtension;

/**
 * This is a helper class to create {@link TexturedButtonWidgetExtension} elements.
 * If no disabled button texture is provided, the normal texture will be used (although it's not good practice to not have a disabled texture).
 */
public interface TexturedButtonWidgetHelper {

	static TexturedButtonWidgetExtension create(int x, int y, int width, int height, Identifier normalTexture, Identifier highlightedTexture, Identifier disabledTexture, PressAction onPress) {
		return new TexturedButtonWidgetExtension(x, y, width, height, normalTexture, highlightedTexture, disabledTexture, onPress);
	}

	static TexturedButtonWidgetExtension create(int x, int y, int width, int height, Identifier normalTexture, Identifier highlightedTexture, Identifier disabledTexture, PressAction onPress, String message) {
		return new TexturedButtonWidgetExtension(x, y, width, height, normalTexture, highlightedTexture, disabledTexture, onPress, message);
	}

	static TexturedButtonWidgetExtension create(int x, int y, int width, int height, Identifier normalTexture, Identifier highlightedTexture, Identifier disabledTexture, PressAction onPress, MutableText message) {
		return new TexturedButtonWidgetExtension(x, y, width, height, normalTexture, highlightedTexture, disabledTexture, onPress, message);
	}

	static TexturedButtonWidgetExtension create(int x, int y, int width, int height, Identifier normalTexture, Identifier highlightedTexture, PressAction onPress) {
		return new TexturedButtonWidgetExtension(x, y, width, height, normalTexture, highlightedTexture, normalTexture, onPress);
	}

	static TexturedButtonWidgetExtension create(int x, int y, int width, int height, Identifier normalTexture, Identifier highlightedTexture, PressAction onPress, String message) {
		return new TexturedButtonWidgetExtension(x, y, width, height, normalTexture, highlightedTexture, normalTexture, onPress, message);
	}

	static TexturedButtonWidgetExtension create(int x, int y, int width, int height, Identifier normalTexture, Identifier highlightedTexture, PressAction onPress, MutableText message) {
		return new TexturedButtonWidgetExtension(x, y, width, height, normalTexture, highlightedTexture, normalTexture, onPress, message);
	}
}
