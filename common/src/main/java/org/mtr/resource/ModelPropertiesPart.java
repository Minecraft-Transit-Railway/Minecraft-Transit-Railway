package org.mtr.resource;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.mtr.MTR;
import org.mtr.client.CustomResourceLoader;
import org.mtr.client.DynamicTextureCache;
import org.mtr.client.IDrawing;
import org.mtr.client.ScrollingText;
import org.mtr.core.data.Data;
import org.mtr.core.data.Vehicle;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.EnumHelper;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.data.VehicleExtension;
import org.mtr.generated.resource.ModelPropertiesPartSchema;
import org.mtr.model.MutableBox;
import org.mtr.model.NewOptimizedModelGroup;
import org.mtr.model.OptimizedModel;
import org.mtr.render.MainRenderer;
import org.mtr.render.QueuedRenderLayer;
import org.mtr.render.StoredMatrixTransformations;

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
			Object2ObjectOpenHashMap<String, ObjectObjectImmutablePair<ModelPart, MutableBox>> nameToPart,
			Object2ObjectOpenHashMap<String, ObjectArrayList<ModelDisplayPart>> nameToDisplayParts,
			PositionDefinitions positionDefinitionsObject,
			ObjectArraySet<Box> floors,
			ObjectArraySet<Box> doorways,
			Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> materialGroupsForPartConditionAndRenderStage,
			Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> materialGroupsForPartConditionAndRenderStageDoorsClosed
	) {
		final ObjectArrayList<ModelPart> modelParts = new ObjectArrayList<>();
		final MutableBox mutableBox = new MutableBox();
		final ObjectArrayList<ObjectArrayList<ModelDisplayPart>> modelDisplayParts = new ObjectArrayList<>();
		final OptimizedModelWrapper optimizedModelDoor;

		names.forEach(name -> {
			final ObjectObjectImmutablePair<ModelPart, MutableBox> part = nameToPart.get(name);
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
						partDetailsList.add(new PartDetails(optimizedModelDoor, addBox(mutableBox.get(), x, y, z, flipped), x, y, z, flipped));
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
					partDetailsList.add(new PartDetails(optimizedModelDoor.get(), addBox(mutableBox.get(), x, y, z, flipped), x, y, z, flipped));
				});
			}
		}));
	}

	public void render(Identifier texture, StoredMatrixTransformations storedMatrixTransformations, @Nullable VehicleExtension vehicle, int carNumber, int[] scrollingDisplayIndexTracker, int light, ObjectArrayList<Box> openDoorways, boolean fromResourcePackCreator) {
		if (vehicle == null || VehicleResource.matchesCondition(vehicle, condition, openDoorways.isEmpty())) {
			switch (type) {
				case NORMAL:
					final ObjectIntImmutablePair<QueuedRenderLayer> renderProperties = getRenderProperties(renderStage, light, vehicle);
					MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (matrixStack, vertexConsumer, offset) -> renderNormal(storedMatrixTransformations, vehicle, renderProperties, openDoorways, light, matrixStack, vertexConsumer, offset));
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
				final float xOffset = box.minX == box.maxX ? 0.1F : 0;
				final float yOffset = box.minY == box.maxY ? 0.1F : 0;
				final float zOffset = box.minZ == box.maxZ ? 0.1F : 0;
				boxes.add(new Box(
						box.minX - xOffset + x,
						box.minY - yOffset,
						box.minZ - zOffset + z,
						box.maxX + xOffset + x,
						box.maxY + yOffset,
						box.maxZ + zOffset + z
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
					partDetails.box.minX,
					partDetails.box.maxX,
					checkDoorway.minX,
					checkDoorway.maxX
			) + getClosestDistance(
					partDetails.box.minY,
					partDetails.box.maxY,
					checkDoorway.minY,
					checkDoorway.maxY
			) + getClosestDistance(
					partDetails.box.minZ,
					partDetails.box.maxZ,
					checkDoorway.minZ,
					checkDoorway.maxZ
			))).ifPresent(closestDoorway -> partDetails.doorway = closestDoorway));
		}
	}

	private boolean isDoor() {
		return doorXMultiplier != 0 || doorZMultiplier != 0;
	}

	private void renderNormal(StoredMatrixTransformations storedMatrixTransformations, @Nullable VehicleExtension vehicle, ObjectIntImmutablePair<QueuedRenderLayer> renderProperties, ObjectArrayList<Box> openDoorways, int light, MatrixStack matrixStack, VertexConsumer vertexConsumer, Vec3d offset) {
		storedMatrixTransformations.transform(matrixStack, offset);
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

			// If doors are open, only render the optimized door parts
			// Otherwise, the main model already includes closed doors
			if (!openDoorways.isEmpty() && partDetails.optimizedModelDoor != null) {
				matrixStack.push();
				matrixStack.translate(x / 16, y / 16, z / 16);
				if (partDetails.flipped) {
					IDrawing.rotateYDegrees(matrixStack, 180);
				}
				CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.queue(partDetails.optimizedModelDoor, matrixStack, light);
				matrixStack.pop();
			}
		});
		matrixStack.pop();
	}

	private void renderLineColor(StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, boolean fromResourcePackCreator) {
		final int color;
		if (fromResourcePackCreator) {
			color = ARGB_BLACK | rainbowColor();
		} else {
			color = getOrDefault(ARGB_BLACK | vehicle.vehicleExtraData.getThisRouteColor(), ARGB_BLACK | vehicle.vehicleExtraData.getNextRouteColor(), ARGB_BLACK | vehicle.vehicleExtraData.getPreviousRouteColor(), 0, vehicle);
		}

		MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, String.format("textures/block/%s.png", displayType == DisplayType.ROUTE_COLOR ? "white" : "sign/circle")), true, QueuedRenderLayer.LIGHT_2, (matrixStack, vertexConsumer, offset) -> {
			storedMatrixTransformations.transform(matrixStack, offset);

			displayPartDetailsList.forEach(displayPartDetails -> {
				matrixStack.push();
				matrixStack.translate(displayPartDetails.x, displayPartDetails.y, displayPartDetails.z);
				IDrawing.rotateYDegrees(matrixStack, displayPartDetails.flipped ? 180 : 0);

				displayPartDetails.modelDisplayParts.forEach(displayParts -> displayParts.forEach(displayPart -> {
					displayPart.storedMatrixTransformations.transform(matrixStack, Vec3d.ZERO);
					matrixStack.translate(displayXPadding / 16, displayYPadding / 16, -SMALL_OFFSET);
					IDrawing.drawTexture(
							matrixStack,
							vertexConsumer,
							0,
							0,
							(displayPart.width - (float) displayXPadding * 2) / 16,
							(displayPart.height - (float) displayYPadding * 2) / 16,
							0, 0, 1, 1, Direction.UP,
							color, DEFAULT_LIGHT
					);
					matrixStack.pop();
				}));

				matrixStack.pop();
			});

			matrixStack.pop();
		});
	}

	private void renderSevenSegmentDisplay(StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle) {
		final String text = formatText(vehicle);
		final HorizontalAlignment horizontalAlignment = getHorizontalAlignment(false);

		MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/block/sign/seven_segment.png"), true, QueuedRenderLayer.LIGHT_2, (matrixStack, vertexConsumer, offset) -> {
			storedMatrixTransformations.transform(matrixStack, offset);

			displayPartDetailsList.forEach(displayPartDetails -> {
				matrixStack.push();
				matrixStack.translate(displayPartDetails.x, displayPartDetails.y, displayPartDetails.z);
				IDrawing.rotateYDegrees(matrixStack, displayPartDetails.flipped ? 180 : 0);

				displayPartDetails.modelDisplayParts.forEach(displayParts -> displayParts.forEach(displayPart -> {
					displayPart.storedMatrixTransformations.transform(matrixStack, Vec3d.ZERO);
					matrixStack.translate(0, displayYPadding / 16, -SMALL_OFFSET);
					IDrawing.drawSevenSegment(
							matrixStack,
							vertexConsumer,
							text,
							(displayPart.width - (float) displayXPadding * 2) / 16,
							0, 0,
							(displayPart.height - (float) displayYPadding * 2) / 16,
							horizontalAlignment,
							ARGB_BLACK | displayColorInt, DEFAULT_LIGHT
					);
					matrixStack.pop();
				}));

				matrixStack.pop();
			});

			matrixStack.pop();
		});
	}

	private void renderScrollingDisplay(StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int carNumber, int[] scrollingDisplayIndexTracker) {
		final String text = formatText(vehicle);
		final ObjectArrayList<ScrollingText> scrollingTexts = vehicle.persistentVehicleData.getScrollingText(carNumber);

		displayPartDetailsList.forEach(displayPartDetails -> {
			final StoredMatrixTransformations storedMatrixTransformations1 = storedMatrixTransformations.copy();
			storedMatrixTransformations1.add(matrixStack -> {
				matrixStack.translate(displayPartDetails.x, displayPartDetails.y, displayPartDetails.z);
				IDrawing.rotateYDegrees(matrixStack, displayPartDetails.flipped ? 180 : 0);
			});

			displayPartDetails.modelDisplayParts.forEach(displayParts -> displayParts.forEach(displayPart -> {
				final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations1.copy();
				storedMatrixTransformations2.add(displayPart.storedMatrixTransformations);
				storedMatrixTransformations2.add(matrixStack -> matrixStack.translate(displayXPadding / 16, displayYPadding / 16, -SMALL_OFFSET));
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
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
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

		MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (matrixStack, vertexConsumer, offset) -> {
			storedMatrixTransformations.transform(matrixStack, offset);

			displayPartDetailsList.forEach(displayPartDetails -> {
				matrixStack.push();
				matrixStack.translate(displayPartDetails.x, displayPartDetails.y, displayPartDetails.z);
				IDrawing.rotateYDegrees(matrixStack, displayPartDetails.flipped ? 180 : 0);

				displayPartDetails.modelDisplayParts.forEach(displayParts -> displayParts.forEach(displayPart -> {
					displayPart.storedMatrixTransformations.transform(matrixStack, Vec3d.ZERO);
					final double totalTextHeight = Math.min(displayPart.height - displayYPadding * 2, displayMaxLineHeight <= 0 ? Double.MAX_VALUE : displayMaxLineHeight * rawTextHeight) / 16;
					final double textScale = totalTextHeight / rawTextHeight / (TEXT_HEIGHT + LINE_PADDING);
					matrixStack.translate(displayXPadding / 16, displayYPadding / 16 + Math.max(0, getVerticalAlignment().getOffset(0, (float) (totalTextHeight - (displayPart.height - displayYPadding * 2) / 16))), -SMALL_OFFSET);

					for (int i = 0; i < textSplit.length; i++) {
						final double availableTextWidth = (displayPart.width - displayXPadding * 2) / 16;
						final double newTextScale = textHeightScale[i] * textScale;
						final MutableText mutableText = IDrawing.withMTRFont(Text.literal(textSplit[i]));
						final double textWidth = textRenderer.getWidth(mutableText) * newTextScale;
						final HorizontalAlignment horizontalAlignment = getHorizontalAlignment(isCjk[i]);
						matrixStack.push();
						matrixStack.translate(Math.max(0, horizontalAlignment.getOffset(0, (float) (textWidth - availableTextWidth))), 0, 0);
						matrixStack.scale((float) (Math.min(1, availableTextWidth / textWidth) * newTextScale), (float) newTextScale, 1);
//						textRenderer.draw(mutableText, 0, 0, isCjk[i] ? displayColorCjkInt : displayColorInt, false, DEFAULT_LIGHT);
						matrixStack.pop();
						matrixStack.translate(0, newTextScale * (TEXT_HEIGHT + LINE_PADDING), 0);
					}

					matrixStack.pop();
				}));

				matrixStack.pop();
			});

			matrixStack.pop();
		});
	}

	private void addCube(Identifier texture, ObjectArrayList<ModelPart> modelParts, Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> materialGroupsForPartConditionAndRenderStage, double x, double y, double z, boolean flipped) {
		modelParts.forEach(modelPart -> materialGroupsForPartConditionAndRenderStage.computeIfAbsent(condition, key -> new NewOptimizedModelGroup()).add(renderStage, texture, vertexConsumer -> {
			final MatrixStack matrixStack = new MatrixStack();
			IDrawing.rotateXDegrees(matrixStack, 180); // Blockbench exports models upside down
			matrixStack.translate((x + doorAnimationType.getDoorAnimationX(doorXMultiplier, flipped, 0)) / 16, y / 16, (z + doorAnimationType.getDoorAnimationZ(doorZMultiplier, flipped, 0, false)) / 16);
			if (flipped) {
				matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
			}
			modelPart.render(matrixStack, vertexConsumer, MAX_LIGHT_INTERIOR, OverlayTexture.DEFAULT_UV);
		}));
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
			return new ObjectIntImmutablePair<>(QueuedRenderLayer.LIGHT_2, DEFAULT_LIGHT);
		} else if (vehicle != null) {
			if (vehicle.getIsOnRoute()) {
				switch (renderStage) {
					case LIGHT:
						return new ObjectIntImmutablePair<>(QueuedRenderLayer.LIGHT, DEFAULT_LIGHT);
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
				(flipped ? -1 : 1) * box.minX + x / 16, box.minY + y / 16, (flipped ? 1 : -1) * box.minZ + z / 16,
				(flipped ? -1 : 1) * box.maxX + x / 16, box.maxY + y / 16, (flipped ? 1 : -1) * box.maxZ + z / 16
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
		private final OptimizedModelWrapper optimizedModelDoor;
		private final Box box;
		private final double x;
		private final double y;
		private final double z;
		private final boolean flipped;

		private PartDetails(@Nullable OptimizedModelWrapper optimizedModelDoor, Box box, double x, double y, double z, boolean flipped) {
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
