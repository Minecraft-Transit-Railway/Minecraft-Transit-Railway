package org.mtr.mod.screen;

import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.data.IGui;
import org.mtr.mod.packet.PacketFetchArrivals;
import org.mtr.mod.render.RenderPIDS;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public final class PIDSFormatter implements Utilities, IGui {

	private int currentColor = RGB_WHITE;
	private BooleanSupplier currentCondition = () -> true;
	private final ObjectImmutableList<ArrivalResponse> arrivalResponseList;
	private final ObjectArrayList<Column> columns = new ObjectArrayList<>();

	private static final char[] TAGS = {'%', '$', '@'};
	private static final char OPENING_BLOCK = '{';
	private static final char CLOSING_BLOCK = '}';

	public PIDSFormatter(ObjectImmutableList<ArrivalResponse> arrivalResponseList, String text) {
		this.arrivalResponseList = arrivalResponseList;
		// Add a default column to begin
		columns.add(new Column(HorizontalAlignment.LEFT, 0, 100));
		format(text);
	}

	public ObjectImmutableList<Column> getColumns() {
		return new ObjectImmutableList<>(columns);
	}

	private void format(String text) {
		if (text.isEmpty() || columns.isEmpty()) {
			return;
		}

		final Column column = Utilities.getElement(columns, -1);
		final String textBeforeTag;
		final String textInTag;
		final String textAfterTag;
		final char tagChar; // Either '%', '$', or '@'

		// Find the next tag and fill in the above variables
		final int nextTagIndexOpening = getNextTagIndex(text);
		if (nextTagIndexOpening < 0) {
			textBeforeTag = text;
			textInTag = "";
			textAfterTag = "";
			tagChar = ' ';
		} else {
			tagChar = text.charAt(nextTagIndexOpening);
			final int nextTagIndexClosing = getNextTagIndex(substringSafe(text, nextTagIndexOpening + 1), tagChar);
			if (nextTagIndexClosing < 0) {
				textBeforeTag = text;
				textInTag = "";
				textAfterTag = "";
			} else {
				textBeforeTag = substringSafe(text, 0, nextTagIndexOpening);
				final int adjustedNextTagIndexClosing = nextTagIndexOpening + 1 + nextTagIndexClosing;
				textInTag = substringSafe(text, nextTagIndexOpening + 1, adjustedNextTagIndexClosing);
				textAfterTag = substringSafe(text, adjustedNextTagIndexClosing + 1);
			}
		}

		if (!textBeforeTag.isEmpty()) {
			column.textChunks.add(new TextChunk(currentColor, textBeforeTag, currentCondition));
		}

		if (!textInTag.isEmpty()) {
			switch (tagChar) {
				case '%':
					// Add text chunks for placeholders
					parseTag("destination", textInTag, arrivalResponse -> {
						// TODO better switching with messages
						final String[] destinationSplit = arrivalResponse.getDestination().split("\\|");
						final String destinationText = destinationSplit.length == 0 ? "" : destinationSplit[((int) Math.floor(InitClient.getGameTick()) / RenderPIDS.SWITCH_LANGUAGE_TICKS) % destinationSplit.length];
						final String suffix = IGui.isCjk(destinationText) ? "_cjk" : "";
						final String destination;
						switch (arrivalResponse.getCircularState()) {
							case CLOCKWISE:
								destination = TextHelper.translatable("gui.mtr.clockwise_via" + suffix, destinationText).getString();
								break;
							case ANTICLOCKWISE:
								destination = TextHelper.translatable("gui.mtr.anticlockwise_via" + suffix, destinationText).getString();
								break;
							default:
								destination = destinationText;
								break;
						}
						addTextFromArrivalResponse(destination, textAfterTag);
					});
					parseTag("RAH", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "H", true, true, false), textAfterTag));
					parseTag("RAm", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "m", true, true, false), textAfterTag));
					parseTag("RAs", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "s", true, true, false), textAfterTag));
					parseTag("RA0H", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "H", true, true, true), textAfterTag));
					parseTag("RA0m", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "m", true, true, true), textAfterTag));
					parseTag("RA0s", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "s", true, true, true), textAfterTag));
					parseTag("AAH", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "H", false, false, false), textAfterTag));
					parseTag("AAh", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "h", false, false, false), textAfterTag));
					parseTag("AAm", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "m", false, false, false), textAfterTag));
					parseTag("AAs", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "s", false, false, false), textAfterTag));
					parseTag("AA0H", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "H", false, false, true), textAfterTag));
					parseTag("AA0h", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "h", false, false, true), textAfterTag));
					parseTag("AA0m", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "m", false, false, true), textAfterTag));
					parseTag("AA0s", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "s", false, false, true), textAfterTag));
					parseTag("AAa", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(arrivalResponse.getArrival(), "a", false, false, false), textAfterTag));
					parseTag("DH", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(Math.abs(arrivalResponse.getDeviation()), "H", false, true, false), textAfterTag));
					parseTag("Dm", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(Math.abs(arrivalResponse.getDeviation()), "m", false, true, false), textAfterTag));
					parseTag("Ds", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(Math.abs(arrivalResponse.getDeviation()), "s", false, true, false), textAfterTag));
					parseTag("D0H", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(Math.abs(arrivalResponse.getDeviation()), "H", false, true, true), textAfterTag));
					parseTag("D0m", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(Math.abs(arrivalResponse.getDeviation()), "m", false, true, true), textAfterTag));
					parseTag("D0s", textInTag, arrivalResponse -> addTextFromArrivalResponse(formatTime(Math.abs(arrivalResponse.getDeviation()), "s", false, true, true), textAfterTag));
					parseTag("index", textInTag, arrivalResponse -> addTextFromArrivalResponse(String.valueOf(arrivalResponse.getDepartureIndex()), textAfterTag));
					parseTag("routeName", textInTag, arrivalResponse -> addTextFromArrivalResponse(arrivalResponse.getRouteName(), textAfterTag));
					parseTag("routeNumber", textInTag, arrivalResponse -> addTextFromArrivalResponse(arrivalResponse.getRouteNumber(), textAfterTag));
					parseTag("platformNumber", textInTag, arrivalResponse -> addTextFromArrivalResponse(arrivalResponse.getPlatformName(), textAfterTag));
				case '$':
					// Change the color
					parseTag("routeColor", textInTag, arrivalResponse -> {
						currentColor = arrivalResponse.getRouteColor();
						format(textAfterTag);
					});
					if (textInTag.startsWith("#")) {
						currentColor = parseInt16(substringSafe(textInTag, 1));
						format(textAfterTag);
					}
					// TODO conditionals
					break;
				case '@':
					// Create a new column with horizontal alignment and start/end percentages
					final HorizontalAlignment horizontalAlignment;
					switch (substringSafe(textInTag, textInTag.length() - 1)) {
						default:
							horizontalAlignment = HorizontalAlignment.LEFT;
							break;
						case "C":
							horizontalAlignment = HorizontalAlignment.CENTER;
							break;
						case "R":
							horizontalAlignment = HorizontalAlignment.RIGHT;
							break;
					}
					final String[] percentagesString = substringSafe(textInTag, 0, textInTag.length() - 1).split("-");
					columns.add(new Column(horizontalAlignment, parseInt(percentagesString[0]), parseInt(percentagesString[1])));
					format(textAfterTag);
					break;
			}
		}
	}

	/**
	 * The use of this method assumes that the tag has the arrival index appended to the end of it.
	 *
	 * @param tagStartsWith what the expected tag name is, excluding the arrival index
	 * @param textInTag     the entire tag, including the arrival index
	 * @param consumer      if the arrival index can be successfully parsed, run a callback with the correct arrival
	 */
	private void parseTag(String tagStartsWith, String textInTag, Consumer<ArrivalResponse> consumer) {
		if (textInTag.startsWith(tagStartsWith)) {
			try {
				final ArrivalResponse arrivalResponse = Utilities.getElement(arrivalResponseList, Math.max(0, parseInt(textInTag.replaceFirst(tagStartsWith, "")) - 1));
				if (arrivalResponse != null) {
					consumer.accept(arrivalResponse);
				}
			} catch (Exception e) {
				Init.logException(e);
			}
		}
	}

	private void addTextFromArrivalResponse(String textToAdd, String textAfterTag) {
		Utilities.getElement(columns, -1).textChunks.add(new TextChunk(currentColor, textToAdd, currentCondition));
		format(textAfterTag);
	}

	/**
	 * Gets the index of the next tag present in the string or {@code -1} if no tags are left
	 */
	private static int getNextTagIndex(String text) {
		final IntArrayList indices = new IntArrayList();
		for (final char tag : TAGS) {
			final int index = text.indexOf(tag);
			if (index >= 0) {
				indices.add(index);
			}
		}
		return indices.intStream().min().orElse(-1);
	}

	/**
	 * Gets the index of the closing tag character in the string or {@code -1} if the closing tag character can't be found
	 */
	private static int getNextTagIndex(String text, char tagChar) {
		int blockCount = 0;
		for (int i = 0; i < text.length(); i++) {
			final char currentChar = text.charAt(i);
			if (currentChar == OPENING_BLOCK) {
				blockCount++;
			} else if (currentChar == CLOSING_BLOCK) {
				blockCount--;
			} else if (currentChar == tagChar && blockCount == 0) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Formats a millisecond value using {@link DateTimeFormatter}.
	 *
	 * @param millis                the input to be formatted
	 * @param pattern               the pattern to be used for formatting, based on {@link DateTimeFormatter}
	 * @param subtractCurrentMillis whether to subtract the current millis from the input
	 * @param padZero               whether to add an extra {@code 0} if the string is only one character long
	 */
	private static String formatTime(long millis, String pattern, boolean subtractCurrentMillis, boolean useUtc, boolean padZero) {
		final String result = DateTimeFormatter.ofPattern(pattern).format(Instant.ofEpochMilli(Math.max(0, millis - (subtractCurrentMillis ? PacketFetchArrivals.getMillisOffset() + System.currentTimeMillis() : 0))).atZone(useUtc ? ZoneOffset.UTC : ZoneId.systemDefault()));
		return (padZero && result.length() == 1 ? "0" : "") + result;
	}

	private static int parseInt(String text) {
		try {
			return Integer.parseInt(text);
		} catch (Exception ignored) {
			return 0;
		}
	}

	private static int parseInt16(String text) {
		try {
			return Integer.parseInt(text, 16);
		} catch (Exception ignored) {
			return 0;
		}
	}

	private static String substringSafe(String text, int beginIndex, int endIndex) {
		try {
			return text.substring(beginIndex, endIndex);
		} catch (Exception ignored) {
			return "";
		}
	}

	private static String substringSafe(String text, int beginIndex) {
		try {
			return text.substring(beginIndex);
		} catch (Exception ignored) {
			return "";
		}
	}

	public static class TextChunk {

		public final int color;
		public final String text;
		public final BooleanSupplier condition;

		public TextChunk(int color, String text, BooleanSupplier condition) {
			this.color = color;
			this.text = text;
			this.condition = condition;
		}
	}

	public static class Column {

		public final HorizontalAlignment horizontalAlignment;
		public final int startPercentage;
		public final int endPercentage;
		public final ObjectArrayList<TextChunk> textChunks = new ObjectArrayList<>();

		public Column(HorizontalAlignment horizontalAlignment, int startPercentage, int endPercentage) {
			this.horizontalAlignment = horizontalAlignment;
			this.startPercentage = Utilities.clamp(startPercentage, 0, 100);
			this.endPercentage = Utilities.clamp(endPercentage, 0, 100);
		}

		public int getTextWidth() {
			final int[] width = {0};
			textChunks.forEach(textChunk -> {
				if (textChunk.condition.getAsBoolean()) {
					width[0] += GraphicsHolder.getTextWidth(textChunk.text);
				}
			});
			return width[0];
		}
	}
}
