package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.block.BlockRouteSignBase;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.Map;

public class RenderRouteSign<T extends BlockRouteSignBase.TileEntityRouteSignBase> extends BlockEntityRendererMapper<T> implements IBlock, IGui {

	private static final float SIDE = 2.5F / 16;
	private static final float BOTTOM = 10.5F / 16;
	private static final float MIDDLE = 13F / 16;
	private static final float TOP = 15.5F / 16;
	private static final float WIDTH = 1 - SIDE * 2;
	private static final float HEIGHT_BOTTOM = MIDDLE - BOTTOM + 1;
	private static final float HEIGHT_TOP = TOP - MIDDLE;
	private static final float TEXTURE_BREAK = MIDDLE / HEIGHT_BOTTOM;

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

		final boolean isTop = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		final int arrowDirection = IBlock.getStatePropertySafe(state, BlockRouteSignBase.ARROW_DIRECTION);

		final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
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

		matrices.pushPose();
		matrices.translate(0.5, 0, 0.5);
		UtilitiesClient.rotateYDegrees(matrices, -facing.toYRot());
		matrices.translate(-0.5, 0, 0.4375 - SMALL_OFFSET * 2);

		final VertexConsumer vertexConsumer1 = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(ClientData.DATA_CACHE.getDirectionArrow(platform.id, (arrowDirection & 0b01) > 0, (arrowDirection & 0b10) > 0, HorizontalAlignment.CENTER, true, 0.2F, WIDTH / HEIGHT_TOP, ARGB_BLACK, ARGB_WHITE, 0).resourceLocation));
		IDrawing.drawTexture(matrices, vertexConsumer1, 1 - SIDE, TOP + (isTop ? 0 : 1), 0, SIDE, MIDDLE + (isTop ? 0 : 1), 0, 0, 0, 1, 1, facing.getOpposite(), -1, light);

		final VertexConsumer vertexConsumer2 = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(ClientData.DATA_CACHE.getRouteMap(platform.id, true, false, HEIGHT_BOTTOM / WIDTH, false).resourceLocation));
		IDrawing.drawTexture(matrices, vertexConsumer2, 1 - SIDE, MIDDLE + (isTop ? 0 : 1), 0, 1 - SIDE, isTop ? 0 : BOTTOM, 0, SIDE, isTop ? 0 : BOTTOM, 0, SIDE, MIDDLE + (isTop ? 0 : 1), 0, 0, 0, isTop ? TEXTURE_BREAK : 1, 1, facing.getOpposite(), -1, light);

		matrices.popPose();
	}

	@Override
	public boolean shouldRenderOffScreen(T blockEntity) {
		return true;
	}
}
