package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.generated.resource.CustomResourcesSchema;

import java.util.function.Consumer;

public final class CustomResources extends CustomResourcesSchema {

	public CustomResources(ObjectArrayList<VehicleResource> vehicles, ObjectArrayList<SignResource> signs) {
		this.vehicles.addAll(vehicles);
		this.signs.addAll(signs);
	}

	public CustomResources(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public void iterateVehicles(Consumer<VehicleResource> consumer) {
		vehicles.forEach(consumer);
	}

	public void iterateSigns(Consumer<SignResource> consumer) {
		signs.forEach(consumer);
	}

	public void iterateRails(Consumer<RailResource> consumer) {
		rails.forEach(consumer);
	}
}
