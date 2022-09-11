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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UpdateSolder {

	private final String sftpPath;
	private final String apiToken;
	private final SFTPClient sftpClient;

	private static final Path OUTPUT_PATH = Paths.get("temp");
	private static final String[] MINECRAFT_VERSIONS = {"1.16.5", "1.17.1", "1.18.2", "1.19.2"};
	private static final Map<String, String> MOD_ID_MAP = new HashMap<>();
	private static final Map<String, String> MODPACK_ID_MAP = new HashMap<>();
	private static final Map<String, String> DUMMY_BUILD_ID_MAP = new HashMap<>();
	private static final Map<String, String> DUMMY_MOD_MAP = new HashMap<>();
	private static final Map<String, String> NEW_MOD_MAP = new HashMap<>();

	static {
		MOD_ID_MAP.put("mtr", "1");
		MOD_ID_MAP.put("mtr-london-underground-addon", "4");
		MOD_ID_MAP.put("fabric-loader", "2");
		MOD_ID_MAP.put("fabric-api", "3");
		MOD_ID_MAP.put("forge-loader", "5");
		MOD_ID_MAP.put("forge-architectury", "6");
		MOD_ID_MAP.put("modmenu", "7");

		MODPACK_ID_MAP.put("fabric-1.16.5", "2");
		MODPACK_ID_MAP.put("forge-1.16.5", "4");
		MODPACK_ID_MAP.put("fabric-1.17.1", "3");
		MODPACK_ID_MAP.put("forge-1.17.1", "8");
		MODPACK_ID_MAP.put("fabric-1.18.2", "5");
		MODPACK_ID_MAP.put("forge-1.18.2", "9");
		MODPACK_ID_MAP.put("fabric-1.19.2", "7");
		MODPACK_ID_MAP.put("forge-1.19.2", "10");
		MODPACK_ID_MAP.put("centown-classic", "6");

		DUMMY_BUILD_ID_MAP.put("fabric-1.16.5", "143");
		DUMMY_BUILD_ID_MAP.put("forge-1.16.5", "144");
		DUMMY_BUILD_ID_MAP.put("fabric-1.17.1", "145");
		DUMMY_BUILD_ID_MAP.put("forge-1.17.1", "146");
		DUMMY_BUILD_ID_MAP.put("fabric-1.18.2", "147");
		DUMMY_BUILD_ID_MAP.put("forge-1.18.2", "148");
		DUMMY_BUILD_ID_MAP.put("fabric-1.19.2", "149");
		DUMMY_BUILD_ID_MAP.put("forge-1.19.2", "150");
		DUMMY_BUILD_ID_MAP.put("centown-classic", "142");

		DUMMY_MOD_MAP.put("mtr-fabric-1.16.5", "fabric-1.16.5-3.0.1");
		DUMMY_MOD_MAP.put("mtr-fabric-1.17.1", "fabric-1.17.1-3.0.1");
		DUMMY_MOD_MAP.put("mtr-fabric-1.18.2", "fabric-1.18.2-3.0.1");
		DUMMY_MOD_MAP.put("mtr-fabric-1.19.2", "fabric-1.19-3.0.1");
		DUMMY_MOD_MAP.put("mtr-forge-1.16.5", "forge-1.16.5-3.0.1");
		DUMMY_MOD_MAP.put("mtr-forge-1.17.1", "forge-1.17.1-3.0.1");
		DUMMY_MOD_MAP.put("mtr-forge-1.18.2", "forge-1.18.2-3.0.1");
		DUMMY_MOD_MAP.put("mtr-forge-1.19.2", "forge-1.19-3.0.1");
		DUMMY_MOD_MAP.put("mtr-london-underground-addon-fabric-1.16.5", "fabric-1.16.5-3.0.1-1.2");
		DUMMY_MOD_MAP.put("mtr-london-underground-addon-fabric-1.17.1", "fabric-1.17.1-3.0.1-1.2");
		DUMMY_MOD_MAP.put("mtr-london-underground-addon-fabric-1.18.2", "fabric-1.18.2-3.0.1-1.2");
		DUMMY_MOD_MAP.put("mtr-london-underground-addon-fabric-1.19.2", "fabric-1.19.2-3.0.1-1.2");
		DUMMY_MOD_MAP.put("mtr-london-underground-addon-forge-1.16.5", "forge-1.16.5-3.0.1-1.2");
		DUMMY_MOD_MAP.put("mtr-london-underground-addon-forge-1.17.1", "forge-1.17.1-3.0.1-1.2");
		DUMMY_MOD_MAP.put("mtr-london-underground-addon-forge-1.18.2", "forge-1.18.2-3.0.1-1.2");
		DUMMY_MOD_MAP.put("mtr-london-underground-addon-forge-1.19.2", "forge-1.19.2-3.0.1-1.2");
		DUMMY_MOD_MAP.put("fabric-loader-1.16.5", "1.16.5-0.14.8");
		DUMMY_MOD_MAP.put("fabric-loader-1.17.1", "1.17.1-0.14.8");
		DUMMY_MOD_MAP.put("fabric-loader-1.18.2", "1.18.2-0.14.8");
		DUMMY_MOD_MAP.put("fabric-loader-1.19.2", "1.19-0.14.8");
		DUMMY_MOD_MAP.put("fabric-api-1.16.5", "0.42.0+1.16");
		DUMMY_MOD_MAP.put("fabric-api-1.17.1", "0.46.1+1.17");
		DUMMY_MOD_MAP.put("fabric-api-1.18.2", "0.57.0+1.18.2");
		DUMMY_MOD_MAP.put("fabric-api-1.19.2", "0.57.0+1.19");
		DUMMY_MOD_MAP.put("forge-loader-1.16.5", "1.16.5-36.2.34");
		DUMMY_MOD_MAP.put("forge-loader-1.17.1", "1.17.1-37.1.1");
		DUMMY_MOD_MAP.put("forge-loader-1.18.2", "1.18.2-40.1.0");
		DUMMY_MOD_MAP.put("forge-loader-1.19.2", "1.19-41.0.63");
		DUMMY_MOD_MAP.put("forge-architectury-1.16.5", "1.32.66+forge-1.16.5");
		DUMMY_MOD_MAP.put("forge-architectury-1.17.1", "2.10.12+forge-1.17.1");
		DUMMY_MOD_MAP.put("forge-architectury-1.18.2", "4.4.71+forge-1.18.2");
		DUMMY_MOD_MAP.put("forge-architectury-1.19.2", "5.7.28+forge-1.19");
		DUMMY_MOD_MAP.put("modmenu-1.16.5", "1.16.5-1.16.22");
		DUMMY_MOD_MAP.put("modmenu-1.17.1", "2.0.15-1.17.1");
		DUMMY_MOD_MAP.put("modmenu-1.18.2", "3.2.3-1.18.2");
		DUMMY_MOD_MAP.put("modmenu-1.19.2", "4.0.0-1.19");
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
		final String luVersion = readUrl("https://www.minecrafttransitrailway.com/libs/latest/latest-lu-addon.json", new JsonObject()).getAsJsonObject().get("version").getAsString();
		for (final String minecraftVersion : MINECRAFT_VERSIONS) {
			for (final Loader loader : Loader.values()) {
				final String modVersionMtr = loader.loader + "-" + minecraftVersion + "-" + mtrVersion;
				final String modVersionLu = loader.loader + "-" + minecraftVersion + "-" + luVersion;
				uploadZip("https://www.minecrafttransitrailway.com/libs/latest/MTR-" + loader.loader + "-" + minecraftVersion + "-latest.jar", minecraftVersion, "mtr", "mods/MTR-" + modVersionMtr + ".jar", modVersionMtr);
				uploadZip("https://www.minecrafttransitrailway.com/libs/latest/MTR-LU-Addon-" + loader.loader + "-" + minecraftVersion + "-latest.jar", minecraftVersion, "mtr-london-underground-addon", "mods/MTR-LU-Addon-" + modVersionLu + ".jar", modVersionLu);
			}

			try {
				final String fabricLoaderVersion = readUrl("https://meta.fabricmc.net/v2/versions/loader/" + minecraftVersion, new JsonArray()).getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject("loader").get("version").getAsString();
				uploadZip("https://meta.fabricmc.net/v2/versions/loader/" + minecraftVersion + "/" + fabricLoaderVersion + "/profile/json", minecraftVersion, "fabric-loader", "bin/version.json", minecraftVersion + "-" + fabricLoaderVersion);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				final JsonObject forgeVersionsObject = readUrl("https://files.minecraftforge.net/net/minecraftforge/forge/promotions_slim.json", new JsonObject()).getAsJsonObject().getAsJsonObject("promos");
				final String forgeVersion = minecraftVersion + "-" + forgeVersionsObject.get(minecraftVersion + "-latest").getAsString();
				uploadZip("https://maven.minecraftforge.net/net/minecraftforge/forge/" + forgeVersion + "/forge-" + forgeVersion + "-installer.jar", minecraftVersion, "forge-loader", "bin/modpack.jar", forgeVersion);
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

		try {
			Thread.sleep(10000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final String buildVersion = mtrVersion + "-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
		for (final String minecraftVersion : MINECRAFT_VERSIONS) {
			for (final Loader loader : Loader.values()) {
				final String combinedVersion = loader.loader + "-" + minecraftVersion;
				final String buildId = sendRequest("create-build?modpack_id=%s&version=%s&minecraft=%s&clone=%s&memory-enabled=on&memory=2048", MODPACK_ID_MAP.get(combinedVersion), buildVersion, minecraftVersion, DUMMY_BUILD_ID_MAP.get(combinedVersion)).get("build_id").getAsString();
				updateModInBuild(buildId, DUMMY_MOD_MAP.get("mtr-" + combinedVersion), combinedVersion + "-" + mtrVersion, loader.loader, minecraftVersion);
				updateModInBuild(buildId, DUMMY_MOD_MAP.get("mtr-london-underground-addon-" + combinedVersion), combinedVersion + "-" + luVersion, loader.loader, minecraftVersion);

				if (loader == Loader.FABRIC) {
					updateModInBuild(buildId, DUMMY_MOD_MAP.get("fabric-loader-" + minecraftVersion), NEW_MOD_MAP.get("fabric-loader-" + minecraftVersion), loader.loader, minecraftVersion);
					updateModInBuild(buildId, DUMMY_MOD_MAP.get("fabric-api-" + minecraftVersion), NEW_MOD_MAP.get("fabric-api-" + minecraftVersion), loader.loader, minecraftVersion);
					updateModInBuild(buildId, DUMMY_MOD_MAP.get("modmenu-" + minecraftVersion), NEW_MOD_MAP.get("modmenu-" + minecraftVersion), loader.loader, minecraftVersion);
				} else {
					updateModInBuild(buildId, DUMMY_MOD_MAP.get("forge-loader-" + minecraftVersion), NEW_MOD_MAP.get("forge-loader-" + minecraftVersion), loader.loader, minecraftVersion);
					updateModInBuild(buildId, DUMMY_MOD_MAP.get("forge-architectury-" + minecraftVersion), NEW_MOD_MAP.get("forge-architectury-" + minecraftVersion), loader.loader, minecraftVersion);
				}
			}
		}
	}

	private void uploadZip(String url, String minecraftVersion, String modId, String zipPath, String modVersion) {
		if (!MOD_ID_MAP.containsKey(modId)) {
			return;
		}

		NEW_MOD_MAP.put(modId + "-" + minecraftVersion, modVersion);

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

	private void updateModInBuild(String buildId, String oldModVersion, String newModVersion, String loaderVersion, String minecraftVersion) {
		final JsonObject response = sendRequest("update-build?build_id=%s&modversion_id=%s&version=%s", buildId, oldModVersion, newModVersion);
		if (response.has("status")) {
			printStatus(String.format("Updated mod %s:", response.get("status").getAsString()), loaderVersion, minecraftVersion, newModVersion);
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
							uploadZip(fileObject.get("url").getAsString(), minecraftVersion, modId, "mods/" + fileObject.get("filename").getAsString(), appendMinecraftVersion(versionObject.get("version_number").getAsString(), minecraftVersion));
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
		System.out.printf("%-30s %-60s%s\n", message, String.format("%s-%s", modId, modVersion), md5);
	}

	private enum Loader {
		FABRIC("fabric"), FORGE("forge");

		private final String loader;

		Loader(String loader) {
			this.loader = loader;
		}
	}
}
