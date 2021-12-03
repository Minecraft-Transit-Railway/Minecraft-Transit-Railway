package mapper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class ScreenMapper extends Screen {

	protected ScreenMapper(Text title) {
		super(title);
	}

	public <T extends ClickableWidget> void addDrawableChild(T child) {
		addButton(child);
	}
}
