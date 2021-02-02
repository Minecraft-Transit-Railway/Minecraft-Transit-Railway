package mtr.render;

import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.stream.Collectors;

public class RouteRenderer implements IGui {

	private final MatrixStack matrices;
	private final VertexConsumerProvider vertexConsumers;
	private final TextRenderer textRenderer;

	private final List<ClientData.PlatformRouteDetails> routeData;
	private final String platformNumber;
	private final boolean vertical;

	private static final int PASSED_STATION_COLOR = 0xFF999999;
	private static final int HEIGHT_TO_SCALE = 27;
	private static final int STATION_CIRCLE_SIZE = 16;
	private static final int STATION_TEXT_PADDING = 14;
	private static final int STATION_NAME_BACKGROUND_PADDING = 3;
	private static final float COLOR_LINE_HALF_HEIGHT = 4.5F;
	private static final int INTERCHANGE_HALF_HEIGHT = 8;
	private static final int INTERCHANGE_LINE_SIZE = 10;
	private static final float PLATFORM_NUMBER_OFFSET_TOP = 0.63F;

	public RouteRenderer(MatrixStack matrices, VertexConsumerProvider vertexConsumers, BlockPos platformPos, boolean vertical) {
		this.matrices = matrices;
		this.vertexConsumers = vertexConsumers;
		routeData = ClientData.platformToRoute.get(platformPos);
		final Platform platform = RailwayData.getPlatformByPos(ClientData.platforms, platformPos);
		platformNumber = platform == null ? "1" : platform.name;
		this.vertical = vertical;
		textRenderer = MinecraftClient.getInstance().textRenderer;
	}

	public void renderColorStrip(float x1, float y1, float z1, float x2, float y2, float z2, int light) {
		final int routeCount = getRouteCount();
		if (routeCount <= 0) {
			return;
		}

		matrices.push();
		final float lineHeightSmall = (y2 - y1) / routeCount;
		for (int i = 0; i < routeCount; i++) {
			final int routeColor = routeData.get(i).routeColor + ARGB_BLACK;
			IGui.drawRectangle(matrices, vertexConsumers, x1, y1 + lineHeightSmall * i, z1, x2, y1 + lineHeightSmall * (i + 1), z2, routeColor, light);
		}
		matrices.pop();
	}

	public void renderLine(float start, float end, float side1, float side2, int scale, int light) {
		final int routeCount = getRouteCount();
		if (routeCount <= 0) {
			return;
		}

		final int scaleSmaller = scale * routeCount;
		final float routeHeight = (side2 - side1) * scaleSmaller / routeCount;
		final float startScaled = start * scaleSmaller;
		final float endScaled = end * scaleSmaller;
		final float smallOffset = IGui.SMALL_OFFSET * scaleSmaller;

		for (int i = 0; i < routeCount; i++) {
			final int routeColor = routeData.get(i).routeColor + ARGB_BLACK;
			final int currentStationIndex = routeData.get(i).currentStationIndex;
			final List<ClientData.PlatformRouteDetails.StationDetails> stationDetails = routeData.get(i).stationDetails;
			final int routeLength = stationDetails.size();
			final float routePosition = routeHeight * (i + 0.5F) + side1 * scaleSmaller;

			matrices.push();
			matrices.scale(1F / scaleSmaller, 1F / scaleSmaller, 1F / scaleSmaller);

			float thisStationPosition = getStationPosition(currentStationIndex, routeLength, startScaled, endScaled);
			if (vertical) {
				IGui.drawRectangle(matrices, vertexConsumers, routePosition - COLOR_LINE_HALF_HEIGHT, Math.min(thisStationPosition, endScaled), routePosition + COLOR_LINE_HALF_HEIGHT, Math.max(thisStationPosition, endScaled), smallOffset, PASSED_STATION_COLOR, light);
				IGui.drawRectangle(matrices, vertexConsumers, routePosition - COLOR_LINE_HALF_HEIGHT, Math.min(startScaled, thisStationPosition), routePosition + COLOR_LINE_HALF_HEIGHT, Math.max(startScaled, thisStationPosition), smallOffset, routeColor, light);
			} else {
				IGui.drawRectangle(matrices, vertexConsumers, Math.min(startScaled, thisStationPosition), routePosition - COLOR_LINE_HALF_HEIGHT, Math.max(startScaled, thisStationPosition), routePosition + COLOR_LINE_HALF_HEIGHT, smallOffset, PASSED_STATION_COLOR, light);
				IGui.drawRectangle(matrices, vertexConsumers, Math.min(thisStationPosition, endScaled), routePosition - COLOR_LINE_HALF_HEIGHT, Math.max(thisStationPosition, endScaled), routePosition + COLOR_LINE_HALF_HEIGHT, smallOffset, routeColor, light);
			}

			for (int j = 0; j < routeLength; j++) {
				final float x = vertical ? routePosition : getStationPosition(j, routeLength, startScaled, endScaled);
				final float y = vertical ? getStationPosition(j, routeLength, startScaled, endScaled) : routePosition;
				final boolean onOrAfterStation = j >= currentStationIndex;
				final boolean onStation = j == currentStationIndex;
				final boolean bottomText = (j % 2) == 0;
				final List<ClientData.ColorNamePair> interchangeRoutes = stationDetails.get(j).interchangeRoutes;
				final int interchangeCount = interchangeRoutes.size();

				final String stationCircleTexture = onOrAfterStation ? "mtr:textures/block/station_circle.png" : "mtr:textures/block/station_circle_passed.png";
				if (interchangeCount <= 1 || onStation) {
					IGui.drawTexture(matrices, vertexConsumers, stationCircleTexture, x - STATION_CIRCLE_SIZE / 2F, y - STATION_CIRCLE_SIZE / 2F, STATION_CIRCLE_SIZE, STATION_CIRCLE_SIZE, light);

					if (!onStation && interchangeCount == 1) {
						if (vertical) {
							IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/block/interchange.png", x + STATION_CIRCLE_SIZE / 2F, y - INTERCHANGE_LINE_SIZE / 2F, INTERCHANGE_LINE_SIZE, INTERCHANGE_LINE_SIZE, 0.625F, 0.3125F, 1, 0.6875F, onOrAfterStation ? interchangeRoutes.get(0).color + ARGB_BLACK : PASSED_STATION_COLOR, light);
							IGui.drawStringWithFont(matrices, textRenderer, interchangeRoutes.get(0).name, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, x + INTERCHANGE_LINE_SIZE * 2, y, 2, onOrAfterStation ? IGui.ARGB_BLACK : PASSED_STATION_COLOR, false, null);
						} else {
							final VerticalAlignment verticalAlignment = bottomText ? VerticalAlignment.BOTTOM : VerticalAlignment.TOP;
							final float yLine = y + (bottomText ? -STATION_CIRCLE_SIZE / 2F - INTERCHANGE_LINE_SIZE : STATION_CIRCLE_SIZE / 2F);
							final float yText = yLine + (bottomText ? 0 : INTERCHANGE_LINE_SIZE);
							IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/block/interchange.png", x - INTERCHANGE_LINE_SIZE / 2F, yLine, INTERCHANGE_LINE_SIZE, INTERCHANGE_LINE_SIZE, 0.3125F, bottomText ? 0 : 0.625F, 0.6875F, bottomText ? 0.375F : 1, onOrAfterStation ? interchangeRoutes.get(0).color + ARGB_BLACK : PASSED_STATION_COLOR, light);
							IGui.drawStringWithFont(matrices, textRenderer, interchangeRoutes.get(0).name, HorizontalAlignment.CENTER, verticalAlignment, x, yText, 2, onOrAfterStation ? IGui.ARGB_BLACK : PASSED_STATION_COLOR, false, null);
						}
					}
				} else {
					final int totalHeight = (interchangeCount - (vertical ? 1 : 0)) * INTERCHANGE_HALF_HEIGHT * 2;
					final float y2 = vertical ? y - totalHeight / 2F : bottomText ? y - totalHeight : y;
					final float y1 = y2 - STATION_CIRCLE_SIZE / 2F;
					final float y3 = y2 + totalHeight;
					IGui.drawTexture(matrices, vertexConsumers, stationCircleTexture, x - STATION_CIRCLE_SIZE / 2F, y1, STATION_CIRCLE_SIZE, STATION_CIRCLE_SIZE / 2F, 0, 0, 1, 0.5F, -1, light);
					IGui.drawTexture(matrices, vertexConsumers, stationCircleTexture, x - STATION_CIRCLE_SIZE / 2F, y2, STATION_CIRCLE_SIZE, totalHeight, 0, 0.49F, 1, 0.51F, -1, light);
					IGui.drawTexture(matrices, vertexConsumers, stationCircleTexture, x - STATION_CIRCLE_SIZE / 2F, y3, STATION_CIRCLE_SIZE, STATION_CIRCLE_SIZE / 2F, 0, 0.5F, 1, 1, -1, light);

					for (int k = 0; k < interchangeCount; k++) {
						final float yLine = y2 + (k + (vertical ? 0 : bottomText ? 0 : 1)) * INTERCHANGE_HALF_HEIGHT * 2;
						IGui.drawStringWithFont(matrices, textRenderer, interchangeRoutes.get(k).name, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, x + INTERCHANGE_LINE_SIZE * 2, yLine, 2, onOrAfterStation ? IGui.ARGB_BLACK : PASSED_STATION_COLOR, false, null);
						IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/block/interchange.png", x + STATION_CIRCLE_SIZE / 2F, yLine - INTERCHANGE_LINE_SIZE / 2F, INTERCHANGE_LINE_SIZE, INTERCHANGE_LINE_SIZE, 0.625F, 0.3125F, 1, 0.6875F, onOrAfterStation ? interchangeRoutes.get(k).color + ARGB_BLACK : PASSED_STATION_COLOR, light);
					}
				}

				final HorizontalAlignment horizontalAlignment = vertical ? HorizontalAlignment.RIGHT : HorizontalAlignment.CENTER;
				final VerticalAlignment verticalAlignment = vertical ? VerticalAlignment.CENTER : bottomText ? VerticalAlignment.TOP : VerticalAlignment.BOTTOM;
				IGui.drawStringWithFont(matrices, textRenderer, IGui.textOrUntitled(stationDetails.get(j).stationName), horizontalAlignment, verticalAlignment, x - (vertical ? STATION_TEXT_PADDING : 0), y + (vertical ? 0 : bottomText ? STATION_TEXT_PADDING : -STATION_TEXT_PADDING), 1, onStation ? IGui.ARGB_WHITE : onOrAfterStation ? IGui.ARGB_BLACK : PASSED_STATION_COLOR, false, (x1, y1, x2, y2) -> {
					if (onStation) {
						matrices.push();
						IGui.drawRectangle(matrices, vertexConsumers, x1 - STATION_NAME_BACKGROUND_PADDING, y1 - STATION_NAME_BACKGROUND_PADDING, x2 + STATION_NAME_BACKGROUND_PADDING, y2 + STATION_NAME_BACKGROUND_PADDING, smallOffset, IGui.ARGB_BLACK, light);
						matrices.pop();
					}
				});
			}

			matrices.pop();
		}
	}

	public void renderArrow(float left, float right, float top, float bottom, boolean hasRight, boolean hasLeft, int light) {
		final int routeCount = getRouteCount();
		if (routeCount <= 0) {
			return;
		}

		final float arrowSize = bottom - top;
		final float arrowPadding = arrowSize / 4;

		String destinationString = IGui.mergeStations(routeData.stream().filter(route -> route.currentStationIndex < route.stationDetails.size() - 1).map(route -> route.stationDetails.get(route.stationDetails.size() - 1).stationName).collect(Collectors.toList()));

		if (!destinationString.isEmpty()) {
			destinationString = IGui.addToStationName(destinationString, new TranslatableText("gui.mtr.to_cjk").getString(), new TranslatableText("gui.mtr.to").getString(), "", "");
		}

		final boolean leftToRight = hasLeft || !hasRight;
		final float centerX = (left + right) / 2;

		matrices.push();

		if (destinationString.isEmpty()) {
			final float chunkHeight = arrowSize / routeCount;
			for (int i = 0; i < routeCount; i++) {
				IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/signs/circle.png", centerX - arrowSize / 2, top + i * chunkHeight, arrowSize, chunkHeight, 0, (float) i / routeCount, 1, (float) (i + 1) / routeCount, IGui.ARGB_BLACK + routeData.get(i).routeColor, light);
			}

			matrices.push();
			matrices.translate(0, 0, -IGui.SMALL_OFFSET);
			IGui.drawStringWithFont(matrices, textRenderer, platformNumber, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, centerX, top + arrowSize * PLATFORM_NUMBER_OFFSET_TOP, HEIGHT_TO_SCALE / arrowSize / 2.2F, IGui.ARGB_WHITE, false, null);
			matrices.pop();
		} else {
			final HorizontalAlignment horizontalAlignment = leftToRight ? HorizontalAlignment.LEFT : HorizontalAlignment.RIGHT;
			final float textX = centerX + (arrowSize + arrowPadding) * ((hasLeft ? 0.5F : 0) + (hasRight ? -0.5F : 0) + (leftToRight ? 0.5F : -0.5F));
			final float maxDestinationWidth = right - left - (arrowSize + arrowPadding) * (1 + (hasLeft ? 1 : 0) + (hasRight ? 1 : 0));
			IGui.drawStringWithFont(matrices, textRenderer, destinationString, horizontalAlignment, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, textX, (top + bottom) / 2, maxDestinationWidth, arrowSize + arrowPadding, HEIGHT_TO_SCALE / arrowSize, IGui.ARGB_BLACK, false, ((x1, y1, x2, y2) -> {
				if (hasLeft) {
					IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/signs/arrow.png", x1 - arrowSize * 2 - arrowPadding * 2, top, arrowSize, arrowSize, 0, 0, 1, 1, IGui.ARGB_BLACK, light);
				}
				if (hasRight) {
					IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/signs/arrow.png", x2 + arrowPadding + (leftToRight ? 0 : arrowSize + arrowPadding), top, arrowSize, arrowSize, 1, 0, 0, 1, IGui.ARGB_BLACK, light);
				}

				final float chunkHeight = arrowSize / routeCount;
				final float circleX = leftToRight ? x1 - arrowSize - arrowPadding : x2 + arrowPadding;
				for (int i = 0; i < routeCount; i++) {
					IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/signs/circle.png", circleX, top + i * chunkHeight, arrowSize, chunkHeight, 0, (float) i / routeCount, 1, (float) (i + 1) / routeCount, IGui.ARGB_BLACK + routeData.get(i).routeColor, light);
				}

				matrices.push();
				matrices.translate(0, 0, -IGui.SMALL_OFFSET);
				IGui.drawStringWithFont(matrices, textRenderer, platformNumber, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, (circleX + arrowSize / 2), top + arrowSize * PLATFORM_NUMBER_OFFSET_TOP, HEIGHT_TO_SCALE / arrowSize / 2.2F, IGui.ARGB_WHITE, false, null);
				matrices.pop();
			}));
		}

		matrices.pop();
	}

	private int getRouteCount() {
		return routeData == null ? -1 : routeData.size();
	}

	private float getStationPosition(int stationIndex, int routeLength, float startScaled, float endScaled) {
		return (vertical ? routeLength - stationIndex - 1 : stationIndex) * (endScaled - startScaled) / (routeLength - 1) + startScaled;
	}
}
