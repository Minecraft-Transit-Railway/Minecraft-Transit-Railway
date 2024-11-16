package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.mod.generated.resource.MinecraftModelResourceSchema;

public final class MinecraftModelResource extends MinecraftModelResourceSchema {

	public MinecraftModelResource(String modelResource, String modelPropertiesResource, String positionDefinitionsResource) {
		super(modelResource, modelPropertiesResource, positionDefinitionsResource);
	}

	public MinecraftModelResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public boolean matchesModelResource(String modelResource) {
		return this.modelResource.equals(modelResource);
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof MinecraftModelResource) {
			final MinecraftModelResource minecraftModelResource = (MinecraftModelResource) object;
			return modelResource.equals(minecraftModelResource.modelResource) && modelPropertiesResource.equals(minecraftModelResource.modelPropertiesResource) && positionDefinitionsResource.equals(minecraftModelResource.positionDefinitionsResource);
		} else {
			return false;
		}
	}
}
