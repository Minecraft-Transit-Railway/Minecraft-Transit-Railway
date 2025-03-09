package org.mtr.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class EventRegistry {

	@ExpectPlatform
	public static void registerServerStarting(Consumer<MinecraftServer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerServerStarted(Consumer<MinecraftServer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerServerStopping(Consumer<MinecraftServer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerServerStopped(Consumer<MinecraftServer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerStartServerTick(Runnable runnable) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerEndServerTick(Runnable runnable) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerStartWorldTick(Consumer<ServerWorld> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerEndWorldTick(Consumer<ServerWorld> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerPlayerJoin(BiConsumer<MinecraftServer, ServerPlayerEntity> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerPlayerDisconnect(BiConsumer<MinecraftServer, ServerPlayerEntity> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerChunkLoad(BiConsumer<ServerWorld, WorldChunk> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerChunkUnload(BiConsumer<ServerWorld, WorldChunk> consumer) {
		throw new AssertionError();
	}
}
