package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.ClientCache;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.Platform;
import mtr.data.Route;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RouteRenderer implements IGui {

	private final PoseStack matrices;
	private final MultiBufferSource vertexConsumers;
	private final MultiBufferSource.BufferSource immediate;
	private final Font textRenderer;

	private final List<ClientCache.PlatformRouteDetails> routeData;
	private final String platformNumber;
	private final boolean vertical;
	private final boolean glowing;

	private static final int PASSED_STATION_COLOR = 0xFF999999;
	private static final int STATION_CIRCLE_SIZE = 16;
	private static final int STATION_TEXT_PADDING = 14;
	private static final int STATION_NAME_BACKGROUND_PADDING = 3;
	private static final float COLOR_LINE_HALF_HEIGHT = 4.5F;
	private static final int INTERCHANGE_HALF_HEIGHT = 8;
	private static final int INTERCHANGE_LINE_SIZE = 10;
	private static final float PLATFORM_NUMBER_OFFSET_TOP = 0.63F;
	private static final String TEMP_CIRCULAR_MARKER = "temp_circular_marker";

	public RouteRenderer(PoseStack matrices, MultiBufferSource vertexConsumers, MultiBufferSource.BufferSource immediate, Platform platform, boolean vertical, boolean glowing) {
		this.matrices = matrices;
		this.vertexConsumers = vertexConsumers;
		this.immediate = immediate;

		final List<ClientCache.PlatformRouteDetails> platformRouteDetails = platform == null ? null : ClientData.DATA_CACHE.requestPlatformIdToRoutes(platform.id);
		routeData = platformRouteDetails == null ? new ArrayList<>() : platformRouteDetails;

		platformNumber = platform == null ? "1" : platform.name;
		this.vertical = vertical;
		this.glowing = glowing;
		textRenderer = Minecraft.getInstance().font;
	}

	public void renderColorStrip(float x1, float y1, float z1, float x2, float y2, float z2, Direction facing, int light) {
		final int routeCount = routeData.size();
		if (routeCount <= 0) {
			return;
		}

		matrices.pushPose();
		final int newLight = convertLight(light);
		final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(getRenderLayer("mtr:textures/block/white.png", newLight, false));
		final float lineHeightSmall = (y2 - y1) / routeCount;
		for (int i = 0; i < routeCount; i++) {
			final int routeColor = routeData.get(i).routeColor | ARGB_BLACK;
			IDrawing.drawTexture(matrices, vertexConsumer, x1, y1 + lineHeightSmall * i, z1, x2, y1 + lineHeightSmall * (i + 1), z2, facing, routeColor, newLight);
		}
		matrices.popPose();
	}

	public void renderLine(float start, float end, float side1, float side2, int scale, Direction facing, int light, boolean skipText) {
		final List<ClientCache.PlatformRouteDetails> filteredRouteData = routeData.stream().filter(platformRouteDetails -> platformRouteDetails.currentStationIndex < platformRouteDetails.stationDetails.size() - 1).collect(Collectors.toList());
		final int routeCount = filteredRouteData.size();
		if (routeCount <= 0) {
			return;
		}

		final int scaleSmaller = scale * routeCount;
		final float routeHeight = (side2 - side1) * scaleSmaller / routeCount;
		final float startScaled = start * scaleSmaller;
		final float endScaled = end * scaleSmaller;
		final float smallOffset = SMALL_OFFSET * scaleSmaller;
		final int newLight = convertLight(light);
		final MultiBufferSource.BufferSource immediateFiltered = skipText ? null : immediate;

		for (int i = 0; i < routeCount; i++) {
			final int routeColor = filteredRouteData.get(i).routeColor | ARGB_BLACK;
			final int currentStationIndex = filteredRouteData.get(i).currentStationIndex;
			final List<ClientCache.PlatformRouteDetails.StationDetails> stationDetails = filteredRouteData.get(i).stationDetails;
			final int routeLength = stationDetails.size();
			final float routePosition = routeHeight * (i + 0.5F) + side1 * scaleSmaller;

			matrices.pushPose();
			matrices.scale(1F / scaleSmaller, 1F / scaleSmaller, 1F / scaleSmaller);

			float thisStationPosition = getStationPosition(currentStationIndex, routeLength, startScaled, endScaled);
			final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(getRenderLayer("mtr:textures/block/white.png", newLight, false));
			if (vertical) {
				IDrawing.drawTexture(matrices, vertexConsumer, routePosition - COLOR_LINE_HALF_HEIGHT, Math.min(thisStationPosition, endScaled), smallOffset, routePosition + COLOR_LINE_HALF_HEIGHT, Math.max(thisStationPosition, endScaled), smallOffset, facing, PASSED_STATION_COLOR, newLight);
				IDrawing.drawTexture(matrices, vertexConsumer, routePosition - COLOR_LINE_HALF_HEIGHT, Math.min(startScaled, thisStationPosition), smallOffset, routePosition + COLOR_LINE_HALF_HEIGHT, Math.max(startScaled, thisStationPosition), smallOffset, facing, routeColor, newLight);
				IDrawing.drawStringWithFont(matrices, textRenderer, immediateFiltered, filteredRouteData.get(i).routeName, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, routePosition - routeHeight / 2, startScaled, 0.125F * scale, -1, 1, ARGB_BLACK, false, newLight, null);
			} else {
				IDrawing.drawTexture(matrices, vertexConsumer, Math.min(startScaled, thisStationPosition), routePosition - COLOR_LINE_HALF_HEIGHT, smallOffset, Math.max(startScaled, thisStationPosition), routePosition + COLOR_LINE_HALF_HEIGHT, smallOffset, facing, PASSED_STATION_COLOR, newLight);
				IDrawing.drawTexture(matrices, vertexConsumer, Math.min(thisStationPosition, endScaled), routePosition - COLOR_LINE_HALF_HEIGHT, smallOffset, Math.max(thisStationPosition, endScaled), routePosition + COLOR_LINE_HALF_HEIGHT, smallOffset, facing, routeColor, newLight);
			}

			for (int j = 0; j < routeLength; j++) {
				final float x = vertical ? routePosition : getStationPosition(j, routeLength, startScaled, endScaled);
				final float y = vertical ? getStationPosition(j, routeLength, startScaled, endScaled) : routePosition;
				final boolean onOrAfterStation = j >= currentStationIndex;
				final boolean onStation = j == currentStationIndex;
				final boolean bottomText = (j % 2) == 0;
				final List<ClientCache.ColorNameTuple> interchangeRoutes = stationDetails.get(j).interchangeRoutes;
				final int interchangeCount = interchangeRoutes.size();

				final VertexConsumer vertexConsumerStationCircle = vertexConsumers.getBuffer(getRenderLayer(onOrAfterStation ? "mtr:textures/block/station_circle.png" : "mtr:textures/block/station_circle_passed.png", newLight, true));
				if (interchangeCount <= 1 || onStation) {
					IDrawing.drawTexture(matrices, vertexConsumerStationCircle, x - STATION_CIRCLE_SIZE / 2F, y - STATION_CIRCLE_SIZE / 2F, STATION_CIRCLE_SIZE, STATION_CIRCLE_SIZE, facing, newLight);

					if (!onStation && interchangeCount == 1) {
						final VertexConsumer vertexConsumerInterchange = vertexConsumers.getBuffer(getRenderLayer("mtr:textures/block/interchange.png", newLight, true));
						if (vertical) {
							IDrawing.drawTexture(matrices, vertexConsumerInterchange, x + STATION_CIRCLE_SIZE / 2F, y - INTERCHANGE_LINE_SIZE / 2F, INTERCHANGE_LINE_SIZE, INTERCHANGE_LINE_SIZE, 0.625F, 0.3125F, 1, 0.6875F, facing, onOrAfterStation ? interchangeRoutes.get(0).color | ARGB_BLACK : PASSED_STATION_COLOR, newLight);
							IDrawing.drawStringWithFont(matrices, textRenderer, immediateFiltered, interchangeRoutes.get(0).name, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, x + INTERCHANGE_LINE_SIZE * 2, y, 2, onOrAfterStation ? ARGB_BLACK : PASSED_STATION_COLOR, false, newLight, null);
						} else {
							final VerticalAlignment verticalAlignment = bottomText ? VerticalAlignment.BOTTOM : VerticalAlignment.TOP;
							final float yLine = y + (bottomText ? -STATION_CIRCLE_SIZE / 2F - INTERCHANGE_LINE_SIZE : STATION_CIRCLE_SIZE / 2F);
							final float yText = yLine + (bottomText ? 0 : INTERCHANGE_LINE_SIZE);
							IDrawing.drawTexture(matrices, vertexConsumerInterchange, x - INTERCHANGE_LINE_SIZE / 2F, yLine, INTERCHANGE_LINE_SIZE, INTERCHANGE_LINE_SIZE, 0.3125F, bottomText ? 0 : 0.625F, 0.6875F, bottomText ? 0.375F : 1, facing, onOrAfterStation ? interchangeRoutes.get(0).color | ARGB_BLACK : PASSED_STATION_COLOR, newLight);
							IDrawing.drawStringWithFont(matrices, textRenderer, immediateFiltered, interchangeRoutes.get(0).name, HorizontalAlignment.CENTER, verticalAlignment, x, yText, 2, onOrAfterStation ? ARGB_BLACK : PASSED_STATION_COLOR, false, newLight, null);
						}
					}
				} else {
					final int totalHeight = (interchangeCount - (vertical ? 1 : 0)) * INTERCHANGE_HALF_HEIGHT * 2;
					final float y2 = vertical ? y - totalHeight / 2F : bottomText ? y - totalHeight : y;
					final float y1 = y2 - STATION_CIRCLE_SIZE / 2F;
					final float y3 = y2 + totalHeight;
					IDrawing.drawTexture(matrices, vertexConsumerStationCircle, x - STATION_CIRCLE_SIZE / 2F, y1, STATION_CIRCLE_SIZE, STATION_CIRCLE_SIZE / 2F, 0, 0, 1, 0.5F, facing, -1, newLight);
					IDrawing.drawTexture(matrices, vertexConsumerStationCircle, x - STATION_CIRCLE_SIZE / 2F, y2, STATION_CIRCLE_SIZE, totalHeight, 0, 0.49F, 1, 0.51F, facing, -1, newLight);
					IDrawing.drawTexture(matrices, vertexConsumerStationCircle, x - STATION_CIRCLE_SIZE / 2F, y3, STATION_CIRCLE_SIZE, STATION_CIRCLE_SIZE / 2F, 0, 0.5F, 1, 1, facing, -1, newLight);

					final VertexConsumer vertexConsumerInterchange = vertexConsumers.getBuffer(getRenderLayer("mtr:textures/block/interchange.png", newLight, true));
					for (int k = 0; k < interchangeCount; k++) {
						final float yLine = y2 + (k + (vertical ? 0 : bottomText ? 0 : 1)) * INTERCHANGE_HALF_HEIGHT * 2;
						IDrawing.drawStringWithFont(matrices, textRenderer, immediateFiltered, interchangeRoutes.get(k).name, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, x + INTERCHANGE_LINE_SIZE * 2, yLine, 2, onOrAfterStation ? ARGB_BLACK : PASSED_STATION_COLOR, false, newLight, null);
						IDrawing.drawTexture(matrices, vertexConsumerInterchange, x + STATION_CIRCLE_SIZE / 2F, yLine - INTERCHANGE_LINE_SIZE / 2F, INTERCHANGE_LINE_SIZE, INTERCHANGE_LINE_SIZE, 0.625F, 0.3125F, 1, 0.6875F, facing, onOrAfterStation ? interchangeRoutes.get(k).color | ARGB_BLACK : PASSED_STATION_COLOR, newLight);
					}
				}

				final HorizontalAlignment horizontalAlignment = vertical ? HorizontalAlignment.RIGHT : HorizontalAlignment.CENTER;
				final VerticalAlignment verticalAlignment = vertical ? VerticalAlignment.CENTER : bottomText ? VerticalAlignment.TOP : VerticalAlignment.BOTTOM;
				IDrawing.drawStringWithFont(matrices, textRenderer, immediateFiltered, IGui.textOrUntitled(stationDetails.get(j).stationName), horizontalAlignment, verticalAlignment, x - (vertical ? STATION_TEXT_PADDING : 0), y + (vertical ? 0 : bottomText ? STATION_TEXT_PADDING : -STATION_TEXT_PADDING), 1, onStation ? ARGB_WHITE : onOrAfterStation ? ARGB_BLACK : PASSED_STATION_COLOR, false, newLight, (x1, y1, x2, y2) -> {
					if (onStation) {
						matrices.pushPose();
						IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(getRenderLayer("mtr:textures/block/white.png", newLight, false)), x1 - STATION_NAME_BACKGROUND_PADDING, y1 - STATION_NAME_BACKGROUND_PADDING, smallOffset, x2 + STATION_NAME_BACKGROUND_PADDING, y2 + STATION_NAME_BACKGROUND_PADDING, smallOffset, facing, ARGB_BLACK, newLight);
						matrices.popPose();
					}
				});
			}

			matrices.popPose();
		}
	}

	public void renderArrow(float left, float right, float top, float bottom, boolean hasRight, boolean hasLeft, Direction facing, int light) {
//		renderArrow(left, right, top, bottom, hasRight, hasLeft, facing, light, true);
	}

	public void renderArrow(float left, float right, float top, float bottom, boolean hasRight, boolean hasLeft, Direction facing, int light, boolean visibleArrow) {
		final int routeCount = routeData.size();
		if (routeCount <= 0) {
			return;
		}

		final float arrowSize = bottom - top;
		final float arrowPadding = arrowSize / 4;

		String destinationString = IGui.mergeStations(routeData.stream().filter(route -> route.currentStationIndex < route.stationDetails.size() - 1).map(route -> {
			if (route.circularState == Route.CircularState.NONE) {
				return route.stationDetails.get(route.stationDetails.size() - 1).stationName;
			} else {
				boolean isVia = false;
				String text = "";
				for (int i = route.currentStationIndex + 1; i < route.stationDetails.size() - 1; i++) {
					if (!route.stationDetails.get(i).interchangeRoutes.isEmpty()) {
						text = route.stationDetails.get(i).stationName;
						isVia = true;
						break;
					}
				}
				if (!isVia) {
					text = route.stationDetails.get(route.stationDetails.size() - 1).stationName;
				}
				final String translationString = String.format("%s_%s", route.circularState == Route.CircularState.CLOCKWISE ? "clockwise" : "anticlockwise", isVia ? "via" : "to");
				return TEMP_CIRCULAR_MARKER + IGui.insertTranslation("gui.mtr." + translationString + "_cjk", "gui.mtr." + translationString, 1, text);
			}
		}).collect(Collectors.toList()));

		final boolean noToString = destinationString.startsWith(TEMP_CIRCULAR_MARKER);
		destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER, "");
		if (!destinationString.isEmpty() && visibleArrow && !noToString) {
			destinationString = IGui.insertTranslation("gui.mtr.to_cjk", "gui.mtr.to", 1, destinationString);
		}

		final boolean leftToRight = hasLeft || !hasRight;
		final float centerX = (left + right) / 2;

		matrices.pushPose();
		final int newLight = convertLight(light);

		if (destinationString.isEmpty() && visibleArrow) {
			final float chunkHeight = arrowSize / routeCount;
			final VertexConsumer vertexConsumerCircle = vertexConsumers.getBuffer(getRenderLayer("mtr:textures/sign/circle.png", newLight, true));
			for (int i = 0; i < routeCount; i++) {
				IDrawing.drawTexture(matrices, vertexConsumerCircle, centerX - arrowSize / 2, top + i * chunkHeight, arrowSize, chunkHeight, 0, (float) i / routeCount, 1, (float) (i + 1) / routeCount, facing, ARGB_BLACK | routeData.get(i).routeColor, newLight);
			}

			matrices.pushPose();
			matrices.translate(0, 0, -SMALL_OFFSET);
			IDrawing.drawStringWithFont(matrices, textRenderer, immediate, platformNumber, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, centerX, top + arrowSize * PLATFORM_NUMBER_OFFSET_TOP, RenderRailwaySign.HEIGHT_TO_SCALE / arrowSize / 2.2F, ARGB_WHITE, false, newLight, null);
			matrices.popPose();
		} else {
			final HorizontalAlignment horizontalAlignment1 = leftToRight ? HorizontalAlignment.LEFT : HorizontalAlignment.RIGHT;
			final HorizontalAlignment horizontalAlignment2;
			final float textX;
			if (vertical && hasLeft != hasRight) {
				horizontalAlignment2 = horizontalAlignment1;
				final float extraPadding = (arrowSize + arrowPadding) * (visibleArrow ? 2 : 1);
				textX = hasLeft ? left + extraPadding : right - extraPadding;
			} else {
				horizontalAlignment2 = HorizontalAlignment.CENTER;
				textX = centerX + (arrowSize + arrowPadding) * ((hasLeft ? 0.5F : 0) + (hasRight ? -0.5F : 0) + (leftToRight ? 0.5F : -0.5F));
			}

			final int textColor = vertical ? ARGB_WHITE : ARGB_BLACK;
			final float maxDestinationWidth = right - left - (arrowSize + arrowPadding) * (1 + (hasLeft && visibleArrow ? 1 : 0) + (hasRight && visibleArrow ? 1 : 0));
			IDrawing.drawStringWithFont(matrices, textRenderer, immediate, destinationString, horizontalAlignment1, VerticalAlignment.CENTER, horizontalAlignment2, textX, (top + bottom) / 2, maxDestinationWidth, arrowSize + arrowPadding, RenderRailwaySign.HEIGHT_TO_SCALE / arrowSize, textColor, false, newLight, ((x1, y1, x2, y2) -> {
				final VertexConsumer vertexConsumerArrow = vertexConsumers.getBuffer(getRenderLayer("mtr:textures/sign/arrow.png", newLight, true));
				if (hasLeft && visibleArrow) {
					IDrawing.drawTexture(matrices, vertexConsumerArrow, x1 - arrowSize * 2 - arrowPadding * 2, top, arrowSize, arrowSize, 0, 0, 1, 1, facing, textColor, newLight);
				}
				if (hasRight && visibleArrow) {
					IDrawing.drawTexture(matrices, vertexConsumerArrow, x2 + arrowPadding + (leftToRight ? 0 : arrowSize + arrowPadding), top, arrowSize, arrowSize, 1, 0, 0, 1, facing, textColor, newLight);
				}

				final VertexConsumer vertexConsumerCircle = vertexConsumers.getBuffer(getRenderLayer("mtr:textures/sign/circle.png", newLight, true));
				final float chunkHeight = arrowSize / routeCount;
				final float circleX = leftToRight ? x1 - arrowSize - arrowPadding : x2 + arrowPadding;
				for (int i = 0; i < routeCount; i++) {
					IDrawing.drawTexture(matrices, vertexConsumerCircle, circleX, top + i * chunkHeight, arrowSize, chunkHeight, 0, (float) i / routeCount, 1, (float) (i + 1) / routeCount, facing, ARGB_BLACK | routeData.get(i).routeColor, newLight);
				}

				matrices.pushPose();
				matrices.translate(0, 0, -SMALL_OFFSET);
				IDrawing.drawStringWithFont(matrices, textRenderer, immediate, platformNumber, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, (circleX + arrowSize / 2), top + arrowSize * PLATFORM_NUMBER_OFFSET_TOP, RenderRailwaySign.HEIGHT_TO_SCALE / arrowSize / 2.2F, ARGB_WHITE, false, newLight, null);
				matrices.popPose();
			}));
		}

		matrices.popPose();
	}

	private float getStationPosition(int stationIndex, int routeLength, float startScaled, float endScaled) {
		return (vertical ? routeLength - stationIndex - 1 : stationIndex) * (endScaled - startScaled) / (routeLength - 1) + startScaled;
	}

	private int convertLight(int light) {
		if (light == MAX_LIGHT_GLOWING && !glowing) {
			return light & 0xE000F0;
		} else {
			return light;
		}
	}

	private static RenderType getRenderLayer(String texture, int light, boolean isTranslucent) {
		return light == MAX_LIGHT_GLOWING ? MoreRenderLayers.getLight(new ResourceLocation(texture), isTranslucent) : MoreRenderLayers.getExterior(new ResourceLocation(texture));
	}
}
