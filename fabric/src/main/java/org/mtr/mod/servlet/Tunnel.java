package org.mtr.mod.servlet;

import org.apache.commons.io.FileUtils;
import org.mtr.mapping.holder.OperatingSystem;
import org.mtr.mapping.holder.Util;
import org.mtr.mod.Init;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public final class Tunnel {

	private String tunnelUrl = "";
	private Runnable stopTunnel = null;

	public Tunnel(File rootDirectory, int port, Runnable setUrlCallback) {
		final Path tunnelDirectory = rootDirectory.toPath().resolve("tunnelmole");
		final Path tunnelPath = tunnelDirectory.resolve("tmole");
		final OperatingSystem operatingSystem = Util.getOperatingSystem();

		// Download and setup Tunnelmole
		if (!Files.exists(tunnelPath)) {
			final String url;
			final String downloadFileName;
			final boolean needsUnzip;
			switch (operatingSystem) {
				case WINDOWS:
					url = "https://tunnelmole.com/downloads/tmole.exe";
					downloadFileName = "tmole";
					needsUnzip = false;
					break;
				case OSX:
					url = "https://tunnelmole.com/downloads/tmole-mac.gz";
					downloadFileName = "tmole.gz";
					needsUnzip = true;
					break;
				default:
					url = "https://tunnelmole.com/downloads/tmole-linux.gz";
					downloadFileName = "tmole.gz";
					needsUnzip = true;
					break;
			}

			try {
				FileUtils.copyURLToFile(new URL(url), tunnelDirectory.resolve(downloadFileName).toFile());
			} catch (Exception e) {
				Init.LOGGER.error("", e);
			}

			if (needsUnzip) {
				executeCommand(null, Init.LOGGER::info, "gunzip", tunnelDirectory.resolve(downloadFileName).toString());
				executeCommand(null, Init.LOGGER::info, "chmod", "+x", tunnelPath.toString());
			}

			Init.LOGGER.info("Successfully downloaded Tunnelmole");
		}

		// Test version
		executeCommand(null, line -> Init.LOGGER.info("Tunnelmole version: {}", line), tunnelPath.toString(), "--version");

		// Run Tunnelmole on a separate thread
		Executors.newSingleThreadExecutor().execute(() -> executeCommand(process -> stopTunnel = () -> {
			if (process.isAlive()) {
				try {
					process.destroyForcibly().waitFor();
					Init.LOGGER.info("Successfully stopped Tunnelmole");
				} catch (Exception e) {
					Init.LOGGER.error("", e);
				}
			}
		}, line -> {
			final String linePart = line.split(" ")[0];
			if (linePart.startsWith("https://")) {
				tunnelUrl = linePart;
				setUrlCallback.run();
			} else if (!linePart.startsWith("http")) {
				Init.LOGGER.info(line);
			}
		}, tunnelPath.toString(), String.valueOf(port)));
		Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
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
