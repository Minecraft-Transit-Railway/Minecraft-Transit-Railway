package mtr.render;

import mtr.block.BlockRailwaySign;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.config.CustomResources;
import mtr.data.*;
import mtr.gui.ClientData;
import mtr.gui.IDrawing;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.WorldAccess;

import java.util.*;
import java.util.stream.Collectors;

public class RenderRailwaySign<T extends BlockRailwaySign.TileEntityRailwaySign> implements IBlock, IGui, IDrawing, BlockEntityRenderer<T> {

	public static final int HEIGHT_TO_SCALE = 27;

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
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.translate(block.getXStart() / 16F - 0.5, 0, -0.0625 - SMALL_OFFSET * 3);

		if (renderBackground) {
			final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new Identifier("mtr:textures/block/white.png")));
			IDrawing.drawTexture(matrices, vertexConsumer, 0, 0, SMALL_OFFSET * 2, 0.5F * (signIds.length), 0.5F, SMALL_OFFSET * 2, facing, backgroundColor + ARGB_BLACK, MAX_LIGHT_GLOWING);
		}
		for (int i = 0; i < signIds.length; i++) {
			if (signIds[i] != null) {
				drawSign(matrices, vertexConsumers, MinecraftClient.getInstance().textRenderer, pos, signIds[i], 0.5F * i, 0, 0.5F, i, signIds.length - i - 1, entity.getSelectedIds(), facing, (textureId, x, y, size, flipTexture) -> {
					final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new Identifier(textureId.toString())));
					IDrawing.drawTexture(matrices, vertexConsumer, x, y, size, size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1, facing, -1, MAX_LIGHT_GLOWING);
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
		final boolean isExit = signId.equals(BlockRailwaySign.SignType.EXIT_LETTER.toString()) || signId.equals(BlockRailwaySign.SignType.EXIT_LETTER_FLIPPED.toString());
		final boolean isLine = signId.equals(BlockRailwaySign.SignType.LINE.toString()) || signId.equals(BlockRailwaySign.SignType.LINE_FLIPPED.toString());
		final boolean isPlatform = signId.equals(BlockRailwaySign.SignType.PLATFORM.toString()) || signId.equals(BlockRailwaySign.SignType.PLATFORM_FLIPPED.toString());

		final VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

		if (vertexConsumers != null && isExit) {
			final Station station = ClientData.getStation(pos);
			if (station == null) {
				return;
			}

			final Map<String, List<String>> exits = station.getGeneratedExits();
			final List<String> selectedExitsSorted = selectedIds.stream().map(Station::deserializeExit).filter(exits::containsKey).sorted(String::compareTo).collect(Collectors.toList());

			matrices.push();
			matrices.translate(x + margin + (flipCustomText ? signSize : 0), y + margin, 0);
			final float maxWidth = ((flipCustomText ? maxWidthLeft : maxWidthRight) + 1) * size - margin * 2;
			final float exitWidth = signSize * selectedExitsSorted.size();
			matrices.scale(Math.min(1, maxWidth / exitWidth), 1, 1);

			final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new Identifier("mtr:textures/sign/exit_letter_blank.png")));

			for (int i = 0; i < selectedExitsSorted.size(); i++) {
				final String selectedExit = selectedExitsSorted.get(flipCustomText ? selectedExitsSorted.size() - i - 1 : i);
				final float offset = (flipCustomText ? -1 : 1) * signSize * i - (flipCustomText ? signSize : 0);

				IDrawing.drawTexture(matrices, vertexConsumer, offset, 0, SMALL_OFFSET, offset + signSize, signSize, SMALL_OFFSET, facing, -1, MAX_LIGHT_GLOWING);

				final String selectedExitLetter = selectedExit.substring(0, 1);
				final String selectedExitNumber = selectedExit.substring(1);
				final boolean hasNumber = !selectedExitNumber.isEmpty();
				final float space = hasNumber ? margin * 1.5F : 0;
				IDrawing.drawStringWithFont(matrices, textRenderer, immediate, selectedExitLetter, hasNumber ? HorizontalAlignment.LEFT : HorizontalAlignment.CENTER, VerticalAlignment.CENTER, offset + (hasNumber ? margin : signSize / 2), signSize / 2 + margin, signSize - margin * 2 - space, signSize - margin, 1, ARGB_WHITE, false, MAX_LIGHT_GLOWING, null);
				if (hasNumber) {
					IDrawing.drawStringWithFont(matrices, textRenderer, immediate, selectedExitNumber, HorizontalAlignment.RIGHT, VerticalAlignment.TOP, offset + signSize - margin, signSize / 2, space, signSize / 2 - margin / 4, 1, ARGB_WHITE, false, MAX_LIGHT_GLOWING, null);
				}

				if (maxWidth > exitWidth && selectedExitsSorted.size() == 1 && !exits.get(selectedExit).isEmpty()) {
					IDrawing.drawStringWithFont(matrices, textRenderer, immediate, exits.get(selectedExit).get(0), flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, VerticalAlignment.CENTER, flipCustomText ? offset - margin : offset + signSize + margin, signSize / 2, maxWidth - exitWidth - margin * 2, signSize, HEIGHT_TO_SCALE / signSize, ARGB_WHITE, false, MAX_LIGHT_GLOWING, null);
				}
			}

			matrices.pop();
		} else if (vertexConsumers != null && isLine) {
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
					IDrawing.drawStringWithFont(matrices, textRenderer, immediate, route.name, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, 10000, -1, size - margin * 3, HEIGHT_TO_SCALE / (size - margin * 3), 0, false, MAX_LIGHT_GLOWING, (x1, y1, x2, y2) -> textWidths.add(x2));
				}

				matrices.push();
				matrices.translate(flipCustomText ? x + size - margin : x + margin, 0, 0);

				final float totalTextWidth = textWidths.stream().reduce(Float::sum).orElse(0F) + 1.5F * margin * selectedCount;
				if (totalTextWidth > maxWidth) {
					matrices.scale((maxWidth - margin / 2) / (totalTextWidth - margin / 2), 1, 1);
				}

				final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new Identifier("mtr:textures/block/white.png")));

				float xOffset = margin * 0.5F;
				for (int i = 0; i < selectedIdsSorted.size(); i++) {
					final ClientData.ColorNamePair route = selectedIdsSorted.get(i);
					IDrawing.drawStringWithFont(matrices, textRenderer, immediate, route.name, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, VerticalAlignment.CENTER, flipCustomText ? -xOffset : xOffset, y + size / 2, -1, size - margin * 3, HEIGHT_TO_SCALE / (size - margin * 3), ARGB_WHITE, false, MAX_LIGHT_GLOWING, (x1, y1, x2, y2) -> {
						IDrawing.drawTexture(matrices, vertexConsumer, x1 - margin / 2, y + margin, SMALL_OFFSET, x2 + margin / 2, y + size - margin, SMALL_OFFSET, facing, route.color + ARGB_BLACK, MAX_LIGHT_GLOWING);
					});
					xOffset += textWidths.get(i) + margin * 1.5F;
				}

				matrices.pop();
			}
		} else if (vertexConsumers != null && isPlatform) {
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
				IDrawing.drawStringWithFont(matrices, textRenderer, immediate, isExit || isLine ? "..." : sign.customText, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, VerticalAlignment.TOP, start, y + fixedMargin, maxWidth, size - fixedMargin * 2, 0.01F, ARGB_WHITE, false, MAX_LIGHT_GLOWING, null);
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
