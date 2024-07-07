package org.mtr.mod.resource;

import org.mtr.core.data.Data;
import org.mtr.core.data.Vehicle;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.EnumHelper;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.*;
import org.mtr.mapping.holder.Box;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.OverlayTexture;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mod.Init;
import org.mtr.mod.MutableBox;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.VehicleExtension;
import org.mtr.mod.generated.resource.ModelPropertiesPartSchema;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Supplier;

public final class ModelPropertiesPart extends ModelPropertiesPartSchema implements IGui {

	private final ObjectArrayList<PartDetails> partDetailsList = new ObjectArrayList<>();
	private final ObjectArrayList<DisplayPartDetails> displayPartDetailsList = new ObjectArrayList<>();
	private final int displayColorCjkInt;
	private final int displayColorInt;

	private static final int LINE_PADDING = 2;

	public ModelPropertiesPart(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		displayColorInt = parseColor(displayColor, 0xFF9900);
		displayColorCjkInt = parseColor(displayColorCjk, displayColorInt);
	}

	ModelPropertiesPart(ObjectSet<String> names) {
		super(PartCondition.NORMAL, RenderStage.EXTERIOR, PartType.NORMAL, 0, 0, "", "", 0, 0, 0, DisplayType.DESTINATION, "", 0, 0, DoorAnimationType.STANDARD);
		this.names.addAll(names);
		positionDefinitions.add("");
		displayColorInt = 0;
		displayColorCjkInt = 0;
	}

	/**
	 * Maps each part name to the corresponding part and collects all floors, doors, and doorways for processing later.
	 * Writes to the collective vehicle model parts (one with doors, one without doors).
	 * If this part is a door, create an optimized model to be rendered later.
	 */
	public void writeCache(
			Identifier texture,
			Object2ObjectOpenHashMap<String, ObjectObjectImmutablePair<ModelPartExtension, MutableBox>> nameToPart,
			Object2ObjectOpenHashMap<String, ModelDisplayPart> nameToDisplayPart,
			PositionDefinitions positionDefinitionsObject,
			ObjectArraySet<Box> floors,
			ObjectArraySet<Box> doorways,
			Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsForPartConditionAndRenderStage,
			Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsForPartConditionAndRenderStageDoorsClosed
	) {
		final ObjectArrayList<ModelPartExtension> modelParts = new ObjectArrayList<>();
		final MutableBox mutableBox = new MutableBox();
		final ObjectArrayList<ModelDisplayPart> modelDisplayParts = new ObjectArrayList<>();
		final OptimizedModelWrapper optimizedModelDoor;

		names.forEach(name -> {
			final ObjectObjectImmutablePair<ModelPartExtension, MutableBox> part = nameToPart.get(name);
			if (part != null) {
				modelParts.add(part.left());
				mutableBox.add(part.right());
			}

			final ModelDisplayPart modelDisplayPart = nameToDisplayPart.get(name);
			if (modelDisplayPart != null) {
				modelDisplayParts.add(modelDisplayPart);
			}
		});

		if (isDoor()) {
			final OptimizedModelWrapper.MaterialGroupWrapper materialGroup = new OptimizedModelWrapper.MaterialGroupWrapper(renderStage.shaderType, texture);
			modelParts.forEach(modelPart -> materialGroup.addCube(modelPart, 0, 0, 0, false, MAX_LIGHT_INTERIOR));
			optimizedModelDoor = OptimizedModelWrapper.fromMaterialGroups(ObjectArrayList.of(materialGroup));
		} else {
			optimizedModelDoor = null;
		}

		positionDefinitions.forEach(positionDefinitionName -> positionDefinitionsObject.getPositionDefinition(positionDefinitionName, (positions, positionsFlipped) -> {
			switch (type) {
				case NORMAL:
					iteratePositions(positions, positionsFlipped, (x, y, z, flipped) -> {
						if (!isDoor()) {
							addCube(texture, modelParts, materialGroupsForPartConditionAndRenderStage, x, y, z, flipped);
						}
						addCube(texture, modelParts, materialGroupsForPartConditionAndRenderStageDoorsClosed, x, y, z, flipped);
						partDetailsList.add(new PartDetails(modelParts, optimizedModelDoor, addBox(mutableBox.get(), x, y, z, flipped), x, y, z, flipped));
					});
					break;
				case DISPLAY:
					iteratePositions(positions, positionsFlipped, (x, y, z, flipped) -> displayPartDetailsList.add(new DisplayPartDetails(modelDisplayParts, x, y, z, flipped)));
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

	public void writeCache(
			Map<String, OptimizedModel.ObjModel> nameToObjModels,
			PositionDefinitions positionDefinitionsObject,
			Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>>> objModelsForPartConditionAndRenderStage,
			Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>>> objModelsForPartConditionAndRenderStageDoorsClosed,
			double modelYOffset
	) {
		final ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper> objModels = new ObjectArrayList<>();
		final MutableBox mutableBox = new MutableBox();
		final Supplier<OptimizedModelWrapper> optimizedModelDoor;

		names.forEach(name -> {
			final OptimizedModel.ObjModel objModel = nameToObjModels.get(name);
			if (objModel != null) {
				objModels.add(new OptimizedModelWrapper.ObjModelWrapper(objModel));
				mutableBox.add(new Box(-objModel.getMinX(), -objModel.getMinY(), -objModel.getMinZ(), -objModel.getMaxX(), -objModel.getMaxY(), -objModel.getMaxZ()));
			}
		});

		optimizedModelDoor = () -> isDoor() ? OptimizedModelWrapper.fromObjModels(objModels) : null;

		positionDefinitions.forEach(positionDefinitionName -> positionDefinitionsObject.getPositionDefinition(positionDefinitionName, (positions, positionsFlipped) -> {
			if (type == PartType.NORMAL) {
				iteratePositions(positions, positionsFlipped, (x, y, z, flipped) -> {
					if (!isDoor()) {
						addObjModelPosition(objModels, objModelsForPartConditionAndRenderStage, x, y, z, flipped, modelYOffset);
					}
					addObjModelPosition(objModels, objModelsForPartConditionAndRenderStageDoorsClosed, x, y, z, flipped, modelYOffset);
					partDetailsList.add(new PartDetails(new ObjectArrayList<>(), optimizedModelDoor.get(), addBox(mutableBox.get(), x, y, z, flipped), x, y, z, flipped));
				});
			}
		}));
	}

	public void render(Identifier texture, StoredMatrixTransformations storedMatrixTransformations, @Nullable VehicleExtension vehicle, int light, ObjectArrayList<Box> openDoorways) {
		if (vehicle == null || VehicleResource.matchesCondition(vehicle, condition, openDoorways.isEmpty())) {
			switch (type) {
				case NORMAL:
					final ObjectIntImmutablePair<QueuedRenderLayer> renderProperties = getRenderProperties(renderStage, light, vehicle);
					if (OptimizedRenderer.hasOptimizedRendering()) {
						MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> renderNormal(storedMatrixTransformations, vehicle, renderProperties, openDoorways, light, graphicsHolder, offset));
					} else {
						MainRenderer.scheduleRender(texture, false, renderProperties.left(), (graphicsHolder, offset) -> renderNormal(storedMatrixTransformations, vehicle, renderProperties, openDoorways, light, graphicsHolder, offset));
					}
					break;
				case DISPLAY:
					if (vehicle != null) {
						if (displayOptions.contains(DisplayOption.SEVEN_SEGMENT.toString())) {
							MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/sign/seven_segment.png"), true, QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolder, offset) -> renderDisplay(graphicsHolder, offset, storedMatrixTransformations, vehicle, true));
						} else {
							MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> renderDisplay(graphicsHolder, offset, storedMatrixTransformations, vehicle, false));
						}
					}
					break;
			}
		}
	}

	public void getOpenDoorBounds(ObjectArrayList<Box> boxes, double time) {
		if (isDoor()) {
			partDetailsList.forEach(partDetails -> {
				final double x = doorAnimationType.getDoorAnimationX(doorXMultiplier, partDetails.flipped, time) / 16;
				final double z = doorAnimationType.getDoorAnimationZ(doorZMultiplier, partDetails.flipped, time, true) / 16;
				final Box box = partDetails.box;
				final float xOffset = box.getMinXMapped() == box.getMaxXMapped() ? 0.1F : 0;
				final float yOffset = box.getMinYMapped() == box.getMaxYMapped() ? 0.1F : 0;
				final float zOffset = box.getMinZMapped() == box.getMaxZMapped() ? 0.1F : 0;
				boxes.add(new Box(
						box.getMinXMapped() - xOffset + x,
						box.getMinYMapped() - yOffset,
						box.getMinZMapped() - zOffset + z,
						box.getMaxXMapped() + xOffset + x,
						box.getMaxYMapped() + yOffset,
						box.getMaxZMapped() + zOffset + z
				));
			});
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

	private void renderNormal(StoredMatrixTransformations storedMatrixTransformations, @Nullable VehicleExtension vehicle, ObjectIntImmutablePair<QueuedRenderLayer> renderProperties, ObjectArrayList<Box> openDoorways, int light, GraphicsHolder graphicsHolder, Vector3d offset) {
		storedMatrixTransformations.transform(graphicsHolder, offset);
		partDetailsList.forEach(partDetails -> {
			final boolean canOpenDoors = openDoorways.contains(partDetails.doorway);
			final float x = (float) (partDetails.x + (vehicle == null ? 0 : doorAnimationType.getDoorAnimationX(doorXMultiplier, partDetails.flipped, canOpenDoors ? vehicle.persistentVehicleData.getDoorValue() : 0)));
			final float y = (float) partDetails.y;
			final float z = (float) (partDetails.z + (vehicle == null ? 0 : doorAnimationType.getDoorAnimationZ(doorZMultiplier, partDetails.flipped, canOpenDoors ? vehicle.persistentVehicleData.getDoorValue() : 0, vehicle.vehicleExtraData.getDoorMultiplier() > 0)));

			if (OptimizedRenderer.hasOptimizedRendering()) {
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

	private void renderDisplay(GraphicsHolder graphicsHolder, Vector3d offset, StoredMatrixTransformations storedMatrixTransformations, Vehicle vehicle, boolean isSevenSegment) {
		final String[] textSplit = formatText(vehicle).split("\\|");
		final boolean[] isCjk = new boolean[textSplit.length];
		double tempTotalHeight = 0;
		for (int i = 0; i < textSplit.length; i++) {
			isCjk[i] = IGui.isCjk(textSplit[i]);
			tempTotalHeight += isCjk[i] ? displayCjkSizeRatio <= 0 ? 1 : displayCjkSizeRatio : 1;
		}
		final double totalHeight = tempTotalHeight;
		storedMatrixTransformations.transform(graphicsHolder, offset);

		displayPartDetailsList.forEach(displayPartDetails -> {
			graphicsHolder.translate(displayPartDetails.x, displayPartDetails.y, displayPartDetails.z);
			if (displayPartDetails.flipped) {
				graphicsHolder.rotateYDegrees(180);
			}

			displayPartDetails.modelDisplayParts.forEach(displayPart -> {
				displayPart.storedMatrixTransformations.transform(graphicsHolder, new Vector3d(0, 0, 0));
				graphicsHolder.translate(isSevenSegment ? 0 : displayXPadding / 16, displayYPadding / 16, -SMALL_OFFSET);

				for (int i = 0; i < textSplit.length; i++) {
					final double lineHeight = isCjk[i] ? displayCjkSizeRatio <= 0 ? 1 : displayCjkSizeRatio : 1;
					final double textHeight = Math.min((displayPart.height - displayYPadding * 2) * lineHeight / totalHeight, displayMaxLineHeight <= 0 ? Double.MAX_VALUE : displayMaxLineHeight * lineHeight) / 16;
					final HorizontalAlignment horizontalAlignment = getHorizontalAlignment(isCjk[i]);

					if (isSevenSegment) {
						IDrawing.drawSevenSegment(graphicsHolder, textSplit[i], (displayPart.width - (float) displayXPadding * 2) / 16, 0, 0, (displayPart.height - (float) displayYPadding * 2) / 16, horizontalAlignment, ARGB_BLACK | displayColorInt, GraphicsHolder.getDefaultLight());
					} else {
						final double heightScale = textHeight / (TEXT_HEIGHT + LINE_PADDING);
						final double textWidth = GraphicsHolder.getTextWidth(textSplit[i]) * heightScale;
						final double availableTextWidth = (displayPart.width - displayXPadding * 2) / 16;
						final double widthScale = Math.min(1, textWidth == 0 ? 1 : availableTextWidth / textWidth);
						graphicsHolder.push();
						graphicsHolder.translate(Math.max(0, horizontalAlignment.getOffset(0, (float) (textWidth - availableTextWidth))), 0, 0);
						graphicsHolder.scale((float) (widthScale * heightScale), (float) heightScale, 1);
						graphicsHolder.drawText(textSplit[i], 0, 0, isCjk[i] ? displayColorCjkInt : displayColorInt, false, GraphicsHolder.getDefaultLight());
						graphicsHolder.pop();
					}

					graphicsHolder.translate(0, textHeight, 0);
				}

				graphicsHolder.pop();
			});
		});

		graphicsHolder.pop();
	}

	private void addCube(Identifier texture, ObjectArrayList<ModelPartExtension> modelParts, Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsForPartConditionAndRenderStage, double x, double y, double z, boolean flipped) {
		modelParts.forEach(modelPart -> Data.put(materialGroupsForPartConditionAndRenderStage, condition, renderStage, oldValue -> {
			final OptimizedModelWrapper.MaterialGroupWrapper materialGroup = oldValue == null ? new OptimizedModelWrapper.MaterialGroupWrapper(renderStage.shaderType, texture) : oldValue;
			materialGroup.addCube(modelPart, (x + doorAnimationType.getDoorAnimationX(doorXMultiplier, flipped, 0)) / 16, y / 16, (z + doorAnimationType.getDoorAnimationZ(doorZMultiplier, flipped, 0, false)) / 16, flipped, MAX_LIGHT_INTERIOR);
			return materialGroup;
		}, Object2ObjectOpenHashMap::new));
	}

	private void addObjModelPosition(
			ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper> objModels,
			Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>>> objModelsForPartConditionAndRenderStage,
			double x, double y, double z, boolean flipped, double modelYOffset
	) {
		objModels.forEach(objModel -> Data.put(objModelsForPartConditionAndRenderStage, condition, renderStage, oldValue -> {
			final ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper> newObjModels = oldValue == null ? new ObjectArrayList<>() : oldValue;
			objModel.addTransformation(renderStage.shaderType, (x + doorAnimationType.getDoorAnimationX(doorXMultiplier, flipped, 0)) / 16, y / 16 - modelYOffset, (z + doorAnimationType.getDoorAnimationZ(doorZMultiplier, flipped, 0, false)) / 16, flipped);
			newObjModels.add(objModel);
			return newObjModels;
		}, Object2ObjectOpenHashMap::new));
	}

	private String formatText(Vehicle vehicle) {
		final String text;
		switch (displayType) {
			case DESTINATION:
				text = vehicle.getIsOnRoute() ? getOrDefault(vehicle.vehicleExtraData.getThisRouteDestination(), getOrDefault(vehicle.vehicleExtraData.getNextRouteDestination(), getOrDefault(vehicle.vehicleExtraData.getNextStationName(), getOrDefault(vehicle.vehicleExtraData.getThisStationName(), displayDefaultText)))) : displayDefaultText;
				break;
			case ROUTE_NUMBER:
				text = vehicle.getIsOnRoute() ? getOrDefault(vehicle.vehicleExtraData.getThisRouteNumber(), vehicle.vehicleExtraData.getThisRouteDestination(), getOrDefault(vehicle.vehicleExtraData.getNextRouteNumber(), vehicle.vehicleExtraData.getNextRouteDestination(), displayDefaultText)) : displayDefaultText;
				break;
			case DEPARTURE_INDEX:
				if (vehicle.getIsOnRoute()) {
					final StringBuilder stringBuilder = new StringBuilder(String.valueOf(vehicle.getDepartureIndex() + 1));
					final int startLength = stringBuilder.length();
					for (int i = startLength; i < displayPadZeros; i++) {
						stringBuilder.insert(0, "0");
					}
					text = stringBuilder.toString();
				} else {
					text = displayDefaultText;
				}
				break;
			case NEXT_STATION:
				text = vehicle.vehicleExtraData.getNextStationName();
				break;
			default:
				text = "";
		}

		String newText = text;
		for (final String displayOption : displayOptions) {
			newText = EnumHelper.valueOf(DisplayOption.NONE, displayOption).format(newText);
		}
		return newText;
	}

	private HorizontalAlignment getHorizontalAlignment(boolean isCjk) {
		if (isCjk) {
			if (displayOptions.contains(DisplayOption.ALIGN_LEFT_CJK.toString())) {
				return HorizontalAlignment.LEFT;
			} else if (displayOptions.contains(DisplayOption.ALIGN_RIGHT_CJK.toString())) {
				return HorizontalAlignment.RIGHT;
			} else if (displayOptions.contains(DisplayOption.ALIGN_CENTER_CJK.toString())) {
				return HorizontalAlignment.CENTER;
			}
		}
		if (displayOptions.contains(DisplayOption.ALIGN_LEFT.toString())) {
			return HorizontalAlignment.LEFT;
		} else if (displayOptions.contains(DisplayOption.ALIGN_RIGHT.toString())) {
			return HorizontalAlignment.RIGHT;
		} else {
			return HorizontalAlignment.CENTER;
		}
	}

	private static ObjectIntImmutablePair<QueuedRenderLayer> getRenderProperties(RenderStage renderStage, int light, @Nullable VehicleExtension vehicle) {
		if (renderStage == RenderStage.ALWAYS_ON_LIGHT) {
			return new ObjectIntImmutablePair<>(QueuedRenderLayer.LIGHT_TRANSLUCENT, GraphicsHolder.getDefaultLight());
		} else if (vehicle != null) {
			if (vehicle.getIsOnRoute()) {
				switch (renderStage) {
					case LIGHT:
						return new ObjectIntImmutablePair<>(QueuedRenderLayer.LIGHT, GraphicsHolder.getDefaultLight());
					case INTERIOR:
						return new ObjectIntImmutablePair<>(QueuedRenderLayer.INTERIOR, MAX_LIGHT_INTERIOR);
					case INTERIOR_TRANSLUCENT:
						return new ObjectIntImmutablePair<>(QueuedRenderLayer.INTERIOR_TRANSLUCENT, MAX_LIGHT_INTERIOR);
				}
			} else {
				if (renderStage == RenderStage.INTERIOR_TRANSLUCENT) {
					return new ObjectIntImmutablePair<>(QueuedRenderLayer.EXTERIOR_TRANSLUCENT, light);
				}
			}
		}

		return new ObjectIntImmutablePair<>(QueuedRenderLayer.EXTERIOR, light);
	}

	private static Box addBox(Box box, double x, double y, double z, boolean flipped) {
		return new Box(
				(flipped ? -1 : 1) * box.getMinXMapped() + x / 16, box.getMinYMapped() + y / 16, (flipped ? 1 : -1) * box.getMinZMapped() + z / 16,
				(flipped ? -1 : 1) * box.getMaxXMapped() + x / 16, box.getMaxYMapped() + y / 16, (flipped ? 1 : -1) * box.getMaxZMapped() + z / 16
		);
	}

	private static void iteratePositions(ObjectArrayList<PartPosition> positions, ObjectArrayList<PartPosition> positionsFlipped, PositionCallback positionCallback) {
		positions.forEach(position -> positionCallback.accept(position.getX(), position.getY(), position.getZ(), false));
		positionsFlipped.forEach(position -> positionCallback.accept(-position.getX(), position.getY(), position.getZ(), true));
	}

	private static double getClosestDistance(double a1, double a2, double b1, double b2) {
		return Math.min(Math.min(Math.abs(b1 - a1), Math.abs(b1 - a2)), Math.min(Math.abs(b2 - a1), Math.abs(b2 - a2)));
	}

	private static int parseColor(String colorString, int defaultColor) {
		try {
			return Integer.parseInt(colorString, 16);
		} catch (Exception ignored) {
			return defaultColor;
		}
	}

	private static String getOrDefault(String checkText, String defaultText) {
		return getOrDefault(checkText, checkText, defaultText);
	}

	private static String getOrDefault(String outputText, String checkText, String defaultText) {
		return checkText.isEmpty() ? defaultText : outputText;
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
			this.modelParts = OptimizedRenderer.hasOptimizedRendering() ? new ObjectArrayList<>() : modelParts;
			this.optimizedModelDoor = optimizedModelDoor;
			this.box = box;
			this.x = x;
			this.y = y;
			this.z = z;
			this.flipped = flipped;
		}
	}

	private static class DisplayPartDetails {

		private final ObjectArrayList<ModelDisplayPart> modelDisplayParts;
		private final double x;
		private final double y;
		private final double z;
		private final boolean flipped;

		private DisplayPartDetails(ObjectArrayList<ModelDisplayPart> modelDisplayParts, double x, double y, double z, boolean flipped) {
			this.modelDisplayParts = modelDisplayParts;
			this.x = x / 16;
			this.y = y / 16;
			this.z = z / 16;
			this.flipped = flipped;
		}
	}

	@FunctionalInterface
	private interface PositionCallback {
		void accept(double x, double y, double z, boolean flipped);
	}
}
