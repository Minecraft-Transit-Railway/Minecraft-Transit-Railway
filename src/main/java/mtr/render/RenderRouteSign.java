package mtr.render;

import mtr.block.BlockRouteSignBase;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.block.IPropagateBlock;
import mtr.data.IGui;
import mtr.data.Platform;
import mtr.data.Station;
import mtr.gui.ClientData;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.WorldAccess;

import java.util.Map;

public class RenderRouteSign<T extends BlockRouteSignBase.TileEntityRouteSignBase> implements IBlock, IGui, BlockEntityRenderer<T> {

	private static final int SCALE = 480;

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

		final VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
		final RouteRenderer routeRenderer = new RouteRenderer(matrices, vertexConsumers, immediate, platform, true, false);

		matrices.push();
		matrices.translate(0.5, 0, 0.5);
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.translate(0, 0, 0.4375 - SMALL_OFFSET * 4);

		routeRenderer.renderArrow(-0.3125F, 0.3125F, -1.9375F, -1.84375F, (arrowDirection & 0b10) > 0, (arrowDirection & 0b01) > 0, facing, light);
		routeRenderer.renderLine(-1.71875F, -0.75F, -0.3125F, 0.3125F, SCALE, facing, light, RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance / 4, null));
		matrices.pop();
		immediate.draw();
	}

	@Override
	public boolean rendersOutsideBoundingBox(T blockEntity) {
		return true;
	}
}
