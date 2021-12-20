package mtr;

import @import@.architectury.platform.forge.EventBuses;
		import net.minecraftforge.eventbus.api.IEventBus;
		import net.minecraftforge.eventbus.api.SubscribeEvent;
		import net.minecraftforge.fml.common.Mod;
		import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
		import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MTR.MOD_ID)
public class MTRForge {

	public MTRForge() {
		final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		EventBuses.registerModEventBus(MTR.MOD_ID, eventBus);
		eventBus.register(MTRForgeRegistry.class);
		MTR.init();
	}

	private static class MTRForgeRegistry {

		@SubscribeEvent
		public static void onClientSetupEvent(FMLClientSetupEvent event) {
			MTRClient.init();
		}
	}
}
