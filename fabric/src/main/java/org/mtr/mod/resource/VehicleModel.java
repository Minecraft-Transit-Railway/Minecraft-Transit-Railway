package org.mtr.mod.resource;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.ReaderBase;
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
		model = createModel(modelProperties[0], positionDefinitions[0]);
	}

	public VehicleModel(ReaderBase readerBase, @Nullable ModelProperties modelProperties, @Nullable PositionDefinitions positionDefinitions) {
		super(readerBase);
		updateData(readerBase);
		model = createModel(modelProperties, positionDefinitions);
	}

	private DynamicVehicleModel createModel(@Nullable ModelProperties modelProperties, @Nullable PositionDefinitions positionDefinitions) {
		final BlockbenchModel[] blockbenchModel = {null};
		CustomResourceLoader.readResource(CustomResourceTools.formatIdentifier(modelResource, "bbmodel"), jsonElement -> blockbenchModel[0] = new BlockbenchModel(new JsonReader(jsonElement)));
		return blockbenchModel[0] == null || modelProperties == null || positionDefinitions == null ? null : new DynamicVehicleModel(blockbenchModel[0], CustomResourceTools.formatIdentifierWithDefault(textureResource, "png"), modelProperties, positionDefinitions);
	}
}
