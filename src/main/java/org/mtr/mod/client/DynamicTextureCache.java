package org.mtr.mod.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import org.mtr.init.MTR;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.data.IGui;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.net.URLEncoder;
import java.text.AttributedString;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class DynamicTextureCache implements IGui {

	private Font font;
	private Font fontCjk;

	private final Object2ObjectLinkedOpenHashMap<String, DynamicResource> dynamicResources = new Object2ObjectLinkedOpenHashMap<>();
	private final ObjectLinkedOpenHashSet<String> resourcesToRefresh = new ObjectLinkedOpenHashSet<>();
	private final ObjectArrayList<Runnable> resourceRegistryQueue = new ObjectArrayList<>();

	public static DynamicTextureCache instance = new DynamicTextureCache();

	public static final float LINE_HEIGHT_MULTIPLIER = 1.25F;
	private static final Identifier DEFAULT_BLACK_RESOURCE = new Identifier(MTR.MOD_ID, "textures/block/black.png");
	private static final Identifier DEFAULT_WHITE_RESOURCE = new Identifier(MTR.MOD_ID, "textures/block/white.png");
	private static final Identifier DEFAULT_TRANSPARENT_RESOURCE = new Identifier(MTR.MOD_ID, "textures/block/transparent.png");

	public void resetFonts() {
		font = null;
		fontCjk = null;
		refreshDynamicResources();
	}

	public void refreshDynamicResources() {
		Init.LOGGER.info("Refreshing dynamic resources");
		resourcesToRefresh.addAll(dynamicResources.keySet());
	}

	public DynamicResource getPixelatedText(String text, int textColor, int maxWidth, float cjkSizeRatio, boolean fullPixel) {
		return getResource(String.format("pixelated_text_%s_%s_%s_%s_%s", text, textColor, maxWidth, cjkSizeRatio, fullPixel), () -> RouteMapGenerator.generatePixelatedText(text, textColor, maxWidth, cjkSizeRatio, fullPixel), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getColorStrip(long platformId) {
		return getResource(String.format("color_%s", platformId), () -> RouteMapGenerator.generateColorStrip(platformId), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getStationName(String stationName, float aspectRatio) {
		return getResource(String.format("station_name_%s_%s", stationName, aspectRatio), () -> RouteMapGenerator.generateStationName(stationName, aspectRatio), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getTallStationName(int textColor, String stationName, int stationColor, float aspectRatio) {
		return getResource(String.format("tall_station_name_%s_%s_%s_%s", textColor, stationName, stationColor, aspectRatio), () -> RouteMapGenerator.generateTallStationName(textColor, stationName, stationColor, aspectRatio), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getStationNameEntrance(int textColor, String stationName, float aspectRatio) {
		return getResource(String.format("station_name_entrance_%s_%s_%s", textColor, stationName, aspectRatio), () -> RouteMapGenerator.generateStationNameEntrance(textColor, stationName, aspectRatio), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getSingleRowStationName(long platformId, float aspectRatio) {
		return getResource(String.format("single_row_station_name_%s_%s", platformId, aspectRatio), () -> RouteMapGenerator.generateSingleRowStationName(platformId, aspectRatio), DefaultRenderingColor.WHITE);
	}

	public DynamicResource getSignText(String text, IGui.HorizontalAlignment horizontalAlignment, float paddingScale, int backgroundColor, int textColor) {
		return getResource(String.format("sign_text_%s_%s_%s_%s_%s", text, horizontalAlignment, paddingScale, backgroundColor, textColor), () -> RouteMapGenerator.generateSignText(text, horizontalAlignment, paddingScale, backgroundColor, textColor), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getLiftPanelDisplay(String originalText, int textColor) {
		return getResource(String.format("lift_panel_display_%s", originalText), () -> RouteMapGenerator.generateLiftPanel(originalText, textColor), DefaultRenderingColor.BLACK);
	}

	public DynamicResource getExitSignLetter(String exitLetter, String exitNumber, int backgroundColor) {
		return getResource(String.format("exit_sign_letter_%s_%s", exitLetter, exitNumber), () -> RouteMapGenerator.generateExitSignLetter(exitLetter, exitNumber, backgroundColor), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getRouteSquare(int color, String routeName, IGui.HorizontalAlignment horizontalAlignment) {
		return getResource(String.format("route_square_%s_%s_%s", color, routeName, horizontalAlignment), () -> RouteMapGenerator.generateRouteSquare(color, routeName, horizontalAlignment), DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getDirectionArrow(long platformId, boolean hasLeft, boolean hasRight, IGui.HorizontalAlignment horizontalAlignment, boolean showToString, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor) {
		return getResource(String.format("direction_arrow_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", platformId, hasLeft, hasRight, horizontalAlignment, showToString, paddingScale, aspectRatio, backgroundColor, textColor, transparentColor), () -> RouteMapGenerator.generateDirectionArrow(platformId, hasLeft, hasRight, horizontalAlignment, showToString, paddingScale, aspectRatio, backgroundColor, textColor, transparentColor), transparentColor == 0 && backgroundColor == ARGB_WHITE ? DefaultRenderingColor.WHITE : DefaultRenderingColor.TRANSPARENT);
	}

	public DynamicResource getRouteMap(long platformId, boolean vertical, boolean flip, float aspectRatio, boolean transparentWhite) {
		return getResource(String.format("route_map_%s_%s_%s_%s_%s", platformId, vertical, flip, aspectRatio, transparentWhite), () -> RouteMapGenerator.generateRouteMap(platformId, vertical, flip, aspectRatio, transparentWhite), transparentWhite ? DefaultRenderingColor.TRANSPARENT : DefaultRenderingColor.WHITE);
	}

	public byte[] getTextPixels(String text, int[] dimensions, int fontSizeCjk, int fontSize) {
		return getTextPixels(text, dimensions, Integer.MAX_VALUE, (int) (Math.max(fontSizeCjk, fontSize) * LINE_HEIGHT_MULTIPLIER), fontSizeCjk, fontSize, 0, null);
	}

	public byte[] getTextPixels(String text, int[] dimensions, int maxWidth, int maxHeight, int fontSizeCjk, int fontSize, int padding, @Nullable IGui.HorizontalAlignment horizontalAlignment) {
		if (maxWidth <= 0) {
			dimensions[0] = 0;
			dimensions[1] = 0;
			return new byte[0];
		}

		final boolean oneRow = horizontalAlignment == null;
		final String[] defaultTextSplit = IGui.textOrUntitled(text).split("\\|");
		final String[] textSplit;
		if (Config.languageOptions() == 0) {
			textSplit = defaultTextSplit;
		} else {
			final String[] tempTextSplit = Arrays.stream(IGui.textOrUntitled(text).split("\\|")).filter(textPart -> IGui.isCjk(textPart) == (Config.languageOptions() == 1)).toArray(String[]::new);
			textSplit = tempTextSplit.length == 0 ? defaultTextSplit : tempTextSplit;
		}
		final AttributedString[] attributedStrings = new AttributedString[textSplit.length];
		final int[] textWidths = new int[textSplit.length];
		final int[] fontSizes = new int[textSplit.length];
		final FontRenderContext context = new FontRenderContext(new AffineTransform(), false, false);
		int width = 0;
		int height = 0;

		for (int index = 0; index < textSplit.length; index++) {
			final int newFontSize = IGui.isCjk(textSplit[index]) || font.canDisplayUpTo(textSplit[index]) >= 0 ? fontSizeCjk : fontSize;
			attributedStrings[index] = new AttributedString(textSplit[index]);
			fontSizes[index] = newFontSize;

			final Font fontSized = font.deriveFont(Font.PLAIN, newFontSize);
			final Font fontCjkSized = fontCjk.deriveFont(Font.PLAIN, newFontSize);

			for (int characterIndex = 0; characterIndex < textSplit[index].length(); characterIndex++) {
				final char character = textSplit[index].charAt(characterIndex);
				final Font newFont;
				if (fontSized.canDisplay(character)) {
					newFont = fontSized;
				} else if (fontCjkSized.canDisplay(character)) {
					newFont = fontCjkSized;
				} else {
					Font defaultFont = null;
					for (final Font testFont : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
						if (testFont.canDisplay(character)) {
							defaultFont = testFont;
							break;
						}
					}
					newFont = (defaultFont == null ? new Font(null) : defaultFont).deriveFont(Font.PLAIN, newFontSize);
				}
				textWidths[index] += newFont.getStringBounds(textSplit[index].substring(characterIndex, characterIndex + 1), context).getBounds().width;
				attributedStrings[index].addAttribute(TextAttribute.FONT, newFont, characterIndex, characterIndex + 1);
			}

			if (oneRow) {
				if (index > 0) {
					width += padding;
				}
				width += textWidths[index];
				height = Math.max(height, (int) (fontSizes[index] * LINE_HEIGHT_MULTIPLIER));
			} else {
				width = Math.max(width, Math.min(maxWidth, textWidths[index]));
				height += (int) (fontSizes[index] * LINE_HEIGHT_MULTIPLIER);
			}
		}

		int textOffset = 0;
		final int imageHeight = Math.min(height, maxHeight);
		final BufferedImage image = new BufferedImage(width + (oneRow ? 0 : padding * 2), imageHeight + (oneRow ? 0 : padding * 2), BufferedImage.TYPE_BYTE_GRAY);
		final Graphics2D graphics2D = image.createGraphics();
		graphics2D.setColor(Color.WHITE);
		graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		for (int index = 0; index < textSplit.length; index++) {
			if (oneRow) {
				graphics2D.drawString(attributedStrings[index].getIterator(), textOffset, height / LINE_HEIGHT_MULTIPLIER);
				textOffset += textWidths[index] + padding;
			} else {
				final float scaleY = (float) imageHeight / height;
				final float textWidth = Math.min(maxWidth, textWidths[index] * scaleY);
				final float scaleX = textWidth / textWidths[index];
				final AffineTransform stretch = new AffineTransform();
				stretch.concatenate(AffineTransform.getScaleInstance(scaleX, scaleY));
				graphics2D.setTransform(stretch);
				graphics2D.drawString(attributedStrings[index].getIterator(), horizontalAlignment.getOffset(0, textWidth - width) / scaleY + padding / scaleX, textOffset + fontSizes[index] + padding / scaleY);
				textOffset += (int) (fontSizes[index] * LINE_HEIGHT_MULTIPLIER);
			}
		}

		dimensions[0] = width + (oneRow ? 0 : padding * 2);
		dimensions[1] = imageHeight + (oneRow ? 0 : padding * 2);
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		graphics2D.dispose();
		image.flush();
		return pixels;
	}

	private DynamicResource getResource(String key, Supplier<NativeImage> supplier, DefaultRenderingColor defaultRenderingColor) {
		if (font == null) {
			ResourceManagerHelper.readResource(new Identifier(MTR.MOD_ID, "font/noto-sans-semibold.ttf"), inputStream -> {
				try {
					font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
				} catch (Exception e) {
					Init.logException(e);
				}
			});
		}

		if (fontCjk == null) {
			ResourceManagerHelper.readResource(new Identifier(MTR.MOD_ID, "font/noto-serif-cjk-tc-semibold.ttf"), inputStream -> {
				try {
					fontCjk = Font.createFont(Font.TRUETYPE_FONT, inputStream);
				} catch (Exception e) {
					Init.logException(e);
				}
			});
		}

		if (!resourceRegistryQueue.isEmpty()) {
			final Runnable runnable = resourceRegistryQueue.remove(0);
			if (runnable != null) {
				runnable.run();
			}
		}

		final boolean needsRefresh = resourcesToRefresh.contains(key);
		final DynamicResource dynamicResource = dynamicResources.get(key);

		if (dynamicResource != null && !needsRefresh) {
			return dynamicResource;
		}

		RouteMapGenerator.setConstants();
		CompletableFuture.supplyAsync(supplier).thenAccept(nativeImage -> resourceRegistryQueue.add(() -> {
			final DynamicResource staticTextureProviderOld = dynamicResources.get(key);
			if (staticTextureProviderOld != null) {
				staticTextureProviderOld.remove();
			}

			final DynamicResource dynamicResourceNew;
			if (nativeImage == null) {
				dynamicResourceNew = defaultRenderingColor.dynamicResource;
			} else {
				final NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);
				String newKey = key;
				try {
					newKey = URLEncoder.encode(key, "UTF-8");
				} catch (Exception e) {
					Init.logException(e);
				}
				final Identifier identifier = new Identifier(MTR.MOD_ID, "dynamic_texture_" + newKey.toLowerCase(Locale.ENGLISH).replaceAll("[^0-9a-z_]", "_"));
				MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, new AbstractTexture(nativeImageBackedTexture.data));
				dynamicResourceNew = new DynamicResource(identifier, nativeImageBackedTexture);
			}

			dynamicResources.put(key, dynamicResourceNew);
		}));

		if (needsRefresh) {
			resourcesToRefresh.remove(key);
		}

		if (dynamicResource == null) {
			dynamicResources.put(key, defaultRenderingColor.dynamicResource);
			return defaultRenderingColor.dynamicResource;
		} else {
			return dynamicResource;
		}
	}

	public static class DynamicResource {

		public final int width;
		public final int height;
		public final Identifier identifier;

		private DynamicResource(Identifier identifier, @Nullable NativeImageBackedTexture nativeImageBackedTexture) {
			this.identifier = identifier;
			if (nativeImageBackedTexture != null) {
				final NativeImage nativeImage = nativeImageBackedTexture.getImage();
				if (nativeImage != null) {
					width = nativeImage.getWidth();
					height = nativeImage.getHeight();
				} else {
					width = 16;
					height = 16;
				}
			} else {
				width = 16;
				height = 16;
			}
		}

		private void remove() {
			if (!identifier.equals(DEFAULT_BLACK_RESOURCE) && !identifier.equals(DEFAULT_WHITE_RESOURCE) && !identifier.equals(DEFAULT_TRANSPARENT_RESOURCE)) {
				final TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
				textureManager.destroyTexture(identifier);
				final AbstractTexture abstractTexture = textureManager.getTexture(identifier);
				if (abstractTexture != null) {
					abstractTexture.clearGlId();
					abstractTexture.close();
				}
			}
		}
	}

	private enum DefaultRenderingColor {
		BLACK(DEFAULT_BLACK_RESOURCE),
		WHITE(DEFAULT_WHITE_RESOURCE),
		TRANSPARENT(DEFAULT_TRANSPARENT_RESOURCE);

		private final DynamicResource dynamicResource;

		DefaultRenderingColor(Identifier identifier) {
			dynamicResource = new DynamicResource(identifier, null);
		}
	}
}
