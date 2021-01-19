package mtr.render;

import mtr.block.BlockPlatformRail;
import mtr.block.IBlock;
import mtr.block.IPropagateBlock;
import mtr.gui.IGui;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public abstract class RenderRouteBase<T extends BlockEntity> extends BlockEntityRenderer<T> implements IGui {

	private static final float EXTRA_PADDING = 0.0625F;

	public RenderRouteBase(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public final void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final World world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();

		final BlockPos platformPos = findPlatformPos(world, pos);
		if (platformPos == null) {
			return;
		}

		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, HorizontalFacingBlock.FACING);
		final int arrowDirection = IBlock.getStatePropertySafe(state, IPropagateBlock.PROPAGATE_PROPERTY);

		final RouteRenderer routeRenderer = new RouteRenderer(matrices, vertexConsumers, platformPos, false);

		matrices.push();
		matrices.translate(0.5, 1, 0.5);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.translate(-0.5, 0, getZ() - SMALL_OFFSET * 2);

		if (isLeft(state)) {
			final int glassLength = getGlassLength(world, pos, facing);
			if (glassLength > 1) {
				switch (getRenderType(world, pos, state)) {
					case ARROW:
						routeRenderer.renderArrow(getSidePadding() + EXTRA_PADDING, glassLength - getSidePadding() - EXTRA_PADDING, getTopPadding() + EXTRA_PADDING, 1 - getBottomPadding() - EXTRA_PADDING, (arrowDirection & 0b10) > 0, (arrowDirection & 0b01) > 0, light);
						break;
					case ROUTE:
						final boolean flipLine = arrowDirection == 1;
						routeRenderer.renderLine(flipLine ? glassLength - getSidePadding() - EXTRA_PADDING * 2 : getSidePadding() + EXTRA_PADDING * 2, flipLine ? getSidePadding() + EXTRA_PADDING * 2 : glassLength - getSidePadding() - EXTRA_PADDING * 2, getTopPadding() + EXTRA_PADDING, 1 - getBottomPadding() - EXTRA_PADDING, getBaseScale(), light);
						break;
				}
			}
		}

		renderAdditional(matrices, vertexConsumers, routeRenderer, state, light);

		matrices.pop();
	}

	protected abstract float getZ();

	protected abstract float getSidePadding();

	protected abstract float getBottomPadding();

	protected abstract float getTopPadding();

	protected abstract int getBaseScale();

	protected abstract boolean isLeft(BlockState state);

	protected abstract boolean isRight(BlockState state);

	protected abstract RenderType getRenderType(WorldAccess world, BlockPos pos, BlockState state);

	protected abstract void renderAdditional(MatrixStack matrices, VertexConsumerProvider vertexConsumers, RouteRenderer routeRenderer, BlockState state, int light);

	private BlockPos findPlatformPos(WorldAccess world, BlockPos pos) {
		final Direction facing = IBlock.getStatePropertySafe(world, pos, HorizontalFacingBlock.FACING);
		for (int y = 1; y <= 3; y++) {
			for (int x = 1; x <= 2; x++) {
				final BlockPos checkPos = pos.down(y).offset(facing, x);
				if (world.getBlockState(checkPos).getBlock() instanceof BlockPlatformRail) {
					return BlockPlatformRail.getPlatformPos1(world, checkPos);
				}
			}
		}
		return null;
	}

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
