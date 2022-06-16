package mtr.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.data.EnumHelper;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import mtr.model.ModelTrainBase;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DynamicTrainModel extends ModelTrainBase implements IResourcePackCreatorProperties {

	private final Map<String, ModelMapper> parts = new HashMap<>();
	private final JsonObject properties;
	private final int doorMax;
	private final Map<String, Boolean> whitelistBlacklistCache = new HashMap<>();

	public DynamicTrainModel(JsonObject model, JsonObject properties) {
		try {
			final JsonObject resolution = model.getAsJsonObject("resolution");
			final int textureWidth = resolution.get("width").getAsInt();
			final int textureHeight = resolution.get("height").getAsInt();

			final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

			final Map<String, ModelMapper> elementsByKey = new HashMap<>();
			model.getAsJsonArray("elements").forEach(element -> elementsByKey.put(element.getAsJsonObject().get("uuid").getAsString(), new ModelMapper(modelDataWrapper)));

			model.getAsJsonArray("outliner").forEach(element -> {
				final JsonObject elementObject = element.getAsJsonObject();
				parts.put(elementObject.get("name").getAsString(), addChildren(elementObject, elementsByKey, modelDataWrapper));
			});

			model.getAsJsonArray("elements").forEach(element -> {
				final JsonObject elementObject = element.getAsJsonObject();
				final ModelMapper child = elementsByKey.get(elementObject.get("uuid").getAsString());

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
		properties.getAsJsonArray(KEY_PROPERTIES_PARTS).forEach(partElement -> {
			final JsonObject partObject = partElement.getAsJsonObject();
			if (!renderDetails && partObject.get(KEY_PROPERTIES_SKIP_RENDERING_IF_TOO_FAR).getAsBoolean() || !renderStage.toString().equals(partObject.get(KEY_PROPERTIES_STAGE).getAsString().toUpperCase())) {
				return;
			}

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

			final boolean skipRender;
			switch (EnumHelper.valueOf(ResourcePackCreatorProperties.RenderCondition.ALL, partObject.get(KEY_PROPERTIES_RENDER_CONDITION).getAsString())) {
				case DOORS_OPEN:
					skipRender = doorLeftZ == 0 && doorRightZ == 0;
					break;
				case DOORS_CLOSED:
					skipRender = doorLeftZ > 0 || doorRightZ > 0;
					break;
				case DOOR_LEFT_OPEN:
					skipRender = doorLeftZ == 0;
					break;
				case DOOR_RIGHT_OPEN:
					skipRender = doorRightZ == 0;
					break;
				case DOOR_LEFT_CLOSED:
					skipRender = doorLeftZ > 0;
					break;
				case DOOR_RIGHT_CLOSED:
					skipRender = doorRightZ > 0;
					break;
				case MOVING_FORWARDS:
					skipRender = !head1IsFront;
					break;
				case MOVING_BACKWARDS:
					skipRender = head1IsFront;
					break;
				default:
					skipRender = false;
					break;
			}
			if (skipRender) {
				return;
			}

			final ModelMapper part = parts.get(partObject.get(KEY_PROPERTIES_NAME).getAsString());

			if (part != null) {
				final float zOffset;
				switch (EnumHelper.valueOf(ResourcePackCreatorProperties.DoorOffset.NONE, partObject.get(KEY_PROPERTIES_DOOR_OFFSET).getAsString())) {
					case LEFT_POSITIVE:
						zOffset = doorLeftZ;
						break;
					case RIGHT_POSITIVE:
						zOffset = doorRightZ;
						break;
					case LEFT_NEGATIVE:
						zOffset = -doorLeftZ;
						break;
					case RIGHT_NEGATIVE:
						zOffset = -doorRightZ;
						break;
					default:
						zOffset = 0;
						break;
				}

				final boolean mirror = partObject.get(KEY_PROPERTIES_MIRROR).getAsBoolean();
				partObject.getAsJsonArray(KEY_PROPERTIES_POSITIONS).forEach(positionElement -> {
					final float x = positionElement.getAsJsonArray().get(0).getAsFloat();
					final float z = positionElement.getAsJsonArray().get(1).getAsFloat();
					if (mirror) {
						renderOnceFlipped(part, matrices, vertices, light, x, z - zOffset);
					} else {
						renderOnce(part, matrices, vertices, light, x, z + zOffset);
					}
				});
			}
		});
	}

	protected int[] getBogiePositions() {
		return new int[]{}; // TODO
	}

	@Override
	protected float getDoorAnimationX(float value, boolean opening) {
		return 0;
	}

	@Override
	protected float getDoorAnimationZ(float value, boolean opening) {
		return smoothEnds(0, doorMax, 0, 0.5F, value);
	}

	private ModelMapper addChildren(JsonObject jsonObject, Map<String, ModelMapper> children, ModelDataWrapper modelDataWrapper) {
		final ModelMapper part = new ModelMapper(modelDataWrapper);
		jsonObject.getAsJsonArray("children").forEach(child -> part.addChild(child.isJsonObject() ? addChildren(child.getAsJsonObject(), children, modelDataWrapper) : children.get(child.getAsString())));
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
}
