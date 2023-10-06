package org.mtr.mod;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jonafanho.apitools.ModId;
import com.jonafanho.apitools.ModLoader;
import com.jonafanho.apitools.ModProvider;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Project;
import org.mtr.mapping.tool.CreateClientWorldRenderingMixin;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class BuildTools {

	public final String minecraftVersion;
	public final String loader;
	public final int javaLanguageVersion;

	private final Path path;
	private final String version;
	private final int majorVersion;

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public BuildTools(String minecraftVersion, String loader, Project project) throws IOException {
		this.minecraftVersion = minecraftVersion;
		this.loader = loader;
		path = project.getProjectDir().toPath();
		version = project.getVersion().toString();
		majorVersion = Integer.parseInt(minecraftVersion.split("\\.")[1]);
		javaLanguageVersion = majorVersion <= 16 ? 8 : majorVersion == 17 ? 16 : 17;
		final Path mixinPath = path.resolve("src/main/java/org/mtr/mixin");
		Files.createDirectories(mixinPath);
		CreateClientWorldRenderingMixin.create(minecraftVersion, loader, mixinPath, "org.mtr.mixin");
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

	public void copyLootTables() throws IOException {
		final Path directory = path.resolve("src/main/resources/data/mtr/loot_tables/blocks");
		Files.createDirectories(directory);
		try (final Stream<Path> stream = Files.list(path.resolve("src/main/loot_table_templates"))) {
			stream.forEach(lootTablePath -> {
				try {
					FileUtils.write(
							directory.resolve(lootTablePath.getFileName()).toFile(),
							FileUtils.readFileToString(lootTablePath.toFile(), StandardCharsets.UTF_8).replace("@condition@", majorVersion >= 20 ? "any_of" : "alternative"),
							StandardCharsets.UTF_8
					);
				} catch (Exception e) {
					logException(e);
				}
			});
		}
	}

	public void copyBuildFile() throws IOException {
		final Path directory = path.getParent().resolve("build/release");
		Files.createDirectories(directory);
		Files.copy(path.resolve(String.format("build/libs/%s-%s%s.jar", loader, version, loader.equals("fabric") ? "" : "-all")), directory.resolve(String.format("MTR-%s-%s-%s.jar", loader, minecraftVersion, version)), StandardCopyOption.REPLACE_EXISTING);
	}

	public static List<String> getLibraries(String minecraftVersion) {
		final JsonArray versionsArray = getJson("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json").getAsJsonObject().getAsJsonArray("versions");
		for (final JsonElement versionElement : versionsArray) {
			final JsonObject versionObject = versionElement.getAsJsonObject();
			if (versionObject.get("id").getAsString().equals(minecraftVersion)) {
				final JsonArray librariesArray = getJson(versionObject.get("url").getAsString()).getAsJsonObject().getAsJsonArray("libraries");
				final List<String> libraries = new ArrayList<>();
				librariesArray.forEach(libraryElement -> {
					final String[] librarySplit = libraryElement.getAsJsonObject().get("name").getAsString().split(":");
					libraries.add(String.format("%s:%s:", librarySplit[0], librarySplit[1]));
				});
				return libraries;
			}
		}
		return new ArrayList<>();
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
