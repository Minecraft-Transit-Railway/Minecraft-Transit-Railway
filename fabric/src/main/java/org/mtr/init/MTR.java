package org.mtr.init;

import net.fabricmc.api.ModInitializer;
import org.mtr.mod.Init;

public class MTR implements ModInitializer {

	@Override
	public void onInitialize() {
		Init.init();
	}
}
