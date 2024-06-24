package org.mtr.mod.screen;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.SoundManager;
import org.mtr.mapping.mapper.TexturedButtonWidgetExtension;

public class WidgetSilentImageButton extends TexturedButtonWidgetExtension {

	private final boolean playSound;

	public WidgetSilentImageButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, org.mtr.mapping.holder.PressAction onPress, boolean playSound) {
		super(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, onPress);
		this.playSound = playSound;
	}

	@Override
	public void playDownSound2(SoundManager soundManager) {
		if (playSound) {
			super.playDownSound2(soundManager);
		}
	}
}
