package org.mtr.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.Chunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.mtr.MTR;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@EventBusSubscriber(modid = MTR.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class MainEventBus {

	public static Consumer<MinecraftServer> serverStartingConsumer = null;
	public static Consumer<MinecraftServer> serverStartedConsumer = null;
	public static Consumer<MinecraftServer> serverStoppingConsumer = null;
	public static Consumer<MinecraftServer> serverStoppedConsumer = null;
	public static Runnable startServerTickRunnable = null;
	public static Runnable endServerTickRunnable = null;
	public static Consumer<ServerWorld> startWorldTickRunnable = null;
	public static Consumer<ServerWorld> endWorldTickRunnable = null;
	public static BiConsumer<MinecraftServer, ServerPlayerEntity> playerJoinRunnable = null;
	public static BiConsumer<MinecraftServer, ServerPlayerEntity> playerDisconnectRunnable = null;
	public static BiConsumer<ServerWorld, Chunk> chunkLoadConsumer = null;
	public static BiConsumer<ServerWorld, Chunk> chunkUnloadConsumer = null;
	public static Consumer<CommandDispatcher<ServerCommandSource>> commandConsumer = null;

	@SubscribeEvent
	public static void serverStarting(ServerStartingEvent event) {
		if (serverStartingConsumer != null) {
			serverStartingConsumer.accept(event.getServer());
		}
	}

	@SubscribeEvent
	public static void serverStarted(ServerStartedEvent event) {
		if (serverStartedConsumer != null) {
			serverStartedConsumer.accept(event.getServer());
		}
	}

	@SubscribeEvent
	public static void serverStopping(ServerStoppingEvent event) {
		if (serverStoppingConsumer != null) {
			serverStoppingConsumer.accept(event.getServer());
		}
	}

	@SubscribeEvent
	public static void serverStopped(ServerStoppedEvent event) {
		if (serverStoppedConsumer != null) {
			serverStoppedConsumer.accept(event.getServer());
		}
	}

	@SubscribeEvent
	public static void serverTickStart(ServerTickEvent.Pre event) {
		if (startServerTickRunnable != null) {
			startServerTickRunnable.run();
		}
	}

	@SubscribeEvent
	public static void serverTickEnd(ServerTickEvent.Post event) {
		if (endServerTickRunnable != null) {
			endServerTickRunnable.run();
		}
	}

	@SubscribeEvent
	public static void worldTickStart(LevelTickEvent.Pre event) {
		if (startWorldTickRunnable != null && event.getLevel() instanceof ServerWorld serverWorld) {
			startWorldTickRunnable.accept(serverWorld);
		}
	}

	@SubscribeEvent
	public static void worldTickEnd(LevelTickEvent.Post event) {
		if (endWorldTickRunnable != null && event.getLevel() instanceof ServerWorld serverWorld) {
			endWorldTickRunnable.accept(serverWorld);
		}
	}

	@SubscribeEvent
	public static void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		final PlayerEntity playerEntity = event.getEntity();
		if (playerJoinRunnable != null && playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
			playerJoinRunnable.accept(serverPlayerEntity.server, serverPlayerEntity);
		}
	}

	@SubscribeEvent
	public static void playerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
		if (playerDisconnectRunnable != null && event.getEntity() instanceof ServerPlayerEntity serverPlayerEntity) {
			playerDisconnectRunnable.accept(serverPlayerEntity.server, serverPlayerEntity);
		}
	}

	@SubscribeEvent
	public static void chunkLoad(ChunkEvent.Load event) {
		if (chunkLoadConsumer != null && event.getLevel() instanceof ServerWorld serverWorld) {
			chunkLoadConsumer.accept(serverWorld, event.getChunk());
		}
	}

	@SubscribeEvent
	public static void chunkUnload(ChunkEvent.Unload event) {
		if (chunkUnloadConsumer != null && event.getLevel() instanceof ServerWorld serverWorld) {
			chunkUnloadConsumer.accept(serverWorld, event.getChunk());
		}
	}

	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		if (commandConsumer != null) {
			commandConsumer.accept(event.getDispatcher());
		}
	}
}
