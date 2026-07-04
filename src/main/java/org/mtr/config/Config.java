package org.mtr.config;

import org.apache.commons.io.FileUtils;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.config.ConfigSchema;
import org.mtr.libraries.com.google.gson.JsonElement;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class Config extends ConfigSchema {

	@Nullable
	private static Config instance;
	@Nullable
	private static Path basePath;

	public static void init(Path baseFolder) {
		if (instance == null || basePath == null) {
			basePath = baseFolder;
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
			MTR.LOGGER.error("", e);
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
			if (instance != null) {
				FileUtils.write(getConfigFilePath().toFile(), Utilities.prettyPrint(Utilities.getJsonObjectFromData(instance)), StandardCharsets.UTF_8);
			}
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	private static Path getConfigFilePath() {
		return basePath == null ? Paths.get("") : basePath.resolve("config/mtr.json");
	}
}
