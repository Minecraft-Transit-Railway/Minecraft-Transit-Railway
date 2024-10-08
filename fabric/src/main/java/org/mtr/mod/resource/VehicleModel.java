package org.mtr.mod.resource;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.generated.resource.VehicleModelSchema;
import org.mtr.mod.render.DynamicVehicleModel;

import javax.annotation.Nullable;

public final class VehicleModel extends VehicleModelSchema {

	final CachedResource<DynamicVehicleModel> cachedModel;

	public static final int MODEL_LIFESPAN = 60000;

	public VehicleModel(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		final JsonReader[] modelPropertiesJsonReader = {null};
		CustomResourceLoader.readResource(CustomResourceTools.formatIdentifier(modelPropertiesResource, "json"), jsonElement -> modelPropertiesJsonReader[0] = new JsonReader(jsonElement));
		final JsonReader[] positionDefinitionsJsonReader = {null};
		CustomResourceLoader.readResource(CustomResourceTools.formatIdentifier(positionDefinitionsResource, "json"), jsonElement -> positionDefinitionsJsonReader[0] = new JsonReader(jsonElement));
		cachedModel = new CachedResource<>(() -> createModel(
				modelPropertiesJsonReader[0] == null ? null : new ModelProperties(modelPropertiesJsonReader[0]),
				positionDefinitionsJsonReader[0] == null ? null : new PositionDefinitions(positionDefinitionsJsonReader[0]),
				modelPropertiesResource
		), MODEL_LIFESPAN);
	}

	public VehicleModel(ReaderBase readerBase, @Nullable JsonReader modelPropertiesJsonReader, @Nullable JsonReader positionDefinitionsJsonReader, String id) {
		super(readerBase);
		updateData(readerBase);
		cachedModel = new CachedResource<>(() -> createModel(
				modelPropertiesJsonReader == null ? null : new ModelProperties(modelPropertiesJsonReader),
				positionDefinitionsJsonReader == null ? null : new PositionDefinitions(positionDefinitionsJsonReader),
				id
		), MODEL_LIFESPAN);
	}

	@Nullable
	private DynamicVehicleModel createModel(@Nullable ModelProperties modelProperties, @Nullable PositionDefinitions positionDefinitions, String id) {
		if (modelProperties == null || positionDefinitions == null) {
			return null;
		}

		final Identifier textureId = CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");
		final DynamicVehicleModel dynamicVehicleModel;

		if (modelResource.endsWith(".bbmodel")) {
			final BlockbenchModel[] blockbenchModel = {null};
			CustomResourceLoader.readResource(CustomResourceTools.formatIdentifier(modelResource, "bbmodel"), jsonElement -> blockbenchModel[0] = new BlockbenchModel(new JsonReader(jsonElement)));
			dynamicVehicleModel = blockbenchModel[0] == null ? null : new DynamicVehicleModel(blockbenchModel[0], textureId, modelProperties, positionDefinitions, id);
		} else if (modelResource.endsWith(".obj")) {
			dynamicVehicleModel = new DynamicVehicleModel(new Object2ObjectAVLTreeMap<>(OptimizedModel.ObjModel.loadModel(CustomResourceTools.formatIdentifierWithDefault(modelResource, "obj"), textureId, null, true, flipTextureV)), textureId, modelProperties, positionDefinitions, id);
		} else {
			dynamicVehicleModel = null;
		}

		return dynamicVehicleModel;
	}
}
