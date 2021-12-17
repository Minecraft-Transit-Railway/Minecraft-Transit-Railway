package mapper;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class ScreenMapper extends Screen {

	protected ScreenMapper(Component title) {
		super(title);
	}

	public <T extends AbstractWidget> void addDrawableChild(T child) {
		addButton(child);
	}
}
