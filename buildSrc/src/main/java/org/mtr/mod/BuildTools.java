package org.mtr.mod;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jonafanho.apitools.ModId;
import com.jonafanho.apitools.ModLoader;
import com.jonafanho.apitools.ModProvider;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Project;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildTools {

	public final String minecraftVersion;
	public final String loader;
	public final int javaLanguageVersion;

	private final Path path;
	private final String version;

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public BuildTools(String minecraftVersion, String loader, Project project) {
		this.minecraftVersion = minecraftVersion;
		this.loader = loader;
		path = project.getProjectDir().toPath();
		version = project.getVersion().toString();
		final int majorVersion = Integer.parseInt(minecraftVersion.split("\\.")[1]);
		javaLanguageVersion = majorVersion <= 16 ? 8 : majorVersion == 17 ? 16 : 17;
	}

	public String getFabricVersion() {
		return getJson("https://meta.fabricmc.net/v2/versions/loader/" + minecraftVersion).getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject("loader").get("version").getAsString();
	}

	public String getYarnVersion() {
		return getJson("https://meta.fabricmc.net/v2/versions/yarn/" + minecraftVersion).getAsJsonArray().get(0).getAsJsonObject().get("version").getAsString();
	}

	public String getFabricApiVersion() {
		final String modIdString = "fabric-api";
		return new ModId(modIdString, ModProvider.MODRINTH).getModFiles(minecraftVersion, ModLoader.FABRIC, "").get(0).fileName.split(".jar")[0].replace(modIdString + "-", "");
	}

	public String getModMenuVersion() {
		final String modIdString = "modmenu";
		return new ModId(modIdString, ModProvider.MODRINTH).getModFiles(minecraftVersion, ModLoader.FABRIC, "").get(0).fileName.split(".jar")[0].replace(modIdString + "-", "");
	}

	public String getForgeVersion() {
		return getJson("https://files.minecraftforge.net/net/minecraftforge/forge/promotions_slim.json").getAsJsonObject().getAsJsonObject("promos").get(minecraftVersion + "-latest").getAsString();
	}

	public void copyBuildFile() throws IOException {
		final Path directory = path.getParent().resolve("build/release");
		Files.createDirectories(directory);
		Files.copy(path.resolve(String.format("build/libs/%s-%s%s.jar", loader, version, loader.equals("fabric") ? "" : "-all")), directory.resolve(String.format("MTR-%s-%s-%s.jar", loader, minecraftVersion, version)), StandardCopyOption.REPLACE_EXISTING);
	}

	private static JsonElement getJson(String url) {
		for (int i = 0; i < 5; i++) {
			try {
				return JsonParser.parseString(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
			} catch (Exception e) {
				logException(e);
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				logException(e);
			}
		}

		return new JsonObject();
	}

	private static void logException(Exception e) {
		LOGGER.log(Level.INFO, e.getMessage(), e);
	}
}
