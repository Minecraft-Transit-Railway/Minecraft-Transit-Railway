package org.mtr.mod.render.pids;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.ReaderBase;

import java.util.ArrayList;

public class PlatformNumberModule extends TextModule {
    public final String type = "platformNumber";

    public PlatformNumberModule(float x, float y, float width, float height, ReaderBase data) {
        super(x, y, width, height, data);
    }

    @Override
    protected ArrayList<String> getText(ObjectImmutableList<ArrivalResponse> arrivals) {
        ArrayList<String> text = new ArrayList<>();
        ArrivalResponse arrivalResponse = Utilities.getElement(arrivals, arrival);
        if (arrivalResponse == null) {
            return null;
        }
        text.add(String.valueOf(arrivalResponse.getPlatformName()));
        return text;
    }
}
