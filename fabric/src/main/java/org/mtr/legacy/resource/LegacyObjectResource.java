package org.mtr.legacy.resource;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.legacy.generated.resource.ObjectResourceSchema;
import org.mtr.libraries.com.google.gson.JsonArray;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mod.resource.ObjectResource;
import org.mtr.mod.resource.ResourceProvider;

public final class LegacyObjectResource extends ObjectResourceSchema {

	public LegacyObjectResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public ObjectResource convert(String id, ResourceProvider resourceProvider) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", id);
		jsonObject.addProperty("name", name);
		jsonObject.addProperty("color", 0);
		jsonObject.addProperty("modelResource", model);
		jsonObject.addProperty("textureResource", textureId);
		jsonObject.addProperty("flipTextureV", flipV);
		final JsonObject baseObject = Utilities.getJsonObjectFromData(this);
		putArray1(baseObject, jsonObject, "translation");
		putArray1(baseObject, jsonObject, "rotation");
		putArray1(baseObject, jsonObject, "scale");
		putArray2(baseObject, jsonObject, "mirror");
		return new ObjectResource(new JsonReader(jsonObject), resourceProvider);
	}

	private static void putArray1(JsonObject baseObject, JsonObject newObject, String key) {
		try {
			final JsonArray jsonArray = baseObject.getAsJsonArray(key);
			final JsonObject childObject = new JsonObject();
			childObject.addProperty("x", jsonArray.get(0).getAsDouble());
			childObject.addProperty("y", jsonArray.get(1).getAsDouble());
			childObject.addProperty("z", jsonArray.get(2).getAsDouble());
			newObject.add(key, childObject);
		} catch (Exception ignored) {
		}
	}

	private static void putArray2(JsonObject baseObject, JsonObject newObject, String key) {
		try {
			final JsonArray jsonArray = baseObject.getAsJsonArray(key);
			final JsonObject childObject = new JsonObject();
			childObject.addProperty("x", jsonArray.get(0).getAsBoolean());
			childObject.addProperty("y", jsonArray.get(1).getAsBoolean());
			childObject.addProperty("z", jsonArray.get(2).getAsBoolean());
			newObject.add(key, childObject);
		} catch (Exception ignored) {
		}
	}
}
