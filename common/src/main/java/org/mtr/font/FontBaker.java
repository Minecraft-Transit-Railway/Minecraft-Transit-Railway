package org.mtr.font;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public final class FontBaker {

	private static final int FONT_SIZE = 32;

	@Nullable
	public static ObjectObjectImmutablePair<byte[], Int2ObjectAVLTreeMap<FontProvider.GlyphCoordinates>> bakeFont(@Nullable Font preferredFont, int textureIndex, int textureCharCount, int size) {
		final BufferedImage texture = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY);
		final Graphics2D gTexture = texture.createGraphics();
		final Font derivedPreferredFont = preferredFont == null ? null : preferredFont.deriveFont(Font.PLAIN, FONT_SIZE);

		final Int2ObjectAVLTreeMap<FontProvider.GlyphCoordinates> glyphMap = new Int2ObjectAVLTreeMap<>();
		final FontRenderContext fontRenderContext = gTexture.getFontRenderContext();

		int currentX = 0;
		int currentY = 0;
		int maxRowHeight = 0;

		for (int i = 0; i < textureCharCount; i++) {
			final char c = (char) (textureCharCount * textureIndex + i);
			final Font font = getFont(derivedPreferredFont, c);

			if (font == null) {
				continue;
			}

			final TextLayout textLayout = new TextLayout(String.valueOf(c), font, fontRenderContext);
			final Rectangle2D bounds = textLayout.getBounds();

			final int glyphWidth = (int) Math.ceil(bounds.getWidth());
			final int glyphHeight = (int) Math.ceil(bounds.getHeight());
			final int xOffset = (int) Math.ceil(-bounds.getX());
			final int yOffset = (int) Math.ceil(-bounds.getY());
			final int advance = gTexture.getFontMetrics(font).charWidth(c);

			// Handle empty characters
			if (glyphWidth == 0 || glyphHeight == 0) {
				glyphMap.put(c, font.canDisplay(c) ? new FontProvider.GlyphCoordinates(
						0, 0, 0, 0,
						(float) glyphWidth / FONT_SIZE, (float) glyphHeight / FONT_SIZE,
						(float) xOffset / FONT_SIZE, (float) yOffset / FONT_SIZE,
						(float) advance / FONT_SIZE
				) : null);
				continue;
			}

			// Check if glyph fits in current row
			if (currentX + glyphWidth > size) {
				currentY += maxRowHeight;
				currentX = 0;
				maxRowHeight = 0;
			}

			// Check if texture has enough vertical space
			if (currentY + glyphHeight > size) {
				return null;
			}

			// Update max row height
			if (glyphHeight > maxRowHeight) {
				maxRowHeight = glyphHeight;
			}

			// Render glyph to temporary image
			final BufferedImage glyphImage = new BufferedImage(glyphWidth, glyphHeight, BufferedImage.TYPE_BYTE_GRAY);
			final Graphics2D gGlyph = glyphImage.createGraphics();
			gGlyph.setColor(Color.WHITE);
			gGlyph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gGlyph.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			gGlyph.translate(xOffset, yOffset);
			textLayout.draw(gGlyph, 0, 0);
			gGlyph.dispose();

			// Copy glyph to texture
			gTexture.drawImage(glyphImage, currentX, currentY, null);
			glyphImage.flush();

			// Store glyph info
			glyphMap.put(c, new FontProvider.GlyphCoordinates(
					(float) currentX / size, (float) currentY / size,
					(float) (currentX + glyphWidth) / size, (float) (currentY + glyphHeight) / size,
					(float) glyphWidth / FONT_SIZE, (float) glyphHeight / FONT_SIZE,
					(float) xOffset / FONT_SIZE, (float) yOffset / FONT_SIZE,
					(float) advance / FONT_SIZE
			));

			currentX += glyphWidth;
		}

		gTexture.dispose();
		final byte[] pixels = ((DataBufferByte) texture.getRaster().getDataBuffer()).getData();
		texture.flush();
		return new ObjectObjectImmutablePair<>(pixels, glyphMap);
	}

	@Nullable
	private static Font getFont(@Nullable Font preferredFont, char c) {
		if (preferredFont == null) {
			for (final Font testFont : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
				if (testFont.canDisplay(c)) {
					return testFont.deriveFont(Font.PLAIN, FONT_SIZE);
				}
			}
			return null;
		} else {
			return preferredFont;
		}
	}
}
