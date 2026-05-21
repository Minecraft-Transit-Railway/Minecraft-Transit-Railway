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
								.then(ClientCommandManager.literal("status").executes(context -> requestStatus(context.getSource()))))
		);
	}

	private static int requestStatus(FabricClientCommandSource source) {
		GpuObjDebugStats.requestStatus();
		sendStatus(source, "Queued GPU instancing status for the next rendered frame. " + GpuObjDebugStats.getStatusSummary());
		return 1;
	}

	private static void sendStatus(FabricClientCommandSource source, String message) {
		Init.LOGGER.info("[MTR Debug] {}", message);
		source.sendFeedback(Text.literal("[MTR Debug] " + message));
	}
}
