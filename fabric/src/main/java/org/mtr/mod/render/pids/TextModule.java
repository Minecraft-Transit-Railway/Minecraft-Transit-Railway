package org.mtr.mod.render.pids;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.render.RenderPIDS;

import java.util.ArrayList;
import java.util.Objects;

public class TextModule extends PIDSModule {
    public final String type = "text";
    protected AlignType align = AlignType.LEFT;
    public enum AlignType {
        RIGHT,
        LEFT,
        CENTER
    }

    protected int color = 0xFFFFFF;
    protected int arrival = 0;
    protected String template = "";

    public TextModule(float x, float y, float width, float height, ReaderBase data) {
        super(x, y, width, height, data);
        data.unpackString("align", (value) -> align = Objects.equals(value, "right") ? AlignType.RIGHT : Objects.equals(value, "center") ? AlignType.CENTER : AlignType.LEFT);
        data.unpackInt("color", (value) -> color = value);
        data.unpackInt("arrival", (value) -> arrival = value);
        data.unpackString("template", (value) -> template = value);
    }

    protected ArrayList<String> getText(ObjectImmutableList<ArrivalResponse> arrivals, int offset) {
        ArrayList<String> text = new ArrayList<>();
        text.add("");
        return text;
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, ObjectImmutableList<ArrivalResponse> arrivals) {
        final float textPadding = height * 0.1f;
        ArrayList<String> placeholders = getText(arrivals, 0);
        if (placeholders == null || placeholders.isEmpty()) {
            return;
        }
        String text = template.formatted(placeholders.toArray());
        RenderPIDS.renderText(graphicsHolder, text, x + textPadding, y + textPadding, height - textPadding * 2, color, width - textPadding * 2, align);
    }

    public int getArrival() {
        return arrival;
    }
}
