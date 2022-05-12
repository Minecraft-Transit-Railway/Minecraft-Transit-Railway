package mtr.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import mtr.MTR;
import mtr.data.IGui;
import mtr.model.ModelTrainBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ResourcePackCreatorProperties implements IResourcePackCreatorProperties, IGui {

	private String modelFileName = "";
	private JsonObject modelObject = new JsonObject();
	private DynamicTrainModel model;
	private String propertiesFileName = "";
	private JsonObject propertiesObject = new JsonObject();
	private String textureFileName = "";
	private ResourceLocation texture;

	public ResourcePackCreatorProperties() {
		propertiesObject.addProperty(KEY_PROPERTIES_DOOR_MAX, 14);
		propertiesObject.add(KEY_PROPERTIES_PARTS, new JsonArray());
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
			propertiesObject = jsonObject;
			if (!propertiesObject.has(KEY_PROPERTIES_DOOR_MAX)) {
				propertiesObject.addProperty(KEY_PROPERTIES_DOOR_MAX, 14);
			}
			if (!propertiesObject.has(KEY_PROPERTIES_PARTS)) {
				propertiesObject.add(KEY_PROPERTIES_PARTS, new JsonArray());
			}
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
		partsObject.add(KEY_PROPERTIES_WHITELISTED_CARS, new JsonArray());
		partsObject.add(KEY_PROPERTIES_BLACKLISTED_CARS, new JsonArray());

		propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS).add(partsObject);
		updateModel();
	}

	public void removePart(int index) {
		getPartFromIndex(index, propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS), partObject -> propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS).remove(index));
		updateModel();
	}

	public void editPartName(int index, String name) {
		getPartFromIndex(index, propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS), partObject -> partObject.addProperty(KEY_PROPERTIES_NAME, name));
		updateModel();
	}

	public void editPartRenderStage(int index, ModelTrainBase.RenderStage renderStage) {
		getPartFromIndex(index, propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS), partObject -> partObject.addProperty(KEY_PROPERTIES_STAGE, renderStage.toString()));
		updateModel();
	}

	public void editPartMirror(int index, boolean mirror) {
		getPartFromIndex(index, propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS), partObject -> partObject.addProperty(KEY_PROPERTIES_MIRROR, mirror));
		updateModel();
	}

	public void editPartSkipRenderingIfTooFar(int index, boolean mirror) {
		getPartFromIndex(index, propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS), partObject -> partObject.addProperty(KEY_PROPERTIES_SKIP_RENDERING_IF_TOO_FAR, mirror));
		updateModel();
	}

	public void editPartDoorOffset(int index, DoorOffset doorOffset) {
		getPartFromIndex(index, propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS), partObject -> partObject.addProperty(KEY_PROPERTIES_DOOR_OFFSET, doorOffset.toString()));
		updateModel();
	}

	public void editPartRenderCondition(int index, RenderCondition renderCondition) {
		getPartFromIndex(index, propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS), partObject -> partObject.addProperty(KEY_PROPERTIES_RENDER_CONDITION, renderCondition.toString()));
		updateModel();
	}

	public void editPartPositions(int index, String positions) {
		final JsonArray positionsArray = new JsonArray();
		final String[] positionsSplit = positions.replaceAll("[^\\d,]", "").split(",");
		for (int i = 1; i < positionsSplit.length; i += 2) {
			final JsonArray positionPairArray = new JsonArray();
			positionPairArray.add(positionsSplit[i - 1]);
			positionPairArray.add(positionsSplit[i]);
			positionsArray.add(positionPairArray);
		}
		getPartFromIndex(index, propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS), partObject -> partObject.add(KEY_PROPERTIES_POSITIONS, positionsArray));
		updateModel();
	}

	public void addPartWhitelistedCar(int index, int car) {
		getPartFromIndex(index, propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS), partObject -> {
			final JsonArray jsonArray = partObject.getAsJsonArray(KEY_PROPERTIES_WHITELISTED_CARS);
			jsonArrayCheckForDuplicates(jsonArray, jsonElement -> jsonElement.getAsInt() == car, () -> jsonArray.add(car));
		});
		updateModel();
	}

	public void removePartWhitelistedCar(int index, int car) {
		getPartFromIndex(index, propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS), partObject -> jsonArrayRemove(partObject.getAsJsonArray(KEY_PROPERTIES_WHITELISTED_CARS), jsonElement -> jsonElement.getAsInt() == car));
		updateModel();
	}

	public void addPartBlacklistedCar(int index, int car) {
		getPartFromIndex(index, propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS), partObject -> {
			final JsonArray jsonArray = partObject.getAsJsonArray(KEY_PROPERTIES_BLACKLISTED_CARS);
			jsonArrayCheckForDuplicates(jsonArray, jsonElement -> jsonElement.getAsInt() == car, () -> jsonArray.add(car));
		});
		updateModel();
	}

	public void removePartBlacklistedCar(int index, int car) {
		getPartFromIndex(index, propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS), partObject -> jsonArrayRemove(partObject.getAsJsonArray(KEY_PROPERTIES_BLACKLISTED_CARS), jsonElement -> jsonElement.getAsInt() == car));
		updateModel();
	}

	public void render(PoseStack matrices, int currentCar, int trainCars) {
		if (model != null) {
			final Minecraft minecraft = Minecraft.getInstance();
			final MultiBufferSource.BufferSource immediate = minecraft.renderBuffers().bufferSource();
			model.render(matrices, immediate, texture == null ? new ResourceLocation("mtr:textures/block/white.png") : texture, MAX_LIGHT_INTERIOR, 0, 0, true, currentCar, trainCars, true, true, false, true);
			immediate.endBatch();
		}
	}

	public JsonArray getModelPartsArray() {
		return modelObject.has(KEY_MODEL_OUTLINER) ? modelObject.getAsJsonArray(KEY_MODEL_OUTLINER) : new JsonArray();
	}

	public JsonArray getPropertiesPartsArray() {
		return propertiesObject.has(KEY_PROPERTIES_PARTS) ? propertiesObject.getAsJsonArray(KEY_PROPERTIES_PARTS) : new JsonArray();
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

	private static void getPartFromIndex(int index, JsonArray jsonArray, Consumer<JsonObject> callback) {
		if (index >= 0 && index < jsonArray.size()) {
			callback.accept(jsonArray.get(index).getAsJsonObject());
		}
	}

	private static void jsonArrayCheckForDuplicates(JsonArray jsonArray, Function<JsonElement, Boolean> equals, Runnable callback) {
		for (final JsonElement jsonElement : jsonArray) {
			if (equals.apply(jsonElement)) {
				return;
			}
		}
		callback.run();
	}

	private static void jsonArrayRemove(JsonArray jsonArray, Function<JsonElement, Boolean> equals) {
		final List<JsonElement> elementsToRemove = new ArrayList<>();
		for (final JsonElement jsonElement : jsonArray) {
			if (equals.apply(jsonElement)) {
				elementsToRemove.add(jsonElement);
			}
		}
		elementsToRemove.forEach(jsonArray::remove);
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
