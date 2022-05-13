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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ResourcePackCreatorProperties implements IResourcePackCreatorProperties, IGui {

	private String modelFileName = "";
	private JsonObject modelObject = new JsonObject();
	private DynamicTrainModel model;
	private String propertiesFileName = "";
	private JsonObject propertiesObject = new JsonObject();
	private String textureFileName = "";
	private ResourceLocation texture;

	public ResourcePackCreatorProperties() {
		IResourcePackCreatorProperties.checkSchema(propertiesObject);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		final String[] positionsSplit = positions.replaceAll("[^\\d.,]", "").split(",");
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
		getPartFromIndex(index, partObject -> partObject.addProperty(KEY_PROPERTIES_WHITELISTED_CARS, whitelistedCars));
		updateModel();
	}

	public void editPartBlacklistedCars(int index, String blacklistedCars) {
		getPartFromIndex(index, partObject -> partObject.addProperty(KEY_PROPERTIES_BLACKLISTED_CARS, blacklistedCars));
		updateModel();
	}

	public void render(PoseStack matrices, int currentCar, int trainCars, int light) {
		if (model != null) {
			final Minecraft minecraft = Minecraft.getInstance();
			final MultiBufferSource.BufferSource immediate = minecraft.renderBuffers().bufferSource();
			model.render(matrices, immediate, texture == null ? new ResourceLocation("mtr:textures/block/white.png") : texture, light, 0, 0, true, currentCar, trainCars, true, true, false, true);
			immediate.endBatch();
		}
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

	public String getModelFileName() {
		return modelFileName;
	}

	public String getPropertiesFileName() {
		return propertiesFileName;
	}

	public String getTextureFileName() {
		return textureFileName;
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

	public enum DoorOffset {NONE, LEFT_POSITIVE, RIGHT_POSITIVE, LEFT_NEGATIVE, RIGHT_NEGATIVE}

	public enum RenderCondition {ALL, DOORS_OPEN, DOORS_CLOSED, DOOR_LEFT_OPEN, DOOR_RIGHT_OPEN, DOOR_LEFT_CLOSED, DOOR_RIGHT_CLOSED, MOVING_FORWARDS, MOVING_BACKWARDS}
}
