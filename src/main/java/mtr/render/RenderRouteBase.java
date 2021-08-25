package mtr.render;

import mtr.block.IBlock;
import mtr.block.IPropagateBlock;
import mtr.data.IGui;
import mtr.data.Platform;
import mtr.gui.ClientData;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public abstract class RenderRouteBase<T extends BlockEntity> implements IGui, BlockEntityRenderer<T> {

	private static final float EXTRA_PADDING = 0.0625F;

	@Override
	public final void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final World world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();
		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, HorizontalFacingBlock.FACING);

		matrices.push();
		matrices.translate(0.5, 0, 0.5);
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));

		renderAdditionalUnmodified(matrices, vertexConsumers, state, facing, light);

		if (!RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, facing)) {
			final int arrowDirection = IBlock.getStatePropertySafe(state, IPropagateBlock.PROPAGATE_PROPERTY);
			final Platform platform = ClientData.getClosePlatform(pos);
			final VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
			final RouteRenderer routeRenderer = new RouteRenderer(matrices, vertexConsumers, immediate, platform, false, false);

			matrices.translate(0, 1, 0);
			matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
			matrices.translate(-0.5, 0, getZ() - SMALL_OFFSET * 2);

			if (isLeft(state)) {
				final int glassLength = getGlassLength(world, pos, facing);
				if (glassLength > 1) {
					switch (getRenderType(world, pos, state)) {
						case ARROW:
							routeRenderer.renderArrow(getSidePadding() + EXTRA_PADDING, glassLength - getSidePadding() - EXTRA_PADDING, getTopPadding() + EXTRA_PADDING, 1 - getBottomPadding() - EXTRA_PADDING, (arrowDirection & 0b10) > 0, (arrowDirection & 0b01) > 0, facing, light);
							break;
						case ROUTE:
							final boolean flipLine = arrowDirection == 1;
							routeRenderer.renderLine(flipLine ? glassLength - getSidePadding() - EXTRA_PADDING * 2 : getSidePadding() + EXTRA_PADDING * 2, flipLine ? getSidePadding() + EXTRA_PADDING * 2 : glassLength - getSidePadding() - EXTRA_PADDING * 2, getTopPadding() + EXTRA_PADDING, 1 - getBottomPadding() - EXTRA_PADDING, getBaseScale(), facing, light, RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance / 4, null));
							break;
					}
				}
			}

			renderAdditional(matrices, vertexConsumers, routeRenderer, state, facing, light);
			immediate.draw();
		}

		matrices.pop();
	}

	protected void renderAdditionalUnmodified(MatrixStack matrices, VertexConsumerProvider vertexConsumers, BlockState state, Direction facing, int light) {
	}

	protected abstract float getZ();

	protected abstract float getSidePadding();

	protected abstract float getBottomPadding();

	protected abstract float getTopPadding();

	protected abstract int getBaseScale();

	protected abstract boolean isLeft(BlockState state);

	protected abstract boolean isRight(BlockState state);

	protected abstract RenderType getRenderType(WorldAccess world, BlockPos pos, BlockState state);

	protected abstract void renderAdditional(MatrixStack matrices, VertexConsumerProvider vertexConsumers, RouteRenderer routeRenderer, BlockState state, Direction facing, int light);

	private int getGlassLength(WorldAccess world, BlockPos pos, Direction facing) {
		int glassLength = 1;

		while (true) {
			final BlockState state = world.getBlockState(pos.offset(facing.rotateYClockwise(), glassLength));
			if (state.getBlock() == world.getBlockState(pos).getBlock() && !isLeft(state)) {
				glassLength++;
				if (isRight(state)) {
					break;
				}
			} else {
				break;
			}
		}

		return glassLength;
	}

	protected enum RenderType {ARROW, ROUTE, NONE}
}
