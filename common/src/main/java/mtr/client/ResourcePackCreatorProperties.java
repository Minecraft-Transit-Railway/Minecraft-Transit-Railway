package mtr.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import mtr.MTR;
import mtr.data.EnumHelper;
import mtr.data.IGui;
import mtr.data.TransportMode;
import mtr.model.ModelTrainBase;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ResourcePackCreatorProperties implements IResourcePackCreatorProperties, ICustomResources, IGui {

	private String customTrainId = "my_custom_train_id";
	private String modelFileName = "";
	private JsonObject modelObject = new JsonObject();
	private DynamicTrainModel model;
	private String propertiesFileName = "";
	private JsonObject propertiesObject = new JsonObject();
	private String textureFileName = "";
	private Path textureFilePath;
	private ResourceLocation texture;
	private final JsonObject customResourcesObject = new JsonObject();

	public ResourcePackCreatorProperties() {
		IResourcePackCreatorProperties.checkSchema(propertiesObject);
		ICustomResources.createCustomTrainSchema(customResourcesObject, customTrainId, "My Custom Train Name", "000000", "", "", 0);
	}

	public void loadModelFile(Path path) {
		readJson(path, (fileName, jsonObject) -> {
			modelFileName = fileName;
			jsonObject.remove("textures");
			modelObject = jsonObject;
			updateModel();
		});
	}

	public void loadPropertiesFile(Path path) {
		readJson(path, (fileName, jsonObject) -> {
			propertiesFileName = fileName;
			DynamicTrainModelLegacy.migrateOldSchema(jsonObject);
			IResourcePackCreatorProperties.checkSchema(jsonObject);
			propertiesObject = jsonObject;
			updateModel();
		});
	}

	public void loadTextureFile(Path path) {
		final Minecraft minecraft = Minecraft.getInstance();
		try {
			final NativeImage nativeImage = NativeImage.read(Files.newInputStream(path, StandardOpenOption.READ));
			texture = minecraft.getTextureManager().register(MTR.MOD_ID, new DynamicTexture(nativeImage));
			textureFileName = path.getFileName().toString();
			textureFilePath = path;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editCustomResourcesId(String id) {
		final String name = getCustomTrainObject().get(CUSTOM_TRAINS_NAME).getAsString();
		final String color = getCustomTrainObject().get(CUSTOM_TRAINS_COLOR).getAsString();
		final String gangwayConnectionId = getCustomTrainObject().get(CUSTOM_TRAINS_GANGWAY_CONNECTION_ID).getAsString();
		final String trainBarrierId = getCustomTrainObject().get(CUSTOM_TRAINS_GANGWAY_CONNECTION_ID).getAsString();
		final float riderOffset = getCustomTrainObject().get(CUSTOM_TRAINS_RIDER_OFFSET).getAsFloat();
		customTrainId = id;
		ICustomResources.createCustomTrainSchema(customResourcesObject, id, name, color, gangwayConnectionId, trainBarrierId, riderOffset);
	}

	public void editCustomResourcesName(String name) {
		getCustomTrainObject().addProperty(CUSTOM_TRAINS_NAME, name);
	}

	public void editCustomResourcesColor(int color) {
		getCustomTrainObject().addProperty(CUSTOM_TRAINS_COLOR, Integer.toHexString(color & RGB_WHITE).toUpperCase());
	}

	public void editCustomResourcesGangwayConnectionId(String gangwayConnectionId) {
		getCustomTrainObject().addProperty(CUSTOM_TRAINS_GANGWAY_CONNECTION_ID, gangwayConnectionId);
	}

	public void editCustomResourcesTrainBarrierId(String trainBarrierId) {
		getCustomTrainObject().addProperty(CUSTOM_TRAINS_TRAIN_BARRIER_ID, trainBarrierId);
	}

	public void editCustomResourcesRiderOffset(float riderOffset) {
		getCustomTrainObject().addProperty(CUSTOM_TRAINS_RIDER_OFFSET, riderOffset);
	}

	public void editTransportMode() {
		cycleEnumProperty(propertiesObject, KEY_PROPERTIES_TRANSPORT_MODE, TransportMode.TRAIN, TransportMode.values());
		updateModel();
	}

	public void editLength(int length) {
		propertiesObject.addProperty(KEY_PROPERTIES_LENGTH, length);
		updateModel();
	}

	public void editWidth(int width) {
		propertiesObject.addProperty(KEY_PROPERTIES_WIDTH, width);
		updateModel();
	}

	public void editDoorMax(int doorMax) {
		propertiesObject.addProperty(KEY_PROPERTIES_DOOR_MAX, doorMax);
		updateModel();
	}

	public void addPart(String partName) {
		final JsonObject partsObject = new JsonObject();
		partsObject.addProperty(KEY_PROPERTIES_NAME, partName);
		partsObject.addProperty(KEY_PROPERTIES_STAGE, ModelTrainBase.RenderStage.EXTERIOR.toString());
		partsObject.addProperty(KEY_PROPERTIES_MIRROR, false);
		partsObject.addProperty(KEY_PROPERTIES_SKIP_RENDERING_IF_TOO_FAR, false);
		partsObject.addProperty(KEY_PROPERTIES_DOOR_OFFSET, DoorOffset.NONE.toString());
		partsObject.addProperty(KEY_PROPERTIES_RENDER_CONDITION, RenderCondition.ALL.toString());
		final JsonArray positionsArray = new JsonArray();
		final JsonArray positionPairArray = new JsonArray();
		positionPairArray.add(0);
		positionPairArray.add(0);
		positionsArray.add(positionPairArray);
		partsObject.add(KEY_PROPERTIES_POSITIONS, positionsArray);
		partsObject.addProperty(KEY_PROPERTIES_WHITELISTED_CARS, "");
		partsObject.addProperty(KEY_PROPERTIES_BLACKLISTED_CARS, "");

		propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS).add(partsObject);
		updateModel();
	}

	public void removePart(int index) {
		getPartFromIndex(index, partObject -> propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS).remove(index));
		updateModel();
	}

	public void editPartRenderStage(int index) {
		getPartFromIndex(index, partObject -> cycleEnumProperty(partObject, KEY_PROPERTIES_STAGE, ModelTrainBase.RenderStage.EXTERIOR, ModelTrainBase.RenderStage.values()));
		updateModel();
	}

	public void editPartMirror(int index, boolean mirror) {
		getPartFromIndex(index, partObject -> partObject.addProperty(KEY_PROPERTIES_MIRROR, mirror));
		updateModel();
	}

	public void editPartSkipRenderingIfTooFar(int index, boolean mirror) {
		getPartFromIndex(index, partObject -> partObject.addProperty(KEY_PROPERTIES_SKIP_RENDERING_IF_TOO_FAR, mirror));
		updateModel();
	}

	public void editPartDoorOffset(int index) {
		getPartFromIndex(index, partObject -> cycleEnumProperty(partObject, KEY_PROPERTIES_DOOR_OFFSET, DoorOffset.NONE, DoorOffset.values()));
		updateModel();
	}

	public void editPartRenderCondition(int index) {
		getPartFromIndex(index, partObject -> cycleEnumProperty(partObject, KEY_PROPERTIES_RENDER_CONDITION, RenderCondition.ALL, RenderCondition.values()));
		updateModel();
	}

	public void editPartPositions(int index, String positions) {
		final JsonArray positionsArray = new JsonArray();
		final String[] positionsSplit = positions.replaceAll("[^\\d.,\\-]", "").split(",");
		for (int i = 1; i < positionsSplit.length; i += 2) {
			try {
				final float x = Float.parseFloat(positionsSplit[i - 1]);
				final float z = Float.parseFloat(positionsSplit[i]);
				final JsonArray positionPairArray = new JsonArray();
				positionPairArray.add(x);
				positionPairArray.add(z);
				positionsArray.add(positionPairArray);
			} catch (Exception ignored) {
			}
		}
		getPartFromIndex(index, partObject -> partObject.add(KEY_PROPERTIES_POSITIONS, positionsArray));
		updateModel();
	}

	public void editPartWhitelistedCars(int index, String whitelistedCars) {
		getPartFromIndex(index, partObject -> partObject.addProperty(KEY_PROPERTIES_WHITELISTED_CARS, formatCarExpression(whitelistedCars)));
		updateModel();
	}

	public void editPartBlacklistedCars(int index, String blacklistedCars) {
		getPartFromIndex(index, partObject -> partObject.addProperty(KEY_PROPERTIES_BLACKLISTED_CARS, formatCarExpression(blacklistedCars)));
		updateModel();
	}

	public void render(PoseStack matrices, int currentCar, int trainCars, boolean head1IsFront, float leftDoorValue, float rightDoorValue, boolean opening, int light) {
		if (model != null) {
			final Minecraft minecraft = Minecraft.getInstance();
			final MultiBufferSource.BufferSource immediate = minecraft.renderBuffers().bufferSource();
			model.render(matrices, immediate, texture == null ? new ResourceLocation("mtr:textures/block/white.png") : texture, light, leftDoorValue, rightDoorValue, opening, currentCar, trainCars, head1IsFront, true, false, true);
			immediate.endBatch();
		}
	}

	public JsonObject getCustomTrainObject() {
		return customResourcesObject.getAsJsonObject(CUSTOM_TRAINS_KEY).getAsJsonObject(customTrainId);
	}

	public JsonArray getModelPartsArray() {
		return modelObject.has(KEY_MODEL_OUTLINER) ? modelObject.getAsJsonArray(KEY_MODEL_OUTLINER) : new JsonArray();
	}

	public String getTransportMode() {
		return propertiesObject.get(KEY_PROPERTIES_TRANSPORT_MODE).getAsString();
	}

	public int getLength() {
		return propertiesObject.get(KEY_PROPERTIES_LENGTH).getAsInt();
	}

	public int getWidth() {
		return propertiesObject.get(KEY_PROPERTIES_WIDTH).getAsInt();
	}

	public int getDoorMax() {
		return propertiesObject.get(KEY_PROPERTIES_DOOR_MAX).getAsInt();
	}

	public JsonArray getPropertiesPartsArray() {
		return propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS);
	}

	public String getCustomTrainId() {
		return customTrainId;
	}

	public String getModelFileName() {
		return modelFileName;
	}

	public String getPropertiesFileName() {
		return propertiesFileName;
	}

	public String getTextureFileName() {
		return textureFileName;
	}

	public void export() {
		try {
			final Minecraft minecraft = Minecraft.getInstance();
			final File resourcePackDirectory = minecraft.getResourcePackDirectory();
			final FileOutputStream fileOutputStream = new FileOutputStream(String.format("%s/%s_%s.zip", resourcePackDirectory.toString(), customTrainId, ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("uuuu_MM_dd_HH_mm_ss"))));
			final ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

			writeToZip(zipOutputStream, "pack.mcmeta", "{\"pack\":{\"pack_format\":6,\"description\":\"Minecraft Transit Railway\\n" + getCustomTrainObject().get(CUSTOM_TRAINS_NAME).getAsString() + "\"}}");
			writeToZip(zipOutputStream, String.format("assets/mtr/%s.json", CUSTOM_RESOURCES_ID), customResourcesObject.toString());
			writeToZip(zipOutputStream, String.format("assets/mtr/%s/%s.bbmodel", customTrainId, customTrainId), modelObject.toString());
			writeToZip(zipOutputStream, String.format("assets/mtr/%s/%s.json", customTrainId, customTrainId), propertiesObject.toString());
			writeToZip(zipOutputStream, String.format("assets/mtr/%s/%s.png", customTrainId, customTrainId), Files.newInputStream(textureFilePath));

			zipOutputStream.close();
			fileOutputStream.close();

			Util.getPlatform().openFile(resourcePackDirectory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeToZip(ZipOutputStream zipOutputStream, String fileName, String content) throws IOException {
		writeToZip(zipOutputStream, fileName, new ByteArrayInputStream(content.getBytes()));
	}

	private void writeToZip(ZipOutputStream zipOutputStream, String fileName, InputStream inputStream) throws IOException {
		zipOutputStream.putNextEntry(new ZipEntry(fileName));
		final byte[] bytes = new byte[1024];
		int length;
		while ((length = inputStream.read(bytes)) >= 0) {
			zipOutputStream.write(bytes, 0, length);
		}
		inputStream.close();
	}

	private void updateModel() {
		try {
			model = new DynamicTrainModel(modelObject, propertiesObject);
		} catch (Exception ignored) {
			model = null;
		}
	}

	private void getPartFromIndex(int index, Consumer<JsonObject> callback) {
		final JsonArray jsonArray = propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS);
		if (index >= 0 && index < jsonArray.size()) {
			callback.accept(jsonArray.get(index).getAsJsonObject());
		}
	}

	private static <T extends Enum<T>> void cycleEnumProperty(JsonObject jsonObject, String key, T defaultValue, T[] enumValues) {
		final T enumValue = EnumHelper.valueOf(defaultValue, jsonObject.get(key).getAsString());
		jsonObject.addProperty(key, enumValues[(enumValue.ordinal() + 1) % enumValues.length].toString());
	}

	private static void readJson(Path path, BiConsumer<String, JsonObject> jsonCallback) {
		try {
			jsonCallback.accept(path.getFileName().toString(), new JsonParser().parse(String.join("", Files.readAllLines(path))).getAsJsonObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String formatCarExpression(String expression) {
		final Matcher matcher = Pattern.compile("%\\d+(\\+\\d+)*|-?\\d+").matcher(expression);
		final StringBuilder result = new StringBuilder();
		while (matcher.find()) {
			result.append(matcher.group()).append(",");
		}
		final String resultString = result.toString();
		return resultString.endsWith(",") ? resultString.substring(0, resultString.length() - 1) : resultString;
	}

	public enum DoorOffset {NONE, LEFT_POSITIVE, RIGHT_POSITIVE, LEFT_NEGATIVE, RIGHT_NEGATIVE}

	public enum RenderCondition {ALL, DOORS_OPEN, DOORS_CLOSED, DOOR_LEFT_OPEN, DOOR_RIGHT_OPEN, DOOR_LEFT_CLOSED, DOOR_RIGHT_CLOSED, MOVING_FORWARDS, MOVING_BACKWARDS}
}
