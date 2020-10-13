package mtr.gui;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import mtr.data.Platform;
import mtr.data.Station;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

public class WidgetMap extends WPlainPanel implements IGui {

	private double centerX, centerY;
	private final Set<Station> stations;
	private final Set<Platform> platforms;
	private double scale;

	private static final int SCALE_UPPER_LIMIT = 8;
	private static final double SCALE_LOWER_LIMIT = 0.0078125;

	public WidgetMap(int width, int height, double playerX, double playerZ, Set<Station> stations, Set<Platform> platforms) {
		this.width = width;
		this.height = height;
		centerX = playerX;
		centerY = playerZ;
		this.stations = stations;
		this.platforms = platforms;
		scale = 1;

		WButton buttonZoomIn = new WButton(new LiteralText("+"));
		buttonZoomIn.setOnClick(() -> scale(1));
		add(buttonZoomIn, width - SQUARE_SIZE, height - SQUARE_SIZE * 2, SQUARE_SIZE, SQUARE_SIZE);

		WButton buttonZoomOut = new WButton(new LiteralText("-"));
		buttonZoomOut.setOnClick(() -> scale(-1));
		add(buttonZoomOut, width - SQUARE_SIZE, height - SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
	}

	@Override
	public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
		for (Platform platform : platforms) {
			BlockPos posStart = platform.getPos1();
			BlockPos posEnd = platform.getPos2().add(1, 0, 1);

			drawRectangle(worldPosToCoords(posStart), worldPosToCoords(posEnd), ARGB_WHITE_TRANSLUCENT);
		}

		Pair<Integer, Integer> mouseWorldPos = coordsToWorldPos(mouseX, mouseY);
		ScreenDrawing.drawStringWithShadow(matrices, String.format("(%d, %d)", mouseWorldPos.getLeft(), mouseWorldPos.getRight()), HorizontalAlignment.RIGHT, x + width - TEXT_PADDING, TEXT_PADDING, 0, ARGB_WHITE);

		super.paint(matrices, x, y, mouseX, mouseY);
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

	private void scale(double amount) {
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
