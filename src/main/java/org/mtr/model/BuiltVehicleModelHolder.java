package org.mtr.model;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;
import org.mtr.data.VehicleExtension;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.render.MainRenderer;
import org.mtr.render.StoredMatrixTransformations;
import org.mtr.resource.*;

import java.util.Comparator;

public final class BuiltVehicleModelHolder {

	public final ModelProperties modelProperties;
	private final Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> builtModels;
	private final ObjectArrayList<ModelPropertiesPart.RawDoorModelDetails> rawDoorModelDetailsList;
	private final VertexFormat.Mode drawMode;
	private final ObjectArrayList<BuiltDoorModelDetails> builtDoorModelDetailsList = new ObjectArrayList<>();
	private final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<ModelDisplayPart>> displays;
	public final ObjectArrayList<AABB> floors;
	public final ObjectArrayList<AABB> doorways;

	public BuiltVehicleModelHolder(
		ModelProperties modelProperties,
		Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> builtModels,
		ObjectArrayList<ModelPropertiesPart.RawDoorModelDetails> rawDoorModelDetailsList,
		VertexFormat.Mode drawMode,
		Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<ModelDisplayPart>> displays,
		ObjectArrayList<AABB> floors,
		ObjectArrayList<AABB> doorways
	) {
		this.modelProperties = modelProperties;
		this.builtModels = builtModels;
		this.rawDoorModelDetailsList = rawDoorModelDetailsList;
		this.drawMode = drawMode;
		this.displays = displays;
		this.floors = floors;
		this.doorways = doorways;
	}

	public void render(StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int carNumber, boolean isWithinHalfRenderDistance, int[] scrollingDisplayIndexTracker, int light, ObjectArrayList<AABB> openDoorways, boolean fromResourcePackCreator) {
		final boolean noOpenDoorways = openDoorways.isEmpty();

		builtModels.forEach((partCondition, models) -> {
			if (matchesCondition(vehicle, partCondition, noOpenDoorways)) {
				MainRenderer.renderModel(models, storedMatrixTransformations, light);
			}
		});

		builtDoorModelDetailsList.forEach(builtDoorModelDetails -> {
			final StoredMatrixTransformations newStoredMatrixTransformations = builtDoorModelDetails.modelPropertiesPart.getDoorOffset(storedMatrixTransformations, openDoorways.contains(builtDoorModelDetails.box) ? vehicle : null, builtDoorModelDetails.flipped);
			builtDoorModelDetails.models.forEach((partCondition, doorModels) -> {
				if (matchesCondition(vehicle, partCondition, noOpenDoorways)) {
					MainRenderer.renderModel(doorModels, newStoredMatrixTransformations, light);
				}
			});
		});

		if (isWithinHalfRenderDistance) {
			displays.forEach((partCondition, modelDisplayParts) -> {
				if (matchesCondition(vehicle, partCondition, noOpenDoorways)) {
					modelDisplayParts.forEach(modelDisplayPart -> modelDisplayPart.render(storedMatrixTransformations, vehicle, carNumber, scrollingDisplayIndexTracker, fromResourcePackCreator));
				}
			});
		}
	}

	/**
	 * Builds and maps door models using the combined doorways from all vehicle models.
	 * Should be called once after all models have been loaded and their doorways aggregated.
	 */
	public void mapDoors(ObjectArrayList<AABB> allDoorways) {
		rawDoorModelDetailsList.forEach(rawDoorModelDetails -> {
			final AABB closestDoorway = allDoorways.isEmpty() ? null : allDoorways.stream().min(Comparator.comparingDouble(checkDoorway ->
				rawDoorModelDetails.boxes().stream().map(box -> getClosestDistance(
					box.minX, box.maxX, checkDoorway.minX, checkDoorway.maxX
				) + getClosestDistance(
					box.minY, box.maxY, checkDoorway.minY, checkDoorway.maxY
				) + getClosestDistance(
					box.minZ, box.maxZ, checkDoorway.minZ, checkDoorway.maxZ
				)).min(Double::compareTo).orElse(Double.MAX_VALUE)
			)).orElse(null);

			final Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> builtDoorModel = new Object2ObjectOpenHashMap<>();
			rawDoorModelDetails.rawModels().forEach((partCondition, newOptimizedModelGroup) -> builtDoorModel.put(partCondition, newOptimizedModelGroup.build(drawMode)));
			builtDoorModelDetailsList.add(new BuiltDoorModelDetails(builtDoorModel, rawDoorModelDetails.modelPropertiesPart(), closestDoorway, rawDoorModelDetails.flipped()));
		});
	}

	private static boolean matchesCondition(VehicleExtension vehicle, PartCondition partCondition, boolean noOpenDoorways) {
		return switch (partCondition) {
			case AT_DEPOT -> !vehicle.getIsOnRoute();
			case ON_ROUTE_FORWARDS -> vehicle.getIsOnRoute() && !vehicle.getReversed();
			case ON_ROUTE_BACKWARDS -> vehicle.getIsOnRoute() && vehicle.getReversed();
			case DOORS_CLOSED -> vehicle.persistentVehicleData.getDoorValue() == 0 && noOpenDoorways;
			case DOORS_OPENED -> vehicle.persistentVehicleData.getDoorValue() > 0 || !noOpenDoorways;
			default -> getChristmasLightState(partCondition);
		};
	}

	private static double getClosestDistance(double a1, double a2, double b1, double b2) {
		return Math.min(Math.min(Math.abs(b1 - a1), Math.abs(b1 - a2)), Math.min(Math.abs(b2 - a1), Math.abs(b2 - a2)));
	}

	private static boolean getChristmasLightState(PartCondition partCondition) {
		final int index;
		switch (partCondition) {
			case CHRISTMAS_LIGHT_RED:
				index = 0;
				break;
			case CHRISTMAS_LIGHT_YELLOW:
				index = 1;
				break;
			case CHRISTMAS_LIGHT_GREEN:
				index = 2;
				break;
			case CHRISTMAS_LIGHT_BLUE:
				index = 3;
				break;
			default:
				return true;
		}
		return CHRISTMAS_LIGHT_STAGES[(int) ((System.currentTimeMillis() / 500) % CHRISTMAS_LIGHT_STAGES.length)][index];
	}

	private static final boolean[][] CHRISTMAS_LIGHT_STAGES = {
		{true, false, false, false},
		{false, true, false, false},
		{false, false, true, false},
		{false, false, false, true},
		{true, false, false, false},
		{false, true, false, false},
		{false, false, true, false},
		{false, false, false, true},

		{true, true, false, false},
		{false, true, true, false},
		{false, false, true, true},
		{true, false, false, true},
		{true, true, false, false},
		{false, true, true, false},
		{false, false, true, true},
		{true, false, false, true},

		{true, false, true, false},
		{false, true, false, true},
		{true, false, true, false},
		{false, true, false, true},
		{true, false, true, false},
		{false, true, false, true},
		{true, false, true, false},
		{false, true, false, true},

		{true, false, false, false},
		{true, true, false, false},
		{true, true, true, false},
		{true, true, true, true},
		{false, true, false, false},
		{false, true, true, false},
		{false, true, true, true},
		{true, true, true, true},
		{false, false, true, false},
		{false, false, true, true},
		{true, false, true, true},
		{true, true, true, true},
		{false, false, false, true},
		{true, false, false, true},
		{true, true, false, true},
		{true, true, true, true},

		{false, false, false, false},
		{true, true, true, true},
		{true, true, true, true},
		{true, true, true, true},
		{false, false, false, false},
		{true, true, true, true},
		{true, true, true, true},
		{true, true, true, true},
	};

	public record BuiltDoorModelDetails(
		Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> models,
		ModelPropertiesPart modelPropertiesPart,
		@Nullable AABB box,
		boolean flipped
	) {
	}
}
