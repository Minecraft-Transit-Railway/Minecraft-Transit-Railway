package org.mtr.render;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.mtr.MTR;
import org.mtr.client.CustomResourceLoader;
import org.mtr.core.data.Data;
import org.mtr.data.VehicleExtension;
import org.mtr.model.MutableBox;
import org.mtr.model.NewOptimizedModelGroup;
import org.mtr.model.OptimizedModel;
import org.mtr.resource.*;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public final class DynamicVehicleModel {

	public final ModelProperties modelProperties;
	private final Identifier texture;
	private final ObjectArraySet<Box> floors = new ObjectArraySet<>();
	private final ObjectArraySet<Box> doorways = new ObjectArraySet<>();
	private final Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> materialGroupsForPartConditionAndRenderStage = new Object2ObjectOpenHashMap<>();
	private final Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> materialGroupsForPartConditionAndRenderStageDoorsClosed = new Object2ObjectOpenHashMap<>();
	private final Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>>> objModelsForPartConditionAndRenderStage = new Object2ObjectOpenHashMap<>();
	private final Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>>> objModelsForPartConditionAndRenderStageDoorsClosed = new Object2ObjectOpenHashMap<>();

	public DynamicVehicleModel(BlockbenchModel blockbenchModel, Identifier texture, ModelProperties modelProperties, PositionDefinitions positionDefinitions, String id) {
		final Object2ObjectOpenHashMap<String, BlockbenchElement> uuidToBlockbenchElement = new Object2ObjectOpenHashMap<>();
		blockbenchModel.getElements().forEach(blockbenchElement -> uuidToBlockbenchElement.put(blockbenchElement.getUuid(), blockbenchElement));

		final Object2ObjectOpenHashMap<String, ObjectObjectImmutablePair<ModelPart, MutableBox>> nameToPart = new Object2ObjectOpenHashMap<>();
		final Object2ObjectOpenHashMap<String, ObjectArrayList<ModelDisplayPart>> nameToDisplayParts = new Object2ObjectOpenHashMap<>();
		blockbenchModel.getOutlines().forEach(blockbenchOutline -> {
			final ModelPartData modelPartData = new ModelData().getRoot();
			final MutableBox mutableBox = new MutableBox();
			final ObjectArrayList<ModelDisplayPart> modelDisplayParts = new ObjectArrayList<>();

			iterateChildren(blockbenchOutline, null, id, new GroupTransformations(), (uuid, groupTransformations) -> {
				final BlockbenchElement blockbenchElement = uuidToBlockbenchElement.remove(uuid);
				if (blockbenchElement != null) {
					final ModelDisplayPart modelDisplayPart = new ModelDisplayPart();
					modelDisplayParts.add(modelDisplayPart);
					mutableBox.add(blockbenchElement.setModelPart(modelPartData.addChild(MTR.randomString()), groupTransformations, modelDisplayPart, (float) modelProperties.getModelYOffset()));
				}
			});

			nameToPart.put(blockbenchOutline.getName(), new ObjectObjectImmutablePair<>(modelPartData.createPart(blockbenchModel.getTextureWidth(), blockbenchModel.getTextureHeight()), mutableBox));

			if (!modelDisplayParts.isEmpty()) {
				nameToDisplayParts.put(blockbenchOutline.getName(), modelDisplayParts);
			}
		});

		modelProperties.addPartsIfEmpty(nameToPart.keySet());
		this.texture = texture;
		this.modelProperties = modelProperties;
		modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.writeCache(texture, nameToPart, nameToDisplayParts, positionDefinitions, floors, doorways, materialGroupsForPartConditionAndRenderStage, materialGroupsForPartConditionAndRenderStageDoorsClosed));
		testDoors(id);
	}

	public DynamicVehicleModel(Object2ObjectAVLTreeMap<String, OptimizedModel.ObjModel> nameToObjModels, Identifier texture, ModelProperties modelProperties, PositionDefinitions positionDefinitions, String id) {
		modelProperties.addPartsIfEmpty(nameToObjModels.keySet());
		this.texture = texture;
		this.modelProperties = modelProperties;
		modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.writeCache(nameToObjModels, positionDefinitions, objModelsForPartConditionAndRenderStage, objModelsForPartConditionAndRenderStageDoorsClosed, modelProperties.getModelYOffset()));
		testDoors(id);
	}

	public void render(StoredMatrixTransformations storedMatrixTransformations, @Nullable VehicleExtension vehicle, int carNumber, int[] scrollingDisplayIndexTracker, int light, ObjectArrayList<Box> openDoorways, boolean fromResourcePackCreator) {
		modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.render(texture, storedMatrixTransformations, vehicle, carNumber, scrollingDisplayIndexTracker, light, openDoorways, fromResourcePackCreator));
	}

	public void writeFloorsAndDoorways(
			ObjectArrayList<Box> floors,
			ObjectArrayList<Box> doorways,
			Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> materialGroupsForPartCondition,
			Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> materialGroupsForPartConditionDoorsClosed,
			Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> objModelsForPartCondition,
			Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> objModelsForPartConditionDoorsClosed
	) {
		floors.addAll(this.floors);
		doorways.addAll(this.doorways);

		materialGroupsForPartConditionAndRenderStage.forEach((partCondition, newOptimizedModelGroup) -> {
			final NewOptimizedModelGroup existingNewOptimizedModelGroup = materialGroupsForPartCondition.get(partCondition);
			if (existingNewOptimizedModelGroup == null) {
				materialGroupsForPartCondition.put(partCondition, newOptimizedModelGroup);
			} else {
				existingNewOptimizedModelGroup.merge(newOptimizedModelGroup);
			}
		});
		materialGroupsForPartConditionAndRenderStageDoorsClosed.forEach((partCondition, newOptimizedModelGroup) -> {
			final NewOptimizedModelGroup existingNewOptimizedModelGroup = materialGroupsForPartConditionDoorsClosed.get(partCondition);
			if (existingNewOptimizedModelGroup == null) {
				materialGroupsForPartConditionDoorsClosed.put(partCondition, newOptimizedModelGroup);
			} else {
				existingNewOptimizedModelGroup.merge(newOptimizedModelGroup);
			}
		});
		objModelsForPartConditionAndRenderStage.forEach((partCondition, objModelsForRenderStage) -> Data.put(objModelsForPartCondition, partCondition, flattenCollection(objModelsForRenderStage.values()), ObjectArrayList::new));
		objModelsForPartConditionAndRenderStageDoorsClosed.forEach((partCondition, objModelsForRenderStage) -> Data.put(objModelsForPartConditionDoorsClosed, partCondition, flattenCollection(objModelsForRenderStage.values()), ObjectArrayList::new));

		materialGroupsForPartConditionAndRenderStage.clear();
		materialGroupsForPartConditionAndRenderStageDoorsClosed.clear();
		objModelsForPartConditionAndRenderStage.clear();
		objModelsForPartConditionAndRenderStageDoorsClosed.clear();
	}

	/**
	 * Simulate door movement to see if doors overlap (meaning that door X and Z multipliers were set incorrectly)
	 */
	private void testDoors(String id) {
		final long startTime = System.nanoTime();
		final ObjectArrayList<ObjectArrayList<Box>> boxesList = new ObjectArrayList<>();
		final int slices = 5;
		for (int i = 0; i <= slices; i++) {
			final ObjectArrayList<Box> boxes = new ObjectArrayList<>();
			final double time = (double) i / slices;
			modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.getOpenDoorBounds(boxes, time));
			boxesList.add(boxes);
		}

		final int count = boxesList.get(0).size();
		for (int i = 0; i < count; i++) {
			for (int j = i + 1; j < count; j++) {
				if (!boxesList.get(0).get(i).intersects(boxesList.get(0).get(j))) {
					for (int k = 1; k <= slices; k++) {
						if (boxesList.get(k).get(i).intersects(boxesList.get(k).get(j))) {
							MTR.LOGGER.warn("Vehicle doors overlapping! Door X and Z multipliers were probably set incorrectly ({})", id);
							return;
						}
					}
				}
			}
		}

		CustomResourceLoader.incrementTestDuration(System.nanoTime() - startTime);
	}

	private static void iterateChildren(BlockbenchOutline blockbenchOutline, @Nullable BlockbenchOutline previousBlockbenchOutline, String id, GroupTransformations groupTransformations, BiConsumer<String, GroupTransformations> consumer) {
		final GroupTransformations newGroupTransformations = blockbenchOutline.add(groupTransformations, previousBlockbenchOutline, id);
		blockbenchOutline.childrenUuid.forEach(uuid -> consumer.accept(uuid, newGroupTransformations));
		blockbenchOutline.getChildren().forEach(childOutline -> iterateChildren(childOutline, blockbenchOutline, id, groupTransformations, consumer));
	}

	private static <T> ObjectArrayList<T> flattenCollection(ObjectCollection<? extends ObjectCollection<T>> collection) {
		final ObjectArrayList<T> combinedList = new ObjectArrayList<>();
		collection.forEach(combinedList::addAll);
		return combinedList;
	}
}
