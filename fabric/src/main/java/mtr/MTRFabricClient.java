package mtr;

import net.fabricmc.api.ClientModInitializer;

public class MTRFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		MTRClient.init();
	}
}
