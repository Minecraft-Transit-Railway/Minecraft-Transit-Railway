package org.mtr.mod.client;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mapping.holder.MathHelper;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mod.Init;
import org.mtr.mod.Patreon;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Config
{
    public static final List<Patreon> PATREON_LIST = new ArrayList<>();
    public static final int TRACK_OFFSET_COUNT = 32;
    public static final int DYNAMIC_RESOLUTION_COUNT = 8;
    public static final int TRAIN_RENDER_DISTANCE_RATIO_COUNT = 16;
    private static final Path CONFIG_FILE_PATH = MinecraftClient.getInstance().getRunDirectoryMapped().toPath().resolve("config").resolve("mtr.json");

    public static final Property<Boolean> USE_MTR_FONT = new Property<Boolean>("use_mtr_font", false)
    {
        @Override
        public Boolean get() {
            return false; // TODO no fancy font rendering
        }
    };

    public static final Property<Boolean> SHOW_ANNOUNCEMENT_MESSAGES = new Property<Boolean>("show_announcement_messages", false);

    public static final Property<Boolean> USE_TTS_ANNOUNCEMENTS = new Property<Boolean>("use_tts_announcements", false);

    public static final Property<Boolean> HIDE_SPECIAL_RAIL_COLORS = new Property<Boolean>("hide_special_rail_colors", false);

    public static final Property<Boolean> HIDE_TRANSLUCENT_PARTS = new Property<Boolean>("hide_translucent_parts", false);

    public static final Property<Boolean> SHIFT_TO_TOGGLE_SITTING = new Property<Boolean>("shift_to_toggle_sitting", false);

    public static final Property<Boolean> USE_DYNAMIC_FPS = new Property<Boolean>("use_dynamic_fps", false);

    public static final Property<Integer> LANGUAGE_OPTIONS = new Property<Integer>("language_options", 0)
    {
        @Override
        public void set(Integer value) {
            this.value = value % 3;
        }
    };

    public static final Property<Integer> TRACK_TEXTURE_OFFSET = new Property<Integer>("track_texture_offset", 0)
    {
        @Override
        public void set(Integer value) {
            this.value = MathHelper.clamp(value, 0, TRACK_OFFSET_COUNT - 1);
        }
    };

    public static final Property<Integer> DYNAMIC_TEXTURE_RESOLUTION = new Property<Integer>("dynamic texture resolution", 2)
    {
        @Override
        public void set(Integer value) {
            this.value = MathHelper.clamp(value, 0, DYNAMIC_RESOLUTION_COUNT - 1);
        }
    };

    public static final Property<Integer> TRAIN_RENDER_DISTANCE_RATIO = new Property<Integer>("rain_render_distance_ratio", 7)
    {
        @Override
        public void set(Integer value) {
            this.value = MathHelper.clamp(value, 0, TRAIN_RENDER_DISTANCE_RATIO_COUNT - 1);
        }
    };

    public static void refreshProperties() {
        Init.LOGGER.info("Refreshed Minecraft Transit Railway mod config");
        try {
            final JsonObject jsonConfig = Utilities.parseJson(String.join("", Files.readAllLines(CONFIG_FILE_PATH)));
            try {
                USE_MTR_FONT.set(jsonConfig.get(USE_MTR_FONT.getId()).getAsBoolean());
            } catch (Exception ignored) {
            }
            try {
                SHOW_ANNOUNCEMENT_MESSAGES.set(jsonConfig.get(SHOW_ANNOUNCEMENT_MESSAGES.getId()).getAsBoolean());
            } catch (Exception ignored) {
            }
            try {
                USE_TTS_ANNOUNCEMENTS.set(jsonConfig.get(USE_TTS_ANNOUNCEMENTS.getId()).getAsBoolean());
            } catch (Exception ignored) {
            }
            try {
                HIDE_SPECIAL_RAIL_COLORS.set(jsonConfig.get(HIDE_SPECIAL_RAIL_COLORS.getId()).getAsBoolean());
            } catch (Exception ignored) {
            }
            try {
                HIDE_TRANSLUCENT_PARTS.set(jsonConfig.get(HIDE_TRANSLUCENT_PARTS.getId()).getAsBoolean());
            } catch (Exception ignored) {
            }
            try {
                SHIFT_TO_TOGGLE_SITTING.set(jsonConfig.get(SHIFT_TO_TOGGLE_SITTING.getId()).getAsBoolean());
            } catch (Exception ignored) {
            }
            try {
                LANGUAGE_OPTIONS.set(jsonConfig.get(LANGUAGE_OPTIONS.getId()).getAsInt());
            } catch (Exception ignored) {
            }
            try {
                TRACK_TEXTURE_OFFSET.set(jsonConfig.get(TRACK_TEXTURE_OFFSET.getId()).getAsInt());
            } catch (Exception ignored) {
            }
            try {
                DYNAMIC_TEXTURE_RESOLUTION.set(jsonConfig.get(DYNAMIC_TEXTURE_RESOLUTION.getId()).getAsInt());
            } catch (Exception ignored) {
            }
            try {
                TRAIN_RENDER_DISTANCE_RATIO.set(jsonConfig.get(TRAIN_RENDER_DISTANCE_RATIO.getId()).getAsInt());
            } catch (Exception ignored) {
            }
        } catch (Exception e) {
            writeToFile();
            Init.logException(e);
        }
    }

    private static void writeToFile() {
        Init.LOGGER.info("Wrote Minecraft Transit Railway mod config to file");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty(USE_MTR_FONT.getId(), USE_MTR_FONT.get());
        jsonConfig.addProperty(SHOW_ANNOUNCEMENT_MESSAGES.getId(), SHOW_ANNOUNCEMENT_MESSAGES.get());
        jsonConfig.addProperty(USE_TTS_ANNOUNCEMENTS.getId(), USE_TTS_ANNOUNCEMENTS.get());
        jsonConfig.addProperty(HIDE_SPECIAL_RAIL_COLORS.getId(), HIDE_SPECIAL_RAIL_COLORS.get());
        jsonConfig.addProperty(HIDE_TRANSLUCENT_PARTS.getId(), HIDE_TRANSLUCENT_PARTS.get());
        jsonConfig.addProperty(SHIFT_TO_TOGGLE_SITTING.getId(), SHIFT_TO_TOGGLE_SITTING.get());
        jsonConfig.addProperty(LANGUAGE_OPTIONS.getId(), LANGUAGE_OPTIONS.get());
        jsonConfig.addProperty(TRACK_TEXTURE_OFFSET.getId(), TRACK_TEXTURE_OFFSET.get());
        jsonConfig.addProperty(DYNAMIC_TEXTURE_RESOLUTION.getId(), DYNAMIC_TEXTURE_RESOLUTION.get());
        jsonConfig.addProperty(TRAIN_RENDER_DISTANCE_RATIO.getId(), TRAIN_RENDER_DISTANCE_RATIO.get());

        try {
            Files.write(CONFIG_FILE_PATH, Collections.singleton(Utilities.prettyPrint(jsonConfig)));
        } catch (Exception e) {
            Init.logException(e);
        }
    }

    public static class Property<T>
    {
        protected T value;
        protected final T defaultValue;
        protected final String id;

        public Property(String id, T defaultValue) {
            this.id = id;
            this.value = defaultValue;
            this.defaultValue = defaultValue;
        }

        public String getId() {
            return id;
        }

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
            writeToFile();
        }

        public T getDefault() {
            return defaultValue;
        }
    }
}
