package org.mtr.mod.render.pids;

import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockPIDSBase;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.RenderPIDS;

import java.util.ArrayList;
import java.util.Objects;

public class TextModule extends PIDSModule {
    public final String type = "text";
    protected IGui.HorizontalAlignment align = IGui.HorizontalAlignment.LEFT;

    protected int layer = 1;
    protected String colorMode = "basic";
    protected int color = 0xFFFFFF;
    protected int arrival = 0;
    protected String template = "";

    public TextModule(float x, float y, float width, float height, ReaderBase data) {
        super(x, y, width, height, data);
        data.unpackString("align", (value) -> align = Objects.equals(value, "right") ? IGui.HorizontalAlignment.RIGHT : Objects.equals(value, "center") ? IGui.HorizontalAlignment.CENTER : IGui.HorizontalAlignment.LEFT);
        data.unpackString("colorMode", (value) -> colorMode = value);
        data.unpackInt("color", (value) -> color = value);
        data.unpackInt("arrival", (value) -> arrival = value);
        data.unpackString("template", (value) -> template = value);
        data.unpackInt("layer", (value) -> this.layer = value);
    }

    protected ArrayList<String> getText(ObjectList<ArrivalResponse> arrivals) {
        ArrayList<String> text = new ArrayList<>();
        text.add("");
        return text;
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, ObjectList<ArrivalResponse> arrivals, RenderPIDS renderPIDS, BlockPIDSBase.BlockEntityBase entity, BlockPos blockPos, Direction facing) {
        render(graphicsHolder, arrivals, renderPIDS, entity, blockPos, facing, null);
    }

    public void render(GraphicsHolder graphicsHolder, ObjectList<ArrivalResponse> arrivals, RenderPIDS renderPIDS, BlockPIDSBase.BlockEntityBase entity, BlockPos blockPos, Direction facing, String textOverride) {
        final float textPadding = height * 0.1f;
        ArrayList<String> placeholders = getText(arrivals);
        if (placeholders == null || placeholders.isEmpty()) {
            return;
        }
        String text;
        if (textOverride != null) {
            text = textOverride;
        } else {
            String[] getText = placeholders.toArray(String[]::new);
            String[] texts = getText[0].split("\\|\\|")[0].replaceAll("\\\\\\|", "^TEMP^").split("\\|");
            int textIndex = (int) Math.floor((double) MinecraftClient.getInstance().getWorldMapped().getTime() / RenderPIDS.SWITCH_TEXT_TICKS) % texts.length;
            getText[0] = texts[textIndex].replaceAll("\\^TEMP\\^", "|");

            String template = this.template.split("\\|\\|")[0];
            String[] templates = template.replaceAll("\\|", "^TEMP^").split("\\|");
            int templateIndex = (int) Math.floor((double) MinecraftClient.getInstance().getWorldMapped().getTime() / RenderPIDS.SWITCH_TEXT_TICKS) % templates.length;
            text = String.format(templates[templateIndex].replaceAll("\\^TEMP\\^", "|"), (Object[]) getText);
        }

        int color = this.color;
        if (this.arrival < arrivals.size()) {
            ArrivalResponse arrival = arrivals.get(this.arrival);
            if (arrival != null) {
                if (colorMode.equals("line")) {
                    color = arrival.getRouteColor();
                } else if (colorMode.equals("station")) {
                    color = InitClient.findStation(blockPos).getColor();
                }
            }
        }

        RenderPIDS.renderText(graphicsHolder, text, x + textPadding, y + textPadding, height - textPadding * 2, color, width - textPadding * 2, align, layer);
    }

    public int getArrival() {
        return arrival;
    }
}
