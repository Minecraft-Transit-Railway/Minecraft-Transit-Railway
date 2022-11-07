package mtr.client;

import com.mojang.blaze3d.platform.NativeImage;
import mtr.MTR;
import mtr.data.*;
import mtr.mappings.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;

public class RouteMapGenerator implements IGui {

	private static int scale;
	private static int lineSize;
	private static int lineSpacing;
	private static int fontSizeBig;
	private static int fontSizeSmall;

	private static final int MIN_VERTICAL_SIZE = 5;
	private static final String LOGO_RESOURCE = "textures/sign/logo.png";
	private static final String EXIT_RESOURCE = "textures/sign/exit_letter_blank.png";
	private static final String ARROW_RESOURCE = "textures/sign/arrow.png";
	private static final String CIRCLE_RESOURCE = "textures/sign/circle.png";
	private static final String TEMP_CIRCULAR_MARKER = "temp_circular_marker";

	public static void setConstants() {
		scale = (int) Math.pow(2, Config.dynamicTextureResolution() + 5);
		lineSize = scale / 8;
		lineSpacing = lineSize * 3 / 2;
		fontSizeBig = lineSize * 2;
		fontSizeSmall = fontSizeBig / 2;
	}

	public static NativeImage generateColorStrip(long platformId) {
		try {
			final List<Integer> colors = getRouteStream(platformId, (route, currentStationIndex) -> {
			});
			if (colors.isEmpty()) {
				return null;
			}
			final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, 1, colors.size(), false);
			for (int i = 0; i < colors.size(); i++) {
				drawPixelSafe(nativeImage, 0, i, ARGB_BLACK | colors.get(i));
			}
			return nativeImage;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static NativeImage generateStationName(String stationName, float aspectRatio) {
		if (aspectRatio <= 0) {
			return null;
		}

		try {
			final int height = scale * 2;
			final int width = Math.round(height * aspectRatio);
			final int padding = scale / 16;
			final int[] dimensions = new int[2];
			final byte[] pixels = ClientData.DATA_CACHE.getTextPixels(stationName, dimensions, width - padding * 2, height - padding * 2, fontSizeBig * 2, fontSizeSmall * 2, padding, HorizontalAlignment.CENTER);

			final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
			nativeImage.fillRect(0, 0, width, height, 0);
			drawString(nativeImage, pixels, width / 2, height / 2, dimensions, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
			return nativeImage;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static NativeImage generateTallStationName(int textColor, String stationName, int stationColor, float aspectRatio) {
		if (aspectRatio <= 0) {
			return null;
		}

		try {
			final int width = Math.round(scale * 1.6F);
			final int height = Math.round(width / aspectRatio);
			final int[] dimensions = new int[2];
			final byte[] pixels = ClientData.DATA_CACHE.getTextPixels(IGui.formatVerticalChinese(stationName), dimensions, width, height, fontSizeBig * 2, fontSizeSmall * 2, 0, HorizontalAlignment.CENTER);

			final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
			nativeImage.fillRect(0, 0, width, height, 0);
			drawString(nativeImage, pixels, width / 2, height / 2, dimensions, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, ARGB_BLACK | stationColor, textColor, false);
			clearColor(nativeImage, invertColor(ARGB_BLACK | stationColor));
			return nativeImage;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static NativeImage generateStationNameEntrance(int textColor, String stationName, float aspectRatio) {
		if (aspectRatio <= 0) {
			return null;
		}

		try {
			final int size = scale * 2;
			final int width = Math.round(size * aspectRatio);
			final int padding = scale / 16;
			final int[] dimensions = new int[2];
			final byte[] pixels = ClientData.DATA_CACHE.getTextPixels(stationName, dimensions, width - size - padding, size - padding * 2, fontSizeBig * 3, fontSizeSmall * 3, padding, HorizontalAlignment.LEFT);
			final int xOffset = (width - dimensions[0] - size) / 2;
			final int fakeBackgroundColor = textColor == ARGB_BLACK ? textColor + 0x010101 : 0;

			final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, size, false);
			nativeImage.fillRect(0, 0, width, size, fakeBackgroundColor);
			drawResource(nativeImage, LOGO_RESOURCE, xOffset, 0, size, size, false, 0, 1, 0, true);
			drawString(nativeImage, pixels, size + xOffset, size / 2, dimensions, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, fakeBackgroundColor, textColor, false);
			clearColor(nativeImage, invertColor(fakeBackgroundColor));

			return nativeImage;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static NativeImage generateSingleRowStationName(long platformId, float aspectRatio) {
		if (aspectRatio <= 0) {
			return null;
		}

		try {
			final int[] dimensions = new int[2];
			final byte[] pixels = ClientData.DATA_CACHE.getTextPixels(getStationName(platformId).replace("|", " | "), dimensions, fontSizeBig, fontSizeSmall);
			final int padding = dimensions[1] / 2;
			final int height = dimensions[1] + padding;
			final int width = Math.max(Math.round(height * aspectRatio), dimensions[0] + padding);

			final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
			nativeImage.fillRect(0, 0, width, height, ARGB_WHITE);
			drawString(nativeImage, pixels, width / 2, height / 2, dimensions, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, ARGB_BLACK, false);
			return nativeImage;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static NativeImage generateSignText(String text, HorizontalAlignment horizontalAlignment, float paddingScale, int backgroundColor, int textColor) {
		try {
			final int height = scale;
			final int padding = Math.round(height * paddingScale);
			final int tileSize = height - padding * 2;
			final int tilePadding = tileSize / 4;

			final int[] dimensions = new int[2];
			final byte[] pixels = ClientData.DATA_CACHE.getTextPixels(text, dimensions, Integer.MAX_VALUE, (int) (tileSize * ClientCache.LINE_HEIGHT_MULTIPLIER), tileSize * 3 / 5, tileSize * 3 / 10, tilePadding, horizontalAlignment);
			final int width = dimensions[0] - tilePadding * 2;

			if (width <= 0 || height <= 0) {
				return null;
			}

			final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
			nativeImage.fillRect(0, 0, width, height, 0);
			drawString(nativeImage, pixels, width / 2, height / 2, dimensions, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, backgroundColor, textColor, false);
			clearColor(nativeImage, invertColor(backgroundColor));

			return nativeImage;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static NativeImage generateLiftPanel(String text, int textColor) {
		try {
			final int width = Math.round(scale * 1.5F);
			final int height = fontSizeSmall * 2 * text.split("\\|").length;
			final int[] dimensions = new int[2];
			final byte[] pixels = ClientData.DATA_CACHE.getTextPixels(text.toUpperCase(Locale.ENGLISH), dimensions, width, height, fontSizeSmall * 2, fontSizeSmall * 2, 0, HorizontalAlignment.CENTER);
			final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
			nativeImage.fillRect(0, 0, width, height, 0);
			drawString(nativeImage, pixels, width / 2, height / 2, dimensions, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, ARGB_BLACK, textColor, false);
			clearColor(nativeImage, invertColor(ARGB_BLACK));

			return nativeImage;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static NativeImage generateExitSignLetter(String exitLetter, String exitNumber, int backgroundColor) {
		try {
			final int size = scale / 2;
			final boolean noNumber = exitNumber.isEmpty();
			final int textSize = size * 7 / 8;
			final int[] dimensions1 = new int[2];
			final byte[] pixels1 = ClientData.DATA_CACHE.getTextPixels(exitLetter, dimensions1, noNumber ? textSize : textSize * 2 / 3, textSize, textSize, size, size, HorizontalAlignment.CENTER);
			final int[] dimensions2 = new int[2];
			final byte[] pixels2 = noNumber ? null : ClientData.DATA_CACHE.getTextPixels(exitNumber, dimensions2, textSize / 3, textSize, textSize / 2, textSize / 2, size, HorizontalAlignment.CENTER);

			final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, size, size, false);
			nativeImage.fillRect(0, 0, size, size, backgroundColor);
			drawResource(nativeImage, EXIT_RESOURCE, 0, 0, size, size, false, 0, 1, 0, true);
			drawString(nativeImage, pixels1, size / 2 - (noNumber ? 0 : textSize / 6 - size / 32), size / 2, dimensions1, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
			if (!noNumber) {
				drawString(nativeImage, pixels2, size / 2 + textSize / 3 - size / 32, size / 2 + textSize / 8, dimensions2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
			}
			return nativeImage;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static NativeImage generateRouteSquare(int color, String routeName, HorizontalAlignment horizontalAlignment) {
		try {
			final int padding = scale / 32;
			final int[] dimensions = new int[2];
			final byte[] pixels = ClientData.DATA_CACHE.getTextPixels(routeName, dimensions, Integer.MAX_VALUE, (int) ((fontSizeBig + fontSizeSmall) * ClientCache.LINE_HEIGHT_MULTIPLIER), fontSizeBig, fontSizeSmall, padding, horizontalAlignment);

			final int width = dimensions[0] + padding * 2;
			final int height = dimensions[1] + padding * 2;
			final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
			nativeImage.fillRect(0, 0, width, height, invertColor(ARGB_BLACK | color));
			drawString(nativeImage, pixels, width / 2, height / 2, dimensions, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
			return nativeImage;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static NativeImage generateDirectionArrow(long platformId, boolean hasLeft, boolean hasRight, HorizontalAlignment horizontalAlignment, boolean showToString, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor) {
		if (aspectRatio <= 0) {
			return null;
		}

		try {
			final List<String> destinations = new ArrayList<>();
			final List<Integer> colors = getRouteStream(platformId, (route, currentStationIndex) -> destinations.add(ClientData.DATA_CACHE.getFormattedRouteDestination(route, currentStationIndex, TEMP_CIRCULAR_MARKER)));
			final boolean isTerminating = destinations.isEmpty();

			final boolean leftToRight = horizontalAlignment == HorizontalAlignment.CENTER ? hasLeft || !hasRight : horizontalAlignment != HorizontalAlignment.RIGHT;
			final int height = scale;
			final int width = Math.round(height * aspectRatio);
			final int padding = Math.round(height * paddingScale);
			final int tileSize = height - padding * 2;

			if (width <= 0 || height <= 0) {
				return null;
			}

			final ClientCache clientCache = ClientData.DATA_CACHE;
			final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
			nativeImage.fillRect(0, 0, width, height, invertColor(backgroundColor));

			final int circleX;
			if (isTerminating) {
				circleX = (int) horizontalAlignment.getOffset(0, tileSize - width);
			} else {
				String destinationString = IGui.mergeStations(destinations);
				final boolean noToString = destinationString.startsWith(TEMP_CIRCULAR_MARKER);
				destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER, "");
				if (!destinationString.isEmpty() && showToString && !noToString) {
					destinationString = IGui.insertTranslation("gui.mtr.to_cjk", "gui.mtr.to", 1, destinationString);
				}

				final int tilePadding = tileSize / 4;
				final int leftSize = ((hasLeft ? 1 : 0) + (leftToRight ? 1 : 0)) * (tileSize + tilePadding);
				final int rightSize = ((hasRight ? 1 : 0) + (leftToRight ? 0 : 1)) * (tileSize + tilePadding);

				final int[] dimensionsDestination = new int[2];
				final byte[] pixelsDestination = clientCache.getTextPixels(destinationString, dimensionsDestination, width - leftSize - rightSize - padding * (showToString ? 2 : 1), (int) (tileSize * ClientCache.LINE_HEIGHT_MULTIPLIER), tileSize * 3 / 5, tileSize * 3 / 10, tilePadding, leftToRight ? HorizontalAlignment.LEFT : HorizontalAlignment.RIGHT);
				final int leftPadding = (int) horizontalAlignment.getOffset(0, leftSize + rightSize + dimensionsDestination[0] - tilePadding * 2 - width);
				drawString(nativeImage, pixelsDestination, leftPadding + leftSize - tilePadding, height / 2, dimensionsDestination, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, backgroundColor, textColor, false);

				if (hasLeft) {
					drawResource(nativeImage, ARROW_RESOURCE, leftPadding, padding, tileSize, tileSize, false, 0, 1, textColor, false);
				}
				if (hasRight) {
					drawResource(nativeImage, ARROW_RESOURCE, leftPadding + leftSize + dimensionsDestination[0] - tilePadding * 2 + rightSize - tileSize, padding, tileSize, tileSize, true, 0, 1, textColor, false);
				}

				circleX = leftPadding + leftSize + (leftToRight ? -tileSize - tilePadding : dimensionsDestination[0] - tilePadding);
			}

			for (int i = 0; i < colors.size(); i++) {
				drawResource(nativeImage, CIRCLE_RESOURCE, circleX, padding, tileSize, tileSize, false, (float) i / colors.size(), (i + 1F) / colors.size(), colors.get(i), false);
			}

			final Platform platform = clientCache.platformIdMap.get(platformId);
			if (platform != null) {
				final int[] dimensionsPlatformNumber = new int[2];
				final byte[] pixelsPlatformNumber = clientCache.getTextPixels(platform.name, dimensionsPlatformNumber, tileSize, (int) (tileSize * ClientCache.LINE_HEIGHT_MULTIPLIER * 3 / 4), tileSize * 3 / 4, tileSize * 3 / 4, 0, HorizontalAlignment.CENTER);
				drawString(nativeImage, pixelsPlatformNumber, circleX + tileSize / 2, padding + tileSize / 2, dimensionsPlatformNumber, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
			}

			if (transparentColor != 0) {
				clearColor(nativeImage, invertColor(transparentColor));
			}

			return nativeImage;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static NativeImage generateRouteMap(long platformId, boolean vertical, boolean flip, float aspectRatio, boolean transparentWhite) {
		if (aspectRatio <= 0) {
			return null;
		}

		try {
			final List<Tuple<Route, Integer>> routeDetails = new ArrayList<>();
			getRouteStream(platformId, (route, currentStationIndex) -> routeDetails.add(new Tuple<>(route, currentStationIndex)));
			final int routeCount = routeDetails.size();

			if (routeCount > 0) {
				final ClientCache clientCache = ClientData.DATA_CACHE;
				final List<List<Long>> stationsIdsBefore = new ArrayList<>();
				final List<List<Long>> stationsIdsAfter = new ArrayList<>();
				final List<Map<Integer, StationPosition>> stationPositions = new ArrayList<>();
				final int[] colorIndices = new int[routeCount];
				final Set<Integer> currentRouteColors = new HashSet<>();
				final Set<String> currentRouteNames = new HashSet<>();
				int colorIndex = -1;
				int previousColor = -1;
				for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
					stationsIdsBefore.add(new ArrayList<>());
					stationsIdsAfter.add(new ArrayList<>());
					stationPositions.add(new HashMap<>());

					final Tuple<Route, Integer> routeDetail = routeDetails.get(routeIndex);
					final List<Route.RoutePlatform> platformIds = routeDetail.getA().platformIds;
					final int currentIndex = routeDetail.getB();
					for (int stationIndex = 0; stationIndex < platformIds.size(); stationIndex++) {
						if (stationIndex != currentIndex) {
							final long stationId = getStationId(platformIds.get(stationIndex).platformId);
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
					currentRouteColors.add(color);
					currentRouteNames.add(routeDetail.getA().name.split("\\|\\|")[0]);
				}

				for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
					stationPositions.get(routeIndex).put(0, new StationPosition(0, getLineOffset(routeIndex, colorIndices), true));
				}

				final float[] bounds = new float[3];
				setup(stationPositions, flip ? stationsIdsBefore : stationsIdsAfter, colorIndices, bounds, flip, true);
				final float xOffset = bounds[0] + 0.5F;
				setup(stationPositions, flip ? stationsIdsAfter : stationsIdsBefore, colorIndices, bounds, !flip, false);
				final float rawHeightPart = Math.abs(bounds[1]) + (vertical ? 0.6F : 1);
				final float rawWidth = xOffset + bounds[0] + 0.5F;
				final float rawHeightTotal = rawHeightPart + bounds[2] + (vertical ? 0.6F : 1);
				final float rawHeight;
				final float yOffset;
				final float extraPadding;
				if (vertical && rawHeightTotal < MIN_VERTICAL_SIZE) {
					rawHeight = MIN_VERTICAL_SIZE;
					extraPadding = (MIN_VERTICAL_SIZE - rawHeightTotal) / 2;
					yOffset = rawHeightPart + extraPadding;
				} else {
					rawHeight = rawHeightTotal;
					extraPadding = 0;
					yOffset = rawHeightPart;
				}

				final int height;
				final int width;
				final float widthScale;
				final float heightScale;
				if (rawWidth / rawHeight > aspectRatio) {
					width = Math.round(rawWidth * scale);
					height = Math.round(width / aspectRatio);
					widthScale = 1;
					heightScale = height / rawHeight / scale;
				} else {
					height = Math.round(rawHeight * scale);
					width = Math.round(height * aspectRatio);
					heightScale = 1;
					widthScale = width / rawWidth / scale;
				}

				if (width <= 0 || height <= 0) {
					return null;
				}

				final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
				nativeImage.fillRect(0, 0, width, height, ARGB_WHITE);

				final Map<Long, Set<StationPositionGrouped>> stationPositionsGrouped = new HashMap<>();
				for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
					final Route route = routeDetails.get(routeIndex).getA();
					final int currentIndex = routeDetails.get(routeIndex).getB();
					final Map<Integer, StationPosition> routeStationPositions = stationPositions.get(routeIndex);

					for (int stationIndex = 0; stationIndex < route.platformIds.size(); stationIndex++) {
						final StationPosition stationPosition = routeStationPositions.get(stationIndex - currentIndex);
						if (stationIndex < route.platformIds.size() - 1) {
							drawLine(nativeImage, stationPosition, routeStationPositions.get(stationIndex + 1 - currentIndex), widthScale, heightScale, xOffset, yOffset, stationIndex < currentIndex ? ARGB_LIGHT_GRAY : ARGB_BLACK | route.color);
						}

						final long stationId = getStationId(route.platformIds.get(stationIndex).platformId);
						if (!stationPositionsGrouped.containsKey(stationId)) {
							stationPositionsGrouped.put(stationId, new HashSet<>());
						}
						if (!stationPosition.isCommon || stationPositionsGrouped.get(stationId).stream().noneMatch(stationPosition2 -> stationPosition2.stationPosition.x == stationPosition.x)) {
							final Map<Integer, ClientCache.ColorNameTuple> interchangeRoutes = getInterchangeRoutes(stationId);
							final List<Integer> allColors = new ArrayList<>(interchangeRoutes.keySet());
							allColors.sort(Integer::compareTo);

							final List<Integer> interchangeColors = new ArrayList<>();
							final List<String> interchangeNames = new ArrayList<>();
							allColors.forEach(color -> {
								final String name = interchangeRoutes.get(color).name;
								if (!currentRouteColors.contains(color) && !currentRouteNames.contains(name)) {
									if (!interchangeColors.contains(color)) {
										interchangeColors.add(color);
									}
									if (!interchangeNames.contains(name)) {
										interchangeNames.add(name);
									}
								}
							});
							stationPositionsGrouped.get(stationId).add(new StationPositionGrouped(stationPosition, stationIndex - currentIndex, interchangeColors, interchangeNames));
						}
					}
				}

				final int maxStringWidth = (int) (scale * 0.9 * ((vertical ? heightScale : widthScale) / 2 + extraPadding / routeCount));
				stationPositionsGrouped.forEach((stationId, stationPositionGroupedSet) -> stationPositionGroupedSet.forEach(stationPositionGrouped -> {
					final int x = Math.round((stationPositionGrouped.stationPosition.x + xOffset) * scale * widthScale);
					final int y = Math.round((stationPositionGrouped.stationPosition.y + yOffset) * scale * heightScale);
					final int lines = stationPositionGrouped.stationPosition.isCommon ? colorIndices[colorIndices.length - 1] : 0;
					final boolean textBelow = vertical || (stationPositionGrouped.stationPosition.isCommon ? Math.abs(stationPositionGrouped.stationOffset) % 2 == 0 : y >= yOffset * scale);
					final boolean currentStation = stationPositionGrouped.stationOffset == 0;
					final boolean passed = stationPositionGrouped.stationOffset < 0;

					final List<Integer> interchangeColors = stationPositionGrouped.interchangeColors;
					if (!interchangeColors.isEmpty() && !currentStation) {
						final int lineHeight = lineSize * 2;
						final int lineWidth = (int) Math.ceil((float) lineSize / interchangeColors.size());
						for (int i = 0; i < interchangeColors.size(); i++) {
							for (int drawX = 0; drawX < lineWidth; drawX++) {
								for (int drawY = 0; drawY < lineHeight; drawY++) {
									drawPixelSafe(nativeImage, x + drawX + lineWidth * i - lineWidth * interchangeColors.size() / 2, y + (textBelow ? -1 : lines * lineSpacing) + (textBelow ? -drawY : drawY), passed ? ARGB_LIGHT_GRAY : ARGB_BLACK | interchangeColors.get(i));
								}
							}
						}

						final int[] dimensions = new int[2];
						final byte[] pixels = clientCache.getTextPixels(IGui.mergeStations(stationPositionGrouped.interchangeNames), dimensions, maxStringWidth - (vertical ? lineHeight : 0), (int) ((fontSizeBig + fontSizeSmall) * ClientCache.LINE_HEIGHT_MULTIPLIER / 2), fontSizeBig / 2, fontSizeSmall / 2, 0, vertical ? HorizontalAlignment.LEFT : HorizontalAlignment.CENTER);
						drawString(nativeImage, pixels, x, y + (textBelow ? -1 - lineHeight : lines * lineSpacing + lineHeight), dimensions, HorizontalAlignment.CENTER, textBelow ? VerticalAlignment.BOTTOM : VerticalAlignment.TOP, 0, passed ? ARGB_LIGHT_GRAY : ARGB_BLACK, vertical);
					}

					drawStation(nativeImage, x, y, heightScale, lines, passed);

					final Station station = clientCache.stationIdMap.get(stationId);
					final int[] dimensions = new int[2];
					final byte[] pixels = clientCache.getTextPixels(station == null ? "" : station.name, dimensions, maxStringWidth, (int) ((fontSizeBig + fontSizeSmall) * ClientCache.LINE_HEIGHT_MULTIPLIER), fontSizeBig, fontSizeSmall, fontSizeSmall / 4, vertical ? HorizontalAlignment.RIGHT : HorizontalAlignment.CENTER);
					drawString(nativeImage, pixels, x, y + (textBelow ? lines * lineSpacing : -1) + (textBelow ? 1 : -1) * lineSize * 5 / 4, dimensions, HorizontalAlignment.CENTER, textBelow ? VerticalAlignment.TOP : VerticalAlignment.BOTTOM, currentStation ? ARGB_BLACK : 0, passed ? ARGB_LIGHT_GRAY : currentStation ? ARGB_WHITE : ARGB_BLACK, vertical);
				}));

				if (transparentWhite) {
					clearColor(nativeImage, ARGB_WHITE);
				}

				return nativeImage;
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
			if (stationId != 0 && !commonStationIds.contains(stationId) && stationsIdLists.stream().allMatch(stationsIds -> stationsIds.contains(stationId))) {
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
						bounds[0] = Math.max(bounds[0], stationX / 2);
						final float stationY = routesIndicesInSection.indexOf(routeIndex) - (routesIndicesInSection.size() - 1) / 2F + getLineOffset(routeIndex, colorIndices);
						bounds[1] = Math.min(bounds[1], stationY);
						bounds[2] = Math.max(bounds[2], stationY);
						stationPositions.get(routeIndex).put(passedMultiplier * (j + traverseIndex[routeIndex] + 1), new StationPosition(reverseMultiplier * stationX / 2, stationY, false));
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
					stationPositions.get(routeIndex).put(passedMultiplier * traverseIndex[routeIndex], new StationPosition(reverseMultiplier * positionXOffset / 2F, stationY, true));
				}
				bounds[0] = positionXOffset / 2F;
			}
		}
	}

	private static float getLineOffset(int routeIndex, int[] colorIndices) {
		return (float) lineSpacing / scale * (colorIndices[routeIndex] - colorIndices[colorIndices.length - 1] / 2F);
	}

	private static List<Integer> getRouteStream(long platformId, BiConsumer<Route, Integer> nonTerminatingCallback) {
		final List<Integer> colors = new ArrayList<>();
		final List<Integer> terminatingColors = new ArrayList<>();
		ClientData.ROUTES.stream().filter(route -> route.containsPlatformId(platformId) && !route.isHidden).sorted((a, b) -> a.color == b.color ? a.compareTo(b) : a.color - b.color).forEach(route -> {
			final int currentStationIndex = route.getPlatformIdIndex(platformId);
			if (currentStationIndex < route.platformIds.size() - 1) {
				nonTerminatingCallback.accept(route, currentStationIndex);
				if (!colors.contains(route.color)) {
					colors.add(route.color);
				}
			} else {
				if (!terminatingColors.contains(route.color)) {
					terminatingColors.add(route.color);
				}
			}
		});
		if (colors.isEmpty()) {
			colors.addAll(terminatingColors);
		}
		return colors;
	}

	private static long getStationId(long platformId) {
		final Station station = ClientData.DATA_CACHE.platformIdToStation.get(platformId);
		return station == null ? -1 : station.id;
	}

	private static String getStationName(long platformId) {
		final Station station = ClientData.DATA_CACHE.platformIdToStation.get(platformId);
		return station == null ? "" : station.name;
	}

	private static Map<Integer, ClientCache.ColorNameTuple> getInterchangeRoutes(long stationId) {
		return ClientData.DATA_CACHE.stationIdToRoutes.get(stationId);
	}

	private static void drawLine(NativeImage nativeImage, StationPosition stationPosition1, StationPosition stationPosition2, float widthScale, float heightScale, float xOffset, float yOffset, int color) {
		final int x1 = Math.round((stationPosition1.x + xOffset) * scale * widthScale);
		final int x2 = Math.round((stationPosition2.x + xOffset) * scale * widthScale);
		final int y1 = Math.round((stationPosition1.y + yOffset) * scale * heightScale);
		final int y2 = Math.round((stationPosition2.y + yOffset) * scale * heightScale);
		final int xChange = x2 - x1;
		final int yChange = y2 - y1;
		final int xChangeAbs = Math.abs(xChange);
		final int yChangeAbs = Math.abs(yChange);
		final int changeDifference = Math.abs(yChangeAbs - xChangeAbs);

		if (xChangeAbs > yChangeAbs) {
			final boolean y1OffsetGreater = Math.abs(y1 - yOffset * scale) > Math.abs(y2 - yOffset * scale);
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
		final int halfLineHeight = lineSize / 2;
		final int xWidth = directionX == 0 ? halfLineHeight : 0;
		final int yWidth = directionX == 0 ? 0 : directionY == 0 ? halfLineHeight : Math.round(lineSize * Mth.SQRT_OF_TWO / 2);
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

	private static void drawStation(NativeImage nativeImage, int x, int y, float heightScale, int lines, boolean passed) {
		for (int offsetX = -lineSize; offsetX < lineSize; offsetX++) {
			for (int offsetY = -lineSize; offsetY < lineSize; offsetY++) {
				final int extraOffsetY = offsetY > 0 ? (int) (lines * lineSpacing * heightScale) : 0;
				final int repeatDraw = offsetY == 0 ? (int) (lines * lineSpacing * heightScale) : 0;
				final double squareSum = (offsetX + 0.5) * (offsetX + 0.5) + (offsetY + 0.5) * (offsetY + 0.5);

				if (squareSum <= 0.5 * lineSize * lineSize) {
					for (int i = 0; i <= repeatDraw; i++) {
						drawPixelSafe(nativeImage, x + offsetX, y + offsetY + extraOffsetY + i, ARGB_WHITE);
					}
				} else if (squareSum <= lineSize * lineSize) {
					for (int i = 0; i <= repeatDraw; i++) {
						drawPixelSafe(nativeImage, x + offsetX, y + offsetY + extraOffsetY + i, passed ? ARGB_LIGHT_GRAY : ARGB_BLACK);
					}
				}
			}
		}
	}

	private static void drawString(NativeImage nativeImage, byte[] pixels, int x, int y, int[] textDimensions, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, int backgroundColor, int textColor, boolean rotate90) {
		if (((backgroundColor >> 24) & 0xFF) > 0) {
			for (int drawX = 0; drawX < textDimensions[rotate90 ? 1 : 0]; drawX++) {
				for (int drawY = 0; drawY < textDimensions[rotate90 ? 0 : 1]; drawY++) {
					drawPixelSafe(nativeImage, (int) horizontalAlignment.getOffset(drawX + x, textDimensions[rotate90 ? 1 : 0]), (int) verticalAlignment.getOffset(drawY + y, textDimensions[rotate90 ? 0 : 1]), backgroundColor);
				}
			}
		}
		int drawX = 0;
		int drawY = rotate90 ? textDimensions[0] - 1 : 0;
		for (int i = 0; i < textDimensions[0] * textDimensions[1]; i++) {
			blendPixel(nativeImage, (int) horizontalAlignment.getOffset(x + drawX, textDimensions[rotate90 ? 1 : 0]), (int) verticalAlignment.getOffset(y + drawY, textDimensions[rotate90 ? 0 : 1]), ((pixels[i] & 0xFF) << 24) + (textColor & RGB_WHITE));
			if (rotate90) {
				drawY--;
				if (drawY < 0) {
					drawY = textDimensions[0] - 1;
					drawX++;
				}
			} else {
				drawX++;
				if (drawX == textDimensions[0]) {
					drawX = 0;
					drawY++;
				}
			}
		}
	}

	private static void drawResource(NativeImage nativeImage, String resource, int x, int y, int width, int height, boolean flipX, float v1, float v2, int color, boolean useActualColor) throws IOException {
		final NativeImage nativeImageResource = NativeImage.read(NativeImage.Format.RGBA, Utilities.getInputStream(Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(MTR.MOD_ID, resource))));
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
				final int pixel1 = nativeImageResource.getPixelRGBA(Mth.clamp(floorX, 0, resourceWidth - 1), Mth.clamp(floorY, 0, resourceHeight - 1));
				final int pixel2 = nativeImageResource.getPixelRGBA(Mth.clamp(ceilX, 0, resourceWidth - 1), Mth.clamp(floorY, 0, resourceHeight - 1));
				final int pixel3 = nativeImageResource.getPixelRGBA(Mth.clamp(floorX, 0, resourceWidth - 1), Mth.clamp(ceilY, 0, resourceHeight - 1));
				final int pixel4 = nativeImageResource.getPixelRGBA(Mth.clamp(ceilX, 0, resourceWidth - 1), Mth.clamp(ceilY, 0, resourceHeight - 1));
				final int newColor;
				if (useActualColor) {
					newColor = invertColor(pixel1);
				} else {
					final float luminance1 = ((pixel1 >> 24) & 0xFF) * percentX1 * percentY1;
					final float luminance2 = ((pixel2 >> 24) & 0xFF) * percentX2 * percentY1;
					final float luminance3 = ((pixel3 >> 24) & 0xFF) * percentX1 * percentY2;
					final float luminance4 = ((pixel4 >> 24) & 0xFF) * percentX2 * percentY2;
					newColor = (color & RGB_WHITE) + ((int) (luminance1 + luminance2 + luminance3 + luminance4) << 24);
				}
				blendPixel(nativeImage, (flipX ? width - drawX - 1 : drawX) + x, drawY + y, newColor);
			}
		}
	}

	private static void blendPixel(NativeImage nativeImage, int x, int y, int color) {
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
				final int finalColor = ARGB_BLACK | (((int) (r1 * inversePercent + r2 * percent) << 16) + ((int) (g1 * inversePercent + g2 * percent) << 8) + (int) (b1 * inversePercent + b2 * percent));
				drawPixelSafe(nativeImage, x, y, finalColor);
			}
		}
	}

	private static void drawPixelSafe(NativeImage nativeImage, int x, int y, int color) {
		if (RailwayData.isBetween(x, 0, nativeImage.getWidth() - 1) && RailwayData.isBetween(y, 0, nativeImage.getHeight() - 1)) {
			nativeImage.setPixelRGBA(x, y, invertColor(color));
		}
	}

	private static int invertColor(int color) {
		return ((color & ARGB_BLACK) != 0 ? ARGB_BLACK : 0) + ((color & 0xFF) << 16) + (color & 0xFF00) + ((color & 0xFF0000) >> 16);
	}

	private static void clearColor(NativeImage nativeImage, int color) {
		for (int x = 0; x < nativeImage.getWidth(); x++) {
			for (int y = 0; y < nativeImage.getHeight(); y++) {
				if (nativeImage.getPixelRGBA(x, y) == color) {
					nativeImage.setPixelRGBA(x, y, 0);
				}
			}
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

	private static class StationPositionGrouped {

		private final StationPosition stationPosition;
		private final int stationOffset;
		private final List<Integer> interchangeColors;
		private final List<String> interchangeNames;

		private StationPositionGrouped(StationPosition stationPosition, int stationOffset, List<Integer> interchangeColors, List<String> interchangeNames) {
			this.stationPosition = stationPosition;
			this.stationOffset = stationOffset;
			this.interchangeColors = interchangeColors;
			this.interchangeNames = interchangeNames;
		}
	}
}
