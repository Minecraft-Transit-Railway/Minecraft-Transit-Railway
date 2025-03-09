package org.mtr.model.render.obj;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.util.Identifier;
import org.mtr.model.render.model.RawMesh;
import org.mtr.resource.ResourceManagerHelper;

public final class AtlasManager {

	private final Object2ObjectOpenHashMap<Identifier, AtlasSprite> sprites = new Object2ObjectOpenHashMap<>();
	private final ObjectOpenHashSet<Identifier> noAtlasList = new ObjectOpenHashSet<>();

	public void load(Identifier atlasConfiguration) {
		final JsonObject atlasConfigurationObject = JsonParser.parseString(ResourceManagerHelper.readResource(atlasConfiguration)).getAsJsonObject();
		final String basePath = atlasConfigurationObject.get("basePath").getAsString();
		atlasConfigurationObject.get("sheets").getAsJsonArray().forEach(sheetObject -> {
			final Identifier sheetConfiguration = ObjModelLoader.resolveRelativePath(atlasConfiguration, sheetObject.getAsString(), ".json");
			final Identifier sheetTexture = ObjModelLoader.resolveRelativePath(atlasConfiguration, sheetObject.getAsString(), ".png");
			final JsonObject sheetConfigurationObject = JsonParser.parseString(ResourceManagerHelper.readResource(sheetConfiguration)).getAsJsonObject();
			final int sheetWidth = sheetConfigurationObject.get("meta").getAsJsonObject().get("size").getAsJsonObject().get("w").getAsInt();
			final int sheetHeight = sheetConfigurationObject.get("meta").getAsJsonObject().get("size").getAsJsonObject().get("h").getAsInt();
			sheetConfigurationObject.get("frames").getAsJsonObject().entrySet().forEach(entry -> {
				final Identifier texture = ObjModelLoader.resolveRelativePath(sheetConfiguration, basePath + entry.getKey(), ".png");
				final JsonObject spriteObject = entry.getValue().getAsJsonObject();
				sprites.put(texture, new AtlasSprite(
						sheetTexture, sheetWidth, sheetHeight,
						spriteObject.get("frame").getAsJsonObject().get("x").getAsInt(), spriteObject.get("frame").getAsJsonObject().get("y").getAsInt(),
						spriteObject.get("frame").getAsJsonObject().get("w").getAsInt(), spriteObject.get("frame").getAsJsonObject().get("h").getAsInt(),
						spriteObject.get("spriteSourceSize").getAsJsonObject().get("x").getAsInt(), spriteObject.get("spriteSourceSize").getAsJsonObject().get("y").getAsInt(),
						spriteObject.get("spriteSourceSize").getAsJsonObject().get("w").getAsInt(), spriteObject.get("spriteSourceSize").getAsJsonObject().get("h").getAsInt(),
						spriteObject.get("sourceSize").getAsJsonObject().get("w").getAsInt(), spriteObject.get("sourceSize").getAsJsonObject().get("h").getAsInt(),
						spriteObject.get("rotated").getAsBoolean()
				));
			});
		});
		atlasConfigurationObject.get("noAtlas").getAsJsonArray().forEach(noAtlasObject -> noAtlasList.add(ObjModelLoader.resolveRelativePath(atlasConfiguration, basePath + noAtlasObject.getAsString(), ".png")));
	}

	public void applyToMesh(RawMesh mesh) {
		if (noAtlasList.contains(mesh.materialProperties.getTexture())) {
			return;
		}
		final AtlasSprite sprite = sprites.get(mesh.materialProperties.getTexture());
		if (sprite != null) {
			sprite.applyToMesh(mesh);
		}
	}

	public void clear() {
		sprites.clear();
	}
}
