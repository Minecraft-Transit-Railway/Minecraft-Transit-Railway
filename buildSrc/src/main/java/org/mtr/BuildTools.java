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

public final class BuildTools {

	public final String minecraftVersion;
	public final String loader;

	private final Path path;
	private final String version;

	private static final Logger LOGGER = LogManager.getLogger("Build");
	private static final long CROWDIN_PROJECT_ID = 455212;

	public BuildTools(String minecraftVersion, String loader, Project project) {
		this.minecraftVersion = minecraftVersion;
		this.loader = loader;
		path = project.getProjectDir().toPath();
		version = project.getVersion().toString();
	}

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

	public void generateTranslations() throws IOException {
		final StringBuilder stringBuilder = new StringBuilder("package org.mtr.generated.lang;import net.minecraft.client.MinecraftClient;import net.minecraft.text.MutableText;import net.minecraft.text.Text;public interface TranslationProvider{\n");
		JsonParser.parseString(FileUtils.readFileToString(path.resolve("src/main/resources/assets/mtr/lang/en_us.json").toFile(), StandardCharsets.UTF_8)).getAsJsonObject().entrySet().forEach(entry -> {
			final String key = entry.getKey();
			if (key.startsWith("block.") || key.startsWith("item.") || key.startsWith("entity.") || key.startsWith("itemGroup.")) {
				stringBuilder.append("@SuppressWarnings(\"unused\")");
			}
			stringBuilder.append(String.format("TranslationHolder %s=new TranslationHolder(\"%s\");\n", key.replace(".", "_").toUpperCase(Locale.ENGLISH), key));
		});
		stringBuilder.append("class TranslationHolder{public final String key;private TranslationHolder(String key){this.key=key;}\n");
		stringBuilder.append("public MutableText getMutableText(Object...arguments){return Text.translatable(key,arguments);}\n");
		stringBuilder.append("public Text getText(Object...arguments){return Text.translatable(key,arguments);}\n");
		stringBuilder.append("public String getString(Object...arguments){return getMutableText(arguments).getString();}\n");
		stringBuilder.append("public int width(Object...arguments){return MinecraftClient.getInstance().textRenderer.getWidth(getMutableText(arguments));}\n");
		stringBuilder.append("}}");
		FileUtils.write(path.resolve("src/main/java/org/mtr/generated/lang/TranslationProvider.java").toFile(), stringBuilder.toString(), StandardCharsets.UTF_8);
	}

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

	public void copyBuildFile() throws IOException {
		final Path directory = path.getParent().resolve("build/release");
		Files.createDirectories(directory);
		Files.copy(path.resolve(String.format("build/libs/mtr-%s-%s.jar", loader, version)), directory.resolve(String.format("MTR-%s-%s+%s.jar", loader, version, minecraftVersion)), StandardCopyOption.REPLACE_EXISTING);
	}

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
