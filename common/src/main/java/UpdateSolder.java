import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.spongepowered.include.com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UpdateSolder {

	private static final Path OUTPUT_PATH = Paths.get("temp");
	private static final String[] MINECRAFT_VERSIONS = {"1.16.5", "1.17.1", "1.18", "1.18.2", "1.19"};
	private static final Set<Thread> THREADS = new HashSet<>();
	private static final Map<String, String> MOD_ID_MAP = new HashMap<>();

	static {
		MOD_ID_MAP.put("mtr", "1");
		MOD_ID_MAP.put("fabric-loader", "2");
		MOD_ID_MAP.put("fabric-api", "3");
		MOD_ID_MAP.put("forge-loader", "5");
		MOD_ID_MAP.put("forge-architectury", "6");
		MOD_ID_MAP.put("modmenu", "7");
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 5) {
			return;
		}

		final String sftpHost = args[0];
		final String sftpUsername = args[1];
		final String sftpPassword = args[2];
		final String sftpPath = args[3];
		final String apiToken = args[4];

		final SSHClient sshClient = new SSHClient();
		sshClient.addHostKeyVerifier(new PromiscuousVerifier());
		sshClient.connect(sftpHost);
		sshClient.authPassword(sftpUsername, sftpPassword);
		final SFTPClient sftpClient = sshClient.newSFTPClient();

		THREADS.clear();
		FileUtils.deleteQuietly(OUTPUT_PATH.toFile());

		final String mtrVersion = readUrl("https://www.minecrafttransitrailway.com/libs/latest/latest.json", new JsonObject()).getAsJsonObject().get("version").getAsString();
		for (final String minecraftVersion : MINECRAFT_VERSIONS) {
			for (final Loader loader : Loader.values()) {
				final String modVersion = loader.loader + "-" + minecraftVersion + "-" + mtrVersion;
				uploadZip("https://www.minecrafttransitrailway.com/libs/latest/MTR-" + loader.loader + "-" + minecraftVersion + "-latest.jar", "mtr", "mods/MTR-" + modVersion + ".jar", modVersion, sftpClient, sftpPath, apiToken);
			}

			try {
				final String fabricLoaderVersion = readUrl("https://meta.fabricmc.net/v2/versions/loader/" + minecraftVersion, new JsonArray()).getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject("loader").get("version").getAsString();
				uploadZip("https://meta.fabricmc.net/v2/versions/loader/" + minecraftVersion + "/" + fabricLoaderVersion + "/profile/json", "fabric-loader", "bin/version.json", minecraftVersion + "-" + fabricLoaderVersion, sftpClient, sftpPath, apiToken);
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
				uploadZip("https://maven.minecraftforge.net/net/minecraftforge/forge/" + forgeVersion + "/forge-" + forgeVersion + "-installer.jar", "forge-loader", "bin/modpack.jar", forgeVersion, sftpClient, sftpPath, apiToken);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		uploadModrinthMod(Loader.FABRIC, "P7dR8mSH", "fabric-api", sftpClient, sftpPath, apiToken);
		uploadModrinthMod(Loader.FABRIC, "mOgUt4GM", "modmenu", sftpClient, sftpPath, apiToken);
		uploadModrinthMod(Loader.FORGE, "lhGA9TYQ", "forge-architectury", sftpClient, sftpPath, apiToken);

		THREADS.forEach(thread -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		FileUtils.deleteQuietly(OUTPUT_PATH.toFile());
		sftpClient.close();
		sshClient.disconnect();
	}

	private static void uploadZip(String url, String modId, String zipPath, String modVersion, SFTPClient sftpClient, String sftpPath, String apiToken) {
		if (!MOD_ID_MAP.containsKey(modId)) {
			return;
		}

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
				System.out.println("Uploading to SFTP: " + modId + "-" + modVersion);
				sftpClient.put(OUTPUT_PATH.toString(), sftpPath + "/mods");

				final HttpURLConnection http = (HttpURLConnection) new URL(String.format("https://minecrafttransitrailway.com/api/mtr/add-version?mod-id=%s&add-version=%s", URLEncoder.encode(MOD_ID_MAP.get(modId), "UTF-8"), URLEncoder.encode(modVersion, "UTF-8"))).openConnection();
				http.setRequestMethod("POST");
				http.setDoOutput(true);
				http.setRequestProperty("Authorization", "Bearer " + apiToken);
				System.out.println("Updating Solder: " + modId + "-" + modVersion);
				System.out.println(IOUtils.toString(http.getInputStream(), StandardCharsets.UTF_8));
				http.disconnect();
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

	private static void uploadModrinthMod(Loader loader, String projectId, String modId, SFTPClient sftpClient, String sftpPath, String apiToken) {
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
							uploadZip(fileObject.get("url").getAsString(), modId, "mods/" + fileObject.get("filename").getAsString(), appendMinecraftVersion(versionObject.get("version_number").getAsString(), minecraftVersion), sftpClient, sftpPath, apiToken);
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
