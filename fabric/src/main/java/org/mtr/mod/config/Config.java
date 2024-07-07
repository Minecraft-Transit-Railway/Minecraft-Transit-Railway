package org.mtr.mod.config;

import org.apache.commons.io.FileUtils;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonElement;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.mod.Init;
import org.mtr.mod.generated.config.ConfigSchema;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class Config extends ConfigSchema {

	private static Config instance;
	private static Path basePath;

	public static void init(File baseFolder) {
		if (instance == null || basePath == null) {
			basePath = baseFolder.toPath();
			try (final InputStream inputStream = Files.newInputStream(getConfigFilePath(), StandardOpenOption.READ)) {
				instance = new Config(new JsonReader(readResource(inputStream)));
			} catch (Exception ignored) {
				instance = new Config(new JsonReader(new JsonObject()));
			}
		}
		save();
	}

	private Config(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public static JsonElement readResource(InputStream inputStream) {
		try (final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(inputStreamReader);
		} catch (Exception e) {
			Init.LOGGER.error("", e);
			return new JsonObject();
		}
	}

	public static Client getClient() {
		return instance == null ? new Client(new JsonReader(new JsonObject())) : instance.client;
	}

	public static Server getServer() {
		return instance == null ? new Server(new JsonReader(new JsonObject())) : instance.server;
	}

	public static void save() {
		try {
			FileUtils.write(getConfigFilePath().toFile(), Utilities.prettyPrint(Utilities.getJsonObjectFromData(instance)), StandardCharsets.UTF_8);
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}
	}

	private static Path getConfigFilePath() {
		return basePath == null ? Paths.get("") : basePath.resolve("config/mtr.json");
	}
}
