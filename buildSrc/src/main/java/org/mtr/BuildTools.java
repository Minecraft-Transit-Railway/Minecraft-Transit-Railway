package org.mtr;

import com.crowdin.client.Client;
import com.crowdin.client.core.model.Credentials;
import com.crowdin.client.translations.model.CrowdinTranslationCreateProjectBuildForm;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Gradle build-time utility class invoked by custom Gradle tasks in {@code buildSrc/}.
 *
 * <p>Handles all code-generation and asset-preparation steps that must run before the main
 * compilation phase:
 * <ul>
 *   <li>Downloading and unpacking Crowdin translations into {@code assets/mtr/lang/}.</li>
 *   <li>Generating the {@code TranslationProvider} interface from {@code en_us.json} using
 *       official Mojang mappings ({@code net.minecraft.network.chat.Component}).</li>
 *   <li>Expanding vehicle JSON templates into {@code mtr_custom_resources.json}.</li>
 *   <li>Copying the assembled JAR into the {@code build/release/} directory with the
 *       canonical {@code MTR-<loader>-<version>+<mcVersion>.jar} name.</li>
 *   <li>Fetching the active Patreon supporter list and generating {@code Patreon.java}.</li>
 *   <li>Downloading and patching the upstream {@code javagl/Obj} library source.</li>
 *   <li>Fixing shaded-library import paths for schema-generated classes.</li>
 * </ul>
 *
 * <p>This class is loader-agnostic: the same instance is used for Fabric and NeoForge builds;
 * the {@code loader} field distinguishes the two at copy time.
 */
public final class BuildTools {

	public final String minecraftVersion;
	public final String loader;

	private final Path path;
	private final String version;

	private static final Logger LOGGER = LogManager.getLogger("Build");
	private static final long CROWDIN_PROJECT_ID = 455212;

	/**
	 * @param minecraftVersion the Minecraft version string (e.g. {@code "1.21.4"})
	 * @param loader           the mod loader identifier ({@code "fabric"} or {@code "neoforge"})
	 * @param version          the mod version string (e.g. {@code "4.1.0-beta.1"})
	 * @param projectPath      root directory of the project whose assets are being processed
	 */
	public BuildTools(String minecraftVersion, String loader, String version, File projectPath) {
		this.minecraftVersion = minecraftVersion;
		this.loader = loader;
		this.version = version;
		path = projectPath.toPath();
	}

	/**
	 * Downloads all translations from the Crowdin project and writes them into
	 * {@code src/main/resources/assets/mtr/lang/}.
	 * Polls until the Crowdin build finishes before downloading the zip.
	 * A no-op when {@code crowdinKey} is empty (local/offline builds).
	 *
	 * @param crowdinKey Crowdin OAuth2 API key; pass an empty string to skip
	 * @throws IOException          if the download or file-write fails
	 * @throws InterruptedException if the polling sleep is interrupted
	 */
	public void downloadTranslations(String crowdinKey) throws IOException, InterruptedException {
		if (!crowdinKey.isEmpty()) {
			final CrowdinTranslationCreateProjectBuildForm crowdinTranslationCreateProjectBuildForm = new CrowdinTranslationCreateProjectBuildForm();
			crowdinTranslationCreateProjectBuildForm.setSkipUntranslatedStrings(true);
			crowdinTranslationCreateProjectBuildForm.setSkipUntranslatedFiles(false);
			crowdinTranslationCreateProjectBuildForm.setExportApprovedOnly(false);

			final Client client = new Client(new Credentials(crowdinKey, null));
			final long buildId = client.getTranslationsApi().buildProjectTranslation(CROWDIN_PROJECT_ID, crowdinTranslationCreateProjectBuildForm).getData().getId();

			while (!client.getTranslationsApi().checkBuildStatus(CROWDIN_PROJECT_ID, buildId).getData().getStatus().equals("finished")) {
				Thread.sleep(1000);
			}

			try (final InputStream inputStream = URI.create(client.getTranslationsApi().downloadProjectTranslations(CROWDIN_PROJECT_ID, buildId).getData().getUrl()).toURL().openStream()) {
				try (final ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
					ZipEntry zipEntry;
					while ((zipEntry = zipInputStream.getNextEntry()) != null) {
						final String name = zipEntry.getName().toLowerCase(Locale.ENGLISH);
						final byte[] content = IOUtils.toByteArray(zipInputStream);
						FileUtils.writeByteArrayToFile(path.resolve("src/main/resources/assets/mtr/lang").resolve(name).toFile(), content);
						zipInputStream.closeEntry();
					}
				}
			}
		}
	}

	/**
	 * Generates {@code src/main/java/org/mtr/generated/lang/TranslationProvider.java} from
	 * {@code assets/mtr/lang/en_us.json}.
	 *
	 * <p>The generated interface exposes one {@code TranslationHolder} constant per translation
	 * key. {@code TranslationHolder} wraps the key and delegates to
	 * {@code net.minecraft.network.chat.Component} (official Mojang mappings) for
	 * {@link net.minecraft.network.chat.MutableComponent} / {@link net.minecraft.network.chat.Component}
	 * conversions. The generated source is overwritten on every build; do not edit it by hand.
	 *
	 * @throws IOException if reading the language file or writing the generated source fails
	 */
	public void generateTranslations() throws IOException {
		final StringBuilder stringBuilder = new StringBuilder("package org.mtr.generated.lang;import net.minecraft.client.Minecraft;import net.minecraft.network.chat.Component;import net.minecraft.network.chat.MutableComponent;public interface TranslationProvider{\n");
		JsonParser.parseString(FileUtils.readFileToString(path.resolve("src/main/resources/assets/mtr/lang/en_us.json").toFile(), StandardCharsets.UTF_8)).getAsJsonObject().entrySet().forEach(entry -> {
			final String key = entry.getKey();
			if (key.startsWith("block.") || key.startsWith("item.") || key.startsWith("entity.") || key.startsWith("itemGroup.")) {
				stringBuilder.append("@SuppressWarnings(\"unused\")");
			}
			stringBuilder.append(String.format("TranslationHolder %s=new TranslationHolder(\"%s\");\n", key.replace(".", "_").toUpperCase(Locale.ENGLISH), key));
		});
		stringBuilder.append("class TranslationHolder{public final String key;private TranslationHolder(String key){this.key=key;}\n");
		stringBuilder.append("public MutableComponent getMutableText(Object...arguments){return Component.translatable(key,arguments);}\n");
		stringBuilder.append("public Component getText(Object...arguments){return Component.translatable(key,arguments);}\n");
		stringBuilder.append("public String getString(Object...arguments){return getMutableText(arguments).getString();}\n");
		stringBuilder.append("public int width(Object...arguments){return Minecraft.getInstance().font.width(getMutableText(arguments));}\n");
		stringBuilder.append("}}");
		FileUtils.write(path.resolve("src/main/java/org/mtr/generated/lang/TranslationProvider.java").toFile(), stringBuilder.toString(), StandardCharsets.UTF_8);
	}

	/**
	 * Expands vehicle JSON templates in {@code src/main/vehicle_templates/} and writes the
	 * combined vehicle list into {@code src/main/resources/assets/mtr/mtr_custom_resources.json}
	 * by replacing the {@code "@token@"} placeholder in the template file.
	 *
	 * <p>Each template file defines a {@code replacements} object whose arrays drive N
	 * variations; bogie positions are computed automatically from the vehicle length.
	 *
	 * @throws IOException if reading templates or writing the output file fails
	 */
	public void copyVehicleTemplates() throws IOException {
		final ObjectArrayList<String> vehicles = new ObjectArrayList<>();

		try (final Stream<Path> stream = Files.list(path.resolve("src/main/vehicle_templates"))) {
			stream.sorted().forEach(vehicleTemplatePath -> {
				try {
					final JsonObject fileObject = JsonParser.parseString(FileUtils.readFileToString(vehicleTemplatePath.toFile(), StandardCharsets.UTF_8)).getAsJsonObject();
					final JsonObject replacementObject = fileObject.getAsJsonObject("replacements");
					final int variationCount = replacementObject.entrySet().stream().map(Map.Entry::getValue).findFirst().orElse(new JsonArray()).getAsJsonArray().size();

					fileObject.getAsJsonArray("vehicles").forEach(vehicleElement -> {
						for (int i = 0; i < variationCount; i++) {
							final JsonObject vehicleObject = vehicleElement.getAsJsonObject();
							final double length = replacementObject.getAsJsonArray("lengths").get(i).getAsDouble();
							if (length > 0) {
								final String id = vehicleObject.get("id").getAsString();
								vehicleObject.addProperty("length", length);

								if (replacementObject.toString().contains("boat_small") && replacementObject.toString().contains("boat_medium")) {
									vehicleObject.addProperty("bogie1Position", -1);
									vehicleObject.addProperty("bogie2Position", 1);
								} else if (replacementObject.toString().contains("a320")) {
									vehicleObject.addProperty("bogie1Position", -14.25);
									vehicleObject.addProperty("bogie2Position", -2);
								} else if (replacementObject.toString().contains("br_423")) {
									vehicleObject.addProperty("bogie1Position", -6);
									vehicleObject.addProperty("bogie2Position", 6);
								} else if (length <= 4 || length <= 14 && id.contains("cab_3")) {
									vehicleObject.addProperty("bogie1Position", 0);
									vehicleObject.addProperty("bogie2Position", 0);
								} else {
									vehicleObject.addProperty("bogie1Position", -length / 2 + (length <= 14 && (id.contains("trailer") || id.contains("cab_2")) ? 0 : 4));
									vehicleObject.addProperty("bogie2Position", length / 2 - (length <= 14 && (id.contains("trailer") || id.contains("cab_1")) ? 0 : 4));
								}
							}

							String newFileString = vehicleObject.toString();
							for (final Map.Entry<String, JsonElement> entry : replacementObject.entrySet()) {
								newFileString = newFileString.replace(String.format("@%s@", entry.getKey()), entry.getValue().getAsJsonArray().get(i).getAsString());
							}
							vehicles.add(newFileString);
						}
					});
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			});
		}

		FileUtils.write(
			path.resolve("src/main/resources/assets/mtr/mtr_custom_resources.json").toFile(),
			FileUtils.readFileToString(path.resolve("src/main/mtr_custom_resources_template.json").toFile(), StandardCharsets.UTF_8).replace("\"@token@\"", String.join(",", vehicles)),
			StandardCharsets.UTF_8
		);
	}

	/**
	 * Copies the assembled JAR from {@code build/libs/mtr-<loader>-<version>.jar} into
	 * {@code <root>/build/release/MTR-<loader>-<version>+<minecraftVersion>.jar}, creating
	 * the release directory if it does not exist.
	 *
	 * @throws IOException if the copy fails
	 */
	public void copyBuildFile() throws IOException {
		final Path directory = path.getParent().resolve("build/release");
		Files.createDirectories(directory);
		Files.copy(path.resolve(String.format("build/libs/mtr-%s-%s.jar", loader, version)), directory.resolve(String.format("MTR-%s-%s+%s.jar", loader, version, minecraftVersion)), StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * Fetches the active Patreon supporter list via the Patreon OAuth2 v2 API and generates
	 * {@code src/main/java/org/mtr/Patreon.java} with a {@code PATREON_LIST} constant array.
	 *
	 * <p>Supporters are sorted by tier amount descending, then by lifetime support descending.
	 * A no-op (empty list) when {@code key} is empty (local/offline builds).
	 *
	 * @param key Patreon Bearer API token; pass an empty string to skip
	 * @throws IOException if writing the generated source file fails
	 */
	public void getPatreonList(String key) throws IOException {
		final ObjectArrayList<Patreon> patreonList = new ObjectArrayList<>();
		final StringBuilder stringBuilder = new StringBuilder("package org.mtr;public class Patreon{public final String name;public final String tierTitle;public final int tierAmount;public final int tierColor;");
		stringBuilder.append("private Patreon(String name,String tierTitle,int tierAmount,int tierColor){this.name=name;this.tierTitle=tierTitle;this.tierAmount=tierAmount;this.tierColor=tierColor;}public static Patreon[]PATREON_LIST={\n");

		if (!key.isEmpty()) {
			try {
				final JsonObject jsonObjectData = getJson("https://www.patreon.com/api/oauth2/v2/campaigns/7782318/members?include=currently_entitled_tiers&fields%5Bmember%5D=full_name,lifetime_support_cents,patron_status&fields%5Btier%5D=title,amount_cents&page%5Bcount%5D=" + Integer.MAX_VALUE, "Authorization", "Bearer " + key).getAsJsonObject();
				final Object2ObjectAVLTreeMap<String, JsonObject> idMap = new Object2ObjectAVLTreeMap<>();
				jsonObjectData.getAsJsonArray("included").forEach(jsonElementData -> {
					final JsonObject jsonObject = jsonElementData.getAsJsonObject();
					idMap.put(jsonObject.get("id").getAsString(), jsonObject.getAsJsonObject("attributes"));
				});

				jsonObjectData.getAsJsonArray("data").forEach(jsonElementData -> {
					final JsonObject jsonObjectAttributes = jsonElementData.getAsJsonObject().getAsJsonObject("attributes");
					final JsonArray jsonObjectTiers = jsonElementData.getAsJsonObject().getAsJsonObject("relationships").getAsJsonObject("currently_entitled_tiers").getAsJsonArray("data");
					if (!jsonObjectAttributes.get("patron_status").isJsonNull() && jsonObjectAttributes.get("patron_status").getAsString().equals("active_patron") && !jsonObjectTiers.isEmpty()) {
						patreonList.add(new Patreon(jsonObjectAttributes, idMap.get(jsonObjectTiers.get(0).getAsJsonObject().get("id").getAsString())));
					}
				});
			} catch (Exception ignored) {
			}

			Collections.sort(patreonList);
		}

		patreonList.forEach(patreon -> stringBuilder.append(String.format("new Patreon(\"%s\",\"%s\",%s,%s),\n", patreon.name, patreon.tierTitle, patreon.tierAmount, patreon.tierColor)));
		stringBuilder.append("};}");
		FileUtils.write(path.resolve("src/main/java/org/mtr/Patreon.java").toFile(), stringBuilder, StandardCharsets.UTF_8);
	}

	/**
	 * Downloads the upstream {@code javagl/Obj} library source zip and extracts it into
	 * {@code src/main/java/de/javagl/obj/}, applying two source patches:
	 * <ul>
	 *   <li>{@code DefaultObj.java} — records the active material group name per face.</li>
	 *   <li>{@code ObjReader.java} — groups objects by name so that {@code g}/{@code o}
	 *       directives with the same identifier are merged into one group.</li>
	 * </ul>
	 * Errors are logged and suppressed so the build continues even when offline.
	 */
	public void setupObjLibrary() {
		final Path libraryPath = path.resolve("src/main/java/de/javagl/obj");
		try {
			FileUtils.copyURLToFile(URI.create("https://github.com/javagl/Obj/archive/refs/heads/master.zip").toURL(), libraryPath.resolve("master.zip").toFile());
			try (final ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(libraryPath.resolve("master.zip")))) {
				ZipEntry zipEntry = zipInputStream.getNextEntry();
				while (zipEntry != null) {
					final Path zipPath = Paths.get(zipEntry.getName());
					if (!zipEntry.isDirectory() && zipPath.startsWith("Obj-master/src/main/java/de/javagl/obj")) {
						final String fileName = zipPath.getFileName().toString();
						final String content = IOUtils.toString(zipInputStream, StandardCharsets.UTF_8);
						final String newContent = switch (fileName) {
							case "DefaultObj.java" -> appendAfter(
								content,
								"startedGroupNames.put(face, nextActiveGroupNames);", "startedMaterialGroupNames.put(face, activeMaterialGroupName);"
							);
							case "ObjReader.java" -> appendAfter(
								content,
								"ObjFaceParser objFaceParser = new ObjFaceParser();", "String groupOrObject = \"\";",
								"case \"g\":", "case \"o\": if (!groupOrObject.equals(identifier) && !groupOrObject.isEmpty()) break;",
								"output.setActiveGroupNames(Arrays.asList(groupNames));", "groupOrObject = identifier;"
							);
							default -> content;
						};
						Files.writeString(libraryPath.resolve(fileName), newContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
					}
					zipEntry = zipInputStream.getNextEntry();
				}
				zipInputStream.closeEntry();
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	/**
	 * Temporary method to fix imports to use shadowed relocated library paths generated by Transport Simulation Core's schema generator.
	 * Also moves these files to the correct directory.
	 */
	public void fixImports(Project project, String inputPath) {
		final Path additionalPath = Paths.get("src/main/java/org/mtr/").resolve(inputPath);
		final Path originalDirectory = project.getProjectDir().toPath().resolve(additionalPath);
		try (final Stream<Path> schemasStream = Files.list(originalDirectory)) {
			schemasStream.forEach(filePath -> {
				try {
					Files.createDirectories(path.resolve(additionalPath));
					Files.writeString(path.resolve(additionalPath).resolve(filePath.getFileName()), Files.readString(filePath).replace("it.unimi", "org.mtr.libraries.it.unimi"));
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			});
			FileUtils.deleteDirectory(originalDirectory.toFile());
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	private static JsonElement getJson(String url, String... requestProperties) {
		for (int i = 0; i < 5; i++) {
			try {
				final HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
				connection.setUseCaches(false);

				for (int j = 0; j < requestProperties.length / 2; j++) {
					connection.setRequestProperty(requestProperties[2 * j], requestProperties[2 * j + 1]);
				}

				try (final InputStream inputStream = connection.getInputStream()) {
					return JsonParser.parseString(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			} catch (Exception e) {
				LOGGER.error("", e);
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				LOGGER.error("", e);
			}
		}

		return new JsonObject();
	}

	private static String appendAfter(String string, String... replacements) {
		String newString = string;
		for (int i = 1; i < replacements.length; i += 2) {
			newString = newString.replace(replacements[i - 1], replacements[i - 1] + replacements[i]);
		}
		return newString;
	}

	private static class Patreon implements Comparable<Patreon> {

		private final String name;
		private final String tierTitle;
		private final int tierAmount;
		private final int tierColor;
		private final int totalAmount;

		public Patreon(JsonObject jsonObjectPatron, JsonObject jsonObjectTiers) {
			name = jsonObjectPatron.get("full_name").getAsString();
			totalAmount = jsonObjectPatron.get("lifetime_support_cents").getAsInt();
			tierTitle = jsonObjectTiers.get("title").getAsString();
			tierAmount = jsonObjectTiers.get("amount_cents").getAsInt();

			int color = 0xFFFFFF;
			try {
				color = RailType.valueOf(tierTitle.toUpperCase(Locale.ENGLISH)).color;
			} catch (Exception ignored) {
			}
			tierColor = color;
		}

		@Override
		public int compareTo(Patreon patreon) {
			return patreon.tierAmount == tierAmount ? patreon.totalAmount - totalAmount : patreon.tierAmount - tierAmount;
		}
	}

	private enum RailType {
		WOODEN(0xFF8F7748),
		STONE(0xFF707070),
		IRON(0xFFA7A7A7),
		DIAMOND(0xFF5CDBD5),
		PLATFORM(0xFF993333),
		SIDING(0xFFE5E533);

		private final int color;

		RailType(int color) {
			this.color = color;
		}
	}
}
