package mtr;

import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MTR.MOD_ID)
public class MTRForge {

	public MTRForge() {
		EventBuses.registerModEventBus(MTR.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
		MTR.init();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> MTRClient::init);
	}
}