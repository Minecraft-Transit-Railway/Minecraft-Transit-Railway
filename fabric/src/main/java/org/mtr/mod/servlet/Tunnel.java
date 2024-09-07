package org.mtr.mod.servlet;

import org.mtr.mod.Init;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public final class Tunnel {

	private String tunnelUrl = "";
	private Runnable stopTunnel = null;

	public Tunnel(File rootDirectory, int port, Runnable setUrlCallback) {
		// TODO
	}

	public void stop() {
		if (stopTunnel != null) {
			stopTunnel.run();
		}
	}

	public String getTunnelUrl() {
		return tunnelUrl;
	}

	private static void executeCommand(@Nullable Consumer<Process> processConsumer, Consumer<String> consoleConsumer, String... arguments) {
		if (arguments.length > 0) {
			try {
				final Process process = new ProcessBuilder(arguments).redirectErrorStream(true).start();
				if (processConsumer != null) {
					processConsumer.accept(process);
				}

				// Process the input stream
				final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					consoleConsumer.accept(line);
				}
			} catch (Exception e) {
				Init.LOGGER.error("", e);
			}
		}
	}
}
