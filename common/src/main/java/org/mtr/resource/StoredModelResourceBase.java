package org.mtr.resource;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.mtr.client.CustomResourceLoader;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.model.NewOptimizedModel;
import org.mtr.model.NewOptimizedModelGroup;
import org.mtr.model.OptimizedModel;
import org.mtr.render.DynamicVehicleModel;
import org.mtr.render.MainRenderer;
import org.mtr.render.QueuedRenderLayer;
import org.mtr.render.StoredMatrixTransformations;

import javax.annotation.Nullable;

public interface StoredModelResourceBase {

	default ObjectObjectImmutablePair<Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>, DynamicVehicleModel> load(String modelResource, String textureResource, boolean flipTextureV, double modelYOffset, ResourceProvider resourceProvider) {
		CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.beginReload();

		final boolean isBlockbench = modelResource.endsWith(".bbmodel");
		final boolean isObj = modelResource.endsWith(".obj");
		final Identifier textureId = CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");
		final ObjectObjectImmutablePair<Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>, DynamicVehicleModel> models;

		if (isBlockbench) {
			final Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> materialGroups = new Object2ObjectOpenHashMap<>();
			final DynamicVehicleModel tempDynamicVehicleModel = new DynamicVehicleModel(
					new BlockbenchModel(new JsonReader(Utilities.parseJson(resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelResource, "bbmodel"))))),
					textureId,
					new ModelProperties(modelYOffset),
					new PositionDefinitions(),
					""
			);
			tempDynamicVehicleModel.writeFloorsAndDoorways(new ObjectArrayList<>(), new ObjectArrayList<>(), new Object2ObjectOpenHashMap<>(), materialGroups, new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>());
			models = new ObjectObjectImmutablePair<>(materialGroups.get(PartCondition.NORMAL).build(), tempDynamicVehicleModel);
		} else if (isObj) {
			final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> objModels = new Object2ObjectOpenHashMap<>();
			final Object2ObjectAVLTreeMap<String, OptimizedModel.ObjModel> rawModels = new Object2ObjectAVLTreeMap<>(OptimizedModel.ObjModel.loadModel(
					resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelResource, "obj")),
					mtlString -> resourceProvider.get(CustomResourceTools.getResourceFromSamePath(modelResource, mtlString, "mtl")),
					textureString -> StringUtils.isEmpty(textureString) ? textureId : CustomResourceTools.getResourceFromSamePath(modelResource, textureString, "png"),
					null, true, flipTextureV
			));
			transform(rawModels.values());
			final DynamicVehicleModel dynamicVehicleModel = new DynamicVehicleModel(
					rawModels,
					textureId,
					new ModelProperties(modelYOffset),
					new PositionDefinitions(),
					""
			);
			dynamicVehicleModel.writeFloorsAndDoorways(new ObjectArrayList<>(), new ObjectArrayList<>(), new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>(), objModels);
			models = new ObjectObjectImmutablePair<>(null, null);
			// TODO
//			models = new ObjectObjectImmutablePair<>(OptimizedModelWrapper.fromObjModels(objModels.get(PartCondition.NORMAL)), dynamicVehicleModel);
		} else {
			models = new ObjectObjectImmutablePair<>(null, null);
		}

		CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.finishReload();
		return models;
	}

	default void render(StoredMatrixTransformations storedMatrixTransformations, int light) {
		final Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> optimizedModel = getOptimizedModel();
		final DynamicVehicleModel dynamicVehicleModel = getDynamicVehicleModel();

		if (optimizedModel != null) {
			MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
//				CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.queue(optimizedModel, matrixStack, light);
				optimizedModel.forEach((renderStage, newOptimizedModel) -> {

				});
				matrixStack.pop();
			});
		}
	}

	@Nullable
	Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> getOptimizedModel();

	@Nullable
	DynamicVehicleModel getDynamicVehicleModel();

	void preload();

	default void transform(ObjectCollection<OptimizedModel.ObjModel> values) {
	}
}
