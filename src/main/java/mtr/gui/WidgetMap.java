package mtr.gui;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import mtr.data.Platform;
import mtr.data.Station;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Set;

public class WidgetMap extends WWidget {

	private double centerX, centerY;
	private final Set<Station> stations;
	private double scale;

	private static final int SCALE_UPPER_LIMIT = 8;
	private static final double SCALE_LOWER_LIMIT = 0.0078125;

	private static final int PLATFORM_RADIUS = 2;
	private static final int TEXT_PADDING = 6;
	private static final int ARGB_WHITE = 0xFFFFFFFF;
	private static final int ARGB_WHITE_TRANSLUCENT = 0x7FFFFFFF;

	public WidgetMap(int width, int height, double playerX, double playerZ, Set<Station> stations) {
		this.width = width;
		this.height = height;
		centerX = playerX;
		centerY = playerZ;
		this.stations = stations;
		scale = 1;
	}

	@Override
	public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
		for (Station station : stations) {
			for (Platform platform : station.platforms) {
				BlockPos posTopLeft = platform.pos.add(-PLATFORM_RADIUS, 0, -PLATFORM_RADIUS);
				BlockPos posBottomRight = platform.pos.add(PLATFORM_RADIUS, 0, PLATFORM_RADIUS);
				BlockPos posStart = platform.pos;
				BlockPos posEnd = platform.pos;

				if (platform.axis == Direction.Axis.X) {
					posBottomRight = posBottomRight.add(platform.length, 0, 1);
					posEnd = posEnd.add(platform.length, 0, 1);
				} else {
					posBottomRight = posBottomRight.add(1, 0, platform.length);
					posEnd = posEnd.add(1, 0, platform.length);
				}

				drawRectangle(worldPosToCoords(posTopLeft), worldPosToCoords(posBottomRight), ARGB_WHITE_TRANSLUCENT);
				drawRectangle(worldPosToCoords(posStart), worldPosToCoords(posEnd), ARGB_WHITE);
			}
		}

		Pair<Integer, Integer> mouseWorldPos = coordsToWorldPos(mouseX, mouseY);
		ScreenDrawing.drawStringWithShadow(matrices, String.format("(%d, %d)", mouseWorldPos.getLeft(), mouseWorldPos.getRight()), HorizontalAlignment.RIGHT, x + width - TEXT_PADDING, TEXT_PADDING, 0, ARGB_WHITE);
	}

	@Override
	public void onMouseDrag(int x, int y, int button, double deltaX, double deltaY) {
		centerX -= deltaX / scale;
		centerY -= deltaY / scale;
	}

	@Override
	public void onMouseScroll(int x, int y, double amount) {
		double oldScale = scale;
		if (oldScale > SCALE_LOWER_LIMIT && amount < 0) {
			centerX -= (x - width / 2D) / scale;
			centerY -= (y - height / 2D) / scale;
		}
		scale(amount);
		if (oldScale < SCALE_UPPER_LIMIT && amount > 0) {
			centerX += (x - width / 2D) / scale;
			centerY += (y - height / 2D) / scale;
		}
	}

	@Override
	public boolean canResize() {
		return true;
	}

	public void scale(double amount) {
		scale *= Math.pow(2, amount);
		if (scale > SCALE_UPPER_LIMIT) {
			scale = SCALE_UPPER_LIMIT;
		} else if (scale > 0.5) {
			scale = Math.round(scale);
		} else if (scale < SCALE_LOWER_LIMIT) {
			scale = SCALE_LOWER_LIMIT;
		}
	}

	private Pair<Double, Double> worldPosToCoords(BlockPos pos) {
		double left = (pos.getX() - centerX) * scale + width / 2D;
		double right = (pos.getZ() - centerY) * scale + height / 2D;
		return new Pair<>(left, right);
	}

	private Pair<Integer, Integer> coordsToWorldPos(int mouseX, int mouseY) {
		int left = (int) Math.floor((mouseX - width / 2D) / scale + centerX);
		int right = (int) Math.floor((mouseY - height / 2D) / scale + centerY);
		return new Pair<>(left, right);
	}

	private void drawRectangle(Pair<Double, Double> topLeft, Pair<Double, Double> bottomRight, int color) {
		double x1 = topLeft.getLeft();
		double y1 = topLeft.getRight();
		double x2 = bottomRight.getLeft();
		double y2 = bottomRight.getRight();
		if (x1 < width && y1 < height && x2 >= 0 && y2 >= 0) {
			ScreenDrawing.coloredRect((int) Math.round(x + x1), (int) Math.round(y + y1), Math.max((int) Math.round(x2 - x1), 1), Math.max((int) Math.round(y2 - y1), 1), color);
		}
	}
}
