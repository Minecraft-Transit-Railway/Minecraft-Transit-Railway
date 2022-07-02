import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.apache.commons.codec.digest.DigestUtils;
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

	private final String sftpPath;
	private final String apiToken;
	private final SFTPClient sftpClient;

	private static final Path OUTPUT_PATH = Paths.get("temp");
	private static final String[] MINECRAFT_VERSIONS = {"1.16.5", "1.17.1", "1.18", "1.18.2", "1.19"};
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
		if (args.length == 5) {
			new UpdateSolder(args[0], args[1], args[2], args[3], args[4]);
		}
	}

	private UpdateSolder(String sftpHost, String sftpUsername, String sftpPassword, String sftpPath, String apiToken) throws IOException {
		this.sftpPath = sftpPath;
		this.apiToken = apiToken;

		final SSHClient sshClient = new SSHClient();
		sshClient.addHostKeyVerifier(new PromiscuousVerifier());
		sshClient.connect(sftpHost);
		sshClient.authPassword(sftpUsername, sftpPassword);
		sftpClient = sshClient.newSFTPClient();

		FileUtils.deleteQuietly(OUTPUT_PATH.toFile());

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
				uploadZip("https://maven.minecraftforge.net/net/minecraftforge/forge/" + forgeVersion + "/forge-" + forgeVersion + "-installer.jar", "forge-loader", "bin/modpack.jar", forgeVersion);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		uploadModrinthMod(Loader.FABRIC, "P7dR8mSH", "fabric-api");
		uploadModrinthMod(Loader.FABRIC, "mOgUt4GM", "modmenu");
		uploadModrinthMod(Loader.FORGE, "lhGA9TYQ", "forge-architectury");

		FileUtils.deleteQuietly(OUTPUT_PATH.toFile());
		sftpClient.close();
		sshClient.disconnect();
	}

	private void uploadZip(String url, String modId, String zipPath, String modVersion) {
		if (!MOD_ID_MAP.containsKey(modId)) {
			return;
		}

		try {
			Files.createDirectories(OUTPUT_PATH.resolve(modId));
			final InputStream inputStream = new URL(url).openStream();
			final Path outputZipPath = OUTPUT_PATH.resolve(modId).resolve(modId + "-" + modVersion + ".zip");
			final OutputStream outputStream = Files.newOutputStream(outputZipPath);
			final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
			zipOutputStream.putNextEntry(new ZipEntry(zipPath));
			ByteStreams.copy(inputStream, zipOutputStream);
			zipOutputStream.closeEntry();
			zipOutputStream.close();
			outputStream.close();
			inputStream.close();

			final String serverMd5 = rehash(modId, modVersion);
			final InputStream zipInputStream = Files.newInputStream(outputZipPath);
			final String newMd5 = DigestUtils.md5Hex(zipInputStream);
			zipInputStream.close();

			if (serverMd5 == null || !serverMd5.equalsIgnoreCase(newMd5)) {
				printStatus("Uploading to SFTP:", modId, modVersion, newMd5);
				sftpClient.put(outputZipPath.toString(), sftpPath + "/mods/" + modId);

				final JsonObject response = sendRequest("add-version?mod-id=%s&add-version=%s", MOD_ID_MAP.get(modId), modVersion);
				if (response.has("status")) {
					final String status = response.get("status").getAsString();
					if (status.equals("success")) {
						printStatus(String.format("Updating Solder %s:", status), modId, modVersion, "");
					}
				}

				rehash(modId, modVersion);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String rehash(String modId, String modVersion) {
		final JsonObject response = sendRequest("update-hash?mod-id=%s&update-hash=%s", MOD_ID_MAP.get(modId), modVersion);
		if (response.has("status")) {
			final String md5 = response.has("md5") ? response.get("md5").getAsString() : null;
			printStatus(String.format("Rehashing %s:", response.get("status").getAsString()), modId, modVersion, md5 == null ? "" : md5);
			return md5;
		} else {
			return null;
		}
	}

	private JsonObject sendRequest(String url, String... arguments) {
		try {
			final Object[] parameters = new Object[arguments.length];
			for (int i = 0; i < arguments.length; i++) {
				parameters[i] = URLEncoder.encode(arguments[i], "UTF-8");
			}
			final HttpURLConnection http = (HttpURLConnection) new URL(String.format("https://minecrafttransitrailway.com/api/mtr/" + url, parameters)).openConnection();
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			http.setRequestProperty("Authorization", "Bearer " + apiToken);
			return new JsonParser().parse(IOUtils.toString(http.getInputStream(), StandardCharsets.UTF_8)).getAsJsonObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JsonObject();
	}

	private void uploadModrinthMod(Loader loader, String projectId, String modId) {
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

	private static <T extends JsonElement> JsonElement readUrl(String url, T defaultValue) {
		try (InputStream inputStream = new URL(url).openStream()) {
			return new JsonParser().parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
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

	private static void printStatus(String message, String modId, String modVersion, String md5) {
		System.out.printf("%-30s %-50s%s\n", message, String.format("%s-%s", modId, modVersion), md5);
	}

	private enum Loader {
		FABRIC("fabric"), FORGE("forge");

		private final String loader;

		Loader(String loader) {
			this.loader = loader;
		}
	}
}
