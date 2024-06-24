package org.mtr.mod.data;

import org.mtr.core.data.Data;
import org.mtr.core.data.Vehicle;
import org.mtr.core.operation.VehicleUpdate;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.core.tool.Vector;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.Items;
import org.mtr.mod.block.BlockTrainAnnouncer;
import org.mtr.mod.block.BlockTrainRedstoneSensor;
import org.mtr.mod.block.BlockTrainSensorBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.client.VehicleRidingMovement;
import org.mtr.mod.packet.PacketTurnOnBlockEntity;
import org.mtr.mod.resource.VehicleResource;

import javax.annotation.Nullable;
import java.util.Locale;

public class VehicleExtension extends Vehicle implements Utilities {

	private double oldSpeed;

	public final PersistentVehicleData persistentVehicleData;

	public VehicleExtension(VehicleUpdate vehicleUpdate, Data data) {
		super(vehicleUpdate.getVehicleExtraData(), null, new JsonReader(Utilities.getJsonObjectFromData(vehicleUpdate.getVehicle())), data);
		final PersistentVehicleData tempPersistentVehicleData = MinecraftClientData.getInstance().vehicleIdToPersistentVehicleData.get(getId());
		if (tempPersistentVehicleData == null) {
			persistentVehicleData = new PersistentVehicleData(vehicleExtraData.immutableVehicleCars);
			MinecraftClientData.getInstance().vehicleIdToPersistentVehicleData.put(getId(), persistentVehicleData);
		} else {
			persistentVehicleData = tempPersistentVehicleData;
		}
	}

	public void updateData(@Nullable JsonObject jsonObject) {
		if (jsonObject != null) {
			updateData(new JsonReader(jsonObject.getAsJsonObject("vehicle")));
			vehicleExtraData.updateData(new JsonReader(jsonObject.getAsJsonObject("data")));
		}
	}

	public void simulate(long millisElapsed) {
		final double oldRailProgress = railProgress;
		oldSpeed = speed;
		simulate(millisElapsed, null, null);
		persistentVehicleData.tick(railProgress, millisElapsed, vehicleExtraData);
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();

		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		final int thisRouteColor = vehicleExtraData.getThisRouteColor();
		final String thisRouteName = formatRouteName(vehicleExtraData.getThisRouteName());
		final int nextRouteColor = vehicleExtraData.getNextRouteColor();
		final String nextRouteName = formatRouteName(vehicleExtraData.getNextRouteName());
		final String thisStationName = vehicleExtraData.getThisStationName();
		final String nextStationName = vehicleExtraData.getNextStationName();
		final String thisRouteDestination = vehicleExtraData.getThisRouteDestination();
		final String nextRouteDestination = vehicleExtraData.getNextRouteDestination();
		final long thisRouteId = vehicleExtraData.getThisRouteId();

		if (VehicleRidingMovement.getRidingVehicleCarNumberAndOffset(id) != null) {
			// Render client action bar floating text
			if (VehicleRidingMovement.showShiftProgressBar() && (!isCurrentlyManual || !isHoldingKey(clientPlayerEntity))) {
				if (speed * MILLIS_PER_SECOND > 5 || thisRouteName.isEmpty() || thisStationName.isEmpty() || thisRouteDestination.isEmpty()) {
					clientPlayerEntity.sendMessage(new Text(TextHelper.translatable("gui.mtr.vehicle_speed", Utilities.round(speed * MILLIS_PER_SECOND, 1), Utilities.round(speed * 3.6F * MILLIS_PER_SECOND, 1)).data), true);
				} else {
					final MutableText text;
					switch ((int) ((System.currentTimeMillis() / 1000) % 3)) {
						default:
							text = getStationText(thisStationName, "this");
							break;
						case 1:
							if (nextStationName.isEmpty()) {
								text = getStationText(thisStationName, "this");
							} else {
								text = getStationText(nextStationName, "next");
							}
							break;
						case 2:
							text = getStationText(thisRouteDestination, "last_" + transportMode.toString().toLowerCase(Locale.ENGLISH));
							break;
					}
					clientPlayerEntity.sendMessage(new Text(text.data), true);
				}
			}

			// TODO chat announcements (next station, route number, etc.)
			if (persistentVehicleData.canAnnounce(oldRailProgress, railProgress)) {
				final ObjectArrayList<String> narrateText = new ObjectArrayList<>();
				final ObjectArrayList<MutableText> chatText = new ObjectArrayList<>();

				if (!nextStationName.isEmpty()) {
					final String nextStationFormatted = IGui.insertTranslation("gui.mtr.next_station_announcement_cjk", "gui.mtr.next_station_announcement", 1, nextStationName);
					narrateText.add(nextStationFormatted);
					chatText.add(TextHelper.literal(IGui.formatStationName(nextStationFormatted)));
				}

				final ObjectArrayList<String> narrateTextThisStation = new ObjectArrayList<>();
				final ObjectArrayList<String> narrateTextOtherStations = new ObjectArrayList<>();
				final ObjectArrayList<MutableText> chatTextThisStation = new ObjectArrayList<>();
				final ObjectArrayList<MutableText> chatTextOtherStations = new ObjectArrayList<>();

				vehicleExtraData.iterateInterchanges((stationName, interchangeColors) -> {
					final ObjectArrayList<String> combinedRouteNames = new ObjectArrayList<>();
					final ObjectArrayList<String> globalVisitedRouteNames = new ObjectArrayList<>();
					final boolean isThisStation = stationName.equals(nextStationName);
					final boolean[] addedStationName = {false};

					interchangeColors.forEach((color, routeNames) -> {
						final ObjectArrayList<String> visitedRouteNames = new ObjectArrayList<>();

						routeNames.forEach(routeName -> {
							final String routeNameFormatted = formatRouteName(routeName);
							if (!routeName.isEmpty() && !visitedRouteNames.contains(routeNameFormatted) && (color != thisRouteColor || !routeNameFormatted.equals(thisRouteName)) && (color != nextRouteColor || !routeNameFormatted.equals(nextRouteName))) {
								if (!isThisStation && !addedStationName[0]) {
									chatTextOtherStations.add(TextHelper.literal(IGui.formatStationName(IGui.insertTranslation("gui.mtr.connecting_station_announcement_cjk", "gui.mtr.connecting_station_announcement", 1, stationName))));
								}

								if (!globalVisitedRouteNames.contains(routeNameFormatted)) {
									combinedRouteNames.add(routeNameFormatted);
								}

								(isThisStation ? chatTextThisStation : chatTextOtherStations).add(TextHelper.append(
										TextHelper.setStyle(TextHelper.literal("-"), Style.getEmptyMapped().withColor(TextColor.fromRgb(color))),
										TextHelper.setStyle(TextHelper.literal(" " + IGui.formatStationName(routeNameFormatted)), Style.getEmptyMapped().withColor(TextFormatting.getWhiteMapped()))
								));

								addedStationName[0] = true;
								globalVisitedRouteNames.add(routeNameFormatted);
								visitedRouteNames.add(routeNameFormatted);
							}
						});
					});

					if (addedStationName[0]) {
						if (isThisStation) {
							narrateTextThisStation.add(IGui.insertTranslation("gui.mtr.interchange_announcement_cjk", "gui.mtr.interchange_announcement", 1, getInterchangeText(combinedRouteNames)));
						} else {
							narrateTextOtherStations.add(IGui.insertTranslation("gui.mtr.connecting_station_part_cjk", "gui.mtr.connecting_station_part", 1, IGui.insertTranslation("gui.mtr.connecting_station_interchange_announcement_part_cjk", "gui.mtr.connecting_station_interchange_announcement_part", 2, getInterchangeText(combinedRouteNames), stationName)));
						}
					}
				});

				narrateText.addAll(narrateTextThisStation);
				narrateText.addAll(narrateTextOtherStations);
				chatText.addAll(chatTextThisStation);
				chatText.addAll(chatTextOtherStations);

				if (!nextRouteName.isEmpty() && (nextRouteColor != thisRouteColor || !nextRouteName.equals(thisRouteName))) {
					final String changeRouteText = IGui.insertTranslation("gui.mtr.next_route_train_announcement_cjk", "gui.mtr.next_route_train_announcement", 2, nextRouteName, nextRouteDestination);
					chatText.add(TextHelper.append(
							TextHelper.setStyle(TextHelper.literal("*"), Style.getEmptyMapped().withColor(TextColor.fromRgb(nextRouteColor))),
							TextHelper.setStyle(TextHelper.literal(" " + IGui.formatStationName(changeRouteText)), Style.getEmptyMapped().withColor(TextFormatting.getWhiteMapped()))
					));
					narrateText.add(changeRouteText);
				}

				IDrawing.narrateOrAnnounce(IGui.formatStationName(IGui.mergeStations(narrateText, " ", " ")), chatText);
			}
		}

		// Check for sensors
		final Vector headPosition = getHeadPosition();
		for (int xOffset = -1; xOffset <= 1; xOffset++) {
			for (int yOffset = -1; yOffset <= 1; yOffset++) {
				for (int zOffset = -1; zOffset <= 1; zOffset++) {
					final BlockPos offsetBlockPos = Init.newBlockPos(headPosition.x + xOffset, headPosition.y + yOffset, headPosition.z + zOffset);
					final BlockState blockState = clientWorld.getBlockState(offsetBlockPos);
					final Block block = blockState.getBlock();
					if (BlockTrainSensorBase.matchesFilter(new World(clientWorld.data), offsetBlockPos, thisRouteId, speed)) {
						if (block.data instanceof BlockTrainRedstoneSensor && IBlock.getStatePropertySafe(blockState, BlockTrainRedstoneSensor.POWERED) < 2) {
							InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketTurnOnBlockEntity(offsetBlockPos));
						} else if (block.data instanceof BlockTrainAnnouncer) {
							// TODO check if player is riding
							final BlockEntity blockEntity = clientWorld.getBlockEntity(offsetBlockPos);
							if (blockEntity != null && blockEntity.data instanceof BlockTrainAnnouncer.BlockEntity) {
								((BlockTrainAnnouncer.BlockEntity) blockEntity.data).announce(new PlayerEntity(clientPlayerEntity.data));
							}
						}
					}
				}
			}
		}
	}

	public void playMotorSound(VehicleResource vehicleResource, int carNumber, int bogieIndex, Vector bogiePosition) {
		persistentVehicleData.playMotorSound(vehicleResource, carNumber, bogieIndex, Init.newBlockPos(bogiePosition.x, bogiePosition.y, bogiePosition.z), (float) speed, (float) (speed - oldSpeed), (float) vehicleExtraData.getAcceleration(), getIsOnRoute());
	}

	public void playDoorSound(VehicleResource vehicleResource, int carNumber, Vector vehiclePosition) {
		persistentVehicleData.playDoorSound(vehicleResource, carNumber, Init.newBlockPos(vehiclePosition.x, vehiclePosition.y, vehiclePosition.z));
	}

	public static boolean isHoldingKey(@Nullable ClientPlayerEntity clientPlayerEntity) {
		return clientPlayerEntity != null && clientPlayerEntity.isHolding(Items.DRIVER_KEY.get());
	}

	private static MutableText getStationText(String text, String textKey) {
		return TextHelper.literal(text.isEmpty() ? "" : IGui.formatStationName(IGui.insertTranslation(String.format("gui.mtr.%s_station_cjk", textKey), String.format("gui.mtr.%s_station", textKey), 1, IGui.textOrUntitled(text))));
	}

	private static String formatRouteName(String routeName) {
		return routeName.split("\\|\\|")[0];
	}

	private static String getInterchangeText(ObjectArrayList<String> names) {
		final ObjectArrayList<String> newNamesCjk = new ObjectArrayList<>();
		final ObjectArrayList<String> newNames = new ObjectArrayList<>();
		names.forEach(name -> {
			for (final String nameSplit : name.split("\\|")) {
				(IGui.isCjk(nameSplit) ? newNamesCjk : newNames).add(nameSplit);
			}
		});
		final String combinedCjk = mergeNames(newNamesCjk, "gui.mtr.comma_cjk", "gui.mtr.comma_last_cjk");
		final String combined = mergeNames(newNames, "gui.mtr.comma", "gui.mtr.comma_last");
		return String.format("%s%s%s", combinedCjk, !combinedCjk.isEmpty() && !combined.isEmpty() ? "|" : "", combined);
	}

	private static String mergeNames(ObjectArrayList<String> names, String keyComma, String keyCommaLast) {
		if (names.isEmpty()) {
			return "";
		} else {
			final StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < names.size(); i++) {
				stringBuilder.append(names.get(i));
				if (i <= names.size() - 3) {
					stringBuilder.append(TextHelper.translatable(keyComma).getString());
				} else if (i == names.size() - 2) {
					stringBuilder.append(TextHelper.translatable(keyCommaLast).getString());
				}
			}
			return stringBuilder.toString();
		}
	}
}
