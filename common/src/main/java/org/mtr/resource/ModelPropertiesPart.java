package org.mtr.resource;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.mtr.MTR;
import org.mtr.client.DynamicTextureCache;
import org.mtr.client.IDrawing;
import org.mtr.client.ScrollingText;
import org.mtr.core.data.Vehicle;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.EnumHelper;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.data.VehicleExtension;
import org.mtr.font.FontGroups;
import org.mtr.font.FontRenderOptions;
import org.mtr.generated.resource.ModelPropertiesPartSchema;
import org.mtr.model.NewOptimizedModelGroup;
import org.mtr.render.MainRenderer;
import org.mtr.render.QueuedRenderLayer;
import org.mtr.render.StoredMatrixTransformations;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;

public final class ModelPropertiesPart extends ModelPropertiesPartSchema {

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
			ConcurrentHashMap<String, NewOptimizedModelGroup> nameToNewOptimizedModelGroup,
			ConcurrentHashMap<String, ObjectArrayList<ObjectObjectImmutablePair<StoredMatrixTransformations, IntIntImmutablePair>>> nameToRawModelDisplayParts,
			PositionDefinitions positionDefinitionsObject,
			Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> rawModels,
			ObjectArrayList<RawDoorModelDetails> rawDoorModelDetailsList,
			Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<ModelDisplayPart>> displays,
			ObjectArrayList<Box> floors,
			ObjectArrayList<Box> doorways,
			double modelYOffset
	) {
		final ObjectArrayList<NewOptimizedModelGroup> newOptimizedModelGroups = new ObjectArrayList<>();
		final ObjectArrayList<Box> boxes = new ObjectArrayList<>();
		final ObjectArrayList<ObjectObjectImmutablePair<StoredMatrixTransformations, IntIntImmutablePair>> rawModelDisplayPartsList = new ObjectArrayList<>();

		names.forEach(name -> {
			final NewOptimizedModelGroup newOptimizedModelGroup = nameToNewOptimizedModelGroup.get(name);
			if (newOptimizedModelGroup != null) {
				newOptimizedModelGroups.add(newOptimizedModelGroup);
				boxes.addAll(newOptimizedModelGroup.boxes);
			}

			final ObjectArrayList<ObjectObjectImmutablePair<StoredMatrixTransformations, IntIntImmutablePair>> rawModelDisplayParts = nameToRawModelDisplayParts.get(name);
			if (rawModelDisplayParts != null) {
				rawModelDisplayPartsList.addAll(rawModelDisplayParts);
			}
		});

		final boolean isDoor = isDoor();

		positionDefinitions.forEach(positionDefinitionName -> positionDefinitionsObject.getPositionDefinition(positionDefinitionName, (positions, positionsFlipped) -> {
			switch (type) {
				case NORMAL:
					iteratePositions(positions, positionsFlipped, (x, y, z, flipped) -> {
						if (isDoor) {
							final Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> rawDoorModels = new Object2ObjectOpenHashMap<>();
							final ObjectArrayList<Box> doorBoxes = new ObjectArrayList<>();
							mergeNewOptimizedModelGroups(newOptimizedModelGroups, rawDoorModels, x, -y + modelYOffset, -z, flipped);
							boxes.forEach(box -> doorBoxes.add(addBox(box, x, y + modelYOffset, z, flipped)));
							rawDoorModelDetailsList.add(new RawDoorModelDetails(rawDoorModels, this, doorBoxes, x, y, z, flipped));
						} else {
							// TODO figure out why inconsistent translations are needed
							mergeNewOptimizedModelGroups(newOptimizedModelGroups, rawModels, x, -y + modelYOffset, -z, flipped);
						}
					});
					break;
				case FLOOR:
					iteratePositions(positions, positionsFlipped, (x, y, z, flipped) -> boxes.forEach(box -> floors.add(addBox(box, x, y + modelYOffset, z, flipped))));
					break;
				case DOORWAY:
					iteratePositions(positions, positionsFlipped, (x, y, z, flipped) -> boxes.forEach(box -> doorways.add(addBox(box, x, y + modelYOffset, z, flipped))));
					break;
				case DISPLAY:
					iteratePositions(positions, positionsFlipped, (x, y, z, flipped) -> rawModelDisplayPartsList.forEach(rawModelDisplayPart -> displays
							.computeIfAbsent(condition, key -> new ObjectArrayList<>())
							.add(new ModelDisplayPart(this, rawModelDisplayPart.left(), rawModelDisplayPart.right().leftInt(), rawModelDisplayPart.right().rightInt(), x, -y + modelYOffset, -z, flipped))
					));
					break;
			}
		}));
	}

	public void renderDisplay(StoredMatrixTransformations storedMatrixTransformations, ModelDisplayPart modelDisplayPart, VehicleExtension vehicle, int carNumber, int[] scrollingDisplayIndexTracker, boolean fromResourcePackCreator) {
		if (displayType == DisplayType.ROUTE_COLOR || displayType == DisplayType.ROUTE_COLOR_ROUNDED) {
			renderLineColor(storedMatrixTransformations, modelDisplayPart, vehicle, fromResourcePackCreator);
		} else {
			if (displayOptions.contains(DisplayOption.SEVEN_SEGMENT.toString())) {
				renderSevenSegmentDisplay(storedMatrixTransformations, modelDisplayPart, vehicle);
			} else if (displayOptions.contains(DisplayOption.SCROLL_NORMAL.toString()) || displayOptions.contains(DisplayOption.SCROLL_LIGHT_RAIL.toString())) {
				renderScrollingDisplay(storedMatrixTransformations, modelDisplayPart, vehicle, carNumber, scrollingDisplayIndexTracker);
			} else {
				renderStandardDisplay(storedMatrixTransformations, modelDisplayPart, vehicle);
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

	public StoredMatrixTransformations getDoorOffset(StoredMatrixTransformations storedMatrixTransformations, @Nullable VehicleExtension vehicle, boolean flipped) {
		final float x;
		final float y = flashOnTime + flashOffTime == 0 || (System.currentTimeMillis() % (flashOnTime + flashOffTime)) > flashOffTime ? 0 : Integer.MAX_VALUE;
		final float z;

		final double doorValue = vehicle == null ? 0 : vehicle.persistentVehicleData.getDoorValue();
		final boolean opening = vehicle == null || vehicle.persistentVehicleData.getAdjustedDoorMultiplier(vehicle.vehicleExtraData) > 0;
		final boolean shouldRender;

		if (opening) {
			shouldRender = renderFromOpeningDoorTime == 0 && renderUntilOpeningDoorTime == 0 || Utilities.isBetween(Math.abs(doorValue) * Vehicle.DOOR_MOVE_TIME, renderFromOpeningDoorTime, renderUntilOpeningDoorTime);
		} else {
			shouldRender = renderFromClosingDoorTime == 0 && renderUntilClosingDoorTime == 0 || Utilities.isBetween(Math.abs(doorValue) * Vehicle.DOOR_MOVE_TIME, renderFromClosingDoorTime, renderUntilClosingDoorTime);
		}

		x = shouldRender ? (float) (doorAnimationType.getDoorAnimationX(doorXMultiplier, flipped, doorValue)) : Integer.MAX_VALUE;
		z = shouldRender ? (float) (doorAnimationType.getDoorAnimationZ(doorZMultiplier, !flipped, doorValue, opening)) : Integer.MAX_VALUE;

		final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
		storedMatrixTransformationsNew.add(matrixStack -> matrixStack.translate(x / 16, y / 16, z / 16));
		return storedMatrixTransformationsNew;
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

	private boolean isDoor() {
		return doorXMultiplier != 0 || doorZMultiplier != 0;
	}

	private void renderLineColor(StoredMatrixTransformations storedMatrixTransformations, ModelDisplayPart modelDisplayPart, VehicleExtension vehicle, boolean fromResourcePackCreator) {
		final int color;
		if (fromResourcePackCreator) {
			color = IGui.ARGB_BLACK | rainbowColor();
		} else {
			color = getOrDefault(IGui.ARGB_BLACK | vehicle.vehicleExtraData.getThisRouteColor(), IGui.ARGB_BLACK | vehicle.vehicleExtraData.getNextRouteColor(), IGui.ARGB_BLACK | vehicle.vehicleExtraData.getPreviousRouteColor(), 0, vehicle);
		}

		if (color != 0) {
			MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, String.format("textures/block/%s.png", displayType == DisplayType.ROUTE_COLOR ? "white" : "sign/circle")), true, QueuedRenderLayer.INTERIOR, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				matrixStack.translate(modelDisplayPart.x, modelDisplayPart.y, modelDisplayPart.z);
				IDrawing.rotateYDegrees(matrixStack, modelDisplayPart.flipped ? 180 : 0);
				IDrawing.rotateXDegrees(matrixStack, 180);
				modelDisplayPart.storedMatrixTransformations.transform(matrixStack, Vec3d.ZERO);
				matrixStack.translate(0, 0, -IGui.SMALL_OFFSET);

				final float x1 = (float) displayXPadding / 16;
				final float y1 = (float) displayYPadding / 16;
				final float x2 = (modelDisplayPart.width - (float) displayXPadding) / 16;
				final float y2 = (modelDisplayPart.height - (float) displayYPadding) / 16;
				vertexConsumer.vertex(matrixStack.peek().getPositionMatrix(), x1, y1, 0).color(color).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(IGui.DEFAULT_LIGHT).normal(0, 1, 0);
				vertexConsumer.vertex(matrixStack.peek().getPositionMatrix(), x1, y2, 0).color(color).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(IGui.DEFAULT_LIGHT).normal(0, 1, 0);
				vertexConsumer.vertex(matrixStack.peek().getPositionMatrix(), x2, y2, 0).color(color).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(IGui.DEFAULT_LIGHT).normal(0, 1, 0);
				vertexConsumer.vertex(matrixStack.peek().getPositionMatrix(), x2, y1, 0).color(color).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(IGui.DEFAULT_LIGHT).normal(0, 1, 0);

				matrixStack.pop();
				matrixStack.pop();
			});
		}
	}

	private void renderSevenSegmentDisplay(StoredMatrixTransformations storedMatrixTransformations, ModelDisplayPart modelDisplayPart, VehicleExtension vehicle) {
		final String text = formatText(vehicle);

		if (!text.isEmpty()) {
			final FontRenderOptions.Alignment horizontalAlignment = getHorizontalAlignment(false);
			MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/block/sign/seven_segment.png"), true, QueuedRenderLayer.LIGHT_2, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				matrixStack.translate(modelDisplayPart.x, modelDisplayPart.y, modelDisplayPart.z);
				IDrawing.rotateYDegrees(matrixStack, modelDisplayPart.flipped ? 180 : 0);
				IDrawing.rotateXDegrees(matrixStack, 180);
				modelDisplayPart.storedMatrixTransformations.transform(matrixStack, Vec3d.ZERO);
				matrixStack.translate(0, displayYPadding / 16, -IGui.SMALL_OFFSET);

				IDrawing.drawSevenSegment(
						matrixStack,
						vertexConsumer,
						text,
						(modelDisplayPart.width - (float) displayXPadding * 2) / 16,
						0, 0,
						(modelDisplayPart.height - (float) displayYPadding * 2) / 16,
						horizontalAlignment,
						IGui.ARGB_BLACK | displayColorInt, IGui.DEFAULT_LIGHT
				);

				matrixStack.pop();
				matrixStack.pop();
			});
		}
	}

	private void renderScrollingDisplay(StoredMatrixTransformations storedMatrixTransformations, ModelDisplayPart modelDisplayPart, VehicleExtension vehicle, int carNumber, int[] scrollingDisplayIndexTracker) {
		final String text = formatText(vehicle);
		final ObjectArrayList<ScrollingText> scrollingTexts = vehicle.persistentVehicleData.getScrollingText(carNumber);

		final double width = (modelDisplayPart.width - displayXPadding * 2) / 16F;
		final double height = (modelDisplayPart.height - displayYPadding * 2) / 16F;

		while (scrollingTexts.size() <= scrollingDisplayIndexTracker[0]) {
			scrollingTexts.add(new ScrollingText(width, height, 4, height < 0.1));
		}

		final ScrollingText scrollingText = scrollingTexts.get(scrollingDisplayIndexTracker[0]);
		scrollingText.changeImage(text.isEmpty() ? null : DynamicTextureCache.instance.getPixelatedText(text, IGui.ARGB_BLACK | displayColorInt, Integer.MAX_VALUE, displayCjkSizeRatio, height < 0.1));

		if (scrollingText.getTextureId() != null) {
			MainRenderer.scheduleRender(scrollingText.getTextureId(), true, QueuedRenderLayer.LIGHT_2, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				matrixStack.translate(modelDisplayPart.x, modelDisplayPart.y, modelDisplayPart.z);
				IDrawing.rotateYDegrees(matrixStack, modelDisplayPart.flipped ? 180 : 0);
				IDrawing.rotateXDegrees(matrixStack, 180);
				modelDisplayPart.storedMatrixTransformations.transform(matrixStack, Vec3d.ZERO);
				matrixStack.translate(displayXPadding / 16, displayYPadding / 16, -IGui.SMALL_OFFSET);
				scrollingText.scrollText(matrixStack, vertexConsumer);
				matrixStack.pop();
				matrixStack.pop();
			});
		}

		scrollingDisplayIndexTracker[0]++;
	}

	private void renderStandardDisplay(StoredMatrixTransformations storedMatrixTransformations, ModelDisplayPart modelDisplayPart, VehicleExtension vehicle) {
		final String text = formatText(vehicle);

		if (!text.isEmpty()) {
			MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				matrixStack.translate(modelDisplayPart.x, modelDisplayPart.y, modelDisplayPart.z);
				IDrawing.rotateYDegrees(matrixStack, modelDisplayPart.flipped ? 180 : 0);
				IDrawing.rotateXDegrees(matrixStack, 180);
				modelDisplayPart.storedMatrixTransformations.transform(matrixStack, Vec3d.ZERO);
				matrixStack.translate(displayXPadding / 16, displayYPadding / 16, -IGui.SMALL_OFFSET);

				FontGroups.renderMTR(matrixStack.peek().getPositionMatrix(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui()), text, FontRenderOptions.builder()
						.color(IGui.ARGB_BLACK | displayColorInt)
						.cjkColor(IGui.ARGB_BLACK | displayColorCjkInt)
						.cjkScaling((float) displayCjkSizeRatio)
						.maxFontSize((float) displayMaxLineHeight)
						.horizontalSpace((float) (modelDisplayPart.width - displayXPadding * 2) / 16)
						.horizontalTextAlignment(getHorizontalAlignment(true))
						.verticalSpace((float) (modelDisplayPart.height - displayYPadding * 2) / 16)
						.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
						.build()
				);

				matrixStack.pop();
				matrixStack.pop();
			});
		}
	}

	private void mergeNewOptimizedModelGroups(ObjectArrayList<NewOptimizedModelGroup> newOptimizedModelGroups, Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> partConditionToNewOptimizedModelGroup, double x, double y, double z, boolean flipped) {
		newOptimizedModelGroups.forEach(newOptimizedModelGroup -> partConditionToNewOptimizedModelGroup.computeIfAbsent(condition, key -> new NewOptimizedModelGroup()).merge(newOptimizedModelGroup, renderStage, x, y, z, flipped));
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

	private FontRenderOptions.Alignment getHorizontalAlignment(boolean isCjk) {
		if (isCjk) {
			if (displayOptions.contains(DisplayOption.ALIGN_LEFT_CJK.toString())) {
				return FontRenderOptions.Alignment.START;
			} else if (displayOptions.contains(DisplayOption.ALIGN_RIGHT_CJK.toString())) {
				return FontRenderOptions.Alignment.END;
			} else {
				return FontRenderOptions.Alignment.CENTER;
			}
		} else {
			if (displayOptions.contains(DisplayOption.ALIGN_LEFT.toString())) {
				return FontRenderOptions.Alignment.START;
			} else if (displayOptions.contains(DisplayOption.ALIGN_RIGHT.toString())) {
				return FontRenderOptions.Alignment.END;
			} else {
				return FontRenderOptions.Alignment.CENTER;
			}
		}
	}

	private IGui.VerticalAlignment getVerticalAlignment() {
		if (displayOptions.contains(DisplayOption.ALIGN_TOP.toString())) {
			return IGui.VerticalAlignment.TOP;
		} else if (displayOptions.contains(DisplayOption.ALIGN_BOTTOM.toString())) {
			return IGui.VerticalAlignment.BOTTOM;
		} else {
			return IGui.VerticalAlignment.CENTER;
		}
	}

	private static Box addBox(Box box, double x, double y, double z, boolean flipped) {
		return new Box(
				(flipped ? -1 : 1) * box.minX + x, box.minY + y, (flipped ? 1 : -1) * box.minZ + z,
				(flipped ? -1 : 1) * box.maxX + x, box.maxY + y, (flipped ? 1 : -1) * box.maxZ + z
		);
	}

	private static void iteratePositions(ObjectArrayList<PartPosition> positions, ObjectArrayList<PartPosition> positionsFlipped, PositionCallback positionCallback) {
		positions.forEach(position -> positionCallback.accept(position.getX() / 16, position.getY() / 16, position.getZ() / 16, false));
		positionsFlipped.forEach(position -> positionCallback.accept(-position.getX() / 16, position.getY() / 16, position.getZ() / 16, true));
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
		//		private final OptimizedModelWrapper optimizedModelDoor;
		private final Box box;
		private final double x;
		private final double y;
		private final double z;
		private final boolean flipped;

		private PartDetails(Box box, double x, double y, double z, boolean flipped) {
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

	public record RawDoorModelDetails(
			Object2ObjectOpenHashMap<PartCondition, NewOptimizedModelGroup> rawModels,
			ModelPropertiesPart modelPropertiesPart,
			ObjectArrayList<Box> boxes,
			double x,
			double y,
			double z,
			boolean flipped
	) {
	}

	@FunctionalInterface
	private interface PositionCallback {
		void accept(double x, double y, double z, boolean flipped);
	}
}
