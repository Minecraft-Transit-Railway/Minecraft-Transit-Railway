package org.mtr.mod.render.pids;

import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.core.operation.ArrivalResponse;

import java.util.List;

public class DestinationModule extends PIDSModule {
    public DestinationModule(float x, float y, float width, float height, int arrivalNum) {
        super(x, y, width, height, arrivalNum);
    }

    public void render(ObjectImmutableList<ArrivalResponse> arrivals) {
        // TODO rendering
    }
}
