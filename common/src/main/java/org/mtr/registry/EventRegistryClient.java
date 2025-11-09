package org.mtr.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.chunk.Chunk;
import org.mtr.MTRClient;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class EventRegistryClient {

	@ExpectPlatform
	public static void registerStartClientTick(Runnable runnable) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerEndClientTick(Runnable runnable) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerStartWorldTick(Consumer<ClientWorld> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerEndWorldTick(Consumer<ClientWorld> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerClientJoin(Runnable runnable) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerClientDisconnect(Runnable runnable) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerChunkLoad(BiConsumer<ClientWorld, Chunk> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerChunkUnload(BiConsumer<ClientWorld, Chunk> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerResourceReloadEvent(Runnable runnable) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerWorldRenderEvent(MTRClient.WorldRenderCallback worldRenderCallback) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerHudLayerRenderEvent(BiConsumer<DrawContext, RenderTickCounter> hudLayerRenderCallback) {
		throw new AssertionError();
	}
}
