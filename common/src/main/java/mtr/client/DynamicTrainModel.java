package mtr.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.data.*;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.model.ModelTrainBase;
import mtr.screen.ResourcePackCreatorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class DynamicTrainModel extends ModelTrainBase implements IResourcePackCreatorProperties {

	private float doorLeftX;
	private float doorRightX;
	private float doorLeftZ;
	private float doorRightZ;
	private boolean head1IsFront;

	public final Map<String, ModelMapper> parts = new HashMap<>();
	public final Map<String, Set<PartInfo>> partsInfo = new HashMap<>();
	public final JsonObject properties;
	public final int doorMax;
	private final Map<String, Boolean> whitelistBlacklistCache = new HashMap<>();

	public DynamicTrainModel(JsonObject model, JsonObject properties, DoorAnimationType doorAnimationType) {
		super(doorAnimationType, false);

		try {
			final JsonObject resolution = model.getAsJsonObject("resolution");
			final int textureWidth = resolution.get("width").getAsInt();
			final int textureHeight = resolution.get("height").getAsInt();

			final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

			final Map<String, ModelMapper> elementsByKey = new HashMap<>();
			model.getAsJsonArray("elements").forEach(element -> elementsByKey.put(element.getAsJsonObject().get("uuid").getAsString(), new ModelMapper(modelDataWrapper)));

			final Map<String, String> uuidToParentString = new HashMap<>();
			model.getAsJsonArray("outliner").forEach(element -> {
				final JsonObject elementObject = element.getAsJsonObject();
				parts.put(elementObject.get("name").getAsString(), addChildren(elementObject, elementsByKey, uuidToParentString, modelDataWrapper));
			});

			model.getAsJsonArray("elements").forEach(element -> {
				final JsonObject elementObject = element.getAsJsonObject();
				final String uuid = elementObject.get("uuid").getAsString();
				final ModelMapper child = elementsByKey.get(uuid);

				final Double[] origin = {0D, 0D, 0D};
				getArrayFromValue(origin, elementObject, "origin", JsonElement::getAsDouble);
				child.setPos(-origin[0].floatValue(), -origin[1].floatValue(), origin[2].floatValue());

				final Double[] rotation = {0D, 0D, 0D};
				getArrayFromValue(rotation, elementObject, "rotation", JsonElement::getAsDouble);
				setRotationAngle(child, -(float) Math.toRadians(rotation[0]), -(float) Math.toRadians(rotation[1]), (float) Math.toRadians(rotation[2]));

				final Integer[] uvOffset = {0, 0};
				getArrayFromValue(uvOffset, elementObject, "uv_offset", JsonElement::getAsInt);

				final Double[] posFrom = {0D, 0D, 0D};
				getArrayFromValue(posFrom, elementObject, "from", JsonElement::getAsDouble);
				final Double[] posTo = {0D, 0D, 0D};
				getArrayFromValue(posTo, elementObject, "to", JsonElement::getAsDouble);

				final double inflate = elementObject.has("inflate") ? elementObject.get("inflate").getAsDouble() : 0;
				final boolean mirror = elementObject.has("shade") && !elementObject.get("shade").getAsBoolean();

				child.texOffs(uvOffset[0], uvOffset[1]).addBox(
						origin[0].floatValue() - posTo[0].floatValue(), origin[1].floatValue() - posTo[1].floatValue(), posFrom[2].floatValue() - origin[2].floatValue(),
						Math.round(posTo[0].floatValue() - posFrom[0].floatValue()), Math.round(posTo[1].floatValue() - posFrom[1].floatValue()), Math.round(posTo[2].floatValue() - posFrom[2].floatValue()),
						(float) inflate, mirror
				);

				final float width = (float) Math.abs(posTo[0] - posFrom[0]) / 16;
				final float height = (float) Math.abs(posTo[1] - posFrom[1]) / 16;
				if (width > 0 && height > 0) {
					final String parentName = uuidToParentString.get(uuid);
					if (!partsInfo.containsKey(parentName)) {
						partsInfo.put(parentName, new HashSet<>());
					}
					partsInfo.get(parentName).add(new PartInfo(
							origin[0] / 16, origin[1] / 16, origin[2] / 16,
							((posFrom[0] + posTo[0]) / 2 - origin[0]) / 16, ((posFrom[1] + posTo[1]) / 2 - origin[1]) / 16, (posFrom[2] - origin[2]) / 16,
							-rotation[0].floatValue(), -rotation[1].floatValue(), rotation[2].floatValue(),
							width, height
					));
				}
			});

			modelDataWrapper.setModelPart(textureWidth, textureHeight);
			parts.values().forEach(part -> {
				part.setPos(0, 0, 0);
				part.texOffs(0, 0).addBox(0, 0, 0, 0, 0, 0, 0, false);
				part.setModelPart();
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		IResourcePackCreatorProperties.checkSchema(properties);
		this.properties = properties;
		doorMax = properties.get(KEY_PROPERTIES_DOOR_MAX).getAsInt();
	}

	@Override
	protected void render(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, int currentCar, int trainCars, boolean head1IsFront, boolean renderDetails) {
		this.doorLeftX = doorLeftX;
		this.doorRightX = doorRightX;
		this.doorLeftZ = doorLeftZ;
		this.doorRightZ = doorRightZ;
		this.head1IsFront = head1IsFront;

		iterateParts(currentCar, trainCars, partObject -> {
			if (!renderDetails && partObject.get(KEY_PROPERTIES_SKIP_RENDERING_IF_TOO_FAR).getAsBoolean() || !renderStage.toString().equals(partObject.get(KEY_PROPERTIES_STAGE).getAsString().toUpperCase(Locale.ENGLISH)) || shouldSkipRender(partObject)) {
				return;
			}

			final ModelMapper part = parts.get(partObject.get(KEY_PROPERTIES_NAME).getAsString());

			if (part != null) {
				final float xOffset = getOffsetX(partObject);
				final float zOffset = getOffsetZ(partObject);
				final boolean mirror = partObject.get(KEY_PROPERTIES_MIRROR).getAsBoolean();
				partObject.getAsJsonArray(KEY_PROPERTIES_POSITIONS).forEach(positionElement -> {
					final float x = positionElement.getAsJsonArray().get(0).getAsFloat();
					final float z = positionElement.getAsJsonArray().get(1).getAsFloat();
					if (mirror) {
						renderOnceFlipped(part, matrices, vertices, light, x - xOffset, z - zOffset);
					} else {
						renderOnce(part, matrices, vertices, light, x + xOffset, z + zOffset);
					}
				});
			}
		});
	}

	@Override
	protected void renderTextDisplays(PoseStack matrices, MultiBufferSource vertexConsumers, Font font, MultiBufferSource.BufferSource immediate, Route thisRoute, Route nextRoute, Station thisStation, Station nextStation, Station lastStation, String customDestination, int car, int totalCars, boolean atPlatform, List<ScrollingText> scrollingTexts) {
		final int[] scrollIndex = {0};

		iterateParts(car, totalCars, partObject -> {
			if (shouldSkipRender(partObject)) {
				return;
			}

			final String partName = partObject.get(KEY_PROPERTIES_NAME).getAsString();
			if (!partsInfo.containsKey(partName) || !partObject.has(KEY_PROPERTIES_DISPLAY)) {
				return;
			}
			final boolean mirror = partObject.get(KEY_PROPERTIES_MIRROR).getAsBoolean();

			partObject.getAsJsonArray(KEY_PROPERTIES_POSITIONS).forEach(positionElement -> {
				final float x = positionElement.getAsJsonArray().get(0).getAsFloat() + getOffsetX(partObject);
				final float z = positionElement.getAsJsonArray().get(1).getAsFloat() + getOffsetZ(partObject);
				final String destinationString = getDestinationString(lastStation, customDestination, TextSpacingType.NORMAL, false);

				final JsonObject displayObject = partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY);
				final int colorCjk = CustomResources.colorStringToInt(displayObject.get(KEY_PROPERTIES_DISPLAY_COLOR_CJK).getAsString()) | ARGB_BLACK;
				final int color = CustomResources.colorStringToInt(displayObject.get(KEY_PROPERTIES_DISPLAY_COLOR).getAsString()) | ARGB_BLACK;
				final float cjkSizeRatio = displayObject.get(KEY_PROPERTIES_DISPLAY_CJK_SIZE_RATIO).getAsFloat();
				final boolean shouldScroll = displayObject.get(KEY_PROPERTIES_DISPLAY_SHOULD_SCROLL).getAsBoolean();
				final ResourcePackCreatorProperties.DisplayType displayType = EnumHelper.valueOf(ResourcePackCreatorProperties.DisplayType.DESTINATION, displayObject.get(KEY_PROPERTIES_DISPLAY_TYPE).getAsString());

				final String tempText1;
				final Screen screen = Minecraft.getInstance().screen;

				if (screen instanceof ResourcePackCreatorScreen) {
					final String testText = ((ResourcePackCreatorScreen) screen).getTestText();
					final Station testStation = new Station();
					final Route testRoute = new Route(TransportMode.TRAIN);
					testStation.name = testText;
					testRoute.name = testText;

					switch (displayType) {
						case DESTINATION:
						case ROUTE_NUMBER:
						case NEXT_STATION_PLAIN:
							tempText1 = testText;
							break;
						case NEXT_STATION_KCR:
							tempText1 = getHongKongNextStationString(testStation, testStation, atPlatform, true);
							break;
						case NEXT_STATION_MTR:
							tempText1 = getHongKongNextStationString(testStation, testStation, atPlatform, false);
							break;
						case NEXT_STATION_UK:
							tempText1 = getLondonNextStationString(testRoute, testRoute, testStation, testStation, testStation, testText, atPlatform);
							break;
						default:
							tempText1 = "";
							break;
					}
				} else {
					switch (displayType) {
						case DESTINATION:
							tempText1 = destinationString;
							break;
						case ROUTE_NUMBER:
							tempText1 = thisRoute == null ? "" : thisRoute.lightRailRouteNumber;
							break;
						case NEXT_STATION_PLAIN:
							final Station station = atPlatform ? thisStation : nextStation;
							tempText1 = station == null ? Text.translatable("gui.mtr.untitled").getString() : station.name;
							break;
						case NEXT_STATION_KCR:
							tempText1 = getHongKongNextStationString(thisStation, nextStation, atPlatform, true);
							break;
						case NEXT_STATION_MTR:
							tempText1 = getHongKongNextStationString(thisStation, nextStation, atPlatform, false);
							break;
						case NEXT_STATION_UK:
							tempText1 = getLondonNextStationString(thisRoute, nextRoute, thisStation, nextStation, lastStation, destinationString, atPlatform);
							break;
						default:
							tempText1 = "";
							break;
					}
				}

				final String tempText2 = displayObject.get(KEY_PROPERTIES_DISPLAY_FORCE_UPPER_CASE).getAsBoolean() ? tempText1.toUpperCase(Locale.ENGLISH) : tempText1;
				final String text = displayObject.get(KEY_PROPERTIES_DISPLAY_FORCE_SINGLE_LINE).getAsBoolean() ? IGui.formatStationName(tempText2) : tempText2;

				partsInfo.get(partName).forEach(partInfo -> {
					final float width = partInfo.width - displayObject.get(KEY_PROPERTIES_DISPLAY_X_PADDING).getAsFloat();
					final float height = partInfo.height - displayObject.get(KEY_PROPERTIES_DISPLAY_Y_PADDING).getAsFloat();
					while (shouldScroll && scrollingTexts.size() <= scrollIndex[0]) {
						scrollingTexts.add(new ScrollingText(width, height, 4, height < 0.2));
					}

					matrices.pushPose();
					matrices.translate(x / 16, 0, z / 16);
					UtilitiesClient.rotateYDegrees(matrices, mirror ? 180 : 0);
					matrices.translate(-partInfo.originX, -partInfo.originY, partInfo.originZ);
					UtilitiesClient.rotateZDegrees(matrices, partInfo.rotationZ);
					UtilitiesClient.rotateYDegrees(matrices, partInfo.rotationY);
					UtilitiesClient.rotateXDegrees(matrices, partInfo.rotationX);
					matrices.translate(-partInfo.offsetX, -partInfo.offsetY, partInfo.offsetZ - SMALL_OFFSET);

					if (shouldScroll) {
						matrices.translate(-width / 2, -height / 2, 0);
						scrollingTexts.get(scrollIndex[0]).changeImage(text.isEmpty() ? null : ClientData.DATA_CACHE.getPixelatedText(text, color, Integer.MAX_VALUE, cjkSizeRatio, height < 0.2));
						scrollingTexts.get(scrollIndex[0]).setVertexConsumer(vertexConsumers);
						scrollingTexts.get(scrollIndex[0]).scrollText(matrices);
						scrollIndex[0]++;
					} else {
						IDrawing.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, text, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, 0, 0, width, height, 1, colorCjk, color, cjkSizeRatio < 0 ? 1 / (1 - cjkSizeRatio) : 1 + cjkSizeRatio, false, MAX_LIGHT_GLOWING, null);
					}

					matrices.popPose();
				});
			});
		});
	}

	@Override
	public int getDoorMax() {
		return doorMax;
	}

	private ModelMapper addChildren(JsonObject jsonObject, Map<String, ModelMapper> children, Map<String, String> uuidToParentString, ModelDataWrapper modelDataWrapper) {
		final ModelMapper part = new ModelMapper(modelDataWrapper);
		jsonObject.getAsJsonArray("children").forEach(child -> {
			final boolean hasMoreChildren = child.isJsonObject();
			if (hasMoreChildren) {
				part.addChild(addChildren(child.getAsJsonObject(), children, uuidToParentString, modelDataWrapper));
			} else {
				part.addChild(children.get(child.getAsString()));
				uuidToParentString.put(child.getAsString(), jsonObject.get("name").getAsString());
			}
		});
		return part;
	}

	private int matchesFilter(String[] filters, int currentCar, int trainCars) {
		int strength = filters.length == 0 ? 1 : 0;

		for (final String filter : filters) {
			if (!filter.isEmpty()) {
				if (filter.contains("%")) {
					try {
						final String[] filterSplit = filter.split("\\+");
						final int multiple = Integer.parseInt(filterSplit[0].replace("%", ""));
						final int additional = filterSplit.length == 1 ? 0 : Integer.parseInt(filterSplit[1]);
						if ((currentCar + 1 + additional) % multiple == 0) {
							strength = 2;
						}
					} catch (Exception ignored) {
					}
				} else {
					try {
						final int car = Integer.parseInt(filter);
						if (car == currentCar + 1 || car == currentCar - trainCars) {
							return 3;
						}
					} catch (Exception ignored) {
					}
				}
			}
		}

		return strength;
	}

	private static <T> void getArrayFromValue(T[] array, JsonObject jsonObject, String key, Function<JsonElement, T> function) {
		if (jsonObject.has(key)) {
			final JsonArray jsonArray = jsonObject.getAsJsonArray(key);
			for (int i = 0; i < array.length; i++) {
				array[i] = function.apply(jsonArray.get(i));
			}
		}
	}

	private void iterateParts(int currentCar, int trainCars, Consumer<JsonObject> callback) {
		properties.getAsJsonArray(KEY_PROPERTIES_PARTS).forEach(partElement -> {
			final JsonObject partObject = partElement.getAsJsonObject();
			final String whitelistedCars = partObject.get(KEY_PROPERTIES_WHITELISTED_CARS).getAsString();
			final String blacklistedCars = partObject.get(KEY_PROPERTIES_BLACKLISTED_CARS).getAsString();
			final String key = String.format("%s|%s|%s|%s", trainCars, currentCar, whitelistedCars, blacklistedCars);
			final boolean skip;
			if (whitelistBlacklistCache.containsKey(key)) {
				skip = whitelistBlacklistCache.get(key);
			} else {
				final String[] whitelistedCarsFilters = whitelistedCars.split(",");
				final String[] blacklistedCarsFilters = blacklistedCars.split(",");
				skip = matchesFilter(blacklistedCarsFilters, currentCar, trainCars) > matchesFilter(whitelistedCarsFilters, currentCar, trainCars);
				whitelistBlacklistCache.put(key, skip);
			}

			if (skip) {
				return;
			}

			callback.accept(partObject);
		});
	}

	private boolean shouldSkipRender(JsonObject partObject) {
		switch (EnumHelper.valueOf(ResourcePackCreatorProperties.RenderCondition.ALL, partObject.get(KEY_PROPERTIES_RENDER_CONDITION).getAsString())) {
			case DOORS_OPEN:
				return doorLeftZ == 0 && doorRightZ == 0;
			case DOORS_CLOSED:
				return doorLeftZ > 0 || doorRightZ > 0;
			case DOOR_LEFT_OPEN:
				return doorLeftZ == 0;
			case DOOR_RIGHT_OPEN:
				return doorRightZ == 0;
			case DOOR_LEFT_CLOSED:
				return doorLeftZ > 0;
			case DOOR_RIGHT_CLOSED:
				return doorRightZ > 0;
			case MOVING_FORWARDS:
				return !head1IsFront;
			case MOVING_BACKWARDS:
				return head1IsFront;
			default:
				return false;
		}
	}

	private float getOffsetX(JsonObject partObject) {
		switch (EnumHelper.valueOf(ResourcePackCreatorProperties.DoorOffset.NONE, partObject.get(KEY_PROPERTIES_DOOR_OFFSET).getAsString())) {
			case LEFT_POSITIVE:
			case LEFT_NEGATIVE:
				return -doorLeftX;
			case RIGHT_POSITIVE:
			case RIGHT_NEGATIVE:
				return doorRightX;
			default:
				return 0;
		}
	}

	private float getOffsetZ(JsonObject partObject) {
		switch (EnumHelper.valueOf(ResourcePackCreatorProperties.DoorOffset.NONE, partObject.get(KEY_PROPERTIES_DOOR_OFFSET).getAsString())) {
			case LEFT_POSITIVE:
				return doorLeftZ;
			case RIGHT_POSITIVE:
				return doorRightZ;
			case LEFT_NEGATIVE:
				return -doorLeftZ;
			case RIGHT_NEGATIVE:
				return -doorRightZ;
			default:
				return 0;
		}
	}

	private static class PartInfo {

		private final double originX;
		private final double originY;
		private final double originZ;
		private final double offsetX;
		private final double offsetY;
		private final double offsetZ;
		private final float rotationX;
		private final float rotationY;
		private final float rotationZ;
		private final float width;
		private final float height;

		private PartInfo(double originX, double originY, double originZ, double offsetX, double offsetY, double offsetZ, float rotationX, float rotationY, float rotationZ, float width, float height) {
			this.originX = originX;
			this.originY = originY;
			this.originZ = originZ;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
			this.offsetZ = offsetZ;
			this.rotationX = rotationX;
			this.rotationY = rotationY;
			this.rotationZ = rotationZ;
			this.width = width;
			this.height = height;
		}
	}
}
