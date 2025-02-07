package org.mtr.mod.resource;

import org.mtr.core.data.Data;
import org.mtr.core.data.Vehicle;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.EnumHelper;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.*;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.Init;
import org.mtr.mod.MutableBox;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.ScrollingText;
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
		super(PartCondition.NORMAL, RenderStage.EXTERIOR, PartType.NORMAL, 0, 0, "", "", 0, 0, 0, DisplayType.DESTINATION, "", 0, 0, DoorAnimationType.STANDARD, 0, 0, 0, 0, 0, 0);
		this.names.addAll(names);
		positionDefinitions.add("");
		displayColorInt = 0;
		displayColorCjkInt = 0;
	}

	ModelPropertiesPart(
			ObjectArrayList<String> names,
			ObjectArrayList<String> positionDefinitions,
			PartCondition condition,
			RenderStage renderStage,
			PartType type,
			double displayXPadding,
			double displayYPadding,
			String displayColorCjk,
			String displayColor,
			double displayMaxLineHeight,
			double displayCjkSizeRatio,
			ObjectArrayList<String> displayOptions,
			double displayPadZeros,
			DisplayType displayType,
			String displayDefaultText,
			double doorXMultiplier,
			double doorZMultiplier,
			DoorAnimationType doorAnimationType,
			long renderFromOpeningDoorTime,
			long renderUntilOpeningDoorTime,
			long renderFromClosingDoorTime,
			long renderUntilClosingDoorTime,
			long flashOnTime,
			long flashOffTime
	) {
		super(
				condition,
				renderStage,
				type,
				displayXPadding,
				displayYPadding,
				displayColorCjk,
				displayColor,
				displayMaxLineHeight,
				displayCjkSizeRatio,
				displayPadZeros,
				displayType,
				displayDefaultText,
				doorXMultiplier,
				doorZMultiplier,
				doorAnimationType,
				renderFromOpeningDoorTime,
				renderUntilOpeningDoorTime,
				renderFromClosingDoorTime,
				renderUntilClosingDoorTime,
				flashOnTime,
				flashOffTime
		);
		this.names.addAll(names);
		this.positionDefinitions.addAll(positionDefinitions);
		this.displayOptions.addAll(displayOptions);
		displayColorInt = parseColor(displayColor, 0xFF9900);
		displayColorCjkInt = parseColor(displayColorCjk, displayColorInt);
	}

	/**
	 * Maps each part name to the corresponding part and collects all floors, doors, and doorways for processing later.
	 * Writes to the collective vehicle model parts (one with doors, one without doors).
	 * If this part is a door, create an optimized model to be rendered later.
	 */
	public void writeCache(
			Identifier texture,
			Object2ObjectOpenHashMap<String, ObjectObjectImmutablePair<ModelPartExtension, MutableBox>> nameToPart,
			Object2ObjectOpenHashMap<String, ObjectArrayList<ModelDisplayPart>> nameToDisplayParts,
			PositionDefinitions positionDefinitionsObject,
			ObjectArraySet<Box> floors,
			ObjectArraySet<Box> doorways,
			Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsForPartConditionAndRenderStage,
			Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsForPartConditionAndRenderStageDoorsClosed
	) {
		final ObjectArrayList<ModelPartExtension> modelParts = new ObjectArrayList<>();
		final MutableBox mutableBox = new MutableBox();
		final ObjectArrayList<ObjectArrayList<ModelDisplayPart>> modelDisplayParts = new ObjectArrayList<>();
		final OptimizedModelWrapper optimizedModelDoor;

		names.forEach(name -> {
			final ObjectObjectImmutablePair<ModelPartExtension, MutableBox> part = nameToPart.get(name);
			if (part != null) {
				modelParts.add(part.left());
				mutableBox.add(part.right());
			}

			final ObjectArrayList<ModelDisplayPart> displayParts = nameToDisplayParts.get(name);
			if (displayParts != null) {
				modelDisplayParts.add(displayParts);
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

	public void render(Identifier texture, StoredMatrixTransformations storedMatrixTransformations, @Nullable VehicleExtension vehicle, int carNumber, int[] scrollingDisplayIndexTracker, int light, ObjectArrayList<Box> openDoorways, boolean fromResourcePackCreator) {
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
						if (displayType == DisplayType.ROUTE_COLOR || displayType == DisplayType.ROUTE_COLOR_ROUNDED) {
							renderLineColor(storedMatrixTransformations, vehicle, fromResourcePackCreator);
						} else {
							if (displayOptions.contains(DisplayOption.SEVEN_SEGMENT.toString())) {
								renderSevenSegmentDisplay(storedMatrixTransformations, vehicle);
							} else if (displayOptions.contains(DisplayOption.SCROLL_NORMAL.toString()) || displayOptions.contains(DisplayOption.SCROLL_LIGHT_RAIL.toString())) {
								renderScrollingDisplay(storedMatrixTransformations, vehicle, carNumber, scrollingDisplayIndexTracker);
							} else {
								renderDisplay(storedMatrixTransformations, vehicle);
							}
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

	void addToModelPropertiesPartWrapperMap(PositionDefinitions actualPositionDefinitions, ObjectArrayList<ModelPropertiesPartWrapper> parts) {
		names.forEach(modelPartName -> positionDefinitions.forEach(positionDefinitionName -> actualPositionDefinitions.getPositionDefinition(positionDefinitionName, (positions, positionsFlipped) -> parts.add(new ModelPropertiesPartWrapper(
				new PositionDefinition(modelPartName, positions, positionsFlipped),
				condition,
				renderStage,
				type,
				displayXPadding,
				displayYPadding,
				displayColorCjk,
				displayColor,
				displayMaxLineHeight,
				displayCjkSizeRatio,
				displayOptions,
				displayPadZeros,
				displayType,
				displayDefaultText,
				doorXMultiplier,
				doorZMultiplier,
				doorAnimationType,
				renderFromOpeningDoorTime,
				renderUntilOpeningDoorTime,
				renderFromClosingDoorTime,
				renderUntilClosingDoorTime,
				flashOnTime,
				flashOffTime
		)))));
	}

	/**
	 * If this part is a door, find the closest doorway.
	 */
	void mapDoors(ObjectArrayList<Box> doorways) {
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
		final boolean flashOn = flashOnTime + flashOffTime == 0 || (System.currentTimeMillis() % (flashOnTime + flashOffTime)) > flashOffTime;
		partDetailsList.forEach(partDetails -> {
			final float x;
			final float y = flashOn ? (float) partDetails.y : Integer.MAX_VALUE;
			final float z;

			if (vehicle == null) {
				x = (float) partDetails.x;
				z = (float) partDetails.z;
			} else {
				final double doorValue = openDoorways.contains(partDetails.doorway) ? vehicle.persistentVehicleData.getDoorValue() : 0;
				final boolean opening = vehicle.persistentVehicleData.getAdjustedDoorMultiplier(vehicle.vehicleExtraData) > 0;
				final boolean shouldRender;

				if (opening) {
					shouldRender = renderFromOpeningDoorTime == 0 && renderUntilOpeningDoorTime == 0 || Utilities.isBetween(Math.abs(doorValue) * Vehicle.DOOR_MOVE_TIME, renderFromOpeningDoorTime, renderUntilOpeningDoorTime);
				} else {
					shouldRender = renderFromClosingDoorTime == 0 && renderUntilClosingDoorTime == 0 || Utilities.isBetween(Math.abs(doorValue) * Vehicle.DOOR_MOVE_TIME, renderFromClosingDoorTime, renderUntilClosingDoorTime);
				}

				x = shouldRender ? (float) (partDetails.x + doorAnimationType.getDoorAnimationX(doorXMultiplier, partDetails.flipped, doorValue)) : Integer.MAX_VALUE;
				z = shouldRender ? (float) (partDetails.z + doorAnimationType.getDoorAnimationZ(doorZMultiplier, partDetails.flipped, doorValue, opening)) : Integer.MAX_VALUE;
			}

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

	private void renderLineColor(StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, boolean fromResourcePackCreator) {
		final int color;
		if (fromResourcePackCreator) {
			color = ARGB_BLACK | rainbowColor();
		} else {
			color = getOrDefault(ARGB_BLACK | vehicle.vehicleExtraData.getThisRouteColor(), ARGB_BLACK | vehicle.vehicleExtraData.getNextRouteColor(), ARGB_BLACK | vehicle.vehicleExtraData.getPreviousRouteColor(), 0, vehicle);
		}

		MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, String.format("textures/block/%s.png", displayType == DisplayType.ROUTE_COLOR ? "white" : "sign/circle")), true, QueuedRenderLayer.LIGHT_2, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);

			displayPartDetailsList.forEach(displayPartDetails -> {
				graphicsHolder.push();
				graphicsHolder.translate(displayPartDetails.x, displayPartDetails.y, displayPartDetails.z);
				graphicsHolder.rotateYDegrees(displayPartDetails.flipped ? 180 : 0);

				displayPartDetails.modelDisplayParts.forEach(displayParts -> displayParts.forEach(displayPart -> {
					displayPart.storedMatrixTransformations.transform(graphicsHolder, Vector3d.getZeroMapped());
					graphicsHolder.translate(displayXPadding / 16, displayYPadding / 16, -SMALL_OFFSET);
					IDrawing.drawTexture(
							graphicsHolder,
							0,
							0,
							(displayPart.width - (float) displayXPadding * 2) / 16,
							(displayPart.height - (float) displayYPadding * 2) / 16,
							0, 0, 1, 1, Direction.UP,
							color, GraphicsHolder.getDefaultLight()
					);
					graphicsHolder.pop();
				}));

				graphicsHolder.pop();
			});

			graphicsHolder.pop();
		});
	}

	private void renderSevenSegmentDisplay(StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle) {
		final String text = formatText(vehicle);
		final HorizontalAlignment horizontalAlignment = getHorizontalAlignment(false);

		MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/sign/seven_segment.png"), true, QueuedRenderLayer.LIGHT_2, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);

			displayPartDetailsList.forEach(displayPartDetails -> {
				graphicsHolder.push();
				graphicsHolder.translate(displayPartDetails.x, displayPartDetails.y, displayPartDetails.z);
				graphicsHolder.rotateYDegrees(displayPartDetails.flipped ? 180 : 0);

				displayPartDetails.modelDisplayParts.forEach(displayParts -> displayParts.forEach(displayPart -> {
					displayPart.storedMatrixTransformations.transform(graphicsHolder, Vector3d.getZeroMapped());
					graphicsHolder.translate(0, displayYPadding / 16, -SMALL_OFFSET);
					IDrawing.drawSevenSegment(
							graphicsHolder,
							text,
							(displayPart.width - (float) displayXPadding * 2) / 16,
							0, 0,
							(displayPart.height - (float) displayYPadding * 2) / 16,
							horizontalAlignment,
							ARGB_BLACK | displayColorInt, GraphicsHolder.getDefaultLight()
					);
					graphicsHolder.pop();
				}));

				graphicsHolder.pop();
			});

			graphicsHolder.pop();
		});
	}

	private void renderScrollingDisplay(StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int carNumber, int[] scrollingDisplayIndexTracker) {
		final String text = formatText(vehicle);
		final ObjectArrayList<ScrollingText> scrollingTexts = vehicle.persistentVehicleData.getScrollingText(carNumber);

		displayPartDetailsList.forEach(displayPartDetails -> {
			final StoredMatrixTransformations storedMatrixTransformations1 = storedMatrixTransformations.copy();
			storedMatrixTransformations1.add(graphicsHolder -> {
				graphicsHolder.translate(displayPartDetails.x, displayPartDetails.y, displayPartDetails.z);
				graphicsHolder.rotateYDegrees(displayPartDetails.flipped ? 180 : 0);
			});

			displayPartDetails.modelDisplayParts.forEach(displayParts -> displayParts.forEach(displayPart -> {
				final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations1.copy();
				storedMatrixTransformations2.add(displayPart.storedMatrixTransformations);
				storedMatrixTransformations2.add(graphicsHolder -> graphicsHolder.translate(displayXPadding / 16, displayYPadding / 16, -SMALL_OFFSET));
				final double width = (displayPart.width - displayXPadding * 2) / 16F;
				final double height = (displayPart.height - displayYPadding * 2) / 16F;

				while (scrollingTexts.size() <= scrollingDisplayIndexTracker[0]) {
					scrollingTexts.add(new ScrollingText(width, height, 4, height < 0.1));
				}

				scrollingTexts.get(scrollingDisplayIndexTracker[0]).changeImage(text.isEmpty() ? null : DynamicTextureCache.instance.getPixelatedText(text, ARGB_BLACK | displayColorInt, Integer.MAX_VALUE, displayCjkSizeRatio, height < 0.1));
				scrollingTexts.get(scrollingDisplayIndexTracker[0]).scrollText(storedMatrixTransformations2);
				scrollingDisplayIndexTracker[0]++;
			}));
		});
	}

	private void renderDisplay(StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle) {
		final String[] textSplit = formatText(vehicle).split("\\|");
		final boolean[] isCjk = new boolean[textSplit.length];
		final double[] textHeightScale = new double[textSplit.length];
		double tempTotalHeight = 0;
		for (int i = 0; i < textSplit.length; i++) {
			isCjk[i] = IGui.isCjk(textSplit[i]);
			textHeightScale[i] = isCjk[i] ? displayCjkSizeRatio <= 0 ? 1 : displayCjkSizeRatio : 1;
			tempTotalHeight += textHeightScale[i];
		}
		final double rawTextHeight = tempTotalHeight;

		MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);

			displayPartDetailsList.forEach(displayPartDetails -> {
				graphicsHolder.push();
				graphicsHolder.translate(displayPartDetails.x, displayPartDetails.y, displayPartDetails.z);
				graphicsHolder.rotateYDegrees(displayPartDetails.flipped ? 180 : 0);

				displayPartDetails.modelDisplayParts.forEach(displayParts -> displayParts.forEach(displayPart -> {
					displayPart.storedMatrixTransformations.transform(graphicsHolder, Vector3d.getZeroMapped());
					final double totalTextHeight = Math.min(displayPart.height - displayYPadding * 2, displayMaxLineHeight <= 0 ? Double.MAX_VALUE : displayMaxLineHeight * rawTextHeight) / 16;
					final double textScale = totalTextHeight / rawTextHeight / (TEXT_HEIGHT + LINE_PADDING);
					graphicsHolder.translate(displayXPadding / 16, displayYPadding / 16 + Math.max(0, getVerticalAlignment().getOffset(0, (float) (totalTextHeight - (displayPart.height - displayYPadding * 2) / 16))), -SMALL_OFFSET);

					for (int i = 0; i < textSplit.length; i++) {
						final double availableTextWidth = (displayPart.width - displayXPadding * 2) / 16;
						final double newTextScale = textHeightScale[i] * textScale;
						final MutableText mutableText = IDrawing.withMTRFont(TextHelper.literal(textSplit[i]));
						final double textWidth = GraphicsHolder.getTextWidth(mutableText) * newTextScale;
						final HorizontalAlignment horizontalAlignment = getHorizontalAlignment(isCjk[i]);
						graphicsHolder.push();
						graphicsHolder.translate(Math.max(0, horizontalAlignment.getOffset(0, (float) (textWidth - availableTextWidth))), 0, 0);
						graphicsHolder.scale((float) (Math.min(1, availableTextWidth / textWidth) * newTextScale), (float) newTextScale, 1);
						graphicsHolder.drawText(mutableText, 0, 0, isCjk[i] ? displayColorCjkInt : displayColorInt, false, GraphicsHolder.getDefaultLight());
						graphicsHolder.pop();
						graphicsHolder.translate(0, newTextScale * (TEXT_HEIGHT + LINE_PADDING), 0);
					}

					graphicsHolder.pop();
				}));

				graphicsHolder.pop();
			});

			graphicsHolder.pop();
		});
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
		final String destination = getOrDefault(vehicle.vehicleExtraData.getThisRouteDestination(), vehicle.vehicleExtraData.getNextRouteDestination(), vehicle.vehicleExtraData.getPreviousRouteDestination(), displayDefaultText, vehicle);
		final String routeNumber = getOrDefault(vehicle.vehicleExtraData.getThisRouteNumber(), vehicle.vehicleExtraData.getNextRouteNumber(), vehicle.vehicleExtraData.getPreviousRouteNumber(), displayDefaultText, vehicle);
		final String routeName = getOrDefault(routeNumber + " ", routeNumber, "") + getOrDefault(vehicle.vehicleExtraData.getThisRouteName(), vehicle.vehicleExtraData.getNextRouteName(), vehicle.vehicleExtraData.getPreviousRouteName(), displayDefaultText, vehicle);
		final String thisStation = getOrDefault(vehicle.vehicleExtraData.getThisStationName(), vehicle.vehicleExtraData.getPreviousStationName());
		final String nextStation = getOrDefault(vehicle.vehicleExtraData.getNextStationName(), vehicle.vehicleExtraData.getThisStationName(), vehicle.vehicleExtraData.getThisStationName());
		final boolean doorsOpen = vehicle.vehicleExtraData.getDoorMultiplier() > 0;

		final String text;
		switch (displayType) {
			case DESTINATION:
				text = vehicle.getIsOnRoute() ? destination : displayDefaultText;
				break;
			case ROUTE_NUMBER:
				text = vehicle.getIsOnRoute() ? routeNumber : displayDefaultText;
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
				text = vehicle.getIsOnRoute() ? doorsOpen ? thisStation : nextStation : displayDefaultText;
				break;
			case NEXT_STATION_KCR:
				text = vehicle.getIsOnRoute() ? DisplayType.getHongKongNextStationString(thisStation, nextStation, doorsOpen, true) : displayDefaultText;
				break;
			case NEXT_STATION_MTR:
				text = vehicle.getIsOnRoute() ? DisplayType.getHongKongNextStationString(thisStation, nextStation, doorsOpen, false) : displayDefaultText;
				break;
			case NEXT_STATION_UK:
				text = vehicle.getIsOnRoute() ? DisplayType.getLondonNextStationString(routeName, thisStation, nextStation, vehicle.vehicleExtraData::iterateInterchanges, destination, doorsOpen, vehicle.vehicleExtraData.getIsTerminating()) : displayDefaultText;
				break;
			default:
				text = "";
				break;
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
			} else {
				return HorizontalAlignment.CENTER;
			}
		} else {
			if (displayOptions.contains(DisplayOption.ALIGN_LEFT.toString())) {
				return HorizontalAlignment.LEFT;
			} else if (displayOptions.contains(DisplayOption.ALIGN_RIGHT.toString())) {
				return HorizontalAlignment.RIGHT;
			} else {
				return HorizontalAlignment.CENTER;
			}
		}
	}

	private VerticalAlignment getVerticalAlignment() {
		if (displayOptions.contains(DisplayOption.ALIGN_TOP.toString())) {
			return VerticalAlignment.TOP;
		} else if (displayOptions.contains(DisplayOption.ALIGN_BOTTOM.toString())) {
			return VerticalAlignment.BOTTOM;
		} else {
			return VerticalAlignment.CENTER;
		}
	}

	private static ObjectIntImmutablePair<QueuedRenderLayer> getRenderProperties(RenderStage renderStage, int light, @Nullable VehicleExtension vehicle) {
		if (renderStage == RenderStage.ALWAYS_ON_LIGHT) {
			return new ObjectIntImmutablePair<>(QueuedRenderLayer.LIGHT_2, GraphicsHolder.getDefaultLight());
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

	private static int rainbowColor() {
		final long timeR = System.currentTimeMillis() % 3000;
		final long timeG = (timeR + 1000) % 3000;
		final long timeB = (timeR + 2000) % 3000;
		int r = timeR < 2000 ? (int) Math.round(Math.sin(timeR * Math.PI / 2000) * 0xFF) : 0;
		int g = timeG < 2000 ? (int) Math.round(Math.sin(timeG * Math.PI / 2000) * 0xFF) : 0;
		int b = timeB < 2000 ? (int) Math.round(Math.sin(timeB * Math.PI / 2000) * 0xFF) : 0;
		return (r << 16) + (g << 8) + b;
	}

	private static String getOrDefault(String checkText, String defaultText) {
		return getOrDefault(checkText, checkText, defaultText);
	}

	private static <T> T getOrDefault(T outputValue, String checkText, T defaultValue) {
		return checkText.isEmpty() ? defaultValue : outputValue;
	}

	private static <T> T getOrDefault(T thisRouteData, T nextRouteData, T previousRouteData, T defaultValue, Vehicle vehicle) {
		if (vehicle.vehicleExtraData.getThisRouteId() != 0) {
			return thisRouteData;
		} else if (vehicle.vehicleExtraData.getNextRouteId() != 0) {
			return nextRouteData;
		} else if (vehicle.vehicleExtraData.getPreviousRouteId() != 0) {
			return previousRouteData;
		} else {
			return defaultValue;
		}
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

		private final ObjectArrayList<ObjectArrayList<ModelDisplayPart>> modelDisplayParts;
		private final double x;
		private final double y;
		private final double z;
		private final boolean flipped;

		private DisplayPartDetails(ObjectArrayList<ObjectArrayList<ModelDisplayPart>> modelDisplayParts, double x, double y, double z, boolean flipped) {
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
