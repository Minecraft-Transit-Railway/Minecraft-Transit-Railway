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
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;

public class RouteRenderer {

	private final MatrixStack matrices;
	private final VertexConsumerProvider vertexConsumers;
	private final TextRenderer textRenderer;

	private final List<Triple<Integer, Integer, List<String>>> routeData;
	private final String platformNumber;
	private final boolean vertical;

	private static final int PASSED_STATION_COLOR = 0xFF999999;
	private static final int HEIGHT_TO_SCALE = 27;
	private static final int STATION_CIRCLE_SIZE = 16;
	private static final int STATION_TEXT_PADDING = 14;
	private static final int STATION_NAME_BACKGROUND_PADDING = 3;
	private static final float COLOR_LINE_HALF_HEIGHT = 4.5F;
	private static final float PLATFORM_NUMBER_OFFSET_TOP = 0.62F;
	private static final float PLATFORM_NUMBER_OFFSET_LEFT = 0.54F;

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
			final int routeColor = routeData.get(i).getLeft() + IGui.ARGB_BLACK;
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

		for (int i = 0; i < routeCount; i++) {
			final int routeColor = routeData.get(i).getLeft() + IGui.ARGB_BLACK;
			final int currentStationIndex = routeData.get(i).getMiddle();
			final List<String> stationNames = routeData.get(i).getRight();
			final int routeLength = stationNames.size();
			final float routePosition = routeHeight * (i + 0.5F) + side1 * scaleSmaller;

			matrices.push();
			matrices.scale(1F / scaleSmaller, 1F / scaleSmaller, 1F / scaleSmaller);

			matrices.push();
			if (vertical) {
				IGui.drawRectangle(matrices, vertexConsumers, routePosition - COLOR_LINE_HALF_HEIGHT, endScaled, routePosition + COLOR_LINE_HALF_HEIGHT, getStationPosition(currentStationIndex, routeLength, startScaled, endScaled), IGui.SMALL_OFFSET * scaleSmaller, PASSED_STATION_COLOR, light);
				IGui.drawRectangle(matrices, vertexConsumers, routePosition - COLOR_LINE_HALF_HEIGHT, getStationPosition(currentStationIndex, routeLength, startScaled, endScaled), routePosition + COLOR_LINE_HALF_HEIGHT, startScaled, IGui.SMALL_OFFSET * scaleSmaller, routeColor, light);
			} else {
				IGui.drawRectangle(matrices, vertexConsumers, startScaled, routePosition - COLOR_LINE_HALF_HEIGHT, getStationPosition(currentStationIndex, routeLength, startScaled, endScaled), routePosition + COLOR_LINE_HALF_HEIGHT, IGui.SMALL_OFFSET * scaleSmaller, PASSED_STATION_COLOR, light);
				IGui.drawRectangle(matrices, vertexConsumers, getStationPosition(currentStationIndex, routeLength, startScaled, endScaled), routePosition - COLOR_LINE_HALF_HEIGHT, endScaled, routePosition + COLOR_LINE_HALF_HEIGHT, IGui.SMALL_OFFSET * scaleSmaller, routeColor, light);
			}
			matrices.pop();

			for (int j = 0; j < routeLength; j++) {
				final float x = vertical ? routePosition : getStationPosition(j, routeLength, startScaled, endScaled);
				final float y = vertical ? getStationPosition(j, routeLength, startScaled, endScaled) : routePosition;
				final boolean onOrAfterStation = j >= currentStationIndex;
				final boolean onStation = j == currentStationIndex;

				matrices.push();
				IGui.drawTexture(matrices, vertexConsumers, onOrAfterStation ? "mtr:textures/block/station_circle.png" : "mtr:textures/block/station_circle_passed.png", x - STATION_CIRCLE_SIZE / 2F, y - STATION_CIRCLE_SIZE / 2F, STATION_CIRCLE_SIZE, STATION_CIRCLE_SIZE, light);
				matrices.pop();

				final boolean bottomText = (j % 2) == 0;
				IGui.drawStringWithFont(matrices, textRenderer, IGui.textOrUntitled(stationNames.get(j)), vertical ? 2 : 1, vertical ? 1 : bottomText ? 0 : 2, x - (vertical ? STATION_TEXT_PADDING : 0), y + (vertical ? 0 : bottomText ? STATION_TEXT_PADDING : -STATION_TEXT_PADDING), onStation ? IGui.ARGB_WHITE : onOrAfterStation ? IGui.ARGB_BLACK : PASSED_STATION_COLOR, 0, (x1, y1, x2, y2) -> {
					if (onStation) {
						matrices.push();
						IGui.drawRectangle(matrices, vertexConsumers, x1 - STATION_NAME_BACKGROUND_PADDING, y1 - STATION_NAME_BACKGROUND_PADDING, x2 + STATION_NAME_BACKGROUND_PADDING, y2 + STATION_NAME_BACKGROUND_PADDING, IGui.SMALL_OFFSET * scaleSmaller, IGui.ARGB_BLACK, light);
						matrices.pop();
					}
				});
			}

			matrices.pop();
		}
	}

	public void renderArrow(float left, float right, float top, float bottom, boolean rightToLeft, int light) {
		final int routeCount = getRouteCount();
		if (routeCount <= 0) {
			return;
		}

		final float arrowSize = bottom - top;
		final float arrowPadding = arrowSize / 4;
		final float scale = HEIGHT_TO_SCALE / arrowSize;
		final float scaleY = (top + bottom) * scale / 2;

		final List<String> destinations = new ArrayList<>();
		routeData.forEach(route -> {
			final String[] destinationSplit = route.getRight().get(route.getRight().size() - 1).split("\\|");
			for (int i = 0; i < destinationSplit.length; i++) {
				if (i < destinations.size()) {
					destinations.set(i, destinations.get(i) + new TranslatableText("gui.mtr.separator_" + i).getString() + destinationSplit[i]);
				} else {
					destinations.add(new TranslatableText("gui.mtr.to_" + i).getString() + destinationSplit[i]);
				}
			}
		});

		final String destinationString = destinations.stream().reduce((a, b) -> a + "|" + b).orElse("");
		final List<Float> textWidths = new ArrayList<>();
		IGui.drawStringWithFont(matrices, textRenderer, destinationString, 0, 1, 0, scaleY, IGui.ARGB_BLACK, -1, (x1, y1, x2, y2) -> textWidths.add(x2 - x1));
		final float textWidth = textWidths.get(0);
		final float scaleX;
		final float xOffset;
		final float maxDestinationWidth = right - left - arrowSize * 2 - arrowPadding * 2;
		if (textWidth > maxDestinationWidth * scale) {
			scaleX = textWidth / maxDestinationWidth;
			xOffset = 0;
		} else {
			scaleX = scale;
			xOffset = (maxDestinationWidth - textWidth / scale) / 2;
		}

		matrices.push();
		matrices.scale(1F / scaleX, 1F / scale, 1F / scale);
		final float textX = arrowSize * 2 + arrowPadding * 2 + xOffset;
		IGui.drawStringWithFont(matrices, textRenderer, destinationString, rightToLeft ? 0 : 2, 1, (rightToLeft ? left + textX : right - textX) * scaleX, scaleY, IGui.ARGB_BLACK, 0, null);
		matrices.pop();

		matrices.push();
		matrices.translate(0, 0, IGui.SMALL_OFFSET);
		IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/signs/arrow.png", rightToLeft ? left + xOffset : right - arrowSize - xOffset, top, arrowSize, arrowSize, rightToLeft ? 0 : 1, 0, rightToLeft ? 1 : 0, 1, IGui.ARGB_BLACK, light);

		final float chunkHeight = arrowSize / routeCount;
		final float circleX = arrowSize + arrowPadding + xOffset;
		for (int i = 0; i < routeCount; i++) {
			IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/signs/circle.png", rightToLeft ? left + circleX : right - arrowSize - circleX, top + i * chunkHeight, arrowSize, chunkHeight, 0, (float) i / routeCount, 1, (float) (i + 1) / routeCount, IGui.ARGB_BLACK + routeData.get(i).getLeft(), light);
		}
		matrices.pop();

		final float scalePlatformNumber = scale / 2.2F;
		matrices.push();
		matrices.scale(1F / scalePlatformNumber, 1F / scalePlatformNumber, 1F / scalePlatformNumber);
		IGui.drawStringWithFont(matrices, textRenderer, platformNumber, 1, 1, (rightToLeft ? left + circleX + arrowSize * PLATFORM_NUMBER_OFFSET_LEFT : right - circleX - arrowSize * (1 - PLATFORM_NUMBER_OFFSET_LEFT)) * scalePlatformNumber, (top + (bottom - top) * PLATFORM_NUMBER_OFFSET_TOP) * scalePlatformNumber, IGui.ARGB_WHITE, 0, null);
		matrices.pop();
	}

	private int getRouteCount() {
		return routeData == null ? -1 : routeData.size();
	}

	private float getStationPosition(int stationIndex, int routeLength, float startScaled, float endScaled) {
		return (vertical ? routeLength - stationIndex - 1 : stationIndex) * (endScaled - startScaled) / (routeLength - 1) + startScaled;
	}
}
