package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mod.generated.resource.RailResourceSchema;
import org.mtr.mod.render.DynamicVehicleModel;

import javax.annotation.Nullable;

public final class RailResource extends RailResourceSchema implements StoredModelResourceBase {

	@Nullable
	private final OptimizedModelWrapper optimizedModel;
	@Nullable
	private final DynamicVehicleModel dynamicVehicleModel;

	public RailResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		final ObjectObjectImmutablePair<OptimizedModelWrapper, DynamicVehicleModel> modelPair = load(modelResource, textureResource, flipTextureV, modelYOffset);
		optimizedModel = modelPair.left();
		dynamicVehicleModel = modelPair.right();
	}

	/**
	 * Used to create the default rail
	 */
	public RailResource(String id, String name) {
		super(id, name, "", "", "", false, 0, 0);
		optimizedModel = null;
		dynamicVehicleModel = null;
	}

	@Override
	@Nullable
	public OptimizedModelWrapper getOptimizedModel() {
		return optimizedModel;
	}

	@Override
	@Nullable
	public DynamicVehicleModel getDynamicVehicleModel() {
		return dynamicVehicleModel;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getColor() {
		return CustomResourceTools.colorStringToInt(color);
	}

	public double getRepeatInterval() {
		return repeatInterval;
	}

	public double getModelYOffset() {
		return modelYOffset;
	}

	public static String getIdWithoutDirection(String id) {
		return id.endsWith("_1") || id.endsWith("_2") ? id.substring(0, id.length() - 2) : id;
	}
}
