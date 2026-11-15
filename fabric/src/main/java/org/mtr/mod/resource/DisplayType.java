package org.mtr.mod.resource;

import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.render.MainRenderer;
import org.mtr.core.data.Vehicle;
import org.mtr.mod.resource.ModelPropertiesPart;
import org.mtr.mapping.mapper.WorldHelper;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.holder.MinecraftClient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public enum DisplayType {
	DESTINATION, ROUTE_NUMBER, ROUTE_NAME, DEPARTURE_INDEX, NEXT_STATION, NEXT_STATION_KCR, NEXT_STATION_MTR, NEXT_STATION_UK, ROUTE_COLOR, ROUTE_COLOR_ROUNDED, TEMPLATE;

	public static String getHongKongNextStationString(String thisStationName, String nextStationName, boolean atPlatform, boolean isKcr) {
		if (atPlatform) {
			return thisStationName;
		} else {
			return IGui.insertTranslation(isKcr ? TranslationProvider.GUI_MTR_NEXT_STATION_CJK : TranslationProvider.GUI_MTR_NEXT_STATION_ANNOUNCEMENT_CJK, isKcr ? TranslationProvider.GUI_MTR_NEXT_STATION : TranslationProvider.GUI_MTR_NEXT_STATION_ANNOUNCEMENT, 1, IGui.textOrUntitled(nextStationName));
		}
	}

	public static String getLondonNextStationString(String thisRouteName, String thisStationName, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, String destinationString, boolean atPlatform, boolean isTerminating) {
		final String stationName = atPlatform ? thisStationName : nextStationName;
		final List<String> messages = new ArrayList<>();

		if (!isTerminating) {
			messages.add(IGui.insertTranslation(TranslationProvider.GUI_MTR_LONDON_TRAIN_ROUTE_ANNOUNCEMENT_CJK, TranslationProvider.GUI_MTR_LONDON_TRAIN_ROUTE_ANNOUNCEMENT, 2, IGui.textOrUntitled(thisRouteName), IGui.textOrUntitled(destinationString)));
		}

		if (atPlatform) {
			messages.add(IGui.insertTranslation(TranslationProvider.GUI_MTR_LONDON_TRAIN_THIS_STATION_ANNOUNCEMENT_CJK, TranslationProvider.GUI_MTR_LONDON_TRAIN_THIS_STATION_ANNOUNCEMENT, 1, IGui.textOrUntitled(stationName)));
		} else {
			messages.add(IGui.insertTranslation(TranslationProvider.GUI_MTR_LONDON_TRAIN_NEXT_STATION_ANNOUNCEMENT_CJK, TranslationProvider.GUI_MTR_LONDON_TRAIN_NEXT_STATION_ANNOUNCEMENT, 1, IGui.textOrUntitled(stationName)));
		}

		final String mergedInterchangeRoutes = MainRenderer.getInterchangeRouteNames(getInterchanges);
		if (!mergedInterchangeRoutes.isEmpty()) {
			messages.add(IGui.insertTranslation(TranslationProvider.GUI_MTR_LONDON_TRAIN_INTERCHANGE_ANNOUNCEMENT_CJK, TranslationProvider.GUI_MTR_LONDON_TRAIN_INTERCHANGE_ANNOUNCEMENT, 1, mergedInterchangeRoutes));
		}

		if (isTerminating) {
			messages.add(IGui.insertTranslation(TranslationProvider.GUI_MTR_LONDON_TRAIN_TERMINATING_ANNOUNCEMENT_CJK, TranslationProvider.GUI_MTR_LONDON_TRAIN_TERMINATING_ANNOUNCEMENT, 1, IGui.textOrUntitled(stationName)));
		}

		return IGui.formatStationName(IGui.mergeStations(messages, "", " "));
	}

	public static String getTemplateText(String templateText, Vehicle vehicle, String displayDefaultText) {
        final String destination = ModelPropertiesPart.getOrDefault(vehicle.vehicleExtraData.getThisRouteDestination(), vehicle.vehicleExtraData.getNextRouteDestination(), vehicle.vehicleExtraData.getPreviousRouteDestination(), displayDefaultText, vehicle);
		final String routeNumber = ModelPropertiesPart.getOrDefault(vehicle.vehicleExtraData.getThisRouteNumber(), vehicle.vehicleExtraData.getNextRouteNumber(), vehicle.vehicleExtraData.getPreviousRouteNumber(), displayDefaultText, vehicle);
		final String routeName = ModelPropertiesPart.getOrDefault(routeNumber + " ", routeNumber, "") + ModelPropertiesPart.getOrDefault(vehicle.vehicleExtraData.getThisRouteName(), vehicle.vehicleExtraData.getNextRouteName(), vehicle.vehicleExtraData.getPreviousRouteName(), displayDefaultText, vehicle).split("||")[0];
		final String nextStation = ModelPropertiesPart.getOrDefault(vehicle.vehicleExtraData.getNextStationName(), vehicle.vehicleExtraData.getThisStationName(), vehicle.vehicleExtraData.getThisStationName());
		final String clock12hr = getClockText(false);
		final String clock24hr = getClockText(true);
		final String clockAMPM = getClockAMPM();

		String text = templateText;
		text = text.replaceAll("%DESTINATION%", destination);
		text = text.replaceAll("%ROUTE_NUMBER%", routeNumber);
		text = text.replaceAll("%ROUTE_NAME%", routeName);
		text = text.replaceAll("%NEXT_STATION%", nextStation);
		text = text.replaceAll("%CLOCK_12HR%", clock12hr);
		text = text.replaceAll("%CLOCK_24HR%", clock24hr);
		text = text.replaceAll("%CLOCK_AM_PM_UPPER%", clockAMPM);
		text = text.replaceAll("%CLOCK_AM_PM_LOWER%", clockAMPM.toLowerCase());

		return text;
	}

	public static String getClockText(boolean is24hr) {
		final World world = World.cast(MinecraftClient.getInstance().getWorldMapped());

		long time = WorldHelper.getTimeOfDay(world) + 6000;
        long hours = time / 1000;
        long minutes = (long)Math.floor((time - (hours * 1000)) / 16.8);
        
		String str = "";

		if (is24hr) {
			str += String.format("%02d", hours % 24);
		} else {
			str += hours % 12;
		}

		str += ":";

		str += String.format("%02d", minutes % 60);
		
		return str;
	}

	public static String getClockAMPM() {
		final World world = World.cast(MinecraftClient.getInstance().getWorldMapped());

		long time = WorldHelper.getTimeOfDay(world) + 6000;
        long hours = time / 1000;
		return (hours % 24 >= 12) ? "PM" : "AM";
	}
}
