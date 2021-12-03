package mapper;

import mtr.gui.WidgetMap;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ScreenMapper extends Screen {

	protected ScreenMapper(Text title) {
		super(title);
	}

	public void addChild(WidgetMap widgetMap) {
		addDrawableChild(widgetMap);
	}
}
