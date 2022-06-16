package mtr.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mtr.data.TransportMode;
import mtr.model.ModelTrainBase;

import java.util.ArrayList;
import java.util.List;

public interface IResourcePackCreatorProperties {

	String KEY_MODEL_OUTLINER = "outliner";

	String KEY_PROPERTIES_TRANSPORT_MODE = "transport_mode";
	String KEY_PROPERTIES_LENGTH = "length";
	String KEY_PROPERTIES_WIDTH = "width";
	String KEY_PROPERTIES_DOOR_MAX = "door_max";
	String KEY_PROPERTIES_PARTS = "parts";

	String KEY_PROPERTIES_NAME = "name";
	String KEY_PROPERTIES_STAGE = "stage";
	String KEY_PROPERTIES_MIRROR = "mirror";
	String KEY_PROPERTIES_SKIP_RENDERING_IF_TOO_FAR = "skip_rendering_if_too_far";
	String KEY_PROPERTIES_DOOR_OFFSET = "door_offset";
	String KEY_PROPERTIES_RENDER_CONDITION = "render_condition";
	String KEY_PROPERTIES_POSITIONS = "positions";

	String KEY_PROPERTIES_WHITELISTED_CARS = "whitelisted_cars";
	String KEY_PROPERTIES_BLACKLISTED_CARS = "blacklisted_cars";

	static void checkSchema(JsonObject jsonObject) {
		if (!jsonObject.has(KEY_PROPERTIES_TRANSPORT_MODE) || !jsonObject.get(KEY_PROPERTIES_TRANSPORT_MODE).isJsonPrimitive()) {
			jsonObject.addProperty(KEY_PROPERTIES_TRANSPORT_MODE, TransportMode.TRAIN.toString());
		}
		if (!jsonObject.has(KEY_PROPERTIES_LENGTH) || !jsonObject.get(KEY_PROPERTIES_LENGTH).isJsonPrimitive()) {
			jsonObject.addProperty(KEY_PROPERTIES_LENGTH, 24);
		}
		if (!jsonObject.has(KEY_PROPERTIES_WIDTH) || !jsonObject.get(KEY_PROPERTIES_WIDTH).isJsonPrimitive()) {
			jsonObject.addProperty(KEY_PROPERTIES_WIDTH, 2);
		}
		if (!jsonObject.has(KEY_PROPERTIES_DOOR_MAX) || !jsonObject.get(KEY_PROPERTIES_DOOR_MAX).isJsonPrimitive()) {
			jsonObject.addProperty(KEY_PROPERTIES_DOOR_MAX, 14);
		}
		if (!jsonObject.has(KEY_PROPERTIES_PARTS) || !jsonObject.get(KEY_PROPERTIES_PARTS).isJsonArray()) {
			jsonObject.add(KEY_PROPERTIES_PARTS, new JsonArray());
		}

		final List<JsonElement> partElementsToRemove = new ArrayList<>();
		jsonObject.getAsJsonArray(KEY_PROPERTIES_PARTS).forEach(partElement -> {
			if (partElement.isJsonObject()) {
				final JsonObject partObject = partElement.getAsJsonObject();

				if (!partObject.has(KEY_PROPERTIES_NAME) || !partObject.get(KEY_PROPERTIES_NAME).isJsonPrimitive()) {
					partObject.addProperty(KEY_PROPERTIES_NAME, "");
				}
				if (!partObject.has(KEY_PROPERTIES_STAGE) || !partObject.get(KEY_PROPERTIES_STAGE).isJsonPrimitive()) {
					partObject.addProperty(KEY_PROPERTIES_STAGE, ModelTrainBase.RenderStage.EXTERIOR.toString());
				}
				if (!partObject.has(KEY_PROPERTIES_MIRROR) || !partObject.get(KEY_PROPERTIES_MIRROR).isJsonPrimitive()) {
					partObject.addProperty(KEY_PROPERTIES_MIRROR, false);
				}
				if (!partObject.has(KEY_PROPERTIES_SKIP_RENDERING_IF_TOO_FAR) || !partObject.get(KEY_PROPERTIES_SKIP_RENDERING_IF_TOO_FAR).isJsonPrimitive()) {
					partObject.addProperty(KEY_PROPERTIES_SKIP_RENDERING_IF_TOO_FAR, false);
				}
				if (!partObject.has(KEY_PROPERTIES_DOOR_OFFSET) || !partObject.get(KEY_PROPERTIES_DOOR_OFFSET).isJsonPrimitive()) {
					partObject.addProperty(KEY_PROPERTIES_DOOR_OFFSET, ResourcePackCreatorProperties.DoorOffset.NONE.toString());
				}
				if (!partObject.has(KEY_PROPERTIES_RENDER_CONDITION) || !partObject.get(KEY_PROPERTIES_RENDER_CONDITION).isJsonPrimitive()) {
					partObject.addProperty(KEY_PROPERTIES_RENDER_CONDITION, ResourcePackCreatorProperties.RenderCondition.ALL.toString());
				}
				if (!partObject.has(KEY_PROPERTIES_POSITIONS) || !partObject.get(KEY_PROPERTIES_POSITIONS).isJsonArray()) {
					partObject.add(KEY_PROPERTIES_POSITIONS, new JsonArray());
				}
				if (!partObject.has(KEY_PROPERTIES_WHITELISTED_CARS) || !partObject.get(KEY_PROPERTIES_WHITELISTED_CARS).isJsonPrimitive()) {
					partObject.addProperty(KEY_PROPERTIES_WHITELISTED_CARS, "");
				}
				if (!partObject.has(KEY_PROPERTIES_BLACKLISTED_CARS) || !partObject.get(KEY_PROPERTIES_BLACKLISTED_CARS).isJsonPrimitive()) {
					partObject.addProperty(KEY_PROPERTIES_BLACKLISTED_CARS, "");
				}

				final List<JsonElement> positionElementsToRemove = new ArrayList<>();
				partObject.getAsJsonArray(KEY_PROPERTIES_POSITIONS).forEach(positionElement -> {
					if (positionElement.isJsonArray()) {
						final JsonArray positionArray = positionElement.getAsJsonArray();
						while (positionArray.size() < 2) {
							positionArray.add(0);
						}
					} else {
						positionElementsToRemove.add(positionElement);
					}
				});

				positionElementsToRemove.forEach(partObject.getAsJsonArray(KEY_PROPERTIES_POSITIONS)::remove);
			} else {
				partElementsToRemove.add(partElement);
			}
		});

		partElementsToRemove.forEach(jsonObject.getAsJsonArray(KEY_PROPERTIES_PARTS)::remove);
	}
}
