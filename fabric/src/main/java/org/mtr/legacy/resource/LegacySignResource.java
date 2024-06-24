package org.mtr.legacy.resource;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.legacy.generated.resource.SignResourceSchema;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.resource.SignResource;

public final class LegacySignResource extends SignResourceSchema {

	public LegacySignResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public void convert(ObjectArrayList<SignResource> signResources, String id) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", "mtr_custom_sign_" + id);
		jsonObject.addProperty("textureResource", texture_id);
		jsonObject.addProperty("flipTexture", flip_texture);
		jsonObject.addProperty("customText", custom_text);
		jsonObject.addProperty("flipCustomText", flip_custom_text);
		jsonObject.addProperty("small", small);
		jsonObject.addProperty("backgroundColor", background_color);
		signResources.add(new SignResource(new JsonReader(jsonObject)));
	}
}
