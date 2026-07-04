package org.mtr.neoforge;

//? if neoforge {

/*import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
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
	public static Consumer<ServerLevel> startWorldTickRunnable = null;
	public static Consumer<ServerLevel> endWorldTickRunnable = null;
	public static BiConsumer<MinecraftServer, ServerPlayer> playerJoinRunnable = null;
	public static BiConsumer<MinecraftServer, ServerPlayer> playerDisconnectRunnable = null;
	public static Consumer<CommandDispatcher<CommandSourceStack>> commandConsumer = null;

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
		if (startWorldTickRunnable != null && event.getLevel() instanceof ServerLevel serverWorld) {
			startWorldTickRunnable.accept(serverWorld);
		}
	}

	@SubscribeEvent
	public static void worldTickEnd(LevelTickEvent.Post event) {
		if (endWorldTickRunnable != null && event.getLevel() instanceof ServerLevel serverWorld) {
			endWorldTickRunnable.accept(serverWorld);
		}
	}

	@SubscribeEvent
	public static void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		final Player playerEntity = event.getEntity();
		if (playerJoinRunnable != null && playerEntity instanceof ServerPlayer serverPlayerEntity) {
			playerJoinRunnable.accept(serverPlayerEntity.server, serverPlayerEntity);
		}
	}

	@SubscribeEvent
	public static void playerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
		if (playerDisconnectRunnable != null && event.getEntity() instanceof ServerPlayer serverPlayerEntity) {
			playerDisconnectRunnable.accept(serverPlayerEntity.server, serverPlayerEntity);
		}
	}

	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		if (commandConsumer != null) {
			commandConsumer.accept(event.getDispatcher());
		}
	}
}

*///? }
