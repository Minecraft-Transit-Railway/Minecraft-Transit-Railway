package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import minecraftmappings.BlockEntityMapper;
import minecraftmappings.BlockEntityRendererMapper;
import mtr.block.IBlock;
import mtr.block.IPropagateBlock;
import mtr.data.IGui;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.gui.ClientData;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RenderRouteBase<T extends BlockEntityMapper> extends BlockEntityRendererMapper<T> implements IGui {

	private static final float EXTRA_PADDING = 0.0625F;

	public RenderRouteBase(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public final void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		final Level world = entity.getLevel();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getBlockPos();
		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, HorizontalDirectionalBlock.FACING);

		matrices.pushPose();
		matrices.translate(0.5, 0, 0.5);
		matrices.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot()));

		renderAdditionalUnmodified(matrices, vertexConsumers, state, facing, light);

		if (!RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, facing)) {
			final int arrowDirection = IBlock.getStatePropertySafe(state, IPropagateBlock.PROPAGATE_PROPERTY);
			final Platform platform = RailwayData.getClosePlatform(ClientData.PLATFORMS, pos);
			final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
			final RouteRenderer routeRenderer = new RouteRenderer(matrices, vertexConsumers, immediate, platform, false, false);

			matrices.translate(0, 1, 0);
			matrices.mulPose(Vector3f.ZP.rotationDegrees(180));
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
			immediate.endBatch();
		}

		matrices.popPose();
	}

	protected void renderAdditionalUnmodified(PoseStack matrices, MultiBufferSource vertexConsumers, BlockState state, Direction facing, int light) {
	}

	protected abstract float getZ();

	protected abstract float getSidePadding();

	protected abstract float getBottomPadding();

	protected abstract float getTopPadding();

	protected abstract int getBaseScale();

	protected abstract boolean isLeft(BlockState state);

	protected abstract boolean isRight(BlockState state);

	protected abstract RenderType getRenderType(BlockGetter world, BlockPos pos, BlockState state);

	protected abstract void renderAdditional(PoseStack matrices, MultiBufferSource vertexConsumers, RouteRenderer routeRenderer, BlockState state, Direction facing, int light);

	private int getGlassLength(BlockGetter world, BlockPos pos, Direction facing) {
		int glassLength = 1;

		while (true) {
			final BlockState state = world.getBlockState(pos.relative(facing.getClockWise(), glassLength));
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
