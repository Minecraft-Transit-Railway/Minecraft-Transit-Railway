package mtr.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import mtr.data.Platform;
import mtr.data.Station;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Set;

public class WidgetMap extends WPlainPanel implements IGui {

	private final GuiDescription guiDescription;
	private double centerX, centerY;
	private final Set<Station> stations;
	private final Set<Platform> platforms;
	private double scale;
	private boolean isDrawing;
	private Pair<Integer, Integer> drawStation1, drawStation2;

	private final WButton buttonAddStation;
	private final WTextField textField;
	private final WButton buttonDoneDrawing;
	private final WButton buttonCancel;

	private final int mapHeight;
	private static final int BUTTON_WIDTH = 64;
	private static final int LINE_HEIGHT = 10;
	private static final int LEFT_MOUSE_BUTTON = 0;
	private static final int SCALE_UPPER_LIMIT = 8;
	private static final double SCALE_LOWER_LIMIT = 0.0078125;

	public WidgetMap(GuiDescription guiDescription, int width, int height, double playerX, double playerZ, Set<Station> stations, Set<Platform> platforms) {
		this.guiDescription = guiDescription;
		this.width = width;
		this.height = height;
		centerX = playerX;
		centerY = playerZ;
		this.stations = stations;
		this.platforms = platforms;
		scale = 1;
		mapHeight = height - SQUARE_SIZE;

		buttonAddStation = new WButton(new TranslatableText("gui.mtr.add_station"));
		buttonAddStation.setOnClick(() -> setIsDrawing(true));

		textField = new WTextField();
		textField.setSuggestion(new TranslatableText("gui.mtr.station_name"));

		buttonDoneDrawing = new WButton(new TranslatableText("gui.done"));

		buttonCancel = new WButton(new TranslatableText("gui.cancel"));
		buttonCancel.setOnClick(() -> setIsDrawing(false));

		WButton buttonZoomIn = new WButton(new LiteralText("+"));
		buttonZoomIn.setOnClick(() -> scale(1));
		add(buttonZoomIn, width - SQUARE_SIZE * 2, mapHeight, SQUARE_SIZE, SQUARE_SIZE);

		WButton buttonZoomOut = new WButton(new LiteralText("-"));
		buttonZoomOut.setOnClick(() -> scale(-1));
		add(buttonZoomOut, width - SQUARE_SIZE, mapHeight, SQUARE_SIZE, SQUARE_SIZE);

		setIsDrawing(false);
	}

	@Override
	public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
		for (Station station : stations) {
			drawRectangle(worldPosToCoords(station.corner1), worldPosToCoords(station.corner2), ARGB_BLUE_TRANSLUCENT);
		}
		for (Platform platform : platforms) {
			BlockPos posStart = platform.getPos1();
			BlockPos posEnd = platform.getPos2().add(1, 0, 1);
			drawRectangle(worldPosToCoords(posStart), worldPosToCoords(posEnd), ARGB_WHITE_TRANSLUCENT);
		}

		if (isDrawing) {
			if (drawStation1 != null && drawStation2 != null) {
				drawRectangle(worldPosToCoords(drawStation1), worldPosToCoords(drawStation2), ARGB_ORANGE_TRANSLUCENT);
			}

			ScreenDrawing.drawStringWithShadow(matrices, new TranslatableText("gui.mtr.draw_station_1").asOrderedText(), HorizontalAlignment.LEFT, x + TEXT_PADDING, y + TEXT_PADDING, 0, ARGB_WHITE);
			ScreenDrawing.drawStringWithShadow(matrices, new TranslatableText("gui.mtr.draw_station_2").asOrderedText(), HorizontalAlignment.LEFT, x + TEXT_PADDING, y + TEXT_PADDING + LINE_HEIGHT, 0, ARGB_WHITE);
		}

		Pair<Integer, Integer> mouseWorldPos = coordsToWorldPos(mouseX, mouseY);
		ScreenDrawing.drawStringWithShadow(matrices, String.format("(%d, %d)", mouseWorldPos.getLeft(), mouseWorldPos.getRight()), HorizontalAlignment.RIGHT, x + width - TEXT_PADDING, y + TEXT_PADDING, 0, ARGB_WHITE);

		super.paint(matrices, x, y, mouseX, mouseY);
	}

	@Override
	public void onMouseDrag(int x, int y, int button, double deltaX, double deltaY) {
		if (isDrawing && button == LEFT_MOUSE_BUTTON) {
			drawStation2 = coordsToWorldPos((int) Math.round(x + deltaX), (int) Math.round(y + deltaY));
			if (drawStation1.getLeft().equals(drawStation2.getLeft())) {
				drawStation2 = new Pair<>(drawStation2.getLeft() + 1, drawStation2.getRight());
			}
			if (drawStation1.getRight().equals(drawStation2.getRight())) {
				drawStation2 = new Pair<>(drawStation2.getLeft(), drawStation2.getRight() + 1);
			}
			buttonDoneDrawing.setEnabled(true);
		} else {
			centerX -= deltaX / scale;
			centerY -= deltaY / scale;
		}
	}

	@Override
	public WWidget onMouseDown(int x, int y, int button) {
		if (isDrawing && button == LEFT_MOUSE_BUTTON) {
			drawStation1 = coordsToWorldPos(x, y);
			drawStation2 = new Pair<>(drawStation1.getLeft() + 1, drawStation1.getRight() + 1);
		}
		return super.onMouseDown(x, y, button);
	}

	@Override
	public void onMouseScroll(int x, int y, double amount) {
		double oldScale = scale;
		if (oldScale > SCALE_LOWER_LIMIT && amount < 0) {
			centerX -= (x - width / 2D) / scale;
			centerY -= (y - mapHeight / 2D) / scale;
		}
		scale(amount);
		if (oldScale < SCALE_UPPER_LIMIT && amount > 0) {
			centerX += (x - width / 2D) / scale;
			centerY += (y - mapHeight / 2D) / scale;
		}
	}

	@Override
	public boolean canResize() {
		return true;
	}

	public void setOnDoneDrawing(OnDoneDrawing onDoneDrawing) {
		buttonDoneDrawing.setOnClick(() -> {
			onDoneDrawing.onDoneDrawing(textField.getText(), drawStation1, drawStation2);
			setIsDrawing(false);
		});
	}

	public void find(Station station) {
		centerX = (station.corner1.getLeft() + station.corner2.getLeft()) / 2D;
		centerY = (station.corner1.getRight() + station.corner2.getRight()) / 2D;
	}

	private void setIsDrawing(boolean isDrawing) {
		this.isDrawing = isDrawing;
		drawStation1 = drawStation2 = null;
		buttonDoneDrawing.setEnabled(false);
		textField.setText("");

		if (isDrawing) {
			remove(buttonAddStation);
			add(textField, 0, mapHeight, width - SQUARE_SIZE * 2 - BUTTON_WIDTH * 2, SQUARE_SIZE);
			add(buttonDoneDrawing, width - SQUARE_SIZE * 2 - BUTTON_WIDTH * 2, mapHeight, BUTTON_WIDTH, SQUARE_SIZE);
			add(buttonCancel, width - SQUARE_SIZE * 2 - BUTTON_WIDTH, mapHeight, BUTTON_WIDTH, SQUARE_SIZE);
		} else {
			remove(textField);
			remove(buttonDoneDrawing);
			remove(buttonCancel);
			add(buttonAddStation, 0, mapHeight, width - SQUARE_SIZE * 2, SQUARE_SIZE);
		}
		validate(guiDescription);
	}

	private void scale(double amount) {
		scale *= Math.pow(2, amount);
		scale = MathHelper.clamp(scale, SCALE_LOWER_LIMIT, SCALE_UPPER_LIMIT);
	}

	private Pair<Double, Double> worldPosToCoords(BlockPos worldPos) {
		return worldPosToCoords(new Pair<>(worldPos.getX(), worldPos.getZ()));
	}

	private Pair<Double, Double> worldPosToCoords(Pair<Integer, Integer> worldPos) {
		double left = (worldPos.getLeft() - centerX) * scale + width / 2D;
		double right = (worldPos.getRight() - centerY) * scale + mapHeight / 2D;
		return new Pair<>(left, right);
	}

	private Pair<Integer, Integer> coordsToWorldPos(int mouseX, int mouseY) {
		int left = (int) Math.floor((mouseX - width / 2D) / scale + centerX);
		int right = (int) Math.floor((mouseY - mapHeight / 2D) / scale + centerY);
		return new Pair<>(left, right);
	}

	private void drawRectangle(Pair<Double, Double> topLeft, Pair<Double, Double> bottomRight, int color) {
		double x1 = Math.min(topLeft.getLeft(), bottomRight.getLeft());
		double y1 = Math.min(topLeft.getRight(), bottomRight.getRight());
		double x2 = Math.max(topLeft.getLeft(), bottomRight.getLeft());
		double y2 = Math.max(topLeft.getRight(), bottomRight.getRight());
		if (x1 < width && y1 < mapHeight && x2 >= 0 && y2 >= 0) {
			ScreenDrawing.coloredRect((int) Math.round(x + x1), (int) Math.round(y + y1), Math.max((int) Math.round(x2 - x1), 1), Math.max((int) Math.round(y2 - y1), 1), color);
		}
	}

	@FunctionalInterface
	public interface OnDoneDrawing {
		void onDoneDrawing(String name, Pair<Integer, Integer> corner1, Pair<Integer, Integer> corner2);
	}
}
