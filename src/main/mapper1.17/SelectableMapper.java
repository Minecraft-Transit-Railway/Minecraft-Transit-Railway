package mapper;

import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;

public interface SelectableMapper extends Selectable {

	@Override
	default SelectionType getType() {
		return Selectable.SelectionType.NONE;
	}

	@Override
	default void appendNarrations(NarrationMessageBuilder builder) {
	}
}
