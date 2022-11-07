package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import mtr.block.BlockRailwaySign;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.client.ClientCache;
import mtr.client.ClientData;
import mtr.client.CustomResources;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.mappings.BlockEntityRendererMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.Collectors;

public class RenderRailwaySign<T extends BlockRailwaySign.TileEntityRailwaySign> extends BlockEntityRendererMapper<T> implements IBlock, IGui, IDrawing {

	public RenderRailwaySign(BlockEntityRenderDispatcher dispatcher) {
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

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations();
		storedMatrixTransformations.add(matricesNew -> {
			matricesNew.translate(0.5 + entity.getBlockPos().getX(), 0.53125 + entity.getBlockPos().getY(), 0.5 + entity.getBlockPos().getZ());
			matricesNew.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot()));
			matricesNew.mulPose(Vector3f.ZP.rotationDegrees(180));
			matricesNew.translate(block.getXStart() / 16F - 0.5, 0, -0.0625 - SMALL_OFFSET * 2);
		});

		matrices.pushPose();
		matrices.translate(0.5, 0.53125, 0.5);
		matrices.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot()));
		matrices.mulPose(Vector3f.ZP.rotationDegrees(180));
		matrices.translate(block.getXStart() / 16F - 0.5, 0, -0.0625 - SMALL_OFFSET * 2);

		if (renderBackground) {
			final int newBackgroundColor = backgroundColor | ARGB_BLACK;
			RenderTrains.scheduleRender(new ResourceLocation("mtr:textures/block/white.png"), false, resourceLocation -> MoreRenderLayers.getLight(resourceLocation, false), (matricesNew, vertexConsumer) -> {
				storedMatrixTransformations.transform(matricesNew);
				IDrawing.drawTexture(matricesNew, vertexConsumer, 0, 0, SMALL_OFFSET, 0.5F * (signIds.length), 0.5F, SMALL_OFFSET, facing, newBackgroundColor, MAX_LIGHT_GLOWING);
				matricesNew.popPose();
			});
		}
		for (int i = 0; i < signIds.length; i++) {
			if (signIds[i] != null) {
				drawSign(matrices, vertexConsumers, storedMatrixTransformations, Minecraft.getInstance().font, pos, signIds[i], 0.5F * i, 0, 0.5F, getMaxWidth(signIds, i, false), getMaxWidth(signIds, i, true), entity.getSelectedIds(), facing, backgroundColor | ARGB_BLACK, (textureId, x, y, size, flipTexture) -> {
					RenderTrains.scheduleRender(new ResourceLocation(textureId.toString()), true, resourceLocation -> MoreRenderLayers.getLight(resourceLocation, true), (matricesNew, vertexConsumer) -> {
						storedMatrixTransformations.transform(matricesNew);
						IDrawing.drawTexture(matricesNew, vertexConsumer, x, y, size, size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1, facing, -1, MAX_LIGHT_GLOWING);
						matricesNew.popPose();
					});
				});
			}
		}

		matrices.popPose();
	}

	@Override
	public boolean shouldRenderOffScreen(T blockEntity) {
		return true;
	}

	public static void drawSign(PoseStack matrices, MultiBufferSource vertexConsumers, StoredMatrixTransformations storedMatrixTransformations, Font textRenderer, BlockPos pos, String signId, float x, float y, float size, float maxWidthLeft, float maxWidthRight, Set<Long> selectedIds, Direction facing, int backgroundColor, DrawTexture drawTexture) {
		if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, facing)) {
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
		final boolean isStation = signId.equals(BlockRailwaySign.SignType.STATION.toString()) || signId.equals(BlockRailwaySign.SignType.STATION_FLIPPED.toString());

		final MultiBufferSource.BufferSource immediate = RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance / 2, null) ? null : MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

		if (vertexConsumers != null && isExit) {
			final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
			if (station == null) {
				return;
			}

			final Map<String, List<String>> exits = station.getGeneratedExits();
			final List<String> selectedExitsSorted = selectedIds.stream().map(Station::deserializeExit).filter(exits::containsKey).sorted(String::compareTo).collect(Collectors.toList());

			matrices.pushPose();
			matrices.translate(x + margin + (flipCustomText ? signSize : 0), y + margin, 0);
			final float maxWidth = ((flipCustomText ? maxWidthLeft : maxWidthRight) + 1) * size - margin * 2;
			final float exitWidth = signSize * selectedExitsSorted.size();
			matrices.scale(Math.min(1, maxWidth / exitWidth), 1, 1);

			for (int i = 0; i < selectedExitsSorted.size(); i++) {
				final String selectedExit = selectedExitsSorted.get(flipCustomText ? selectedExitsSorted.size() - i - 1 : i);
				final float offset = (flipCustomText ? -1 : 1) * signSize * i - (flipCustomText ? signSize : 0);

				RenderTrains.scheduleRender(ClientData.DATA_CACHE.getExitSignLetter(selectedExit.substring(0, 1), selectedExit.substring(1), backgroundColor).resourceLocation, true, resourceLocation -> MoreRenderLayers.getLight(resourceLocation, true), (matricesNew, vertexConsumer) -> {
					storedMatrixTransformations.transform(matricesNew);
					matricesNew.translate(x + margin + (flipCustomText ? signSize : 0), y + margin, 0);
					matricesNew.scale(Math.min(1, maxWidth / exitWidth), 1, 1);
					IDrawing.drawTexture(matricesNew, vertexConsumer, offset, 0, signSize, signSize, facing, MAX_LIGHT_GLOWING);
					matricesNew.popPose();
				});

				if (maxWidth > exitWidth && selectedExitsSorted.size() == 1 && !exits.get(selectedExit).isEmpty()) {
					renderCustomText(exits.get(selectedExit).get(0), storedMatrixTransformations, facing, size, flipCustomText ? x : x + size, flipCustomText, maxWidth - exitWidth - margin * 2, backgroundColor);
				}
			}

			matrices.popPose();
		} else if (vertexConsumers != null && isLine) {
			final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
			if (station == null) {
				return;
			}

			final Map<Integer, ClientCache.ColorNameTuple> routesInStation = ClientData.DATA_CACHE.getAllRoutesIncludingConnectingStations(station);
			final List<ClientCache.ColorNameTuple> selectedIdsSorted = selectedIds.stream().filter(selectedId -> RailwayData.isBetween(selectedId, Integer.MIN_VALUE, Integer.MAX_VALUE)).map(Math::toIntExact).filter(routesInStation::containsKey).map(routesInStation::get).sorted(Comparator.comparingInt(route -> route.color)).collect(Collectors.toList());

			final float maxWidth = Math.max(0, ((flipCustomText ? maxWidthLeft : maxWidthRight) + 1) * size - margin * 2);
			final float height = size - margin * 2;
			final List<ClientCache.DynamicResource> resourceLocationDataList = new ArrayList<>();
			float totalTextWidth = 0;
			for (final ClientCache.ColorNameTuple route : selectedIdsSorted) {
				final ClientCache.DynamicResource resourceLocationData = ClientData.DATA_CACHE.getRouteSquare(route.color, route.name, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT);
				resourceLocationDataList.add(resourceLocationData);
				totalTextWidth += height * resourceLocationData.width / resourceLocationData.height + margin / 2F;
			}

			final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations.copy();
			storedMatrixTransformations2.add(matricesNew -> matricesNew.translate(flipCustomText ? x + size - margin : x + margin, 0, 0));

			if (totalTextWidth > margin / 2F) {
				totalTextWidth -= margin / 2F;
			}
			if (totalTextWidth > maxWidth) {
				final float finalTotalTextWidth = totalTextWidth;
				storedMatrixTransformations2.add(matricesNew -> matricesNew.scale(maxWidth / finalTotalTextWidth, 1, 1));
			}

			float xOffset = 0;
			for (final ClientCache.DynamicResource resourceLocationData : resourceLocationDataList) {
				final float width = height * resourceLocationData.width / resourceLocationData.height;
				final float finalXOffset = xOffset;
				RenderTrains.scheduleRender(resourceLocationData.resourceLocation, true, resourceLocation -> MoreRenderLayers.getLight(resourceLocation, false), (matricesNew, vertexConsumer) -> {
					storedMatrixTransformations2.transform(matricesNew);
					IDrawing.drawTexture(matricesNew, vertexConsumer, flipCustomText ? -finalXOffset - width : finalXOffset, margin, width, height, Direction.UP, MAX_LIGHT_GLOWING);
					matricesNew.popPose();
				});
				xOffset += width + margin / 2F;
			}
		} else if (vertexConsumers != null && isPlatform) {
			final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
			if (station == null) {
				return;
			}

			final Map<Long, Platform> platformPositions = ClientData.DATA_CACHE.requestStationIdToPlatforms(station.id);
			if (platformPositions != null) {
				final List<Long> selectedIdsSorted = selectedIds.stream().filter(platformPositions::containsKey).sorted(Comparator.comparing(platformPositions::get)).collect(Collectors.toList());
				final int selectedCount = selectedIdsSorted.size();

				final float extraMargin = margin - margin / selectedCount;
				final float height = (size - extraMargin * 2) / selectedCount;
				for (int i = 0; i < selectedIdsSorted.size(); i++) {
					final float topOffset = i * height + extraMargin;
					final float bottomOffset = (i + 1) * height + extraMargin;
					final float left = flipCustomText ? x - maxWidthLeft * size : x + margin;
					final float right = flipCustomText ? x + size - margin : x + (maxWidthRight + 1) * size;
					RenderTrains.scheduleRender(ClientData.DATA_CACHE.getDirectionArrow(selectedIdsSorted.get(i), false, false, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, false, margin / size, (right - left) / (bottomOffset - topOffset), backgroundColor, ARGB_WHITE, backgroundColor).resourceLocation, true, resourceLocation -> MoreRenderLayers.getLight(resourceLocation, true), (matricesNew, vertexConsumer) -> {
						storedMatrixTransformations.transform(matricesNew);
						IDrawing.drawTexture(matricesNew, vertexConsumer, left, topOffset, 0, right, bottomOffset, 0, 0, 0, 1, 1, facing, -1, MAX_LIGHT_GLOWING);
						matricesNew.popPose();
					});
				}
			}
		} else {
			drawTexture.drawTexture(sign.textureId, x + margin, y + margin, signSize, flipTexture);

			if (hasCustomText) {
				final float fixedMargin = size * (1 - BlockRailwaySign.SMALL_SIGN_PERCENTAGE) / 2;
				final boolean isSmall = sign.small;
				final float maxWidth = Math.max(0, (flipCustomText ? maxWidthLeft : maxWidthRight) * size - fixedMargin * (isSmall ? 1 : 2));
				final float start = flipCustomText ? x - (isSmall ? 0 : fixedMargin) : x + size + (isSmall ? 0 : fixedMargin);
				if (vertexConsumers == null) {
					IDrawing.drawStringWithFont(matrices, textRenderer, immediate, isExit || isLine ? "..." : sign.customText, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, VerticalAlignment.TOP, start, y + fixedMargin, maxWidth, size - fixedMargin * 2, 0.01F, ARGB_WHITE, false, MAX_LIGHT_GLOWING, null);
				} else {
					final String signText;
					if (isStation) {
						signText = IGui.mergeStations(selectedIds.stream().filter(ClientData.DATA_CACHE.stationIdMap::containsKey).sorted(Long::compareTo).map(stationId -> IGui.insertTranslation("gui.mtr.station_cjk", "gui.mtr.station", 1, ClientData.DATA_CACHE.stationIdMap.get(stationId).name)).collect(Collectors.toList()));
					} else {
						signText = sign.customText;
					}
					renderCustomText(signText, storedMatrixTransformations, facing, size, start, flipCustomText, maxWidth, backgroundColor);
				}
			}
		}

		if (immediate != null) {
			immediate.endBatch();
		}
	}

	private static void renderCustomText(String signText, StoredMatrixTransformations storedMatrixTransformations, Direction facing, float size, float start, boolean flipCustomText, float maxWidth, int backgroundColor) {
		final ClientCache.DynamicResource dynamicResource = ClientData.DATA_CACHE.getSignText(signText, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, (1 - BlockRailwaySign.SMALL_SIGN_PERCENTAGE) / 2, backgroundColor, ARGB_WHITE);
		final float width = Math.min(size * dynamicResource.width / dynamicResource.height, maxWidth);
		RenderTrains.scheduleRender(dynamicResource.resourceLocation, true, resourceLocation -> MoreRenderLayers.getLight(resourceLocation, true), (matricesNew, vertexConsumer) -> {
			storedMatrixTransformations.transform(matricesNew);
			IDrawing.drawTexture(matricesNew, vertexConsumer, start - (flipCustomText ? width : 0), 0, 0, start + (flipCustomText ? 0 : width), size, 0, 0, 0, 1, 1, facing, -1, MAX_LIGHT_GLOWING);
			matricesNew.popPose();
		});
	}

	public static CustomResources.CustomSign getSign(String signId) {
		try {
			final BlockRailwaySign.SignType sign = BlockRailwaySign.SignType.valueOf(signId);
			return new CustomResources.CustomSign(sign.textureId, sign.flipTexture, sign.customText, sign.flipCustomText, sign.small, sign.backgroundColor);
		} catch (Exception ignored) {
			return signId == null ? null : CustomResources.CUSTOM_SIGNS.get(signId);
		}
	}

	public static float getMaxWidth(String[] signIds, int index, boolean right) {
		float maxWidthLeft = 0;
		for (int i = index + (right ? 1 : -1); right ? i < signIds.length : i >= 0; i += (right ? 1 : -1)) {
			if (signIds[i] != null) {
				final CustomResources.CustomSign sign = RenderRailwaySign.getSign(signIds[i]);
				if (sign != null && sign.hasCustomText() && right == sign.flipCustomText) {
					maxWidthLeft /= 2;
				}
				return maxWidthLeft;
			}
			maxWidthLeft++;
		}

		return maxWidthLeft;
	}

	@FunctionalInterface
	public interface DrawTexture {
		void drawTexture(ResourceLocation textureId, float x, float y, float size, boolean flipTexture);
	}
}
