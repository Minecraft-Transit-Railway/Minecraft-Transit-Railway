package org.mtr.font;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mtr.data.IGui;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class FontRenderOptions {

	@Builder.Default
	private int color = IGui.ARGB_WHITE;
	@Builder.Default
	private int light = IGui.DEFAULT_LIGHT;
	@Builder.Default
	private float maxFontSize = 8;
	@Builder.Default
	private boolean cjkScaling = false;
	@Builder.Default
	private float horizontalSpace = 0;
	@Builder.Default
	private float verticalSpace = 0;
	@Builder.Default
	private Alignment horizontalPositioning = Alignment.START;
	@Builder.Default
	private Alignment verticalPositioning = Alignment.START;
	@Builder.Default
	private Alignment horizontalTextAlignment = Alignment.START;
	@Builder.Default
	private Alignment verticalTextAlignment = Alignment.START;
	@Builder.Default
	private TextOverflow textOverflow = TextOverflow.CROP;
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

	public enum TextOverflow {CROP, COMPRESS, SCROLL}

	public enum LineBreak {SPLIT, FORCE_ONE_LINE, ALTERNATE}
}
