package org.mtr.fabric;

import net.fabricmc.api.ModInitializer;
import org.mtr.MTR;

public final class MTRFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		MTR.init();
	}
}
