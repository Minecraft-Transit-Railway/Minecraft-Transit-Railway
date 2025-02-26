package org.mtr.mod.render;

import org.mtr.mapping.holder.*;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockPSDAPGDoorBase;
import org.mtr.mod.block.BlockPSDAPGGlassEndBase;
import org.mtr.mod.block.BlockPSDTop;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.IDrawing;

public class RenderPSDTop extends RenderRouteBase<BlockPSDTop.BlockEntity> {

	private static final float END_FRONT_OFFSET = 1 / (MathHelper.getSquareRootOfTwoMapped() * 16);
	private static final float BOTTOM_DIAGONAL_OFFSET = ((float) Math.sqrt(3) - 1) / 32;
	private static final float ROOT_TWO_SCALED = MathHelper.getSquareRootOfTwoMapped() / 16;
	private static final float BOTTOM_END_DIAGONAL_OFFSET = END_FRONT_OFFSET - BOTTOM_DIAGONAL_OFFSET / MathHelper.getSquareRootOfTwoMapped();
	private static final float COLOR_STRIP_START = 14.5F / 16;
	private static final float COLOR_STRIP_END = 15 / 16F;

	public RenderPSDTop(Argument dispatcher) {
		super(dispatcher, 2 - SMALL_OFFSET_16, 7.5F, 1.5F, 0.125F, true, 3, BlockPSDTop.ARROW_DIRECTION);
	}

	@Override
	protected RenderType getRenderType(World world, BlockPos pos, BlockState state) {
		final BlockPSDTop.EnumPersistent persistent = IBlock.getStatePropertySafe(state, BlockPSDTop.PERSISTENT);
		if (persistent == BlockPSDTop.EnumPersistent.NONE) {
			final Block blockBelow = world.getBlockState(pos.down()).getBlock();
			if (blockBelow.data instanceof BlockPSDAPGDoorBase) {
				return RenderType.ARROW;
			} else if (!(blockBelow.data instanceof BlockPSDAPGGlassEndBase)) {
				return RenderType.ROUTE;
			} else {
				return RenderType.NONE;
			}
		} else {
			return persistent == BlockPSDTop.EnumPersistent.ARROW ? RenderType.ARROW : persistent == BlockPSDTop.EnumPersistent.ROUTE ? RenderType.ROUTE : RenderType.NONE;
		}
	}

	@Override
	protected void renderAdditionalUnmodified(StoredMatrixTransformations storedMatrixTransformations, BlockState state, Direction facing, int light) {
		final boolean airLeft = IBlock.getStatePropertySafe(state, BlockPSDTop.AIR_LEFT);
		final boolean airRight = IBlock.getStatePropertySafe(state, BlockPSDTop.AIR_RIGHT);
		final boolean persistent = IBlock.getStatePropertySafe(state, BlockPSDTop.PERSISTENT) != BlockPSDTop.EnumPersistent.NONE;
		if (!airLeft && !airRight || persistent) {
			return;
		}
		MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/psd_top.png"), false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			if (airLeft) {
				// back
				IDrawing.drawTexture(graphicsHolder, -0.125F, 0, 0.5F, 0.5F, 0, -0.125F, 0.5F, 1, -0.125F, -0.125F, 1, 0.5F, 0, 0, 1, 1, facing, -1, light);
				// front
				IDrawing.drawTexture(graphicsHolder, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, -0.25F - END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, -0.25F - END_FRONT_OFFSET, 1, 0.25F - END_FRONT_OFFSET, 0.5F - END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0, 0, 1, 0.9375F, facing.getOpposite(), -1, light);
				// top curve
				IDrawing.drawTexture(graphicsHolder, 0.5F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, -0.25F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, -0.25F - END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0, 0.9375F, 1, 0.96875F, facing.getOpposite(), -1, light);
				// bottom curve
				IDrawing.drawTexture(graphicsHolder, 0.5F, 0, -0.5F, -0.25F, 0, 0.25F, -0.25F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, 0.5F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, 0, 0.96875F, 1, 1, facing.getOpposite(), -1, light);
				// bottom
				IDrawing.drawTexture(graphicsHolder, 0.5F, SMALL_OFFSET, -0.125F, -0.125F, SMALL_OFFSET, 0.5F, -0.125F, SMALL_OFFSET, 0.125F, 0.5F, SMALL_OFFSET, -0.5F, 0.125F, 0.125F, 0.1875F, 0.1875F, facing, -1, light);
				// top
				IDrawing.drawTexture(graphicsHolder, 0.5F, 1 - SMALL_OFFSET, -0.5F, -0.125F, 1 - SMALL_OFFSET, 0.125F, -0.125F, 1 - SMALL_OFFSET, 0.5F, 0.5F, 1 - SMALL_OFFSET, -0.125F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
				// top front
				IDrawing.drawTexture(graphicsHolder, 0.5F - END_FRONT_OFFSET, 1 - SMALL_OFFSET, -0.5F - END_FRONT_OFFSET, -0.125F - ROOT_TWO_SCALED, 1 - SMALL_OFFSET, 0.125F, -0.125F, 1 - SMALL_OFFSET, 0.125F, 0.5F, 1 - SMALL_OFFSET, -0.5F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
				// left side diagonal
				IDrawing.drawTexture(graphicsHolder, 0.5F, 0.0625F, -0.5F, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0.5F - END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0.5F, 1, -0.5F, 0.9375F, 0, 1, 0.9375F, facing, -1, light);
				// left side diagonal square
				IDrawing.drawTexture(graphicsHolder, 0.5F, 0, -0.5F, 0.5F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0.5F, 0.0625F, -0.5F, 0.9375F, 0.9375F, 1, 1, facing, -1, light);
			}
			if (airRight) {
				// back
				IDrawing.drawTexture(graphicsHolder, -0.5F, 0, -0.125F, 0.125F, 0, 0.5F, 0.125F, 1, 0.5F, -0.5F, 1, -0.125F, 0, 0, 1, 1, facing, -1, light);
				// front
				IDrawing.drawTexture(graphicsHolder, 0.25F + END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, -0.5F + END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0.25F + END_FRONT_OFFSET, 1, 0.25F - END_FRONT_OFFSET, 0, 0, 1, 0.9375F, facing.getOpposite(), -1, light);
				// top curve
				IDrawing.drawTexture(graphicsHolder, 0.25F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, -0.5F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0.25F + END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, 0, 0.9375F, 1, 0.96875F, facing.getOpposite(), -1, light);
				// bottom curve
				IDrawing.drawTexture(graphicsHolder, 0.25F, 0, 0.25F, -0.5F, 0, -0.5F, -0.5F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, 0.25F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, 0, 0.96875F, 1, 1, facing.getOpposite(), -1, light);
				// bottom
				IDrawing.drawTexture(graphicsHolder, 0.125F, SMALL_OFFSET, 0.5F, -0.5F, SMALL_OFFSET, -0.125F, -0.5F, SMALL_OFFSET, -0.5F, 0.125F, SMALL_OFFSET, 0.125F, 0.125F, 0.125F, 0.1875F, 0.1875F, facing, -1, light);
				// top
				IDrawing.drawTexture(graphicsHolder, 0.125F, 1 - SMALL_OFFSET, 0.125F, -0.5F, 1 - SMALL_OFFSET, -0.5F, -0.5F, 1 - SMALL_OFFSET, -0.125F, 0.125F, 1 - SMALL_OFFSET, 0.5F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
				// top front
				IDrawing.drawTexture(graphicsHolder, 0.125F + ROOT_TWO_SCALED, 1 - SMALL_OFFSET, 0.125F, -0.5F + END_FRONT_OFFSET, 1 - SMALL_OFFSET, -0.5F - END_FRONT_OFFSET, -0.5F, 1 - SMALL_OFFSET, -0.5F, 0.125F, 1 - SMALL_OFFSET, 0.125F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
				// left side diagonal
				IDrawing.drawTexture(graphicsHolder, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, -0.5F, 0.0625F, -0.5F, -0.5F, 1, -0.5F, -0.5F + END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0, 0, 0.0625F, 0.9375F, facing, -1, light);
				// left side diagonal square
				IDrawing.drawTexture(graphicsHolder, -0.5F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, -0.5F, 0, -0.5F, -0.5F, 0.0625F, -0.5F, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0, 0.9375F, 0.0625F, 1, facing, -1, light);
			}
			graphicsHolder.pop();
		});
	}

	@Override
	protected void renderAdditional(StoredMatrixTransformations storedMatrixTransformations, long platformId, BlockState state, int leftBlocks, int rightBlocks, Direction facing, int color, int light) {
		final boolean isNotPersistent = IBlock.getStatePropertySafe(state, BlockPSDTop.PERSISTENT) == BlockPSDTop.EnumPersistent.NONE;
		final boolean airLeft = isNotPersistent && IBlock.getStatePropertySafe(state, BlockPSDTop.AIR_LEFT);
		final boolean airRight = isNotPersistent && IBlock.getStatePropertySafe(state, BlockPSDTop.AIR_RIGHT);
		MainRenderer.scheduleRender(DynamicTextureCache.instance.getColorStrip(platformId).identifier, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			IDrawing.drawTexture(graphicsHolder, airLeft ? 0.625F : 0, COLOR_STRIP_START, 0, airRight ? 0.375F : 1, COLOR_STRIP_END, 0, facing, color, light);
			if (airLeft) {
				IDrawing.drawTexture(graphicsHolder, END_FRONT_OFFSET, COLOR_STRIP_START, -0.625F - END_FRONT_OFFSET, 0.75F + END_FRONT_OFFSET, COLOR_STRIP_END, 0.125F - END_FRONT_OFFSET, facing, -1, light);
			}
			if (airRight) {
				IDrawing.drawTexture(graphicsHolder, 0.25F - END_FRONT_OFFSET, COLOR_STRIP_START, 0.125F - END_FRONT_OFFSET, 1 - END_FRONT_OFFSET, COLOR_STRIP_END, -0.625F - END_FRONT_OFFSET, facing, -1, light);
			}
			graphicsHolder.pop();
		});
	}

	@Override
	protected float getAdditionalOffset(BlockState state) {
		return IBlock.getStatePropertySafe(state, BlockPSDTop.PERSISTENT) == BlockPSDTop.EnumPersistent.NONE ? 0 : BlockPSDTop.PERSISTENT_OFFSET_SMALL;
	}
}
