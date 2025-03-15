package org.mtr.font;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImageWrite;
import org.mtr.MTR;
import org.mtr.resource.ResourceManagerHelper;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Locale;

public final class FontProvider {

	private final String fontIdPrefix;
	private final Path cacheDirectory;
	@Nullable
	private final Font font;
	private final Int2ObjectAVLTreeMap<Int2ObjectAVLTreeMap<GlyphCoordinates>> charGlyphCoordinates = new Int2ObjectAVLTreeMap<>();

	private static final int TEXTURE_CHAR_COUNT = 256;

	public FontProvider(@Nullable String fontFile) {
		// Set up ID and cache directory
		fontIdPrefix = fontFile == null ? "fallback" : "font_" + fontFile.toLowerCase(Locale.ENGLISH).replaceAll("[^a-z0-9_-]", "_");
		cacheDirectory = MinecraftClient.getInstance().runDirectory.toPath().resolve("config/cache/font").resolve(fontIdPrefix);
		try {
			Files.createDirectories(cacheDirectory);
		} catch (IOException e) {
			MTR.LOGGER.error("", e);
		}

		// Read all images and glyph sizes and offsets already inside the cache directory
		try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(cacheDirectory)) {
			directoryStream.forEach(path -> {
				final String[] fileNameSplit = path.getFileName().toString().split("\\.");
				if (fileNameSplit.length == 2 && fileNameSplit[1].equals("png")) {
					registerTexture(path);
				} else {
					try {
						final int textureIndex = Integer.parseInt(fileNameSplit[0]);
						final Int2ObjectAVLTreeMap<GlyphCoordinates> innerMap = new Int2ObjectAVLTreeMap<>();
						charGlyphCoordinates.put(textureIndex, innerMap);
						final List<String> lines = Files.readAllLines(path);
						if (lines.size() != TEXTURE_CHAR_COUNT) {
							MTR.LOGGER.warn("[{} {}] Font cache might be corrupted!", fontIdPrefix, fileNameSplit[0]);
						}
						for (int i = 0; i < lines.size(); i++) {
							final String line = lines.get(i);
							innerMap.put(textureIndex * TEXTURE_CHAR_COUNT + i, GlyphCoordinates.create(line));
						}
					} catch (Exception e) {
						MTR.LOGGER.error("", e);
					}
				}
			});
		} catch (IOException e) {
			MTR.LOGGER.error("", e);
		}

		// Read all font files from assets/mtr/font (including resource packs)
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
	public ObjectObjectImmutablePair<Identifier, GlyphCoordinates> render(char c) {
		final int textureIndex = c / TEXTURE_CHAR_COUNT;
		final Int2ObjectAVLTreeMap<GlyphCoordinates> innerMap = charGlyphCoordinates.get(textureIndex);
		final GlyphCoordinates glyphCoordinates;

		if (innerMap == null) {
			if (font != null && !font.canDisplay(c)) {
				// Don't generate textures if the font can't render the character anyway
				glyphCoordinates = null;
			} else {
				generateFontTexture(textureIndex);
				glyphCoordinates = charGlyphCoordinates.getOrDefault(textureIndex, new Int2ObjectAVLTreeMap<>()).get(c);
			}
		} else {
			glyphCoordinates = innerMap.get(c);
		}

		if (glyphCoordinates == null) {
			return null;
		} else {
			return new ObjectObjectImmutablePair<>(Identifier.of(MTR.MOD_ID, String.format("%s_%s.png", fontIdPrefix, textureIndex)), glyphCoordinates);
		}
	}

	private void generateFontTexture(int textureIndex) {
		int size = 512;
		while (true) {
			final ObjectObjectImmutablePair<byte[], Int2ObjectAVLTreeMap<FontProvider.GlyphCoordinates>> fontDetails = FontBaker.bakeFont(font, textureIndex, TEXTURE_CHAR_COUNT, size);
			if (fontDetails == null) {
				// Increment texture size until all glyphs fit
				size *= 2;
			} else {
				// Convert to white text on transparent background
				final ByteBuffer bitmapByteBufferConverted = BufferUtils.createByteBuffer(size * size * 4);
				for (int i = 0; i < size * size; i++) {
					bitmapByteBufferConverted.put((byte) 0xFF);
					bitmapByteBufferConverted.put((byte) 0xFF);
					bitmapByteBufferConverted.put((byte) 0xFF);
					bitmapByteBufferConverted.put((byte) (fontDetails.left()[i] & 0xFF));
				}
				bitmapByteBufferConverted.flip();
				final Path texturePath = cacheDirectory.resolve(textureIndex + ".png");
				if (!STBImageWrite.stbi_write_png(texturePath.toString(), size, size, 4, bitmapByteBufferConverted, size * 4)) {
					MTR.LOGGER.error("Failed to write font PNG file");
				}

				// Register the texture to Minecraft
				registerTexture(texturePath);

				// Save glyph sizes and offsets
				charGlyphCoordinates.put(textureIndex, fontDetails.right());

				// Write glyph sizes and offsets to file
				try {
					Files.writeString(cacheDirectory.resolve(String.valueOf(textureIndex)), String.join("\n", fontDetails.right().values().stream().map(glyphCoordinates -> glyphCoordinates == null ? "" : glyphCoordinates.writeLine()).toList()), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
				} catch (IOException e) {
					MTR.LOGGER.error("", e);
				}

				return;
			}
		}
	}

	private void registerTexture(Path path) {
		try {
			final String id = String.format("%s_%s", fontIdPrefix, path.getFileName());
			MinecraftClient.getInstance().getTextureManager().registerTexture(Identifier.of(MTR.MOD_ID, id), new NativeImageBackedTexture(NativeImage.read(Files.readAllBytes(path))));
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	private static String floatToString(float value) {
		if (value == (int) value) {
			return String.valueOf((int) value);
		} else {
			return String.valueOf(value);
		}
	}

	public record GlyphCoordinates(float u1, float v1, float u2, float v2, float width, float height, float xOffset, float yOffset, float xAdvance) {

		@Nullable
		public static GlyphCoordinates create(String line) {
			if (line.isEmpty()) {
				return null;
			} else {
				final String[] lineSplit = line.split(" ");
				return new GlyphCoordinates(
						Float.parseFloat(lineSplit[0]),
						Float.parseFloat(lineSplit[1]),
						Float.parseFloat(lineSplit[2]),
						Float.parseFloat(lineSplit[3]),
						Float.parseFloat(lineSplit[4]),
						Float.parseFloat(lineSplit[5]),
						Float.parseFloat(lineSplit[6]),
						Float.parseFloat(lineSplit[7]),
						Float.parseFloat(lineSplit[8])
				);
			}
		}

		private String writeLine() {
			return String.format("%s %s %s %s %s %s %s %s %s", floatToString(u1), floatToString(v1), floatToString(u2), floatToString(v2), floatToString(width), floatToString(height), floatToString(xOffset), floatToString(yOffset), floatToString(xAdvance));
		}
	}
}
