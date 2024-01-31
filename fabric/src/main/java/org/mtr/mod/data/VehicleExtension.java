package org.mtr.mod.data;

import org.mtr.core.data.Data;
import org.mtr.core.data.Vehicle;
import org.mtr.core.integration.VehicleUpdate;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Items;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.VehicleRidingMovement;
import org.mtr.mod.resource.VehicleResource;

import javax.annotation.Nullable;
import java.util.Locale;

public class VehicleExtension extends Vehicle implements Utilities {

	private double oldSpeed;

	public final PersistentVehicleData persistentVehicleData;

	public VehicleExtension(VehicleUpdate vehicleUpdate, Data data) {
		super(vehicleUpdate.getVehicleExtraData(), null, new JsonReader(Utilities.getJsonObjectFromData(vehicleUpdate.getVehicle())), data);
		final PersistentVehicleData tempPersistentVehicleData = ClientData.getInstance().vehicleIdToPersistentVehicleData.get(getId());
		if (tempPersistentVehicleData == null) {
			persistentVehicleData = new PersistentVehicleData();
			ClientData.getInstance().vehicleIdToPersistentVehicleData.put(getId(), persistentVehicleData);
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
		oldSpeed = speed;
		simulate(millisElapsed, null, null);
		persistentVehicleData.tick(millisElapsed, vehicleExtraData);
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		final String thisRouteName = vehicleExtraData.getThisRouteName();
		final String thisStationName = vehicleExtraData.getThisStationName();
		final String nextStationName = vehicleExtraData.getNextStationName();
		final String thisRouteDestination = vehicleExtraData.getThisRouteDestination();

		// Render client action bar floating text
		if (clientPlayerEntity != null && VehicleRidingMovement.getRidingVehicleCarNumberAndOffset(id) != null && VehicleRidingMovement.showShiftProgressBar() && (!isCurrentlyManual || !isHoldingKey(clientPlayerEntity))) {
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
	}

	public void playSound(VehicleResource vehicleResource, int carNumber, int bogieIndex, BlockPos bogiePosition) {
		persistentVehicleData.playSound(vehicleResource, carNumber, bogieIndex, bogiePosition, (float) speed, (float) (speed - oldSpeed), (float) vehicleExtraData.getAcceleration(), getIsOnRoute());
	}

	public static boolean isHoldingKey(@Nullable ClientPlayerEntity clientPlayerEntity) {
		return clientPlayerEntity != null && clientPlayerEntity.isHolding(Items.DRIVER_KEY.get());
	}

	private static MutableText getStationText(String text, String textKey) {
		return TextHelper.literal(text.isEmpty() ? "" : IGui.formatStationName(IGui.insertTranslation(String.format("gui.mtr.%s_station_cjk", textKey), String.format("gui.mtr.%s_station", textKey), 1, IGui.textOrUntitled(text))));
	}
}
