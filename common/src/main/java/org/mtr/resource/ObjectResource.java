package org.mtr.resource;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.client.IDrawing;
import org.mtr.config.Config;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.generated.resource.ObjectResourceSchema;
import org.mtr.model.NewOptimizedModel;
import org.mtr.render.StoredMatrixTransformations;

import javax.annotation.Nullable;

public final class ObjectResource extends ObjectResourceSchema implements StoredModelResourceBase {

	public final boolean shouldPreload;
	private final CachedResource<Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> cachedObjectResource;

	public ObjectResource(ReaderBase readerBase, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
		shouldPreload = Config.getClient().matchesPreloadResourcePattern(id);
		cachedObjectResource = new CachedResource<>(() -> load(modelResource, textureResource, flipTextureV, 0, resourceProvider), shouldPreload ? Integer.MAX_VALUE : VehicleModel.MODEL_LIFESPAN);
	}

	@Override
	@Nullable
	public Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> getOptimizedModel() {
		return cachedObjectResource.getData(false);
	}

	@Override
	public void render(StoredMatrixTransformations storedMatrixTransformations, int light) {
		final StoredMatrixTransformations newStoredMatrixTransformations = storedMatrixTransformations.copy();
		newStoredMatrixTransformations.add(matrixStack -> {
					matrixStack.translate(translation.getX(), translation.getY(), translation.getZ());
					IDrawing.rotateXDegrees(matrixStack, (float) rotation.getX());
					IDrawing.rotateYDegrees(matrixStack, (float) rotation.getY());
					IDrawing.rotateZDegrees(matrixStack, (float) rotation.getZ());
					matrixStack.scale(clampNumber(scale.getX()), clampNumber(scale.getY()), clampNumber(scale.getZ()));
//					matrixStack.mirror(mirror.getX(), mirror.getY(), mirror.getZ());
				}
		);
		StoredModelResourceBase.super.render(newStoredMatrixTransformations, light);
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

	private static float clampNumber(double value) {
		return value <= 0 ? 1 : (float) value;
	}
}
