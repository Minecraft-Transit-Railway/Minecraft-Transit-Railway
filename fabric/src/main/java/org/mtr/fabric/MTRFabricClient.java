package org.mtr.fabric;

import net.fabricmc.api.ClientModInitializer;
import org.mtr.MTRClient;

public final class MTRFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		MTRClient.init();
	}
}
