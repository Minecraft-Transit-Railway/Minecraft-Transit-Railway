package mtr.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class Config {

	private static boolean useMTRFont;
	private static boolean showAnnouncementMessages;
	private static boolean useTTSAnnouncements;
	private static boolean useDynamicFPS;

	private static final Path CONFIG_FILE_PATH = MinecraftClient.getInstance().runDirectory.toPath().resolve("config").resolve("mtr.json");
	private static final String USE_MTR_FONT_KEY = "use_mtr_font";
	private static final String SHOW_ANNOUNCEMENT_MESSAGES = "show_announcement_messages";
	private static final String USE_TTS_ANNOUNCEMENTS = "use_tts_announcements";
	private static final String USE_DYNAMIC_FPS = "use_dynamic_fps";

	public static boolean useMTRFont() {
		return useMTRFont;
	}

	public static boolean showAnnouncementMessages() {
		return showAnnouncementMessages;
	}

	public static boolean useTTSAnnouncements() {
		return useTTSAnnouncements;
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

	public static boolean setUseDynamicFPS(boolean value) {
		useDynamicFPS = value;
		writeToFile();
		return useDynamicFPS;
	}

	public static void refreshProperties() {
		System.out.println("Refreshed mtr.MTR.MTR mod config");
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
				useDynamicFPS = jsonConfig.get(USE_DYNAMIC_FPS).getAsBoolean();
			} catch (Exception ignored) {
			}
		} catch (Exception e) {
			writeToFile();
			e.printStackTrace();
		}
	}

	private static void writeToFile() {
		System.out.println("Wrote mtr.MTR.MTR mod config to file");
		final JsonObject jsonConfig = new JsonObject();
		jsonConfig.addProperty(USE_MTR_FONT_KEY, useMTRFont);
		jsonConfig.addProperty(SHOW_ANNOUNCEMENT_MESSAGES, showAnnouncementMessages);
		jsonConfig.addProperty(USE_TTS_ANNOUNCEMENTS, useTTSAnnouncements);
		jsonConfig.addProperty(USE_DYNAMIC_FPS, useDynamicFPS);

		try {
			Files.write(CONFIG_FILE_PATH, Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
