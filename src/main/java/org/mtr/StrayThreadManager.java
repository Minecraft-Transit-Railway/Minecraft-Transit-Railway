package org.mtr;

import net.minecraft.server.MinecraftServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Fixes hanging on Minecraft server shutdown.
 * Source code adapted from <a href="https://github.com/navneetset/pebbles-stray-thread-killer">here</a>.
 */
public final class StrayThreadManager {

	private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
	private static final int SLEEP_TIME = 5000;

	public static void register(MinecraftServer minecraftServer) {
		EXECUTOR_SERVICE.execute(() -> {
			try {
				while (true) {
					//noinspection BusyWait
					Thread.sleep(SLEEP_TIME);
					if (!minecraftServer.isRunning()) {
						MTR.LOGGER.info("Server is no longer running, stopping thread monitoring.");
						//noinspection BusyWait
						Thread.sleep(SLEEP_TIME);
						forceShutdownStrayThreads();
						break;
					}
				}
			} catch (InterruptedException e) {
				MTR.LOGGER.warn("Watch thread interrupted", e);
			} finally {
				MTR.LOGGER.info("Shutting down watch thread executor.");
				EXECUTOR_SERVICE.shutdown();
			}
		});
	}

	private static void forceShutdownStrayThreads() {
		MTR.LOGGER.info("Attempting to shutdown stray threads...");

		for (final Thread thread : Thread.getAllStackTraces().keySet()) {
			if (!thread.isDaemon()) {
				MTR.LOGGER.info("Interrupting thread [{}]", thread.getName());
				thread.interrupt();
			}
		}

		MTR.LOGGER.info("All non-daemon threads have been interrupted. Forcing server shutdown...");
		Runtime.getRuntime().halt(0);
	}
}
