package mtr.render;

import mtr.block.BlockRailwaySign;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.config.CustomResources;
import mtr.data.NameColorDataBase;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import net.minecraft.block.BlockState;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.*;
import java.util.stream.Collectors;

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
		if (entity.getSignIds().length != block.length) {
			return;
		}
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
		final String[] signIds = entity.getSignIds();

		boolean renderBackground = false;
		int backgroundColor = 0;
		for (final String signId : signIds) {
			if (signId != null) {
				final CustomResources.CustomSign sign = getSign(signId);
				if (sign != null) {
					renderBackground = true;
					if (sign.backgroundColor != 0) {
						backgroundColor = sign.backgroundColor;
						break;
					}
				}
			}
		}

		matrices.push();
		matrices.translate(0.5, 0.53125, 0.5);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.translate(block.getXStart() / 16F - 0.5, 0, -0.0625 - SMALL_OFFSET * 3);

		if (renderBackground) {
			final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new Identifier("mtr:textures/block/white.png")));
			IGui.drawTexture(matrices, vertexConsumer, 0, 0, SMALL_OFFSET * 2, 0.5F * (signIds.length), 0.5F, SMALL_OFFSET * 2, facing, backgroundColor + ARGB_BLACK, MAX_LIGHT_GLOWING);
		}
		for (int i = 0; i < signIds.length; i++) {
			if (signIds[i] != null) {
				drawSign(matrices, vertexConsumers, dispatcher.getTextRenderer(), pos, signIds[i], 0.5F * i, 0, 0.5F, i, signIds.length - i - 1, entity.getSelectedIds(), facing, (textureId, x, y, size, flipTexture) -> {
					final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new Identifier(textureId.toString())));
					IGui.drawTexture(matrices, vertexConsumer, x, y, size, size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1, facing, -1, MAX_LIGHT_GLOWING);
				});
			}
		}

		matrices.pop();
	}

	@Override
	public boolean rendersOutsideBoundingBox(T blockEntity) {
		return true;
	}

	public static void drawSign(MatrixStack matrices, VertexConsumerProvider vertexConsumers, TextRenderer textRenderer, BlockPos pos, String signId, float x, float y, float size, float maxWidthLeft, float maxWidthRight, Set<Long> selectedIds, Direction facing, DrawTexture drawTexture) {
		if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance)) {
			return;
		}

		final CustomResources.CustomSign sign = getSign(signId);
		if (sign == null) {
			return;
		}

		final float signSize = (sign.small ? BlockRailwaySign.SMALL_SIGN_PERCENTAGE : 1) * size;
		final float margin = (size - signSize) / 2;

		final boolean hasCustomText = sign.hasCustomText();
		final boolean flipCustomText = sign.flipCustomText;
		final boolean flipTexture = sign.flipTexture;

		final VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

		if (vertexConsumers != null && (signId.equals(BlockRailwaySign.SignType.LINE.toString()) || signId.equals(BlockRailwaySign.SignType.LINE_FLIPPED.toString()))) {
			final Station station = ClientData.getStation(pos);
			if (station == null) {
				return;
			}

			final Map<Integer, ClientData.ColorNamePair> routesInStation = ClientData.routesInStation.get(station.id);
			if (routesInStation != null) {
				final List<ClientData.ColorNamePair> selectedIdsSorted = selectedIds.stream().filter(selectedId -> RailwayData.isBetween(selectedId, Integer.MIN_VALUE, Integer.MAX_VALUE)).map(Math::toIntExact).filter(routesInStation::containsKey).map(routesInStation::get).sorted(Comparator.comparingInt(route -> route.color)).collect(Collectors.toList());
				final int selectedCount = selectedIdsSorted.size();

				final float maxWidth = Math.max(0, ((flipCustomText ? maxWidthLeft : maxWidthRight) + 1) * size - margin * 1.5F);
				final List<Float> textWidths = new ArrayList<>();
				for (final ClientData.ColorNamePair route : selectedIdsSorted) {
					IGui.drawStringWithFont(matrices, textRenderer, immediate, route.name, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, 10000, -1, size - margin * 3, 1, 0, false, MAX_LIGHT_GLOWING, (x1, y1, x2, y2) -> textWidths.add(x2));
				}

				matrices.push();
				matrices.translate(flipCustomText ? x + size - margin : x + margin, 0, 0);

				final float totalTextWidth = textWidths.stream().reduce(Float::sum).orElse(0F) + 1.5F * margin * selectedCount;
				if (totalTextWidth > maxWidth) {
					matrices.scale((maxWidth - margin / 2) / (totalTextWidth - margin / 2), 1, 1);
				}

				float xOffset = margin * 0.5F;
				for (int i = 0; i < selectedIdsSorted.size(); i++) {
					final ClientData.ColorNamePair route = selectedIdsSorted.get(i);
					IGui.drawStringWithFont(matrices, textRenderer, immediate, route.name, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, VerticalAlignment.TOP, flipCustomText ? -xOffset : xOffset, y + margin * 1.5F, -1, size - margin * 3, 0.01F, ARGB_WHITE, false, MAX_LIGHT_GLOWING, (x1, y1, x2, y2) -> {
						final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new Identifier("mtr:textures/block/white.png")));
						IGui.drawTexture(matrices, vertexConsumer, x1 - margin / 2, y1 - margin / 2, SMALL_OFFSET, x2 + margin / 2, y2 + margin / 2, SMALL_OFFSET, facing, route.color + ARGB_BLACK, MAX_LIGHT_GLOWING);
					});
					xOffset += textWidths.get(i) + margin * 1.5F;
				}

				matrices.pop();
			}
		} else if (vertexConsumers != null && (signId.equals(BlockRailwaySign.SignType.PLATFORM.toString()) || signId.equals(BlockRailwaySign.SignType.PLATFORM_FLIPPED.toString()))) {
			final Station station = ClientData.getStation(pos);
			if (station == null) {
				return;
			}

			final Map<Long, Platform> platformPositions = ClientData.platformsInStation.get(station.id);
			if (platformPositions != null) {
				final List<Platform> selectedIdsSorted = selectedIds.stream().filter(platformPositions::containsKey).map(platformPositions::get).sorted(NameColorDataBase::compareTo).collect(Collectors.toList());
				final int selectedCount = selectedIdsSorted.size();

				final float smallPadding = margin / selectedCount;
				final float height = (size - margin * 2 + smallPadding) / selectedCount;
				for (int i = 0; i < selectedIdsSorted.size(); i++) {
					final float topOffset = i * height + margin;
					final float bottomOffset = (i + 1) * height + margin - smallPadding;
					final RouteRenderer routeRenderer = new RouteRenderer(matrices, vertexConsumers, immediate, selectedIdsSorted.get(i), true, true);
					routeRenderer.renderArrow((flipCustomText ? x - maxWidthLeft * size : x) + margin, (flipCustomText ? x + size : x + (maxWidthRight + 1) * size) - margin, topOffset, bottomOffset, flipCustomText, !flipCustomText, facing, MAX_LIGHT_GLOWING, false);
				}
			}
		} else {
			drawTexture.drawTexture(sign.textureId, x + margin, y + margin, signSize, flipTexture);

			if (hasCustomText) {
				final float fixedMargin = size * (1 - BlockRailwaySign.SMALL_SIGN_PERCENTAGE) / 2;
				final boolean isSmall = sign.small;
				final float maxWidth = Math.max(0, (flipCustomText ? maxWidthLeft : maxWidthRight) * size - fixedMargin * (isSmall ? 1 : 2));
				final float start = flipCustomText ? x - (isSmall ? 0 : fixedMargin) : x + size + (isSmall ? 0 : fixedMargin);
				IGui.drawStringWithFont(matrices, textRenderer, immediate, sign.customText, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, VerticalAlignment.TOP, start, y + fixedMargin, maxWidth, size - fixedMargin * 2, 0.01F, ARGB_WHITE, false, MAX_LIGHT_GLOWING, null);
			}
		}

		immediate.draw();
	}

	public static CustomResources.CustomSign getSign(String signId) {
		try {
			final BlockRailwaySign.SignType sign = BlockRailwaySign.SignType.valueOf(signId);
			return new CustomResources.CustomSign(sign.textureId, sign.flipTexture, sign.customText, sign.flipCustomText, sign.small, sign.backgroundColor);
		} catch (Exception ignored) {
			return signId == null ? null : CustomResources.customSigns.get(signId);
		}
	}

	@FunctionalInterface
	public interface DrawTexture {
		void drawTexture(Identifier textureId, float x, float y, float size, boolean flipTexture);
	}
}
