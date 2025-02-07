package org.mtr.mod.render.pids;

import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockPIDSBase;
import org.mtr.mod.render.RenderModularPIDS;

import java.time.ZonedDateTime;
import java.util.regex.Matcher;

public class TemplateModule extends TextModule {
    public final String type = "destination";

    public TemplateModule(float x, float y, float width, float height, ReaderBase data) {
        super(x, y, width, height, data);
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, ObjectList<ArrivalResponse> arrivals, RenderModularPIDS renderPIDS, BlockPIDSBase.BlockEntityBase entity, BlockPos blockPos, Direction facing) {
        ArrivalResponse arrivalResponse = Utilities.getElement(arrivals, arrival);
        if (arrivalResponse == null) {
            return;
        }

        String text = useTemplate(template, "$s", InitClient.findStation(blockPos).getName());
        text = useTemplate(text, "$d", arrivalResponse.getDestination());
        text = useTemplate(text, "$p", arrivalResponse.getPlatformName());
        text = useTemplate(text, "$l", String.valueOf(arrivalResponse.getCarCount()));
        text = useTemplate(text, "$n", arrivalResponse.getRouteName());

        //Time Identifiers
        if (template.contains("$t")) {
            ZonedDateTime date = ZonedDateTime.now();
            int time = (int) ((date.toEpochSecond() + date.getOffset().getTotalSeconds()) % 86400);
            String ampm = time >= 43200 ? "PM" : "AM";
            int seconds = (int) Math.floor(time % 60F);
            int minutes = (int) Math.floor(time % 3600 / 60F);
            int hours = (int) Math.floor(time / 3600F);
            int hours12 = hours % 12;
            hours12 = hours12 == 0 ? 12 : hours12;
            // These identifiers will never have | or ||, so the wrapper is skipped
            text = text
                .replaceAll("\\$ts", String.format("%02d", seconds))
                .replaceAll("\\$tm", String.format("%02d", minutes))
                .replaceAll("\\$th2", String.valueOf(hours12))
                .replaceAll("\\$th4", String.valueOf(hours))
                .replaceAll("\\$tap", ampm);
        }

        //Game Time Identifiers
        if (template.contains("$tg")) {
            int time = (int) MinecraftClient.getInstance().getWorldMapped().getTime();
            time = ((int) Math.floor(time * 72F / 20) + 21600) % 86400;
            String ampm = time >= 43200 ? "PM" : "AM";
            int seconds = (int) Math.floor(time % 60F);
            int minutes = (int) Math.floor(time % 3600 / 60F);
            int hours = (int) Math.floor(time / 3600F);
            int hours12 = hours % 12;
            hours12 = hours12 == 0 ? 12 : hours12;
            // Same as above
            text = text
                .replaceAll("\\$tgs", String.format("%02d", seconds))
                .replaceAll("\\$tgm", String.format("%02d", minutes))
                .replaceAll("\\$tgh2", String.valueOf(hours12))
                .replaceAll("\\$tgh4", String.valueOf(hours))
                .replaceAll("\\$tgap", ampm);
        }

        super.render(graphicsHolder, arrivals, renderPIDS, entity, blockPos, facing, text);
    }

    // Wrapper for identifier replacement that supports | and ||
    private String useTemplate(String string, String template, String value) {
        if (!string.contains(template)) return string;

        String[] texts = value.split("\\|\\|")[0].replaceAll("\\\\\\|", "^TEMP^").split("\\|");
        int textIndex = (int) Math.floor((double) MinecraftClient.getInstance().getWorldMapped().getTime() / RenderModularPIDS.SWITCH_TEXT_TICKS) % texts.length;
        String parseText = texts[textIndex].replaceAll("\\^TEMP\\^", "|");
        return string.replaceAll(Matcher.quoteReplacement(template), parseText);
    }
}
