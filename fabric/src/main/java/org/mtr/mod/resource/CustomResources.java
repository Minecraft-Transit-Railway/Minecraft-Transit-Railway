package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.generated.resource.CustomResourcesSchema;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public final class CustomResources extends CustomResourcesSchema {

	public CustomResources(ObjectArrayList<VehicleResource> vehicles, ObjectArrayList<SignResource> signs) {
		super(identifier -> "");
		this.vehicles.addAll(vehicles);
		this.signs.addAll(signs);
	}

	public CustomResources(ReaderBase readerBase, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
	}

	/**
	 * @deprecated for {@link ResourceWrapper} use only
	 */
	@Deprecated
	public CustomResources(ReaderBase readerBase) {
		super(readerBase, identifier -> "");
		updateData(readerBase);
	}

	@Nonnull
	@Override
	protected ResourceProvider vehiclesResourceProviderParameter() {
		return resourceProvider;
	}

	@Nonnull
	@Override
	protected ResourceProvider railsResourceProviderParameter() {
		return resourceProvider;
	}

	@Nonnull
	@Override
	protected ResourceProvider objectsResourceProviderParameter() {
		return resourceProvider;
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

	public void iterateObjects(Consumer<ObjectResource> consumer) {
		objects.forEach(consumer);
	}
}
