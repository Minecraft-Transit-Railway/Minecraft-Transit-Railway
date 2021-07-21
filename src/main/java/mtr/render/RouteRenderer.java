package mtr.render;

import mtr.data.IGui;
import mtr.data.Platform;
import mtr.gui.ClientData;
import mtr.gui.IDrawing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RouteRenderer implements IGui {

	private final MatrixStack matrices;
	private final VertexConsumerProvider vertexConsumers;
	private final VertexConsumerProvider.Immediate immediate;
	private final TextRenderer textRenderer;

	private final List<ClientData.PlatformRouteDetails> routeData;
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

	public RouteRenderer(MatrixStack matrices, VertexConsumerProvider vertexConsumers, VertexConsumerProvider.Immediate immediate, Platform platform, boolean vertical, boolean glowing) {
		this.matrices = matrices;
		this.vertexConsumers = vertexConsumers;
		this.immediate = immediate;

		final List<ClientData.PlatformRouteDetails> platformRouteDetails = ClientData.platformToRoute.get(platform);
		routeData = platform == null || platformRouteDetails == null ? new ArrayList<>() : platformRouteDetails;

		platformNumber = platform == null ? "1" : platform.name;
		this.vertical = vertical;
		this.glowing = glowing;
		textRenderer = MinecraftClient.getInstance().textRenderer;
	}

	public void renderColorStrip(float x1, float y1, float z1, float x2, float y2, float z2, Direction facing, int light) {
		final int routeCount = routeData.size();
		if (routeCount <= 0) {
			return;
		}

		matrices.push();
		final int newLight = convertLight(light);
		final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(getRenderLayer("mtr:textures/block/white.png", newLight));
		final float lineHeightSmall = (y2 - y1) / routeCount;
		for (int i = 0; i < routeCount; i++) {
			final int routeColor = routeData.get(i).routeColor + ARGB_BLACK;
			IDrawing.drawTexture(matrices, vertexConsumer, x1, y1 + lineHeightSmall * i, z1, x2, y1 + lineHeightSmall * (i + 1), z2, facing, routeColor, newLight);
		}
		matrices.pop();
	}

	public void renderLine(float start, float end, float side1, float side2, int scale, Direction facing, int light) {
		final List<ClientData.PlatformRouteDetails> filteredRouteData = routeData.stream().filter(platformRouteDetails -> platformRouteDetails.currentStationIndex < platformRouteDetails.stationDetails.size() - 1).collect(Collectors.toList());
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

		for (int i = 0; i < routeCount; i++) {
			final int routeColor = filteredRouteData.get(i).routeColor + ARGB_BLACK;
			final int currentStationIndex = filteredRouteData.get(i).currentStationIndex;
			final List<ClientData.PlatformRouteDetails.StationDetails> stationDetails = filteredRouteData.get(i).stationDetails;
			final int routeLength = stationDetails.size();
			final float routePosition = routeHeight * (i + 0.5F) + side1 * scaleSmaller;

			matrices.push();
			matrices.scale(1F / scaleSmaller, 1F / scaleSmaller, 1F / scaleSmaller);

			float thisStationPosition = getStationPosition(currentStationIndex, routeLength, startScaled, endScaled);
			final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(getRenderLayer("mtr:textures/block/white.png", newLight));
			if (vertical) {
				IDrawing.drawTexture(matrices, vertexConsumer, routePosition - COLOR_LINE_HALF_HEIGHT, Math.min(thisStationPosition, endScaled), smallOffset, routePosition + COLOR_LINE_HALF_HEIGHT, Math.max(thisStationPosition, endScaled), smallOffset, facing, PASSED_STATION_COLOR, newLight);
				IDrawing.drawTexture(matrices, vertexConsumer, routePosition - COLOR_LINE_HALF_HEIGHT, Math.min(startScaled, thisStationPosition), smallOffset, routePosition + COLOR_LINE_HALF_HEIGHT, Math.max(startScaled, thisStationPosition), smallOffset, facing, routeColor, newLight);
				IDrawing.drawStringWithFont(matrices, textRenderer, immediate, filteredRouteData.get(i).routeName, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, routePosition - routeHeight / 2, startScaled, 0.125F * scale, -1, 1, ARGB_BLACK, false, newLight, null);
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
				final List<ClientData.ColorNamePair> interchangeRoutes = stationDetails.get(j).interchangeRoutes;
				final int interchangeCount = interchangeRoutes.size();

				final VertexConsumer vertexConsumerStationCircle = vertexConsumers.getBuffer(getRenderLayer(onOrAfterStation ? "mtr:textures/block/station_circle.png" : "mtr:textures/block/station_circle_passed.png", newLight));
				if (interchangeCount <= 1 || onStation) {
					IDrawing.drawTexture(matrices, vertexConsumerStationCircle, x - STATION_CIRCLE_SIZE / 2F, y - STATION_CIRCLE_SIZE / 2F, STATION_CIRCLE_SIZE, STATION_CIRCLE_SIZE, facing, newLight);

					if (!onStation && interchangeCount == 1) {
						final VertexConsumer vertexConsumerInterchange = vertexConsumers.getBuffer(getRenderLayer("mtr:textures/block/interchange.png", newLight));
						if (vertical) {
							IDrawing.drawTexture(matrices, vertexConsumerInterchange, x + STATION_CIRCLE_SIZE / 2F, y - INTERCHANGE_LINE_SIZE / 2F, INTERCHANGE_LINE_SIZE, INTERCHANGE_LINE_SIZE, 0.625F, 0.3125F, 1, 0.6875F, facing, onOrAfterStation ? interchangeRoutes.get(0).color + ARGB_BLACK : PASSED_STATION_COLOR, newLight);
							IDrawing.drawStringWithFont(matrices, textRenderer, immediate, interchangeRoutes.get(0).name, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, x + INTERCHANGE_LINE_SIZE * 2, y, 2, onOrAfterStation ? ARGB_BLACK : PASSED_STATION_COLOR, false, newLight, null);
						} else {
							final VerticalAlignment verticalAlignment = bottomText ? VerticalAlignment.BOTTOM : VerticalAlignment.TOP;
							final float yLine = y + (bottomText ? -STATION_CIRCLE_SIZE / 2F - INTERCHANGE_LINE_SIZE : STATION_CIRCLE_SIZE / 2F);
							final float yText = yLine + (bottomText ? 0 : INTERCHANGE_LINE_SIZE);
							IDrawing.drawTexture(matrices, vertexConsumerInterchange, x - INTERCHANGE_LINE_SIZE / 2F, yLine, INTERCHANGE_LINE_SIZE, INTERCHANGE_LINE_SIZE, 0.3125F, bottomText ? 0 : 0.625F, 0.6875F, bottomText ? 0.375F : 1, facing, onOrAfterStation ? interchangeRoutes.get(0).color + ARGB_BLACK : PASSED_STATION_COLOR, newLight);
							IDrawing.drawStringWithFont(matrices, textRenderer, immediate, interchangeRoutes.get(0).name, HorizontalAlignment.CENTER, verticalAlignment, x, yText, 2, onOrAfterStation ? ARGB_BLACK : PASSED_STATION_COLOR, false, newLight, null);
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

					final VertexConsumer vertexConsumerInterchange = vertexConsumers.getBuffer(getRenderLayer("mtr:textures/block/interchange.png", newLight));
					for (int k = 0; k < interchangeCount; k++) {
						final float yLine = y2 + (k + (vertical ? 0 : bottomText ? 0 : 1)) * INTERCHANGE_HALF_HEIGHT * 2;
						IDrawing.drawStringWithFont(matrices, textRenderer, immediate, interchangeRoutes.get(k).name, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, x + INTERCHANGE_LINE_SIZE * 2, yLine, 2, onOrAfterStation ? ARGB_BLACK : PASSED_STATION_COLOR, false, newLight, null);
						IDrawing.drawTexture(matrices, vertexConsumerInterchange, x + STATION_CIRCLE_SIZE / 2F, yLine - INTERCHANGE_LINE_SIZE / 2F, INTERCHANGE_LINE_SIZE, INTERCHANGE_LINE_SIZE, 0.625F, 0.3125F, 1, 0.6875F, facing, onOrAfterStation ? interchangeRoutes.get(k).color + ARGB_BLACK : PASSED_STATION_COLOR, newLight);
					}
				}

				final HorizontalAlignment horizontalAlignment = vertical ? HorizontalAlignment.RIGHT : HorizontalAlignment.CENTER;
				final VerticalAlignment verticalAlignment = vertical ? VerticalAlignment.CENTER : bottomText ? VerticalAlignment.TOP : VerticalAlignment.BOTTOM;
				IDrawing.drawStringWithFont(matrices, textRenderer, immediate, IGui.textOrUntitled(stationDetails.get(j).stationName), horizontalAlignment, verticalAlignment, x - (vertical ? STATION_TEXT_PADDING : 0), y + (vertical ? 0 : bottomText ? STATION_TEXT_PADDING : -STATION_TEXT_PADDING), 1, onStation ? ARGB_WHITE : onOrAfterStation ? ARGB_BLACK : PASSED_STATION_COLOR, false, newLight, (x1, y1, x2, y2) -> {
					if (onStation) {
						matrices.push();
						IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(getRenderLayer("mtr:textures/block/white.png", newLight)), x1 - STATION_NAME_BACKGROUND_PADDING, y1 - STATION_NAME_BACKGROUND_PADDING, smallOffset, x2 + STATION_NAME_BACKGROUND_PADDING, y2 + STATION_NAME_BACKGROUND_PADDING, smallOffset, facing, ARGB_BLACK, newLight);
						matrices.pop();
					}
				});
			}

			matrices.pop();
		}
	}

	public void renderArrow(float left, float right, float top, float bottom, boolean hasRight, boolean hasLeft, Direction facing, int light) {
		renderArrow(left, right, top, bottom, hasRight, hasLeft, facing, light, true);
	}

	public void renderArrow(float left, float right, float top, float bottom, boolean hasRight, boolean hasLeft, Direction facing, int light, boolean visibleArrow) {
		final int routeCount = routeData.size();
		if (routeCount <= 0) {
			return;
		}

		final float arrowSize = bottom - top;
		final float arrowPadding = arrowSize / 4;

		String destinationString = IGui.mergeStations(routeData.stream().filter(route -> route.currentStationIndex < route.stationDetails.size() - 1).map(route -> route.stationDetails.get(route.stationDetails.size() - 1).stationName).collect(Collectors.toList()));

		if (!destinationString.isEmpty() && visibleArrow) {
			destinationString = IGui.addToStationName(destinationString, new TranslatableText("gui.mtr.to_cjk").getString(), new TranslatableText("gui.mtr.to").getString(), "", "");
		}

		final boolean leftToRight = hasLeft || !hasRight;
		final float centerX = (left + right) / 2;

		matrices.push();
		final int newLight = convertLight(light);

		if (destinationString.isEmpty() && visibleArrow) {
			final float chunkHeight = arrowSize / routeCount;
			final VertexConsumer vertexConsumerCircle = vertexConsumers.getBuffer(getRenderLayer("mtr:textures/sign/circle.png", newLight));
			for (int i = 0; i < routeCount; i++) {
				IDrawing.drawTexture(matrices, vertexConsumerCircle, centerX - arrowSize / 2, top + i * chunkHeight, arrowSize, chunkHeight, 0, (float) i / routeCount, 1, (float) (i + 1) / routeCount, facing, ARGB_BLACK + routeData.get(i).routeColor, newLight);
			}

			matrices.push();
			matrices.translate(0, 0, -SMALL_OFFSET);
			IDrawing.drawStringWithFont(matrices, textRenderer, immediate, platformNumber, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, centerX, top + arrowSize * PLATFORM_NUMBER_OFFSET_TOP, RenderRailwaySign.HEIGHT_TO_SCALE / arrowSize / 2.2F, ARGB_WHITE, false, newLight, null);
			matrices.pop();
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
				final VertexConsumer vertexConsumerArrow = vertexConsumers.getBuffer(getRenderLayer("mtr:textures/sign/arrow.png", newLight));
				if (hasLeft && visibleArrow) {
					IDrawing.drawTexture(matrices, vertexConsumerArrow, x1 - arrowSize * 2 - arrowPadding * 2, top, arrowSize, arrowSize, 0, 0, 1, 1, facing, textColor, newLight);
				}
				if (hasRight && visibleArrow) {
					IDrawing.drawTexture(matrices, vertexConsumerArrow, x2 + arrowPadding + (leftToRight ? 0 : arrowSize + arrowPadding), top, arrowSize, arrowSize, 1, 0, 0, 1, facing, textColor, newLight);
				}

				final VertexConsumer vertexConsumerCircle = vertexConsumers.getBuffer(getRenderLayer("mtr:textures/sign/circle.png", newLight));
				final float chunkHeight = arrowSize / routeCount;
				final float circleX = leftToRight ? x1 - arrowSize - arrowPadding : x2 + arrowPadding;
				for (int i = 0; i < routeCount; i++) {
					IDrawing.drawTexture(matrices, vertexConsumerCircle, circleX, top + i * chunkHeight, arrowSize, chunkHeight, 0, (float) i / routeCount, 1, (float) (i + 1) / routeCount, facing, ARGB_BLACK + routeData.get(i).routeColor, newLight);
				}

				matrices.push();
				matrices.translate(0, 0, -SMALL_OFFSET);
				IDrawing.drawStringWithFont(matrices, textRenderer, immediate, platformNumber, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, (circleX + arrowSize / 2), top + arrowSize * PLATFORM_NUMBER_OFFSET_TOP, RenderRailwaySign.HEIGHT_TO_SCALE / arrowSize / 2.2F, ARGB_WHITE, false, newLight, null);
				matrices.pop();
			}));
		}

		matrices.pop();
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

	private static RenderLayer getRenderLayer(String texture, int light) {
		return light == MAX_LIGHT_GLOWING ? MoreRenderLayers.getLight(new Identifier(texture)) : MoreRenderLayers.getExterior(new Identifier(texture));
	}
}
