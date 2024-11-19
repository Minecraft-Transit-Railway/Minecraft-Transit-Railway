package org.mtr.init;

import net.fabricmc.api.ClientModInitializer;
import org.mtr.mod.InitClient;

public final class MTRClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		InitClient.init();
	}
}
