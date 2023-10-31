package org.mtr.core.data;

import org.mtr.core.generated.ModelPropertiesPartSchema;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.*;
import org.mtr.mapping.holder.Box;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.OverlayTexture;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.MutableBox;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.VehicleExtension;
import org.mtr.mod.render.RenderTrains;
import org.mtr.mod.render.StoredMatrixTransformations;

import javax.annotation.Nullable;
import java.util.Comparator;

public final class ModelPropertiesPart extends ModelPropertiesPartSchema implements IGui {

	private final ObjectArrayList<PartDetails> partDetailsList = new ObjectArrayList<>();

	public ModelPropertiesPart(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	/**
	 * Maps each part name to the corresponding part and collects all floors, doors, and doorways for processing later.
	 */
	public void writeCache(Object2ObjectOpenHashMap<String, ObjectObjectImmutablePair<ModelPartExtension, Box>> nameToPart, PositionDefinitions positionDefinitionsObject, ObjectArraySet<Box> floors, ObjectArraySet<Box> doorways) {
		final ObjectArrayList<ModelPartExtension> modelParts = new ObjectArrayList<>();
		final MutableBox mutableBox = new MutableBox();

		names.forEach(name -> {
			final ObjectObjectImmutablePair<ModelPartExtension, Box> part = nameToPart.get(name);
			if (part != null) {
				modelParts.add(part.left());
				mutableBox.add(part.right());
			}
		});

		positionDefinitions.forEach(positionDefinitionName -> positionDefinitionsObject.getPositionDefinition(positionDefinitionName, (positions, positionsFlipped) -> {
			switch (type) {
				case NORMAL:
				case DISPLAY:
					positions.forEach(position -> partDetailsList.add(new PartDetails(modelParts, add(mutableBox, position.getX(), position.getY(), position.getZ(), false), position, false)));
					positionsFlipped.forEach(position -> partDetailsList.add(new PartDetails(modelParts, add(mutableBox, position.getX(), position.getY(), position.getZ(), true), position, true)));
					break;
				case FLOOR:
					positions.forEach(position -> floors.add(add(mutableBox, position.getX(), position.getY(), position.getZ(), false)));
					positionsFlipped.forEach(position -> floors.add(add(mutableBox, position.getX(), position.getY(), position.getZ(), true)));
					break;
				case DOORWAY:
					positions.forEach(position -> doorways.add(add(mutableBox, position.getX(), position.getY(), position.getZ(), false)));
					positionsFlipped.forEach(position -> doorways.add(add(mutableBox, position.getX(), position.getY(), position.getZ(), true)));
					break;
			}
		}));
	}

	public void render(Identifier texture, StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int light, @Nullable ObjectArrayList<Box> openDoorways) {
		if (matchesCondition(vehicle, openDoorways == null || openDoorways.isEmpty())) {
			switch (type) {
				case NORMAL:
					renderNormal(texture, storedMatrixTransformations, vehicle, getRenderProperties(renderStage, light, vehicle), openDoorways);
					break;
				case DISPLAY:
					break;
			}
		}
	}

	/**
	 * If this part is a door, find the closest doorway.
	 */
	void mapDoors(ObjectArraySet<Box> doorways) {
		if (doorXMultiplier != 0 || doorZMultiplier != 0) {
			partDetailsList.forEach(partDetails -> doorways.stream().min(Comparator.comparingDouble(checkDoorway -> getClosestDistance(
					partDetails.box.getMinXMapped(),
					partDetails.box.getMaxXMapped(),
					checkDoorway.getMinXMapped(),
					checkDoorway.getMaxXMapped()
			) + getClosestDistance(
					partDetails.box.getMinYMapped(),
					partDetails.box.getMaxYMapped(),
					checkDoorway.getMinYMapped(),
					checkDoorway.getMaxYMapped()
			) + getClosestDistance(
					partDetails.box.getMinZMapped(),
					partDetails.box.getMaxZMapped(),
					checkDoorway.getMinZMapped(),
					checkDoorway.getMaxZMapped()
			))).ifPresent(closestDoorway -> partDetails.doorway = closestDoorway));
		}
	}

	private void renderNormal(Identifier texture, StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, ObjectIntImmutablePair<RenderTrains.QueuedRenderLayer> renderProperties, @Nullable ObjectArrayList<Box> openDoorways) {
		RenderTrains.scheduleRender(texture, false, renderProperties.left(), (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			partDetailsList.forEach(partDetails -> {
				final boolean canOpenDoors = openDoorways != null && openDoorways.contains(partDetails.doorway);
				final float x = (float) (partDetails.position.getX() + doorAnimationType.getDoorAnimationX(doorXMultiplier, canOpenDoors ? vehicle.persistentVehicleData.getDoorValue() : 0));
				final float y = (float) partDetails.position.getY();
				final float z = (float) (partDetails.position.getZ() + doorAnimationType.getDoorAnimationZ(doorZMultiplier, canOpenDoors ? vehicle.persistentVehicleData.getDoorValue() : 0, vehicle.vehicleExtraData.getDoorMultiplier() > 0));
				partDetails.modelParts.forEach(modelPart -> modelPart.render(graphicsHolder, x, y, z, partDetails.flipped ? (float) Math.PI : 0, renderProperties.rightInt(), OverlayTexture.getDefaultUvMapped()));
			});
			graphicsHolder.pop();
		});
	}

	private boolean matchesCondition(VehicleExtension vehicle, boolean noOpenDoorways) {
		switch (condition) {
			case AT_DEPOT:
				return !vehicle.getIsOnRoute();
			case ON_ROUTE_FORWARDS:
				return vehicle.getIsOnRoute() && !vehicle.getReversed();
			case ON_ROUTE_BACKWARDS:
				return vehicle.getIsOnRoute() && vehicle.getReversed();
			case DOORS_CLOSED:
				return vehicle.persistentVehicleData.getDoorValue() == 0 || noOpenDoorways;
			case DOORS_OPENED:
				return vehicle.persistentVehicleData.getDoorValue() > 0 && !noOpenDoorways;
			default:
				return true;
		}
	}

	private static ObjectIntImmutablePair<RenderTrains.QueuedRenderLayer> getRenderProperties(RenderStage renderStage, int light, VehicleExtension vehicle) {
		if (renderStage == RenderStage.ALWAYS_ON_LIGHT) {
			return new ObjectIntImmutablePair<>(RenderTrains.QueuedRenderLayer.LIGHT_TRANSLUCENT, MAX_LIGHT_GLOWING);
		} else {
			if (vehicle.getIsOnRoute()) {
				switch (renderStage) {
					case LIGHT:
						return new ObjectIntImmutablePair<>(RenderTrains.QueuedRenderLayer.LIGHT, MAX_LIGHT_GLOWING);
					case INTERIOR:
						return new ObjectIntImmutablePair<>(RenderTrains.QueuedRenderLayer.INTERIOR, MAX_LIGHT_INTERIOR);
					case INTERIOR_TRANSLUCENT:
						return new ObjectIntImmutablePair<>(RenderTrains.QueuedRenderLayer.INTERIOR_TRANSLUCENT, MAX_LIGHT_INTERIOR);
				}
			} else {
				if (renderStage == RenderStage.INTERIOR_TRANSLUCENT) {
					return new ObjectIntImmutablePair<>(RenderTrains.QueuedRenderLayer.EXTERIOR_TRANSLUCENT, light);
				}
			}
		}

		return new ObjectIntImmutablePair<>(RenderTrains.QueuedRenderLayer.EXTERIOR, light);
	}

	private static Box add(MutableBox mutableBox, double x, double y, double z, boolean flipped) {
		final Box box = mutableBox.get();
		return new Box(
				(flipped ? -1 : 1) * box.getMinXMapped() + x / 16, box.getMinYMapped() + y / 16, (flipped ? 1 : -1) * box.getMinZMapped() + z / 16,
				(flipped ? -1 : 1) * box.getMaxXMapped() + x / 16, box.getMaxYMapped() + y / 16, (flipped ? 1 : -1) * box.getMaxZMapped() + z / 16
		);
	}

	private static double getClosestDistance(double a1, double a2, double b1, double b2) {
		return Math.min(Math.min(Math.abs(b1 - a1), Math.abs(b1 - a2)), Math.min(Math.abs(b2 - a1), Math.abs(b2 - a2)));
	}

	private static class PartDetails {

		private Box doorway;
		private final ObjectArrayList<ModelPartExtension> modelParts;
		private final Box box;
		private final PartPosition position;
		private final boolean flipped;

		private PartDetails(ObjectArrayList<ModelPartExtension> modelParts, Box box, PartPosition position, boolean flipped) {
			this.modelParts = modelParts;
			this.box = box;
			this.position = position;
			this.flipped = flipped;
		}
	}
}
