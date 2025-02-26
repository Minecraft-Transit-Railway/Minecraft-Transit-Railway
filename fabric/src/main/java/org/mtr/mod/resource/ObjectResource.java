package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mod.config.Config;
import org.mtr.mod.generated.resource.ObjectResourceSchema;
import org.mtr.mod.render.DynamicVehicleModel;

import javax.annotation.Nullable;

public final class ObjectResource extends ObjectResourceSchema implements StoredModelResourceBase {

	public final boolean shouldPreload;
	private final CachedResource<ObjectObjectImmutablePair<OptimizedModelWrapper, DynamicVehicleModel>> cachedObjectResource;

	public ObjectResource(ReaderBase readerBase, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
		shouldPreload = Config.getClient().matchesPreloadResourcePattern(id);
		cachedObjectResource = new CachedResource<>(() -> load(modelResource, textureResource, flipTextureV, 0, resourceProvider), shouldPreload ? Integer.MAX_VALUE : VehicleModel.MODEL_LIFESPAN);
	}

	@Override
	@Nullable
	public OptimizedModelWrapper getOptimizedModel() {
		final ObjectObjectImmutablePair<OptimizedModelWrapper, DynamicVehicleModel> railResource = cachedObjectResource.getData(false);
		return railResource == null ? null : railResource.left();
	}

	@Override
	@Nullable
	public DynamicVehicleModel getDynamicVehicleModel() {
		final ObjectObjectImmutablePair<OptimizedModelWrapper, DynamicVehicleModel> railResource = cachedObjectResource.getData(false);
		return railResource == null ? null : railResource.right();
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

	@Override
	public void preload() {
		cachedObjectResource.getData(true);
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

	private static double clampNumber(double value) {
		return value <= 0 ? 1 : value;
	}
}
