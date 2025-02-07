package org.mtr.mod.render.pids;

import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockPIDSBase;
import org.mtr.mod.render.RenderModularPIDS;

public class BlockModule extends PIDSModule {
    public final String type = "block";

    protected int layer = 0;
    protected String colorMode = "basic";
    protected int color = 0xFFFFFF;
    protected int arrival = 0;

    public BlockModule(float x, float y, float width, float height, ReaderBase data) {
        super(x, y, width, height, data);
        data.unpackString("colorMode", (value) -> colorMode = value);
        data.unpackInt("color", (value) -> color = value);
        data.unpackInt("arrival", (value) -> arrival = value);
        data.unpackInt("layer", (value) -> this.layer = value);
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, ObjectList<ArrivalResponse> arrivals, RenderModularPIDS renderPIDS, BlockPIDSBase.BlockEntityBase entity, BlockPos blockPos, Direction facing) {
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
        renderPIDS.renderRect(entity, blockPos, facing, x, y, width, height, color + 0xFF000000, layer);
    }
}
