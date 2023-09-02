package org.mtr.mod.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2BooleanAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.core.data.EnumHelper;
import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.data.IGui;
import org.mtr.mod.model.ModelTrainBase;
import org.mtr.mod.screen.ResourcePackCreatorScreen;

import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;

public class DynamicTrainModel extends ModelTrainBase implements IResourcePackCreatorProperties {

	private float doorLeftX;
	private float doorRightX;
	private float doorLeftZ;
	private float doorRightZ;
	private boolean head1IsFront;

	private final Object2ObjectAVLTreeMap<String, ModelPartExtension> parts = new Object2ObjectAVLTreeMap<>();
	private final Object2ObjectAVLTreeMap<String, ObjectOpenHashSet<PartInfo>> partsInfo = new Object2ObjectAVLTreeMap<>();
	private final JsonObject properties;
	private final int doorMax;
	private final Object2BooleanAVLTreeMap<String> whitelistBlacklistCache = new Object2BooleanAVLTreeMap<>();

	public DynamicTrainModel(JsonObject model, JsonObject properties, DoorAnimationType doorAnimationType) {
		super(tryWrapper(() -> model.getAsJsonObject("resolution").get("width").getAsInt()), tryWrapper(() -> model.getAsJsonObject("resolution").get("height").getAsInt()), doorAnimationType, false);

		try {
			final Object2ObjectAVLTreeMap<String, ModelPartExtension> elementsByKey = new Object2ObjectAVLTreeMap<>();
			model.getAsJsonArray("elements").forEach(element -> elementsByKey.put(element.getAsJsonObject().get("uuid").getAsString(), createModelPart()));

			final Object2ObjectAVLTreeMap<String, String> uuidToParentString = new Object2ObjectAVLTreeMap<>();
			model.getAsJsonArray("outliner").forEach(element -> {
				final JsonObject elementObject = element.getAsJsonObject();
				parts.put(elementObject.get("name").getAsString(), addChildren(elementObject, elementsByKey, uuidToParentString));
			});

			model.getAsJsonArray("elements").forEach(element -> {
				final JsonObject elementObject = element.getAsJsonObject();
				final String uuid = elementObject.get("uuid").getAsString();
				final ModelPartExtension child = elementsByKey.get(uuid);

				final Double[] origin = {0D, 0D, 0D};
				getArrayFromValue(origin, elementObject, "origin", JsonElement::getAsDouble);
				child.setPivot(-origin[0].floatValue(), -origin[1].floatValue(), origin[2].floatValue());

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

				child.setTextureUVOffset(uvOffset[0], uvOffset[1]).addCuboid(
						origin[0].floatValue() - posTo[0].floatValue(), origin[1].floatValue() - posTo[1].floatValue(), posFrom[2].floatValue() - origin[2].floatValue(),
						Math.round(posTo[0].floatValue() - posFrom[0].floatValue()), Math.round(posTo[1].floatValue() - posFrom[1].floatValue()), Math.round(posTo[2].floatValue() - posFrom[2].floatValue()),
						(float) inflate, mirror
				);

				final float width = (float) Math.abs(posTo[0] - posFrom[0]) / 16;
				final float height = (float) Math.abs(posTo[1] - posFrom[1]) / 16;
				if (width > 0 && height > 0) {
					final String parentName = uuidToParentString.get(uuid);
					if (!partsInfo.containsKey(parentName)) {
						partsInfo.put(parentName, new ObjectOpenHashSet<>());
					}
					partsInfo.get(parentName).add(new PartInfo(
							origin[0] / 16, origin[1] / 16, origin[2] / 16,
							((posFrom[0] + posTo[0]) / 2 - origin[0]) / 16, ((posFrom[1] + posTo[1]) / 2 - origin[1]) / 16, (posFrom[2] - origin[2]) / 16,
							-rotation[0].floatValue(), -rotation[1].floatValue(), rotation[2].floatValue(),
							width, height
					));
				}
			});

			parts.values().forEach(part -> {
				part.setPivot(0, 0, 0);
				part.setTextureUVOffset(0, 0).addCuboid(0, 0, 0, 0, 0, 0, 0, false);
			});

			buildModel();
		} catch (Exception e) {
			e.printStackTrace();
		}

		IResourcePackCreatorProperties.checkSchema(properties);
		this.properties = properties;
		doorMax = properties.get(KEY_PROPERTIES_DOOR_MAX).getAsInt();
	}

	@Override
	protected void render(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, int currentCar, int trainCars, boolean head1IsFront, boolean renderDetails) {
		this.doorLeftX = doorLeftX;
		this.doorRightX = doorRightX;
		this.doorLeftZ = doorLeftZ;
		this.doorRightZ = doorRightZ;
		this.head1IsFront = head1IsFront;

		iterateParts(currentCar, trainCars, partObject -> {
			if (!renderDetails && partObject.get(KEY_PROPERTIES_SKIP_RENDERING_IF_TOO_FAR).getAsBoolean() || !renderStage.toString().equals(partObject.get(KEY_PROPERTIES_STAGE).getAsString().toUpperCase(Locale.ENGLISH)) || shouldSkipRender(partObject)) {
				return;
			}

			final ModelPartExtension part = parts.get(partObject.get(KEY_PROPERTIES_NAME).getAsString());

			if (part != null) {
				final float xOffset = getOffsetX(partObject);
				final float zOffset = getOffsetZ(partObject);
				final boolean mirror = partObject.get(KEY_PROPERTIES_MIRROR).getAsBoolean();
				partObject.getAsJsonArray(KEY_PROPERTIES_POSITIONS).forEach(positionElement -> {
					final float x = positionElement.getAsJsonArray().get(0).getAsFloat();
					final float z = positionElement.getAsJsonArray().get(1).getAsFloat();
					if (mirror) {
						renderOnceFlipped(part, graphicsHolder, light, x - xOffset, z - zOffset);
					} else {
						renderOnce(part, graphicsHolder, light, x + xOffset, z + zOffset);
					}
				});
			}
		});
	}

	@Override
	protected void renderTextDisplays(GraphicsHolder graphicsHolder, int thisRouteColor, String thisRouteName, String thisRouteNumber, String thisStationName, String thisRouteDestination, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, int car, int totalCars, boolean atPlatform, boolean isTerminating, ObjectArrayList<ScrollingText> scrollingTexts) {
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
				final String destinationString = getDestinationString(thisRouteDestination, TextSpacingType.NORMAL, false);

				final JsonObject displayObject = partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY);
				final int colorCjk = CustomResources.colorStringToInt(displayObject.get(KEY_PROPERTIES_DISPLAY_COLOR_CJK).getAsString()) | ARGB_BLACK;
				final int color = CustomResources.colorStringToInt(displayObject.get(KEY_PROPERTIES_DISPLAY_COLOR).getAsString()) | ARGB_BLACK;
				final float cjkSizeRatio = displayObject.get(KEY_PROPERTIES_DISPLAY_CJK_SIZE_RATIO).getAsFloat();
				final boolean shouldScroll = displayObject.get(KEY_PROPERTIES_DISPLAY_SHOULD_SCROLL).getAsBoolean();
				final ResourcePackCreatorProperties.DisplayType displayType = EnumHelper.valueOf(ResourcePackCreatorProperties.DisplayType.DESTINATION, displayObject.get(KEY_PROPERTIES_DISPLAY_TYPE).getAsString());

				final String tempText1;
				final Screen screen = MinecraftClient.getInstance().getCurrentScreenMapped();

				if (screen != null && screen.data instanceof ResourcePackCreatorScreen) {
					final String testText = ((ResourcePackCreatorScreen) screen.data).getTestText();

					switch (displayType) {
						case DESTINATION:
						case ROUTE_NUMBER:
						case NEXT_STATION_PLAIN:
							tempText1 = testText;
							break;
						case NEXT_STATION_KCR:
							tempText1 = getHongKongNextStationString(testText, testText, atPlatform, true);
							break;
						case NEXT_STATION_MTR:
							tempText1 = getHongKongNextStationString(testText, testText, atPlatform, false);
							break;
						case NEXT_STATION_UK:
							tempText1 = getLondonNextStationString(testText, testText, testText, consumer -> {
							}, testText, atPlatform, false);
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
							tempText1 = thisRouteNumber;
							break;
						case NEXT_STATION_PLAIN:
							tempText1 = IGui.textOrUntitled(atPlatform ? thisStationName : nextStationName);
							break;
						case NEXT_STATION_KCR:
							tempText1 = getHongKongNextStationString(nextStationName, nextStationName, atPlatform, true);
							break;
						case NEXT_STATION_MTR:
							tempText1 = getHongKongNextStationString(nextStationName, nextStationName, atPlatform, false);
							break;
						case NEXT_STATION_UK:
							tempText1 = getLondonNextStationString(thisRouteName, thisStationName, nextStationName, getInterchanges, destinationString, atPlatform, isTerminating);
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

					graphicsHolder.push();
					graphicsHolder.translate(x / 16, 0, z / 16);
					graphicsHolder.rotateYDegrees(mirror ? 180 : 0);
					graphicsHolder.translate(-partInfo.originX, -partInfo.originY, partInfo.originZ);
					graphicsHolder.rotateZDegrees(partInfo.rotationZ);
					graphicsHolder.rotateYDegrees(partInfo.rotationY);
					graphicsHolder.rotateXDegrees(partInfo.rotationX);
					graphicsHolder.translate(-partInfo.offsetX, -partInfo.offsetY, partInfo.offsetZ - SMALL_OFFSET);

					if (shouldScroll) {
						graphicsHolder.translate(-width / 2, -height / 2, 0);
						scrollingTexts.get(scrollIndex[0]).changeImage(text.isEmpty() ? null : DynamicTextureCache.instance.getPixelatedText(text, color, Integer.MAX_VALUE, cjkSizeRatio, height < 0.2));
						scrollingTexts.get(scrollIndex[0]).createVertexConsumer(graphicsHolder);
						scrollingTexts.get(scrollIndex[0]).scrollText(graphicsHolder);
						scrollIndex[0]++;
					} else {
						IDrawing.drawStringWithFont(graphicsHolder, text, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, 0, 0, width, height, 1, colorCjk, color, cjkSizeRatio < 0 ? 1 / (1 - cjkSizeRatio) : 1 + cjkSizeRatio, false, MAX_LIGHT_GLOWING, null);
					}

					graphicsHolder.pop();
				});
			});
		});
	}

	@Override
	public int getDoorMax() {
		return doorMax;
	}

	private ModelPartExtension addChildren(JsonObject jsonObject, Object2ObjectAVLTreeMap<String, ModelPartExtension> children, Object2ObjectAVLTreeMap<String, String> uuidToParentString) {
		final ModelPartExtension part = createModelPart();
		jsonObject.getAsJsonArray("children").forEach(child -> {
			final boolean hasMoreChildren = child.isJsonObject();
			if (hasMoreChildren) {
				part.addChild(addChildren(child.getAsJsonObject(), children, uuidToParentString));
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
				skip = whitelistBlacklistCache.getBoolean(key);
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

	private static int tryWrapper(IntSupplier supplier) {
		try {
			return supplier.getAsInt();
		} catch (Exception ignored) {
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
