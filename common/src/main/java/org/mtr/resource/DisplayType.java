package org.mtr.mod.resource;

import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.render.MainRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public enum DisplayType {
	DESTINATION, ROUTE_NUMBER, DEPARTURE_INDEX, NEXT_STATION, NEXT_STATION_KCR, NEXT_STATION_MTR, NEXT_STATION_UK, ROUTE_COLOR, ROUTE_COLOR_ROUNDED;

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
}
