package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.generated.resource.VehicleWrapperSchema;

public final class VehicleWrapper extends VehicleWrapperSchema {

	public VehicleWrapper(
			VehicleResource vehicleResource,
			ObjectArrayList<ModelPropertiesAndPositionDefinitionsWrapper> modelPropertiesAndPositionDefinitionsWrapperList,
			ObjectArrayList<ModelPropertiesAndPositionDefinitionsWrapper> bogie1ModelPropertiesAndPositionDefinitionsWrapperList,
			ObjectArrayList<ModelPropertiesAndPositionDefinitionsWrapper> bogie2ModelPropertiesAndPositionDefinitionsWrapperList
	) {
		super(vehicleResource);
		this.modelPropertiesAndPositionDefinitionsWrapperList.addAll(modelPropertiesAndPositionDefinitionsWrapperList);
		this.bogie1ModelPropertiesAndPositionDefinitionsWrapperList.addAll(bogie1ModelPropertiesAndPositionDefinitionsWrapperList);
		this.bogie2ModelPropertiesAndPositionDefinitionsWrapperList.addAll(bogie2ModelPropertiesAndPositionDefinitionsWrapperList);
	}

	public VehicleWrapper(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}
}
