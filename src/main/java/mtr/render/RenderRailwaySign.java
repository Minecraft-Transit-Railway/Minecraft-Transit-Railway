package mtr.render;

import mtr.block.BlockRailwaySign;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import net.minecraft.block.BlockState;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.List;

public class RenderRailwaySign<T extends BlockRailwaySign.TileEntityRailwaySign> extends BlockEntityRenderer<T> implements IBlock, IGui {

	public RenderRailwaySign(BlockEntityRenderDispatcher dispatcher) {
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
		if (!(state.getBlock() instanceof BlockRailwaySign)) {
			return;
		}
		final BlockRailwaySign block = (BlockRailwaySign) state.getBlock();
		if (entity.getSign().length != block.length) {
			return;
		}
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
		final BlockRailwaySign.SignType[] signTypes = entity.getSign();

		matrices.push();
		matrices.translate(0.5, 0.53125, 0.5);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.translate(block.getXStart() / 16F - 0.5, 0, -0.0625 - SMALL_OFFSET * 3);

		for (int i = 0; i < signTypes.length; i++) {
			if (signTypes[i] != null) {
				IGui.drawRectangle(matrices, vertexConsumers, 0, 0, 0.5F * (signTypes.length), 0.5F, SMALL_OFFSET * 2, facing, ARGB_BLACK, light);

				final int index = i;
				drawSign(matrices, vertexConsumers, dispatcher.getTextRenderer(), pos, signTypes[i], 0.5F * i, 0, 0.5F, i, signTypes.length - i - 1, entity.getPlatformRouteIndex(), facing, (x, y, size, flipTexture) -> IGui.drawTexture(matrices, vertexConsumers, signTypes[index].id.toString(), x, y, size, size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1, facing, -1, -1));
			}
		}

		matrices.pop();
	}

	@Override
	public boolean rendersOutsideBoundingBox(T blockEntity) {
		return true;
	}

	public static void drawSign(MatrixStack matrices, VertexConsumerProvider vertexConsumers, TextRenderer textRenderer, BlockPos pos, BlockRailwaySign.SignType signType, float x, float y, float size, float maxWidthLeft, float maxWidthRight, int platformIndex, Direction facing, DrawTexture drawTexture) {
		final float signSize = (signType.small ? BlockRailwaySign.SMALL_SIGN_PERCENTAGE : 1) * size;
		final float margin = (size - signSize) / 2;

		final boolean hasCustomText = signType.hasCustomText;
		final boolean flipped = signType.flipped;
		final boolean flipTexture = flipped && !hasCustomText;

		final Long stationId = ClientData.stations.stream().filter(station1 -> station1.inStation(pos.getX(), pos.getZ())).map(station -> station.id).findFirst().orElse(0L);

		if (signType == BlockRailwaySign.SignType.LINE || signType == BlockRailwaySign.SignType.LINE_FLIPPED) {
			final List<ClientData.ColorNamePair> routes = ClientData.routesInStation.get(stationId);
			if (routes != null && routes.size() > 0) {
				final ClientData.ColorNamePair colorNamePair = routes.get(platformIndex % routes.size());

				final float maxWidth = Math.max(0, ((flipped ? maxWidthLeft : maxWidthRight) + 1) * size - margin * 3);
				IGui.drawStringWithFont(matrices, textRenderer, colorNamePair.name, flipped ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, VerticalAlignment.TOP, flipped ? x + size - margin * 1.5F : x + margin * 1.5F, y + margin * 1.5F, maxWidth, size - margin * 3, 0.01F, ARGB_WHITE, false, (x1, y1, x2, y2) -> IGui.drawRectangle(matrices, vertexConsumers, x1 - margin / 2, y1 - margin / 2, x2 + margin / 2, y2 + margin / 2, SMALL_OFFSET, facing, colorNamePair.color + ARGB_BLACK, -1));
			}
		} else if (signType == BlockRailwaySign.SignType.PLATFORM || signType == BlockRailwaySign.SignType.PLATFORM_FLIPPED) {
			if (vertexConsumers == null) {
				drawTexture.drawTexture(x + margin, y + margin, signSize, flipTexture);
			}

			final List<BlockPos> platformPositions = ClientData.platformPositionsInStation.get(stationId);
			if (platformPositions != null && platformPositions.size() > 0) {
				final RouteRenderer routeRenderer = new RouteRenderer(matrices, vertexConsumers, platformPositions.get(platformIndex % platformPositions.size()), true);
				routeRenderer.renderArrow((flipped ? x - maxWidthLeft * size : x - size) + margin, (flipped ? x + size * 2 : x + (maxWidthRight + 1) * size) - margin, y + margin, y + size - margin, flipped, !flipped, facing, -1, false);
			}
		} else {
			drawTexture.drawTexture(x + margin, y + margin, signSize, flipTexture);

			if (hasCustomText) {
				final float fixedMargin = size * (1 - BlockRailwaySign.SMALL_SIGN_PERCENTAGE) / 2;
				final boolean isSmall = signType.small;
				final float maxWidth = Math.max(0, (flipped ? maxWidthLeft : maxWidthRight) * size - fixedMargin * (isSmall ? 1 : 2));
				final float start = flipped ? x - (isSmall ? 0 : fixedMargin) : x + size + (isSmall ? 0 : fixedMargin);
				IGui.drawStringWithFont(matrices, textRenderer, signType.text, flipped ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, VerticalAlignment.TOP, start, y + fixedMargin, maxWidth, size - fixedMargin * 2, 0.01F, ARGB_WHITE, false, null);
			}
		}
	}

	@FunctionalInterface
	public interface DrawTexture {

		void drawTexture(float x, float y, float size, boolean flipTexture);
	}
}
