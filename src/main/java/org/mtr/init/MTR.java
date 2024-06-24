package org.mtr.init;

import net.fabricmc.api.ModInitializer;
import org.mtr.mod.Init;

public class MTR implements ModInitializer {

	public static final String MOD_ID = "mtr";

	@Override
	public void onInitialize() {
		Init.init();
	}
}
