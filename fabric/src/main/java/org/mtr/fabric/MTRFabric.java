package org.mtr.fabric;

import net.fabricmc.api.ModInitializer;
import org.mtr.MTR;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.packet.PacketBufferReceiver;
import org.mtr.packet.PacketHandler;

import java.util.function.Function;

public final class MTRFabric implements ModInitializer {

	public static final Object2ObjectOpenHashMap<String, Function<PacketBufferReceiver, ? extends PacketHandler>> PACKETS = new Object2ObjectOpenHashMap<>();

	@Override
	public void onInitialize() {
		MTR.init();
	}
}
