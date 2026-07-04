package org.mtr.neoforge;

//? if neoforge {

/*import net.minecraft.client.KeyMapping;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import org.mtr.MTR;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@EventBusSubscriber(modid = MTR.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModEventBusClient {

	public static BiConsumer<ClientLevel, ChunkAccess> chunkLoadConsumer = null;
	public static BiConsumer<ClientLevel, ChunkAccess> chunkUnloadConsumer = null;
	public static Runnable resourceReloadRunnable = null;
	public static final ObjectArrayList<Runnable> CLIENT_OBJECTS_TO_REGISTER = new ObjectArrayList<>();
	public static final ObjectArrayList<Consumer<EntityRenderersEvent.RegisterRenderers>> BLOCK_ENTITY_RENDERERS = new ObjectArrayList<>();
	public static final ObjectArrayList<KeyMapping> KEY_BINDINGS = new ObjectArrayList<>();
	public static final ObjectArrayList<Consumer<RegisterColorHandlersEvent.Block>> BLOCK_COLORS = new ObjectArrayList<>();

	@SubscribeEvent
	public static void registerClient(FMLClientSetupEvent event) {
		CLIENT_OBJECTS_TO_REGISTER.forEach(Runnable::run);
	}

	@SubscribeEvent
	public static void chunkLoad(ChunkEvent.Load event) {
		if (chunkLoadConsumer != null && event.getLevel() instanceof ClientLevel clientWorld) {
			chunkLoadConsumer.accept(clientWorld, event.getChunk());
		}
	}

	@SubscribeEvent
	public static void chunkUnload(ChunkEvent.Unload event) {
		if (chunkUnloadConsumer != null && event.getLevel() instanceof ClientLevel clientWorld) {
			chunkUnloadConsumer.accept(clientWorld, event.getChunk());
		}
	}

	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		BLOCK_ENTITY_RENDERERS.forEach(consumer -> consumer.accept(event));
	}

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		KEY_BINDINGS.forEach(event::register);
	}

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		BLOCK_COLORS.forEach(consumer -> consumer.accept(event));
	}

	@SubscribeEvent
	public static void resourceReload(TextureAtlasStitchedEvent event) {
		if (resourceReloadRunnable != null && event.getAtlas().location().getPath().endsWith("blocks.png")) {
			resourceReloadRunnable.run();
		}
	}
}

*///? }
