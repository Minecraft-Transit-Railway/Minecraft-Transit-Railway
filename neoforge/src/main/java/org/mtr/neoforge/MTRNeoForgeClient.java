package org.mtr.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import org.mtr.MTR;
import org.mtr.MTRClient;

@Mod(value = MTR.MOD_ID, dist = Dist.CLIENT)
public final class MTRNeoForgeClient {

	public MTRNeoForgeClient() {
		MTRClient.init();
	}
}
