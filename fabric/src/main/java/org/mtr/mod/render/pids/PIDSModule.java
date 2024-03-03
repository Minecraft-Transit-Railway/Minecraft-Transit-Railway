package org.mtr.mod.render.pids;

import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.core.operation.ArrivalResponse;

import java.util.List;

public abstract class PIDSModule {
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final int arrivalNum;

    public PIDSModule(float x, float y, float width, float height, int arrivalNum) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.arrivalNum = arrivalNum;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public abstract void render(ObjectImmutableList<ArrivalResponse> arrivals);
}
