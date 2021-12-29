package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mtr.block.IBlock;
import mtr.block.IPropagateBlock;
import mtr.data.IGui;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.gui.ClientData;
import mtr.gui.IDrawing;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RenderRouteBase<T extends BlockEntityMapper> extends BlockEntityRendererMapper<T> implements IGui, IBlock {

	protected final float sidePadding;
	protected final float bottomPadding;
	protected final float topPadding;
	private final float z;
	private final boolean renderWhite;

	private static final float EXTRA_PADDING = 0.0625F;

	public RenderRouteBase(BlockEntityRenderDispatcher dispatcher, float z, float sidePadding, float bottomPadding, float topPadding, boolean renderWhite) {
		super(dispatcher);
		this.z = z / 16;
		this.sidePadding = sidePadding / 16;
		this.bottomPadding = bottomPadding / 16;
		this.topPadding = topPadding / 16;
		this.renderWhite = renderWhite;
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
			matrices.translate(-0.5, 0, z);

			if (isLeft(state)) {
				final int glassLength = getGlassLength(world, pos, facing);
				if (glassLength > 1) {
					switch (getRenderType(world, pos, state)) {
						case ARROW:
							routeRenderer.renderArrow(sidePadding + EXTRA_PADDING, glassLength - sidePadding - EXTRA_PADDING, topPadding + EXTRA_PADDING, 1 - bottomPadding - EXTRA_PADDING, (arrowDirection & 0b10) > 0, (arrowDirection & 0b01) > 0, facing, light);
							break;
						case ROUTE:
							final boolean flipLine = arrowDirection == 1;
							if (platform != null) {
								final float width = glassLength - sidePadding * 2;
								final float height = 1 - topPadding - bottomPadding;
								final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(ClientData.DATA_CACHE.getRouteMap(platform.id, renderWhite, flipLine, Math.round(width / height))));
								IDrawing.drawTexture(matrices, vertexConsumer, sidePadding, topPadding, width, height, 0, 0, 1, 1, facing.getOpposite(), ARGB_WHITE, light);
							}
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

	protected boolean isLeft(BlockState state) {
		return IBlock.getStatePropertySafe(state, SIDE_EXTENDED) == IBlock.EnumSide.LEFT;
	}

	protected boolean isRight(BlockState state) {
		return IBlock.getStatePropertySafe(state, SIDE_EXTENDED) == IBlock.EnumSide.RIGHT;
	}

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
