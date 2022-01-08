package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPSDAPGGlassEndBase;
import mtr.block.BlockPSDTop;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class RenderPSDTop extends RenderRouteBase<BlockPSDTop.TileEntityPSDTop> {

	private static final float END_FRONT_OFFSET = 1 / (Mth.SQRT_OF_TWO * 16);
	private static final float BOTTOM_DIAGONAL_OFFSET = ((float) Math.sqrt(3) - 1) / 32;
	private static final float ROOT_TWO_SCALED = Mth.SQRT_OF_TWO / 16;
	private static final float BOTTOM_END_DIAGONAL_OFFSET = END_FRONT_OFFSET - BOTTOM_DIAGONAL_OFFSET / Mth.SQRT_OF_TWO;
	private static final float COLOR_STRIP_START = 0.90625F;
	private static final float COLOR_STRIP_END = 0.9375F;

	public RenderPSDTop(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher, 2 - SMALL_OFFSET_16, 0.125F, 1.5F, 7.5F, true);
	}

	@Override
	protected RenderType getRenderType(BlockGetter world, BlockPos pos, BlockState state) {
		if (world.getBlockState(pos.below()).getBlock() instanceof BlockPSDAPGDoorBase) {
			return RenderType.ARROW;
		} else if (!(world.getBlockState(pos.below()).getBlock() instanceof BlockPSDAPGGlassEndBase)) {
			return RenderType.ROUTE;
		} else {
			return RenderType.NONE;
		}
	}

	@Override
	protected void renderAdditionalUnmodified(PoseStack matrices, MultiBufferSource vertexConsumers, BlockState state, Direction facing, int light) {
		final boolean airLeft = IBlock.getStatePropertySafe(state, BlockPSDTop.AIR_LEFT);
		final boolean airRight = IBlock.getStatePropertySafe(state, BlockPSDTop.AIR_RIGHT);
		if (!airLeft && !airRight) {
			return;
		}

		final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new ResourceLocation("mtr:textures/block/psd_top.png")));

		if (airLeft) {
			// back
			IDrawing.drawTexture(matrices, vertexConsumer, -0.125F, 0, 0.5F, 0.5F, 0, -0.125F, 0.5F, 1, -0.125F, -0.125F, 1, 0.5F, 0, 0, 1, 1, facing, -1, light);
			// front
			IDrawing.drawTexture(matrices, vertexConsumer, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, -0.25F - END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, -0.25F - END_FRONT_OFFSET, 1, 0.25F - END_FRONT_OFFSET, 0.5F - END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0, 0, 1, 0.9375F, facing.getOpposite(), -1, light);
			// top curve
			IDrawing.drawTexture(matrices, vertexConsumer, 0.5F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, -0.25F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, -0.25F - END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0, 0.9375F, 1, 0.96875F, facing.getOpposite(), -1, light);
			// bottom curve
			IDrawing.drawTexture(matrices, vertexConsumer, 0.5F, 0, -0.5F, -0.25F, 0, 0.25F, -0.25F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, 0.5F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, 0, 0.96875F, 1, 1, facing.getOpposite(), -1, light);
			// bottom
			IDrawing.drawTexture(matrices, vertexConsumer, 0.5F, SMALL_OFFSET, -0.125F, -0.125F, SMALL_OFFSET, 0.5F, -0.125F, SMALL_OFFSET, 0.125F, 0.5F, SMALL_OFFSET, -0.5F, 0.125F, 0.125F, 0.1875F, 0.1875F, facing, -1, light);
			// top
			IDrawing.drawTexture(matrices, vertexConsumer, 0.5F, 1 - SMALL_OFFSET, -0.5F, -0.125F, 1 - SMALL_OFFSET, 0.125F, -0.125F, 1 - SMALL_OFFSET, 0.5F, 0.5F, 1 - SMALL_OFFSET, -0.125F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
			// top front
			IDrawing.drawTexture(matrices, vertexConsumer, 0.5F - END_FRONT_OFFSET, 1 - SMALL_OFFSET, -0.5F - END_FRONT_OFFSET, -0.125F - ROOT_TWO_SCALED, 1 - SMALL_OFFSET, 0.125F, -0.125F, 1 - SMALL_OFFSET, 0.125F, 0.5F, 1 - SMALL_OFFSET, -0.5F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
			// left side diagonal
			IDrawing.drawTexture(matrices, vertexConsumer, 0.5F, 0.0625F, -0.5F, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0.5F - END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0.5F, 1, -0.5F, 0.9375F, 0, 1, 0.9375F, facing, -1, light);
			// left side diagonal square
			IDrawing.drawTexture(matrices, vertexConsumer, 0.5F, 0, -0.5F, 0.5F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0.5F, 0.0625F, -0.5F, 0.9375F, 0.9375F, 1, 1, facing, -1, light);
		}
		if (airRight) {
			// back
			IDrawing.drawTexture(matrices, vertexConsumer, -0.5F, 0, -0.125F, 0.125F, 0, 0.5F, 0.125F, 1, 0.5F, -0.5F, 1, -0.125F, 0, 0, 1, 1, facing, -1, light);
			// front
			IDrawing.drawTexture(matrices, vertexConsumer, 0.25F + END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, -0.5F + END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0.25F + END_FRONT_OFFSET, 1, 0.25F - END_FRONT_OFFSET, 0, 0, 1, 0.9375F, facing.getOpposite(), -1, light);
			// top curve
			IDrawing.drawTexture(matrices, vertexConsumer, 0.25F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, -0.5F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0.25F + END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, 0, 0.9375F, 1, 0.96875F, facing.getOpposite(), -1, light);
			// bottom curve
			IDrawing.drawTexture(matrices, vertexConsumer, 0.25F, 0, 0.25F, -0.5F, 0, -0.5F, -0.5F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, 0.25F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, 0, 0.96875F, 1, 1, facing.getOpposite(), -1, light);
			// bottom
			IDrawing.drawTexture(matrices, vertexConsumer, 0.125F, SMALL_OFFSET, 0.5F, -0.5F, SMALL_OFFSET, -0.125F, -0.5F, SMALL_OFFSET, -0.5F, 0.125F, SMALL_OFFSET, 0.125F, 0.125F, 0.125F, 0.1875F, 0.1875F, facing, -1, light);
			// top
			IDrawing.drawTexture(matrices, vertexConsumer, 0.125F, 1 - SMALL_OFFSET, 0.125F, -0.5F, 1 - SMALL_OFFSET, -0.5F, -0.5F, 1 - SMALL_OFFSET, -0.125F, 0.125F, 1 - SMALL_OFFSET, 0.5F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
			// top front
			IDrawing.drawTexture(matrices, vertexConsumer, 0.125F + ROOT_TWO_SCALED, 1 - SMALL_OFFSET, 0.125F, -0.5F + END_FRONT_OFFSET, 1 - SMALL_OFFSET, -0.5F - END_FRONT_OFFSET, -0.5F, 1 - SMALL_OFFSET, -0.5F, 0.125F, 1 - SMALL_OFFSET, 0.125F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
			// left side diagonal
			IDrawing.drawTexture(matrices, vertexConsumer, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, -0.5F, 0.0625F, -0.5F, -0.5F, 1, -0.5F, -0.5F + END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0, 0, 0.0625F, 0.9375F, facing, -1, light);
			// left side diagonal square
			IDrawing.drawTexture(matrices, vertexConsumer, -0.5F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, -0.5F, 0, -0.5F, -0.5F, 0.0625F, -0.5F, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0, 0.9375F, 0.0625F, 1, facing, -1, light);
		}
	}

	@Override
	protected void renderAdditional(PoseStack matrices, MultiBufferSource vertexConsumers, long platformId, BlockState state, int glassLength, Direction facing, int light) {
		final boolean airLeft = IBlock.getStatePropertySafe(state, BlockPSDTop.AIR_LEFT);
		final boolean airRight = IBlock.getStatePropertySafe(state, BlockPSDTop.AIR_RIGHT);
		final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(ClientData.DATA_CACHE.getColorStrip(platformId)));
		IDrawing.drawTexture(matrices, vertexConsumer, airLeft ? 0.625F : 0, COLOR_STRIP_START, 0, airRight ? 0.375F : 1, COLOR_STRIP_END, 0, facing, -1, light);
		if (airLeft) {
			IDrawing.drawTexture(matrices, vertexConsumer, END_FRONT_OFFSET, COLOR_STRIP_START, -0.625F - END_FRONT_OFFSET, 0.75F + END_FRONT_OFFSET, COLOR_STRIP_END, 0.125F - END_FRONT_OFFSET, facing, -1, light);
		}
		if (airRight) {
			IDrawing.drawTexture(matrices, vertexConsumer, 0.25F - END_FRONT_OFFSET, COLOR_STRIP_START, 0.125F - END_FRONT_OFFSET, 1 - END_FRONT_OFFSET, COLOR_STRIP_END, -0.625F - END_FRONT_OFFSET, facing, -1, light);
		}
	}
}
