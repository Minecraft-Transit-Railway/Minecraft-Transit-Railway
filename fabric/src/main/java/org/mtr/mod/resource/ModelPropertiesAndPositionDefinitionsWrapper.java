package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.mod.generated.resource.ModelPropertiesAndPositionDefinitionsWrapperSchema;

public final class ModelPropertiesAndPositionDefinitionsWrapper extends ModelPropertiesAndPositionDefinitionsWrapperSchema {

	public ModelPropertiesAndPositionDefinitionsWrapper(ModelProperties modelProperties, PositionDefinitions positionDefinitions) {
		super(modelProperties, positionDefinitions);
	}

	public ModelPropertiesAndPositionDefinitionsWrapper(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}
}
