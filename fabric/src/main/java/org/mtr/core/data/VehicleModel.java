package org.mtr.core.data;

import org.mtr.core.generated.VehicleModelSchema;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.render.DynamicVehicleModel;

import javax.annotation.Nullable;

public final class VehicleModel extends VehicleModelSchema {

	public final DynamicVehicleModel model;

	public VehicleModel(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		final BlockbenchModel[] blockbenchModel = {null};
		ResourceManagerHelper.readResource(formatIdentifier(modelResource, "bbmodel"), inputStream -> blockbenchModel[0] = new BlockbenchModel(CustomResourceLoader.readResource(inputStream)));
		final ModelProperties[] modelProperties = {null};
		ResourceManagerHelper.readResource(formatIdentifier(modelPropertiesResource, "json"), inputStream -> modelProperties[0] = new ModelProperties(CustomResourceLoader.readResource(inputStream)));
		model = blockbenchModel[0] == null || modelProperties[0] == null ? null : new DynamicVehicleModel(blockbenchModel[0], formatIdentifier(textureResource, "png"), modelProperties[0]);
	}

	@Nullable
	static Identifier formatIdentifier(String identifierString, String extension) {
		if (identifierString.isEmpty()) {
			return null;
		} else if (identifierString.endsWith("." + extension)) {
			return new Identifier(identifierString);
		} else {
			return new Identifier(String.format("%s.%s", identifierString, extension));
		}
	}
}
