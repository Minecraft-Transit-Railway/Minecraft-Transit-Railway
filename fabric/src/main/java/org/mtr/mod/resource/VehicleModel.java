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

	@Nullable
	final DynamicVehicleModel model;

	public VehicleModel(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		final ModelProperties[] modelProperties = {null};
		CustomResourceLoader.readResource(CustomResourceTools.formatIdentifier(modelPropertiesResource, "json"), jsonElement -> modelProperties[0] = new ModelProperties(new JsonReader(jsonElement)));
		final PositionDefinitions[] positionDefinitions = {null};
		CustomResourceLoader.readResource(CustomResourceTools.formatIdentifier(positionDefinitionsResource, "json"), jsonElement -> positionDefinitions[0] = new PositionDefinitions(new JsonReader(jsonElement)));
		model = createModel(modelProperties[0], positionDefinitions[0], modelPropertiesResource);
	}

	public VehicleModel(ReaderBase readerBase, @Nullable ModelProperties modelProperties, @Nullable PositionDefinitions positionDefinitions, String id) {
		super(readerBase);
		updateData(readerBase);
		model = createModel(modelProperties, positionDefinitions, id);
	}

	@Nullable
	private DynamicVehicleModel createModel(@Nullable ModelProperties modelProperties, @Nullable PositionDefinitions positionDefinitions, String id) {
		if (modelProperties == null || positionDefinitions == null) {
			return null;
		}

		final Identifier textureId = CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");

		if (modelResource.endsWith(".bbmodel")) {
			final BlockbenchModel[] blockbenchModel = {null};
			CustomResourceLoader.readResource(CustomResourceTools.formatIdentifier(modelResource, "bbmodel"), jsonElement -> blockbenchModel[0] = new BlockbenchModel(new JsonReader(jsonElement)));
			return blockbenchModel[0] == null ? null : new DynamicVehicleModel(blockbenchModel[0], textureId, modelProperties, positionDefinitions, id);
		} else if (modelResource.endsWith(".obj")) {
			return new DynamicVehicleModel(new Object2ObjectAVLTreeMap<>(OptimizedModel.ObjModel.loadModel(CustomResourceTools.formatIdentifierWithDefault(modelResource, "obj"), textureId, null, true, flipTextureV)), textureId, modelProperties, positionDefinitions, id);
		} else {
			return null;
		}
	}
}
