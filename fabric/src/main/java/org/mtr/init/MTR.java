package org.mtr.init;

import net.fabricmc.api.ModInitializer;
import org.mtr.mod.Init;

public final class MTR implements ModInitializer {

	@Override
	public void onInitialize() {
		Init.init();
	}
}
