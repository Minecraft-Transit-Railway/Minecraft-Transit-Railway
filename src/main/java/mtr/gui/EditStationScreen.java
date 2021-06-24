package mtr.gui;

import mtr.data.DataConverter;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.data.Station;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditStationScreen extends Screen implements IGui, IPacket {

	String editingExit;
	int editingDestinationIndex;

	private final Station station;
	private final DashboardScreen dashboardScreen;

	private final Text stationNameText = new TranslatableText("gui.mtr.station_name");
	private final Text stationColorText = new TranslatableText("gui.mtr.station_color");
	private final Text stationZoneText = new TranslatableText("gui.mtr.zone");
	private final Text exitParentsText = new TranslatableText("gui.mtr.exit_parents");
	private final Text exitDestinationsText = new TranslatableText("gui.mtr.exit_destinations");

	private final TextFieldWidget textFieldName;
	private final TextFieldWidget textFieldColor;
	private final TextFieldWidget textFieldZone;
	private final TextFieldWidget textFieldExitParentLetter;
	private final TextFieldWidget textFieldExitParentNumber;
	private final TextFieldWidget textFieldExitDestination;

	private final ButtonWidget buttonAddExitParent;
	private final ButtonWidget buttonDoneExitParent;
	private final ButtonWidget buttonAddExitDestination;
	private final ButtonWidget buttonDoneExitDestination;

	private final DashboardList exitParentList;
	private final DashboardList exitDestinationList;

	private static final int EXIT_PANELS_START = SQUARE_SIZE * 3 + TEXT_FIELD_PADDING + TEXT_PADDING;

	public EditStationScreen(Station station, DashboardScreen dashboardScreen) {
		super(new LiteralText(""));
		this.station = station;
		this.dashboardScreen = dashboardScreen;

		textRenderer = MinecraftClient.getInstance().textRenderer;
		textFieldName = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		textFieldColor = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		textFieldZone = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		textFieldExitParentLetter = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		textFieldExitParentNumber = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		textFieldExitDestination = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));

		buttonAddExitParent = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_exit"), button -> changeEditingExit("", -1));
		buttonDoneExitParent = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.done"), button -> onDoneExitParent());
		buttonAddExitDestination = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_exit_destination"), button -> changeEditingExit(editingExit, station.exits.containsKey(editingExit) ? station.exits.get(editingExit).size() : -1));
		buttonDoneExitDestination = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.done"), button -> onDoneExitDestination());

		exitParentList = new DashboardList(this::addButton, this::addChild, null, null, this::onEditExitParent, null, null, this::onDeleteExitParent, null);
		exitDestinationList = new DashboardList(this::addButton, this::addChild, null, null, this::onEditExitDestination, this::onSortExitDestination, null, this::onDeleteExitDestination, this::getExitDestinationList);
	}

	@Override
	protected void init() {
		super.init();
		final int yStart = SQUARE_SIZE + TEXT_FIELD_PADDING / 2;
		IDrawing.setPositionAndWidth(textFieldName, TEXT_FIELD_PADDING / 2, yStart, width - width / 2 - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldColor, width / 2 + TEXT_FIELD_PADDING / 2, yStart, width / 4 - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldZone, width / 4 * 3 + TEXT_FIELD_PADDING / 2, yStart, width / 4 - TEXT_FIELD_PADDING);

		final int yExitText = height - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING / 2;
		IDrawing.setPositionAndWidth(textFieldExitParentLetter, TEXT_FIELD_PADDING / 2, yExitText, width / 4 - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldExitParentNumber, TEXT_FIELD_PADDING / 2 + width / 4, yExitText, width / 4 - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldExitDestination, width / 2 + TEXT_FIELD_PADDING / 2, yExitText, width / 2 - TEXT_FIELD_PADDING);

		IDrawing.setPositionAndWidth(buttonAddExitParent, 0, height - SQUARE_SIZE, width / 2);
		IDrawing.setPositionAndWidth(buttonDoneExitParent, 0, height - SQUARE_SIZE, width / 2);
		IDrawing.setPositionAndWidth(buttonAddExitDestination, width / 2, height - SQUARE_SIZE, width / 2);
		IDrawing.setPositionAndWidth(buttonDoneExitDestination, width / 2, height - SQUARE_SIZE, width / 2);

		textFieldName.setMaxLength(DashboardScreen.MAX_NAME_LENGTH);
		textFieldName.setText(station.name);
		textFieldColor.setMaxLength(DashboardScreen.MAX_COLOR_ZONE_LENGTH);
		textFieldColor.setText(DashboardScreen.colorIntToString(station.color));
		textFieldColor.setChangedListener(text -> {
			final String newText = text.toUpperCase().replaceAll("[^0-9A-F]", "");
			if (!newText.equals(text)) {
				textFieldColor.setText(newText);
			}
		});
		textFieldZone.setMaxLength(DashboardScreen.MAX_COLOR_ZONE_LENGTH);
		textFieldZone.setText(String.valueOf(station.zone));
		textFieldZone.setChangedListener(text -> {
			final String newText = text.replaceAll("[^0-9-]", "");
			if (!newText.equals(text)) {
				textFieldZone.setText(newText);
			}
		});

		textFieldExitParentLetter.setMaxLength(1);
		textFieldExitParentLetter.setChangedListener(text -> {
			final String newText = text.toUpperCase().replaceAll("[^A-Z]", "");
			if (!newText.equals(text)) {
				textFieldExitParentLetter.setText(newText);
			}
			textFieldExitParentLetter.setSuggestion(newText.isEmpty() ? "A" : "");
		});
		textFieldExitParentNumber.setMaxLength(2);
		textFieldExitParentNumber.setChangedListener(text -> {
			final String newText = text.replaceAll("[^0-9]", "");
			if (!newText.equals(text)) {
				textFieldExitParentNumber.setText(newText);
			}
			textFieldExitParentNumber.setSuggestion(newText.isEmpty() ? "1" : "");
		});
		textFieldExitDestination.setMaxLength(DashboardScreen.MAX_NAME_LENGTH);

		exitParentList.x = 0;
		exitParentList.y = EXIT_PANELS_START;
		exitParentList.height = height - EXIT_PANELS_START - SQUARE_SIZE;
		exitParentList.width = width / 2;

		exitDestinationList.x = width / 2;
		exitDestinationList.y = EXIT_PANELS_START;
		exitDestinationList.height = height - EXIT_PANELS_START - SQUARE_SIZE;
		exitDestinationList.width = width / 2;

		exitParentList.init();
		exitDestinationList.init();

		addChild(textFieldName);
		addChild(textFieldColor);
		addChild(textFieldZone);
		addChild(textFieldExitParentLetter);
		addChild(textFieldExitParentNumber);
		addChild(textFieldExitDestination);
		addButton(buttonAddExitParent);
		addButton(buttonDoneExitParent);
		addButton(buttonAddExitDestination);
		addButton(buttonDoneExitDestination);

		changeEditingExit(null, -1);
	}

	@Override
	public void tick() {
		textFieldName.tick();
		textFieldColor.tick();
		textFieldZone.tick();
		textFieldExitParentLetter.tick();
		textFieldExitParentNumber.tick();
		textFieldExitDestination.tick();

		exitParentList.tick();
		exitDestinationList.tick();

		final List<DataConverter> exitParents = station.exits.keySet().stream().sorted().map(value -> {
			final List<String> destinations = station.exits.get(value);
			final String additional = destinations.size() > 1 ? "(+" + (destinations.size() - 1) + ")" : "";
			return new DataConverter(destinations.size() > 0 ? value + "|" + destinations.get(0) + "|" + additional : value, 0);
		}).collect(Collectors.toList());
		exitParentList.setData(exitParents, false, false, true, false, false, true);

		final List<DataConverter> exitDestinations = parentExists() ? station.exits.get(editingExit).stream().map(value -> new DataConverter(value, 0)).collect(Collectors.toList()) : new ArrayList<>();
		exitDestinationList.setData(exitDestinations, false, false, true, true, false, true);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			textFieldName.render(matrices, mouseX, mouseY, delta);
			textFieldColor.render(matrices, mouseX, mouseY, delta);
			textFieldZone.render(matrices, mouseX, mouseY, delta);
			textFieldExitParentLetter.render(matrices, mouseX, mouseY, delta);
			textFieldExitParentNumber.render(matrices, mouseX, mouseY, delta);
			textFieldExitDestination.render(matrices, mouseX, mouseY, delta);

			drawVerticalLine(matrices, width / 2, EXIT_PANELS_START - SQUARE_SIZE, height, ARGB_WHITE_TRANSLUCENT);

			exitParentList.render(matrices, textRenderer, mouseX, mouseY, delta);
			exitDestinationList.render(matrices, textRenderer, mouseX, mouseY, delta);

			drawCenteredText(matrices, textRenderer, stationNameText, width / 4, TEXT_PADDING, ARGB_WHITE);
			drawCenteredText(matrices, textRenderer, stationColorText, width / 8 * 5, TEXT_PADDING, ARGB_WHITE);
			drawCenteredText(matrices, textRenderer, stationZoneText, width / 8 * 7, TEXT_PADDING, ARGB_WHITE);
			drawCenteredText(matrices, textRenderer, exitParentsText, width / 4, EXIT_PANELS_START - SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			if (parentExists()) {
				drawCenteredText(matrices, textRenderer, exitDestinationsText, 3 * width / 4, EXIT_PANELS_START - SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			}

			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		exitParentList.mouseMoved(mouseX, mouseY);
		exitDestinationList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		exitParentList.mouseScrolled(mouseX, mouseY, amount);
		exitDestinationList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public void onClose() {
		super.onClose();
		if (client != null) {
			client.openScreen(dashboardScreen);
		}

		station.name = textFieldName.getText();
		station.color = DashboardScreen.colorStringToInt(textFieldColor.getText());
		try {
			station.zone = Integer.parseInt(textFieldZone.getText());
		} catch (Exception ignored) {
			station.zone = 0;
		}
		station.setZone(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_STATION, packet));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void changeEditingExit(String editingExit, int editingDestinationIndex) {
		this.editingExit = editingExit;
		this.editingDestinationIndex = parentExists() ? editingDestinationIndex : -1;

		if (editingExit != null) {
			textFieldExitParentLetter.setText(editingExit.toUpperCase().replaceAll("[^A-Z]", ""));
			textFieldExitParentNumber.setText(editingExit.replaceAll("[^0-9]", ""));
		}
		if (editingDestinationIndex >= 0 && editingDestinationIndex < station.exits.get(editingExit).size()) {
			textFieldExitDestination.setText(station.exits.get(editingExit).get(editingDestinationIndex));
		} else {
			textFieldExitDestination.setText("");
		}

		textFieldExitParentLetter.visible = editingExit != null;
		textFieldExitParentNumber.visible = editingExit != null;
		textFieldExitDestination.visible = editingDestinationIndex >= 0;
		buttonAddExitParent.visible = editingExit == null;
		buttonDoneExitParent.visible = editingExit != null;
		buttonAddExitDestination.visible = parentExists() && editingDestinationIndex < 0;
		buttonDoneExitDestination.visible = editingDestinationIndex >= 0;
		exitDestinationList.x = parentExists() ? width / 2 : width;
		exitParentList.height = height - EXIT_PANELS_START - (editingExit == null ? SQUARE_SIZE : SQUARE_SIZE * 2 + TEXT_FIELD_PADDING);
		exitDestinationList.height = height - EXIT_PANELS_START - (editingDestinationIndex >= 0 ? SQUARE_SIZE * 2 + TEXT_FIELD_PADDING : SQUARE_SIZE);
	}

	private void onDoneExitParent() {
		final String parentLetter = textFieldExitParentLetter.getText();
		final String parentNumber = textFieldExitParentNumber.getText();
		if (!parentLetter.isEmpty() && !parentNumber.isEmpty()) {
			try {
				final String exitParent = parentLetter + Integer.valueOf(parentNumber);
				station.setExitParent(editingExit, exitParent, packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_STATION, packet));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		changeEditingExit(null, -1);
	}

	private void onDoneExitDestination() {
		final String destination = textFieldExitDestination.getText();
		if (parentExists() && editingDestinationIndex >= 0 && !destination.isEmpty()) {
			final List<String> destinations = station.exits.get(editingExit);
			if (editingDestinationIndex < destinations.size()) {
				destinations.set(editingDestinationIndex, destination);
			} else {
				destinations.add(destination);
			}
			station.setExitDestinations(editingExit, packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_STATION, packet));
		}
		changeEditingExit(editingExit, -1);
	}

	private void onEditExitParent(NameColorDataBase data, int index) {
		changeEditingExit(formatExitName(data.name), -1);
	}

	private void onDeleteExitParent(NameColorDataBase data, int index) {
		station.deleteExitParent(formatExitName(data.name), packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_STATION, packet));
		changeEditingExit(null, -1);
	}

	private void onEditExitDestination(NameColorDataBase data, int index) {
		changeEditingExit(editingExit, index);
	}

	private void onSortExitDestination() {
		station.setExitDestinations(editingExit, packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_STATION, packet));
		changeEditingExit(editingExit, -1);
	}

	private void onDeleteExitDestination(NameColorDataBase data, int index) {
		if (parentExists()) {
			station.exits.get(editingExit).remove(data.name);
			station.setExitDestinations(editingExit, packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_STATION, packet));
		}
		changeEditingExit(editingExit, -1);
	}

	private List<String> getExitDestinationList() {
		return parentExists() ? station.exits.get(editingExit) : new ArrayList<>();
	}

	private boolean parentExists() {
		return editingExit != null && station.exits.containsKey(editingExit);
	}

	private static String formatExitName(String text) {
		return text.split("\\|")[0];
	}
}
