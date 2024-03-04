package org.mtr.mod.resource;

import org.mtr.core.data.Data;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.*;
import org.mtr.mapping.holder.Box;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.OverlayTexture;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.MutableBox;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.VehicleExtension;
import org.mtr.mod.generated.resource.ModelPropertiesPartSchema;
import org.mtr.mod.render.RenderTrains;
import org.mtr.mod.render.RenderVehicles;
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
	 * Writes to the collective vehicle model parts (one with doors, one without doors).
	 * If this part is a door, create an optimized model to be rendered later.
	 */
	public void writeCache(
			Identifier texture,
			Object2ObjectOpenHashMap<String, ObjectObjectImmutablePair<ModelPartExtension, MutableBox>> nameToPart,
			PositionDefinitions positionDefinitionsObject,
			ObjectArraySet<Box> floors,
			ObjectArraySet<Box> doorways,
			Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsForPartConditionAndRenderStage,
			Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsForPartConditionAndRenderStageDoorsClosed
	) {
		final ObjectArrayList<ModelPartExtension> modelParts = new ObjectArrayList<>();
		final MutableBox mutableBox = new MutableBox();
		final OptimizedModelWrapper optimizedModelDoor;

		names.forEach(name -> {
			final ObjectObjectImmutablePair<ModelPartExtension, MutableBox> part = nameToPart.get(name);
			if (part != null) {
				modelParts.add(part.left());
				mutableBox.add(part.right());
			}
		});

		if (isDoor()) {
			final OptimizedModelWrapper.MaterialGroupWrapper materialGroup = new OptimizedModelWrapper.MaterialGroupWrapper(renderStage.shaderType, texture);
			modelParts.forEach(modelPart -> materialGroup.addCube(modelPart, 0, 0, 0, false, MAX_LIGHT_INTERIOR));
			optimizedModelDoor = new OptimizedModelWrapper(ObjectArrayList.of(materialGroup));
		} else {
			optimizedModelDoor = null;
		}

		positionDefinitions.forEach(positionDefinitionName -> positionDefinitionsObject.getPositionDefinition(positionDefinitionName, (positions, positionsFlipped) -> {
			switch (type) {
				case NORMAL:
				case DISPLAY:
					iteratePositions(positions, positionsFlipped, (x, y, z, flipped) -> {
						if (!isDoor()) {
							addCube(texture, modelParts, materialGroupsForPartConditionAndRenderStage, x, y, z, flipped);
						}
						addCube(texture, modelParts, materialGroupsForPartConditionAndRenderStageDoorsClosed, x, y, z, flipped);
						partDetailsList.add(new PartDetails(modelParts, optimizedModelDoor, addBox(mutableBox.get(), x, y, z, flipped), x, y, z, flipped));
					});
					break;
				case FLOOR:
					iteratePositions(positions, positionsFlipped, (x, y, z, flipped) -> mutableBox.getAll().forEach(box -> floors.add(addBox(box, x, y, z, flipped))));
					break;
				case DOORWAY:
					iteratePositions(positions, positionsFlipped, (x, y, z, flipped) -> mutableBox.getAll().forEach(box -> doorways.add(addBox(box, x, y, z, flipped))));
					break;
			}
		}));
	}

	public void render(Identifier texture, StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int light, ObjectArrayList<Box> openDoorways) {
		if (VehicleResource.matchesCondition(vehicle, condition, openDoorways.isEmpty())) {
			switch (type) {
				case NORMAL:
					final ObjectIntImmutablePair<RenderTrains.QueuedRenderLayer> renderProperties = getRenderProperties(renderStage, light, vehicle);
					if (RenderVehicles.useOptimizedRendering()) {
						RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> renderNormal(storedMatrixTransformations, vehicle, renderProperties, openDoorways, light, graphicsHolder, offset));
					} else {
						RenderTrains.scheduleRender(texture, false, renderProperties.left(), (graphicsHolder, offset) -> renderNormal(storedMatrixTransformations, vehicle, renderProperties, openDoorways, light, graphicsHolder, offset));
					}
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
		if (isDoor()) {
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

	private boolean isDoor() {
		return doorXMultiplier != 0 || doorZMultiplier != 0;
	}

	private void renderNormal(StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, ObjectIntImmutablePair<RenderTrains.QueuedRenderLayer> renderProperties, ObjectArrayList<Box> openDoorways, int light, GraphicsHolder graphicsHolder, Vector3d offset) {
		storedMatrixTransformations.transform(graphicsHolder, offset);
		partDetailsList.forEach(partDetails -> {
			final boolean canOpenDoors = openDoorways.contains(partDetails.doorway);
			final float x = (float) (partDetails.x + doorAnimationType.getDoorAnimationX(doorXMultiplier, canOpenDoors ? vehicle.persistentVehicleData.getDoorValue() : 0));
			final float y = (float) partDetails.y;
			final float z = (float) (partDetails.z + doorAnimationType.getDoorAnimationZ(doorZMultiplier, canOpenDoors ? vehicle.persistentVehicleData.getDoorValue() : 0, vehicle.vehicleExtraData.getDoorMultiplier() > 0));

			if (RenderVehicles.useOptimizedRendering()) {
				// If doors are open, only render the optimized door parts
				// Otherwise, the main model already includes closed doors
				if (!openDoorways.isEmpty() && partDetails.optimizedModelDoor != null) {
					graphicsHolder.push();
					graphicsHolder.translate(x / 16, y / 16, z / 16);
					if (partDetails.flipped) {
						graphicsHolder.rotateYDegrees(180);
					}
					CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.queue(partDetails.optimizedModelDoor, graphicsHolder, light);
					graphicsHolder.pop();
				}
			} else {
				partDetails.modelParts.forEach(modelPart -> modelPart.render(graphicsHolder, x, y, z, partDetails.flipped ? (float) Math.PI : 0, renderProperties.rightInt(), OverlayTexture.getDefaultUvMapped()));
			}
		});
		graphicsHolder.pop();
	}

	private void addCube(Identifier texture, ObjectArrayList<ModelPartExtension> modelParts, Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsForPartConditionAndRenderStage, double x, double y, double z, boolean flipped) {
		modelParts.forEach(modelPart -> Data.put(materialGroupsForPartConditionAndRenderStage, condition, renderStage, oldValue -> {
			final OptimizedModelWrapper.MaterialGroupWrapper materialGroup = oldValue == null ? new OptimizedModelWrapper.MaterialGroupWrapper(renderStage.shaderType, texture) : oldValue;
			materialGroup.addCube(modelPart, (x + doorAnimationType.getDoorAnimationX(doorXMultiplier, 0)) / 16, y / 16, (z + doorAnimationType.getDoorAnimationZ(doorZMultiplier, 0, false)) / 16, flipped, MAX_LIGHT_INTERIOR);
			return materialGroup;
		}, Object2ObjectOpenHashMap::new));
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

	private static Box addBox(Box box, double x, double y, double z, boolean flipped) {
		return new Box(
				(flipped ? -1 : 1) * box.getMinXMapped() + x / 16, box.getMinYMapped() + y / 16, (flipped ? 1 : -1) * box.getMinZMapped() + z / 16,
				(flipped ? -1 : 1) * box.getMaxXMapped() + x / 16, box.getMaxYMapped() + y / 16, (flipped ? 1 : -1) * box.getMaxZMapped() + z / 16
		);
	}

	private static void iteratePositions(ObjectArrayList<PartPosition> positions, ObjectArrayList<PartPosition> positionsFlipped, PositionCallback positionCallback) {
		positions.forEach(position -> positionCallback.accept(position.getX(), position.getY(), position.getZ(), false));
		positionsFlipped.forEach(position -> positionCallback.accept(position.getX(), position.getY(), position.getZ(), true));
	}

	private static double getClosestDistance(double a1, double a2, double b1, double b2) {
		return Math.min(Math.min(Math.abs(b1 - a1), Math.abs(b1 - a2)), Math.min(Math.abs(b2 - a1), Math.abs(b2 - a2)));
	}

	private static class PartDetails {

		private Box doorway;
		private final ObjectArrayList<ModelPartExtension> modelParts;
		private final OptimizedModelWrapper optimizedModelDoor;
		private final Box box;
		private final double x;
		private final double y;
		private final double z;
		private final boolean flipped;

		private PartDetails(ObjectArrayList<ModelPartExtension> modelParts, @Nullable OptimizedModelWrapper optimizedModelDoor, Box box, double x, double y, double z, boolean flipped) {
			this.modelParts = modelParts;
			this.optimizedModelDoor = optimizedModelDoor;
			this.box = box;
			this.x = x;
			this.y = y;
			this.z = z;
			this.flipped = flipped;
		}
	}

	@FunctionalInterface
	private interface PositionCallback {
		void accept(double x, double y, double z, boolean flipped);
	}
}
