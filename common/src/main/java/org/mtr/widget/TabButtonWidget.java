package org.mtr.widget;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.mtr.font.FontRenderOptions;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;

public final class TabButtonWidget extends BetterButtonWidget {

	private boolean selected = false;

	public TabButtonWidget(Text message, int width, Runnable onPress) {
		super(message, width, onPress);
	}

	/**
	 * Marks the button as selected; only changes the visual appearance.
	 * This is different from {@link ButtonWidget#isSelected()}.
	 *
	 * @param selected whether this button should be selected
	 */
	public void select(boolean selected) {
		this.selected = selected;
	}

	@Override
	protected void renderAdditional(Drawing drawing) {
		drawing.setVerticesWH(getX(), height - 1, getWidth(), 1).setColor(selected ? GuiHelper.WHITE_COLOR : GuiHelper.BLACK_COLOR).draw();
	}

	@Override
	protected FontRenderOptions.FontRenderOptionsBuilder createFontRenderOptionsBuilder() {
		return super.createFontRenderOptionsBuilder().color(selected ? GuiHelper.WHITE_COLOR : GuiHelper.LIGHT_GRAY_COLOR);
	}
}
