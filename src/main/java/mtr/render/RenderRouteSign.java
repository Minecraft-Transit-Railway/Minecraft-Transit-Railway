package mtr.render;

import mtr.block.BlockRouteSignBase;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.block.IPropagateBlock;
import mtr.data.Platform;
import mtr.data.Station;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

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
		if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance)) {
			return;
		}

		final BlockState state = world.getBlockState(pos);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return;
		}
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
		final int arrowDirection = IBlock.getStatePropertySafe(state, IPropagateBlock.PROPAGATE_PROPERTY);

		final Station station = ClientData.getStation(pos);
		if (station == null) {
			return;
		}

		final Map<Long, Platform> platformPositions = ClientData.platformsInStation.get(station.id);
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
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.translate(0, 0, 0.4375 - SMALL_OFFSET * 4);

		routeRenderer.renderArrow(-0.3125F, 0.3125F, -1.9375F, -1.84375F, (arrowDirection & 0b10) > 0, (arrowDirection & 0b01) > 0, facing, light);
		if (!RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance / 2)) {
			routeRenderer.renderLine(-1.71875F, -0.75F, -0.3125F, 0.3125F, SCALE, facing, light);
		}
		matrices.pop();
		immediate.draw();
	}

	@Override
	public boolean rendersOutsideBoundingBox(T blockEntity) {
		return true;
	}
}
