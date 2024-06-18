package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mod.generated.resource.ObjectResourceSchema;
import org.mtr.mod.render.DynamicVehicleModel;

import javax.annotation.Nullable;

public final class ObjectResource extends ObjectResourceSchema implements StoredModelResourceBase {

	@Nullable
	private final OptimizedModelWrapper optimizedModel;
	@Nullable
	private final DynamicVehicleModel dynamicVehicleModel;

	public ObjectResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		final ObjectObjectImmutablePair<OptimizedModelWrapper, DynamicVehicleModel> modelPair = load(modelResource, textureResource, flipTextureV, 0);
		optimizedModel = modelPair.left();
		dynamicVehicleModel = modelPair.right();
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

	@Override
	public void transform(ObjectCollection<OptimizedModel.ObjModel> values) {
		values.forEach(objModel -> {
			objModel.applyTranslation(translation.getX(), translation.getY(), translation.getZ());
			objModel.applyRotation(rotation.getX(), rotation.getY(), rotation.getZ());
			objModel.applyScale(clampNumber(scale.getX()), clampNumber(scale.getY()), clampNumber(scale.getZ()));
			objModel.applyMirror(mirror.getX(), mirror.getY(), mirror.getZ());
		});
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	private static double clampNumber(double value) {
		return value <= 0 ? 1 : value;
	}
}
