package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.block.BlockAPGGlass;
import mtr.block.IBlock;
import mtr.data.IGui;
import mtr.gui.IDrawing;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class RenderAPGGlass extends RenderRouteBase<BlockAPGGlass.TileEntityAPGGlass> implements IGui, IBlock {

	private static final float COLOR_STRIP_START = 0.75F;
	private static final float COLOR_STRIP_END = 0.78125F;

	public RenderAPGGlass(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	protected float getZ() {
		return 0.25F;
	}

	@Override
	protected float getSidePadding() {
		return 0.5F;
	}

	@Override
	protected float getBottomPadding() {
		return 0.25F;
	}

	@Override
	protected float getTopPadding() {
		return 0.5F;
	}

	@Override
	protected int getBaseScale() {
		return 480;
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
	protected RenderType getRenderType(BlockGetter world, BlockPos pos, BlockState state) {
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER) {
			return RenderType.NONE;
		} else if ((Math.floorMod(pos.getX(), 8) < 4) == (Math.floorMod(pos.getZ(), 8) < 4)) {
			return RenderType.ARROW;
		} else {
			return RenderType.ROUTE;
		}
	}

	@Override
	protected void renderAdditional(PoseStack matrices, MultiBufferSource vertexConsumers, RouteRenderer routeRenderer, BlockState state, Direction facing, int light) {
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER && IBlock.getStatePropertySafe(state, SIDE_EXTENDED) != EnumSide.SINGLE) {
			final boolean isLeft = isLeft(state);
			final boolean isRight = isRight(state);
			routeRenderer.renderColorStrip(isLeft ? getSidePadding() : 0, COLOR_STRIP_START, 0, isRight ? getSidePadding() : 1, COLOR_STRIP_END, 0, facing, light);
			routeRenderer.renderColorStrip(isRight ? getSidePadding() : 1, COLOR_STRIP_START, 0.125F, isLeft ? getSidePadding() : 0, COLOR_STRIP_END, 0.125F, facing, light);
			final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new ResourceLocation("mtr:textures/block/white.png")));
			IDrawing.drawTexture(matrices, vertexConsumer, isLeft ? getSidePadding() : 0, getTopPadding(), SMALL_OFFSET * 2, isRight ? getSidePadding() : 1, COLOR_STRIP_END, SMALL_OFFSET * 2, facing, ARGB_WHITE, light);
			IDrawing.drawTexture(matrices, vertexConsumer, isRight ? getSidePadding() : 1, getTopPadding(), 0.125F - SMALL_OFFSET * 2, isLeft ? getSidePadding() : 0, COLOR_STRIP_END, 0.125F - SMALL_OFFSET * 2, facing, ARGB_WHITE, light);
		}
	}
}
