package org.mtr.mod.screen;

import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.mapper.ScreenExtension;

public class MTRScreenBase extends ScreenExtension {
    protected Screen previousScreen;

    protected MTRScreenBase() {
        super();
    }

    public MTRScreenBase withPreviousScreen(Screen screen) {
        this.previousScreen = screen;
        return this;
    }

    public MTRScreenBase withPreviousScreen(ScreenExtension screenExtension) {
        return withPreviousScreen(new Screen(screenExtension));
    }

    @Override
    public void onClose2() {
        super.onClose2();
        if(previousScreen != null) {
            MinecraftClient.getInstance().openScreen(previousScreen);
        } else {
            MinecraftClient.getInstance().openScreen(null);
        }
    }
}
