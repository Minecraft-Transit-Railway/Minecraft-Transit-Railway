package org.mtr.registry.fabric;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.Chunk;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class EventRegistryImpl {

	public static void registerServerStarting(Consumer<MinecraftServer> consumer) {
		ServerLifecycleEvents.SERVER_STARTING.register(consumer::accept);
	}

	public static void registerServerStarted(Consumer<MinecraftServer> consumer) {
		ServerLifecycleEvents.SERVER_STARTED.register(consumer::accept);
	}

	public static void registerServerStopping(Consumer<MinecraftServer> consumer) {
		ServerLifecycleEvents.SERVER_STOPPING.register(consumer::accept);
	}

	public static void registerServerStopped(Consumer<MinecraftServer> consumer) {
		ServerLifecycleEvents.SERVER_STOPPED.register(consumer::accept);
	}

	public static void registerStartServerTick(Runnable runnable) {
		ServerTickEvents.START_SERVER_TICK.register(minecraftServer -> runnable.run());
	}

	public static void registerEndServerTick(Runnable runnable) {
		ServerTickEvents.END_SERVER_TICK.register(minecraftServer -> runnable.run());
	}

	public static void registerStartWorldTick(Consumer<ServerWorld> consumer) {
		ServerTickEvents.START_WORLD_TICK.register(consumer::accept);
	}

	public static void registerEndWorldTick(Consumer<ServerWorld> consumer) {
		ServerTickEvents.END_WORLD_TICK.register(consumer::accept);
	}

	public static void registerPlayerJoin(BiConsumer<MinecraftServer, ServerPlayerEntity> consumer) {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> consumer.accept(server, handler.player));
	}

	public static void registerPlayerDisconnect(BiConsumer<MinecraftServer, ServerPlayerEntity> consumer) {
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> consumer.accept(server, handler.player));
	}

	public static void registerChunkLoad(BiConsumer<ServerWorld, Chunk> consumer) {
		ServerChunkEvents.CHUNK_LOAD.register(consumer::accept);
	}

	public static void registerChunkUnload(BiConsumer<ServerWorld, Chunk> consumer) {
		ServerChunkEvents.CHUNK_UNLOAD.register(consumer::accept);
	}
}
