package org.mtr.mod.render;

import org.mtr.core.data.Data;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.*;
import org.mtr.mapping.holder.Box;
import org.mtr.mapping.holder.EntityAbstractMapping;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.EntityModelExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mod.MutableBox;
import org.mtr.mod.ObjectHolder;
import org.mtr.mod.data.VehicleExtension;
import org.mtr.mod.resource.*;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public final class DynamicVehicleModel extends EntityModelExtension<EntityAbstractMapping> {

	public final ModelProperties modelProperties;
	private final Identifier texture;
	private final ObjectArraySet<Box> floors = new ObjectArraySet<>();
	private final ObjectArraySet<Box> doorways = new ObjectArraySet<>();
	private final Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsForPartConditionAndRenderStage = new Object2ObjectOpenHashMap<>();
	private final Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsForPartConditionAndRenderStageDoorsClosed = new Object2ObjectOpenHashMap<>();
	private final Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>>> objModelsForPartConditionAndRenderStage = new Object2ObjectOpenHashMap<>();
	private final Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>>> objModelsForPartConditionAndRenderStageDoorsClosed = new Object2ObjectOpenHashMap<>();

	public DynamicVehicleModel(BlockbenchModel blockbenchModel, Identifier texture, ModelProperties modelProperties, PositionDefinitions positionDefinitions) {
		super(blockbenchModel.getTextureWidth(), blockbenchModel.getTextureHeight());

		final Object2ObjectOpenHashMap<String, BlockbenchElement> uuidToBlockbenchElement = new Object2ObjectOpenHashMap<>();
		blockbenchModel.getElements().forEach(blockbenchElement -> uuidToBlockbenchElement.put(blockbenchElement.getUuid(), blockbenchElement));

		final Object2ObjectOpenHashMap<String, ObjectObjectImmutablePair<ModelPartExtension, MutableBox>> nameToPart = new Object2ObjectOpenHashMap<>();
		final Object2ObjectOpenHashMap<String, ModelDisplayPart> nameToDisplayPart = new Object2ObjectOpenHashMap<>();
		blockbenchModel.getOutlines().forEach(blockbenchOutline -> {
			final ObjectHolder<ModelPartExtension> parentModelPart = new ObjectHolder<>(this::createModelPart);
			final MutableBox mutableBox = new MutableBox();
			final ObjectHolder<ModelDisplayPart> modelDisplayPart = new ObjectHolder<>(ModelDisplayPart::new);

			iterateChildren(blockbenchOutline, new GroupTransformations(), (uuid, groupTransformations) -> {
				final BlockbenchElement blockbenchElement = uuidToBlockbenchElement.remove(uuid);
				if (blockbenchElement != null) {
					mutableBox.add(blockbenchElement.setModelPart(parentModelPart.createAndGet().addChild(), groupTransformations, modelDisplayPart.createAndGet(), (float) modelProperties.getModelYOffset()));
				}
			});

			if (parentModelPart.exists()) {
				nameToPart.put(blockbenchOutline.getName(), new ObjectObjectImmutablePair<>(parentModelPart.createAndGet(), mutableBox));
			}

			if (modelDisplayPart.exists()) {
				nameToDisplayPart.put(blockbenchOutline.getName(), modelDisplayPart.createAndGet());
			}
		});

		buildModel();
		modelProperties.addPartsIfEmpty(nameToPart.keySet());
		this.texture = texture;
		this.modelProperties = modelProperties;
		modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.writeCache(texture, nameToPart, nameToDisplayPart, positionDefinitions, floors, doorways, materialGroupsForPartConditionAndRenderStage, materialGroupsForPartConditionAndRenderStageDoorsClosed));
	}

	public DynamicVehicleModel(Object2ObjectAVLTreeMap<String, OptimizedModel.ObjModel> nameToObjModels, Identifier texture, ModelProperties modelProperties, PositionDefinitions positionDefinitions) {
		super(0, 0);
		buildModel();
		modelProperties.addPartsIfEmpty(nameToObjModels.keySet());
		this.texture = texture;
		this.modelProperties = modelProperties;
		modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.writeCache(texture, nameToObjModels, positionDefinitions, objModelsForPartConditionAndRenderStage, objModelsForPartConditionAndRenderStageDoorsClosed, modelProperties.getModelYOffset()));
	}

	@Override
	public void setAngles2(EntityAbstractMapping entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public void render(StoredMatrixTransformations storedMatrixTransformations, @Nullable VehicleExtension vehicle, int light, ObjectArrayList<Box> openDoorways) {
		modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.render(texture, storedMatrixTransformations, vehicle, light, openDoorways));
	}

	public void writeFloorsAndDoorways(
			ObjectArraySet<Box> floors,
			ObjectArraySet<Box> doorways,
			Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsForPartCondition,
			Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsForPartConditionDoorsClosed,
			Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> objModelsForPartCondition,
			Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> objModelsForPartConditionDoorsClosed
	) {
		floors.addAll(this.floors);
		doorways.addAll(this.doorways);
		materialGroupsForPartConditionAndRenderStage.forEach((partCondition, materialGroupsForRenderStage) -> Data.put(materialGroupsForPartCondition, partCondition, materialGroupsForRenderStage.values(), ObjectArrayList::new));
		materialGroupsForPartConditionAndRenderStageDoorsClosed.forEach((partCondition, materialGroupsForRenderStage) -> Data.put(materialGroupsForPartConditionDoorsClosed, partCondition, materialGroupsForRenderStage.values(), ObjectArrayList::new));
		objModelsForPartConditionAndRenderStage.forEach((partCondition, objModelsForRenderStage) -> Data.put(objModelsForPartCondition, partCondition, flattenCollection(objModelsForRenderStage.values()), ObjectArrayList::new));
		objModelsForPartConditionAndRenderStageDoorsClosed.forEach((partCondition, objModelsForRenderStage) -> Data.put(objModelsForPartConditionDoorsClosed, partCondition, flattenCollection(objModelsForRenderStage.values()), ObjectArrayList::new));
	}

	private static void iterateChildren(BlockbenchOutline blockbenchOutline, GroupTransformations groupTransformations, BiConsumer<String, GroupTransformations> consumer) {
		final GroupTransformations newGroupTransformations = blockbenchOutline.add(groupTransformations);
		blockbenchOutline.childrenUuid.forEach(uuid -> consumer.accept(uuid, newGroupTransformations));
		blockbenchOutline.getChildren().forEach(childOutline -> iterateChildren(childOutline, newGroupTransformations, consumer));
	}

	private static <T> ObjectArrayList<T> flattenCollection(ObjectCollection<? extends ObjectCollection<T>> collection) {
		final ObjectArrayList<T> combinedList = new ObjectArrayList<>();
		collection.forEach(combinedList::addAll);
		return combinedList;
	}
}
