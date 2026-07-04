package org.mtr.neoforge;

//? if neoforge {

/*import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.mtr.MTR;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.packet.PacketBufferReceiver;
import org.mtr.packet.PacketHandler;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@EventBusSubscriber(modid = MTR.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEventBus {

	public static BiConsumer<ServerLevel, ChunkAccess> chunkLoadConsumer = null;
	public static BiConsumer<ServerLevel, ChunkAccess> chunkUnloadConsumer = null;
	public static final Object2ObjectOpenHashMap<String, Function<PacketBufferReceiver, ? extends PacketHandler>> PACKETS = new Object2ObjectOpenHashMap<>();
	public static final ObjectArrayList<Consumer<PayloadRegistrar>> PAYLOAD_HANDLERS = new ObjectArrayList<>();
	private static final String PROTOCOL_VERSION = "1";

	@SubscribeEvent
	public static void chunkLoad(ChunkEvent.Load event) {
		if (chunkLoadConsumer != null && event.getLevel() instanceof ServerLevel serverWorld) {
			chunkLoadConsumer.accept(serverWorld, event.getChunk());
		}
	}

	@SubscribeEvent
	public static void chunkUnload(ChunkEvent.Unload event) {
		if (chunkUnloadConsumer != null && event.getLevel() instanceof ServerLevel serverWorld) {
			chunkUnloadConsumer.accept(serverWorld, event.getChunk());
		}
	}

	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar payloadRegistrar = event.registrar(PROTOCOL_VERSION);
		PAYLOAD_HANDLERS.forEach(payloadHandler -> payloadHandler.accept(payloadRegistrar));
	}
}

*///? }
