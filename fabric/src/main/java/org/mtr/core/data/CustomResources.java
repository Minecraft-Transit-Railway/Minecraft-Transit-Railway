package org.mtr.core.data;

import org.mtr.core.generated.CustomResourcesSchema;
import org.mtr.core.serializers.ReaderBase;

import java.util.function.Consumer;

public final class CustomResources extends CustomResourcesSchema {

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
}
