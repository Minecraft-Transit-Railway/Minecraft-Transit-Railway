package mtr.render;

import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPSDAPGGlassEndBase;
import mtr.block.BlockPSDTop;
import mtr.block.BlockPSDTopOnly;
import mtr.block.IBlock;
import mtr.gui.IDrawing;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldAccess;

public class RenderPSDTopOnly extends RenderRouteBase<BlockPSDTopOnly.TileEntityPSDTopOnly> implements IBlock {

    private static final float END_FRONT_OFFSET = 1 / (MathHelper.SQUARE_ROOT_OF_TWO * 16);
    private static final float BOTTOM_DIAGONAL_OFFSET = ((float) Math.sqrt(3) - 1) / 32;
    private static final float ROOT_TWO_SCALED = MathHelper.SQUARE_ROOT_OF_TWO / 16;
    private static final float BOTTOM_END_DIAGONAL_OFFSET = END_FRONT_OFFSET - BOTTOM_DIAGONAL_OFFSET / MathHelper.SQUARE_ROOT_OF_TWO;
    private static final float COLOR_STRIP_START = 0.90625F;
    private static final float COLOR_STRIP_END = 0.9375F;

    public RenderPSDTopOnly(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected float getZ() {
        return 0.125F;
    }

    @Override
    protected float getSidePadding() {
        return 0.125F;
    }

    @Override
    protected float getBottomPadding() {
        return 0.125F;
    }

    @Override
    protected float getTopPadding() {
        return 0.5F;
    }

    @Override
    protected int getBaseScale() {
        return 320;
    }

    @Override
    protected boolean isLeft(BlockState state) {
        return IBlock.getStatePropertySafe(state, SIDE_EXTENDED) == EnumSide.LEFT;
    }

    @Override
    protected boolean isRight(BlockState state) {
        return IBlock.getStatePropertySafe(state, SIDE_EXTENDED) == EnumSide.RIGHT;
    }

    @Override
    protected RenderType getRenderType(WorldAccess world, BlockPos pos, BlockState state) {
        /*if (world.getBlockState(pos.down()).getBlock() instanceof BlockPSDAPGDoorBase) {
            return RenderType.ARROW;
        } else if (!(world.getBlockState(pos.down()).getBlock() instanceof BlockPSDAPGGlassEndBase)) {
            return RenderType.ROUTE;
        } else {
            return RenderType.NONE;
        }*/
        return RenderType.ROUTE;
    }

    @Override
    protected void renderAdditionalUnmodified(MatrixStack matrices, VertexConsumerProvider vertexConsumers, BlockState state, Direction facing, int light) {
        return; // Ignore AIR_LEFT and AIR_RIGHT because they don't exist.
    }

    @Override
    protected void renderAdditional(MatrixStack matrices, VertexConsumerProvider vertexConsumers, RouteRenderer routeRenderer, BlockState state, Direction facing, int light) {
        routeRenderer.renderColorStrip(0, COLOR_STRIP_START, 0, 1, COLOR_STRIP_END, 0, facing, light);
    }
}