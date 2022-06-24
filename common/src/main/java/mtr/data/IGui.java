package mtr.data;

import mtr.mappings.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface IGui {

	int SQUARE_SIZE = 20;
	int PANEL_WIDTH = 144;
	int TEXT_HEIGHT = 8;
	int TEXT_PADDING = 6;
	int TEXT_FIELD_PADDING = 4;
	int LINE_HEIGHT = 10;

	float SMALL_OFFSET_16 = 0.05F;
	float SMALL_OFFSET = SMALL_OFFSET_16 / 16;

	int RGB_WHITE = 0xFFFFFF;
	int ARGB_WHITE = 0xFFFFFFFF;
	int ARGB_BLACK = 0xFF000000;
	int ARGB_WHITE_TRANSLUCENT = 0x7FFFFFFF;
	int ARGB_BLACK_TRANSLUCENT = 0x7F000000;
	int ARGB_LIGHT_GRAY = 0xFFAAAAAA;
	int ARGB_GRAY = 0xFF666666;
	int ARGB_BACKGROUND = 0xFF121212;

	int MAX_LIGHT_INTERIOR = 0xF000B0; // LightmapTextureManager.pack(0xFF,0xFF); doesn't work with shaders
	int MAX_LIGHT_GLOWING = 0xF000F0;

	static String formatStationName(String name) {
		return name.replace('|', ' ');
	}

	static String textOrUntitled(String text) {
		return text.isEmpty() ? Text.translatable("gui.mtr.untitled").getString() : text;
	}

	static String formatVerticalChinese(String text) {
		final StringBuilder textBuilder = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			final boolean isChinese = Character.isIdeographic(text.codePointAt(i));
			if (isChinese) {
				textBuilder.append('|');
			}
			textBuilder.append(text, i, i + 1);
			if (isChinese) {
				textBuilder.append('|');
			}
		}

		String newText = textBuilder.toString();
		while (newText.contains("||")) {
			newText = newText.replace("||", "|");
		}

		if (newText.startsWith("|")) {
			newText = newText.substring(1);
		}
		if (newText.endsWith("|")) {
			newText = newText.substring(0, newText.length() - 1);
		}

		return newText;
	}

	static String insertTranslation(String keyCJK, String key, int expectedArguments, String... arguments) {
		return insertTranslation(keyCJK, key, null, expectedArguments, arguments);
	}

	static String insertTranslation(String keyCJK, String key, String overrideFirst, int expectedArguments, String... arguments) {
		if (arguments.length < expectedArguments) {
			return "";
		}

		final List<String[]> dataCJK = new ArrayList<>();
		final List<String[]> data = new ArrayList<>();
		for (int i = 0; i < arguments.length; i++) {
			final String[] argumentSplit = arguments[i].split("\\|");

			int indexCJK = 0;
			int index = 0;
			for (final String text : argumentSplit) {
				if (text.codePoints().anyMatch(Character::isIdeographic)) {
					if (indexCJK == dataCJK.size()) {
						dataCJK.add(new String[expectedArguments]);
					}
					dataCJK.get(indexCJK)[i] = text;
					indexCJK++;
				} else {
					if (index == data.size()) {
						data.add(new String[expectedArguments]);
					}
					data.get(index)[i] = text;
					index++;
				}
			}
		}

		final StringBuilder result = new StringBuilder();
		dataCJK.forEach(combinedArguments -> {
			if (Arrays.stream(combinedArguments).allMatch(Objects::nonNull)) {
				result.append("|");
				if (overrideFirst == null) {
					result.append(Text.translatable(keyCJK, (Object[]) combinedArguments).getString());
				} else {
					final String[] newCombinedArguments = new String[expectedArguments + 1];
					System.arraycopy(combinedArguments, 0, newCombinedArguments, 1, expectedArguments);
					newCombinedArguments[0] = overrideFirst;
					result.append(Text.translatable(keyCJK, (Object[]) newCombinedArguments).getString());
				}
			}
		});
		data.forEach(combinedArguments -> {
			if (Arrays.stream(combinedArguments).allMatch(Objects::nonNull)) {
				result.append("|");
				if (overrideFirst == null) {
					result.append(Text.translatable(key, (Object[]) combinedArguments).getString());
				} else {
					final String[] newCombinedArguments = new String[expectedArguments + 1];
					System.arraycopy(combinedArguments, 0, newCombinedArguments, 1, expectedArguments);
					newCombinedArguments[0] = overrideFirst;
					result.append(Text.translatable(key, (Object[]) newCombinedArguments).getString());
				}
			}
		});

		if (result.length() > 0) {
			return result.substring(1);
		} else {
			return "";
		}
	}

	static String mergeStations(List<String> stations) {
		return mergeStations(stations, null);
	}

	static String mergeStations(List<String> stations, String separator) {
		final List<List<String>> combinedCJK = new ArrayList<>();
		final List<List<String>> combined = new ArrayList<>();

		for (final String station : stations) {
			final String[] stationSplit = station.split("\\|");
			final List<String> currentStationCJK = new ArrayList<>();
			final List<String> currentStation = new ArrayList<>();

			for (final String stationSplitPart : stationSplit) {
				if (stationSplitPart.codePoints().anyMatch(Character::isIdeographic)) {
					currentStationCJK.add(stationSplitPart);
				} else {
					currentStation.add(stationSplitPart);
				}
			}

			for (int i = 0; i < currentStationCJK.size(); i++) {
				if (i < combinedCJK.size()) {
					if (!combinedCJK.get(i).contains(currentStationCJK.get(i))) {
						combinedCJK.get(i).add(currentStationCJK.get(i));
					}
				} else {
					final int index = i;
					combinedCJK.add(new ArrayList<String>() {{
						add(currentStationCJK.get(index));
					}});
				}
			}

			for (int i = 0; i < currentStation.size(); i++) {
				if (i < combined.size()) {
					if (!combined.get(i).contains(currentStation.get(i))) {
						combined.get(i).add(currentStation.get(i));
					}
				} else {
					final int index = i;
					combined.add(new ArrayList<String>() {{
						add(currentStation.get(index));
					}});
				}
			}
		}

		final List<String> flattened = combinedCJK.stream().map(subList -> subList.stream().reduce((a, b) -> a + (separator == null ? Text.translatable("gui.mtr.separator_cjk").getString() : separator) + b).orElse("")).collect(Collectors.toList());
		flattened.addAll(combined.stream().map(subList -> subList.stream().reduce((a, b) -> a + (separator == null ? Text.translatable("gui.mtr.separator").getString() : separator) + b).orElse("")).collect(Collectors.toList()));
		return flattened.stream().reduce((a, b) -> a + "|" + b).orElse("");
	}

	enum HorizontalAlignment {
		LEFT, CENTER, RIGHT;

		public float getOffset(float x, float width) {
			switch (this) {
				case CENTER:
					return x - width / 2;
				case RIGHT:
					return x - width;
				default:
					return x;
			}
		}
	}

	enum VerticalAlignment {
		TOP, CENTER, BOTTOM;

		public float getOffset(float y, float height) {
			switch (this) {
				case CENTER:
					return y - height / 2;
				case BOTTOM:
					return y - height;
				default:
					return y;
			}
		}
	}
}
