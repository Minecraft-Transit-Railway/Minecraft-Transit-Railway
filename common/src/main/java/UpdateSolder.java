import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.spongepowered.include.com.google.common.io.ByteStreams;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UpdateSolder {

	private static final Path OUTPUT_PATH = Paths.get(System.getProperty("user.home")).resolve("desktop");
	private static final String[] MINECRAFT_VERSIONS = {"1.16.5", "1.17.1", "1.18", "1.18.2", "1.19"};
	private static final Set<Thread> THREADS = new HashSet<>();

	public static void main(String[] args) {
		THREADS.clear();

		final String mtrVersion = readUrl("https://www.minecrafttransitrailway.com/libs/latest/latest.json", new JsonObject()).getAsJsonObject().get("version").getAsString();
		for (final String minecraftVersion : MINECRAFT_VERSIONS) {
			for (final Loader loader : Loader.values()) {
				final String modVersion = loader.loader + "-" + minecraftVersion + "-" + mtrVersion;
				uploadZip("https://www.minecrafttransitrailway.com/libs/latest/MTR-" + loader.loader + "-" + minecraftVersion + "-latest.jar", "mtr", "mods/MTR-" + modVersion + ".jar", modVersion);
			}

			try {
				final String fabricLoaderVersion = readUrl("https://meta.fabricmc.net/v2/versions/loader/" + minecraftVersion, new JsonArray()).getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject("loader").get("version").getAsString();
				uploadZip("https://meta.fabricmc.net/v2/versions/loader/" + minecraftVersion + "/" + fabricLoaderVersion + "/profile/json", "fabric-loader", "bin/version.json", minecraftVersion + "-" + fabricLoaderVersion);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				final JsonObject forgeVersionsObject = readUrl("https://files.minecraftforge.net/net/minecraftforge/forge/promotions_slim.json", new JsonObject()).getAsJsonObject().getAsJsonObject("promos");
				final String forgeVersion;
				if (forgeVersionsObject.has(minecraftVersion + "-recommended")) {
					forgeVersion = minecraftVersion + "-" + forgeVersionsObject.get(minecraftVersion + "-recommended").getAsString();
				} else {
					forgeVersion = minecraftVersion + "-" + forgeVersionsObject.get(minecraftVersion + "-latest").getAsString();
				}
				uploadZip("https://maven.minecraftforge.net/net/minecraftforge/forge/" + forgeVersion + "/forge-" + forgeVersion + "-installer.jar", "forge-loader", "bin/installer.jar", forgeVersion);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		uploadModrinthMod(Loader.FABRIC, "P7dR8mSH", "fabric-api");
		uploadModrinthMod(Loader.FABRIC, "mOgUt4GM", "modmenu");
		uploadModrinthMod(Loader.FORGE, "lhGA9TYQ", "forge-architectury");

		THREADS.forEach(thread -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	private static void uploadZip(String url, String modId, String zipPath, String modVersion) {
		final Thread thread = new Thread(() -> {
			try {
				Files.createDirectories(OUTPUT_PATH.resolve(modId));
				final InputStream inputStream = new URL(url).openStream();
				final OutputStream outputStream = Files.newOutputStream(OUTPUT_PATH.resolve(modId).resolve(modId + "-" + modVersion + ".zip"));
				final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
				zipOutputStream.putNextEntry(new ZipEntry(zipPath));
				ByteStreams.copy(inputStream, zipOutputStream);
				zipOutputStream.closeEntry();
				zipOutputStream.close();
				outputStream.close();
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		thread.start();
		THREADS.add(thread);
	}

	private static <T extends JsonElement> JsonElement readUrl(String url, T defaultValue) {
		try (InputStream inputStream = new URL(url).openStream()) {
			return new JsonParser().parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}

	private static void uploadModrinthMod(Loader loader, String projectId, String modId) {
		try {
			final Set<String> minecraftVersions = new HashSet<>();
			Collections.addAll(minecraftVersions, MINECRAFT_VERSIONS);
			final JsonArray versionsArray = readUrl("https://api.modrinth.com/v2/project/" + projectId + "/version", new JsonArray()).getAsJsonArray();
			for (final JsonElement jsonElement : versionsArray) {
				final JsonObject versionObject = jsonElement.getAsJsonObject();
				if (stringJsonArrayContains(versionObject.getAsJsonArray("loaders"), loader.loader)) {
					final Set<String> minecraftVersionsToRemove = new HashSet<>();
					for (final String minecraftVersion : minecraftVersions) {
						if (stringJsonArrayContains(versionObject.getAsJsonArray("game_versions"), minecraftVersion)) {
							final JsonObject fileObject = versionObject.getAsJsonArray("files").get(0).getAsJsonObject();
							uploadZip(fileObject.get("url").getAsString(), modId, "mods/" + fileObject.get("filename").getAsString(), appendMinecraftVersion(versionObject.get("version_number").getAsString(), minecraftVersion));
							minecraftVersionsToRemove.add(minecraftVersion);
						}
					}
					minecraftVersionsToRemove.forEach(minecraftVersions::remove);
					if (minecraftVersions.isEmpty()) {
						return;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean stringJsonArrayContains(JsonArray jsonArray, String text) {
		for (final JsonElement jsonElement : jsonArray) {
			if (jsonElement.getAsString().equals(text)) {
				return true;
			}
		}
		return false;
	}

	private static String appendMinecraftVersion(String text, String minecraftVersion) {
		final String[] minecraftVersionSplit = minecraftVersion.split("\\.");
		final int size = Math.min(minecraftVersionSplit.length, 2);
		final StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < size; i++) {
			stringBuilder.append(minecraftVersionSplit[i]);
			if (i < size - 1) {
				stringBuilder.append(".");
			}
		}
		if (text.contains(stringBuilder)) {
			return text;
		} else {
			return text + "-" + minecraftVersion;
		}
	}

	private enum Loader {
		FABRIC("fabric"), FORGE("forge");

		private final String loader;

		Loader(String loader) {
			this.loader = loader;
		}
	}
}
