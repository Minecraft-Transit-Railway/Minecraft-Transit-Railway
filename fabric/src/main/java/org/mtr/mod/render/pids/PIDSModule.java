package org.mtr.mod.render.pids;

import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.block.BlockPIDSBase;
import org.mtr.mod.render.RenderModularPIDS;

public abstract class PIDSModule {
    protected final float x;
    protected final float y;
    protected final float width;
    protected final float height;
    protected int layer;

    public PIDSModule(float x, float y, float width, float height, ReaderBase data) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }

    public abstract void render(GraphicsHolder graphicsHolder, ObjectList<ArrivalResponse> arrivals, RenderModularPIDS renderPIDS, BlockPIDSBase.BlockEntityBase entity, BlockPos blockPos, Direction facing);
}
