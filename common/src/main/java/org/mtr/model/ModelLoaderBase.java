package org.mtr.model;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.mtr.render.StoredMatrixTransformations;
import org.mtr.resource.*;

import java.util.Comparator;

public abstract class ModelLoaderBase {

	protected final Identifier defaultTexture;
	private final VertexFormat.DrawMode drawMode;

	private final Object2ObjectOpenHashMap<String, NewOptimizedModelGroup> nameToNewOptimizedModelGroup = new Object2ObjectOpenHashMap<>();
	private final Object2ObjectOpenHashMap<String, ObjectArrayList<ObjectObjectImmutablePair<StoredMatrixTransformations, IntIntImmutablePair>>> nameToRawModelDisplayParts = new Object2ObjectOpenHashMap<>();

	public ModelLoaderBase(Identifier defaultTexture, VertexFormat.DrawMode drawMode) {
		this.defaultTexture = defaultTexture;
		this.drawMode = drawMode;
	}

	public final BuiltVehicleModelHolder build(ModelProperties modelProperties, PositionDefinitions positionDefinitions) {
		final Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> rawModels = new Object2ObjectOpenHashMap<>();
		final ObjectArrayList<ModelPropertiesPart.RawDoorModelDetails> rawDoorModelDetailsList = new ObjectArrayList<>();
		final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<ModelDisplayPart>> displays = new Object2ObjectOpenHashMap<>();
		final ObjectArrayList<Box> floors = new ObjectArrayList<>();
		final ObjectArrayList<Box> doorways = new ObjectArrayList<>();
		modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.writeCache(nameToNewOptimizedModelGroup, nameToRawModelDisplayParts, positionDefinitions, rawModels, rawDoorModelDetailsList, displays, floors, doorways, modelProperties.getModelYOffset()));
		return new BuiltVehicleModelHolder(modelProperties, build(rawModels), mapDoors(rawDoorModelDetailsList, doorways), displays, floors, doorways);
	}

	public final Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> build() {
		final NewOptimizedModelGroup newOptimizedModelGroupCombined = new NewOptimizedModelGroup();
		nameToNewOptimizedModelGroup.values().forEach(newOptimizedModelGroup -> newOptimizedModelGroupCombined.merge(newOptimizedModelGroup, RenderStage.EXTERIOR, 0, 0, 0, false));
		return newOptimizedModelGroupCombined.build(drawMode);
	}

	public final ObjectArrayList<String> getNames() {
		return new ObjectArrayList<>(nameToNewOptimizedModelGroup.keySet());
	}

	protected final void addModel(String name, NewOptimizedModelGroup newOptimizedModelGroup) {
		nameToNewOptimizedModelGroup.put(name, newOptimizedModelGroup);
	}

	protected final void addModelDisplayParts(String name, ObjectArrayList<ObjectObjectImmutablePair<StoredMatrixTransformations, IntIntImmutablePair>> modelDisplayParts) {
		nameToRawModelDisplayParts.put(name, modelDisplayParts);
	}

	private Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> build(Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> rawModels) {
		final Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> models = new Object2ObjectOpenHashMap<>();
		rawModels.forEach((partCondition, newOptimizedModelGroup) -> models.put(partCondition, newOptimizedModelGroup.build(drawMode)));
		return models;
	}

	/**
	 * If this part is a door, find the closest doorway.
	 */
	private ObjectArrayList<BuiltVehicleModelHolder.BuiltDoorModelDetails> mapDoors(
			ObjectArrayList<ModelPropertiesPart.RawDoorModelDetails> rawDoorModelDetailsList,
			ObjectArrayList<Box> doorways
	) {
		final ObjectArrayList<BuiltVehicleModelHolder.BuiltDoorModelDetails> builtDoorModelDetailsList = new ObjectArrayList<>();
		rawDoorModelDetailsList.forEach(rawDoorModelDetails -> {
			final Box closestDoorway = doorways.stream().min(Comparator.comparingDouble(checkDoorway -> rawDoorModelDetails.boxes().stream().map(box -> getClosestDistance(
					box.minX,
					box.maxX,
					checkDoorway.minX,
					checkDoorway.maxX
			) + getClosestDistance(
					box.minY,
					box.maxY,
					checkDoorway.minY,
					checkDoorway.maxY
			) + getClosestDistance(
					box.minZ,
					box.maxZ,
					checkDoorway.minZ,
					checkDoorway.maxZ
			)).min(Double::compareTo).orElse(Double.MAX_VALUE))).orElse(null);

			final Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> builtDoorModel = new Object2ObjectOpenHashMap<>();
			rawDoorModelDetails.rawModels().forEach((partCondition, newOptimizedModelGroup) -> builtDoorModel.put(partCondition, newOptimizedModelGroup.build(drawMode)));
			builtDoorModelDetailsList.add(new BuiltVehicleModelHolder.BuiltDoorModelDetails(builtDoorModel, rawDoorModelDetails.modelPropertiesPart(), closestDoorway, rawDoorModelDetails.flipped()));
		});
		return builtDoorModelDetailsList;
	}

	private static double getClosestDistance(double a1, double a2, double b1, double b2) {
		return Math.min(Math.min(Math.abs(b1 - a1), Math.abs(b1 - a2)), Math.min(Math.abs(b2 - a1), Math.abs(b2 - a2)));
	}
}
