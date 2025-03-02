package org.mtr.legacy.resource;

import org.mtr.core.data.TransportMode;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.JsonWriter;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.legacy.generated.resource.VehicleResourceSchema;
import org.mtr.libraries.com.google.gson.JsonArray;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.resource.ResourceProvider;
import org.mtr.mod.resource.VehicleModel;
import org.mtr.mod.resource.VehicleResource;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.stream.Collectors;

public final class LegacyVehicleResource extends VehicleResourceSchema {

	public LegacyVehicleResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public void convert(ObjectArrayList<VehicleResource> vehicleResources, String id, ResourceProvider resourceProvider) {
		for (int i = 0; i < Variation.values().length; i++) {
			final Variation variation = Variation.values()[i];
			final VehicleResource[] baseVehicleResource = {null};
			for (final TransportMode transportMode : TransportMode.values()) {
				final String newBaseTrainType;
				if (transportMode == TransportMode.CABLE_CAR || base_train_type.startsWith("light_rail")) {
					newBaseTrainType = base_train_type.endsWith("_rht") || base_train_type.endsWith("_lht") ? base_train_type : base_train_type + "_lht";
				} else {
					newBaseTrainType = String.format("%s_%s", base_train_type, variation.key);
				}
				CustomResourceLoader.getVehicleById(transportMode, newBaseTrainType, vehicleResourceDetails -> baseVehicleResource[0] = vehicleResourceDetails.left());
				if (baseVehicleResource[0] != null) {
					break;
				}
			}

			final JsonObject baseObject = new JsonObject();
			if (baseVehicleResource[0] != null) {
				baseVehicleResource[0].serializeData(new JsonWriter(baseObject));
			}

			baseObject.addProperty("id", String.format("mtr_custom_train_%s_%s", id, variation.key));
			baseObject.addProperty("name", name + variation.description);
			baseObject.addProperty("color", color);

			if (!description.isEmpty()) {
				baseObject.addProperty("description", description);
			}

			if (!wikipedia_article.isEmpty()) {
				baseObject.addProperty("wikipediaArticle", wikipedia_article);
			}

			if(!bve_sound_base_id.isEmpty()) {
				baseObject.addProperty("bveSoundBaseResource", bve_sound_base_id);
			}

			if(!speed_sound_base_id.isEmpty()) {
				baseObject.addProperty("legacySpeedSoundBaseResource", speed_sound_base_id);
			}

			if(!door_sound_base_id.isEmpty()) {
				baseObject.addProperty("legacyDoorSoundBaseResource", door_sound_base_id);
			}

			if(speed_sound_count != 0) {
				baseObject.addProperty("legacySpeedSoundCount", speed_sound_count);
			}

			if(accel_sound_at_coast) {
				baseObject.addProperty("legacyUseAccelerationSoundsWhenCoasting", accel_sound_at_coast);
			}

			if(const_playback_speed) {
				baseObject.addProperty("legacyConstantPlaybackSpeed", const_playback_speed);
			}

			baseObject.addProperty("legacyDoorCloseSoundTime", door_close_sound_time == 0 ? 0.5 : door_close_sound_time); // MTR 3 defaults to 0.5s if not specified

			if (baseVehicleResource[0] == null) {
				final JsonObject propertiesObject = model_properties.isEmpty() ? null : Utilities.parseJson(resourceProvider.get(new Identifier(model_properties)));
				if (propertiesObject == null) {
					continue;
				}

				double length = 0;
				double width = 0;
				double doorMax = 0;

				try {
					baseObject.addProperty("transportMode", propertiesObject.get("transport_mode").getAsString());
					length = propertiesObject.get("length").getAsDouble() + 1;
					width = propertiesObject.get("width").getAsDouble();
				} catch (Exception ignored1) {
					try {
						final String[] baseTrainTypeSplit = (base_train_type.startsWith("base_") ? base_train_type.replace("base_", "train_") : base_train_type).toUpperCase(Locale.ENGLISH).replace("CABLE_CAR", "CABLE-CAR").split("_");
						baseObject.addProperty("transportMode", baseTrainTypeSplit[0].replace("CABLE-CAR", "CABLE_CAR"));
						length = Double.parseDouble(baseTrainTypeSplit[1]) + 1;
						width = Double.parseDouble(baseTrainTypeSplit[2]);
					} catch (Exception ignored2) {
					}
				}

				try {
					doorMax = propertiesObject.get("door_max").getAsDouble();
				} catch (Exception ignored) {
				}

				if (length == 0 || width == 0) {
					continue;
				}

				baseObject.addProperty("length", length);
				baseObject.addProperty("width", width);
				final double bogiePosition = length < 10 ? 0 : length * 0.34;
				baseObject.addProperty("bogie1Position", -bogiePosition);
				baseObject.addProperty("bogie2Position", bogiePosition);
				baseObject.addProperty("couplingPadding1", (i & 0b01) == 0 ? 0 : 1);
				baseObject.addProperty("couplingPadding2", (i & 0b10) == 0 ? 0 : 1);
				baseObject.addProperty("hasGangway1", (!gangway_connection_id.isEmpty() || has_gangway_connection) && (i & 0b01) == 0);
				baseObject.addProperty("hasGangway2", (!gangway_connection_id.isEmpty() || has_gangway_connection) && (i & 0b10) == 0);
				baseObject.addProperty("hasBarrier1", !train_barrier_id.isEmpty() && (i & 0b01) == 0);
				baseObject.addProperty("hasBarrier2", !train_barrier_id.isEmpty() && (i & 0b10) == 0);
				baseObject.addProperty("legacyRiderOffset", rider_offset);

				final double finalDoorMax = doorMax;
				vehicleResources.add(new VehicleResource(
						new JsonReader(baseObject),
						(currentCar, totalCars) -> {
							boolean isObj = false;
							boolean reversed = false;
							final ObjectArrayList<JsonObject> modelObjects = new ObjectArrayList<>();
							final String[] modelSplit = splitWithEmptyStrings(model, '|');
							for (int j = 0; j < modelSplit.length; j += 2) {
								final String[] conditions = j + 1 < modelSplit.length ? splitWithEmptyStrings(modelSplit[j + 1], ';') : new String[]{};
								if (conditions.length < 2 || matchesFilter(conditions[1].split(","), currentCar, totalCars) <= matchesFilter(conditions[0].split(","), currentCar, totalCars)) {
									final JsonObject modelObject = new JsonObject();
									modelObject.addProperty("modelResource", modelSplit[j]);
									modelObject.addProperty("textureResource", texture_id);
									modelObject.addProperty("flipTextureV", flipV);
									modelObjects.add(modelObject);
									if (modelSplit[j].endsWith(".obj")) {
										isObj = true;
									}
									if (conditions.length >= 3 && conditions[2].equals("reversed")) {
										reversed = true;
									}
								}
							}

							final JsonArray positionDefinitionsArray = new JsonArray();
							final JsonArray partsArray = new JsonArray();

							try {
								processModel(currentCar, totalCars, isObj, reversed, propertiesObject.getAsJsonArray("parts_normal"), positionDefinitionsArray, partsArray, finalDoorMax, "NORMAL", null, null);
							} catch (Exception ignored) {
							}
							try {
								processModel(currentCar, totalCars, isObj, reversed, propertiesObject.getAsJsonArray("parts_head_1"), positionDefinitionsArray, partsArray, finalDoorMax, "NORMAL", "1", "%1");
							} catch (Exception ignored) {
							}
							try {
								processModel(currentCar, totalCars, isObj, reversed, propertiesObject.getAsJsonArray("parts_head_2"), positionDefinitionsArray, partsArray, finalDoorMax, "NORMAL", "-1", "%1");
							} catch (Exception ignored) {
							}
							try {
								processModel(currentCar, totalCars, isObj, reversed, propertiesObject.getAsJsonArray("parts_head_1_headlights"), positionDefinitionsArray, partsArray, finalDoorMax, "ON_ROUTE_FORWARDS", "1", "%1");
							} catch (Exception ignored) {
							}
							try {
								processModel(currentCar, totalCars, isObj, reversed, propertiesObject.getAsJsonArray("parts_head_2_headlights"), positionDefinitionsArray, partsArray, finalDoorMax, "ON_ROUTE_BACKWARDS", "-1", "%1");
							} catch (Exception ignored) {
							}
							try {
								processModel(currentCar, totalCars, isObj, reversed, propertiesObject.getAsJsonArray("parts_head_1_tail_lights"), positionDefinitionsArray, partsArray, finalDoorMax, "ON_ROUTE_BACKWARDS", "1", "%1");
							} catch (Exception ignored) {
							}
							try {
								processModel(currentCar, totalCars, isObj, reversed, propertiesObject.getAsJsonArray("parts_head_2_tail_lights"), positionDefinitionsArray, partsArray, finalDoorMax, "ON_ROUTE_FORWARDS", "-1", "%1");
							} catch (Exception ignored) {
							}
							try {
								processModel(currentCar, totalCars, isObj, reversed, propertiesObject.getAsJsonArray("parts_end_1"), positionDefinitionsArray, partsArray, finalDoorMax, "NORMAL", "%1", "1");
							} catch (Exception ignored) {
							}
							try {
								processModel(currentCar, totalCars, isObj, reversed, propertiesObject.getAsJsonArray("parts_end_2"), positionDefinitionsArray, partsArray, finalDoorMax, "NORMAL", "%1", "-1");
							} catch (Exception ignored) {
							}
							try {
								processModel(currentCar, totalCars, isObj, reversed, propertiesObject.getAsJsonArray("parts_door_opened"), positionDefinitionsArray, partsArray, finalDoorMax, "DOORS_OPENED", null, null);
							} catch (Exception ignored) {
							}
							try {
								processModel(currentCar, totalCars, isObj, reversed, propertiesObject.getAsJsonArray("parts_door_closed"), positionDefinitionsArray, partsArray, finalDoorMax, "DOORS_CLOSED", null, null);
							} catch (Exception ignored) {
							}
							try {
								processModel(currentCar, totalCars, isObj, reversed, propertiesObject.getAsJsonArray("parts"), positionDefinitionsArray, partsArray, finalDoorMax, null, null, null);
							} catch (Exception ignored) {
							}

							final JsonObject modelPropertiesObject = new JsonObject();
							modelPropertiesObject.addProperty("modelYOffset", 1);
							modelPropertiesObject.addProperty("gangwayInnerSideResource", (gangway_connection_id.isEmpty() ? texture_id : gangway_connection_id) + "_connector_side.png");
							modelPropertiesObject.addProperty("gangwayInnerTopResource", (gangway_connection_id.isEmpty() ? texture_id : gangway_connection_id) + "_connector_roof.png");
							modelPropertiesObject.addProperty("gangwayInnerBottomResource", (gangway_connection_id.isEmpty() ? texture_id : gangway_connection_id) + "_connector_floor.png");
							modelPropertiesObject.addProperty("gangwayOuterSideResource", (gangway_connection_id.isEmpty() ? texture_id : gangway_connection_id) + "_connector_exterior.png");
							modelPropertiesObject.addProperty("gangwayOuterTopResource", (gangway_connection_id.isEmpty() ? texture_id : gangway_connection_id) + "_connector_exterior.png");
							modelPropertiesObject.addProperty("gangwayOuterBottomResource", (gangway_connection_id.isEmpty() ? texture_id : gangway_connection_id) + "_connector_exterior.png");
							modelPropertiesObject.addProperty("gangwayWidth", 1.5);
							modelPropertiesObject.addProperty("gangwayHeight", 2.25);
							modelPropertiesObject.addProperty("gangwayYOffset", 1);
							modelPropertiesObject.addProperty("gangwayZOffset", 0.5);
							modelPropertiesObject.addProperty("barrierInnerSideResource", train_barrier_id + "_exterior.png");
							modelPropertiesObject.addProperty("barrierOuterSideResource", train_barrier_id + "_exterior.png");
							modelPropertiesObject.addProperty("barrierWidth", 2.25);
							modelPropertiesObject.addProperty("barrierHeight", 1);
							modelPropertiesObject.addProperty("barrierYOffset", 1.25);
							modelPropertiesObject.addProperty("barrierZOffset", 0.25);
							modelPropertiesObject.add("parts", partsArray);

							final JsonObject positionDefinitionsObject = new JsonObject();
							positionDefinitionsObject.add("positionDefinitions", positionDefinitionsArray);
							return modelObjects.stream().map(modelObject -> new VehicleModel(new JsonReader(modelObject), new JsonReader(modelPropertiesObject), new JsonReader(positionDefinitionsObject), id, resourceProvider)).collect(Collectors.toCollection(ObjectArrayList::new));
						},
						resourceProvider
				));
			} else {
				baseObject.getAsJsonArray("models").forEach(jsonElement -> {
					final JsonObject modelObject = jsonElement.getAsJsonObject();
					if (!tryGet(modelObject, "textureResource").contains("overlay")) {
						modelObject.addProperty("textureResource", texture_id);
					}
				});

				vehicleResources.add(new VehicleResource(new JsonReader(baseObject), resourceProvider));
			}
		}
	}

	private void processModel(int currentCar, int totalCars, boolean isObj, boolean reversed, JsonArray propertiesPartsArray, JsonArray positionDefinitionsArray, JsonArray partsArray, double doorMax, @Nullable String renderConditionOverride, @Nullable String whitelistedCarsOverride, @Nullable String blacklistedCarsOverride) {
		propertiesPartsArray.forEach(jsonElement -> {
			final JsonObject propertiesPartsObject = jsonElement.getAsJsonObject();
			final String[] whitelistedCarsFilters = (whitelistedCarsOverride == null ? tryGet(propertiesPartsObject, "whitelisted_cars") : whitelistedCarsOverride).split(",");
			final String[] blacklistedCarsFilters = (blacklistedCarsOverride == null ? tryGet(propertiesPartsObject, "blacklisted_cars") : blacklistedCarsOverride).split(",");

			if (matchesFilter(blacklistedCarsFilters, currentCar, totalCars) <= matchesFilter(whitelistedCarsFilters, currentCar, totalCars)) {
				final JsonObject partsObject = new JsonObject();
				partsArray.add(partsObject);

				if (propertiesPartsObject.has("name")) {
					addSingleArrayItem(partsObject, "names", tryGet(propertiesPartsObject, "name"));
				} else {
					addSingleArrayItem(partsObject, "names", tryGet(propertiesPartsObject, "part_name"));
				}

				partsObject.addProperty("renderStage", tryGet(propertiesPartsObject, "stage").toUpperCase(Locale.ENGLISH));
				partsObject.addProperty("doorAnimationType", door_animation_type);

				if (renderConditionOverride == null) {
					final String renderCondition;
					switch (tryGet(propertiesPartsObject, "render_condition").toUpperCase(Locale.ENGLISH)) {
						case "DOORS_OPEN":
						case "DOORS_LEFT_OPEN":
						case "DOORS_RIGHT_OPEN":
							renderCondition = "DOORS_OPENED";
							break;
						case "DOORS_CLOSED":
						case "DOORS_LEFT_CLOSED":
						case "DOORS_RIGHT_CLOSED":
							renderCondition = "DOORS_CLOSED";
							break;
						case "MOVING_FORWARDS":
							renderCondition = "ON_ROUTE_FORWARDS";
							break;
						case "MOVING_BACKWARDS":
							renderCondition = "ON_ROUTE_BACKWARDS";
							break;
						default:
							renderCondition = "NORMAL";
							break;
					}
					partsObject.addProperty("condition", renderCondition);
				} else {
					partsObject.addProperty("condition", renderConditionOverride);
				}

				if (propertiesPartsObject.has("display")) {
					partsObject.addProperty("type", "DISPLAY");

					final JsonObject displayObject = propertiesPartsObject.getAsJsonObject("display");
					partsObject.addProperty("displayXPadding", tryGetNumber(displayObject, "x_padding"));
					partsObject.addProperty("displayYPadding", tryGetNumber(displayObject, "y_padding"));
					partsObject.addProperty("displayCjkSizeRatio", tryGetNumber(displayObject, "cjk_size_ratio"));
					partsObject.addProperty("displayColorCjk", tryGet(displayObject, "color_cjk"));
					partsObject.addProperty("displayColor", tryGet(displayObject, "color"));
					partsObject.addProperty("displayPadZeros", tryGetNumber(displayObject, "display_pad_zeros"));

					final String displayType = tryGet(displayObject, "type");
					partsObject.addProperty("displayType", "NEXT_STATION_PLAIN".equals(displayType) ? "NEXT_STATION" : displayType);

					final JsonArray displayOptionsArray = new JsonArray();
					partsObject.add("displayOptions", displayOptionsArray);
					if (tryGetBoolean(displayObject, "should_scroll")) {
						displayOptionsArray.add("SCROLL_NORMAL");
					}
					if (tryGetBoolean(displayObject, "force_upper_case")) {
						displayOptionsArray.add("UPPER_CASE");
					}
					if (tryGetBoolean(displayObject, "force_single_line")) {
						displayOptionsArray.add("SINGLE_LINE");
					}
				} else {
					partsObject.addProperty("type", "NORMAL");
				}

				final boolean mirror = tryGetBoolean(propertiesPartsObject, "mirror");
				final double doorXMultiplier;
				final double doorZMultiplier;
				if (propertiesPartsObject.has("door_offset")) {
					switch (tryGet(propertiesPartsObject, "door_offset").toUpperCase(Locale.ENGLISH)) {
						case "LEFT_NEGATIVE":
							doorXMultiplier = mirror ? -1 : 1;
							doorZMultiplier = -doorMax;
							break;
						case "RIGHT_NEGATIVE":
							doorXMultiplier = mirror ? 1 : -1;
							doorZMultiplier = -doorMax;
							break;
						case "LEFT_POSITIVE":
							doorXMultiplier = mirror ? -1 : 1;
							doorZMultiplier = doorMax;
							break;
						case "RIGHT_POSITIVE":
							doorXMultiplier = mirror ? 1 : -1;
							doorZMultiplier = doorMax;
							break;
						default:
							doorXMultiplier = 0;
							doorZMultiplier = 0;
							break;
					}
				} else {
					switch (tryGet(propertiesPartsObject, "door_offset_x").toUpperCase(Locale.ENGLISH)) {
						case "LEFT":
						case "LEFT_NEGATIVE":
						case "RIGHT":
						case "RIGHT_NEGATIVE":
							doorXMultiplier = -1;
							break;
						default:
							doorXMultiplier = 0;
							break;
					}
					switch (tryGet(propertiesPartsObject, "door_offset_z").toUpperCase(Locale.ENGLISH)) {
						case "LEFT_NEGATIVE":
						case "RIGHT_NEGATIVE":
							doorZMultiplier = -doorMax;
							break;
						case "LEFT":
						case "RIGHT":
							doorZMultiplier = doorMax;
							break;
						default:
							doorZMultiplier = 0;
							break;
					}
				}
				partsObject.addProperty("doorXMultiplier", doorXMultiplier);
				partsObject.addProperty("doorZMultiplier", (isObj ? -1 : 1) * doorZMultiplier);

				final String positionDefinitionName = "definition_" + Init.randomString();
				final JsonArray positionDefinitionPositionsArray = new JsonArray();
				final JsonArray positionDefinitionPositionsFlippedArray = new JsonArray();
				final JsonObject positionDefinitionObject = new JsonObject();
				positionDefinitionObject.addProperty("name", positionDefinitionName);
				positionDefinitionObject.add("positions", reversed ? positionDefinitionPositionsFlippedArray : positionDefinitionPositionsArray);
				positionDefinitionObject.add("positionsFlipped", reversed ? positionDefinitionPositionsArray : positionDefinitionPositionsFlippedArray);
				positionDefinitionsArray.add(positionDefinitionObject);

				processPositions(propertiesPartsObject, "positions", reversed, mirror ? positionDefinitionPositionsFlippedArray : positionDefinitionPositionsArray);
				processPositions(propertiesPartsObject, "positions_flipped", reversed, positionDefinitionPositionsFlippedArray);

				addSingleArrayItem(partsObject, "positionDefinitions", positionDefinitionName);
			}
		});
	}

	private static void processPositions(JsonObject propertiesPartsObject, String property, boolean reversed, JsonArray positionDefinitionPositionsArray) {
		try {
			propertiesPartsObject.getAsJsonArray(property).forEach(jsonElement -> {
				final JsonObject positionDefinitionObject = new JsonObject();
				positionDefinitionObject.addProperty("x", jsonElement.getAsJsonArray().get(0).getAsDouble());
				positionDefinitionObject.addProperty("z", jsonElement.getAsJsonArray().get(1).getAsDouble() * (reversed ? -1 : 1));
				positionDefinitionPositionsArray.add(positionDefinitionObject);
			});
		} catch (Exception ignored) {
		}
	}

	private static void addSingleArrayItem(JsonObject jsonObject, String property, String string) {
		final JsonArray jsonArray = new JsonArray();
		jsonArray.add(string);
		jsonObject.add(property, jsonArray);
	}

	private static String tryGet(JsonObject jsonObject, String property) {
		try {
			return jsonObject.get(property).getAsString();
		} catch (Exception ignored) {
			return "";
		}
	}

	private static double tryGetNumber(JsonObject jsonObject, String property) {
		try {
			return jsonObject.get(property).getAsDouble();
		} catch (Exception ignored) {
			return 0;
		}
	}

	private static boolean tryGetBoolean(JsonObject jsonObject, String property) {
		try {
			return jsonObject.has(property) && jsonObject.get(property).getAsBoolean();
		} catch (Exception ignored) {
			return false;
		}
	}

	private static int matchesFilter(String[] filters, int currentCar, int totalCars) {
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
						if (car == currentCar + 1 || car == currentCar - totalCars) {
							return 3;
						}
					} catch (Exception ignored) {
					}
				}
			}
		}

		return strength;
	}

	private static String[] splitWithEmptyStrings(String string, char token) {
		final String filler = Init.randomString();
		final String[] firstSplit = string.replace(String.valueOf(token), String.format("%1$s%2$s%1$s", filler, token)).split(("\\") + token);
		final String[] finalSplit = new String[firstSplit.length];
		for (int i = 0; i < firstSplit.length; i++) {
			finalSplit[i] = firstSplit[i].replace(filler, "");
		}
		return finalSplit;
	}

	private enum Variation {
		TRAILER(" (Trailer)"), CAB_1(" Cab (Forwards)"), CAB_2(" Cab (Backwards)"), CAB_3(" Cab (Double)");

		private final String key;
		private final String description;

		Variation(String description) {
			key = toString().toLowerCase(Locale.ENGLISH);
			this.description = description;
		}
	}
}
