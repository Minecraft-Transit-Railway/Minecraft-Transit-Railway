package org.mtr.mod.render.pids;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.ReaderBase;

import java.util.ArrayList;

public class StopsAtModule extends TextModule {
    public final String type = "stopsAt";

    public StopsAtModule(float x, float y, float width, float height, ReaderBase data) {
        super(x, y, width, height, data);
    }

    @Override
    protected ArrayList<String> getText(ObjectImmutableList<ArrivalResponse> arrivals) {
        ArrayList<String> text = new ArrayList<>();
        text.add("Fairview Docks"); //TODO - add actual stops
        return text;
    }
}
