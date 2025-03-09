package org.mtr.neoforge;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.mtr.MTR;
import org.mtr.packet.PacketBufferReceiver;
import org.mtr.packet.PacketHandler;

import java.util.function.Consumer;
import java.util.function.Function;

@EventBusSubscriber(modid = MTR.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEventBus {

	public static final Object2ObjectOpenHashMap<String, Function<PacketBufferReceiver, ? extends PacketHandler>> PACKETS = new Object2ObjectOpenHashMap<>();
	public static final Identifier PACKETS_IDENTIFIER = Identifier.of(MTR.MOD_ID, "packet");
	public static final ObjectArrayList<Consumer<PayloadRegistrar>> PAYLOAD_HANDLERS = new ObjectArrayList<>();
	private static final String PROTOCOL_VERSION = "1";

	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar payloadRegistrar = event.registrar(PROTOCOL_VERSION);
		PAYLOAD_HANDLERS.forEach(payloadHandler -> payloadHandler.accept(payloadRegistrar));
	}
}
