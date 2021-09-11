package mtr.render;

import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPSDAPGGlassEndBase;
import mtr.block.BlockPSDTop;
import mtr.block.IBlock;
import mtr.gui.RenderingInstruction;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldAccess;

import java.util.List;

public class RenderPSDTop extends RenderRouteBase<BlockPSDTop.TileEntityPSDTop> implements IBlock {

	private static final float END_FRONT_OFFSET = 1 / (MathHelper.SQUARE_ROOT_OF_TWO * 16);
	private static final float BOTTOM_DIAGONAL_OFFSET = ((float) Math.sqrt(3) - 1) / 32;
	private static final float ROOT_TWO_SCALED = MathHelper.SQUARE_ROOT_OF_TWO / 16;
	private static final float BOTTOM_END_DIAGONAL_OFFSET = END_FRONT_OFFSET - BOTTOM_DIAGONAL_OFFSET / MathHelper.SQUARE_ROOT_OF_TWO;
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
	protected void renderAdditionalUnmodified(List<RenderingInstruction> renderingInstructions, BlockState state, Direction facing, int light) {
		final boolean airLeft = IBlock.getStatePropertySafe(state, BlockPSDTop.AIR_LEFT);
		final boolean airRight = IBlock.getStatePropertySafe(state, BlockPSDTop.AIR_RIGHT);
		if (!airLeft && !airRight) {
			return;
		}

		RenderingInstruction.addVertexConsumer(renderingInstructions, vertexConsumers -> vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new Identifier("mtr:textures/block/psd_top.png"))));

		if (airLeft) {
			// back
			RenderingInstruction.addDrawTexture(renderingInstructions, -0.125F, 0, 0.5F, 0.5F, 0, -0.125F, 0.5F, 1, -0.125F, -0.125F, 1, 0.5F, 0, 0, 1, 1, facing, -1, light);
			// front
			RenderingInstruction.addDrawTexture(renderingInstructions, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, -0.25F - END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, -0.25F - END_FRONT_OFFSET, 1, 0.25F - END_FRONT_OFFSET, 0.5F - END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0, 0, 1, 0.9375F, facing.getOpposite(), -1, light);
			// top curve
			RenderingInstruction.addDrawTexture(renderingInstructions, 0.5F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, -0.25F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, -0.25F - END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0, 0.9375F, 1, 0.96875F, facing.getOpposite(), -1, light);
			// bottom curve
			RenderingInstruction.addDrawTexture(renderingInstructions, 0.5F, 0, -0.5F, -0.25F, 0, 0.25F, -0.25F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, 0.5F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, 0, 0.96875F, 1, 1, facing.getOpposite(), -1, light);
			// bottom
			RenderingInstruction.addDrawTexture(renderingInstructions, 0.5F, SMALL_OFFSET, -0.125F, -0.125F, SMALL_OFFSET, 0.5F, -0.125F, SMALL_OFFSET, 0.125F, 0.5F, SMALL_OFFSET, -0.5F, 0.125F, 0.125F, 0.1875F, 0.1875F, facing, -1, light);
			// top
			RenderingInstruction.addDrawTexture(renderingInstructions, 0.5F, 1 - SMALL_OFFSET, -0.5F, -0.125F, 1 - SMALL_OFFSET, 0.125F, -0.125F, 1 - SMALL_OFFSET, 0.5F, 0.5F, 1 - SMALL_OFFSET, -0.125F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
			// top front
			RenderingInstruction.addDrawTexture(renderingInstructions, 0.5F - END_FRONT_OFFSET, 1 - SMALL_OFFSET, -0.5F - END_FRONT_OFFSET, -0.125F - ROOT_TWO_SCALED, 1 - SMALL_OFFSET, 0.125F, -0.125F, 1 - SMALL_OFFSET, 0.125F, 0.5F, 1 - SMALL_OFFSET, -0.5F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
			// left side diagonal
			RenderingInstruction.addDrawTexture(renderingInstructions, 0.5F, 0.0625F, -0.5F, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0.5F - END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0.5F, 1, -0.5F, 0.9375F, 0, 1, 0.9375F, facing, -1, light);
			// left side diagonal square
			RenderingInstruction.addDrawTexture(renderingInstructions, 0.5F, 0, -0.5F, 0.5F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0.5F, 0.0625F, -0.5F, 0.9375F, 0.9375F, 1, 1, facing, -1, light);
		}
		if (airRight) {
			// back
			RenderingInstruction.addDrawTexture(renderingInstructions, -0.5F, 0, -0.125F, 0.125F, 0, 0.5F, 0.125F, 1, 0.5F, -0.5F, 1, -0.125F, 0, 0, 1, 1, facing, -1, light);
			// front
			RenderingInstruction.addDrawTexture(renderingInstructions, 0.25F + END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, -0.5F + END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0.25F + END_FRONT_OFFSET, 1, 0.25F - END_FRONT_OFFSET, 0, 0, 1, 0.9375F, facing.getOpposite(), -1, light);
			// top curve
			RenderingInstruction.addDrawTexture(renderingInstructions, 0.25F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, -0.5F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0.25F + END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, 0, 0.9375F, 1, 0.96875F, facing.getOpposite(), -1, light);
			// bottom curve
			RenderingInstruction.addDrawTexture(renderingInstructions, 0.25F, 0, 0.25F, -0.5F, 0, -0.5F, -0.5F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, 0.25F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, 0, 0.96875F, 1, 1, facing.getOpposite(), -1, light);
			// bottom
			RenderingInstruction.addDrawTexture(renderingInstructions, 0.125F, SMALL_OFFSET, 0.5F, -0.5F, SMALL_OFFSET, -0.125F, -0.5F, SMALL_OFFSET, -0.5F, 0.125F, SMALL_OFFSET, 0.125F, 0.125F, 0.125F, 0.1875F, 0.1875F, facing, -1, light);
			// top
			RenderingInstruction.addDrawTexture(renderingInstructions, 0.125F, 1 - SMALL_OFFSET, 0.125F, -0.5F, 1 - SMALL_OFFSET, -0.5F, -0.5F, 1 - SMALL_OFFSET, -0.125F, 0.125F, 1 - SMALL_OFFSET, 0.5F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
			// top front
			RenderingInstruction.addDrawTexture(renderingInstructions, 0.125F + ROOT_TWO_SCALED, 1 - SMALL_OFFSET, 0.125F, -0.5F + END_FRONT_OFFSET, 1 - SMALL_OFFSET, -0.5F - END_FRONT_OFFSET, -0.5F, 1 - SMALL_OFFSET, -0.5F, 0.125F, 1 - SMALL_OFFSET, 0.125F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
			// left side diagonal
			RenderingInstruction.addDrawTexture(renderingInstructions, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, -0.5F, 0.0625F, -0.5F, -0.5F, 1, -0.5F, -0.5F + END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0, 0, 0.0625F, 0.9375F, facing, -1, light);
			// left side diagonal square
			RenderingInstruction.addDrawTexture(renderingInstructions, -0.5F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, -0.5F, 0, -0.5F, -0.5F, 0.0625F, -0.5F, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0, 0.9375F, 0.0625F, 1, facing, -1, light);
		}
	}

	@Override
	protected void renderAdditional(List<RenderingInstruction> renderingInstructions, RouteRenderer routeRenderer, BlockState state, Direction facing, int light) {
		final boolean airLeft = IBlock.getStatePropertySafe(state, BlockPSDTop.AIR_LEFT);
		final boolean airRight = IBlock.getStatePropertySafe(state, BlockPSDTop.AIR_RIGHT);
		routeRenderer.renderColorStrip(airLeft ? 0.625F : 0, COLOR_STRIP_START, 0, airRight ? 0.375F : 1, COLOR_STRIP_END, 0, facing, light);
		if (airLeft) {
			routeRenderer.renderColorStrip(END_FRONT_OFFSET, COLOR_STRIP_START, -0.625F - END_FRONT_OFFSET, 0.75F + END_FRONT_OFFSET, COLOR_STRIP_END, 0.125F - END_FRONT_OFFSET, facing, light);
		}
		if (airRight) {
			routeRenderer.renderColorStrip(0.25F - END_FRONT_OFFSET, COLOR_STRIP_START, 0.125F - END_FRONT_OFFSET, 1 - END_FRONT_OFFSET, COLOR_STRIP_END, -0.625F - END_FRONT_OFFSET, facing, light);
		}
	}
}
