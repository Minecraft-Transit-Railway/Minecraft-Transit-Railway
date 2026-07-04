package org.mtr.mixin;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EditBox.class)
public interface TextFieldSelectionEndAccessor {

	@Accessor("highlightPos")
	int getHighlightPos();
}
