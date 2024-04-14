package org.mtr.mod.screen;

import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mod.data.IGui;

public class PIDSDashboardScreen extends ScreenExtension implements IGui {

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        renderBackground(graphicsHolder);
        super.render(graphicsHolder, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen2() {
        return false;
    }
}
