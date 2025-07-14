package org.mtr.mod.data;

import org.mtr.core.data.Data;
import org.mtr.core.data.PathData;
import org.mtr.core.data.Vehicle;
import org.mtr.core.data.VehicleCar;
import org.mtr.core.operation.VehicleUpdate;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.core.tool.Vector;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.doubles.DoubleObjectImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockTrainAnnouncer;
import org.mtr.mod.block.BlockTrainRedstoneSensor;
import org.mtr.mod.block.BlockTrainSensorBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.client.VehicleRidingMovement;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketCheckRouteIdHasDisabledAnnouncements;
import org.mtr.mod.packet.PacketTurnOnBlockEntity;
import org.mtr.mod.render.DrivingGuiRenderer;
import org.mtr.mod.resource.VehicleResource;

import javax.annotation.Nullable;

public class VehicleExtension extends Vehicle implements Utilities {

	private double oldSpeed;
	private int speedLimitKilometersPerHour;
	@Nullable
	private DoubleObjectImmutablePair<DoubleDoubleImmutablePair> platformStoppingDetails;

	public final PersistentVehicleData persistentVehicleData;

	public VehicleExtension(VehicleUpdate vehicleUpdate, Data data) {
		super(vehicleUpdate.getVehicleExtraData(), null, new JsonReader(Utilities.getJsonObjectFromData(vehicleUpdate.getVehicle())), data);
		final PersistentVehicleData tempPersistentVehicleData = MinecraftClientData.getInstance().vehicleIdToPersistentVehicleData.get(getId());
		if (tempPersistentVehicleData == null) {
			persistentVehicleData = new PersistentVehicleData(vehicleExtraData.immutableVehicleCars, getTransportMode());
			MinecraftClientData.getInstance().vehicleIdToPersistentVehicleData.put(getId(), persistentVehicleData);
		} else {
			persistentVehicleData = tempPersistentVehicleData;
			persistentVehicleData.update(railProgress, vehicleExtraData.getTotalVehicleLength());
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

		if (VehicleRidingMovement.isRiding(id)) {
			// Render client action bar floating text
			if (VehicleRidingMovement.showShiftProgressBar()) {
				if (speed * MILLIS_PER_SECOND > 5 || thisRouteName.isEmpty() || thisStationName.isEmpty() || thisRouteDestination.isEmpty()) {
					clientPlayerEntity.sendMessage(TranslationProvider.GUI_MTR_VEHICLE_SPEED.getText(Utilities.round(speed * MILLIS_PER_SECOND, 1), Utilities.round(speed * 3.6F * MILLIS_PER_SECOND, 1)), true);
				} else {
					final MutableText text;
					switch ((int) ((System.currentTimeMillis() / 1000) % 3)) {
						default:
							text = getStationText(thisStationName, TranslationProvider.GUI_MTR_THIS_STATION_CJK, TranslationProvider.GUI_MTR_THIS_STATION);
							break;
						case 1:
							if (nextStationName.isEmpty()) {
								text = getStationText(thisStationName, TranslationProvider.GUI_MTR_THIS_STATION_CJK, TranslationProvider.GUI_MTR_THIS_STATION);
							} else {
								text = getStationText(nextStationName, TranslationProvider.GUI_MTR_NEXT_STATION_CJK, TranslationProvider.GUI_MTR_NEXT_STATION);
							}
							break;
						case 2:
							switch (transportMode) {
								case TRAIN:
									text = getStationText(thisRouteDestination, TranslationProvider.GUI_MTR_LAST_TRAIN_STATION_CJK, TranslationProvider.GUI_MTR_LAST_TRAIN_STATION);
									break;
								case BOAT:
									text = getStationText(thisRouteDestination, TranslationProvider.GUI_MTR_LAST_BOAT_STATION_CJK, TranslationProvider.GUI_MTR_LAST_BOAT_STATION);
									break;
								case CABLE_CAR:
									text = getStationText(thisRouteDestination, TranslationProvider.GUI_MTR_LAST_CABLE_CAR_STATION_CJK, TranslationProvider.GUI_MTR_LAST_CABLE_CAR_STATION);
									break;
								case AIRPLANE:
									text = getStationText(thisRouteDestination, TranslationProvider.GUI_MTR_LAST_AIRPLANE_STATION_CJK, TranslationProvider.GUI_MTR_LAST_AIRPLANE_STATION);
									break;
								default:
									text = TextHelper.literal("");
							}
							break;
					}
					clientPlayerEntity.sendMessage(new Text(text.data), true);
				}
			}

			// TODO chat announcements (next station, route number, etc.)
			if (persistentVehicleData.canAnnounce(oldRailProgress, railProgress)) {
				InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketCheckRouteIdHasDisabledAnnouncements(thisRouteId, routeIdHasDisabledAnnouncements -> {
					if (!routeIdHasDisabledAnnouncements) {
						final ObjectArrayList<String> narrateText = new ObjectArrayList<>();
						final ObjectArrayList<MutableText> chatText = new ObjectArrayList<>();

						if (!nextStationName.isEmpty()) {
							final String nextStationFormatted = IGui.insertTranslation(TranslationProvider.GUI_MTR_NEXT_STATION_ANNOUNCEMENT_CJK, TranslationProvider.GUI_MTR_NEXT_STATION_ANNOUNCEMENT, 1, nextStationName);
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
											chatTextOtherStations.add(TextHelper.literal(IGui.formatStationName(IGui.insertTranslation(TranslationProvider.GUI_MTR_CONNECTING_STATION_ANNOUNCEMENT_CJK, TranslationProvider.GUI_MTR_CONNECTING_STATION_ANNOUNCEMENT, 1, stationName))));
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
									narrateTextThisStation.add(IGui.insertTranslation(TranslationProvider.GUI_MTR_INTERCHANGE_ANNOUNCEMENT_CJK, TranslationProvider.GUI_MTR_INTERCHANGE_ANNOUNCEMENT, 1, getInterchangeText(combinedRouteNames)));
								} else {
									narrateTextOtherStations.add(IGui.insertTranslation(TranslationProvider.GUI_MTR_CONNECTING_STATION_PART_CJK, TranslationProvider.GUI_MTR_CONNECTING_STATION_PART, 1, IGui.insertTranslation(TranslationProvider.GUI_MTR_CONNECTING_STATION_INTERCHANGE_ANNOUNCEMENT_PART_CJK, TranslationProvider.GUI_MTR_CONNECTING_STATION_INTERCHANGE_ANNOUNCEMENT_PART, 2, getInterchangeText(combinedRouteNames), stationName)));
								}
							}
						});

						narrateText.addAll(narrateTextThisStation);
						narrateText.addAll(narrateTextOtherStations);
						chatText.addAll(chatTextThisStation);
						chatText.addAll(chatTextOtherStations);

						if (!nextRouteName.isEmpty() && (nextRouteColor != thisRouteColor || !nextRouteName.equals(thisRouteName))) {
							final String changeRouteText = IGui.insertTranslation(TranslationProvider.GUI_MTR_NEXT_ROUTE_TRAIN_ANNOUNCEMENT_CJK, TranslationProvider.GUI_MTR_NEXT_ROUTE_TRAIN_ANNOUNCEMENT, 2, nextRouteName, nextRouteDestination);
							chatText.add(TextHelper.append(
									TextHelper.setStyle(TextHelper.literal("*"), Style.getEmptyMapped().withColor(TextColor.fromRgb(nextRouteColor))),
									TextHelper.setStyle(TextHelper.literal(" " + IGui.formatStationName(changeRouteText)), Style.getEmptyMapped().withColor(TextFormatting.getWhiteMapped()))
							));
							narrateText.add(changeRouteText);
						}

						IDrawing.narrateOrAnnounce(IGui.formatStationName(IGui.mergeStations(narrateText, " ", " ")), chatText);
					}
				}));
			}

			DrivingGuiRenderer.setVehicle(this);
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
						} else if (block.data instanceof BlockTrainAnnouncer && VehicleRidingMovement.isRiding(id)) {
							final BlockEntity blockEntity = clientWorld.getBlockEntity(offsetBlockPos);
							if (blockEntity != null && blockEntity.data instanceof BlockTrainAnnouncer.BlockEntity) {
								((BlockTrainAnnouncer.BlockEntity) blockEntity.data).announce();
							}
						}
					}
				}
			}
		}

		// Oscillation
		double totalLength = 0;
		for (int i = 0; i < vehicleExtraData.immutableVehicleCars.size(); i++) {
			final int currentIndex = Utilities.getIndexFromConditionalList(vehicleExtraData.immutablePath, oldRailProgress - totalLength);
			if (currentIndex >= 0 && currentIndex < vehicleExtraData.immutablePath.size()) {
				final int carNumber = reversed ? vehicleExtraData.immutableVehicleCars.size() - i - 1 : i;

				// When moving
				if (speed * MILLIS_PER_SECOND > 5 && Math.random() < 0.01) {
					persistentVehicleData.getOscillation(carNumber).startOscillation(Math.sqrt(speed) * Math.random());
				}

				// When passing node
				if (railProgress - totalLength >= vehicleExtraData.immutablePath.get(currentIndex).getEndDistance()) {
					persistentVehicleData.getOscillation(carNumber).startOscillation(Math.sqrt(speed) * 2 * (Math.random() + 0.5));
				}

				totalLength += vehicleExtraData.immutableVehicleCars.get(carNumber).getLength();
			}
		}

		// Write signals
		final double padding = 0.5 * speed * speed / vehicleExtraData.getDeceleration() + transportMode.stoppingSpace;
		final int headIndexPadded = Utilities.getIndexFromConditionalList(vehicleExtraData.immutablePath, railProgress + padding);
		final int headIndex = Utilities.getIndexFromConditionalList(vehicleExtraData.immutablePath, railProgress - 1);
		final int endIndex = Utilities.getIndexFromConditionalList(vehicleExtraData.immutablePath, railProgress - vehicleExtraData.getTotalVehicleLength());
		final int endIndexPadded = Utilities.getIndexFromConditionalList(vehicleExtraData.immutablePath, railProgress - vehicleExtraData.getTotalVehicleLength() - padding);
		for (int i = Math.max(0, endIndexPadded); i <= Math.min(vehicleExtraData.immutablePath.size() - 1, headIndexPadded); i++) {
			final PathData pathData = vehicleExtraData.immutablePath.get(i);
			if (i > endIndexPadded && i <= headIndex) {
				MinecraftClientData.getInstance().blockedRailIds.add(pathData.getHexId(false));
			}
			if (i < headIndexPadded && i >= endIndex) {
				MinecraftClientData.getInstance().blockedRailIds.add(pathData.getHexId(true));
			}
		}

		// Write speed limit
		final PathData pathDataHead = Utilities.getElement(vehicleExtraData.immutablePath, headIndex);
		if (pathDataHead != null) {
			speedLimitKilometersPerHour = (int) pathDataHead.getSpeedLimitKilometersPerHour();
		}

		// Write platform stopping position
		platformStoppingDetails = null;
		PathData previousPathData = null;
		for (int i = Math.min(vehicleExtraData.immutablePath.size() - 1, headIndex + 1); i >= 0; i--) {
			final PathData pathData = vehicleExtraData.immutablePath.get(i);

			if (i <= headIndex) {
				if (pathData.getEndDistance() <= railProgress - vehicleExtraData.getTotalVehicleLength() || pathData.getSavedRailBaseId() == vehicleExtraData.getSidingId()) {
					break;
				}

				if (pathData.getDwellTime() > 0) {
					platformStoppingDetails = new DoubleObjectImmutablePair<>(
							pathData.getEndDistance() - railProgress,
							new DoubleDoubleImmutablePair(pathData.getRailLength(), previousPathData != null && previousPathData.isOppositeRail(pathData) ? 0 : vehicleExtraData.getTotalVehicleLength())
					);
					break;
				}
			}

			previousPathData = pathData;
		}
	}

	public ObjectArrayList<ObjectObjectImmutablePair<VehicleCar, ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>>>> getSmoothedVehicleCarsAndPositions(long millisElapsed) {
		final double oldRailProgress = railProgress;
		railProgress = persistentVehicleData.getSmoothedRailProgress(railProgress, persistentVehicleData.getDoorValue() > 0 ? 0 : millisElapsed * (speed == 0 ? Integer.MAX_VALUE : speed / 10));
		final ObjectArrayList<ObjectObjectImmutablePair<VehicleCar, ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>>>> vehicleCarsAndPositions = getVehicleCarsAndPositions();
		railProgress = oldRailProgress;
		return vehicleCarsAndPositions;
	}

	public void playMotorSound(VehicleResource vehicleResource, int carNumber, Vector bogiePosition) {
		persistentVehicleData.playMotorSound(vehicleResource, carNumber, Init.newBlockPos(bogiePosition.x, bogiePosition.y, bogiePosition.z), (float) speed, (float) (speed - oldSpeed), (float) vehicleExtraData.getAcceleration(), getIsOnRoute());
	}

	public void playDoorSound(VehicleResource vehicleResource, int carNumber, Vector vehiclePosition) {
		persistentVehicleData.playDoorSound(vehicleResource, carNumber, Init.newBlockPos(vehiclePosition.x, vehiclePosition.y, vehiclePosition.z));
	}

	public int getSpeedLimitKilometersPerHour() {
		return speedLimitKilometersPerHour;
	}

	public double getSpeed() {
		return speed;
	}

	public boolean isVehiclePastSafeStoppingDistance() {
		return speed > 0 && railProgress + 0.5 * speed * speed / vehicleExtraData.getDeceleration() * POWER_LEVEL_RATIO >= vehicleExtraData.getStoppingPoint();
	}

	@Nullable
	public DoubleObjectImmutablePair<DoubleDoubleImmutablePair> getPlatformStoppingDetails() {
		return platformStoppingDetails;
	}

	private static MutableText getStationText(String text, TranslationProvider.TranslationHolder keyCjk, TranslationProvider.TranslationHolder key) {
		return TextHelper.literal(text.isEmpty() ? "" : IGui.formatStationName(IGui.insertTranslation(keyCjk, key, 1, IGui.textOrUntitled(text))));
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
		final String combinedCjk = mergeNames(newNamesCjk, TranslationProvider.GUI_MTR_COMMA_CJK, TranslationProvider.GUI_MTR_COMMA_LAST_CJK);
		final String combined = mergeNames(newNames, TranslationProvider.GUI_MTR_COMMA, TranslationProvider.GUI_MTR_COMMA_LAST);
		return String.format("%s%s%s", combinedCjk, !combinedCjk.isEmpty() && !combined.isEmpty() ? "|" : "", combined);
	}

	private static String mergeNames(ObjectArrayList<String> names, TranslationProvider.TranslationHolder keyComma, TranslationProvider.TranslationHolder keyCommaLast) {
		if (names.isEmpty()) {
			return "";
		} else {
			final StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < names.size(); i++) {
				stringBuilder.append(names.get(i));
				if (i <= names.size() - 3) {
					stringBuilder.append(keyComma.getString());
				} else if (i == names.size() - 2) {
					stringBuilder.append(keyCommaLast.getString());
				}
			}
			return stringBuilder.toString();
		}
	}

	/**
	 * Dispose the vehicle and execute the associated clean-up work, such as stopping vehicle sounds.
	 */
	public void dispose() {
		persistentVehicleData.dispose();
	}
}
