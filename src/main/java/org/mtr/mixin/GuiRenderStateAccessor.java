package org.mtr.mixin;

//? if >= 26.1 {

/*import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// Reaches the render state a screen is drawing into.
//
// From 26.1 a screen does not draw, it records what it wants drawn, and custom geometry is
// recorded by handing a GuiElementRenderState to that render state. The field holding it is
// private with no accessor, and NeoForge does not open it either, so it is opened here rather
// than through a loader specific access widener, which would have to be written twice and kept
// in step.
@Mixin(GuiGraphics.class)
public interface GuiRenderStateAccessor {

	@Accessor("guiRenderState")
	GuiRenderState getGuiRenderState();
}

*///? }
