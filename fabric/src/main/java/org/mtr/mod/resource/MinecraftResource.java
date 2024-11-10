package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.mod.generated.resource.MinecraftResourceSchema;

public final class MinecraftResource extends MinecraftResourceSchema {

	public MinecraftResource(String modelResource, String modelPropertiesResource, String textureResource) {
		super(modelResource, modelPropertiesResource, textureResource);
	}

	public MinecraftResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof MinecraftResource) {
			final MinecraftResource minecraftResource = (MinecraftResource) object;
			return modelResource.equals(minecraftResource.modelResource) && modelPropertiesResource.equals(minecraftResource.modelPropertiesResource) && textureResource.equals(minecraftResource.textureResource);
		} else {
			return false;
		}
	}
}
