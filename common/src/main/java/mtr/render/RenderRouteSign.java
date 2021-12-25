package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import mtr.block.BlockRouteSignBase;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.block.IPropagateBlock;
import mtr.data.IGui;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.gui.ClientData;
import mtr.mappings.BlockEntityRendererMapper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.Map;

public class RenderRouteSign<T extends BlockRouteSignBase.TileEntityRouteSignBase> extends BlockEntityRendererMapper<T> implements IBlock, IGui {

	private static final int SCALE = 480;

	public RenderRouteSign(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		final BlockGetter world = entity.getLevel();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getBlockPos();
		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
		if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, facing)) {
			return;
		}

		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return;
		}
		final int arrowDirection = IBlock.getStatePropertySafe(state, IPropagateBlock.PROPAGATE_PROPERTY);

		final Station station = RailwayData.getStation(ClientData.STATIONS, pos);
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

		final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		final RouteRenderer routeRenderer = new RouteRenderer(matrices, vertexConsumers, immediate, platform, true, false);

		matrices.pushPose();
		matrices.translate(0.5, 0, 0.5);
		matrices.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot()));
		matrices.mulPose(Vector3f.ZP.rotationDegrees(180));
		matrices.translate(0, 0, 0.4375 - SMALL_OFFSET * 4);

		routeRenderer.renderArrow(-0.3125F, 0.3125F, -1.9375F, -1.84375F, (arrowDirection & 0b10) > 0, (arrowDirection & 0b01) > 0, facing, light);
		routeRenderer.renderLine(-1.71875F, -0.75F, -0.3125F, 0.3125F, SCALE, facing, light, RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance / 4, null));
		matrices.popPose();
		immediate.endBatch();
	}

	@Override
	public boolean shouldRenderOffScreen(T blockEntity) {
		return true;
	}
}
