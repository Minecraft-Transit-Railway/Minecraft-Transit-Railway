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
    private static int PIDSMaxDistance = 256;
    private static int APGMaxDistance = 256;
    private static int clockMaxDistance = 256;
    private static int liftButtonMaxDistance = 256;
    private static int liftPanelMaxDistance = 256;
    private static int PSDAPGDoorMaxDistance = 256;
    private static int PSDTopMaxDistance = 256;
    private static int railwaySignMaxDistance = 256;
    private static int routeSignMaxDistance = 256;
    private static int signalMaxDistance = 256;
    private static int stationNameTallMaxDistance = 256;
    private static int stationNameTiledMaxDistance = 256;
    private static int liftMaxDistance = 256;

    public static final List<Patreon> PATREON_LIST = new ArrayList<>();
    public static final int TRACK_OFFSET_COUNT = 32;
    public static final int DYNAMIC_RESOLUTION_COUNT = 8;
    public static final int MAX_VIEW_DISTANCE = 256;
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
    private static final String PIDS_MAX_DISTANCE = "pids_max_view_distance";
    private static final String APG_MAX_DISTANCE = "apg_max_view_distance";
    private static final String CLOCK_MAX_DISTANCE = "clock_max_view_distance";
    private static final String LIFT_BOTTOM_MAX_DISTANCE = "lift_bottom_max_view_distance";
    private static final String LIFT_PANEL_MAX_DISTANCE = "lift_panel_max_view_distance";
    private static final String PSD_APG_DOOR_MAX_DISTANCE = "psd_apg_door_max_view_distance";
    private static final String PSD_TOP_MAX_DISTANCE = "psd_top_max_view_distance";
    private static final String RAILWAY_SIGN_MAX_DISTANCE = "railway_sign_max_view_distance";
    private static final String ROUTE_SIGN_MAX_DISTANCE = "route_sign_max_view_distance";
    private static final String SIGNAL_MAX_DISTANCE = "signal_max_view_distance";
    private static final String STATION_NAME_TALL_MAX_DISTANCE = "station_name_tall_max_view_distance";
    private static final String STATION_NAME_TILE_MAX_DISTANCE = "station_name_tile_max_view_distance";
    private static final String LIFT_MAX_DISTANCE = "lift_max_view_distance";

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

    public static int PIDSMaxDistance() {
        return PIDSMaxDistance;
    }

    public static int APGMaxDistance() {
        return APGMaxDistance;
    }

    public static int clockMaxDistance() {
        return clockMaxDistance;
    }

    public static int liftButtonMaxDistance() {
        return liftButtonMaxDistance;
    }

    public static int liftPanelMaxDistance() {
        return liftPanelMaxDistance;
    }

    public static int PSDAPGDoorMaxDistance() {
        return PSDAPGDoorMaxDistance;
    }

    public static int PSDTopMaxDistance() {
        return PSDTopMaxDistance;
    }

    public static int railwaySignMaxDistance() {
        return railwaySignMaxDistance;
    }

    public static int routeSignMaxDistance() {
        return routeSignMaxDistance;
    }

    public static int signalMaxDistance() {
        return signalMaxDistance;
    }

    public static int stationNameTallMaxDistance() {
        return stationNameTallMaxDistance;
    }

    public static int stationNameTiledMaxDistance() {
        return stationNameTiledMaxDistance;
    }

    public static int liftMaxDistance() {
        return liftMaxDistance;
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

    public static int setPIDSMaxDistance(int PIDSMaxDistance) {
        Config.PIDSMaxDistance = PIDSMaxDistance;
        writeToFile();
        return PIDSMaxDistance;
    }

    public static int setAPGMaxDistance(int APGMaxDistance) {
        Config.APGMaxDistance = APGMaxDistance;
        writeToFile();
        return APGMaxDistance;
    }

    public static int setClockMaxDistance(int clockMaxDistance) {
        Config.clockMaxDistance = clockMaxDistance;
        writeToFile();
        return clockMaxDistance;
    }

    public static int setLiftButtonMaxDistance(int liftButtonMaxDistance) {
        Config.liftButtonMaxDistance = liftButtonMaxDistance;
        writeToFile();
        return liftButtonMaxDistance;
    }

    public static int setLiftPanelMaxDistance(int liftPanelMaxDistance) {
        Config.liftPanelMaxDistance = liftPanelMaxDistance;
        writeToFile();
        return liftPanelMaxDistance;
    }

    public static int setPSDAPGDoorMaxDistance(int PSDAPGDoorMaxDistance) {
        Config.PSDAPGDoorMaxDistance = PSDAPGDoorMaxDistance;
        writeToFile();
        return PSDAPGDoorMaxDistance;
    }

    public static int setPSDTopMaxDistance(int PSDTopMaxDistance) {
        Config.PSDTopMaxDistance = PSDTopMaxDistance;
        writeToFile();
        return PSDTopMaxDistance;
    }

    public static int setRailwaySignMaxDistance(int railwaySignMaxDistance) {
        Config.railwaySignMaxDistance = railwaySignMaxDistance;
        writeToFile();
        return railwaySignMaxDistance;
    }

    public static int setRouteSignMaxDistance(int routeSignMaxDistance) {
        Config.routeSignMaxDistance = routeSignMaxDistance;
        writeToFile();
        return routeSignMaxDistance;
    }

    public static int setSignalMaxDistance(int signalMaxDistance) {
        Config.signalMaxDistance = signalMaxDistance;
        writeToFile();
        return signalMaxDistance;
    }

    public static int setStationNameTallMaxDistance(int stationNameTallMaxDistance) {
        Config.stationNameTallMaxDistance = stationNameTallMaxDistance;
        writeToFile();
        return stationNameTallMaxDistance;
    }

    public static int setStationNameTiledMaxDistance(int stationNameTiledMaxDistance) {
        Config.stationNameTiledMaxDistance = stationNameTiledMaxDistance;
        writeToFile();
        return stationNameTiledMaxDistance;
    }

    public static int setLiftMaxDistance(int liftMaxDistance) {
        Config.liftMaxDistance = liftMaxDistance;
        writeToFile();
        return liftMaxDistance;
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
            try {
                PIDSMaxDistance = jsonConfig.get(PIDS_MAX_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
            try {
                APGMaxDistance = jsonConfig.get(APG_MAX_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
            try {
                clockMaxDistance = jsonConfig.get(CLOCK_MAX_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
            try {
                liftButtonMaxDistance = jsonConfig.get(LIFT_BOTTOM_MAX_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
            try {
                liftPanelMaxDistance = jsonConfig.get(LIFT_PANEL_MAX_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
            try {
                PSDAPGDoorMaxDistance = jsonConfig.get(PSD_APG_DOOR_MAX_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
            try {
                PSDTopMaxDistance = jsonConfig.get(PSD_TOP_MAX_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
            try {
                railwaySignMaxDistance = jsonConfig.get(RAILWAY_SIGN_MAX_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
            try {
                routeSignMaxDistance = jsonConfig.get(ROUTE_SIGN_MAX_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
            try {
                signalMaxDistance = jsonConfig.get(SIGNAL_MAX_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
            try {
                stationNameTallMaxDistance = jsonConfig.get(STATION_NAME_TALL_MAX_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
            try {
                stationNameTiledMaxDistance = jsonConfig.get(STATION_NAME_TILE_MAX_DISTANCE).getAsInt();
            } catch (Exception ignored) {
            }
            try {
                liftMaxDistance = jsonConfig.get(LIFT_MAX_DISTANCE).getAsInt();
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
		jsonConfig.addProperty(PIDS_MAX_DISTANCE, PIDSMaxDistance);
		jsonConfig.addProperty(APG_MAX_DISTANCE, APGMaxDistance);
		jsonConfig.addProperty(CLOCK_MAX_DISTANCE, clockMaxDistance);
		jsonConfig.addProperty(LIFT_BOTTOM_MAX_DISTANCE, liftButtonMaxDistance);
		jsonConfig.addProperty(LIFT_PANEL_MAX_DISTANCE, liftPanelMaxDistance);
		jsonConfig.addProperty(PSD_APG_DOOR_MAX_DISTANCE, PSDAPGDoorMaxDistance);
		jsonConfig.addProperty(PSD_TOP_MAX_DISTANCE, PSDTopMaxDistance);
		jsonConfig.addProperty(RAILWAY_SIGN_MAX_DISTANCE, railwaySignMaxDistance);
		jsonConfig.addProperty(ROUTE_SIGN_MAX_DISTANCE, routeSignMaxDistance);
		jsonConfig.addProperty(SIGNAL_MAX_DISTANCE, signalMaxDistance);
		jsonConfig.addProperty(STATION_NAME_TALL_MAX_DISTANCE, stationNameTallMaxDistance);
		jsonConfig.addProperty(STATION_NAME_TILE_MAX_DISTANCE, stationNameTiledMaxDistance);
		jsonConfig.addProperty(LIFT_MAX_DISTANCE, liftMaxDistance);
        try {
            Files.write(CONFIG_FILE_PATH, Collections.singleton(RailwayData.prettyPrint(jsonConfig)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}