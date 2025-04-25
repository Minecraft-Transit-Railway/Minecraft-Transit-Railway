package org.mtr.font;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mtr.data.IGui;
import org.mtr.tool.GuiHelper;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class FontRenderOptions {

	/**
	 * Colour of the text (including alpha)
	 */
	@Builder.Default
	private int color = GuiHelper.WHITE_COLOR;
	/**
	 * Colour of the CJK text (including alpha)
	 */
	@Builder.Default
	private int cjkColor = 0;
	/**
	 * Brightness of the text
	 */
	@Builder.Default
	private int light = IGui.DEFAULT_LIGHT;
	/**
	 * The vertical size (in pixels) non-CJK text should be rendered, excluding descent
	 */
	@Builder.Default
	private float maxFontSize = GuiHelper.MINECRAFT_FONT_SIZE;
	/**
	 * How many times bigger the CJK text will be rendered
	 */
	@Builder.Default
	private float cjkScaling = 1;
	/**
	 * How much total horizontal space the text is given (unlimited space for non-positive values)
	 */
	@Builder.Default
	private float horizontalSpace = 0;
	/**
	 * How much total vertical space the text is given (unlimited space for non-positive values)
	 */
	@Builder.Default
	private float verticalSpace = 0;
	/**
	 * Where the total text space should be positioned horizontally
	 */
	@Builder.Default
	private Alignment horizontalPositioning = Alignment.START;
	/**
	 * Where the total text space should be positioned vertically
	 */
	@Builder.Default
	private Alignment verticalPositioning = Alignment.START;
	/**
	 * Where the text should be positioned horizontally inside the space provided
	 */
	@Builder.Default
	private Alignment horizontalTextAlignment = Alignment.START;
	/**
	 * Where the text should be positioned vertically inside the space provided
	 */
	@Builder.Default
	private Alignment verticalTextAlignment = Alignment.START;
	/**
	 * Overflow behaviour for if the text doesn't fit inside the space provided
	 */
	@Builder.Default
	private TextOverflow textOverflow = TextOverflow.NONE;
	/**
	 * Line break behaviour (lines are split based on the {@code |} character)
	 */
	@Builder.Default
	private LineBreak lineBreak = LineBreak.SPLIT;

	public float getHorizontalSpace() {
		return horizontalSpace <= 0 ? Integer.MAX_VALUE : horizontalSpace;
	}

	public float getVerticalSpace() {
		return verticalSpace <= 0 ? Integer.MAX_VALUE : verticalSpace;
	}

	public enum Alignment {
		START(0), CENTER(0.5F), END(1);

		private final float offset;

		Alignment(float offset) {
			this.offset = offset;
		}

		public float getOffset(float dimension) {
			return -dimension * offset;
		}
	}

	public enum TextOverflow {
		/**
		 * No overflow; the text is allowed to run off the edge
		 */
		NONE,
		/**
		 * The text will be compressed horizontally to fit the space
		 */
		COMPRESS,
		/**
		 * The text will be rendered smaller (affecting vertical size) to avoid any horizontal overflow
		 */
		SCALE,
		/**
		 * (not implemented yet)
		 */
		SCROLL
	}

	public enum LineBreak {
		/**
		 * Split text into lines based on the {@code |} character
		 */
		SPLIT,
		/**
		 * Don't split the text; the {@code |} character will be replaced with a space
		 */
		FORCE_ONE_LINE,
		/**
		 * (not implemented yet)
		 */
		ALTERNATE
	}
}
