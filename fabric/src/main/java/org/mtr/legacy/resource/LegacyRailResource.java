package org.mtr.legacy.resource;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.legacy.generated.resource.RailResourceSchema;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mod.resource.RailResource;

public final class LegacyRailResource extends RailResourceSchema {

	public LegacyRailResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public RailResource convert(String id) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", id);
		jsonObject.addProperty("name", name);
		jsonObject.addProperty("color", 0);
		jsonObject.addProperty("modelResource", model);
		jsonObject.addProperty("textureResource", textureId);
		jsonObject.addProperty("flipTextureV", flipV);
		jsonObject.addProperty("repeatInterval", repeatInterval);
		jsonObject.addProperty("modelYOffset", yOffset);
		return new RailResource(new JsonReader(jsonObject));
	}
}
