package org.mtr.mod.resource;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mod.generated.resource.ResourceWrapperSchema;

import java.util.function.Consumer;

public final class ResourceWrapper extends ResourceWrapperSchema {

	public ResourceWrapper(
			ObjectArrayList<VehicleResourceWrapper> vehicles,
			ObjectArrayList<ModelWrapper> modelResources,
			ObjectArrayList<String> textureResources,
			ObjectArrayList<MinecraftModelResource> minecraftModelResources,
			ObjectArrayList<String> minecraftTextureResources
	) {
		this.vehicles.addAll(vehicles);
		this.modelResources.addAll(modelResources);
		this.textureResources.addAll(textureResources);
		this.minecraftModelResources.addAll(minecraftModelResources);
		this.minecraftTextureResources.addAll(minecraftTextureResources);
	}

	public ResourceWrapper(JsonReader jsonReader, ObjectArrayList<MinecraftModelResource> minecraftModelResources, ObjectArrayList<String> minecraftTextureResources) {
		super(jsonReader);
		updateData(jsonReader);
		this.minecraftModelResources.clear();
		this.minecraftModelResources.addAll(minecraftModelResources);
		this.minecraftTextureResources.clear();
		this.minecraftTextureResources.addAll(minecraftTextureResources);
	}

	public void iterateVehicles(Consumer<VehicleResourceWrapper> consumer) {
		vehicles.forEach(consumer);
	}

	public void addModelResource(ModelWrapper modelResource) {
		modelResources.add(modelResource);
	}

	public void addTextureResource(String textureResource) {
		textureResources.add(textureResource);
	}

	public void updateMinecraftInfo() {
		isMinecraftPaused = MinecraftClient.getInstance().isPaused();
		exportDirectory = MinecraftClient.getInstance().getRunDirectoryMapped().getAbsolutePath().replace("\\", "/") + "/resourcepacks";
	}

	public void clean() {
		vehicles.forEach(VehicleResourceWrapper::clean);
	}

	public JsonObject flatten() {
		return Utilities.getJsonObjectFromData(new ResourceWrapper(vehicles, new ObjectArrayList<>(), new ObjectArrayList<>(), new ObjectArrayList<>(), new ObjectArrayList<>()));
	}
}
