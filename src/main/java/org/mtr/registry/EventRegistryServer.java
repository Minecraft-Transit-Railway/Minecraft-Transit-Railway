package org.mtr.registry;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

//? if fabric {
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
//? }

//? if neoforge {
/*import org.mtr.neoforge.MainEventBus;
import org.mtr.neoforge.ModEventBus;
*///? }

public final class EventRegistryServer {

	public static void registerServerStarting(Consumer<MinecraftServer> consumer) {
//? if fabric {
		ServerLifecycleEvents.SERVER_STARTING.register(consumer::accept);
//? }

//? if neoforge {
		/*MainEventBus.serverStartingConsumer = consumer;
//
*///? }
	}

	public static void registerServerStarted(Consumer<MinecraftServer> consumer) {
//? if fabric {
		ServerLifecycleEvents.SERVER_STARTED.register(consumer::accept);
//? }

//? if neoforge {
		/*MainEventBus.serverStartedConsumer = consumer;
//
*///? }
	}

	public static void registerServerStopping(Consumer<MinecraftServer> consumer) {
//? if fabric {
		ServerLifecycleEvents.SERVER_STOPPING.register(consumer::accept);
//? }

//? if neoforge {
		/*MainEventBus.serverStoppingConsumer = consumer;
//
*///? }
	}

	public static void registerServerStopped(Consumer<MinecraftServer> consumer) {
//? if fabric {
		ServerLifecycleEvents.SERVER_STOPPED.register(consumer::accept);
//? }

//? if neoforge {
		/*MainEventBus.serverStoppedConsumer = consumer;
//
*///? }
	}

	public static void registerStartServerTick(Runnable runnable) {
//? if fabric {
		ServerTickEvents.START_SERVER_TICK.register(minecraftServer -> runnable.run());
//? }

//? if neoforge {
		/*MainEventBus.startServerTickRunnable = runnable;
//
*///? }
	}

	public static void registerEndServerTick(Runnable runnable) {
//? if fabric {
		ServerTickEvents.END_SERVER_TICK.register(minecraftServer -> runnable.run());
//? }

//? if neoforge {
		/*MainEventBus.endServerTickRunnable = runnable;
//
*///? }
	}

	public static void registerStartWorldTick(Consumer<ServerLevel> consumer) {
//? if fabric {
		ServerTickEvents.START_WORLD_TICK.register(consumer::accept);
//? }

//? if neoforge {
		/*MainEventBus.startWorldTickRunnable = consumer;
//
*///? }
	}

	public static void registerEndWorldTick(Consumer<ServerLevel> consumer) {
//? if fabric {
		ServerTickEvents.END_WORLD_TICK.register(consumer::accept);
//? }

//? if neoforge {
		/*MainEventBus.endWorldTickRunnable = consumer;
//
*///? }
	}

	public static void registerPlayerJoin(BiConsumer<MinecraftServer, ServerPlayer> consumer) {
//? if fabric {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> consumer.accept(server, handler.player));
//? }

//? if neoforge {
		/*MainEventBus.playerJoinRunnable = consumer;
//
*///? }
	}

	public static void registerPlayerDisconnect(BiConsumer<MinecraftServer, ServerPlayer> consumer) {
//? if fabric {
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> consumer.accept(server, handler.player));
//? }

//? if neoforge {
		/*MainEventBus.playerDisconnectRunnable = consumer;
//
*///? }
	}

	public static void registerChunkLoad(BiConsumer<ServerLevel, ChunkAccess> consumer) {
//? if fabric {
		ServerChunkEvents.CHUNK_LOAD.register(consumer::accept);
//? }

//? if neoforge {
		/*ModEventBus.chunkLoadConsumer = consumer;
//
*///? }
	}

	public static void registerChunkUnload(BiConsumer<ServerLevel, ChunkAccess> consumer) {
//? if fabric {
		ServerChunkEvents.CHUNK_UNLOAD.register(consumer::accept);
//? }

//? if neoforge {
		/*ModEventBus.chunkUnloadConsumer = consumer;
//
*///? }
	}
}
