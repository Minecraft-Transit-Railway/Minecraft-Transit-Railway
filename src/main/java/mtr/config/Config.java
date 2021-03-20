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
	private static boolean useTTSAnnouncements;

	private static final Path CONFIG_FILE_PATH = MinecraftClient.getInstance().runDirectory.toPath().resolve("config").resolve("mtr.json");
	private static final String USE_MTR_FONT_KEY = "use_mtr_font";
	private static final String USE_TTS_ANNOUNCEMENTS = "use_tts_announcements";

	public static boolean useMTRFont() {
		return useMTRFont;
	}

	public static boolean useTTSAnnouncements() {
		return useTTSAnnouncements;
	}

	public static boolean setUseMTRFont(boolean value) {
		useMTRFont = value;
		writeToFile();
		return useMTRFont;
	}

	public static boolean setUseTTSAnnouncements(boolean value) {
		useTTSAnnouncements = value;
		writeToFile();
		return useTTSAnnouncements;
	}

	public static void refreshProperties() {
		System.out.println("Refreshed MTR mod config");
		try {
			final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_FILE_PATH))).getAsJsonObject();
			useMTRFont = jsonConfig.get(USE_MTR_FONT_KEY).getAsBoolean();
			useTTSAnnouncements = jsonConfig.get(USE_TTS_ANNOUNCEMENTS).getAsBoolean();
		} catch (Exception e) {
			writeToFile();
			e.printStackTrace();
		}
	}

	private static void writeToFile() {
		System.out.println("Wrote MTR mod config to file");
		final JsonObject jsonConfig = new JsonObject();
		jsonConfig.addProperty(USE_MTR_FONT_KEY, useMTRFont);
		jsonConfig.addProperty(USE_TTS_ANNOUNCEMENTS, useTTSAnnouncements);

		try {
			Files.write(CONFIG_FILE_PATH, Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
