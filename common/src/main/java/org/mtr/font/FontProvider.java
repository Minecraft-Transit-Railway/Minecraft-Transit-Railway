package org.mtr.font;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.mtr.MTR;
import org.mtr.cache.CachedFileProvider;
import org.mtr.resource.ResourceManagerHelper;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Locale;

public final class FontProvider extends CachedFileProvider<FontResourceBase> {

	@Nullable
	private final Font font;
	@Nullable
	private final Int2ObjectOpenHashMap<byte[]> charBitmap;

	public FontProvider(@Nullable Identifier fontFileIdentifier) {
		super(MinecraftClient.getInstance().runDirectory.toPath().resolve("config/cache/font").resolve(fontFileIdentifier == null ? "fallback" : fontFileIdentifier.getPath().toLowerCase(Locale.ENGLISH).replaceAll("[^a-z0-9_-]", "_")));

		// Read font file from assets/mtr/font (including from resource packs)
		final Font[] tempFont = {null};
		final Int2ObjectOpenHashMap<byte[]> tempCharBitmap = new Int2ObjectOpenHashMap<>();
		if (fontFileIdentifier != null) {
			final String[] fileSplit = fontFileIdentifier.getPath().split("\\.");
			final String extension = fileSplit[fileSplit.length - 1].toLowerCase(Locale.ENGLISH);

			if (extension.equals("json")) {
				// Read file as a Minecraft font JSON
				MinecraftFontResource.parseMinecraftFontProviders(fontFileIdentifier, tempCharBitmap);
			} else {
				// Read file directly as a font file
				ResourceManagerHelper.readResource(fontFileIdentifier, inputStream -> {
					try {
						tempFont[0] = Font.createFont(Font.PLAIN, inputStream);
					} catch (Exception e) {
						MTR.LOGGER.error("", e);
					}
				});
			}
		}

		font = tempFont[0];
		charBitmap = tempCharBitmap.isEmpty() ? null : tempCharBitmap;
	}

	@Nullable
	public IntObjectImmutablePair<byte[]> render(int character) {
		final int textureIndex = character / FontResourceBase.TEXTURE_CHAR_COUNT;
		final FontResourceBase fontResourceBase = get(textureIndex, cacheDirectory -> {
			if (charBitmap == null) {
				return new FileFontResource(font, textureIndex, cacheDirectory.resolve(String.valueOf(textureIndex)));
			} else {
				return new MinecraftFontResource(charBitmap, textureIndex, cacheDirectory.resolve(String.valueOf(textureIndex)));
			}
		});
		if (fontResourceBase == null || fontResourceBase.getData() == null) {
			return null;
		} else {
			final int index = FileFontResource.getInt(fontResourceBase.getData(), (character - textureIndex * FontResourceBase.TEXTURE_CHAR_COUNT) * 4);
			return index == 0 ? null : new IntObjectImmutablePair<>(index, fontResourceBase.getData());
		}
	}

	public boolean isFileFont() {
		return charBitmap == null;
	}
}
