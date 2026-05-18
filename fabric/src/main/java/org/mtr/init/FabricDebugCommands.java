package org.mtr.init;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import org.mtr.mod.Init;
import org.mtr.mod.render.GpuObjDebugStats;

public final class FabricDebugCommands {

	private FabricDebugCommands() {
	}

	public static void init() {
		ClientCommandRegistrationCallback.EVENT.register(FabricDebugCommands::register);
	}

	private static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, net.minecraft.command.CommandRegistryAccess registryAccess) {
		dispatcher.register(
				ClientCommandManager.literal("mtrdebug")
						.then(ClientCommandManager.literal("instancing")
								.then(ClientCommandManager.literal("stats").executes(context -> {
									GpuObjDebugStats.emitReport("command stats");
									sendStatus(context.getSource(), "Wrote GPU instancing report to the backend log.");
									return 1;
								}))
								.then(ClientCommandManager.literal("watch").executes(context -> {
									GpuObjDebugStats.startWatch();
									sendStatus(context.getSource(), "Started GPU instancing watch (1s interval).");
									return 1;
								})
										.then(ClientCommandManager.literal("start").executes(context -> {
											GpuObjDebugStats.startWatch();
											sendStatus(context.getSource(), "Started GPU instancing watch (1s interval).");
											return 1;
										}))
										.then(ClientCommandManager.literal("stop").executes(context -> {
											if (GpuObjDebugStats.isWatchActive()) {
												GpuObjDebugStats.emitReport("watch stop");
												GpuObjDebugStats.stopWatch();
												sendStatus(context.getSource(), "Stopped GPU instancing watch.");
											} else {
												sendStatus(context.getSource(), "GPU instancing watch is not active.");
											}
											return 1;
										}))))
		);
	}

	private static void sendStatus(FabricClientCommandSource source, String message) {
		Init.LOGGER.info("[MTR Debug] {}", message);
		source.sendFeedback(Text.literal("[MTR Debug] " + message));
	}
}
