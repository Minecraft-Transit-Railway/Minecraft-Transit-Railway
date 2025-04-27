package org.mtr.tool;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.ColorHelper;
import org.mtr.core.tool.Utilities;

public final class GuiHelper {

	public static final int BLACK_COLOR = 0xFF000000;
	/**
	 * Alternative dark background colour. Most dark themes don't use pitch black for the background.
	 */
	public static final int BACKGROUND_COLOR = 0xFF111111;
	public static final int BACKGROUND_ACCENT_COLOR = 0xFF333333;
	/**
	 * Mouse hover colour, regardless of if the control is selected or not.
	 */
	public static final int HOVER_COLOR = 0xFF444444;
	public static final int SCROLL_BAR_COLOR = HOVER_COLOR;
	public static final int SCROLL_BAR_HOVER_COLOR = 0xFF888888;
	public static final int LIGHT_GRAY_COLOR = 0xFFAAAAAA;
	public static final int WHITE_COLOR = 0xFFFFFFFF;

	public static final int MINECRAFT_FONT_SIZE = 8;
	public static final int MINECRAFT_TEXT_LINE_HEIGHT = 10;
	public static final int DEFAULT_PADDING = MINECRAFT_FONT_SIZE / 2;
	/**
	 * The standard line size for consistent GUI design. This can be used for both the line height and square button sizes.
	 */
	public static final int DEFAULT_LINE_SIZE = MINECRAFT_FONT_SIZE + DEFAULT_PADDING * 2;

	private static final int SHADOW_COLOR_DARK = 0x11000000;
	private static final int SHADOW_COLOR_LIGHT = 0x11FFFFFF;

	/**
	 * Draws a circle (without antialiasing). This can be used to indicate a platform number.
	 * The circle is drawn by a series of vertical bars. Colours are applied as horizontal stripes across the circle.
	 *
	 * @param drawing    the {@link Drawing} object
	 * @param x          the left coordinate of the circle
	 * @param y          the top coordinate of the circle
	 * @param diameter   the circle's diameter
	 * @param resolution how many vertical bars to use for drawing
	 * @param colors     colours to fill the circle
	 */
	public static void drawCircle(Drawing drawing, double x, double y, double diameter, int resolution, IntArrayList colors) {
		if (diameter <= 0 || resolution < 2 || colors.isEmpty()) {
			return;
		}

		// Create circle parts
		final int halfResolution = resolution / 2;
		final double radius = diameter / 2;
		final double pixelSize = radius / halfResolution;
		final ObjectArrayList<CirclePart> circleParts = new ObjectArrayList<>();

		for (int i = 0; i < halfResolution; i++) {
			final double sliceY = radius - pixelSize * (i + 0.5);
			final double sliceLength = Math.round(Math.sqrt(radius * radius - sliceY * sliceY) / pixelSize) * pixelSize * 2;
			final CirclePart lastCirclePart = Utilities.getElement(circleParts, -1);
			if (lastCirclePart == null || lastCirclePart.sliceLength != sliceLength) {
				circleParts.add(new CirclePart(sliceLength, i * pixelSize, (i + 1) * pixelSize));
			} else {
				lastCirclePart.sliceEnd += pixelSize;
			}
		}

		if (circleParts.isEmpty()) {
			return;
		}

		// Mirror circle parts
		final CirclePart lastCirclePart = circleParts.getLast();
		lastCirclePart.sliceEnd += lastCirclePart.sliceEnd - lastCirclePart.sliceStart;
		for (int i = circleParts.size() - 2; i >= 0; i--) {
			final CirclePart copyCirclePart = circleParts.get(i);
			final CirclePart previousCirclePart = circleParts.getLast();
			circleParts.add(new CirclePart(copyCirclePart.sliceLength, previousCirclePart.sliceEnd, previousCirclePart.sliceEnd + copyCirclePart.sliceEnd - copyCirclePart.sliceStart));
		}

		// Create colour parts
		final double colorSize = diameter / colors.size();
		final ObjectArrayList<CircleColorPart> circleColorParts = new ObjectArrayList<>();

		for (int i = 0; i < colors.size(); i++) {
			circleColorParts.add(new CircleColorPart(i * colorSize, (i + 1) * colorSize, colors.getInt(i)));
		}

		// Draw circle
		circleParts.forEach(circlePart -> {
			int circleColorPartsToRemove = 0;
			final double emptySpace = (diameter - circlePart.sliceLength) / 2;
			for (final CircleColorPart circleColorPart : circleColorParts) {
				if (circleColorPart.sliceStart >= circlePart.sliceEnd) {
					break;
				}
				if (circleColorPart.sliceEnd <= circlePart.sliceEnd) {
					circleColorPartsToRemove++;
				}
				drawing.setVertices(
						x + emptySpace,
						y + Math.max(circlePart.sliceStart, circleColorPart.sliceStart),
						x + diameter - emptySpace,
						y + Math.min(circlePart.sliceEnd, circleColorPart.sliceEnd)
				).setColor(ColorHelper.fullAlpha(circleColorPart.color)).draw();
			}
			for (int i = 0; i < circleColorPartsToRemove; i++) {
				circleColorParts.removeFirst();
			}
		});
	}

	/**
	 * Draws a shadow around an area that fades from a translucent colour to completely transparent.
	 *
	 * @param drawing      the {@link Drawing} object
	 * @param x1           the left coordinate of the area
	 * @param y1           the top coordinate of the area
	 * @param x2           the right coordinate of the area
	 * @param y2           the bottom coordinate of the area
	 * @param shadowRadius how far the shadow should extend; if positive, the shadow is drawn on the outside of the area, if negative, the shadow is drawn on the inside of the area
	 * @param intensity    how strong the shadow is; if positive, a black shadow, if negative, a white shadow
	 */
	public static void drawShadow(Drawing drawing, double x1, double y1, double x2, double y2, int z, double shadowRadius, int intensity) {
		if (intensity != 0) {
			final double r1 = shadowRadius > 0 ? shadowRadius : 0;
			final double r2 = shadowRadius < 0 ? -shadowRadius : 0;
			final int color = ColorHelper.withAlpha(0x11 * Math.abs(intensity), intensity > 0 ? BLACK_COLOR : WHITE_COLOR);
			final int color1 = shadowRadius > 0 ? color : 0;
			final int color2 = shadowRadius < 0 ? color : 0;
			drawing.setVertices(x1 - r1, y1 - r1, z, x1 - r1, y2 + r1, z, x1 + r2, y2 - r2, z, x1 + r2, y1 + r2, z).setColor(color2, color2, color1, color1).draw();
			drawing.setVertices(x2 - r2, y1 + r2, z, x2 - r2, y2 - r2, z, x2 + r1, y2 + r1, z, x2 + r1, y1 - r1, z).setColor(color1, color1, color2, color2).draw();
			drawing.setVertices(x1 - r1, y1 - r1, z, x1 + r2, y1 + r2, z, x2 - r2, y1 + r2, z, x2 + r1, y1 - r1, z).setColor(color2, color1, color1, color2).draw();
			drawing.setVertices(x1 + r2, y2 - r2, z, x1 - r1, y2 + r1, z, x2 + r1, y2 + r1, z, x2 - r2, y2 - r2, z).setColor(color1, color2, color2, color1).draw();
		}
	}

	public static void drawShadowWH(Drawing drawing, int x, int y, int width, int height, int z, double shadowRadius, int intensity) {
		drawShadow(drawing, x, y, x + width, y + height, z, shadowRadius, intensity);
	}

	private static class CirclePart {

		private final double sliceLength;
		private final double sliceStart;
		private double sliceEnd;

		private CirclePart(double sliceLength, double sliceStart, double sliceEnd) {
			this.sliceLength = sliceLength;
			this.sliceStart = sliceStart;
			this.sliceEnd = sliceEnd;
		}
	}

	private record CircleColorPart(double sliceStart, double sliceEnd, int color) {
	}
}
