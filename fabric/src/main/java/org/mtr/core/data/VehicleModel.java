package org.mtr.core.data;

import org.mtr.core.generated.VehicleModelSchema;
import org.mtr.core.serializers.JsonReader;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.render.DynamicVehicleModel;

public final class VehicleModel extends VehicleModelSchema {

	final DynamicVehicleModel model;

	public VehicleModel(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		final BlockbenchModel[] blockbenchModel = {null};
		CustomResourceLoader.readResource(CustomResourceTools.formatIdentifier(modelResource, "bbmodel"), jsonElement -> blockbenchModel[0] = new BlockbenchModel(new JsonReader(jsonElement)));
		final ModelProperties[] modelProperties = {null};
		CustomResourceLoader.readResource(CustomResourceTools.formatIdentifier(modelPropertiesResource, "json"), jsonElement -> modelProperties[0] = new ModelProperties(new JsonReader(jsonElement)));
		final PositionDefinitions[] positionDefinitions = {null};
		CustomResourceLoader.readResource(CustomResourceTools.formatIdentifier(positionDefinitionsResource, "json"), jsonElement -> positionDefinitions[0] = new PositionDefinitions(new JsonReader(jsonElement)));
		model = blockbenchModel[0] == null || modelProperties[0] == null || positionDefinitions[0] == null ? null : new DynamicVehicleModel(blockbenchModel[0], CustomResourceTools.formatIdentifier(textureResource, "png"), modelProperties[0], positionDefinitions[0]);
	}
}
