package mtr.gui;

import com.mojang.blaze3d.platform.NativeImage;
import mtr.MTR;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.data.Route;
import mtr.data.Station;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;

import java.util.*;

public class RouteMapGenerator implements IGui {

	private static final int SCALE = 32;
	private static final int PADDING = 1;
	private static final float LINE_OFFSET = 1F / 8;

	public static ResourceLocation generate(List<Tuple<Route, Integer>> routeDetails) {
		final int routeCount = routeDetails.size();

		if (routeCount > 0) {
			try {
				routeDetails.sort(Comparator.comparingInt(routeDetail -> routeDetail.getA().color));

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
					for (int stationindex = 0; stationindex < platformIds.size(); stationindex++) {
						if (stationindex != currentIndex) {
							final Station station = ClientData.DATA_CACHE.platformIdToStation.get(platformIds.get(stationindex));
							final long stationId = station == null ? -1 : station.id;
							if (stationindex < currentIndex) {
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
				setup(stationPositions, stationsIdsBefore, colorIndices, bounds, true);
				final float xOffset = bounds[0] + PADDING;
				setup(stationPositions, stationsIdsAfter, colorIndices, bounds, false);
				final float yOffset = Math.abs(bounds[1]) + PADDING;

				final int width = Math.round((xOffset + bounds[0] + PADDING) * SCALE);
				final int height = Math.round((yOffset + bounds[2] + PADDING) * SCALE);

				final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
				nativeImage.fillRect(0, 0, width, height, ARGB_WHITE);

				for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
					final Route route = routeDetails.get(routeIndex).getA();
					final int currentIndex = routeDetails.get(routeIndex).getB();
					final Map<Integer, StationPosition> routeStationPositions = stationPositions.get(routeIndex);

					for (int stationIndex = 0; stationIndex < route.platformIds.size() - 1; stationIndex++) {
						final StationPosition stationPosition1 = routeStationPositions.get(stationIndex - currentIndex);
						final StationPosition stationPosition2 = routeStationPositions.get(stationIndex + 1 - currentIndex);
						drawLine(nativeImage, stationPosition1, stationPosition2, xOffset, yOffset, route.color);
					}

					routeStationPositions.forEach((stationOffsetIndex, stationPosition) -> {
						final int x = Math.round((stationPosition.x + xOffset) * SCALE);
						final int y = Math.round((stationPosition.y + yOffset) * SCALE);
						final int color = getColor(route.platformIds.get(stationOffsetIndex + currentIndex));
						nativeImage.fillRect(x - 2, y - 2, 4, 4, color);
					});
				}

				return Minecraft.getInstance().getTextureManager().register(MTR.MOD_ID, new DynamicTexture(nativeImage));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return new ResourceLocation(MTR.MOD_ID, "textures/block/white.png");
	}

	private static void setup(List<Map<Integer, StationPosition>> stationPositions, List<List<Long>> stationsIdLists, int[] colorIndices, float[] bounds, boolean reverse) {
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
						stationPositions.get(routeIndex).put(reverseMultiplier * (j + traverseIndex[routeIndex] + 1), new StationPosition(reverseMultiplier * stationX, stationY, false));
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
					stationPositions.get(routeIndex).put(reverseMultiplier * traverseIndex[routeIndex], new StationPosition(reverseMultiplier * positionXOffset, stationY, true));
				}
				bounds[0] = positionXOffset;
			}
		}
	}

	private static float getLineOffset(int routeIndex, int[] colorIndices) {
		return LINE_OFFSET * (colorIndices[routeIndex] - colorIndices[colorIndices.length - 1] / 2F);
	}

	private static int getColor(long platformId) {
		final Station station = ClientData.DATA_CACHE.platformIdToStation.get(platformId);
		return formatColor(station == null ? ARGB_BLACK : (station.color | ARGB_BLACK));
	}

	private static void drawLine(NativeImage nativeImage, StationPosition stationPosition1, StationPosition stationPosition2, float xOffset, float yOffset, int color) {
		final int x1 = Math.round((stationPosition1.x + xOffset) * SCALE);
		final int x2 = Math.round((stationPosition2.x + xOffset) * SCALE);
		final int y1 = Math.round((stationPosition1.y + yOffset) * SCALE);
		final int y2 = Math.round((stationPosition2.y + yOffset) * SCALE);

		final int steps = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
		for (int i = 0; i < steps; i++) {
			drawPixelSafe(nativeImage, x1 + (x2 - x1) * i / steps, y1 + (y2 - y1) * i / steps, color);
		}
	}

	private static void drawPixelSafe(NativeImage nativeImage, int x, int y, int color) {
		if (RailwayData.isBetween(x, 0, nativeImage.getWidth() - 1) && RailwayData.isBetween(y, 0, nativeImage.getHeight() - 1)) {
			nativeImage.setPixelRGBA(x, y, formatColor(color));
		}
	}

	private static int formatColor(int color) {
		return ARGB_BLACK + ((color & 0xFF) << 16) + (color & 0xFF00) + ((color & 0xFF0000) >> 16);
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
