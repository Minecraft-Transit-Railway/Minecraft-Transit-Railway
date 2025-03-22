package org.mtr.font;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.mtr.MTR;
import org.mtr.resource.ResourceManagerHelper;

import javax.annotation.Nullable;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public final class MinecraftFontResource extends FontResourceBase {

	private final Int2ObjectOpenHashMap<byte[]> charBitmap;

	private static final int MINECRAFT_FONT_SIZE = 8;
	private static final int MINECRAFT_FONT_SCALE = FontProvider.FONT_SIZE / MINECRAFT_FONT_SIZE;

	public MinecraftFontResource(Int2ObjectOpenHashMap<byte[]> charBitmap, int textureIndex, Path path) {
		super(textureIndex, path);
		this.charBitmap = charBitmap;
	}

	@Nullable
	@Override
	protected byte[] generate(@Nullable byte[] oldData) {
		final ByteArrayList byteArrayList1 = new ByteArrayList(FontProvider.TEXTURE_CHAR_COUNT * 4);
		final ByteArrayList byteArrayList2 = new ByteArrayList();
		boolean modified = false;

		for (int i = 0; i < FontProvider.TEXTURE_CHAR_COUNT; i++) {
			final byte[] pixels = charBitmap.get(FontProvider.TEXTURE_CHAR_COUNT * textureIndex + i);

			if (pixels == null) {
				writeInt(byteArrayList1, 0);
			} else {
				writeInt(byteArrayList1, byteArrayList2.size() + FontProvider.TEXTURE_CHAR_COUNT * 4);
				for (final byte pixel : pixels) {
					byteArrayList2.add(pixel);
				}
				modified = true;
			}
		}

		byteArrayList1.addAll(byteArrayList2);
		return modified ? byteArrayList1.toByteArray() : oldData;
	}

	public static void parseMinecraftFontProviders(Identifier identifier, Int2ObjectOpenHashMap<byte[]> charBitmap) {
		ResourceManagerHelper.readResource(identifier, providerInputStream -> {
			try {
				final JsonArray providersArray = JsonParser.parseReader(new InputStreamReader(providerInputStream, StandardCharsets.UTF_8)).getAsJsonObject().getAsJsonArray("providers");
				providersArray.forEach(providerElement -> {
					final JsonObject providerObject = providerElement.getAsJsonObject();
					switch (providerObject.get("type").getAsString()) {
						case "bitmap":
							parseBitmapFont(providerObject, charBitmap);
							break;
						case "space":
							providerObject.getAsJsonObject("advances").entrySet().forEach(entry -> {
								final ByteArrayList byteArrayList = new ByteArrayList();
								addToList(byteArrayList, 0, 0, 0, 0, entry.getValue().getAsInt() * MINECRAFT_FONT_SCALE);
								charBitmap.put(entry.getKey().codePointAt(0), byteArrayList.toByteArray());
							});
							break;
						case "reference":
							parseMinecraftFontProviders(Identifier.of(providerObject.get("id").getAsString() + ".json").withPrefixedPath("font/"), charBitmap);
							break;
						// Minecraft's ttf and unihex types are not implemented for now
					}
				});
			} catch (Exception e) {
				MTR.LOGGER.error("", e);
			}
		});
	}

	private static void parseBitmapFont(JsonObject providerObject, Int2ObjectOpenHashMap<byte[]> charBitmap) {
		// Count expected columns in the texture
		final int[] charsPerRow = {0};
		final ObjectArrayList<String> charsRows = new ObjectArrayList<>();
		providerObject.getAsJsonArray("chars").forEach(charsElement -> {
			final String charsRow = charsElement.getAsString();
			charsRows.add(charsRow);
			charsPerRow[0] = Math.max(charsPerRow[0], charsRow.codePointCount(0, charsRow.length()));
		});

		final int rawHeight = providerObject.has("height") ? providerObject.get("height").getAsInt() : MINECRAFT_FONT_SIZE;
		final int ascent = providerObject.get("ascent").getAsInt();

		// Read the font bitmap
		ResourceManagerHelper.readResource(Identifier.of(providerObject.get("file").getAsString()).withPrefixedPath("textures/"), imageInputStream -> {
			try (MemoryStack memoryStack = MemoryStack.stackPush()) {
				final byte[] imageData = IOUtils.toByteArray(imageInputStream);
				final ByteBuffer inputByteBuffer = ByteBuffer.allocateDirect(imageData.length).put(imageData);
				inputByteBuffer.flip();

				final IntBuffer widthBuffer = memoryStack.mallocInt(1);
				final IntBuffer heightBuffer = memoryStack.mallocInt(1);
				final IntBuffer channelsBuffer = memoryStack.mallocInt(1);
				final ByteBuffer imageByteBuffer = STBImage.stbi_load_from_memory(inputByteBuffer, widthBuffer, heightBuffer, channelsBuffer, 4);

				if (imageByteBuffer != null) {
					final int width = widthBuffer.get();
					final int height = heightBuffer.get();
					final int glyphWidth = width / charsPerRow[0];
					final int glyphHeight = height / charsRows.size();
					final int additionalScale = MINECRAFT_FONT_SCALE * rawHeight / glyphHeight;

					// Read the pixels for each glyph
					for (int glyphRow = 0; glyphRow < charsRows.size(); glyphRow++) {
						final int[] charsRow = charsRows.get(glyphRow).codePoints().toArray();
						for (int glyphColumn = 0; glyphColumn < charsRow.length; glyphColumn++) {
							final int character = charsRow[glyphColumn];
							if (character > 0 && !charBitmap.containsKey(character)) {
								final int[] pixels = new int[glyphWidth * glyphHeight];
								int totalColumnsWithPixels = 0;

								for (int x = 0; x < glyphWidth; x++) {
									for (int y = 0; y < glyphHeight; y++) {
										final int index = glyphColumn * glyphWidth + x + (glyphRow * glyphHeight + y) * width;
										final int color = imageByteBuffer.get(index * 4 + 3) & 0xFF;
										pixels[x + y * glyphWidth] = color;
										if (color > 0) {
											totalColumnsWithPixels = x + 1;
										}
									}
								}

								// Write to the byte array
								final ByteArrayList byteArrayList = new ByteArrayList();
								addToList(byteArrayList, glyphWidth, glyphHeight, additionalScale, ascent * MINECRAFT_FONT_SCALE, totalColumnsWithPixels * additionalScale + (totalColumnsWithPixels > 0 ? MINECRAFT_FONT_SCALE : 0));
								writeImage(pixels, (color, count) -> {
									byteArrayList.add((byte) (color & 0xFF));
									byteArrayList.add((byte) count);
								});
								charBitmap.put(character, byteArrayList.toByteArray());
							}
						}
					}
				}
			} catch (Exception e) {
				MTR.LOGGER.error("", e);
			}
		});
	}
}
