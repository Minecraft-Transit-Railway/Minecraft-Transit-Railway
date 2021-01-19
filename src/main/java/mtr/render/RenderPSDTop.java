package mtr.render;

import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPSDAPGGlassEndBase;
import mtr.block.BlockPSDTop;
import mtr.block.IBlock;
import mtr.model.PSDTopModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public class RenderPSDTop extends RenderRouteBase<BlockPSDTop.TileEntityPSDTop> implements IBlock {

	private static final float COLOR_STRIP_START = 0.90625F;
	private static final float COLOR_STRIP_END = 0.9375F;

	public RenderPSDTop(BlockEntityRenderDispatcher dispatcher) {
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
		if (world.getBlockState(pos.down()).getBlock() instanceof BlockPSDAPGDoorBase) {
			return RenderType.ARROW;
		} else if (!(world.getBlockState(pos.down()).getBlock() instanceof BlockPSDAPGGlassEndBase)) {
			return RenderType.ROUTE;
		} else {
			return RenderType.NONE;
		}
	}

	@Override
	protected void renderAdditional(MatrixStack matrices, VertexConsumerProvider vertexConsumers, RouteRenderer routeRenderer, BlockState state, int light) {
		final boolean airLeft = IBlock.getStatePropertySafe(state, BlockPSDTop.AIR_LEFT);
		final boolean airRight = IBlock.getStatePropertySafe(state, BlockPSDTop.AIR_RIGHT);
		routeRenderer.renderColorStrip(airLeft ? 0.625F : 0, COLOR_STRIP_START, 0, airRight ? 0.375F : 1, COLOR_STRIP_END, 0, light);
		if (airLeft) {
			routeRenderer.renderColorStrip(PSDTopModel.END_FRONT_OFFSET, COLOR_STRIP_START, -0.625F - PSDTopModel.END_FRONT_OFFSET, 0.75F + PSDTopModel.END_FRONT_OFFSET, COLOR_STRIP_END, 0.125F - PSDTopModel.END_FRONT_OFFSET, light);
		}
		if (airRight) {
			routeRenderer.renderColorStrip(0.25F - PSDTopModel.END_FRONT_OFFSET, COLOR_STRIP_START, 0.125F - PSDTopModel.END_FRONT_OFFSET, 1 - PSDTopModel.END_FRONT_OFFSET, COLOR_STRIP_END, -0.625F - PSDTopModel.END_FRONT_OFFSET, light);
		}
	}
}
