package mtr.render;

import mtr.gui.IGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.List;

public class RouteRenderer {

	private final float startScaled;
	private final float endScaled;
	private final float routePosition;
	private final int scale;

	private final int routeColor;
	private final int currentStationIndex;
	private final List<String> stationNames;
	private final boolean vertical;

	private static final int PASSED_STATION_COLOR = 0x999999;
	private static final int STATION_CIRCLE_SIZE = 16;
	private static final int STATION_TEXT_PADDING = 14;
	private static final int STATION_NAME_BACKGROUND_PADDING = 3;
	private static final float COLOR_LINE_HALF_HEIGHT = 4.5F;

	public RouteRenderer(float start, float end, float side1, float side2, int scale, int routeColor, int currentStationIndex, List<String> stationNames, boolean vertical) {
		startScaled = start * scale;
		endScaled = end * scale;
		routePosition = (side1 + side2) * scale / 2F;
		this.scale = scale;

		this.routeColor = routeColor;
		this.currentStationIndex = currentStationIndex;
		this.stationNames = stationNames;
		this.vertical = vertical;
	}

	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		matrices.push();
		matrices.scale(1F / scale, 1F / scale, 1F / scale);

		matrices.push();
		if (vertical) {
			IGui.drawRectangle(matrices.peek().getModel(), vertexConsumers, routePosition - COLOR_LINE_HALF_HEIGHT, endScaled, routePosition + COLOR_LINE_HALF_HEIGHT, getStationPosition(currentStationIndex), IGui.SMALL_OFFSET * scale, PASSED_STATION_COLOR, light);
			IGui.drawRectangle(matrices.peek().getModel(), vertexConsumers, routePosition - COLOR_LINE_HALF_HEIGHT, getStationPosition(currentStationIndex), routePosition + COLOR_LINE_HALF_HEIGHT, startScaled, IGui.SMALL_OFFSET * scale, routeColor, light);
		} else {
			IGui.drawRectangle(matrices.peek().getModel(), vertexConsumers, startScaled, routePosition - COLOR_LINE_HALF_HEIGHT, getStationPosition(currentStationIndex), routePosition + COLOR_LINE_HALF_HEIGHT, IGui.SMALL_OFFSET * scale, PASSED_STATION_COLOR, light);
			IGui.drawRectangle(matrices.peek().getModel(), vertexConsumers, getStationPosition(currentStationIndex), routePosition - COLOR_LINE_HALF_HEIGHT, endScaled, routePosition + COLOR_LINE_HALF_HEIGHT, IGui.SMALL_OFFSET * scale, routeColor, light);
		}
		matrices.pop();

		for (int i = 0; i < stationNames.size(); i++) {
			final float x = vertical ? routePosition : getStationPosition(i);
			final float y = vertical ? getStationPosition(i) : routePosition;
			final boolean onOrAfterStation = i >= currentStationIndex;
			final boolean onStation = i == currentStationIndex;

			matrices.push();
			final VertexConsumer vertexConsumerText = vertexConsumers.getBuffer(RenderLayer.getText(new Identifier(onOrAfterStation ? "mtr:textures/block/station_circle.png" : "mtr:textures/block/station_circle_passed.png")));
			IGui.drawTexture(matrices.peek().getModel(), vertexConsumerText, light, x - STATION_CIRCLE_SIZE / 2F, y - STATION_CIRCLE_SIZE / 2F, STATION_CIRCLE_SIZE, STATION_CIRCLE_SIZE);
			matrices.pop();

			final boolean bottomText = (i % 2) == 0;
			IGui.drawStringWithFont(matrices, textRenderer, IGui.textOrUntitled(stationNames.get(i)), vertical ? 2 : 1, vertical ? 1 : bottomText ? 0 : 2, x - (vertical ? STATION_TEXT_PADDING : 0), y + (vertical ? 0 : bottomText ? STATION_TEXT_PADDING : -STATION_TEXT_PADDING), onStation ? IGui.ARGB_WHITE : onOrAfterStation ? IGui.ARGB_BLACK : PASSED_STATION_COLOR, false, (x1, y1, x2, y2) -> {
				if (onStation) {
					matrices.push();
					IGui.drawRectangle(matrices.peek().getModel(), vertexConsumers, x1 - STATION_NAME_BACKGROUND_PADDING, y1 - STATION_NAME_BACKGROUND_PADDING, x2 + STATION_NAME_BACKGROUND_PADDING, y2 + STATION_NAME_BACKGROUND_PADDING, IGui.SMALL_OFFSET * scale, IGui.ARGB_BLACK, light);
					matrices.pop();
				}
			});
		}

		matrices.pop();
	}

	private float getStationPosition(int stationIndex) {
		final int routeLength = stationNames.size();
		return (vertical ? routeLength - stationIndex - 1 : stationIndex) * (endScaled - startScaled) / (routeLength - 1) + startScaled;
	}
}
