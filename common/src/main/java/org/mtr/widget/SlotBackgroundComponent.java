package org.mtr.widget;

import org.mtr.tool.ReleasedDynamicTextureManager;

public class SlotBackgroundComponent extends StitchedImageComponent {

	private static final int TEXTURE_SIZE = 18;

	public SlotBackgroundComponent() {
		super(TEXTURE_SIZE, TEXTURE_SIZE, 1, 0, ReleasedDynamicTextureManager.SLOT_TEXTURE.get());
	}
}
