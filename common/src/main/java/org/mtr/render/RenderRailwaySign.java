package org.mtr.render;

import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.block.BlockRailwaySign;
import org.mtr.block.IBlock;
import org.mtr.client.CustomResourceLoader;
import org.mtr.client.DynamicTextureCache;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.data.Station;
import org.mtr.data.FormattedStationExit;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.resource.SignResource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RenderRailwaySign<T extends BlockRailwaySign.RailwaySignBlockEntity> extends BlockEntityRendererExtension<T> implements IBlock, IGui, IDrawing {

	@Override
	public void render(T entity, ClientWorld world, ClientPlayerEntity player, float tickDelta, int light, int overlay) {
		final BlockPos pos = entity.getPos();
		final BlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockRailwaySign block)) {
			return;
		}
		if (entity.getSignIds().length != block.length) {
			return;
		}
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
		final String[] signIds = entity.getSignIds();

		boolean renderBackground = false;
		int backgroundColor = 0;
		for (final String signId : signIds) {
			if (signId != null) {
				final SignResource sign = getSign(signId);
				if (sign != null) {
					renderBackground = true;
					if (sign.getBackgroundColor() != 0) {
						backgroundColor = sign.getBackgroundColor();
						break;
					}
				}
			}
		}

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getPos().getX(), 0.53125 + entity.getPos().getY(), 0.5 + entity.getPos().getZ());
		storedMatrixTransformations.add(matrixStack -> {
			IDrawing.rotateYDegrees(matrixStack, -facing.getPositiveHorizontalDegrees());
			IDrawing.rotateZDegrees(matrixStack, 180);
			matrixStack.translate(block.getXStart() / 16F - 0.5, 0, -0.0625 - SMALL_OFFSET * 2);
		});

		if (renderBackground) {
			final int newBackgroundColor = backgroundColor | ARGB_BLACK;
			MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				IDrawing.drawTexture(matrixStack, vertexConsumer, 0, 0, SMALL_OFFSET, 0.5F * (signIds.length), 0.5F, SMALL_OFFSET, facing, newBackgroundColor, DEFAULT_LIGHT);
				matrixStack.pop();
			});
		}
		for (int i = 0; i < signIds.length; i++) {
			if (signIds[i] != null) {
				drawSign(
						storedMatrixTransformations,
						pos,
						signIds[i],
						0.5F * i,
						0,
						0.5F,
						getMaxWidth(signIds, i, false),
						getMaxWidth(signIds, i, true),
						entity.getSelectedIds(),
						facing,
						backgroundColor | ARGB_BLACK,
						(textureId, x, y, size, flipTexture) -> MainRenderer.scheduleRender(textureId, true, QueuedRenderLayer.LIGHT_TRANSLUCENT, (matrixStack, vertexConsumer, offset) -> {
							storedMatrixTransformations.transform(matrixStack, offset);
							IDrawing.drawTexture(matrixStack, vertexConsumer, x, y, size, size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1, facing, -1, DEFAULT_LIGHT);
							matrixStack.pop();
						})
				);
			}
		}
	}

	public static void drawSign(@Nullable StoredMatrixTransformations storedMatrixTransformations, BlockPos pos, String signId, float x, float y, float size, float maxWidthLeft, float maxWidthRight, LongAVLTreeSet selectedIds, Direction facing, int backgroundColor, DrawTexture drawTexture) {
		final SignResource sign = getSign(signId);
		if (sign == null) {
			return;
		}

		final float signSize = (sign.getSmall() ? BlockRailwaySign.SMALL_SIGN_PERCENTAGE : 1) * size;
		final float margin = (size - signSize) / 2;

		final boolean hasCustomText = sign.hasCustomText;
		final boolean flipCustomText = sign.getFlipCustomText();
		final boolean flipTexture = sign.getFlipTexture();
		final boolean isExit = signId.equals("exit_letter") || signId.equals("exit_letter_flipped");
		final boolean isLine = signId.equals("line") || signId.equals("line_flipped");
		final boolean isPlatform = signId.equals("platform") || signId.equals("platform_flipped");
		final boolean isStation = signId.equals("station") || signId.equals("station_flipped");

		if (storedMatrixTransformations != null && isExit) {
			final Station station = MTRClient.findStation(pos);
			if (station == null) {
				return;
			}

			final ObjectArrayList<FormattedStationExit> selectedExitsSorted = new ObjectArrayList<>();
			FormattedStationExit.getFormattedStationExits(station.getExits(), true).forEach(exit -> {
				if (selectedIds.longStream().anyMatch(selectedId -> deserializeExit(selectedId).equals(exit.name))) {
					selectedExitsSorted.add(exit);
				}
			});

			final float maxWidth = ((flipCustomText ? maxWidthLeft : maxWidthRight) + 1) * size - margin * 2;
			final float exitWidth = signSize * selectedExitsSorted.size();

			for (int i = 0; i < selectedExitsSorted.size(); i++) {
				final FormattedStationExit stationExit = selectedExitsSorted.get(flipCustomText ? selectedExitsSorted.size() - i - 1 : i);
				final float signOffset = (flipCustomText ? -1 : 1) * signSize * i - (flipCustomText ? signSize : 0);

				MainRenderer.scheduleRender(DynamicTextureCache.instance.getExitSignLetter(stationExit.parent, stationExit.number, backgroundColor).identifier, true, QueuedRenderLayer.LIGHT_TRANSLUCENT, (matrixStack, vertexConsumer, offset) -> {
					storedMatrixTransformations.transform(matrixStack, offset);
					matrixStack.translate(x + margin + (flipCustomText ? signSize : 0), y + margin, 0);
					matrixStack.scale(Math.min(1, maxWidth / exitWidth), 1, 1);
					IDrawing.drawTexture(matrixStack, vertexConsumer, signOffset, 0, signSize, signSize, facing, DEFAULT_LIGHT);
					matrixStack.pop();
				});

				if (maxWidth > exitWidth && selectedExitsSorted.size() == 1 && !stationExit.destinations.isEmpty()) {
					renderCustomText(stationExit.destinations.getFirst(), storedMatrixTransformations, facing, size, flipCustomText ? x : x + size, flipCustomText, maxWidth - exitWidth - margin * 2, backgroundColor);
				}
			}
		} else if (storedMatrixTransformations != null && isLine) {
			final Station station = MTRClient.findStation(pos);
			if (station == null) {
				return;
			}

			final LongAVLTreeSet platformIds = new LongAVLTreeSet();
			station.savedRails.forEach(platform -> platformIds.add(platform.getId()));
			station.connectedStations.forEach(connectingStation -> connectingStation.savedRails.forEach(platform -> platformIds.add(platform.getId())));

			final ObjectArrayList<IntObjectImmutablePair<String>> selectedRoutesSorted = new ObjectArrayList<>();
			final IntAVLTreeSet addedColors = new IntAVLTreeSet();
			MinecraftClientData.getInstance().simplifiedRoutes.forEach(simplifiedRoute -> {
				if (!simplifiedRoute.getName().isEmpty()) {
					final int color = simplifiedRoute.getColor();
					if (!addedColors.contains(color) && selectedIds.contains(color) && simplifiedRoute.getPlatforms().stream().anyMatch(simplifiedRoutePlatform -> platformIds.contains(simplifiedRoutePlatform.getPlatformId()))) {
						selectedRoutesSorted.add(new IntObjectImmutablePair<>(color, simplifiedRoute.getName().split("\\|\\|")[0]));
						addedColors.add(color);
					}
				}
			});

			selectedRoutesSorted.sort(Comparator.comparingInt(IntObjectImmutablePair::leftInt));
			final float maxWidth = Math.max(0, ((flipCustomText ? maxWidthLeft : maxWidthRight) + 1) * size - margin * 2);
			final float height = size - margin * 2;
			final List<DynamicTextureCache.DynamicResource> resourceLocationDataList = new ArrayList<>();
			float totalTextWidth = 0;
			for (final IntObjectImmutablePair<String> route : selectedRoutesSorted) {
				final DynamicTextureCache.DynamicResource resourceLocationData = DynamicTextureCache.instance.getRouteSquare(route.leftInt(), route.right(), flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT);
				resourceLocationDataList.add(resourceLocationData);
				totalTextWidth += height * resourceLocationData.width / resourceLocationData.height + margin / 2F;
			}

			final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations.copy();
			storedMatrixTransformations2.add(graphicsHolderNew -> graphicsHolderNew.translate(flipCustomText ? x + size - margin : x + margin, 0, 0));

			if (totalTextWidth > margin / 2F) {
				totalTextWidth -= margin / 2F;
			}
			if (totalTextWidth > maxWidth) {
				final float finalTotalTextWidth = totalTextWidth;
				storedMatrixTransformations2.add(graphicsHolderNew -> graphicsHolderNew.scale(maxWidth / finalTotalTextWidth, 1, 1));
			}

			float xOffset = 0;
			for (final DynamicTextureCache.DynamicResource resourceLocationData : resourceLocationDataList) {
				final float width = height * resourceLocationData.width / resourceLocationData.height;
				final float finalXOffset = xOffset;
				MainRenderer.scheduleRender(resourceLocationData.identifier, true, QueuedRenderLayer.LIGHT, (matrixStack, vertexConsumer, offset) -> {
					storedMatrixTransformations2.transform(matrixStack, offset);
					IDrawing.drawTexture(matrixStack, vertexConsumer, flipCustomText ? -finalXOffset - width : finalXOffset, margin, width, height, Direction.UP, DEFAULT_LIGHT);
					matrixStack.pop();
				});
				xOffset += width + margin / 2F;
			}
		} else if (storedMatrixTransformations != null && isPlatform) {
			final Station station = MTRClient.findStation(pos);
			if (station == null) {
				return;
			}

			final LongArrayList selectedIdsSorted = station.savedRails.stream().sorted().mapToLong(NameColorDataBase::getId).filter(selectedIds::contains).boxed().collect(Collectors.toCollection(LongArrayList::new));
			final int selectedCount = selectedIdsSorted.size();

			final float extraMargin = margin - margin / selectedCount;
			final float height = (size - extraMargin * 2) / selectedCount;
			for (int i = 0; i < selectedIdsSorted.size(); i++) {
				final float topOffset = i * height + extraMargin;
				final float bottomOffset = (i + 1) * height + extraMargin;
				final float left = flipCustomText ? x - maxWidthLeft * size : x + margin;
				final float right = flipCustomText ? x + size - margin : x + (maxWidthRight + 1) * size;
				MainRenderer.scheduleRender(DynamicTextureCache.instance.getDirectionArrow(selectedIdsSorted.getLong(i), false, false, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, false, margin / size, (right - left) / (bottomOffset - topOffset), backgroundColor, ARGB_WHITE, backgroundColor).identifier, true, QueuedRenderLayer.LIGHT_TRANSLUCENT, (matrixStack, vertexConsumer, offset) -> {
					storedMatrixTransformations.transform(matrixStack, offset);
					IDrawing.drawTexture(matrixStack, vertexConsumer, left, topOffset, 0, right, bottomOffset, 0, 0, 0, 1, 1, facing, -1, DEFAULT_LIGHT);
					matrixStack.pop();
				});
			}
		} else {
			drawTexture.drawTexture(sign.getTexture(), x + margin, y + margin, signSize, flipTexture);

			if (hasCustomText) {
				final float fixedMargin = size * (1 - BlockRailwaySign.SMALL_SIGN_PERCENTAGE) / 2;
				final boolean isSmall = sign.getSmall();
				final float maxWidth = Math.max(0, (flipCustomText ? maxWidthLeft : maxWidthRight) * size - fixedMargin * (isSmall ? 1 : 2));
				final float start = flipCustomText ? x - (isSmall ? 0 : fixedMargin) : x + size + (isSmall ? 0 : fixedMargin);
				if (storedMatrixTransformations == null) {
//					IDrawing.drawStringWithFont(matrixStack, vertexConsumer, isExit || isLine ? "..." : sign.getCustomText().getString(), flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, VerticalAlignment.TOP, start, y + fixedMargin, maxWidth, size - fixedMargin * 2, 0.01F, ARGB_WHITE, false, DEFAULT_LIGHT, null);
				} else {
					final String signText;
					if (isStation) {
						signText = IGui.mergeStations(selectedIds.longStream()
								.filter(MinecraftClientData.getInstance().stationIdMap::containsKey)
								.sorted()
								.mapToObj(stationId -> IGui.insertTranslation(TranslationProvider.GUI_MTR_STATION_CJK, TranslationProvider.GUI_MTR_STATION, 1, MinecraftClientData.getInstance().stationIdMap.get(stationId).getName()))
								.collect(Collectors.toList())
						);
					} else {
						signText = sign.getCustomText().getString();
					}
					renderCustomText(signText, storedMatrixTransformations, facing, size, start, flipCustomText, maxWidth, backgroundColor);
				}
			}
		}
	}

	private static void renderCustomText(String signText, StoredMatrixTransformations storedMatrixTransformations, Direction facing, float size, float start, boolean flipCustomText, float maxWidth, int backgroundColor) {
		final DynamicTextureCache.DynamicResource dynamicResource = DynamicTextureCache.instance.getSignText(signText, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, (1 - BlockRailwaySign.SMALL_SIGN_PERCENTAGE) / 2, backgroundColor, ARGB_WHITE);
		final float width = Math.min(size * dynamicResource.width / dynamicResource.height, maxWidth);
		MainRenderer.scheduleRender(dynamicResource.identifier, true, QueuedRenderLayer.LIGHT_TRANSLUCENT, (matrixStack, vertexConsumer, offset) -> {
			storedMatrixTransformations.transform(matrixStack, offset);
			IDrawing.drawTexture(matrixStack, vertexConsumer, start - (flipCustomText ? width : 0), 0, 0, start + (flipCustomText ? 0 : width), size, 0, 0, 0, 1, 1, facing, -1, DEFAULT_LIGHT);
			matrixStack.pop();
		});
	}

	public static SignResource getSign(@Nullable String signId) {
		// TODO load existing signs from BlockRailwaySign.SignType using the resource pack format
		if (signId == null) {
			return null;
		} else {
			final SignResource[] signResource = {null};
			CustomResourceLoader.getSignById(signId, newSignResource -> signResource[0] = newSignResource);
			return signResource[0];
		}
	}

	public static float getMaxWidth(String[] signIds, int index, boolean right) {
		float maxWidthLeft = 0;
		for (int i = index + (right ? 1 : -1); right ? i < signIds.length : i >= 0; i += (right ? 1 : -1)) {
			if (signIds[i] != null) {
				final SignResource sign = RenderRailwaySign.getSign(signIds[i]);
				if (sign != null && sign.hasCustomText && right == sign.getFlipCustomText()) {
					maxWidthLeft /= 2;
				}
				return maxWidthLeft;
			}
			maxWidthLeft++;
		}

		return maxWidthLeft;
	}

	private static String deserializeExit(long code) {
		final StringBuilder exit = new StringBuilder();
		long charCodes = code;
		while (charCodes > 0) {
			exit.insert(0, (char) (charCodes & 0xFF));
			charCodes = charCodes >> 8;
		}
		return exit.toString();
	}

	@FunctionalInterface
	public interface DrawTexture {
		void drawTexture(Identifier textureId, float x, float y, float size, boolean flipTexture);
	}
}
