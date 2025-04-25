package org.mtr.widget;

import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.util.Identifier;
import org.mtr.data.IGui;

public final class BetterTexturedButtonWidget extends TexturedButtonWidget {

	private final boolean playSound;

	public BetterTexturedButtonWidget(Identifier normalTexture, Identifier highlightedTexture, PressAction onPress, boolean playSound) {
		super(0, 0, 0, IGui.SQUARE_SIZE, new ButtonTextures(normalTexture, highlightedTexture), onPress);
		this.playSound = playSound;
	}

	public BetterTexturedButtonWidget(Identifier normalTexture, Identifier highlightedTexture, Identifier disabledTexture, PressAction onPress, boolean playSound) {
		super(0, 0, 0, IGui.SQUARE_SIZE, new ButtonTextures(normalTexture, disabledTexture, highlightedTexture), onPress);
		this.playSound = playSound;
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
		if (playSound) {
			super.playDownSound(soundManager);
		}
	}
}
