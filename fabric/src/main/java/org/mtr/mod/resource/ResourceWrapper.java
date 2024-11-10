package org.mtr.mod.resource;

import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.generated.resource.ResourceWrapperSchema;

import java.util.function.Consumer;

public final class ResourceWrapper extends ResourceWrapperSchema {

	public ResourceWrapper(
			ObjectArrayList<VehicleWrapper> vehicles,
			ObjectArrayList<ModelWrapper> modelResources,
			ObjectArrayList<String> textureResources,
			ObjectArrayList<MinecraftResource> minecraftResources
	) {
		this.vehicles.addAll(vehicles);
		this.modelResources.addAll(modelResources);
		this.textureResources.addAll(textureResources);
		this.minecraftResources.addAll(minecraftResources);
	}

	public ResourceWrapper(JsonReader jsonReader, ObjectArrayList<MinecraftResource> minecraftResources) {
		super(jsonReader);
		updateData(jsonReader);
		this.minecraftResources.clear();
		this.minecraftResources.addAll(minecraftResources);
	}

	public void iterateModelResources(Consumer<ModelWrapper> consumer) {
		modelResources.forEach(consumer);
	}

	public void iterateTextureResources(Consumer<String> consumer) {
		textureResources.forEach(consumer);
	}

	public void addModelResource(ModelWrapper modelResource) {
		modelResources.add(modelResource);
	}

	public void addTextureResource(String textureResource) {
		textureResources.add(textureResource);
	}
}
