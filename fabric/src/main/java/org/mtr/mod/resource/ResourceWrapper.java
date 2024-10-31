package org.mtr.mod.resource;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.generated.resource.ResourceWrapperSchema;

public final class ResourceWrapper extends ResourceWrapperSchema {

	public ResourceWrapper(CustomResources customResources, ObjectArrayList<BlockbenchModel> blockbenchModels, ObjectArrayList<String> textures) {
		super(customResources);
		this.models.addAll(blockbenchModels);
		this.textures.addAll(textures);
	}
}
