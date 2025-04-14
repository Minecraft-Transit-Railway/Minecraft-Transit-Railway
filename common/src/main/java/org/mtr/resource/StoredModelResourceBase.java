package org.mtr.resource;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.model.NewOptimizedModel;
import org.mtr.model.NewOptimizedModelGroup;
import org.mtr.model.ObjModelLoader;
import org.mtr.render.DynamicVehicleModel;
import org.mtr.render.MainRenderer;
import org.mtr.render.StoredMatrixTransformations;

import javax.annotation.Nullable;

public interface StoredModelResourceBase {

	default Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> load(String modelResource, String textureResource, boolean flipTextureV, double modelYOffset, ResourceProvider resourceProvider) {
		final boolean isBlockbench = modelResource.endsWith(".bbmodel");
		final boolean isObj = modelResource.endsWith(".obj");
		final Identifier textureId = CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");
		final Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> models;

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
			models = materialGroups.get(PartCondition.NORMAL).build(VertexFormat.DrawMode.QUADS);
		} else if (isObj) {
			final Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> objModels = new Object2ObjectOpenHashMap<>();
			final Object2ObjectOpenHashMap<String, NewOptimizedModelGroup> rawModels = ObjModelLoader.loadModel(
					resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelResource, "obj")),
					mtlString -> resourceProvider.get(CustomResourceTools.getResourceFromSamePath(modelResource, mtlString, "mtl")),
					textureString -> StringUtils.isEmpty(textureString) ? textureId : CustomResourceTools.getResourceFromSamePath(modelResource, textureString, "png"),
					true, flipTextureV
			);
			// TODO transform(rawModels.values());
			final DynamicVehicleModel dynamicVehicleModel = new DynamicVehicleModel(
					rawModels,
					textureId,
					new ModelProperties(modelYOffset),
					new PositionDefinitions(),
					""
			);
			dynamicVehicleModel.writeFloorsAndDoorways(new ObjectArrayList<>(), new ObjectArrayList<>(), new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>(), objModels);
			models = objModels.get(PartCondition.NORMAL).build(VertexFormat.DrawMode.TRIANGLES);
		} else {
			models = null;
		}

		return models;
	}

	default void render(StoredMatrixTransformations storedMatrixTransformations, int light) {
		final Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> models = getOptimizedModel();
		if (models != null) {
			MainRenderer.renderModel(models, storedMatrixTransformations, light);
		}
	}

	@Nullable
	Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> getOptimizedModel();

	void preload();
}
