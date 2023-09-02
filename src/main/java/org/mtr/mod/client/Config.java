package mtr.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mtr.Patreon;
import mtr.data.RailwayData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Config {

	private static boolean useMTRFont;
	private static boolean showAnnouncementMessages;
	private static boolean useTTSAnnouncements;
	private static boolean hideSpecialRailColors;
	private static boolean hideTranslucentParts;
	private static boolean shiftToToggleSitting;
	private static int languageOptions;
	private static boolean useDynamicFPS = true;
	private static int trackTextureOffset;
	private static int dynamicTextureResolution = 2;
	private static int trainRenderDistanceRatio = 7;

	public static final List<Patreon> PATREON_LIST = new ArrayList<>();
	public static final int TRACK_OFFSET_COUNT = 32;
	public static final int DYNAMIC_RESOLUTION_COUNT = 8;
	public static final int TRAIN_RENDER_DISTANCE_RATIO_COUNT = 16;
	private static final Path CONFIG_FILE_PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("mtr.json");
	private static final String USE_MTR_FONT_KEY = "use_mtr_font";
	private static final String SHOW_ANNOUNCEMENT_MESSAGES = "show_announcement_messages";
	private static final String HIDE_SPECIAL_RAIL_COLORS = "hide_special_rail_colors";
	private static final String HIDE_TRANSLUCENT_PARTS = "hide_translucent_parts";
	private static final String SHIFT_TO_TOGGLE_SITTING = "shift_to_toggle_sitting";
	private static final String LANGUAGE_OPTIONS = "language_options";
	private static final String USE_TTS_ANNOUNCEMENTS = "use_tts_announcements";
	private static final String TRACK_TEXTURE_OFFSET = "track_texture_offset";
	private static final String DYNAMIC_TEXTURE_RESOLUTION = "dynamic texture resolution";
	private static final String TRAIN_RENDER_DISTANCE_RATIO = "train_render_distance_ratio";

	public static boolean useMTRFont() {
		return useMTRFont;
	}

	public static boolean showAnnouncementMessages() {
		return showAnnouncementMessages;
	}

	public static boolean useTTSAnnouncements() {
		return useTTSAnnouncements;
	}

	public static boolean hideTranslucentParts() {
		return hideTranslucentParts;
	}

	public static boolean shiftToToggleSitting() {
		return shiftToToggleSitting;
	}

	public static int languageOptions() {
		return languageOptions;
	}

	public static boolean hideSpecialRailColors() {
		return hideSpecialRailColors;
	}

	public static boolean useDynamicFPS() {
		return useDynamicFPS;
	}

	public static int trackTextureOffset() {
		return trackTextureOffset;
	}

	public static int dynamicTextureResolution() {
		return dynamicTextureResolution;
	}

	public static int trainRenderDistanceRatio() {
		return trainRenderDistanceRatio;
	}

	public static boolean setUseMTRFont(boolean value) {
		useMTRFont = value;
		writeToFile();
		return useMTRFont;
	}

	public static boolean setShowAnnouncementMessages(boolean value) {
		showAnnouncementMessages = value;
		writeToFile();
		return showAnnouncementMessages;
	}

	public static boolean setUseTTSAnnouncements(boolean value) {
		useTTSAnnouncements = value;
		writeToFile();
		return useTTSAnnouncements;
	}

	public static boolean setHideSpecialRailColors(boolean value) {
		hideSpecialRailColors = value;
		writeToFile();
		return hideSpecialRailColors;
	}

	public static boolean setHideTranslucentParts(boolean value) {
		hideTranslucentParts = value;
		writeToFile();
		return hideTranslucentParts;
	}

	public static boolean setShiftToToggleSitting(boolean value) {
		shiftToToggleSitting = value;
		writeToFile();
		return shiftToToggleSitting;
	}

	public static int setLanguageOptions(int value) {
		languageOptions = value % 3;
		writeToFile();
		return languageOptions;
	}

	public static boolean setUseDynamicFPS(boolean value) {
		useDynamicFPS = value;
		writeToFile();
		return useDynamicFPS;
	}

	public static void setTrackTextureOffset(int value) {
		trackTextureOffset = Mth.clamp(value, 0, TRACK_OFFSET_COUNT - 1);
		writeToFile();
	}

	public static void setDynamicTextureResolution(int value) {
		dynamicTextureResolution = Mth.clamp(value, 0, DYNAMIC_RESOLUTION_COUNT - 1);
		writeToFile();
	}

	public static void setTrainRenderDistanceRatio(int value) {
		trainRenderDistanceRatio = Mth.clamp(value, 0, TRAIN_RENDER_DISTANCE_RATIO_COUNT - 1);
		writeToFile();
	}

	public static void refreshProperties() {
		System.out.println("Refreshed MTR mod config");
		try {
			final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_FILE_PATH))).getAsJsonObject();
			try {
				useMTRFont = jsonConfig.get(USE_MTR_FONT_KEY).getAsBoolean();
			} catch (Exception ignored) {
			}
			try {
				showAnnouncementMessages = jsonConfig.get(SHOW_ANNOUNCEMENT_MESSAGES).getAsBoolean();
			} catch (Exception ignored) {
			}
			try {
				useTTSAnnouncements = jsonConfig.get(USE_TTS_ANNOUNCEMENTS).getAsBoolean();
			} catch (Exception ignored) {
			}
			try {
				hideSpecialRailColors = jsonConfig.get(HIDE_SPECIAL_RAIL_COLORS).getAsBoolean();
			} catch (Exception ignored) {
			}
			try {
				hideTranslucentParts = jsonConfig.get(HIDE_TRANSLUCENT_PARTS).getAsBoolean();
			} catch (Exception ignored) {
			}
			try {
				shiftToToggleSitting = jsonConfig.get(SHIFT_TO_TOGGLE_SITTING).getAsBoolean();
			} catch (Exception ignored) {
			}
			try {
				languageOptions = jsonConfig.get(LANGUAGE_OPTIONS).getAsInt() % 3;
			} catch (Exception ignored) {
			}
			try {
				trackTextureOffset = Mth.clamp(jsonConfig.get(TRACK_TEXTURE_OFFSET).getAsInt(), 0, TRACK_OFFSET_COUNT - 1);
			} catch (Exception ignored) {
			}
			try {
				dynamicTextureResolution = Mth.clamp(jsonConfig.get(DYNAMIC_TEXTURE_RESOLUTION).getAsInt(), 0, DYNAMIC_RESOLUTION_COUNT - 1);
			} catch (Exception ignored) {
			}
			try {
				trainRenderDistanceRatio = Mth.clamp(jsonConfig.get(TRAIN_RENDER_DISTANCE_RATIO).getAsInt(), 0, TRAIN_RENDER_DISTANCE_RATIO_COUNT - 1);
			} catch (Exception ignored) {
			}
		} catch (Exception e) {
			writeToFile();
			e.printStackTrace();
		}
	}

	private static void writeToFile() {
		System.out.println("Wrote MTR mod config to file");
		final JsonObject jsonConfig = new JsonObject();
		jsonConfig.addProperty(USE_MTR_FONT_KEY, useMTRFont);
		jsonConfig.addProperty(SHOW_ANNOUNCEMENT_MESSAGES, showAnnouncementMessages);
		jsonConfig.addProperty(USE_TTS_ANNOUNCEMENTS, useTTSAnnouncements);
		jsonConfig.addProperty(HIDE_SPECIAL_RAIL_COLORS, hideSpecialRailColors);
		jsonConfig.addProperty(HIDE_TRANSLUCENT_PARTS, hideTranslucentParts);
		jsonConfig.addProperty(SHIFT_TO_TOGGLE_SITTING, shiftToToggleSitting);
		jsonConfig.addProperty(LANGUAGE_OPTIONS, languageOptions);
		jsonConfig.addProperty(TRACK_TEXTURE_OFFSET, trackTextureOffset);
		jsonConfig.addProperty(DYNAMIC_TEXTURE_RESOLUTION, dynamicTextureResolution);
		jsonConfig.addProperty(TRAIN_RENDER_DISTANCE_RATIO, trainRenderDistanceRatio);

		try {
			Files.write(CONFIG_FILE_PATH, Collections.singleton(RailwayData.prettyPrint(jsonConfig)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
