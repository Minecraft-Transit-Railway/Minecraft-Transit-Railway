package org.mtr.font;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import org.mtr.cache.CachedFileResource;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

public final class FontResource extends CachedFileResource {

	@Nullable
	private final Font preferredFont;
	private final int textureIndex;

	private static final int LIFESPAN = 60000;

	public FontResource(@Nullable Font preferredFont, int textureIndex, Path path) {
		super(path, LIFESPAN);
		this.preferredFont = preferredFont;
		this.textureIndex = textureIndex;
	}

	@Override
	protected byte[] generate(@Nullable byte[] oldData) {
		final ByteArrayList byteArrayList1 = new ByteArrayList(FontProvider.TEXTURE_CHAR_COUNT * 4);
		final ByteArrayList byteArrayList2 = new ByteArrayList();
		final Font derivedPreferredFont = preferredFont == null ? null : preferredFont.deriveFont(Font.PLAIN, FontProvider.FONT_SIZE);
		boolean modified = false;

		for (int i = 0; i < FontProvider.TEXTURE_CHAR_COUNT; i++) {
			final char c = (char) (FontProvider.TEXTURE_CHAR_COUNT * textureIndex + i);
			final Font font = getFont(derivedPreferredFont, c);

			if (font != null && font.canDisplay(c)) {
				final BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
				final Graphics2D dummyGraphics = dummyImage.createGraphics();
				final TextLayout textLayout = new TextLayout(String.valueOf(c), font, dummyGraphics.getFontRenderContext());
				final Rectangle2D bounds = textLayout.getBounds();
				final int glyphWidth = (int) Math.ceil(bounds.getWidth());
				final int glyphHeight = (int) Math.ceil(bounds.getHeight());

				if (font.canDisplay(c)) {
					writeInt(byteArrayList1, byteArrayList2.size() + FontProvider.TEXTURE_CHAR_COUNT * 4);
					final int advance = dummyGraphics.getFontMetrics(font).charWidth(c);
					modified = true;

					if (glyphWidth > 0 && glyphHeight > 0) {
						final int xOffset = (int) Math.ceil(-bounds.getX());
						final int yOffset = (int) Math.ceil(-bounds.getY());
						addToList(byteArrayList2, glyphWidth, glyphHeight, xOffset, yOffset, advance);

						final BufferedImage glyphImage = new BufferedImage(glyphWidth, glyphHeight, BufferedImage.TYPE_BYTE_GRAY);
						final Graphics2D glyphGraphics = glyphImage.createGraphics();

						glyphGraphics.setColor(Color.WHITE);
						glyphGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						glyphGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
						textLayout.draw(glyphGraphics, xOffset, yOffset);

						writeImage(glyphImage.getRGB(0, 0, glyphWidth, glyphHeight, null, 0, glyphWidth), (color, count) -> {
							byteArrayList2.add((byte) (color & 0xFF));
							byteArrayList2.add((byte) count);
						});

						glyphGraphics.dispose();
						glyphImage.flush();
					} else {
						addToList(byteArrayList2, 0, 0, 0, 0, advance);
					}
				} else {
					writeInt(byteArrayList1, 0);
				}

				dummyGraphics.dispose();
				dummyImage.flush();
			} else {
				writeInt(byteArrayList1, 0);
			}
		}

		byteArrayList1.addAll(byteArrayList2);
		return modified ? byteArrayList1.toByteArray() : oldData;
	}

	@Nullable
	private static Font getFont(@Nullable Font preferredFont, char c) {
		if (preferredFont == null) {
			for (final Font testFont : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
				if (testFont.canDisplay(c)) {
					return testFont.deriveFont(Font.PLAIN, FontProvider.FONT_SIZE);
				}
			}
			return null;
		} else {
			return preferredFont;
		}
	}

	private static void addToList(ByteArrayList byteArrayList, int glyphWidth, int glyphHeight, int xOffset, int yOffset, int advance) {
		byteArrayList.add((byte) glyphWidth);
		byteArrayList.add((byte) glyphHeight);
		byteArrayList.add((byte) xOffset);
		byteArrayList.add((byte) yOffset);
		byteArrayList.add((byte) advance);
	}
}
