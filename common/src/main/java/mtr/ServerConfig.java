package mtr;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerConfig {

	public static String dataDiffLoggerDiscordWebhookUrl = "";
	public static int webServerPort = 8888;

	public static void readFromFile(MinecraftServer server) {
		final Path configFilePath = server.getServerDirectory().toPath().resolve("config").resolve("mtr-server.json");
		final Path legacyPortFilePath = server.getServerDirectory().toPath().resolve("config").resolve("mtr_webserver_port.txt");

		if (Files.exists(configFilePath)) {
			try {
				final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(configFilePath))).getAsJsonObject();
				try {
					dataDiffLoggerDiscordWebhookUrl = jsonConfig.get("data-diff-logger").getAsJsonObject().get("discord-webhook-url").getAsString();
					dataDiffLoggerDiscordWebhookUrl = dataDiffLoggerDiscordWebhookUrl.trim();
					try {
						new URL(dataDiffLoggerDiscordWebhookUrl);
					} catch (Exception ignored) {
						dataDiffLoggerDiscordWebhookUrl = "";
					}
				} catch (Exception ignored) {
				}
				try {
					webServerPort = jsonConfig.get("web-server").getAsJsonObject().get("port").getAsInt();
					webServerPort = Mth.clamp(webServerPort, 1025, 65535);
				} catch (Exception ignored) {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Files.exists(legacyPortFilePath)) {
			try {
				if (!Files.exists(configFilePath)) {
					String fileContent = String.join("", Files.readAllLines(legacyPortFilePath));
					webServerPort = Integer.parseInt(fileContent.replaceAll("[^0-9]", ""));
					webServerPort = Mth.clamp(webServerPort, 1025, 65535);
				}
				writeToFile(server);
				Files.write(legacyPortFilePath, Collections.singleton("""
					OBSOLETE
					Server port configuration has moved to mtr-server.json. You can delete this file.
					伺服器網頁端口配置已移動至 mtr-server.json。您可移除本文檔。"""
				));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		writeToFile(server);
	}

	private static void writeToFile(MinecraftServer server) {
		final Path configFilePath = server.getServerDirectory().toPath().resolve("config").resolve("mtr-server.json");
		final JsonObject jsonConfig = new JsonObject();

		JsonObject webServer = new JsonObject();
		webServer.addProperty("port", webServerPort);
		jsonConfig.add("web-server", webServer);

		JsonObject dataDiffLogger = new JsonObject();
		dataDiffLogger.addProperty("discord-webhook-url", dataDiffLoggerDiscordWebhookUrl);
		jsonConfig.add("data-diff-logger", dataDiffLogger);

		try {
			Files.write(configFilePath, Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
