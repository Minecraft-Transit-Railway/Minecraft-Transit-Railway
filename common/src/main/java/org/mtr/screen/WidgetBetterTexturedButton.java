package org.mtr.screen;

import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.util.Identifier;
import org.mtr.data.IGui;

public class WidgetBetterTexturedButton extends TexturedButtonWidget implements IGui {

	private final boolean playSound;

	public WidgetBetterTexturedButton(Identifier normalTexture, Identifier highlightedTexture, PressAction onPress, boolean playSound) {
		super(0, 0, 0, SQUARE_SIZE, new ButtonTextures(normalTexture, highlightedTexture), onPress);
		this.playSound = playSound;
	}

	public WidgetBetterTexturedButton(Identifier normalTexture, Identifier highlightedTexture, Identifier disabledTexture, PressAction onPress, boolean playSound) {
		super(0, 0, 0, SQUARE_SIZE, new ButtonTextures(normalTexture, disabledTexture, highlightedTexture), onPress);
		this.playSound = playSound;
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
		if (playSound) {
			super.playDownSound(soundManager);
		}
	}
}
