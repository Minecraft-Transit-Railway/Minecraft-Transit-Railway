package org.mtr.tool;

import org.mtr.cache.GenericStringCache;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.generated.lang.TranslationProvider;

public final class DataHelper {

	private static final GenericStringCache<String[]> STATION_EXIT_NAME_SPLIT_CACHE = new GenericStringCache<>(30000, false);

	public static String getNameOrUntitled(NameColorDataBase nameColorDataBase) {
		return getNameOrUntitled(nameColorDataBase.getName());
	}

	public static String getNameOrUntitled(String name) {
		return name.isEmpty() ? TranslationProvider.GUI_MTR_UNTITLED.getString() : name;
	}

	public static String[] getSplitStationExitName(String stationExitName) {
		return STATION_EXIT_NAME_SPLIT_CACHE.get(stationExitName, () -> splitStationExitName(stationExitName));
	}

	private static String[] splitStationExitName(String stationExitName) {
		final char[] chars = stationExitName.toCharArray();
		final StringBuilder firstString = new StringBuilder();
		final StringBuilder secondString = new StringBuilder();
		int firstCharType = 0;

		for (int i = 0; i < chars.length; i++) {
			final char c = chars[i];
			if (i == 0) {
				firstCharType = getCharType(c);
				firstString.append(c);
			} else {
				if (firstCharType != 0 && getCharType(c) == firstCharType) {
					firstString.append(c);
				} else {
					firstCharType = 0;
					secondString.append(c);
				}
			}
		}

		return new String[]{firstString.toString(), secondString.toString()};
	}

	private static int getCharType(char c) {
		if (Character.isDigit(c)) {
			return 1;
		} else if (Character.isEmoji(c)) {
			return 2;
		} else if (Character.isUpperCase(c)) {
			return 3;
		} else if (Character.isLowerCase(c)) {
			return 4;
		} else if (Character.isAlphabetic(c)) {
			return 5;
		} else {
			return 6;
		}
	}
}
