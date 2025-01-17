package org.mtr.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;

@Mod(Init.MOD_ID)
public final class MTR {

	public MTR() {
		Init.init();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> InitClient::init);
	}
}
