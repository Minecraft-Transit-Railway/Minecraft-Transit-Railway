package org.mtr.font;

import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.mtr.MTR;
import org.mtr.cache.CachedFileProvider;
import org.mtr.resource.ResourceManagerHelper;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Locale;

public final class FontProvider extends CachedFileProvider<FontResource> {

	@Nullable
	private final Font font;

	public static final int TEXTURE_CHAR_COUNT = 256;
	public static final int FONT_SIZE = 32;

	public FontProvider(@Nullable String fontFile) {
		super(MinecraftClient.getInstance().runDirectory.toPath().resolve("config/cache/font").resolve(fontFile == null ? "fallback" : "font_" + fontFile.toLowerCase(Locale.ENGLISH).replaceAll("[^a-z0-9_-]", "_")));

		// Read font file from assets/mtr/font (including from resource packs)
		final Font[] tempFont = {null};
		if (fontFile != null) {
			final Identifier fontFileIdentifier = Identifier.of(MTR.MOD_ID, "font/" + fontFile);
			ResourceManagerHelper.readResource(fontFileIdentifier, inputStream -> {
				try {
					tempFont[0] = Font.createFont(Font.PLAIN, inputStream);
				} catch (Exception e) {
					MTR.LOGGER.error("", e);
				}
			});
		}
		font = tempFont[0];
	}

	@Nullable
	public IntObjectImmutablePair<byte[]> render(char c) {
		final int textureIndex = c / TEXTURE_CHAR_COUNT;
		final byte[] data = get(textureIndex, cacheDirectory -> new FontResource(font, textureIndex, cacheDirectory.resolve(String.valueOf(textureIndex))));
		if (data == null) {
			return null;
		} else {
			final int index = FontResource.getInt(data, (c - textureIndex * TEXTURE_CHAR_COUNT) * 4);
			return index == 0 ? null : new IntObjectImmutablePair<>(index, data);
		}
	}
}
