package org.mtr.registry.neoforge;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.chunk.Chunk;
import org.mtr.MTRClient;
import org.mtr.neoforge.MainEventBusClient;
import org.mtr.neoforge.ModEventBusClient;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class EventRegistryClientImpl {

	public static void registerStartClientTick(Runnable runnable) {
		MainEventBusClient.startClientTickRunnable = runnable;
	}

	public static void registerEndClientTick(Runnable runnable) {
		MainEventBusClient.endClientTickRunnable = runnable;
	}

	public static void registerStartWorldTick(Consumer<ClientWorld> consumer) {
		MainEventBusClient.startWorldTickRunnable = consumer;
	}

	public static void registerEndWorldTick(Consumer<ClientWorld> consumer) {
		MainEventBusClient.endWorldTickRunnable = consumer;
	}

	public static void registerClientJoin(Runnable runnable) {
		MainEventBusClient.clientJoinRunnable = runnable;
	}

	public static void registerClientDisconnect(Runnable runnable) {
		MainEventBusClient.clientDisconnectRunnable = runnable;
	}

	public static void registerChunkLoad(BiConsumer<ClientWorld, Chunk> consumer) {
		MainEventBusClient.chunkLoadConsumer = consumer;
	}

	public static void registerChunkUnload(BiConsumer<ClientWorld, Chunk> consumer) {
		MainEventBusClient.chunkUnloadConsumer = consumer;
	}

	public static void registerResourceReloadEvent(Runnable runnable) {
		ModEventBusClient.resourceReloadRunnable = runnable;
	}

	public static void registerWorldRenderEvent(MTRClient.WorldRenderCallback worldRenderCallback) {
		MainEventBusClient.worldRenderCallback = worldRenderCallback;
	}

	public static void registerHudLayerRenderEvent(BiConsumer<DrawContext, RenderTickCounter> hudLayerRenderCallback) {
		MainEventBusClient.hudLayerRenderCallback = hudLayerRenderCallback;
	}
}
