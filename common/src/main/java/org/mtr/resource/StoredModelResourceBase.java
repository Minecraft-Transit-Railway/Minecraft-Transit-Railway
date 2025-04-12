package org.mtr.resource;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.mtr.client.CustomResourceLoader;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.model.NewOptimizedModel;
import org.mtr.model.NewOptimizedModelGroup;
import org.mtr.model.OptimizedModel;
import org.mtr.render.*;

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
			models = new ObjectObjectImmutablePair<>(materialGroups.get(PartCondition.NORMAL).build(VertexFormat.DrawMode.QUADS), tempDynamicVehicleModel);
		} else if (isObj) {
			final Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> objModels = new Object2ObjectOpenHashMap<>();
			final Object2ObjectAVLTreeMap<String, OptimizedModel.ObjModel> rawModels = OptimizedModel.ObjModel.loadModel(
					resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelResource, "obj")),
					mtlString -> resourceProvider.get(CustomResourceTools.getResourceFromSamePath(modelResource, mtlString, "mtl")),
					textureString -> StringUtils.isEmpty(textureString) ? textureId : CustomResourceTools.getResourceFromSamePath(modelResource, textureString, "png"),
					null, true, flipTextureV
			);
			transform(rawModels.values());
			final DynamicVehicleModel dynamicVehicleModel = new DynamicVehicleModel(
					rawModels,
					textureId,
					new ModelProperties(modelYOffset),
					new PositionDefinitions(),
					""
			);
			dynamicVehicleModel.writeFloorsAndDoorways(new ObjectArrayList<>(), new ObjectArrayList<>(), new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>(), objModels);
			models = new ObjectObjectImmutablePair<>(objModels.get(PartCondition.NORMAL).build(VertexFormat.DrawMode.TRIANGLES), dynamicVehicleModel);
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
			optimizedModel.forEach((renderStage, newOptimizedModels) -> newOptimizedModels.forEach(newOptimizedModel -> {
				final Identifier texture = newOptimizedModel.texture;
				final RenderLayer renderLayer = switch (renderStage.queuedRenderLayer) {
					case LIGHT -> MoreRenderLayers.getLight(texture, false);
					case LIGHT_TRANSLUCENT -> MoreRenderLayers.getLight(texture, true);
					case LIGHT_2 -> MoreRenderLayers.getLight2(texture);
					case INTERIOR -> MoreRenderLayers.getInterior(texture);
					case INTERIOR_TRANSLUCENT -> MoreRenderLayers.getInteriorTranslucent(texture);
					case EXTERIOR -> MoreRenderLayers.getExterior(texture);
					case EXTERIOR_TRANSLUCENT -> MoreRenderLayers.getExteriorTranslucent(texture);
					case LINES -> RenderLayer.getLines();
					default -> null;
				};
				MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (matrixStack, vertexConsumer, offset) -> {
					if (renderLayer != null) {
						renderLayer.startDrawing();
						storedMatrixTransformations.transform(matrixStack, offset);
						newOptimizedModel.render(matrixStack.peek().getPositionMatrix(), RenderSystem.getShader());
						matrixStack.pop();
						renderLayer.endDrawing();
					}
				});
			}));
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
