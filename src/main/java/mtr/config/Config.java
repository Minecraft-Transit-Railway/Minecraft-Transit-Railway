package mtr.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mtr.Patreon;
import net.minecraft.client.MinecraftClient;

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
	private static boolean useDynamicFPS = true;

	public static final List<Patreon> PATREON_LIST = new ArrayList<>();
	private static final Path CONFIG_FILE_PATH = MinecraftClient.getInstance().runDirectory.toPath().resolve("config").resolve("mtr.json");
	private static final String USE_MTR_FONT_KEY = "use_mtr_font";
	private static final String SHOW_ANNOUNCEMENT_MESSAGES = "show_announcement_messages";
	private static final String HIDE_SPECIAL_RAIL_COLORS = "hide_special_rail_colors";
	private static final String HIDE_TRANSLUCENT_PARTS = "hide_translucent_parts";
	private static final String USE_TTS_ANNOUNCEMENTS = "use_tts_announcements";

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

	public static boolean hideSpecialRailColors() {
		return hideSpecialRailColors;
	}

	public static boolean useDynamicFPS() {
		return useDynamicFPS;
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

	public static boolean setUseDynamicFPS(boolean value) {
		useDynamicFPS = value;
		writeToFile();
		return useDynamicFPS;
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
		} catch (Exception e) {
			writeToFile();
			e.printStackTrace();
		}
	}

	public static void getPatreonList() {
		PATREON_LIST.clear();
		PATREON_LIST.addAll(Patreon.getPatreonList());
	}

	private static void writeToFile() {
		System.out.println("Wrote MTR mod config to file");
		final JsonObject jsonConfig = new JsonObject();
		jsonConfig.addProperty(USE_MTR_FONT_KEY, useMTRFont);
		jsonConfig.addProperty(SHOW_ANNOUNCEMENT_MESSAGES, showAnnouncementMessages);
		jsonConfig.addProperty(USE_TTS_ANNOUNCEMENTS, useTTSAnnouncements);
		jsonConfig.addProperty(HIDE_SPECIAL_RAIL_COLORS, hideSpecialRailColors);
		jsonConfig.addProperty(HIDE_TRANSLUCENT_PARTS, hideTranslucentParts);

		try {
			Files.write(CONFIG_FILE_PATH, Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
