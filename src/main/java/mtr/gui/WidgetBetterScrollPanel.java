package mtr.gui;

import io.github.cottonmc.cotton.gui.widget.WScrollBar;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import net.minecraft.util.math.MathHelper;

public class WidgetBetterScrollPanel extends WScrollPanel {

	public WidgetBetterScrollPanel(WWidget widget) {
		super(widget);
		verticalScrollBar = new WidgetBetterScrollBar();
	}

	@Override
	public void onMouseScroll(int x, int y, double amount) {
		verticalScrollBar.onMouseScroll(x, y, amount);
	}

	private static class WidgetBetterScrollBar extends WScrollBar implements IGui {

		public WidgetBetterScrollBar() {
			axis = Axis.VERTICAL;
		}

		@Override
		public void onMouseScroll(int x, int y, double amount) {
			setValue(MathHelper.clamp(value - (int) Math.round(amount * SQUARE_SIZE), 0, maxValue));
		}
	}
}
