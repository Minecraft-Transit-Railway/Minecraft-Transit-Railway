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
								.then(ClientCommandManager.literal("stats").executes(context -> writeReport(context.getSource(), "command stats")))
								.then(ClientCommandManager.literal("report").executes(context -> writeReport(context.getSource(), "command report")))
								.then(ClientCommandManager.literal("status").executes(context -> writeStatus(context.getSource(), "command status")))
								.then(ClientCommandManager.literal("watch").executes(context -> startWatch(context.getSource()))
										.then(ClientCommandManager.literal("start").executes(context -> {
											return startWatch(context.getSource());
										}))
										.then(ClientCommandManager.literal("stop").executes(context -> {
											if (GpuObjDebugStats.isWatchActive()) {
												GpuObjDebugStats.emitReport("watch stop");
												GpuObjDebugStats.stopWatch();
												sendStatus(context.getSource(), "Stopped GPU instancing watch. " + GpuObjDebugStats.getStatusSummary());
											} else {
												sendStatus(context.getSource(), "GPU instancing watch is not active.");
											}
											return 1;
										})))
								.then(ClientCommandManager.literal("diagnose").executes(context -> writeStatus(context.getSource(), "command diagnose")))
										.then(ClientCommandManager.literal("start").executes(context -> {
											GpuObjDebugStats.enableDiagnostics();
											sendStatus(context.getSource(), "Enabled GPU instancing diagnostics. " + GpuObjDebugStats.getStatusSummary());
											return 1;
										}))
										.then(ClientCommandManager.literal("stop").executes(context -> {
											GpuObjDebugStats.disableDiagnostics();
											sendStatus(context.getSource(), "Disabled GPU instancing diagnostics and reset diagnostic toggles. " + GpuObjDebugStats.getStatusSummary());
											return 1;
										}))
										.then(ClientCommandManager.literal("status").executes(context -> writeStatus(context.getSource(), "command diagnose status")))
										.then(ClientCommandManager.literal("skipOffset")
												.then(ClientCommandManager.literal("on").executes(context -> setSkipOffset(context.getSource(), true)))
												.then(ClientCommandManager.literal("off").executes(context -> setSkipOffset(context.getSource(), false))))
										.then(ClientCommandManager.literal("cameraOffset")
												.then(ClientCommandManager.literal("on").executes(context -> setSkipOffset(context.getSource(), true)))
												.then(ClientCommandManager.literal("off").executes(context -> setSkipOffset(context.getSource(), false))))
										.then(ClientCommandManager.literal("forceNoCull")
												.then(ClientCommandManager.literal("on").executes(context -> setForceNoCull(context.getSource(), true)))
												.then(ClientCommandManager.literal("off").executes(context -> setForceNoCull(context.getSource(), false))))
										.then(ClientCommandManager.literal("noCull")
												.then(ClientCommandManager.literal("on").executes(context -> setForceNoCull(context.getSource(), true)))
												.then(ClientCommandManager.literal("off").executes(context -> setForceNoCull(context.getSource(), false))))
										.then(ClientCommandManager.literal("forceWhiteCutout")
												.then(ClientCommandManager.literal("on").executes(context -> setForceWhiteCutout(context.getSource(), true)))
												.then(ClientCommandManager.literal("off").executes(context -> setForceWhiteCutout(context.getSource(), false))))
										.then(ClientCommandManager.literal("whiteCutout")
												.then(ClientCommandManager.literal("on").executes(context -> setForceWhiteCutout(context.getSource(), true)))
												.then(ClientCommandManager.literal("off").executes(context -> setForceWhiteCutout(context.getSource(), false)))))))
		);
	}

	private static int writeReport(FabricClientCommandSource source, String reason) {
		GpuObjDebugStats.emitReport(reason);
		sendStatus(source, "Wrote GPU instancing report to the backend log. " + GpuObjDebugStats.getStatusSummary());
		return 1;
	}

	private static int writeStatus(FabricClientCommandSource source, String reason) {
		GpuObjDebugStats.emitReport(reason);
		sendStatus(source, "Wrote GPU instancing status report to the backend log. " + GpuObjDebugStats.getStatusSummary());
		return 1;
	}

	private static int startWatch(FabricClientCommandSource source) {
		GpuObjDebugStats.startWatch();
		sendStatus(source, "Started GPU instancing watch (1s interval). " + GpuObjDebugStats.getStatusSummary());
		return 1;
	}

	private static int setSkipOffset(FabricClientCommandSource source, boolean enabled) {
		GpuObjDebugStats.setDiagnosticSkipCameraOffset(enabled);
		sendStatus(source, (enabled ? "Enabled" : "Disabled") + " GPU instancing diagnostic toggle: skipCameraOffset. " + GpuObjDebugStats.getStatusSummary());
		return 1;
	}

	private static int setForceNoCull(FabricClientCommandSource source, boolean enabled) {
		GpuObjDebugStats.setDiagnosticForceNoCull(enabled);
		sendStatus(source, (enabled ? "Enabled" : "Disabled") + " GPU instancing diagnostic toggle: forceNoCull. " + GpuObjDebugStats.getStatusSummary());
		return 1;
	}

	private static int setForceWhiteCutout(FabricClientCommandSource source, boolean enabled) {
		GpuObjDebugStats.setDiagnosticForceWhiteCutout(enabled);
		sendStatus(source, (enabled ? "Enabled" : "Disabled") + " GPU instancing diagnostic toggle: forceWhiteCutout. " + GpuObjDebugStats.getStatusSummary());
		return 1;
	}

	private static void sendStatus(FabricClientCommandSource source, String message) {
		Init.LOGGER.info("[MTR Debug] {}", message);
		source.sendFeedback(Text.literal("[MTR Debug] " + message));
	}
}
