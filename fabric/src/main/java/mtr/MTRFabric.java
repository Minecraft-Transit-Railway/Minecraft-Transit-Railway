package mtr;

import net.fabricmc.api.ModInitializer;

public class MTRFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		MTR.init();
	}
}
