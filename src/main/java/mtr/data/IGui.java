package mtr.data;

import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;
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
	int ARGB_BACKGROUND = 0xFF121212;

	int MAX_LIGHT_INTERIOR = 0xF000B0; // LightmapTextureManager.pack(0xFF,0xFF); doesn't work with shaders
	int MAX_LIGHT_GLOWING = 0xF000F0;

	static String formatStationName(String name) {
		return name.replace('|', ' ');
	}

	static String textOrUntitled(String text) {
		return text.isEmpty() ? new TranslatableText("gui.mtr.untitled").getString() : text;
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

		return newText;
	}

	static String addToStationName(String name, String prefixCJK, String prefix, String suffixCJK, String suffix) {
		final String[] nameSplit = name.split("\\|");
		final StringBuilder newName = new StringBuilder();
		for (final String namePart : nameSplit) {
			if (namePart.codePoints().anyMatch(Character::isIdeographic)) {
				newName.append(prefixCJK).append(namePart).append(suffixCJK);
			} else {
				newName.append(prefix).append(namePart).append(suffix);
			}
			newName.append("|");
		}
		return newName.deleteCharAt(newName.length() - 1).toString();
	}

	static String mergeStations(List<String> stations) {
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

		final List<String> flattened = combinedCJK.stream().map(subList -> subList.stream().reduce((a, b) -> a + new TranslatableText("gui.mtr.separator_cjk").getString() + b).orElse("")).collect(Collectors.toList());
		flattened.addAll(combined.stream().map(subList -> subList.stream().reduce((a, b) -> a + new TranslatableText("gui.mtr.separator").getString() + b).orElse("")).collect(Collectors.toList()));
		return flattened.stream().reduce((a, b) -> a + "|" + b).orElse("");
	}

	@FunctionalInterface
	interface DrawingCallback {
		void drawingCallback(float x1, float y1, float x2, float y2);
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
