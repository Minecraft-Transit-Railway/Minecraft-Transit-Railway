package org.mtr.mod.render.pids;

import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.mapping.holder.MinecraftClient;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class TimeModule extends TextModule {
    public final String type = "time";
    private boolean use24Hour = false;
    private boolean showHours = true;
    private boolean showMinutes = true;
    private boolean showSeconds = true;
    private boolean useRealTime = true;

    public TimeModule(float x, float y, float width, float height, ReaderBase data) {
        super(x, y, width, height, data);
        data.unpackBoolean("show24Hour", (value) -> use24Hour = value);
        data.unpackBoolean("showHours", (value) -> showHours = value);
        data.unpackBoolean("showMinutes", (value) -> showMinutes = value);
        data.unpackBoolean("showSeconds", (value) -> showSeconds = value);
        data.unpackString("loc", (value) -> useRealTime = Objects.equals(value, "s"));
    }

    @Override
    protected ArrayList<String> getText(ObjectList<ArrivalResponse> arrivals) {
        ArrayList<String> text = new ArrayList<>();
        int time = 0;
        if (useRealTime) {
            ZonedDateTime date = ZonedDateTime.now();
            time = (int) ((date.toEpochSecond() + date.getOffset().getTotalSeconds()) % 86400);
        } else {
            if (MinecraftClient.getInstance().getWorldMapped() != null) {
                time = (int) MinecraftClient.getInstance().getWorldMapped().getTime();
                time = ((int) Math.floor(time * 72F / 20) + 21600) % 86400;
            }
        }
        String ampm = time >= 43200 ? "PM" : "AM";
        if (!use24Hour) {
            time = time % 43200;
        }
        final int hours = (int) Math.floor(time / 3600F);
        if (showHours && use24Hour) {
            text.add(String.valueOf(hours == 0 ? 24 : hours));
        } else if (showHours) {
            text.add(String.valueOf(hours == 0 ? 12 : hours));
        }
        if (showMinutes) text.add(String.format("%02d", (int) Math.floor(time % 3600 / 60F)));
        if (showSeconds) text.add(String.format("%02d", (int) Math.floor(time % 60F)));
        if (!use24Hour) text.add(ampm);
        return text;
    }
}
