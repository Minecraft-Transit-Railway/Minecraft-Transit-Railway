package org.mtr.mod.resource;

import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.*;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.render.DynamicVehicleModel;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;

import javax.annotation.Nullable;

public interface StoredModelResourceBase {

	default ObjectObjectImmutablePair<OptimizedModelWrapper, DynamicVehicleModel> load(String modelResource, String textureResource, boolean flipTextureV, double modelYOffset) {
		CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.beginReload();

		final OptimizedModelWrapper[] tempOptimizedModel = {null};
		final DynamicVehicleModel[] tempDynamicVehicleModel = {null};
		final boolean isBlockbench = modelResource.endsWith(".bbmodel");
		final boolean isObj = modelResource.endsWith(".obj");
		final Identifier textureId = CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");
		final ObjectObjectImmutablePair<OptimizedModelWrapper, DynamicVehicleModel> models;

		if (isBlockbench) {
			CustomResourceLoader.readResource(CustomResourceTools.formatIdentifier(modelResource, "bbmodel"), jsonElement -> {
				final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper>> materialGroups = new Object2ObjectOpenHashMap<>();
				tempDynamicVehicleModel[0] = new DynamicVehicleModel(
						new BlockbenchModel(new JsonReader(jsonElement)),
						textureId,
						new ModelProperties(modelYOffset),
						new PositionDefinitions(),
						""
				);
				tempDynamicVehicleModel[0].writeFloorsAndDoorways(new ObjectArraySet<>(), new ObjectArraySet<>(), new Object2ObjectOpenHashMap<>(), materialGroups, new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>());
				tempOptimizedModel[0] = OptimizedModelWrapper.fromMaterialGroups(materialGroups.get(PartCondition.NORMAL));
			});
			models = new ObjectObjectImmutablePair<>(tempOptimizedModel[0], tempDynamicVehicleModel[0]);
		} else if (isObj) {
			final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> objModels = new Object2ObjectOpenHashMap<>();
			final Object2ObjectAVLTreeMap<String, OptimizedModel.ObjModel> rawModels = new Object2ObjectAVLTreeMap<>(OptimizedModel.ObjModel.loadModel(CustomResourceTools.formatIdentifierWithDefault(modelResource, "obj"), textureId, null, true, flipTextureV));
			transform(rawModels.values());
			final DynamicVehicleModel dynamicVehicleModel = new DynamicVehicleModel(
					rawModels,
					textureId,
					new ModelProperties(modelYOffset),
					new PositionDefinitions(),
					""
			);
			dynamicVehicleModel.writeFloorsAndDoorways(new ObjectArraySet<>(), new ObjectArraySet<>(), new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>(), objModels);
			models = new ObjectObjectImmutablePair<>(OptimizedModelWrapper.fromObjModels(objModels.get(PartCondition.NORMAL)), dynamicVehicleModel);
		} else {
			models = new ObjectObjectImmutablePair<>(null, null);
		}

		CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.finishReload();
		return models;
	}

	default void render(StoredMatrixTransformations storedMatrixTransformations, int light) {
		final OptimizedModelWrapper optimizedModel = getOptimizedModel();
		final DynamicVehicleModel dynamicVehicleModel = getDynamicVehicleModel();

		if (OptimizedRenderer.hasOptimizedRendering()) {
			if (optimizedModel != null) {
				MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> {
					storedMatrixTransformations.transform(graphicsHolder, offset);
					CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.queue(optimizedModel, graphicsHolder, light);
					graphicsHolder.pop();
				});
			}
		} else {
			if (dynamicVehicleModel != null) {
				dynamicVehicleModel.render(storedMatrixTransformations, null, light, new ObjectArrayList<>());
			}
		}
	}

	@Nullable
	OptimizedModelWrapper getOptimizedModel();

	@Nullable
	DynamicVehicleModel getDynamicVehicleModel();

	default void transform(ObjectCollection<OptimizedModel.ObjModel> values) {
	}
}
