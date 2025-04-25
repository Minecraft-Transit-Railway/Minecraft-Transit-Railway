package org.mtr.tool;

public final class GuiHelper {

	public static final int BLACK_COLOR = 0xFF000000;
	/**
	 * Alternative dark background colour. Most dark themes don't use pitch black for the background.
	 */
	public static final int BACKGROUND_COLOR = 0xFF111111;
	/**
	 * Mouse hover colour, regardless of if the control is selected or not.
	 */
	public static final int HOVER_COLOR = 0xFF444444;
	public static final int SCROLL_BAR_COLOR = HOVER_COLOR;
	public static final int SCROLL_BAR_HOVER_COLOR = 0xFF888888;
	public static final int LIGHT_GRAY_COLOR = 0xFFAAAAAA;
	public static final int WHITE_COLOR = 0xFFFFFFFF;

	public static final int MINECRAFT_FONT_SIZE = 8;
	public static final int DEFAULT_PADDING = MINECRAFT_FONT_SIZE / 2;
	/**
	 * The standard line size for consistent GUI design. This can be used for both the line height and square button widths.
	 */
	public static final int DEFAULT_LINE_SIZE = MINECRAFT_FONT_SIZE + DEFAULT_PADDING * 2;

	private static final int SHADOW_COLOR = 0x11000000;

	/**
	 * Draws a shadow around an area that fades from a translucent black colour to completely transparent.
	 * If {@code shadowRadius} is positive, the shadow is drawn on the outside of the area.
	 * If {@code shadowRadius} is negative, the shadow is drawn on the inside of the area.
	 */
	public static void drawShadow(Drawing drawing, double x1, double y1, double x2, double y2, int z, double shadowRadius) {
		final double r1 = shadowRadius > 0 ? shadowRadius : 0;
		final double r2 = shadowRadius < 0 ? -shadowRadius : 0;
		final int color1 = shadowRadius > 0 ? SHADOW_COLOR : 0;
		final int color2 = shadowRadius < 0 ? SHADOW_COLOR : 0;
		drawing.setVertices(x1 - r1, y1 - r1, z, x1 - r1, y2 + r1, z, x1 + r2, y2 - r2, z, x1 + r2, y1 + r2, z).setColor(color2, color2, color1, color1).draw();
		drawing.setVertices(x2 - r2, y1 + r2, z, x2 - r2, y2 - r2, z, x2 + r1, y2 + r1, z, x2 + r1, y1 - r1, z).setColor(color1, color1, color2, color2).draw();
		drawing.setVertices(x1 - r1, y1 - r1, z, x1 + r2, y1 + r2, z, x2 - r2, y1 + r2, z, x2 + r1, y1 - r1, z).setColor(color2, color1, color1, color2).draw();
		drawing.setVertices(x1 + r2, y2 - r2, z, x1 - r1, y2 + r1, z, x2 + r1, y2 + r1, z, x2 - r2, y2 - r2, z).setColor(color1, color2, color2, color1).draw();
	}
}
