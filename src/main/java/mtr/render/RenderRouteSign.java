package mtr.render;

import mtr.block.BlockRouteSign;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.block.IPropagateBlock;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.List;

public class RenderRouteSign extends BlockEntityRenderer<BlockRouteSign.TileEntityRouteSign> implements IBlock, IGui {

	private static final int SCALE = 480;
	private static final int SCALE_LARGE = 320;

	public RenderRouteSign(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockRouteSign.TileEntityRouteSign entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final WorldAccess world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();
		final BlockState state = world.getBlockState(pos);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return;
		}
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
		final int arrowDirection = IBlock.getStatePropertySafe(state, IPropagateBlock.PROPAGATE_PROPERTY);

		final Long stationId = ClientData.stations.stream().filter(station1 -> station1.inStation(pos.getX(), pos.getZ())).map(station -> station.id).findFirst().orElse(0L);
		final List<BlockPos> platformPositions = ClientData.platformPositionsInStation.get(stationId);
		if (platformPositions == null) {
			return;
		}

		final RouteRenderer routeRenderer = new RouteRenderer(matrices, vertexConsumers, platformPositions.get(entity.getPlatformIndex() % platformPositions.size()), true);

		matrices.push();
		matrices.translate(0.5, 0, 0.5);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.translate(0, 0, 0.4375 - SMALL_OFFSET * 4);

		routeRenderer.renderArrow(-0.3125F, 0.3125F, -1.9375F, -1.84375F, (arrowDirection & 0b10) > 0, (arrowDirection & 0b01) > 0, light);
		routeRenderer.renderLine(-1.71875F, -0.75F, -0.25F, 0.25F, SCALE, light);

		// TODO use actual line name
		IGui.drawStringWithFont(matrices, MinecraftClient.getInstance().textRenderer, "Line Name", HorizontalAlignment.LEFT, VerticalAlignment.TOP, -0.3125F, -1.78125F, 0.125F, -1, SCALE_LARGE, ARGB_BLACK, false, null);
		matrices.pop();
	}
}
