package org.mtr.resource;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.config.Config;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.generated.resource.RailResourceSchema;
import org.mtr.model.NewOptimizedModel;

import javax.annotation.Nullable;

public final class RailResource extends RailResourceSchema implements StoredModelResourceBase {

	public final boolean shouldPreload;
	private final CachedResource<Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> cachedRailResource;

	public RailResource(ReaderBase readerBase, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
		shouldPreload = Config.getClient().matchesPreloadResourcePattern(id);
		cachedRailResource = new CachedResource<>(() -> load(modelResource, textureResource, flipTextureV, modelYOffset, resourceProvider), shouldPreload ? Integer.MAX_VALUE : VehicleModel.MODEL_LIFESPAN);
	}

	/**
	 * Used to create the default rail
	 */
	public RailResource(String id, String name, ResourceProvider resourceProvider) {
		super(id, name, "777777", "", "", false, 0, 0, resourceProvider);
		shouldPreload = false;
		cachedRailResource = new CachedResource<>(() -> null, Integer.MAX_VALUE);
	}

	@Override
	@Nullable
	public Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> getOptimizedModel() {
		return cachedRailResource.getData(false);
	}

	@Override
	public void preload() {
		cachedRailResource.getData(true);
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
