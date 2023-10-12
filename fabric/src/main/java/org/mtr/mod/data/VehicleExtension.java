package org.mtr.mod.data;

import org.apache.commons.lang3.StringUtils;
import org.mtr.core.data.Data;
import org.mtr.core.data.Vehicle;
import org.mtr.core.data.VehicleExtraData;
import org.mtr.core.serializers.JsonReader;
import org.mtr.core.tools.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Items;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.ScrollingText;

import javax.annotation.Nullable;
import java.util.Locale;

public class VehicleExtension extends Vehicle {

	private final ObjectArrayList<ObjectArrayList<ScrollingText>> scrollingTexts = new ObjectArrayList<>();

	private static final int SHIFT_ACTIVATE_TICKS = 30;
	private static final int DISMOUNT_PROGRESS_BAR_LENGTH = 30;

	public VehicleExtension(JsonObject jsonObject, Data data) {
		super(new VehicleExtraData(new JsonReader(jsonObject.getAsJsonObject("data"))), null, true, new JsonReader(jsonObject.getAsJsonObject("vehicle")), data);
	}

	public void updateData(@Nullable JsonObject jsonObject) {
		if (jsonObject != null) {
			updateData(new JsonReader(jsonObject.getAsJsonObject("vehicle")));
			vehicleExtraData.updateData(new JsonReader(jsonObject.getAsJsonObject("data")));
		}
	}

	public void simulate(long millisElapsed) {
		simulate(millisElapsed, null, null);
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		final String thisRouteName = vehicleExtraData.getThisRouteName();
		final String thisStationName = vehicleExtraData.getThisStationName();
		final String nextStationName = vehicleExtraData.getNextStationName();
		final String thisRouteDestination = vehicleExtraData.getThisRouteDestination();

		// Render client action bar floating text
		if (clientPlayerEntity != null && showShiftProgressBar() && (!isCurrentlyManual || !isHoldingKey(clientPlayerEntity))) {
			if (speed > 5 || thisRouteName.isEmpty() || thisStationName.isEmpty() || thisRouteDestination.isEmpty()) {
				clientPlayerEntity.sendMessage(new Text(TextHelper.translatable("gui.mtr.vehicle_speed", Utilities.round(speed, 1), Utilities.round(speed * 3.6F, 1)).data), true);
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

	public ObjectArrayList<ScrollingText> getScrollingText(int car) {
		while (scrollingTexts.size() <= car) {
			scrollingTexts.add(new ObjectArrayList<>());
		}
		return scrollingTexts.get(car);
	}

	public static boolean showShiftProgressBar() {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		final float shiftHoldingTicks = ClientData.getShiftHoldingTicks();

		if (shiftHoldingTicks > 0 && clientPlayerEntity != null) {
			final int progressFilled = MathHelper.clamp((int) (shiftHoldingTicks * DISMOUNT_PROGRESS_BAR_LENGTH / SHIFT_ACTIVATE_TICKS), 0, DISMOUNT_PROGRESS_BAR_LENGTH);
			final String progressBar = String.format("§6%s§7%s", StringUtils.repeat('|', progressFilled), StringUtils.repeat('|', DISMOUNT_PROGRESS_BAR_LENGTH - progressFilled));
			clientPlayerEntity.sendMessage(new Text(TextHelper.translatable("gui.mtr.dismount_hold", minecraftClient.getOptionsMapped().getKeySneakMapped().getBoundKeyLocalizedText(), progressBar).data), true);
			return false;
		} else {
			return true;
		}
	}

	public static boolean isHoldingKey(@Nullable ClientPlayerEntity clientPlayerEntity) {
		return clientPlayerEntity != null && clientPlayerEntity.isHolding(Items.DRIVER_KEY.get());
	}

	private static MutableText getStationText(String text, String textKey) {
		return TextHelper.literal(text.isEmpty() ? "" : IGui.formatStationName(IGui.insertTranslation(String.format("gui.mtr.%s_station_cjk", textKey), String.format("gui.mtr.%s_station", textKey), 1, IGui.textOrUntitled(text))));
	}
}
