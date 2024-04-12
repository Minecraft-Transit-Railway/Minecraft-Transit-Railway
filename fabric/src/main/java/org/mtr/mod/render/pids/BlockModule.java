package org.mtr.mod.render.pids;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.mapping.mapper.GraphicsHolder;

public class BlockModule extends PIDSModule {
    public final String type = "block";

    protected int color = 0xFFFFFF;

    public BlockModule(float x, float y, float width, float height, ReaderBase data) {
        super(x, y, width, height, data);
        data.unpackInt("color", (value) -> color = value);
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, ObjectImmutableList<ArrivalResponse> arrivals) {
        // TODO rendering
    }
}
