package org.mtr.mod.resource;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.generated.resource.VehicleModelSchema;
import org.mtr.mod.render.DynamicVehicleModel;
import org.mtr.mod.render.GpuObjCompat;
import org.mtr.mod.render.GpuObjDebugStats;

public final class VehicleModel extends VehicleModelSchema {

	boolean shouldPreload = false;
	final CachedResource<DynamicVehicleModel> cachedModel;
	final CachedResource<VehicleGpuCache> cachedGpuCache;
	private final JsonReader modelPropertiesJsonReader;
	private final JsonReader positionDefinitionsJsonReader;

	public static final int MODEL_LIFESPAN = 60000;

	public VehicleModel(ReaderBase readerBase, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
		modelPropertiesJsonReader = new JsonReader(Utilities.parseJson(resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelPropertiesResource, "json"))));
		positionDefinitionsJsonReader = new JsonReader(Utilities.parseJson(resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(positionDefinitionsResource, "json"))));
		cachedModel = new CachedResource<>(() -> createModel(new ModelProperties(modelPropertiesJsonReader), new PositionDefinitions(positionDefinitionsJsonReader), modelPropertiesResource), shouldPreload ? Integer.MAX_VALUE : MODEL_LIFESPAN);
		cachedGpuCache = new CachedResource<>(this::createGpuCache, shouldPreload ? Integer.MAX_VALUE : MODEL_LIFESPAN);
	}

	public VehicleModel(ReaderBase readerBase, JsonReader modelPropertiesJsonReader, JsonReader positionDefinitionsJsonReader, String id, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
		this.modelPropertiesJsonReader = modelPropertiesJsonReader;
		this.positionDefinitionsJsonReader = positionDefinitionsJsonReader;
		cachedModel = new CachedResource<>(() -> createModel(new ModelProperties(modelPropertiesJsonReader), new PositionDefinitions(positionDefinitionsJsonReader), id), shouldPreload ? Integer.MAX_VALUE : MODEL_LIFESPAN);
		cachedGpuCache = new CachedResource<>(this::createGpuCache, shouldPreload ? Integer.MAX_VALUE : MODEL_LIFESPAN);
	}

	VehicleModel(
			String modelResource,
			String textureResource,
			String modelPropertiesResource,
			String positionDefinitionsResource,
			boolean flipTextureV,
			ResourceProvider resourceProvider
	) {
		super(
				modelResource,
				textureResource,
				modelPropertiesResource,
				positionDefinitionsResource,
				flipTextureV,
				resourceProvider
		);
		modelPropertiesJsonReader = new JsonReader(Utilities.parseJson(resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelPropertiesResource, "json"))));
		positionDefinitionsJsonReader = new JsonReader(Utilities.parseJson(resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(positionDefinitionsResource, "json"))));
		cachedModel = new CachedResource<>(() -> createModel(new ModelProperties(modelPropertiesJsonReader), new PositionDefinitions(positionDefinitionsJsonReader), modelPropertiesResource), shouldPreload ? Integer.MAX_VALUE : MODEL_LIFESPAN);
		cachedGpuCache = new CachedResource<>(this::createGpuCache, shouldPreload ? Integer.MAX_VALUE : MODEL_LIFESPAN);
	}

	public MinecraftModelResource getAsMinecraftResource() {
		return new MinecraftModelResource(modelResource, modelPropertiesResource, positionDefinitionsResource);
	}

	public void addToTextureResource(ObjectArraySet<String> textureResources) {
		final ModelProperties modelProperties = new ModelProperties(modelPropertiesJsonReader);
		if (modelProperties.gangwayInnerSideTexture != null) {
			textureResources.add(modelProperties.gangwayInnerSideTexture.data.toString());
		}
		if (modelProperties.gangwayInnerTopTexture != null) {
			textureResources.add(modelProperties.gangwayInnerTopTexture.data.toString());
		}
		if (modelProperties.gangwayInnerBottomTexture != null) {
			textureResources.add(modelProperties.gangwayInnerBottomTexture.data.toString());
		}
		if (modelProperties.gangwayOuterSideTexture != null) {
			textureResources.add(modelProperties.gangwayOuterSideTexture.data.toString());
		}
		if (modelProperties.gangwayOuterTopTexture != null) {
			textureResources.add(modelProperties.gangwayOuterTopTexture.data.toString());
		}
		if (modelProperties.gangwayOuterBottomTexture != null) {
			textureResources.add(modelProperties.gangwayOuterBottomTexture.data.toString());
		}
		if (modelProperties.barrierInnerSideTexture != null) {
			textureResources.add(modelProperties.barrierInnerSideTexture.data.toString());
		}
		if (modelProperties.barrierInnerTopTexture != null) {
			textureResources.add(modelProperties.barrierInnerTopTexture.data.toString());
		}
		if (modelProperties.barrierInnerBottomTexture != null) {
			textureResources.add(modelProperties.barrierInnerBottomTexture.data.toString());
		}
		if (modelProperties.barrierOuterSideTexture != null) {
			textureResources.add(modelProperties.barrierOuterSideTexture.data.toString());
		}
		if (modelProperties.barrierOuterTopTexture != null) {
			textureResources.add(modelProperties.barrierOuterTopTexture.data.toString());
		}
		if (modelProperties.barrierOuterBottomTexture != null) {
			textureResources.add(modelProperties.barrierOuterBottomTexture.data.toString());
		}
		textureResources.add(textureResource);
	}

	VehicleModelWrapper toVehicleModelWrapper() {
		final ModelProperties modelProperties = new ModelProperties(modelPropertiesJsonReader);
		final PositionDefinitions positionDefinitions = new PositionDefinitions(positionDefinitionsJsonReader);
		final ObjectArrayList<ModelPropertiesPartWrapper> parts = new ObjectArrayList<>();
		modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.addToModelPropertiesPartWrapperMap(positionDefinitions, parts));
		return modelProperties.toVehicleModelWrapper(modelResource, textureResource, modelPropertiesResource, positionDefinitionsResource, flipTextureV, parts);
	}

	private DynamicVehicleModel createModel(ModelProperties modelProperties, PositionDefinitions positionDefinitions, String id) {
		final Identifier textureId = CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");

		if (modelResource.endsWith(".bbmodel")) {
			CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.beginReload();
			final DynamicVehicleModel dynamicVehicleModel = new DynamicVehicleModel(
					new BlockbenchModel(new JsonReader(Utilities.parseJson(resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelResource, "bbmodel"))))),
					textureId,
					modelProperties,
					positionDefinitions,
					id
			);
			CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.finishReload();
			return dynamicVehicleModel;
		} else if (ModelResourceLoader.isSupportedModelResource(modelResource)) {
			try {
				CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.beginReload();
				final DynamicVehicleModel dynamicVehicleModel = new DynamicVehicleModel(ModelResourceLoader.loadModel(modelResource, textureId, flipTextureV, resourceProvider), textureId, modelProperties, positionDefinitions, id);
				CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.finishReload();
				return dynamicVehicleModel;
			} catch (Exception e) {
				Init.LOGGER.error("[{}] Invalid model!", modelResource, e);
				CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.finishReload();
			}
		} else {
			Init.LOGGER.error("[{}] Invalid model!", textureId.data.toString());
		}

		CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.beginReload();
		final DynamicVehicleModel dynamicVehicleModel = new DynamicVehicleModel(
				new BlockbenchModel(new JsonReader(new JsonObject())),
				textureId,
				modelProperties,
				positionDefinitions,
				id
		);
		CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.finishReload();
		return dynamicVehicleModel;
	}

	private VehicleGpuCache createGpuCache() {
		if (!GpuObjCompat.isSupported()) {
			return VehicleGpuCache.EMPTY;
		}

		final ModelProperties modelProperties = new ModelProperties(modelPropertiesJsonReader);
		final PositionDefinitions positionDefinitions = new PositionDefinitions(positionDefinitionsJsonReader);
		final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<VehicleGpuCache.Part>> gpuPartsForCondition = new Object2ObjectOpenHashMap<>();
		final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<VehicleGpuCache.FallbackPart>> fallbackPartsForCondition = new Object2ObjectOpenHashMap<>();
		final Object2ObjectOpenHashMap<PartCondition, VehicleGpuCache.PlacementStats> placementStatsByCondition = new Object2ObjectOpenHashMap<>();
		final boolean modelIsObj = modelResource.endsWith(".obj");
		final Identifier textureId = CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");
		final GpuObjModelWrapper gpuObjModelWrapper = modelIsObj ? GpuObjModelRegistry.getOrCreate(modelResource, textureId, flipTextureV, resourceProvider) : null;
		final GpuObjDebugStats.VehicleFallbackReason modelFallbackReason = !modelIsObj ? GpuObjDebugStats.VehicleFallbackReason.MODEL_NOT_OBJ : gpuObjModelWrapper == null ? GpuObjDebugStats.VehicleFallbackReason.GPU_CACHE_UNAVAILABLE : null;
		modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.writeGpuCache(modelResource, textureId, flipTextureV, resourceProvider, gpuObjModelWrapper, positionDefinitions, gpuPartsForCondition, fallbackPartsForCondition, placementStatsByCondition, modelProperties.getModelYOffset(), modelFallbackReason));
		return new VehicleGpuCache(gpuPartsForCondition, fallbackPartsForCondition, placementStatsByCondition);
	}
}
