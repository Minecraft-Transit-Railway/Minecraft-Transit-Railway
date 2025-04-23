package org.mtr.resource;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.mtr.MTR;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.resource.VehicleModelSchema;
import org.mtr.model.BlockbenchModelLoader;
import org.mtr.model.BuiltVehicleModelHolder;
import org.mtr.model.ModelLoaderBase;
import org.mtr.model.ObjModelLoader;

import java.util.function.Supplier;

public final class VehicleModel extends VehicleModelSchema {

	final Supplier<BuiltVehicleModelHolder> builtVehicleModelHolderSupplier;
	private final JsonReader modelPropertiesJsonReader;
	private final JsonReader positionDefinitionsJsonReader;
	private final ModelLoaderBase modelLoaderBase;

	public static final int MODEL_LIFESPAN = 60000;

	public VehicleModel(ReaderBase readerBase, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
		modelPropertiesJsonReader = new JsonReader(Utilities.parseJson(resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelPropertiesResource, "json"))));
		positionDefinitionsJsonReader = new JsonReader(Utilities.parseJson(resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(positionDefinitionsResource, "json"))));
		modelLoaderBase = getModelLoaderBase(modelResource, textureResource, resourceProvider, flipTextureV);
		builtVehicleModelHolderSupplier = createModelSupplier(modelPropertiesJsonReader, positionDefinitionsJsonReader);
	}

	public VehicleModel(ReaderBase readerBase, JsonReader modelPropertiesJsonReader, JsonReader positionDefinitionsJsonReader, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
		this.modelPropertiesJsonReader = modelPropertiesJsonReader;
		this.positionDefinitionsJsonReader = positionDefinitionsJsonReader;
		modelLoaderBase = getModelLoaderBase(modelResource, textureResource, resourceProvider, flipTextureV);
		builtVehicleModelHolderSupplier = createModelSupplier(modelPropertiesJsonReader, positionDefinitionsJsonReader);
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
		modelLoaderBase = getModelLoaderBase(modelResource, textureResource, resourceProvider, flipTextureV);
		builtVehicleModelHolderSupplier = createModelSupplier(modelPropertiesJsonReader, positionDefinitionsJsonReader);
	}

	public void reset() {
		modelLoaderBase.reset();
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

	private Supplier<BuiltVehicleModelHolder> createModelSupplier(JsonReader modelPropertiesJsonReader, JsonReader positionDefinitionsJsonReader) {
		final ModelProperties modelProperties = new ModelProperties(modelPropertiesJsonReader);
		final PositionDefinitions positionDefinitions = new PositionDefinitions(positionDefinitionsJsonReader);
		return () -> modelLoaderBase.get(modelProperties, positionDefinitions);
	}

	public static ModelLoaderBase getModelLoaderBase(String modelResource, String textureResource, ResourceProvider resourceProvider, boolean flipTextureV) {
		final Identifier texture = CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");
		final ModelLoaderBase modelLoaderBase;

		if (modelResource.endsWith(".bbmodel")) {
			final BlockbenchModelLoader blockbenchModelLoader = new BlockbenchModelLoader(texture);
			blockbenchModelLoader.loadModel(new BlockbenchModel(new JsonReader(Utilities.parseJson(resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelResource, "bbmodel"))))));
			modelLoaderBase = blockbenchModelLoader;
		} else if (modelResource.endsWith(".obj")) {
			final ObjModelLoader objModelLoader = new ObjModelLoader(texture);
			objModelLoader.loadModel(
					resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelResource, "obj")),
					mtlString -> resourceProvider.get(CustomResourceTools.getResourceFromSamePath(modelResource, mtlString, "mtl")),
					textureString -> StringUtils.isEmpty(textureString) ? texture : CustomResourceTools.getResourceFromSamePath(modelResource, textureString, "png"),
					true, flipTextureV
			);
			// TODO transform object if needed
			modelLoaderBase = objModelLoader;
		} else {
			MTR.LOGGER.error("[{}] Invalid model!", texture.toString());
			final BlockbenchModelLoader blockbenchModelLoader = new BlockbenchModelLoader(texture);
			blockbenchModelLoader.loadModel(new BlockbenchModel(new JsonReader(new JsonObject())));
			modelLoaderBase = blockbenchModelLoader;
		}

		return modelLoaderBase;
	}
}
