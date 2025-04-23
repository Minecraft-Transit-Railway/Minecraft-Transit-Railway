package org.mtr.resource;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.cache.GenericCache;
import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.render.MainRenderer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public enum DisplayType {
	DESTINATION, ROUTE_NUMBER, DEPARTURE_INDEX, NEXT_STATION, NEXT_STATION_KCR, NEXT_STATION_MTR, NEXT_STATION_UK, ROUTE_COLOR, ROUTE_COLOR_ROUNDED;

	private static final int CACHE_TIMEOUT = 5000;
	private static final GenericCache<String> NEXT_STATION_CACHE_HONG_KONG = new GenericCache<>(CACHE_TIMEOUT);
	private static final GenericCache<String> NEXT_STATION_CACHE_LONDON = new GenericCache<>(CACHE_TIMEOUT);

	public static String getHongKongNextStationString(String thisStationName, String nextStationName, boolean atPlatform, boolean isKcr) {
		if (atPlatform) {
			return thisStationName;
		} else {
			return NEXT_STATION_CACHE_HONG_KONG.get(thisStationName + nextStationName + isKcr, () -> IGui.insertTranslation(
					isKcr ? TranslationProvider.GUI_MTR_NEXT_STATION_CJK : TranslationProvider.GUI_MTR_NEXT_STATION_ANNOUNCEMENT_CJK,
					isKcr ? TranslationProvider.GUI_MTR_NEXT_STATION : TranslationProvider.GUI_MTR_NEXT_STATION_ANNOUNCEMENT,
					1,
					IGui.textOrUntitled(nextStationName)
			));
		}
	}

	public static String getLondonNextStationString(String thisRouteName, String thisStationName, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, String destinationString, boolean atPlatform, boolean isTerminating) {
		return NEXT_STATION_CACHE_LONDON.get(thisRouteName + thisStationName + nextStationName + destinationString + atPlatform + isTerminating, () -> {
			final String stationName = atPlatform ? thisStationName : nextStationName;
			final ObjectArrayList<String> messages = new ObjectArrayList<>();

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
		});
	}
}
