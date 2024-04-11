package org.mtr.mod.render.pids;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.ReaderBase;

import java.util.ArrayList;

public class TrainLengthModule extends TextModule {
    public final String type = "trainLength";

    public TrainLengthModule(float x, float y, float width, float height, ReaderBase data) {
        super(x, y, width, height, data);
    }

    @Override
    protected ArrayList<String> getText(ObjectImmutableList<ArrivalResponse> arrivals, int offset) {
        ArrayList<String> text = new ArrayList<>();
        ArrivalResponse arrivalResponse = Utilities.getElement(arrivals, arrival + offset);
        if (arrivalResponse == null) {
            return null;
        }
        text.add(String.valueOf(arrivalResponse.getCarCount()));
        return text;
    }
}
