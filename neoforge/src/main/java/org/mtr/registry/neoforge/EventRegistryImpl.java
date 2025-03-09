package org.mtr.registry.neoforge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import org.mtr.neoforge.MainEventBus;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class EventRegistryImpl {

	public static void registerServerStarting(Consumer<MinecraftServer> consumer) {
		MainEventBus.serverStartingConsumer = consumer;
	}

	public static void registerServerStarted(Consumer<MinecraftServer> consumer) {
		MainEventBus.serverStartedConsumer = consumer;
	}

	public static void registerServerStopping(Consumer<MinecraftServer> consumer) {
		MainEventBus.serverStoppingConsumer = consumer;
	}

	public static void registerServerStopped(Consumer<MinecraftServer> consumer) {
		MainEventBus.serverStoppedConsumer = consumer;
	}

	public static void registerStartServerTick(Runnable runnable) {
		MainEventBus.startServerTickRunnable = runnable;
	}

	public static void registerEndServerTick(Runnable runnable) {
		MainEventBus.endServerTickRunnable = runnable;
	}

	public static void registerStartWorldTick(Consumer<ServerWorld> consumer) {
		MainEventBus.startWorldTickRunnable = consumer;
	}

	public static void registerEndWorldTick(Consumer<ServerWorld> consumer) {
		MainEventBus.endWorldTickRunnable = consumer;
	}

	public static void registerPlayerJoin(BiConsumer<MinecraftServer, ServerPlayerEntity> consumer) {
		MainEventBus.playerJoinRunnable = consumer;
	}

	public static void registerPlayerDisconnect(BiConsumer<MinecraftServer, ServerPlayerEntity> consumer) {
		MainEventBus.playerDisconnectRunnable = consumer;
	}

	public static void registerChunkLoad(BiConsumer<ServerWorld, WorldChunk> consumer) {
		MainEventBus.chunkLoadConsumer = consumer;
	}

	public static void registerChunkUnload(BiConsumer<ServerWorld, WorldChunk> consumer) {
		MainEventBus.chunkUnloadConsumer = consumer;
	}
}
