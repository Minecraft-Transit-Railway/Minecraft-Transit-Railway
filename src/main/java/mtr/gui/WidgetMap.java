package mtr.gui;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import mtr.data.Platform;
import mtr.data.Route;
import mtr.data.Station;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WidgetMap extends WPlainPanel implements IGui {

	private double centerX, centerY;
	private final Set<Station> stations;
	private final Set<Platform> platforms;

	private double scale;
	private Station editingStation;
	private Route editingRoute;
	private Pair<Integer, Integer> drawStation1, drawStation2;
	private List<Station> moreStations;

	private final WButton buttonAddStation;
	private final WButton buttonAddRoute;
	private final WTextField textFieldName;
	private final WTextField textFieldColor;
	private final WButton buttonDoneEditingStation;
	private final WButton buttonDoneEditingRoute;
	private final WButton buttonCancel;

	private final int mapHeight, longWidth;
	private static final int BUTTON_WIDTH = 64;
	private static final int LEFT_MOUSE_BUTTON = 0;
	private static final int SCALE_UPPER_LIMIT = 16;
	private static final double SCALE_LOWER_LIMIT = 0.0078125;
	private static final int MAX_STATION_LENGTH = 128;
	private static final int MAX_COLOR_LENGTH = 6;

	public WidgetMap(int width, int height, double playerX, double playerZ, Set<Station> stations, Set<Platform> platforms) {
		this.width = width;
		this.height = height;
		centerX = playerX;
		centerY = playerZ;
		this.stations = stations;
		this.platforms = platforms;
		scale = 1;
		mapHeight = height - SQUARE_SIZE;
		longWidth = width - SQUARE_SIZE * 2 - BUTTON_WIDTH;

		buttonAddStation = new WButton(new TranslatableText("gui.mtr.add_station"));
		buttonAddStation.setOnClick(() -> startEditingStation(new Station()));

		buttonAddRoute = new WButton(new TranslatableText("gui.mtr.add_route"));
		buttonAddRoute.setOnClick(() -> startEditingRoute(new Route()));

		textFieldName = new WTextField();
		textFieldName.setMaxLength(MAX_STATION_LENGTH);
		textFieldName.setSuggestion(new TranslatableText("gui.mtr.name"));

		textFieldColor = new WTextField();
		textFieldColor.setMaxLength(MAX_COLOR_LENGTH);
		textFieldColor.setSuggestion(new TranslatableText("gui.mtr.color"));

		buttonDoneEditingStation = new WButton(new TranslatableText("gui.done"));

		buttonDoneEditingRoute = new WButton(new TranslatableText("gui.done"));

		buttonCancel = new WButton(new TranslatableText("gui.cancel"));
		buttonCancel.setOnClick(this::stopEditing);

		stopEditing();
	}

	@Override
	public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
		for (Station station : stations) {
			drawRectangle(matrices, worldPosToCoords(station.corner1), worldPosToCoords(station.corner2), ARGB_BLACK_TRANSLUCENT + station.color, scale > 1 ? station.name : "");
		}
		for (Platform platform : platforms) {
			BlockPos posStart = platform.getPos1();
			BlockPos posEnd = platform.getPos2().add(1, 0, 1);
			drawRectangle(worldPosToCoords(posStart), worldPosToCoords(posEnd), ARGB_WHITE_TRANSLUCENT);
		}

		if (editingStation != null) {
			if (drawStation1 != null && drawStation2 != null) {
				drawRectangle(worldPosToCoords(drawStation1), worldPosToCoords(drawStation2), ARGB_WHITE_TRANSLUCENT);
			}

			ScreenDrawing.drawStringWithShadow(matrices, new TranslatableText("gui.mtr.edit_station_1").asOrderedText(), HorizontalAlignment.LEFT, x + TEXT_PADDING, y + TEXT_PADDING, 0, ARGB_WHITE);
			ScreenDrawing.drawStringWithShadow(matrices, new TranslatableText("gui.mtr.edit_station_2").asOrderedText(), HorizontalAlignment.LEFT, x + TEXT_PADDING, y + TEXT_PADDING + LINE_HEIGHT, 0, ARGB_WHITE);
		}

		if (editingRoute != null) {
			ScreenDrawing.drawStringWithShadow(matrices, new TranslatableText("gui.mtr.edit_route").asOrderedText(), HorizontalAlignment.LEFT, x + TEXT_PADDING, y + TEXT_PADDING, 0, ARGB_WHITE);
			for (int i = 0; i < moreStations.size(); i++) {
				ScreenDrawing.drawStringWithShadow(matrices, IGui.formatStationName(moreStations.get(i).name), HorizontalAlignment.LEFT, x + TEXT_PADDING, y + TEXT_PADDING * 2 + LINE_HEIGHT * (i + 1), 0, ARGB_WHITE);
			}
		}

		Pair<Integer, Integer> mouseWorldPos = coordsToWorldPos(mouseX, mouseY);
		ScreenDrawing.drawStringWithShadow(matrices, String.format("(%d, %d)", mouseWorldPos.getLeft(), mouseWorldPos.getRight()), HorizontalAlignment.RIGHT, x + width - TEXT_PADDING, y + TEXT_PADDING, 0, ARGB_WHITE);

		super.paint(matrices, x, y, mouseX, mouseY);
	}

	@Override
	public void onMouseDrag(int x, int y, int button, double deltaX, double deltaY) {
		if (editingStation != null && button == LEFT_MOUSE_BUTTON) {
			drawStation2 = coordsToWorldPos((int) Math.round(x + deltaX), (int) Math.round(y + deltaY));
			if (drawStation1.getLeft().equals(drawStation2.getLeft())) {
				drawStation2 = new Pair<>(drawStation2.getLeft() + 1, drawStation2.getRight());
			}
			if (drawStation1.getRight().equals(drawStation2.getRight())) {
				drawStation2 = new Pair<>(drawStation2.getLeft(), drawStation2.getRight() + 1);
			}
			buttonDoneEditingStation.setEnabled(true);
		} else {
			centerX -= deltaX / scale;
			centerY -= deltaY / scale;
		}
	}

	@Override
	public WWidget onMouseDown(int x, int y, int button) {
		if (editingStation != null && button == LEFT_MOUSE_BUTTON) {
			drawStation1 = coordsToWorldPos(x, y);
			drawStation2 = new Pair<>(drawStation1.getLeft() + 1, drawStation1.getRight() + 1);
		} else if (editingRoute != null) {
			Pair<Integer, Integer> worldPos = coordsToWorldPos(x, y);
			stations.stream().filter(station -> station.inStation(worldPos.getLeft(), worldPos.getRight())).findAny().ifPresent(station -> moreStations.add(station));
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

	public void setOnDoneEditing(OnDoneEditingStation onDoneEditingStation, OnDoneEditingRoute onDoneEditingRoute) {
		buttonDoneEditingStation.setOnClick(() -> {
			String name = textFieldName.getText();
			int color = 0;
			try {
				color = Integer.parseInt(textFieldColor.getText(), 16);
			} catch (Exception ignored) {
			}
			onDoneEditingStation.onDoneEditingStation(editingStation, name.isEmpty() ? new TranslatableText("gui.mtr.untitled").getString() : name, drawStation1, drawStation2, color);
			stopEditing();
		});

		buttonDoneEditingRoute.setOnClick(() -> {
			String name = textFieldName.getText();
			int color = 0;
			try {
				color = Integer.parseInt(textFieldColor.getText(), 16);
			} catch (Exception ignored) {
			}
			onDoneEditingRoute.onDoneEditingRoute(editingRoute, name.isEmpty() ? new TranslatableText("gui.mtr.untitled").getString() : name, color, moreStations);
			stopEditing();
		});
	}

	public void find(Station station) {
		centerX = (station.corner1.getLeft() + station.corner2.getLeft()) / 2D;
		centerY = (station.corner1.getRight() + station.corner2.getRight()) / 2D;
		scale = Math.max(2, scale);
	}

	public void startEditingStation(Station editingStation) {
		this.editingStation = editingStation;
		editingRoute = null;
		drawStation1 = editingStation.corner1;
		drawStation2 = editingStation.corner2;
		buttonDoneEditingStation.setEnabled(drawStation1 != null && drawStation2 != null);
		textFieldName.setText(editingStation.name);
		textFieldColor.setText(parseColor(editingStation.color));

		children.clear();
		add(buttonDoneEditingStation, 0, mapHeight, longWidth, SQUARE_SIZE);
		addEditingWidgets();
	}

	public void startEditingRoute(Route editingRoute) {
		editingStation = null;
		this.editingRoute = editingRoute;
		moreStations = new ArrayList<>();
		buttonDoneEditingStation.setEnabled(true);
		textFieldName.setText(editingRoute.name);
		textFieldColor.setText(parseColor(editingRoute.color));

		children.clear();
		add(buttonDoneEditingRoute, 0, mapHeight, longWidth, SQUARE_SIZE);
		addEditingWidgets();
	}

	private void stopEditing() {
		editingStation = null;
		editingRoute = null;
		drawStation1 = drawStation2 = null;
		moreStations = new ArrayList<>();

		children.clear();
		final int buttonWidth = (longWidth + BUTTON_WIDTH) / 2;
		add(buttonAddStation, 0, mapHeight, buttonWidth, SQUARE_SIZE);
		add(buttonAddRoute, buttonWidth, mapHeight, buttonWidth, SQUARE_SIZE);
		addOtherWidgets();
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
		drawRectangle(null, topLeft, bottomRight, color, "");
	}

	private void drawRectangle(MatrixStack matrices, Pair<Double, Double> topLeft, Pair<Double, Double> bottomRight, int color, String text) {
		double x1 = Math.min(topLeft.getLeft(), bottomRight.getLeft());
		double y1 = Math.min(topLeft.getRight(), bottomRight.getRight());
		double x2 = Math.max(topLeft.getLeft(), bottomRight.getLeft());
		double y2 = Math.max(topLeft.getRight(), bottomRight.getRight());
		if (x1 < width && y1 < mapHeight && x2 >= 0 && y2 >= 0) {
			ScreenDrawing.coloredRect((int) Math.round(x + x1), (int) Math.round(y + y1), Math.max((int) Math.round(x2 - x1), 1), Math.max((int) Math.round(y2 - y1), 1), color);
			if (matrices != null && !text.isEmpty()) {
				IGui.drawStringWithFont(matrices, text, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, x + (int) Math.round((x1 + x2) / 2), y + (int) Math.round((y1 + y2) / 2), false);
			}
		}
	}

	private void addEditingWidgets() {
		add(textFieldName, TEXT_FIELD_PADDING / 2, mapHeight - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, longWidth - TEXT_FIELD_PADDING, SQUARE_SIZE);
		add(textFieldColor, longWidth + TEXT_FIELD_PADDING / 2, mapHeight - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, BUTTON_WIDTH - TEXT_FIELD_PADDING, SQUARE_SIZE);
		add(buttonCancel, longWidth, mapHeight, BUTTON_WIDTH, SQUARE_SIZE);
		addOtherWidgets();
	}

	private void addOtherWidgets() {
		WButton buttonZoomIn = new WButton(new LiteralText("+"));
		buttonZoomIn.setOnClick(() -> scale(1));
		add(buttonZoomIn, width - SQUARE_SIZE * 2, mapHeight, SQUARE_SIZE, SQUARE_SIZE);

		WButton buttonZoomOut = new WButton(new LiteralText("-"));
		buttonZoomOut.setOnClick(() -> scale(-1));
		add(buttonZoomOut, width - SQUARE_SIZE, mapHeight, SQUARE_SIZE, SQUARE_SIZE);

		if (getHost() != null) {
			validate(getHost());
		}
	}

	private String parseColor(int color) {
		return StringUtils.leftPad(Integer.toHexString(color == 0 ? (new Random()).nextInt(RGB_WHITE + 1) : color).toUpperCase(), 6, "0");
	}

	@FunctionalInterface
	public interface OnDoneEditingStation {
		void onDoneEditingStation(Station station, String name, Pair<Integer, Integer> corner1, Pair<Integer, Integer> corner2, int color);
	}

	@FunctionalInterface
	public interface OnDoneEditingRoute {
		void onDoneEditingRoute(Route route, String name, int color, List<Station> moreStations);
	}
}
