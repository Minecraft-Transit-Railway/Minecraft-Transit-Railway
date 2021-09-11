package mtr.render;

import mtr.block.BlockRouteSignBase;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.block.IPropagateBlock;
import mtr.data.IGui;
import mtr.data.Platform;
import mtr.data.Station;
import mtr.gui.ClientData;
import mtr.gui.RenderingInstruction;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RenderRouteSign<T extends BlockRouteSignBase.TileEntityRouteSignBase> extends BlockEntityRenderer<T> implements IBlock, IGui {

	private static final int SCALE = 480;

	public RenderRouteSign(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final WorldAccess world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();
		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
		if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, facing)) {
			return;
		}

		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return;
		}
		final int arrowDirection = IBlock.getStatePropertySafe(state, IPropagateBlock.PROPAGATE_PROPERTY);

		final Station station = ClientData.getStation(pos);
		if (station == null) {
			return;
		}

		final Map<Long, Platform> platformPositions = ClientData.DATA_CACHE.requestStationIdToPlatforms(station.id);
		if (platformPositions == null || platformPositions.isEmpty()) {
			return;
		}

		final Platform platform = platformPositions.get(entity.getPlatformId());
		if (platform == null) {
			return;
		}

		final VertexConsumerProvider.Immediate immediate = RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance / 4, null) ? null : VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

		ClientData.DATA_CACHE.requestRenderForPos(matrices, vertexConsumers, immediate, pos, () -> {
			final List<RenderingInstruction> renderingInstructions = new ArrayList<>();
			final RouteRenderer routeRenderer = new RouteRenderer(renderingInstructions, platform, true, false);

			RenderingInstruction.addPush(renderingInstructions);
			RenderingInstruction.addTranslate(renderingInstructions, 0.5F, 0, 0.5F);
			RenderingInstruction.addRotateYDegrees(renderingInstructions, -facing.asRotation());
			RenderingInstruction.addRotateZDegrees(renderingInstructions, 180);
			RenderingInstruction.addTranslate(renderingInstructions, 0, 0, 0.4375F - SMALL_OFFSET * 4);

			routeRenderer.renderArrow(-0.3125F, 0.3125F, -1.9375F, -1.84375F, (arrowDirection & 0b10) > 0, (arrowDirection & 0b01) > 0, facing, light);
			routeRenderer.renderLine(-1.71875F, -0.75F, -0.3125F, 0.3125F, SCALE, facing, light);

			RenderingInstruction.addPop(renderingInstructions);
			return renderingInstructions;
		});
	}

	@Override
	public boolean rendersOutsideBoundingBox(T blockEntity) {
		return true;
	}
}
