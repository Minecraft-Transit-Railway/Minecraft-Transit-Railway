package mtr.gui;

import com.mojang.blaze3d.platform.NativeImage;
import mtr.MTR;
import mtr.data.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RouteMapGenerator implements IGui {

	private static final int SCALE = 128;
	private static final int LINE_SIZE = SCALE / 8;
	private static final int LINE_SPACING = LINE_SIZE * 3 / 2;
	private static final int FONT_SIZE_BIG = LINE_SIZE * 2;
	private static final int FONT_SIZE_SMALL = FONT_SIZE_BIG / 2;

	private static final String TEMP_CIRCULAR_MARKER = "temp_circular_marker";

	public static DynamicTexture generateColorStrip(long platformId) {
		try {
			final List<Integer> colors = getRouteStream(platformId).map(route -> route.color).distinct().collect(Collectors.toList());
			final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, 1, colors.size(), false);
			for (int i = 0; i < colors.size(); i++) {
				drawPixelSafe(nativeImage, 0, i, ARGB_BLACK + colors.get(i));
			}
			return new DynamicTexture(nativeImage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static DynamicTexture generateStationName(long platformId, float aspectRatio) {
		try {
			final int[] dimensions = new int[2];
			final byte[] pixels = ClientData.DATA_CACHE.getTextPixels(getStationName(platformId), dimensions, FONT_SIZE_BIG, FONT_SIZE_SMALL);
			final int padding = dimensions[1] / 2;
			final int height = dimensions[1] + padding;
			final int width = Math.max(Math.round(height * aspectRatio), dimensions[0] + padding);

			final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
			nativeImage.fillRect(0, 0, width, height, ARGB_WHITE);
			drawString(nativeImage, pixels, width / 2, padding / 2, dimensions, 0, ARGB_BLACK, true);
			return new DynamicTexture(nativeImage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static DynamicTexture generateDirectionArrow(long platformId, boolean renderWhite, boolean hasLeft, boolean hasRight, boolean showToString, float aspectRatio) {
		try {
			final List<Integer> colors = new ArrayList<>();
			final List<String> destinations = new ArrayList<>();
			getRouteStream(platformId).forEach(route -> {
				if (!colors.contains(route.color)) {
					colors.add(route.color);
				}

				if (route.circularState == Route.CircularState.NONE) {
					destinations.add(getStationName(route.platformIds.get(route.platformIds.size() - 1)));
				} else {
					final int currentStationIndex = route.platformIds.indexOf(platformId);
					boolean isVia = false;
					String text = "";
					for (int i = currentStationIndex + 1; i < route.platformIds.size() - 1; i++) {
						if (getInterchangeRoutes(route.platformIds.get(i)).size() > 1) {
							text = getStationName(route.platformIds.get(i));
							isVia = true;
							break;
						}
					}
					if (!isVia) {
						text = getStationName(route.platformIds.get(route.platformIds.size() - 1));
					}
					final String translationString = String.format("%s_%s", route.circularState == Route.CircularState.CLOCKWISE ? "clockwise" : "anticlockwise", isVia ? "via" : "to");
					destinations.add(TEMP_CIRCULAR_MARKER + IGui.insertTranslation("gui.mtr." + translationString + "_cjk", "gui.mtr." + translationString, 1, text));
				}
			});
			String destinationString = IGui.mergeStations(destinations);
			final boolean noToString = destinationString.startsWith(TEMP_CIRCULAR_MARKER);
			destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER, "");
			if (!destinationString.isEmpty() && showToString && !noToString) {
				destinationString = IGui.insertTranslation("gui.mtr.to_cjk", "gui.mtr.to", 1, destinationString);
			}

			final boolean leftToRight = hasLeft || !hasRight;
			final int height = SCALE * 7 / 8;
			final int width = Math.round(height * aspectRatio);
			final int padding = height / 4;
			final int tileSize = height - padding * 2;
			final int tilePadding = tileSize / 4;

			final int leftSize = ((hasLeft ? 1 : 0) + (leftToRight ? 1 : 0)) * (tileSize + tilePadding);
			final int rightSize = ((hasRight ? 1 : 0) + (leftToRight ? 0 : 1)) * (tileSize + tilePadding);
			final ClientCache clientCache = ClientData.DATA_CACHE;
			final int[] dimensionsDestination = new int[2];
			final byte[] pixelsDestination = clientCache.getTextPixels(destinationString, dimensionsDestination, width - leftSize - rightSize - padding * 2, FONT_SIZE_BIG, FONT_SIZE_SMALL, 0, leftToRight ? HorizontalAlignment.LEFT : HorizontalAlignment.RIGHT);

			final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
			if (renderWhite) {
				nativeImage.fillRect(0, 0, width, height, ARGB_WHITE);
			}

			final int sidePadding = (width - leftSize - rightSize - dimensionsDestination[0]) / 2;
			if (hasLeft) {
				drawResource(nativeImage, "textures/sign/arrow.png", sidePadding, padding, tileSize, tileSize, false, 0, 1, ARGB_BLACK, renderWhite);
			}
			if (hasRight) {
				drawResource(nativeImage, "textures/sign/arrow.png", width - sidePadding - tileSize, padding, tileSize, tileSize, true, 0, 1, ARGB_BLACK, renderWhite);
			}

			final int circleX = leftToRight ? sidePadding + leftSize - tileSize - tilePadding : width - sidePadding - rightSize + tilePadding;
			for (int i = 0; i < colors.size(); i++) {
				drawResource(nativeImage, "textures/sign/circle.png", circleX, padding, tileSize, tileSize, false, (float) i / colors.size(), (i + 1F) / colors.size(), colors.get(i), renderWhite);
			}

			final Platform platform = clientCache.platformIdMap.get(platformId);
			if (platform != null) {
				final int[] dimensionsPlatformNumber = new int[2];
				final byte[] pixelsPlatformNumber = clientCache.getTextPixels(platform.name, dimensionsPlatformNumber, tileSize, FONT_SIZE_BIG, FONT_SIZE_BIG, 0, HorizontalAlignment.CENTER);
				drawString(nativeImage, pixelsPlatformNumber, circleX + tileSize / 2, padding + (tileSize - dimensionsPlatformNumber[1]) / 2, dimensionsPlatformNumber, 0, ARGB_WHITE, renderWhite);
			}

			drawString(nativeImage, pixelsDestination, sidePadding + leftSize + dimensionsDestination[0] / 2, (height - dimensionsDestination[1]) / 2, dimensionsDestination, 0, ARGB_BLACK, renderWhite);
			return new DynamicTexture(nativeImage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static DynamicTexture generateHorizontalRouteMap(long platformId, boolean renderWhite, boolean flip, float aspectRatio) {
		try {
			final List<Tuple<Route, Integer>> routeDetails = getRouteStream(platformId).map(route -> {
				final int currentIndex = route.platformIds.indexOf(platformId);
				return currentIndex + 1 < route.platformIds.size() ? new Tuple<>(route, currentIndex) : null;
			}).filter(Objects::nonNull).collect(Collectors.toList());
			final int routeCount = routeDetails.size();

			if (routeCount > 0) {
				final ClientCache clientCache = ClientData.DATA_CACHE;
				final List<List<Long>> stationsIdsBefore = new ArrayList<>();
				final List<List<Long>> stationsIdsAfter = new ArrayList<>();
				final List<Map<Integer, StationPosition>> stationPositions = new ArrayList<>();
				final int[] colorIndices = new int[routeCount];
				int colorIndex = -1;
				int previousColor = -1;
				for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
					stationsIdsBefore.add(new ArrayList<>());
					stationsIdsAfter.add(new ArrayList<>());
					stationPositions.add(new HashMap<>());

					final Tuple<Route, Integer> routeDetail = routeDetails.get(routeIndex);
					final List<Long> platformIds = routeDetail.getA().platformIds;
					final int currentIndex = routeDetail.getB();
					for (int stationIndex = 0; stationIndex < platformIds.size(); stationIndex++) {
						if (stationIndex != currentIndex) {
							final long stationId = getStationId(platformIds.get(stationIndex));
							if (stationIndex < currentIndex) {
								stationsIdsBefore.get(stationsIdsBefore.size() - 1).add(0, stationId);
							} else {
								stationsIdsAfter.get(stationsIdsAfter.size() - 1).add(stationId);
							}
						}
					}

					final int color = routeDetail.getA().color;
					if (color != previousColor) {
						colorIndex++;
						previousColor = color;
					}
					colorIndices[routeIndex] = colorIndex;
				}

				for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
					stationPositions.get(routeIndex).put(0, new StationPosition(0, getLineOffset(routeIndex, colorIndices), true));
				}

				final float[] bounds = new float[3];
				setup(stationPositions, flip ? stationsIdsBefore : stationsIdsAfter, colorIndices, bounds, flip, true);
				final float xOffset = bounds[0] + 1;
				setup(stationPositions, flip ? stationsIdsAfter : stationsIdsBefore, colorIndices, bounds, !flip, false);
				final float yOffset = Math.abs(bounds[1]) + 1;

				final int height = Math.round((yOffset + bounds[2] + 1) * SCALE);
				final int width = Math.round(height * aspectRatio);
				final float widthScale = width / (xOffset + bounds[0] + 1) / SCALE;

				final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
				if (renderWhite) {
					nativeImage.fillRect(0, 0, width, height, ARGB_WHITE);
				}

				final Map<Long, Map<StationPosition, Integer>> stationPositionsGrouped = new HashMap<>();
				for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
					final Route route = routeDetails.get(routeIndex).getA();
					final int currentIndex = routeDetails.get(routeIndex).getB();
					final Map<Integer, StationPosition> routeStationPositions = stationPositions.get(routeIndex);

					for (int stationIndex = 0; stationIndex < route.platformIds.size(); stationIndex++) {
						final StationPosition stationPosition = routeStationPositions.get(stationIndex - currentIndex);
						if (stationIndex < route.platformIds.size() - 1) {
							drawLine(nativeImage, stationPosition, routeStationPositions.get(stationIndex + 1 - currentIndex), widthScale, xOffset, yOffset, stationIndex < currentIndex ? ARGB_LIGHT_GRAY : ARGB_BLACK + route.color);
						}

						final long stationId = getStationId(route.platformIds.get(stationIndex));
						if (!stationPositionsGrouped.containsKey(stationId)) {
							stationPositionsGrouped.put(stationId, new HashMap<>());
						}
						if (!stationPosition.isCommon || stationPositionsGrouped.get(stationId).keySet().stream().noneMatch(stationPosition2 -> stationPosition2.isCommon)) {
							stationPositionsGrouped.get(stationId).put(stationPosition, stationIndex - currentIndex);
						}
					}
				}

				stationPositionsGrouped.forEach((stationId, stationPositionGrouped) -> stationPositionGrouped.forEach((stationPosition, stationOffset) -> {
					final int x = Math.round((stationPosition.x + xOffset) * SCALE * widthScale);
					final int y = Math.round((stationPosition.y + yOffset) * SCALE);
					final int lines = stationPosition.isCommon ? colorIndices[colorIndices.length - 1] : 0;
					drawStation(nativeImage, x, y, lines, stationOffset < 0, renderWhite);
					final Station station = clientCache.stationIdMap.get(stationId);
					final int[] dimensions = new int[2];
					final byte[] pixels = clientCache.getTextPixels(station == null ? "" : station.name, dimensions, (int) (SCALE * widthScale), FONT_SIZE_BIG, FONT_SIZE_SMALL, FONT_SIZE_SMALL / 4, HorizontalAlignment.CENTER);
					drawString(nativeImage, pixels, x, y + lines * LINE_SPACING + LINE_SIZE * 5 / 4, dimensions, stationOffset == 0 ? ARGB_BLACK : 0, stationOffset < 0 ? ARGB_LIGHT_GRAY : stationOffset == 0 ? ARGB_WHITE : ARGB_BLACK, renderWhite);
				}));

				return new DynamicTexture(nativeImage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static void setup(List<Map<Integer, StationPosition>> stationPositions, List<List<Long>> stationsIdLists, int[] colorIndices, float[] bounds, boolean passed, boolean reverse) {
		final int passedMultiplier = passed ? -1 : 1;
		final int reverseMultiplier = reverse ? -1 : 1;
		bounds[0] = 0;

		final List<Long> commonStationIds = new ArrayList<>();
		stationsIdLists.get(0).forEach(stationId -> {
			if (stationId > 0 && !commonStationIds.contains(stationId) && stationsIdLists.stream().allMatch(stationsIds -> stationsIds.contains(stationId))) {
				commonStationIds.add(stationId);
			}
		});

		int positionXOffset = 0;
		final int routeCount = stationsIdLists.size();
		final int[] traverseIndex = new int[routeCount];
		for (int commonStationIndex = 0; commonStationIndex <= commonStationIds.size(); commonStationIndex++) {
			final boolean lastStation = commonStationIndex == commonStationIds.size();
			final long commonStationId = lastStation ? -1 : commonStationIds.get(commonStationIndex);

			int intermediateSegmentsMaxCount = 0;
			final int[] intermediateSegmentsCounts = new int[routeCount];
			for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
				intermediateSegmentsCounts[routeIndex] = (lastStation ? stationsIdLists.get(routeIndex).size() : stationsIdLists.get(routeIndex).indexOf(commonStationId) + 1) - traverseIndex[routeIndex];
				intermediateSegmentsMaxCount = Math.max(intermediateSegmentsMaxCount, intermediateSegmentsCounts[routeIndex]);
			}

			final List<Integer> routesIndicesInSection = new ArrayList<>();
			for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
				if (!lastStation || intermediateSegmentsCounts[routeIndex] > 0) {
					routesIndicesInSection.add(routeIndex);
				}
			}

			for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
				if (intermediateSegmentsCounts[routeIndex] > 0) {
					final float increment = (float) intermediateSegmentsMaxCount / intermediateSegmentsCounts[routeIndex];
					for (int j = 0; j < intermediateSegmentsCounts[routeIndex] - (lastStation ? 0 : 1); j++) {
						final float stationX = positionXOffset + increment * (j + 1);
						bounds[0] = Math.max(bounds[0], stationX);
						final float stationY = routesIndicesInSection.indexOf(routeIndex) - (routesIndicesInSection.size() - 1) / 2F + getLineOffset(routeIndex, colorIndices);
						bounds[1] = Math.min(bounds[1], stationY);
						bounds[2] = Math.max(bounds[2], stationY);
						stationPositions.get(routeIndex).put(passedMultiplier * (j + traverseIndex[routeIndex] + 1), new StationPosition(reverseMultiplier * stationX, stationY, false));
					}
					traverseIndex[routeIndex] += intermediateSegmentsCounts[routeIndex];
				}
			}

			if (!lastStation) {
				positionXOffset += intermediateSegmentsMaxCount;
				for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
					final float stationY = getLineOffset(routeIndex, colorIndices);
					bounds[1] = Math.min(bounds[1], stationY);
					bounds[2] = Math.max(bounds[2], stationY);
					stationPositions.get(routeIndex).put(passedMultiplier * traverseIndex[routeIndex], new StationPosition(reverseMultiplier * positionXOffset, stationY, true));
				}
				bounds[0] = positionXOffset;
			}
		}
	}

	private static float getLineOffset(int routeIndex, int[] colorIndices) {
		return (float) LINE_SPACING / SCALE * (colorIndices[routeIndex] - colorIndices[colorIndices.length - 1] / 2F);
	}

	private static Stream<Route> getRouteStream(long platformId) {
		return ClientData.ROUTES.stream().filter(route -> route.platformIds.contains(platformId)).sorted((a, b) -> a.color == b.color ? a.compareTo(b) : a.color - b.color);
	}

	private static long getStationId(long platformId) {
		final Station station = ClientData.DATA_CACHE.platformIdToStation.get(platformId);
		return station == null ? -1 : station.id;
	}

	private static String getStationName(long platformId) {
		final Station station = ClientData.DATA_CACHE.platformIdToStation.get(platformId);
		return station == null ? "" : station.name;
	}

	private static Map<Integer, ClientCache.ColorNameTuple> getInterchangeRoutes(long platformId) {
		return ClientData.DATA_CACHE.stationIdToRoutes.get(getStationId(platformId));
	}

	private static void drawLine(NativeImage nativeImage, StationPosition stationPosition1, StationPosition stationPosition2, float widthScale, float xOffset, float yOffset, int color) {
		final int x1 = Math.round((stationPosition1.x + xOffset) * SCALE * widthScale);
		final int x2 = Math.round((stationPosition2.x + xOffset) * SCALE * widthScale);
		final int y1 = Math.round((stationPosition1.y + yOffset) * SCALE);
		final int y2 = Math.round((stationPosition2.y + yOffset) * SCALE);
		final int xChange = x2 - x1;
		final int yChange = y2 - y1;
		final int xChangeAbs = Math.abs(xChange);
		final int yChangeAbs = Math.abs(yChange);
		final int changeDifference = Math.abs(yChangeAbs - xChangeAbs);

		if (xChangeAbs > yChangeAbs) {
			final boolean y1OffsetGreater = Math.abs(y1 - yOffset * SCALE) > Math.abs(y2 - yOffset * SCALE);
			drawLine(nativeImage, x1, y1, x2 - x1, y1OffsetGreater ? 0 : y2 - y1, y1OffsetGreater ? changeDifference : yChangeAbs, color);
			drawLine(nativeImage, x2, y2, x1 - x2, y1OffsetGreater ? y1 - y2 : 0, y1OffsetGreater ? yChangeAbs : changeDifference, color);
		} else {
			final int halfXChangeAbs = xChangeAbs / 2;
			drawLine(nativeImage, x1, y1, x2 - x1, y2 - y1, halfXChangeAbs, color);
			drawLine(nativeImage, x2, y2, x1 - x2, y1 - y2, halfXChangeAbs, color);
			drawLine(nativeImage, (x1 + x2) / 2, y1 + (int) Math.copySign(halfXChangeAbs, y2 - y1), 0, y2 - y1, changeDifference, color);
		}
	}

	private static void drawLine(NativeImage nativeImage, int x, int y, int directionX, int directionY, int length, int color) {
		final int halfLineHeight = LINE_SIZE / 2;
		final int xWidth = directionX == 0 ? halfLineHeight : 0;
		final int yWidth = directionX == 0 ? 0 : directionY == 0 ? halfLineHeight : Math.round(LINE_SIZE * Mth.SQRT_OF_TWO / 2);
		final int yMin = y - halfLineHeight - (directionY < 0 ? length : 0) + 1;
		final int yMax = y + halfLineHeight + (directionY > 0 ? length : 0) - 1;
		final int drawOffset = directionX != 0 && directionY != 0 ? halfLineHeight : 0;

		for (int i = -drawOffset; i < Math.abs(length) + drawOffset; i++) {
			final int drawX = x + (directionX == 0 ? 0 : (int) Math.copySign(i, directionX)) + (directionX < 0 ? -1 : 0);
			final int drawY = y + (directionY == 0 ? 0 : (int) Math.copySign(i, directionY)) + (directionY < 0 ? -1 : 0);

			for (int xOffset = 0; xOffset < xWidth; xOffset++) {
				drawPixelSafe(nativeImage, drawX - xOffset - 1, drawY, color);
				drawPixelSafe(nativeImage, drawX + xOffset, drawY, color);
			}

			for (int yOffset = 0; yOffset < yWidth; yOffset++) {
				drawPixelSafe(nativeImage, drawX, Math.max(drawY - yOffset, yMin) - 1, color);
				drawPixelSafe(nativeImage, drawX, Math.min(drawY + yOffset, yMax), color);
			}
		}
	}

	private static void drawStation(NativeImage nativeImage, int x, int y, int lines, boolean passed, boolean renderWhite) {
		for (int offsetX = -LINE_SIZE; offsetX < LINE_SIZE; offsetX++) {
			for (int offsetY = -LINE_SIZE; offsetY < LINE_SIZE; offsetY++) {
				final int extraOffsetY = offsetY > 0 ? lines * LINE_SPACING : 0;
				final int repeatDraw = offsetY == 0 ? lines * LINE_SPACING : 0;
				final double squareSum = (offsetX + 0.5) * (offsetX + 0.5) + (offsetY + 0.5) * (offsetY + 0.5);

				if (squareSum <= 0.5 * LINE_SIZE * LINE_SIZE) {
					for (int i = 0; i <= repeatDraw; i++) {
						drawPixelSafe(nativeImage, x + offsetX, y + offsetY + extraOffsetY + i, renderWhite ? ARGB_WHITE : 0);
					}
				} else if (squareSum <= LINE_SIZE * LINE_SIZE) {
					for (int i = 0; i <= repeatDraw; i++) {
						drawPixelSafe(nativeImage, x + offsetX, y + offsetY + extraOffsetY + i, passed ? ARGB_LIGHT_GRAY : ARGB_BLACK);
					}
				}
			}
		}
	}

	private static void drawString(NativeImage nativeImage, byte[] pixels, int x, int y, int[] textDimensions, int backgroundColor, int textColor, boolean renderWhite) {
		if (((backgroundColor >> 24) & 0xFF) > 0) {
			for (int drawX = 0; drawX < textDimensions[0]; drawX++) {
				for (int drawY = 0; drawY < textDimensions[1]; drawY++) {
					drawPixelSafe(nativeImage, drawX + x - textDimensions[0] / 2, drawY + y, backgroundColor);
				}
			}
		}
		int drawX = 0;
		int drawY = 0;
		for (int i = 0; i < textDimensions[0] * textDimensions[1]; i++) {
			blendPixel(nativeImage, x + drawX - textDimensions[0] / 2, y + drawY, ((pixels[i] & 0xFF) << 24) + (textColor & RGB_WHITE), renderWhite);
			drawX++;
			if (drawX == textDimensions[0]) {
				drawX = 0;
				drawY++;
			}
		}
	}

	private static void drawResource(NativeImage nativeImage, String resource, int x, int y, int width, int height, boolean flipX, float v1, float v2, int color, boolean renderWhite) throws IOException {
		final NativeImage nativeImageResource = NativeImage.read(NativeImage.Format.RGBA, Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(MTR.MOD_ID, resource)).getInputStream());
		final int resourceWidth = nativeImageResource.getWidth();
		final int resourceHeight = nativeImageResource.getHeight();
		for (int drawX = 0; drawX < width; drawX++) {
			for (int drawY = Math.round(v1 * height); drawY < Math.round(v2 * height); drawY++) {
				final float pixelX = (float) drawX / width * resourceWidth;
				final float pixelY = (float) drawY / height * resourceHeight;
				final int floorX = (int) pixelX;
				final int floorY = (int) pixelY;
				final int ceilX = floorX + 1;
				final int ceilY = floorY + 1;
				final float percentX1 = ceilX - pixelX;
				final float percentY1 = ceilY - pixelY;
				final float percentX2 = pixelX - floorX;
				final float percentY2 = pixelY - floorY;
				final float luminance1 = ((nativeImageResource.getPixelRGBA(Mth.clamp(floorX, 0, resourceWidth - 1), Mth.clamp(floorY, 0, resourceHeight - 1)) >> 24) & 0xFF) * percentX1 * percentY1;
				final float luminance2 = ((nativeImageResource.getPixelRGBA(Mth.clamp(ceilX, 0, resourceWidth - 1), Mth.clamp(floorY, 0, resourceHeight - 1)) >> 24) & 0xFF) * percentX2 * percentY1;
				final float luminance3 = ((nativeImageResource.getPixelRGBA(Mth.clamp(floorX, 0, resourceWidth - 1), Mth.clamp(ceilY, 0, resourceHeight - 1)) >> 24) & 0xFF) * percentX1 * percentY2;
				final float luminance4 = ((nativeImageResource.getPixelRGBA(Mth.clamp(ceilX, 0, resourceWidth - 1), Mth.clamp(ceilY, 0, resourceHeight - 1)) >> 24) & 0xFF) * percentX2 * percentY2;
				blendPixel(nativeImage, (flipX ? width - drawX - 1 : drawX) + x, drawY + y, (color & RGB_WHITE) + ((int) (luminance1 + luminance2 + luminance3 + luminance4) << 24), renderWhite);
			}
		}
	}

	private static void blendPixel(NativeImage nativeImage, int x, int y, int color, boolean renderWhite) {
		if (RailwayData.isBetween(x, 0, nativeImage.getWidth() - 1) && RailwayData.isBetween(y, 0, nativeImage.getHeight() - 1)) {
			final float percent = (float) ((color >> 24) & 0xFF) / 0xFF;
			if (percent > 0) {
				final int existingPixel = nativeImage.getPixelRGBA(x, y);
				final boolean existingTransparent = ((existingPixel >> 24) & 0xFF) == 0;
				final int r1 = existingTransparent ? 0xFF : (existingPixel & 0xFF);
				final int g1 = existingTransparent ? 0xFF : ((existingPixel >> 8) & 0xFF);
				final int b1 = existingTransparent ? 0xFF : ((existingPixel >> 16) & 0xFF);
				final int r2 = (color >> 16) & 0xFF;
				final int g2 = (color >> 8) & 0xFF;
				final int b2 = color & 0xFF;
				final float inversePercent = 1 - percent;
				final int finalColor = ARGB_BLACK + ((int) (r1 * inversePercent + r2 * percent) << 16) + ((int) (g1 * inversePercent + g2 * percent) << 8) + (int) (b1 * inversePercent + b2 * percent);
				drawPixelSafe(nativeImage, x, y, !renderWhite && finalColor == ARGB_WHITE ? 0 : finalColor);
			}
		}
	}

	private static void drawPixelSafe(NativeImage nativeImage, int x, int y, int color) {
		if (RailwayData.isBetween(x, 0, nativeImage.getWidth() - 1) && RailwayData.isBetween(y, 0, nativeImage.getHeight() - 1)) {
			nativeImage.setPixelRGBA(x, y, ((color & ARGB_BLACK) != 0 ? ARGB_BLACK : 0) + ((color & 0xFF) << 16) + (color & 0xFF00) + ((color & 0xFF0000) >> 16));
		}
	}

	private static class StationPosition {

		private final float x;
		private final float y;
		private final boolean isCommon;

		private StationPosition(float x, float y, boolean isCommon) {
			this.x = x;
			this.y = y;
			this.isCommon = isCommon;
		}
	}
}
