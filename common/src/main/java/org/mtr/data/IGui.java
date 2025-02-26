package org.mtr.mod.data;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.mapper.GuiDrawing;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

	static String formatStationName(String name) {
		return name.replace('|', ' ');
	}

	static String textOrUntitled(String text) {
		return text.isEmpty() ? TranslationProvider.GUI_MTR_UNTITLED.getString() : text;
	}

	static String formatVerticalChinese(String text) {
		final StringBuilder textBuilder = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			final boolean isCjk = isCjk(text.substring(i, i + 1));
			if (isCjk) {
				textBuilder.append('|');
			}
			textBuilder.append(text, i, i + 1);
			if (isCjk) {
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

	static String insertTranslation(TranslationProvider.TranslationHolder keyCJK, TranslationProvider.TranslationHolder key, int expectedArguments, String... arguments) {
		return insertTranslation(keyCJK, key, null, expectedArguments, arguments);
	}

	static String insertTranslation(TranslationProvider.TranslationHolder keyCJK, TranslationProvider.TranslationHolder key, @Nullable String overrideFirst, int expectedArguments, String... arguments) {
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
				if (isCjk(text)) {
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
					result.append(keyCJK.getString((Object[]) combinedArguments));
				} else {
					final String[] newCombinedArguments = new String[expectedArguments + 1];
					System.arraycopy(combinedArguments, 0, newCombinedArguments, 1, expectedArguments);
					newCombinedArguments[0] = overrideFirst;
					result.append(keyCJK.getString((Object[]) newCombinedArguments));
				}
			}
		});
		data.forEach(combinedArguments -> {
			if (Arrays.stream(combinedArguments).allMatch(Objects::nonNull)) {
				result.append("|");
				if (overrideFirst == null) {
					result.append(key.getString((Object[]) combinedArguments));
				} else {
					final String[] newCombinedArguments = new String[expectedArguments + 1];
					System.arraycopy(combinedArguments, 0, newCombinedArguments, 1, expectedArguments);
					newCombinedArguments[0] = overrideFirst;
					result.append(key.getString((Object[]) newCombinedArguments));
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
		return mergeStations(stations, TranslationProvider.GUI_MTR_SEPARATOR_CJK.getString(), TranslationProvider.GUI_MTR_SEPARATOR.getString());
	}

	static String mergeStationsWithCommas(ObjectArrayList<String> stationNames) {
		return mergeStations(stationNames, null, null);
	}

	static String mergeStations(List<String> stations, @Nullable String separatorCjk, @Nullable String separator) {
		final List<List<String>> combinedCJK = new ArrayList<>();
		final List<List<String>> combined = new ArrayList<>();

		for (final String station : stations) {
			final String[] stationSplit = station.split("\\|");
			final List<String> currentStationCJK = new ArrayList<>();
			final List<String> currentStation = new ArrayList<>();

			for (final String stationSplitPart : stationSplit) {
				if (isCjk(stationSplitPart)) {
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

		final List<String> flattened = new ArrayList<>();
		combinedCJK.forEach(subList -> {
			final int listSize = subList.size();
			final StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < listSize; i++) {
				stringBuilder.append(subList.get(i));
				if (i <= listSize - 2) {
					if (separatorCjk == null) {
						if (listSize > 2) {
							stringBuilder.append(TranslationProvider.GUI_MTR_COMMA_CJK.getString());
						}
						if (i == listSize - 2) {
							stringBuilder.append(TranslationProvider.GUI_MTR_COMMA_LAST_CJK.getString());
						}
					} else {
						stringBuilder.append(separatorCjk);
					}
				}
			}
			final String result = stringBuilder.toString().replace("  ", " ");
			if (!result.isEmpty()) {
				flattened.add(stringBuilder.toString().replace("  ", " "));
			}
		});
		combined.forEach(subList -> {
			final int listSize = subList.size();
			final StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < listSize; i++) {
				stringBuilder.append(subList.get(i));
				if (i <= listSize - 2) {
					if (separator == null) {
						if (listSize > 2) {
							stringBuilder.append(TranslationProvider.GUI_MTR_COMMA.getString());
						}
						if (i == listSize - 2) {
							stringBuilder.append(TranslationProvider.GUI_MTR_COMMA_LAST.getString());
						}
					} else {
						stringBuilder.append(separator);
					}
				}
			}
			final String result = stringBuilder.toString().replace("  ", " ");
			if (!result.isEmpty()) {
				flattened.add(result);
			}
		});

		return String.join("|", flattened);
	}

	static boolean isCjk(String text) {
		return text.codePoints().anyMatch(codePoint -> {
			final Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(codePoint);
			return Character.isIdeographic(codePoint) ||
					unicodeBlock == Character.UnicodeBlock.CJK_COMPATIBILITY ||
					unicodeBlock == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS ||
					unicodeBlock == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
					unicodeBlock == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT ||
					unicodeBlock == Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT ||
					unicodeBlock == Character.UnicodeBlock.CJK_STROKES ||
					unicodeBlock == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ||
					unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
					unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ||
					unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B ||
					unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C ||
					unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D ||
					unicodeBlock == Character.UnicodeBlock.ENCLOSED_CJK_LETTERS_AND_MONTHS ||
					unicodeBlock == Character.UnicodeBlock.BOPOMOFO ||
					unicodeBlock == Character.UnicodeBlock.BOPOMOFO_EXTENDED ||
					unicodeBlock == Character.UnicodeBlock.HIRAGANA ||
					unicodeBlock == Character.UnicodeBlock.KATAKANA ||
					unicodeBlock == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS ||
					unicodeBlock == Character.UnicodeBlock.KANA_SUPPLEMENT ||
					unicodeBlock == Character.UnicodeBlock.KANBUN ||
					unicodeBlock == Character.UnicodeBlock.HANGUL_JAMO ||
					unicodeBlock == Character.UnicodeBlock.HANGUL_JAMO_EXTENDED_A ||
					unicodeBlock == Character.UnicodeBlock.HANGUL_JAMO_EXTENDED_B ||
					unicodeBlock == Character.UnicodeBlock.HANGUL_SYLLABLES ||
					unicodeBlock == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO ||
					unicodeBlock == Character.UnicodeBlock.KANGXI_RADICALS ||
					unicodeBlock == Character.UnicodeBlock.TAI_XUAN_JING_SYMBOLS ||
					unicodeBlock == Character.UnicodeBlock.IDEOGRAPHIC_DESCRIPTION_CHARACTERS;
		});
	}

	static void drawTexture(GuiDrawing guiDrawing, int x, int y, int u, int v, int width, int height) {
		drawTexture(guiDrawing, x, y, (float) u, (float) v, width, height, 256, 256);
	}

	static void drawTexture(GuiDrawing guiDrawing, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
		drawTexture(guiDrawing, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
	}

	static void drawTexture(GuiDrawing guiDrawing, int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
		drawTexture(guiDrawing, x, y, x + width, y + height, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
	}

	static void drawTexture(GuiDrawing guiDrawing, int x1, int y1, int x2, int y2, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
		drawTexture(guiDrawing, x1, y1, x2, y2, u / textureWidth, v / textureHeight, (u + regionWidth) / textureWidth, (v + regionHeight) / textureHeight);
	}

	static void drawTexture(GuiDrawing guiDrawing, int x1, int y1, int x2, int y2, float u1, float v1, float u2, float v2) {
		guiDrawing.drawTexture(x1, y1, x2, y2, u1, v1, u2, v2);
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
