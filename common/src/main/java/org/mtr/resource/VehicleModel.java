package org.mtr.resource;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.mtr.MTR;
import org.mtr.client.CustomResourceLoader;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.resource.VehicleModelSchema;
import org.mtr.model.OptimizedModel;
import org.mtr.render.DynamicVehicleModel;

public final class VehicleModel extends VehicleModelSchema {

	boolean shouldPreload = false;
	final CachedResource<DynamicVehicleModel> cachedModel;
	private final JsonReader modelPropertiesJsonReader;
	private final JsonReader positionDefinitionsJsonReader;

	public static final int MODEL_LIFESPAN = 60000;

	public VehicleModel(ReaderBase readerBase, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
		modelPropertiesJsonReader = new JsonReader(Utilities.parseJson(resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelPropertiesResource, "json"))));
		positionDefinitionsJsonReader = new JsonReader(Utilities.parseJson(resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(positionDefinitionsResource, "json"))));
		cachedModel = new CachedResource<>(() -> createModel(new ModelProperties(modelPropertiesJsonReader), new PositionDefinitions(positionDefinitionsJsonReader), modelPropertiesResource), shouldPreload ? Integer.MAX_VALUE : MODEL_LIFESPAN);
	}

	public VehicleModel(ReaderBase readerBase, JsonReader modelPropertiesJsonReader, JsonReader positionDefinitionsJsonReader, String id, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
		this.modelPropertiesJsonReader = modelPropertiesJsonReader;
		this.positionDefinitionsJsonReader = positionDefinitionsJsonReader;
		cachedModel = new CachedResource<>(() -> createModel(new ModelProperties(modelPropertiesJsonReader), new PositionDefinitions(positionDefinitionsJsonReader), id), shouldPreload ? Integer.MAX_VALUE : MODEL_LIFESPAN);
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
		cachedModel.getData(true);
	}

	public MinecraftModelResource getAsMinecraftResource() {
		return new MinecraftModelResource(modelResource, modelPropertiesResource, positionDefinitionsResource);
	}

	public void addToTextureResource(ObjectArraySet<String> textureResources) {
		final ModelProperties modelProperties = new ModelProperties(modelPropertiesJsonReader);
		if (modelProperties.gangwayInnerSideTexture != null) {
			textureResources.add(modelProperties.gangwayInnerSideTexture.toString());
		}
		if (modelProperties.gangwayInnerTopTexture != null) {
			textureResources.add(modelProperties.gangwayInnerTopTexture.toString());
		}
		if (modelProperties.gangwayInnerBottomTexture != null) {
			textureResources.add(modelProperties.gangwayInnerBottomTexture.toString());
		}
		if (modelProperties.gangwayOuterSideTexture != null) {
			textureResources.add(modelProperties.gangwayOuterSideTexture.toString());
		}
		if (modelProperties.gangwayOuterTopTexture != null) {
			textureResources.add(modelProperties.gangwayOuterTopTexture.toString());
		}
		if (modelProperties.gangwayOuterBottomTexture != null) {
			textureResources.add(modelProperties.gangwayOuterBottomTexture.toString());
		}
		if (modelProperties.barrierInnerSideTexture != null) {
			textureResources.add(modelProperties.barrierInnerSideTexture.toString());
		}
		if (modelProperties.barrierInnerTopTexture != null) {
			textureResources.add(modelProperties.barrierInnerTopTexture.toString());
		}
		if (modelProperties.barrierInnerBottomTexture != null) {
			textureResources.add(modelProperties.barrierInnerBottomTexture.toString());
		}
		if (modelProperties.barrierOuterSideTexture != null) {
			textureResources.add(modelProperties.barrierOuterSideTexture.toString());
		}
		if (modelProperties.barrierOuterTopTexture != null) {
			textureResources.add(modelProperties.barrierOuterTopTexture.toString());
		}
		if (modelProperties.barrierOuterBottomTexture != null) {
			textureResources.add(modelProperties.barrierOuterBottomTexture.toString());
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
		} else if (modelResource.endsWith(".obj")) {
			CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.beginReload();
			final DynamicVehicleModel dynamicVehicleModel = new DynamicVehicleModel(new Object2ObjectAVLTreeMap<>(OptimizedModel.ObjModel.loadModel(
					resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelResource, "obj")),
					mtlString -> resourceProvider.get(CustomResourceTools.getResourceFromSamePath(modelResource, mtlString, "mtl")),
					textureString -> StringUtils.isEmpty(textureString) ? textureId : CustomResourceTools.getResourceFromSamePath(modelResource, textureString, "png"),
					null, true, flipTextureV
			)), textureId, modelProperties, positionDefinitions, id);
			CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.finishReload();
			return dynamicVehicleModel;
		} else {
			MTR.LOGGER.error("[{}] Invalid model!", textureId.toString());
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
	}
}
