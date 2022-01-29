package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.block.BlockAPGGlass;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class RenderAPGGlass extends RenderRouteBase<BlockAPGGlass.TileEntityAPGGlass> {

	private static final float COLOR_STRIP_START = 0.75F;
	private static final float COLOR_STRIP_END = 0.78125F;

	public RenderAPGGlass(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher, 4, 8, 4, 8, false);
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
	protected void renderAdditional(PoseStack matrices, MultiBufferSource vertexConsumers, long platformId, BlockState state, int glassLength, Direction facing, int light) {
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER && IBlock.getStatePropertySafe(state, SIDE_EXTENDED) != EnumSide.SINGLE) {
			final boolean isLeft = isLeft(state);
			final boolean isRight = isRight(state);
			final VertexConsumer vertexConsumer1 = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(ClientData.DATA_CACHE.getColorStrip(platformId)));
			IDrawing.drawTexture(matrices, vertexConsumer1, isLeft ? sidePadding : 0, COLOR_STRIP_START, 0, isRight ? 1 - sidePadding : 1, COLOR_STRIP_END, 0, facing, -1, light);
			IDrawing.drawTexture(matrices, vertexConsumer1, isRight ? 1 - sidePadding : 1, COLOR_STRIP_START, 0.125F, isLeft ? sidePadding : 0, COLOR_STRIP_END, 0.125F, facing, -1, light);

			if (glassLength > 0) {
				final float width = glassLength - sidePadding * 2;
				final float height = 1 - topPadding - bottomPadding;
				final VertexConsumer vertexConsumer2 = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(ClientData.DATA_CACHE.getStationName(platformId, width / height)));
				IDrawing.drawTexture(matrices, vertexConsumer2, glassLength - sidePadding, 1 - topPadding, 0.125F, sidePadding, 1 - bottomPadding, 0.125F, facing, -1, light);
			}
		}
	}
}
