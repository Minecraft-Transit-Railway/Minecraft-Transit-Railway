package org.mtr.mod.resource;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.generated.resource.RailResourceSchema;
import org.mtr.mod.render.DynamicVehicleModel;
import org.mtr.mod.render.RenderTrains;
import org.mtr.mod.render.RenderVehicles;
import org.mtr.mod.render.StoredMatrixTransformations;

import javax.annotation.Nullable;

public final class RailResource extends RailResourceSchema {

	@Nullable
	private final OptimizedModelWrapper optimizedModel;
	@Nullable
	private final DynamicVehicleModel dynamicVehicleModel;

	public RailResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		final OptimizedModelWrapper[] tempOptimizedModel = {null};
		final DynamicVehicleModel[] tempDynamicVehicleModel = {null};
		final boolean isBlockbench = modelResource.endsWith(".bbmodel");
		final boolean isObj = modelResource.endsWith(".obj");

		if (isBlockbench) {
			CustomResourceLoader.readResource(CustomResourceTools.formatIdentifier(modelResource, "bbmodel"), jsonElement -> {
				final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper>> materialGroups = new Object2ObjectOpenHashMap<>();
				tempDynamicVehicleModel[0] = new DynamicVehicleModel(
						new BlockbenchModel(new JsonReader(jsonElement)),
						CustomResourceTools.formatIdentifierWithDefault(textureResource, "png"),
						new ModelProperties(modelYOffset),
						new PositionDefinitions(),
						""
				);
				tempDynamicVehicleModel[0].writeFloorsAndDoorways(new ObjectArraySet<>(), new ObjectArraySet<>(), new Object2ObjectOpenHashMap<>(), materialGroups, new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>());
				tempOptimizedModel[0] = OptimizedModelWrapper.fromMaterialGroups(materialGroups.get(PartCondition.NORMAL));
			});
			optimizedModel = tempOptimizedModel[0];
			dynamicVehicleModel = tempDynamicVehicleModel[0];
		} else if (isObj) {
			final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> objModels = new Object2ObjectOpenHashMap<>();
			dynamicVehicleModel = new DynamicVehicleModel(
					new Object2ObjectAVLTreeMap<>(OptimizedModel.ObjModel.loadModel(CustomResourceTools.formatIdentifierWithDefault(modelResource, "obj"), null, true, flipTextureV)),
					CustomResourceTools.formatIdentifierWithDefault(textureResource, "png"),
					new ModelProperties(modelYOffset),
					new PositionDefinitions(),
					""
			);
			dynamicVehicleModel.writeFloorsAndDoorways(new ObjectArraySet<>(), new ObjectArraySet<>(), new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>(), objModels);
			optimizedModel = OptimizedModelWrapper.fromObjModels(objModels.get(PartCondition.NORMAL));
		} else {
			dynamicVehicleModel = null;
			optimizedModel = null;
		}
	}

	/**
	 * Used to create the default rail
	 */
	public RailResource(String id, String name) {
		super(id, name, "", "", "", false, 0, 0);
		optimizedModel = null;
		dynamicVehicleModel = null;
	}

	public void render(StoredMatrixTransformations storedMatrixTransformations, int light) {
		if (RenderVehicles.useOptimizedRendering()) {
			if (optimizedModel != null) {
				RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> {
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
