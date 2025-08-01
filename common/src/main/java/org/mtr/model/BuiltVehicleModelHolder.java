package org.mtr.model;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.Box;
import org.mtr.data.VehicleExtension;
import org.mtr.render.MainRenderer;
import org.mtr.render.StoredMatrixTransformations;
import org.mtr.resource.*;

import javax.annotation.Nullable;

public final class BuiltVehicleModelHolder {

	public final ModelProperties modelProperties;
	private final Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> builtModels;
	private final ObjectArrayList<BuiltDoorModelDetails> builtDoorModelDetailsList;
	private final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<ModelDisplayPart>> displays;
	public final ObjectArrayList<Box> floors;
	public final ObjectArrayList<Box> doorways;

	public BuiltVehicleModelHolder(
			ModelProperties modelProperties,
			Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> builtModels,
			ObjectArrayList<BuiltDoorModelDetails> builtDoorModelDetailsList,
			Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<ModelDisplayPart>> displays,
			ObjectArrayList<Box> floors,
			ObjectArrayList<Box> doorways
	) {
		this.modelProperties = modelProperties;
		this.builtModels = builtModels;
		this.builtDoorModelDetailsList = builtDoorModelDetailsList;
		this.displays = displays;
		this.floors = floors;
		this.doorways = doorways;
	}

	public void render(StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int carNumber, int[] scrollingDisplayIndexTracker, int light, ObjectArrayList<Box> openDoorways, boolean fromResourcePackCreator) {
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

		displays.forEach((partCondition, modelDisplayParts) -> {
			if (matchesCondition(vehicle, partCondition, noOpenDoorways)) {
				modelDisplayParts.forEach(modelDisplayPart -> modelDisplayPart.render(storedMatrixTransformations, vehicle, carNumber, scrollingDisplayIndexTracker, fromResourcePackCreator));
			}
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
			@Nullable Box box,
			boolean flipped
	) {
	}
}
